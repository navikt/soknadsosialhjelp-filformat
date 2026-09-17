package filformat.codegen

import com.squareup.kotlinpoet.ANY
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
import com.squareup.kotlinpoet.asTypeName
import java.io.File

private val JSON_PROPERTY = ClassName("com.fasterxml.jackson.annotation", "JsonProperty")
private val JSON_PROPERTY_DESCRIPTION = ClassName("com.fasterxml.jackson.annotation", "JsonPropertyDescription")
private val JSON_PROPERTY_ORDER = ClassName("com.fasterxml.jackson.annotation", "JsonPropertyOrder")
private val JSON_INCLUDE = ClassName("com.fasterxml.jackson.annotation", "JsonInclude")
private val JSON_IGNORE_PROPERTIES = ClassName("com.fasterxml.jackson.annotation", "JsonIgnoreProperties")
private val JSON_VALUE = ClassName("com.fasterxml.jackson.annotation", "JsonValue")
private val JSON_CREATOR = ClassName("com.fasterxml.jackson.annotation", "JsonCreator")
private val JSON_TYPE_INFO = ClassName("com.fasterxml.jackson.annotation", "JsonTypeInfo")
private val JSON_SUB_TYPES = ClassName("com.fasterxml.jackson.annotation", "JsonSubTypes")

class JacksonEmitter(private val model: SchemaModel, private val outputDir: File) {

    fun emit() {
        model.objects.keys.forEach { emitObject(it) }
        model.enums.keys.forEach { emitEnum(it) }
    }

    private fun kotlinType(type: TypeRef, nullable: Boolean): TypeName = when (type) {
        is TypeRef.Primitive -> when (type.kind) {
            PrimitiveKind.STRING -> STRING
            PrimitiveKind.INTEGER -> Int::class.asTypeName()
            PrimitiveKind.NUMBER -> Double::class.asTypeName()
            PrimitiveKind.BOOLEAN -> Boolean::class.asTypeName()
        }.copy(nullable = nullable)
        is TypeRef.ArrayOf -> LIST.parameterizedBy(kotlinType(type.element, false)).copy(nullable = nullable)
        is TypeRef.Named -> NameMap.jackson(type.fqBase).copy(nullable = nullable)
        is TypeRef.NestedEnumRef -> NameMap.jackson(type.ownerFqBase)
            .nestedClass(type.simpleName).copy(nullable = nullable)
        else -> error("unexpected type ref $type")
    }

    private fun isDiscriminator(obj: ObjectType, property: Property): Boolean =
        model.isUnion(obj.fqBase) && property.name == "type"

    private fun isInheritedProperty(obj: ObjectType, property: Property): Boolean =
        obj.superFqBase != null && property !in obj.properties

    private fun propertyIsNullable(property: Property): Boolean =
        !property.required && (property.type !is TypeRef.ArrayOf || property.explicitNullDefault)

    private fun orderedProperties(properties: List<Property>): List<Property> =
        properties.filter { it.required } + properties.filterNot { it.required }

    private fun emitObject(fqBase: String) {
        val obj = model.objects.getValue(fqBase)
        val className = NameMap.jackson(fqBase)
        val isUnion = obj.oneOfSubtypes.isNotEmpty()
        val typeBuilder = TypeSpec.classBuilder(className)
            .addAnnotation(AnnotationSpec.builder(JSON_IGNORE_PROPERTIES).addMember("ignoreUnknown = true").build())
            .addAnnotation(
                AnnotationSpec.builder(JSON_INCLUDE)
                    .addMember("%T.Include.NON_NULL", JSON_INCLUDE)
                    .build(),
            )

        obj.docs?.let { typeBuilder.addKdoc(it.replace("%", "%%")) }
        if (obj.properties.isNotEmpty()) {
            typeBuilder.addAnnotation(
                AnnotationSpec.builder(JSON_PROPERTY_ORDER)
                    .addMember(obj.properties.joinToString(", ") { "%S" }, *obj.properties.map { it.jsonName }.toTypedArray())
                    .build(),
            )
        }

        if (isUnion) {
            emitUnionBase(typeBuilder, className, obj)
        } else if (model.objects.values.any { it.superFqBase == obj.fqBase }) {
            emitOpenClass(typeBuilder, className, obj)
        } else {
            emitDataClass(typeBuilder, className, obj)
        }

        FileSpec.builder(className)
            .addType(typeBuilder.build())
            .build()
            .writeTo(outputDir)
    }

    private fun emitUnionBase(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType) {
        typeBuilder.addModifiers(KModifier.ABSTRACT)
        typeBuilder.addAnnotation(
            AnnotationSpec.builder(JSON_TYPE_INFO)
                .addMember("use = %T.Id.NAME", JSON_TYPE_INFO)
                .addMember("include = %T.As.EXISTING_PROPERTY", JSON_TYPE_INFO)
                .addMember("property = %S", "type")
                .addMember("visible = false")
                .build(),
        )
        val subTypes = CodeBlock.builder().add("value = [")
        obj.oneOfSubtypes.forEachIndexed { index, subFq ->
            if (index > 0) subTypes.add(", ")
            val subObj = model.objects.getValue(subFq)
            subTypes.add("%T(value = %T::class, name = %S)", JSON_SUB_TYPES.nestedClass("Type"), NameMap.jackson(subFq), subObj.discriminatorTag)
        }
        subTypes.add("]")
        typeBuilder.addAnnotation(
            AnnotationSpec.builder(JSON_SUB_TYPES)
                .addMember(subTypes.build())
                .build(),
        )
        obj.properties.forEach { property ->
            typeBuilder.addProperty(
                PropertySpec.builder(property.name, kotlinType(property.type, propertyIsNullable(property)))
                    .addModifiers(KModifier.ABSTRACT)
                    .addAnnotation(jsonPropertyAnnotation(property))
                    .build(),
            )
        }
        addOwnNestedEnums(typeBuilder, className, obj)
    }

    private fun emitDataClass(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType) {
        typeBuilder.addModifiers(KModifier.DATA)
        obj.superFqBase?.let { typeBuilder.superclass(NameMap.jackson(it)) }

        val superFqBase = obj.superFqBase
        val constructor = FunSpec.constructorBuilder()
            .addAnnotation(
                AnnotationSpec.builder(JSON_CREATOR)
                    .addMember("mode = %T.Mode.PROPERTIES", JSON_CREATOR)
                    .build(),
            )
        for (property in orderedProperties(allProperties(model, obj.fqBase))) {
            if (isInheritedProperty(obj, property) && property.name == "type") {
                continue
            }
            val parameter = ParameterSpec.builder(property.name, kotlinType(property.type, propertyIsNullable(property)))
                .addAnnotation(jsonCreatorParameterAnnotation(property))
            defaultInitializer(property)?.let { parameter.defaultValue(it) }
            constructor.addParameter(parameter.build())
            val propertyBuilder = PropertySpec.builder(property.name, kotlinType(property.type, propertyIsNullable(property)))
                .initializer(property.name)
                .addAnnotation(jsonPropertyAnnotation(property))
            if (isInheritedProperty(obj, property)) propertyBuilder.addModifiers(KModifier.OVERRIDE)
            typeBuilder.addProperty(
                propertyBuilder.build(),
            )
        }
        typeBuilder.primaryConstructor(constructor.build())

        if (superFqBase != null) {
            val superObj = model.objects.getValue(superFqBase)
            if (model.isUnion(superFqBase)) {
                val discriminator = superObj.properties.first { it.name == "type" }
                typeBuilder.addProperty(
                    PropertySpec.builder(discriminator.name, kotlinType(discriminator.type, false))
                        .addModifiers(KModifier.OVERRIDE)
                        .getter(
                            FunSpec.getterBuilder()
                                .addStatement("return %T.%N", NameMap.jackson(superObj.fqBase).nestedClass("Type"), screamingSnakeCase(obj.discriminatorTag!!))
                                .build(),
                        )
                        .build(),
                )
            } else {
                superObj.properties.forEach { property ->
                    typeBuilder.addSuperclassConstructorParameter("%N", property.name)
                }
            }
        }
        addOwnNestedEnums(typeBuilder, className, obj)
    }

    private fun emitOpenClass(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType) {
        typeBuilder.addModifiers(KModifier.OPEN)
        val constructor = FunSpec.constructorBuilder()
            .addAnnotation(
                AnnotationSpec.builder(JSON_CREATOR)
                    .addMember("mode = %T.Mode.PROPERTIES", JSON_CREATOR)
                    .build(),
            )
        for (property in orderedProperties(obj.properties)) {
            val parameter = ParameterSpec.builder(property.name, kotlinType(property.type, propertyIsNullable(property)))
                .addAnnotation(jsonCreatorParameterAnnotation(property))
            defaultInitializer(property)?.let { parameter.defaultValue(it) }
            constructor.addParameter(parameter.build())
            typeBuilder.addProperty(
                PropertySpec.builder(property.name, kotlinType(property.type, propertyIsNullable(property)))
                    .addModifiers(KModifier.OPEN)
                    .initializer(property.name)
                    .addAnnotation(jsonPropertyAnnotation(property))
                    .build(),
            )
        }
        typeBuilder.primaryConstructor(constructor.build())
        addOwnNestedEnums(typeBuilder, className, obj)
    }

    private fun jsonPropertyAnnotation(property: Property): AnnotationSpec =
        AnnotationSpec.builder(JSON_PROPERTY)
            .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
            .addMember("%S", property.jsonName)
            .build()

    private fun jsonCreatorParameterAnnotation(property: Property): AnnotationSpec =
        AnnotationSpec.builder(JSON_PROPERTY)
            .addMember("%S", property.jsonName)
            .build()

    private fun addOwnNestedEnums(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType) {
        obj.properties.filter { it.type is TypeRef.NestedEnumRef }.forEach { property ->
            val type = property.type as TypeRef.NestedEnumRef
            if (type.ownerFqBase == obj.fqBase) {
                val spec = obj.nestedEnums.firstOrNull { it.simpleName == type.simpleName }
                    ?: error("Missing nested enum ${type.simpleName} in ${obj.fqBase}")
                typeBuilder.addType(buildJacksonEnum(className.nestedClass(spec.simpleName), spec))
            }
        }
    }

    private fun defaultInitializer(property: Property): CodeBlock? {
        if (property.required) return null
        if (property.default != null && property.type is TypeRef.NestedEnumRef) {
            val enumClass = NameMap.jackson(property.type.ownerFqBase).nestedClass(property.type.simpleName)
            return CodeBlock.of("%T.fromValue(%S)", enumClass, property.default)
        }
        if (property.type is TypeRef.ArrayOf && !property.explicitNullDefault) return CodeBlock.of("emptyList()")
        return CodeBlock.of("null")
    }

    private fun buildJacksonEnum(className: ClassName, spec: EnumSpec): TypeSpec {
        val builder = TypeSpec.enumBuilder(className)
            .primaryConstructor(FunSpec.constructorBuilder().addParameter("value", STRING).build())
            .addProperty(
                PropertySpec.builder("value", STRING)
                    .initializer("value")
                    .addAnnotation(JSON_VALUE)
                    .build(),
            )
        for (value in spec.values) {
            builder.addEnumConstant(
                screamingSnakeCase(value),
                TypeSpec.anonymousClassBuilder().addSuperclassConstructorParameter("%S", value).build(),
            )
        }
        builder.addType(
            TypeSpec.companionObjectBuilder()
                .addFunction(
                    FunSpec.builder("fromValue")
                        .addAnnotation(JSON_CREATOR)
                        .addParameter("value", STRING)
                        .returns(className)
                        .addStatement("return entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)")
                        .build(),
                )
                .build(),
        )
        return builder.build()
    }

    private fun emitEnum(fqBase: String) {
        val topLevel = model.enums.getValue(fqBase)
        val className = NameMap.jackson(fqBase)
        FileSpec.builder(className)
            .addType(buildJacksonEnum(className, EnumSpec(className.simpleName, topLevel.values, topLevel.default)))
            .build()
            .writeTo(outputDir)
    }
}
