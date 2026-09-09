package filformat.codegen

enum class PrimitiveKind { STRING, INTEGER, NUMBER, BOOLEAN }

sealed class TypeRef {
    data class Named(val fqBase: String) : TypeRef()
    data class NestedEnumRef(val ownerFqBase: String, val simpleName: String) : TypeRef()
    data class Primitive(val kind: PrimitiveKind) : TypeRef()
    data class ArrayOf(val element: TypeRef) : TypeRef()
}

data class EnumSpec(
    val simpleName: String,
    val values: List<String>,
    val default: String?,
)

data class Property(
    val name: String,
    val jsonName: String,
    val type: TypeRef,
    val required: Boolean,
    val docs: String?,
    val default: String?,
)

data class ObjectType(
    val fqBase: String,
    var superFqBase: String? = null,
    var discriminatorTag: String? = null,
    var oneOfSubtypes: MutableList<String> = mutableListOf(),
    var properties: MutableList<Property> = mutableListOf(),
    var nestedEnums: MutableList<EnumSpec> = mutableListOf(),
    var docs: String? = null,
)

data class TopLevelEnum(
    val fqBase: String,
    val values: List<String>,
    val default: String?,
    val docs: String?,
)

class SchemaModel {
    val objects = LinkedHashMap<String, ObjectType>()
    val enums = LinkedHashMap<String, TopLevelEnum>()

    fun isUnion(fqBase: String): Boolean = objects[fqBase]?.oneOfSubtypes?.isNotEmpty() == true

    /** Union base fqBase for a subtype, or null if [fqBase] isn't a union subtype. */
    val subtypeUnionBase: Map<String, String> by lazy {
        objects.values.flatMap { obj -> obj.oneOfSubtypes.map { it to obj.fqBase } }.toMap()
    }
}
