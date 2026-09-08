package filformat.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.STRING
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.WildcardTypeName
import com.squareup.kotlinpoet.asTypeName
import java.io.File

private const val SER_PKG = "kotlinx.serialization"
private val SERIALIZABLE_ANN = ClassName(SER_PKG, "Serializable")
private val KSERIALIZER = ClassName(SER_PKG, "KSerializer")
private val SERIALIZATION_EXCEPTION = ClassName(SER_PKG, "SerializationException")
private val SERIAL_DESCRIPTOR = ClassName("$SER_PKG.descriptors", "SerialDescriptor")
private val BUILD_CLASS_SERIAL_DESCRIPTOR = MemberName("$SER_PKG.descriptors", "buildClassSerialDescriptor")
private val DECODER = ClassName("$SER_PKG.encoding", "Decoder")
private val ENCODER = ClassName("$SER_PKG.encoding", "Encoder")
private val JSON_CONTENT_POLYMORPHIC_SERIALIZER = ClassName("$SER_PKG.json", "JsonContentPolymorphicSerializer")
private val JSON_DECODER = ClassName("$SER_PKG.json", "JsonDecoder")
private val JSON_ENCODER = ClassName("$SER_PKG.json", "JsonEncoder")
private val JSON_OBJECT = ClassName("$SER_PKG.json", "JsonObject")
private val JSON_ELEMENT = ClassName("$SER_PKG.json", "JsonElement")
private val UKJENT_TOLERANT_ENUM_SERIALIZER = ClassName("no.nav.sosialhjelp.filformat", "UkjentTolerantEnumSerializer")

private val M_JSON_OBJECT = MemberName("kotlinx.serialization.json", "jsonObject")
private val M_JSON_PRIMITIVE = MemberName("kotlinx.serialization.json", "jsonPrimitive")
private val M_CONTENT_OR_NULL = MemberName("kotlinx.serialization.json", "contentOrNull")
private val M_DECODE_FROM_JSON_ELEMENT = MemberName("kotlinx.serialization.json", "decodeFromJsonElement")

class KotlinxEmitter(private val model: SchemaModel, private val outputDir: File) {

    fun emit() {
        model.objects.values
            .filter { it.oneOfSubtypes.isEmpty() && it.fqBase !in model.subtypeUnionBase }
            .forEach { emitPlainObject(it) }
        model.objects.values.filter { it.oneOfSubtypes.isNotEmpty() }.forEach { emitUnion(it) }
        model.enums.keys.forEach { emitTopLevelEnum(it) }
    }

    private fun isDiscriminator(obj: ObjectType, property: Property) =
        model.isUnion(obj.fqBase) && property.name == "type"

    /**
     * Union subtypes live in the same kotlinx package as their union base (e.g. `SoknadsStatus`
     * next to `Hendelse`, not in a nested `.hendelse` package) to match the package layout the
     * hand-written KMP model already published.
     */
    private fun kotlinxSubtypeClassName(baseClassName: ClassName, subFqBase: String): ClassName =
        ClassName(baseClassName.packageName, simpleOf(subFqBase))

    private fun kotlinxOwnerClassName(fqBase: String): ClassName {
        val unionBase = model.subtypeUnionBase[fqBase] ?: return NameMap.kotlinx(fqBase)
        return kotlinxSubtypeClassName(NameMap.kotlinx(unionBase), fqBase)
    }

    private fun kotlinType(type: TypeRef, nullable: Boolean): TypeName = when (type) {
        is TypeRef.Primitive -> when (type.kind) {
            PrimitiveKind.STRING -> STRING
            PrimitiveKind.INTEGER -> Int::class.asTypeName()
            PrimitiveKind.NUMBER -> Double::class.asTypeName()
            PrimitiveKind.BOOLEAN -> Boolean::class.asTypeName()
        }.copy(nullable = nullable)
        is TypeRef.ArrayOf -> LIST.parameterizedBy(kotlinType(type.element, false)).copy(nullable = nullable)
        is TypeRef.Named -> kotlinxOwnerClassName(type.fqBase).copy(nullable = nullable)
        is TypeRef.NestedEnumRef -> kotlinxOwnerClassName(type.ownerFqBase).nestedClass(type.simpleName).copy(nullable = nullable)
        else -> error("unexpected type ref $type")
    }

    private fun defaultLiteral(property: Property, ownerClassName: ClassName): CodeBlock? {
        if (property.required) return null
        if (property.type is TypeRef.NestedEnumRef && property.default != null) {
            val enumClass = ownerClassName.nestedClass(property.type.simpleName)
            return CodeBlock.of("%T.%N", enumClass, screamingSnakeCase(property.default))
        }
        return CodeBlock.of("null")
    }

    /** Required properties first (stable order), then optional ones: valid Kotlin parameter order. */
    private fun orderedProperties(properties: List<Property>): List<Property> =
        properties.filter { it.required } + properties.filterNot { it.required }

    private fun addConstructorProperty(
        ctor: FunSpec.Builder,
        typeBuilder: TypeSpec.Builder,
        className: ClassName,
        property: Property,
        overrideModifier: Boolean,
    ) {
        val nullable = !property.required
        val propType = kotlinType(property.type, nullable)
        val paramSpec = ParameterSpec.builder(property.name, propType)
        defaultLiteral(property, className)?.let { paramSpec.defaultValue(it) }
        ctor.addParameter(paramSpec.build())
        val propSpec = PropertySpec.builder(property.name, propType).initializer(property.name)
        if (overrideModifier) propSpec.addModifiers(KModifier.OVERRIDE)
        property.docs?.let { propSpec.addKdoc(it.replace("%", "%%")) }
        typeBuilder.addProperty(propSpec.build())
    }

    private fun addOwnNestedEnums(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType, properties: List<Property>) {
        for (property in properties) {
            if (property.type is TypeRef.NestedEnumRef &&
                property.type.ownerFqBase == obj.fqBase &&
                !isDiscriminator(obj, property)
            ) {
                val spec = obj.nestedEnums.first { it.simpleName == property.type.simpleName }
                val nestedClassName = className.nestedClass(spec.simpleName)
                val (enumSpec, serializerSpec) = buildTolerantEnum(nestedClassName, spec)
                typeBuilder.addType(enumSpec)
                typeBuilder.addType(serializerSpec)
            }
        }
    }

    private fun emitPlainObject(obj: ObjectType) {
        val className = NameMap.kotlinx(obj.fqBase)
        val allProps = orderedProperties(allProperties(model, obj.fqBase))
        val typeBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(SERIALIZABLE_ANN)
        obj.docs?.let { typeBuilder.addKdoc(it.replace("%", "%%")) }

        val ctor = FunSpec.constructorBuilder()
        for (property in allProps) {
            addConstructorProperty(ctor, typeBuilder, className, property, overrideModifier = false)
        }
        typeBuilder.primaryConstructor(ctor.build())
        addOwnNestedEnums(typeBuilder, className, obj, allProps)

        FileSpec.builder(className).addType(typeBuilder.build()).build().writeTo(outputDir)
    }

    private fun buildTolerantEnum(className: ClassName, spec: EnumSpec): Pair<TypeSpec, TypeSpec> {
        val serializerName = className.simpleName + "Serializer"
        val serializerClassName = ClassName(className.packageName, *className.simpleNames.dropLast(1).toTypedArray(), serializerName)

        val enumBuilder = TypeSpec.enumBuilder(className)
            .addAnnotation(
                AnnotationSpec.builder(SERIALIZABLE_ANN).addMember("with = %T::class", serializerClassName).build(),
            )
            .primaryConstructor(FunSpec.constructorBuilder().addParameter("jsonValue", STRING).build())
            .addProperty(PropertySpec.builder("jsonValue", STRING).initializer("jsonValue").build())
        for (value in spec.values) {
            enumBuilder.addEnumConstant(
                screamingSnakeCase(value),
                TypeSpec.anonymousClassBuilder().addSuperclassConstructorParameter("%S", value).build(),
            )
        }
        enumBuilder.addEnumConstant(
            "UKJENT",
            TypeSpec.anonymousClassBuilder().addSuperclassConstructorParameter("%S", "UKJENT").build(),
        )

        val serializerSpec = TypeSpec.objectBuilder(serializerName)
            .superclass(UKJENT_TOLERANT_ENUM_SERIALIZER.parameterizedBy(className))
            .addSuperclassConstructorParameter("%S", className.simpleNames.joinToString("."))
            .addSuperclassConstructorParameter("%T.entries.toTypedArray()", className)
            .addSuperclassConstructorParameter("%T.UKJENT", className)
            .addSuperclassConstructorParameter("%T::jsonValue", className)
            .build()

        return enumBuilder.build() to serializerSpec
    }

    private fun emitTopLevelEnum(fqBase: String) {
        val topLevel = model.enums.getValue(fqBase)
        val className = NameMap.kotlinx(fqBase)
        val spec = EnumSpec(className.simpleName, topLevel.values, topLevel.default)
        val (enumType, serializerType) = buildTolerantEnum(className, spec)
        FileSpec.builder(className).addType(enumType).addType(serializerType).build().writeTo(outputDir)
    }

    private fun emitUnion(obj: ObjectType) {
        val className = NameMap.kotlinx(obj.fqBase)
        val serializerClassName = ClassName(className.packageName, className.simpleName + "Serializer")
        val ukjentClassName = ClassName(className.packageName, "Ukjent${className.simpleName}")
        val ukjentSerializerClassName = ClassName(className.packageName, "Ukjent${className.simpleName}Serializer")

        val commonProps = obj.properties

        val interfaceBuilder = TypeSpec.interfaceBuilder(className)
            .addModifiers(KModifier.SEALED)
            .addAnnotation(
                AnnotationSpec.builder(SERIALIZABLE_ANN).addMember("with = %T::class", serializerClassName).build(),
            )
        obj.docs?.let { interfaceBuilder.addKdoc(it.replace("%", "%%")) }
        for (property in commonProps) {
            val type = if (isDiscriminator(obj, property)) STRING else kotlinType(property.type, !property.required)
            interfaceBuilder.addProperty(PropertySpec.builder(property.name, type).build())
        }
        FileSpec.builder(className).addType(interfaceBuilder.build()).build().writeTo(outputDir)

        for (subFq in obj.oneOfSubtypes) {
            emitUnionSubtype(model.objects.getValue(subFq), obj, className)
        }

        emitUnionSerializer(className, serializerClassName, ukjentClassName, obj)
        emitUkjentFallback(ukjentClassName, ukjentSerializerClassName, className, obj)
    }

    private fun emitUnionSubtype(subObj: ObjectType, baseObj: ObjectType, baseClassName: ClassName) {
        val className = kotlinxSubtypeClassName(baseClassName, subObj.fqBase)
        val discriminatorProp = baseObj.properties.first { isDiscriminator(baseObj, it) }
        val overrideNames = baseObj.properties.map { it.name }.toSet() - discriminatorProp.name
        val inheritedNonDiscriminator = baseObj.properties.filterNot { isDiscriminator(baseObj, it) }
        val ownProps = orderedProperties(subObj.properties + inheritedNonDiscriminator)

        val typeBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.DATA)
            .addAnnotation(SERIALIZABLE_ANN)
            .addSuperinterface(baseClassName)
        subObj.docs?.let { typeBuilder.addKdoc(it.replace("%", "%%")) }

        val ctor = FunSpec.constructorBuilder()
        for (property in ownProps) {
            addConstructorProperty(ctor, typeBuilder, className, property, overrideModifier = property.name in overrideNames)
        }
        ctor.addParameter(
            ParameterSpec.builder(discriminatorProp.name, STRING)
                .defaultValue("%S", subObj.discriminatorTag)
                .build(),
        )
        typeBuilder.addProperty(
            PropertySpec.builder(discriminatorProp.name, STRING)
                .addModifiers(KModifier.OVERRIDE)
                .initializer(discriminatorProp.name)
                .build(),
        )
        typeBuilder.primaryConstructor(ctor.build())
        addOwnNestedEnums(typeBuilder, className, subObj, subObj.properties)

        FileSpec.builder(className).addType(typeBuilder.build()).build().writeTo(outputDir)
    }

    private fun emitUnionSerializer(
        className: ClassName,
        serializerClassName: ClassName,
        ukjentClassName: ClassName,
        obj: ObjectType,
    ) {
        val whenBlock = CodeBlock.builder().beginControlFlow(
            "return when (element.%M[%S]?.%M?.%M)",
            M_JSON_OBJECT,
            "type",
            M_JSON_PRIMITIVE,
            M_CONTENT_OR_NULL,
        )
        for (subFq in obj.oneOfSubtypes) {
            val subObj = model.objects.getValue(subFq)
            whenBlock.addStatement("%S -> %T.serializer()", subObj.discriminatorTag, kotlinxSubtypeClassName(className, subFq))
        }
        whenBlock.addStatement("else -> %T.serializer()", ukjentClassName)
        whenBlock.endControlFlow()

        val serializerType = TypeSpec.objectBuilder(serializerClassName)
            .superclass(JSON_CONTENT_POLYMORPHIC_SERIALIZER.parameterizedBy(className))
            .addSuperclassConstructorParameter("%T::class", className)
            .addFunction(
                FunSpec.builder("selectDeserializer")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("element", JSON_ELEMENT)
                    .returns(KSERIALIZER.parameterizedBy(WildcardTypeName.producerOf(className)))
                    .addCode(whenBlock.build())
                    .build(),
            )
            .build()
        FileSpec.builder(serializerClassName).addType(serializerType).build().writeTo(outputDir)
    }

    private fun emitUkjentFallback(
        ukjentClassName: ClassName,
        ukjentSerializerClassName: ClassName,
        baseClassName: ClassName,
        obj: ObjectType,
    ) {
        val commonProps = obj.properties
        val dataBuilder = TypeSpec.classBuilder(ukjentClassName)
            .addModifiers(KModifier.DATA)
            .addAnnotation(
                AnnotationSpec.builder(SERIALIZABLE_ANN).addMember("with = %T::class", ukjentSerializerClassName).build(),
            )
            .addSuperinterface(baseClassName)
        val ctor = FunSpec.constructorBuilder()
        for (property in commonProps) {
            val isDiscriminatorProp = isDiscriminator(obj, property)
            val propType = if (isDiscriminatorProp) STRING else kotlinType(property.type, !property.required)
            ctor.addParameter(property.name, propType)
            dataBuilder.addProperty(
                PropertySpec.builder(property.name, propType).addModifiers(KModifier.OVERRIDE).initializer(property.name).build(),
            )
        }
        ctor.addParameter("raw", JSON_OBJECT)
        dataBuilder.addProperty(PropertySpec.builder("raw", JSON_OBJECT).initializer("raw").build())
        dataBuilder.primaryConstructor(ctor.build())
        FileSpec.builder(ukjentClassName).addType(dataBuilder.build()).build().writeTo(outputDir)

        val deserializeBody = CodeBlock.builder()
            .addStatement("val obj = (decoder as %T).decodeJsonElement().%M", JSON_DECODER, M_JSON_OBJECT)
        for (property in commonProps) {
            val isDiscriminatorProp = isDiscriminator(obj, property)
            when {
                isDiscriminatorProp || (property.type is TypeRef.Primitive && property.type.kind == PrimitiveKind.STRING) -> {
                    val default = if (property.required) {
                        CodeBlock.of("throw %T(%S)", SERIALIZATION_EXCEPTION, "${ukjentClassName.simpleName} mangler pakrevd felt '${property.name}'")
                    } else {
                        CodeBlock.of("null")
                    }
                    deserializeBody.addStatement(
                        "val %N = obj[%S]?.%M?.%M ?: %L",
                        property.name,
                        property.name,
                        M_JSON_PRIMITIVE,
                        M_CONTENT_OR_NULL,
                        default,
                    )
                }
                property.required -> deserializeBody.addStatement(
                    "val %N = (decoder as %T).json.%M<%T>(obj.getValue(%S))",
                    property.name,
                    JSON_DECODER,
                    M_DECODE_FROM_JSON_ELEMENT,
                    kotlinType(property.type, false),
                    property.name,
                )
                else -> deserializeBody.addStatement(
                    "val %N = obj[%S]?.let { (decoder as %T).json.%M<%T>(it) }",
                    property.name,
                    property.name,
                    JSON_DECODER,
                    M_DECODE_FROM_JSON_ELEMENT,
                    kotlinType(property.type, false),
                )
            }
        }
        val ctorFormat = "%T(" + "%N = %N, ".repeat(commonProps.size) + "raw = obj)"
        val ctorArgs = buildList {
            add(ukjentClassName)
            commonProps.forEach { add(it.name); add(it.name) }
        }
        deserializeBody.addStatement("return $ctorFormat", *ctorArgs.toTypedArray())

        val serializerType = TypeSpec.objectBuilder(ukjentSerializerClassName)
            .addSuperinterface(KSERIALIZER.parameterizedBy(ukjentClassName))
            .addProperty(
                PropertySpec.builder("descriptor", SERIAL_DESCRIPTOR)
                    .addModifiers(KModifier.OVERRIDE)
                    .initializer("%M(%S)", BUILD_CLASS_SERIAL_DESCRIPTOR, ukjentClassName.canonicalName)
                    .build(),
            )
            .addFunction(
                FunSpec.builder("deserialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("decoder", DECODER)
                    .returns(ukjentClassName)
                    .addCode(deserializeBody.build())
                    .build(),
            )
            .addFunction(
                FunSpec.builder("serialize")
                    .addModifiers(KModifier.OVERRIDE)
                    .addParameter("encoder", ENCODER)
                    .addParameter("value", ukjentClassName)
                    .addStatement("(encoder as %T).encodeJsonElement(value.raw)", JSON_ENCODER)
                    .build(),
            )
            .build()
        FileSpec.builder(ukjentSerializerClassName).addType(serializerType).build().writeTo(outputDir)
    }
}
