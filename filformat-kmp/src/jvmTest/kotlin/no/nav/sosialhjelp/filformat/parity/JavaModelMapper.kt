package no.nav.sosialhjelp.filformat.parity

import com.fasterxml.jackson.annotation.JsonAnyGetter
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonDigisosSoker
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonFilreferanse
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse
import tools.jackson.databind.json.JsonMapper
import java.io.File
import java.util.jar.JarFile

/**
 * Builds the Jackson mapper used to read the Java reference model in [ParityTest].
 *
 * The Java model is generated with `includeAdditionalProperties = true`, so every generated
 * class captures unknown fields and re-emits them through `@JsonAnyGetter`. The KMP model
 * discards them (`ignoreUnknownKeys`). Left alone, that difference would make the two
 * forward-compatibility fixtures (`kan-ha-nye-felter.json`, `gyldig_ekstrafelter.json`) fail
 * for a reason that has nothing to do with model drift.
 *
 * Suppressing `additionalProperties` on the Java side is the right fix rather than relaxing
 * the assertion for those two files: a relaxed assertion ("ignore fields the KMP model does
 * not have") would also hide a known field the KMP model had genuinely forgotten to declare,
 * which is precisely the drift this suite exists to catch.
 *
 * The retain-vs-discard behaviour difference is itself asserted, deliberately, in
 * `ToleranseTest.nye felter tolereres`.
 *
 * Mixins are registered per concrete class -- Jackson does not broadcast a mixin registered
 * against `Object`. The class list is discovered by scanning the compiled root project so
 * that types added to the JSON schemas later are covered without touching this file.
 */
internal object JavaModelMapper {

    private val generatedModelPackages = listOf(
        "no/nav/sbl/soknadsosialhjelp/digisos/",
        "no/nav/sbl/soknadsosialhjelp/vedlegg/",
    )

    private interface IgnorerAdditionalProperties {
        // `@JsonAnyGetter(enabled = false)` rather than `@JsonIgnore`: ignoral applies to
        // ordinary properties, and does not switch off an any-getter.
        @get:JsonAnyGetter(enabled = false)
        val additionalProperties: Map<String, Any?>
    }

    // JsonSosialhjelpObjectMapper already registers mixins on JsonHendelse and
    // JsonFilreferanse to carry @JsonTypeInfo/@JsonSubTypes. Jackson allows only ONE mixin
    // per class, and re-registering would silently replace that type information, collapsing
    // every hendelse to its base type -- with the subclass-specific fields (status,
    // navKontor, id, ...) vanishing from the comparison. Combining the two via mixin
    // interface inheritance does NOT work: Jackson does not pick up @JsonSubTypes from a
    // mixin's supertypes. So these two are left alone.
    //
    // This is safe because every concrete subclass overrides getAdditionalProperties(), and
    // Jackson serializes using the runtime type -- so the any-getter is still suppressed on
    // the class that actually does the capturing.
    private val skipMixIn: Set<Class<*>> = setOf(
        JsonHendelse::class.java,
        JsonFilreferanse::class.java,
    )

    fun create(): JsonMapper {
        val builder = no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper.createJsonMapperBuilder()
        val classes = generatedModelClasses()
        check(classes.size >= 20) {
            "Only found ${classes.size} generated model classes; the classpath scan is broken. " +
                "Without it, additionalProperties would leak into the parity comparison."
        }
        classes.forEach {
            if (it !in skipMixIn) builder.addMixIn(it, IgnorerAdditionalProperties::class.java)
        }
        return builder.build()
    }

    private fun generatedModelClasses(): List<Class<*>> {
        val location = JsonDigisosSoker::class.java.protectionDomain.codeSource.location
        val root = File(location.toURI())
        val names = if (root.isDirectory) scanDirectory(root) else scanJar(root)
        return names.map { Class.forName(it) }
    }

    private fun scanDirectory(root: File): List<String> =
        root.walkTopDown()
            .filter { it.isFile && it.extension == "class" }
            .map { it.relativeTo(root).path.replace(File.separatorChar, '/') }
            .filter { entry -> generatedModelPackages.any { entry.startsWith(it) } }
            .map { it.removeSuffix(".class").replace('/', '.') }
            .toList()

    private fun scanJar(jar: File): List<String> =
        JarFile(jar).use { archive ->
            archive.entries().asSequence()
                .map { it.name }
                .filter { it.endsWith(".class") }
                .filter { entry -> generatedModelPackages.any { entry.startsWith(it) } }
                .map { it.removeSuffix(".class").replace('/', '.') }
                .toList()
        }
}
