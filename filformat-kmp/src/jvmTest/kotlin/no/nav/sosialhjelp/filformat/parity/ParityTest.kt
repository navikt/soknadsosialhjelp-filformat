package no.nav.sosialhjelp.filformat.parity

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.JsonElement
import no.nav.sosialhjelp.filformat.digisos.soker.Avsender
import no.nav.sosialhjelp.filformat.digisos.soker.DigisosSoker
import no.nav.sosialhjelp.filformat.digisos.soker.Filreferanse
import no.nav.sosialhjelp.filformat.digisos.soker.Hendelse
import no.nav.sosialhjelp.filformat.filformatJson
import no.nav.sosialhjelp.filformat.parity.Fixtures.invariantPath
import no.nav.sosialhjelp.filformat.vedlegg.VedleggSpesifikasjon
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestFactory
import java.io.File

/**
 * The safety net that makes hand-writing the KMP model acceptable.
 *
 * For every fixture in the root project this asserts, depending on its classification in
 * [ParityExpectations]:
 *
 *  1. the KMP model and the jsonschema2pojo-generated Java model produce the same data, and
 *  2. the KMP model round-trips back to semantically equal JSON.
 *
 * Because this runs as part of `./gradlew build`, drift between the two models fails CI.
 */
class ParityTest {

    private val javaMapper = JavaModelMapper.create()

    /**
     * Resolves which type a fixture should be read as, mirroring
     * `TestDataFiles.determineSchemaUri` in the root project: a fixture is validated against
     * the schema named after the directory it sits in.
     */
    private fun targetFor(path: String): Target = when {
        path.startsWith("digisos/soker/parts/hendelse/") ->
            Target(Hendelse.serializer(), no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse::class.java)

        path.startsWith("digisos/soker/parts/filreferanse/") ->
            Target(Filreferanse.serializer(), no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonFilreferanse::class.java)

        path.startsWith("digisos/soker/parts/avsender/") ->
            Target(Avsender.serializer(), no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonAvsender::class.java)

        path.startsWith("digisos/soker/") ->
            Target(DigisosSoker.serializer(), no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonDigisosSoker::class.java)

        path.startsWith("vedlegg/") ->
            Target(VedleggSpesifikasjon.serializer(), no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedleggSpesifikasjon::class.java)

        else -> error("No target type mapped for fixture '$path'")
    }

    private data class Target(val kotlin: KSerializer<*>, val java: Class<*>)

    private fun fixtures(): List<File> = Fixtures.under("digisos/soker") + Fixtures.under("vedlegg")

    @TestFactory
    fun `alle fixtures oppfoerer seg som forventet`(): List<DynamicTest> =
        fixtures().map { file ->
            val path = file.invariantPath
            DynamicTest.dynamicTest(path) { check(file, path) }
        }

    private fun check(file: File, path: String) {
        val expected = ParityExpectations.forPath(path)
        val target = targetFor(path)
        val text = file.readText()

        when (expected.expectation) {
            Expectation.MUST_THROW -> {
                assertThatThrownBy { filformatJson.decodeFromString(target.kotlin, text) }
                    .describedAs(
                        "%s is classified MUST_THROW (%s) but deserialized without error. " +
                            "Either the model got more permissive, or the classification in " +
                            "ParityExpectations is wrong.",
                        path, expected.reason,
                    )
                    .isInstanceOf(Exception::class.java)
            }

            Expectation.TOLERATED -> {
                val model = filformatJson.decodeFromString(target.kotlin, text)
                assertThat(model)
                    .describedAs("%s is classified TOLERATED (%s)", path, expected.reason)
                    .isNotNull()
                // No Java comparison: the Java model throws on unknown enum / hendelse type,
                // which is precisely the behaviour this library exists to change.
            }

            Expectation.PARITY, Expectation.PARITY_UKJENTE_FELTER_FORKASTES -> {
                val model = filformatJson.decodeFromString(target.kotlin, text)

                @Suppress("UNCHECKED_CAST")
                val kotlinTree: JsonElement = JsonNormalizer.parseAndNormalize(
                    filformatJson.encodeToString(target.kotlin as KSerializer<Any?>, model),
                )

                val javaObject = javaMapper.readValue(text, target.java)
                val javaTree = JsonNormalizer.parseAndNormalize(javaMapper.writeValueAsString(javaObject))

                assertThat(kotlinTree)
                    .describedAs(
                        "KMP model disagrees with the Java model for %s. " +
                            "The two models have drifted apart.",
                        path,
                    )
                    .isEqualTo(javaTree)

                val inputTree = JsonNormalizer.parseAndNormalize(text)
                if (expected.expectation == Expectation.PARITY) {
                    assertThat(kotlinTree)
                        .describedAs("KMP model does not round-trip %s", path)
                        .isEqualTo(inputTree)
                } else {
                    assertThat(JsonNormalizer.isDeepSubset(kotlinTree, inputTree))
                        .describedAs(
                            "%s is classified PARITY_UKJENTE_FELTER_FORKASTES (%s), so the " +
                                "round-trip only has to be a subset of the input -- but it " +
                                "was not even that.%nround-tripped: %s%ninput: %s",
                            path, expected.reason, kotlinTree, inputTree,
                        )
                        .isTrue()
                }
            }
        }
    }

    /**
     * Guards the triage table itself: an entry pointing at a fixture that no longer exists
     * means coverage was silently dropped when a file was renamed or deleted.
     */
    @Test
    fun `triage-tabellen refererer bare til fixtures som finnes`() {
        val actual = fixtures().map { it.invariantPath }.toSet()
        val dangling = ParityExpectations.byPath.keys - actual
        assertThat(dangling)
            .describedAs("ParityExpectations references fixtures that do not exist")
            .isEmpty()
    }

    /** Sanity check that the suite is actually looking at the fixtures we think it is. */
    @Test
    fun `finner forventet antall fixtures`() {
        assertThat(Fixtures.under("digisos/soker")).hasSize(45)
        assertThat(Fixtures.under("vedlegg")).hasSize(9)
    }
}
