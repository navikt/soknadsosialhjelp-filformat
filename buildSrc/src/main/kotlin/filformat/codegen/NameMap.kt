package filformat.codegen

import com.squareup.kotlinpoet.ClassName

object NameMap {
    fun jackson(fqBase: String): ClassName {
        val (pkg, simple) = jacksonFqName(fqBase)
        return ClassName(pkg, simple)
    }

    fun kotlinx(fqBase: String): ClassName {
        val (pkg, simple) = kotlinxFqName(fqBase)
        return ClassName(pkg, simple)
    }
}

fun screamingSnakeCase(value: String): String =
    value.replace(Regex("(?<=[a-z0-9])(?=[A-Z])"), "_").uppercase()

/** Own properties first (declaration order), then inherited properties base-most last. */
fun allProperties(model: SchemaModel, fqBase: String): List<Property> {
    val obj = model.objects.getValue(fqBase)
    val result = obj.properties.toMutableList()
    var superFq = obj.superFqBase
    while (superFq != null) {
        val superObj = model.objects.getValue(superFq)
        result.addAll(superObj.properties)
        superFq = superObj.superFqBase
    }
    return result
}
