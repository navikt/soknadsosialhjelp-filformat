package no.nav.sosialhjelp.filformat.parity

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import java.io.File

/**
 * Shared plumbing for the parity tests.
 *
 * The fixtures live in the ROOT project (`src/test/resources/json`), not in this module.
 * Their location is injected via the `filformat.fixtures` system property by the `jvmTest`
 * task, so the tests do not depend on the working directory.
 */
internal object Fixtures {

    val root: File by lazy {
        val path = System.getProperty("filformat.fixtures")
            ?: error(
                "System property 'filformat.fixtures' is not set. It is configured on the " +
                    "jvmTest task in filformat-kmp/build.gradle.kts.",
            )
        File(path).also {
            check(it.isDirectory) { "Fixture directory does not exist: $it" }
        }
    }

    fun under(relative: String): List<File> =
        root.resolve(relative).walkTopDown()
            .filter { it.isFile && it.extension == "json" }
            // Mirrors TestDataFiles.PREFIX_IGNORE in the root project.
            .filterNot { it.name.startsWith("_") }
            .sortedBy { it.invariantPath }
            .toList()

    /** Path relative to the fixture root, with `/` separators, for stable test names. */
    val File.invariantPath: String
        get() = this.relativeTo(root).path.replace(File.separatorChar, '/')
}

/**
 * How a fixture is expected to behave when fed to the KMP model.
 *
 * The root project's `TestDataFiles` treats every `feil-`/`ikkegyldig_` file as "must fail
 * JSON-schema validation". That is a weaker statement than "must fail to deserialize": the
 * schema validator enforces `pattern`, `minItems` and friends, which neither Jackson nor
 * kotlinx.serialization look at. So each negative fixture is classified explicitly below.
 *
 * Getting a classification wrong makes this suite pass while asserting the wrong thing.
 * Review changes to the table in [ParityExpectations] carefully.
 */
internal enum class Expectation {
    /** Deserializes, matches the Java model field for field, and round-trips exactly. */
    PARITY,

    /**
     * As [PARITY], except the fixture deliberately contains fields that are not in the
     * schema, so an exact round-trip is impossible: the KMP model discards unknown keys.
     * Parity against the Java model is still asserted in full; the round-trip assertion is
     * weakened to "output is a subset of input".
     */
    PARITY_UKJENTE_FELTER_FORKASTES,

    /** Deserialization must throw. The document is structurally invalid. */
    MUST_THROW,

    /**
     * Deserialization must succeed even though the JSON-schema validator rejects the file.
     * Always paired with a reason, because "we tolerate invalid input here" is a claim that
     * needs justifying.
     */
    TOLERATED,
}

internal data class Expected(val expectation: Expectation, val reason: String = "")

/** Compares two JSON trees while ignoring differences that are not semantic. */
internal object JsonNormalizer {

    /**
     * Normalizes a tree so that the Java and KMP models can be compared:
     *
     * - drops `null`-valued object entries, since the Java model (jsonschema2pojo, no
     *   `@JsonInclude(NON_NULL)`) writes absent optional fields as explicit nulls while the
     *   KMP model omits them. Absent and null mean the same thing in this format.
     * - drops empty arrays for the same reason.
     * - canonicalises numbers via [Double], so `5000.00`, `5000.0` and `5000` compare equal.
     *   Safe here because the only numeric fields in this schema are `belop` (`"type":
     *   "number"`, `Double` in the Java model) and `nr` (a small `integer`).
     * - sorts object keys.
     */
    fun normalize(element: JsonElement): JsonElement = when (element) {
        is JsonObject ->
            JsonObject(
                element.entries
                    .filterNot { it.value is JsonNull }
                    .filterNot { it.value is JsonArray && (it.value as JsonArray).isEmpty() }
                    .associate { it.key to normalize(it.value) }
                    .toSortedMap(),
            )

        is JsonArray -> JsonArray(element.map { normalize(it) })

        is JsonPrimitive -> when {
            element.isString -> element
            else -> element.content.toDoubleOrNull()
                ?.let { JsonPrimitive(it.toString()) }
                ?: element
        }
    }

    fun parseAndNormalize(json: String): JsonElement =
        normalize(Json.parseToJsonElement(json))

    /**
     * True if [subset] is [full] with zero or more object entries removed, at any depth.
     * Used to check the round-trip of fixtures that carry fields outside the schema.
     */
    fun isDeepSubset(subset: JsonElement, full: JsonElement): Boolean = when {
        subset is JsonObject && full is JsonObject ->
            subset.all { (key, value) ->
                full[key]?.let { isDeepSubset(value, it) } == true
            }

        subset is JsonArray && full is JsonArray ->
            subset.size == full.size &&
                subset.indices.all { isDeepSubset(subset[it], full[it]) }

        else -> subset == full
    }
}
