package filformat.codegen

import com.squareup.kotlinpoet.ANY
import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.LIST
import com.squareup.kotlinpoet.MUTABLE_MAP
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
private val JSON_ANY_GETTER = ClassName("com.fasterxml.jackson.annotation", "JsonAnyGetter")
private val JSON_ANY_SETTER = ClassName("com.fasterxml.jackson.annotation", "JsonAnySetter")
private val JSON_IGNORE = ClassName("com.fasterxml.jackson.annotation", "JsonIgnore")
private val JSON_VALUE = ClassName("com.fasterxml.jackson.annotation", "JsonValue")
private val JSON_CREATOR = ClassName("com.fasterxml.jackson.annotation", "JsonCreator")
private val JSON_TYPE_INFO = ClassName("com.fasterxml.jackson.annotation", "JsonTypeInfo")
private val JSON_SUB_TYPES = ClassName("com.fasterxml.jackson.annotation", "JsonSubTypes")
private val SERIALIZABLE = ClassName("java.io", "Serializable")
private val LINKED_HASH_MAP = ClassName("kotlin.collections", "LinkedHashMap")

class JacksonEmitter(private val model: SchemaModel, private val outputDir: File) {

    fun emit() {
        model.objects.keys.forEach { emitObject(it) }
        model.enums.keys.forEach { emitEnum(it) }
    }

    private fun kotlinType(type: TypeRef): TypeName = when (type) {
        is TypeRef.Primitive -> when (type.kind) {
            PrimitiveKind.STRING -> STRING
            PrimitiveKind.INTEGER -> Int::class.asTypeName()
            PrimitiveKind.NUMBER -> Double::class.asTypeName()
            PrimitiveKind.BOOLEAN -> Boolean::class.asTypeName()
        }.copy(nullable = true)
        is TypeRef.ArrayOf -> LIST.parameterizedBy(kotlinType(type.element).copy(nullable = false))
            .copy(nullable = true)
        is TypeRef.Named -> NameMap.jackson(type.fqBase).copy(nullable = true)
        is TypeRef.NestedEnumRef -> NameMap.jackson(type.ownerFqBase)
            .nestedClass(type.simpleName).copy(nullable = true)
        else -> error("unexpected type ref $type")
    }

    private fun emitObject(fqBase: String) {
        val obj = model.objects.getValue(fqBase)
        val className = NameMap.jackson(fqBase)
        val typeBuilder = TypeSpec.classBuilder(className)
            .addModifiers(KModifier.OPEN)
            .addSuperinterface(SERIALIZABLE)

        obj.docs?.let { typeBuilder.addKdoc(it.replace("%", "%%")) }

        obj.superFqBase?.let { typeBuilder.superclass(NameMap.jackson(it)) }

        typeBuilder.addAnnotation(
            AnnotationSpec.builder(JSON_INCLUDE)
                .addMember("%T.Include.NON_NULL", JSON_INCLUDE)
                .build(),
        )
        if (obj.properties.isNotEmpty()) {
            typeBuilder.addAnnotation(
                AnnotationSpec.builder(JSON_PROPERTY_ORDER)
                    .addMember(obj.properties.joinToString(", ") { "%S" }, *obj.properties.map { it.jsonName }.toTypedArray())
                    .build(),
            )
        }

        if (obj.oneOfSubtypes.isNotEmpty()) {
            typeBuilder.addAnnotation(
                AnnotationSpec.builder(JSON_TYPE_INFO)
                    .addMember("use = %T.Id.NAME", JSON_TYPE_INFO)
                    .addMember("include = %T.As.EXISTING_PROPERTY", JSON_TYPE_INFO)
                    .addMember("property = %S", "type")
                    .addMember("visible = true")
                    .build(),
            )
            val subTypesBlock = CodeBlock.builder().add("value = [")
            obj.oneOfSubtypes.forEachIndexed { index, subFq ->
                if (index > 0) subTypesBlock.add(", ")
                val subObj = model.objects.getValue(subFq)
                subTypesBlock.add(
                    "%T(value = %T::class, name = %S)",
                    JSON_SUB_TYPES.nestedClass("Type"),
                    NameMap.jackson(subFq),
                    subObj.discriminatorTag,
                )
            }
            subTypesBlock.add("]")
            typeBuilder.addAnnotation(
                AnnotationSpec.builder(JSON_SUB_TYPES).addMember(subTypesBlock.build()).build(),
            )
        }

        for (property in obj.properties) {
            val propType = kotlinType(property.type)
            val propBuilder = PropertySpec.builder(property.name, propType)
                .mutable(true)
                .initializer(defaultInitializer(property, propType))
                .addAnnotation(
                    AnnotationSpec.builder(JSON_PROPERTY).useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                        .addMember("%S", property.jsonName).build(),
                )
                .addAnnotation(
                    AnnotationSpec.builder(JSON_PROPERTY).useSiteTarget(AnnotationSpec.UseSiteTarget.SET)
                        .addMember("%S", property.jsonName).build(),
                )
            property.docs?.let {
                propBuilder.addAnnotation(
                    AnnotationSpec.builder(JSON_PROPERTY_DESCRIPTION)
                        .useSiteTarget(AnnotationSpec.UseSiteTarget.GET)
                        .addMember("%S", it.replace("\n\n", " "))
                        .build(),
                )
            }
            typeBuilder.addProperty(propBuilder.build())

            if (property.type is TypeRef.NestedEnumRef && property.type.ownerFqBase == fqBase) {
                val spec = obj.nestedEnums.first { it.simpleName == property.type.simpleName }
                typeBuilder.addType(buildJacksonEnum(className.nestedClass(spec.simpleName), spec))
            }
        }

        val additionalPropsType = MUTABLE_MAP.parameterizedBy(STRING, ANY.copy(nullable = true))
        typeBuilder.addProperty(
            PropertySpec.builder("additionalProperties", additionalPropsType)
                .addModifiers(KModifier.PRIVATE)
                .initializer("%T()", LINKED_HASH_MAP)
                .addAnnotation(JSON_IGNORE)
                .build(),
        )
        val hasSuper = obj.superFqBase != null
        val inheritanceModifier = if (hasSuper) KModifier.OVERRIDE else KModifier.OPEN

        typeBuilder.addFunction(
            FunSpec.builder("getAdditionalProperties")
                .addModifiers(inheritanceModifier)
                .addAnnotation(JSON_ANY_GETTER)
                .returns(additionalPropsType)
                .addStatement("return additionalProperties")
                .build(),
        )
        typeBuilder.addFunction(
            FunSpec.builder("setAdditionalProperty")
                .addModifiers(inheritanceModifier)
                .addAnnotation(JSON_ANY_SETTER)
                .addParameter("name", STRING)
                .addParameter("value", ANY.copy(nullable = true))
                .addStatement("additionalProperties[name] = value")
                .build(),
        )

        val ownPropertyNames = obj.properties.map { it.name }.toSet()
        for (property in allProperties(model, fqBase)) {
            val propType = kotlinType(property.type)
            val modifier = if (property.name in ownPropertyNames) KModifier.OPEN else KModifier.OVERRIDE
            typeBuilder.addFunction(
                FunSpec.builder("with${property.name.replaceFirstChar { it.uppercase() }}")
                    .addModifiers(modifier)
                    .addParameter(property.name, propType)
                    .returns(className)
                    .addStatement("this.%N = %N", property.name, property.name)
                    .addStatement("return this")
                    .build(),
            )
        }
        typeBuilder.addFunction(
            FunSpec.builder("withAdditionalProperty")
                .addModifiers(inheritanceModifier)
                .addParameter("name", STRING)
                .addParameter("value", ANY.copy(nullable = true))
                .returns(className)
                .addStatement("additionalProperties[name] = value")
                .addStatement("return this")
                .build(),
        )

        addEqualsHashCodeToString(typeBuilder, className, obj)

        FileSpec.builder(className)
            .addType(typeBuilder.build())
            .build()
            .writeTo(outputDir)
    }

    private fun addEqualsHashCodeToString(typeBuilder: TypeSpec.Builder, className: ClassName, obj: ObjectType) {
        val ownNames = obj.properties.map { it.name } + "additionalProperties"
        val hasSuper = obj.superFqBase != null

        val equalsBody = CodeBlock.builder()
            .addStatement("if (other === this) return true")
            .addStatement("if (other !is %T) return false", className)
            .add("return ")
        equalsBody.add(ownNames.joinToString(" &&\n    ") { "$it == other.$it" })
        if (hasSuper) equalsBody.add(" &&\n    super.equals(other)")
        typeBuilder.addFunction(
            FunSpec.builder("equals")
                .addModifiers(KModifier.OVERRIDE)
                .addParameter("other", ANY.copy(nullable = true))
                .returns(Boolean::class.asTypeName())
                .addCode(equalsBody.build())
                .build(),
        )

        val hashCodeBody = CodeBlock.builder().addStatement("var result = 1")
        for (property in obj.properties) {
            hashCodeBody.addStatement("result = result * 31 + (%N?.hashCode() ?: 0)", property.name)
        }
        hashCodeBody.addStatement("result = result * 31 + additionalProperties.hashCode()")
        if (hasSuper) hashCodeBody.addStatement("result = result * 31 + super.hashCode()")
        hashCodeBody.addStatement("return result")
        typeBuilder.addFunction(
            FunSpec.builder("hashCode")
                .addModifiers(KModifier.OVERRIDE)
                .returns(Int::class.asTypeName())
                .addCode(hashCodeBody.build())
                .build(),
        )

        val toStringParts = ownNames.joinToString(", ") { "$it=\$$it" }
        val toStringExpr = if (hasSuper) {
            "\"${className.simpleName}($toStringParts, super=\${super.toString()})\""
        } else {
            "\"${className.simpleName}($toStringParts)\""
        }
        typeBuilder.addFunction(
            FunSpec.builder("toString")
                .addModifiers(KModifier.OVERRIDE)
                .returns(STRING)
                .addStatement("return %L", toStringExpr)
                .build(),
        )
    }

    private fun defaultInitializer(property: Property, propType: TypeName): CodeBlock {
        if (property.default != null && property.type is TypeRef.NestedEnumRef) {
            val enumClass = NameMap.jackson(property.type.ownerFqBase).nestedClass(property.type.simpleName)
            return CodeBlock.of("%T.fromValue(%S)", enumClass, property.default)
        }
        if (property.type is TypeRef.ArrayOf) {
            // jsonschema2pojo initializes array-typed fields eagerly (initializeCollections=true)
            return CodeBlock.of("mutableListOf()")
        }
        return CodeBlock.of("null")
    }

    private fun buildJacksonEnum(className: ClassName, spec: EnumSpec): TypeSpec {
        val builder = TypeSpec.enumBuilder(className)
            .primaryConstructor(
                FunSpec.constructorBuilder().addParameter("value", STRING).build(),
            )
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
                        .addStatement(
                            "return entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)",
                        )
                        .build(),
                )
                .build(),
        )
        return builder.build()
    }

    private fun emitEnum(fqBase: String) {
        val topLevel = model.enums.getValue(fqBase)
        val className = NameMap.jackson(fqBase)
        val spec = EnumSpec(className.simpleName, topLevel.values, topLevel.default)
        val enumType = buildJacksonEnum(className, spec)
        FileSpec.builder(className).addType(enumType).build().writeTo(outputDir)
    }
}
