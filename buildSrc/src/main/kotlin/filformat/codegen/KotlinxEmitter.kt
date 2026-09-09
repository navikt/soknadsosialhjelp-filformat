package filformat.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
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
private val SERIAL_NAME = ClassName(SER_PKG, "SerialName")
private val SERIALIZATION_EXCEPTION = ClassName(SER_PKG, "SerializationException")
private val JSON_CONTENT_POLYMORPHIC_SERIALIZER = ClassName("$SER_PKG.json", "JsonContentPolymorphicSerializer")
private val JSON_ELEMENT = ClassName("$SER_PKG.json", "JsonElement")

private val M_JSON_OBJECT = com.squareup.kotlinpoet.MemberName("kotlinx.serialization.json", "jsonObject")
private val M_JSON_PRIMITIVE = com.squareup.kotlinpoet.MemberName("kotlinx.serialization.json", "jsonPrimitive")
private val M_CONTENT_OR_NULL = com.squareup.kotlinpoet.MemberName("kotlinx.serialization.json", "contentOrNull")

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
                val enumSpec = buildEnum(nestedClassName, spec)
                typeBuilder.addType(enumSpec)
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

    private fun buildEnum(className: ClassName, spec: EnumSpec): TypeSpec {
        val enumBuilder = TypeSpec.enumBuilder(className)
            .addAnnotation(SERIALIZABLE_ANN)
        for (value in spec.values) {
            enumBuilder.addEnumConstant(
                screamingSnakeCase(value),
                TypeSpec.anonymousClassBuilder()
                    .addAnnotation(AnnotationSpec.builder(SERIAL_NAME).addMember("%S", value).build())
                    .build(),
            )
        }
        return enumBuilder.build()
    }

    private fun emitTopLevelEnum(fqBase: String) {
        val topLevel = model.enums.getValue(fqBase)
        val className = NameMap.kotlinx(fqBase)
        val spec = EnumSpec(className.simpleName, topLevel.values, topLevel.default)
        FileSpec.builder(className).addType(buildEnum(className, spec)).build().writeTo(outputDir)
    }

    private fun emitUnion(obj: ObjectType) {
        val className = NameMap.kotlinx(obj.fqBase)
        val serializerClassName = ClassName(className.packageName, className.simpleName + "Serializer")

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

        emitUnionSerializer(className, serializerClassName, obj)
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
        whenBlock.addStatement("else -> throw %T(%S)", SERIALIZATION_EXCEPTION, "Ukjent type for ${className.simpleName}")
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

}
