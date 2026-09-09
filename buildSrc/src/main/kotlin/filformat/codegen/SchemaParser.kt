package filformat.codegen

import java.io.File
import tools.jackson.databind.JsonNode
import tools.jackson.databind.json.JsonMapper

data class SchemaLocation(val file: File, val pointer: String)

class SchemaParser(private val root: File) {

    private val mapper = JsonMapper.builder().build()
    private val documentCache = HashMap<File, JsonNode>()
    private val nodeCache = HashMap<SchemaLocation, TypeRef>()
    val model = SchemaModel()

    fun parseAll() {
        root.walkTopDown()
            .filter { it.isFile && it.extension == "json" && it.name != "swagger.json" }
            .sortedBy { it.path }
            .forEach { file ->
                val doc = loadDocument(file)
                parseNode(SchemaLocation(file, ""), doc)
            }
    }

    private fun loadDocument(file: File): JsonNode =
        documentCache.getOrPut(file) { mapper.readTree(file) }

    private fun resolveLocation(base: File, ref: String): SchemaLocation =
        if (ref.startsWith("#/")) {
            SchemaLocation(base, ref.removePrefix("#"))
        } else if (ref.contains("#/")) {
            val (filePart, pointerPart) = ref.split("#/", limit = 2)
            val targetFile = base.parentFile.resolve(filePart).canonicalFile
            SchemaLocation(targetFile, "/$pointerPart")
        } else {
            val targetFile = base.parentFile.resolve(ref).canonicalFile
            SchemaLocation(targetFile, "")
        }

    private fun nodeAt(location: SchemaLocation): JsonNode {
        var node = loadDocument(location.file)
        if (location.pointer.isNotEmpty()) {
            for (segment in location.pointer.removePrefix("/").split('/')) {
                node = node.get(segment) ?: error("missing pointer segment '$segment' in ${location.file}${location.pointer}")
            }
        }
        return node
    }

    private fun parseRef(base: File, ref: String): TypeRef {
        val location = resolveLocation(base, ref)
        return parseNode(location, nodeAt(location))
    }

    private fun parseNode(location: SchemaLocation, node: JsonNode): TypeRef {
        nodeCache[location]?.let { return it }

        // ref-only node ({"$ref": "..."} as a property value, or a file's top-level
        // wrapper redirecting into its own "definitions")
        if (node.has("\$ref")) {
            val result = parseRef(location.file, node.get("\$ref").asText())
            nodeCache[location] = result
            return result
        }

        if (node.has("enum")) {
            val values = node.get("enum").toList().map { it.asText() }
            val default = node.get("default")?.asText()
            if (node.has("javaType")) {
                val fqBase = normalizeJavaType(node.get("javaType").asText())
                model.enums.getOrPut(fqBase) {
                    TopLevelEnum(fqBase, values, default, docs(node))
                }
                val result = TypeRef.Named(fqBase)
                nodeCache[location] = result
                return result
            }
            // inline enum: caller materializes it against the owning object + property name
            return InlineEnumMarker(values, default)
        }

        if (node.get("type")?.asText() == "array" || node.has("items")) {
            val itemsNode = node.get("items")
            val element = parseNode(SchemaLocation(location.file, location.pointer + "/items"), itemsNode)
            val result = TypeRef.ArrayOf(element)
            nodeCache[location] = result
            return result
        }

        if (node.has("javaType") || node.get("type")?.asText() == "object" || node.has("properties") || node.has("oneOf")) {
            return parseObject(location, node)
        }

        val kind = when (node.get("type")?.asText()) {
            "integer" -> PrimitiveKind.INTEGER
            "number" -> PrimitiveKind.NUMBER
            "boolean" -> PrimitiveKind.BOOLEAN
            else -> PrimitiveKind.STRING
        }
        val result = TypeRef.Primitive(kind)
        nodeCache[location] = result
        return result
    }

    // Sentinel used only within parseObject's property loop; never returned outward.
    private class InlineEnumMarker(val values: List<String>, val default: String?) : TypeRef()

    private fun parseObject(location: SchemaLocation, node: JsonNode): TypeRef {
        val javaTypeRaw = node.get("javaType")?.asText()
            ?: error("object node without javaType at ${location.file}${location.pointer}")
        val fqBase = normalizeJavaType(javaTypeRaw)

        model.objects[fqBase]?.let {
            val result = TypeRef.Named(fqBase)
            nodeCache[location] = result
            return result
        }

        val obj = ObjectType(fqBase, docs = docs(node))
        model.objects[fqBase] = obj
        val result = TypeRef.Named(fqBase)
        nodeCache[location] = result

        node.get("extends")?.let { ext ->
            val refStr = (ext.get("\$ref") ?: ext.get("ONLY_CODEGEN\$ref"))?.asText()
                ?: error("extends without \$ref at ${location.file}${location.pointer}")
            val superRef = parseRef(location.file, refStr)
            obj.superFqBase = (superRef as TypeRef.Named).fqBase
        }

        node.get("allOf")?.let { allOf ->
            for (branch in allOf) {
                val typeEnum = branch.get("properties")?.get("type")?.get("enum")
                if (typeEnum != null && typeEnum.size() == 1) {
                    obj.discriminatorTag = typeEnum[0].asText()
                }
                // pure {"$ref": ...} branches matching the extends target carry no
                // extra structure (validation only) and are otherwise ignored.
            }
        }

        node.get("oneOf")?.let { oneOf ->
            for (branch in oneOf) {
                val refStr = branch.get("\$ref")?.asText() ?: continue
                val subRef = parseRef(location.file, refStr) as TypeRef.Named
                obj.oneOfSubtypes.add(subRef.fqBase)
            }
        }

        val required = node.get("required")?.toList()?.map { it.asText() }.orEmpty().toSet()
        node.get("properties")?.properties()?.forEach { entry ->
            val propName = entry.key
            val propSchema = entry.value
            val propLocation = SchemaLocation(location.file, "${location.pointer}/properties/$propName")
            val parsed = parseNode(propLocation, propSchema)
            val type = if (parsed is InlineEnumMarker) {
                val enumSimpleName = propName.replaceFirstChar { it.uppercase() }
                obj.nestedEnums.add(EnumSpec(enumSimpleName, parsed.values, parsed.default))
                TypeRef.NestedEnumRef(fqBase, enumSimpleName)
            } else {
                parsed
            }
            obj.properties.add(
                Property(
                    name = propName,
                    jsonName = propName,
                    type = type,
                    required = propName in required,
                    docs = docs(propSchema),
                    default = propSchema.get("default")?.asText(),
                ),
            )
        }

        return result
    }

    private fun docs(node: JsonNode): String? {
        val title = node.get("title")?.asText()
        val description = node.get("description")?.asText()
        return listOfNotNull(title, description).joinToString("\n\n").ifBlank { null }
    }
}
