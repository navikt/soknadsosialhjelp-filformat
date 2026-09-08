package filformat.codegen

fun packageOf(fqBase: String): String = fqBase.substringBeforeLast('.')

fun simpleOf(fqBase: String): String {
    val last = fqBase.substringAfterLast('.')
    return if (last.startsWith("Json") && last.length > 4) last.removePrefix("Json") else last
}

fun normalizeJavaType(raw: String): String {
    val pkg = packageOf(raw)
    return "$pkg.${simpleOf(raw)}"
}

fun jacksonFqName(fqBase: String): Pair<String, String> =
    packageOf(fqBase) to "Json${simpleOf(fqBase)}"

private const val ROOT_PACKAGE = "no.nav.sbl.soknadsosialhjelp"
private const val KOTLINX_ROOT_PACKAGE = "no.nav.sosialhjelp.filformat"

fun kotlinxFqName(fqBase: String): Pair<String, String> {
    val pkg = packageOf(fqBase)
    val tail = pkg.removePrefix(ROOT_PACKAGE)
    return "$KOTLINX_ROOT_PACKAGE$tail" to simpleOf(fqBase)
}
