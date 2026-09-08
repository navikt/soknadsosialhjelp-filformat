package no.nav.sosialhjelp.filformat.parity

import no.nav.sosialhjelp.filformat.digisos.soker.DigisosSoker
import no.nav.sosialhjelp.filformat.digisos.soker.Hendelse
import no.nav.sosialhjelp.filformat.filformatJson
import no.nav.sosialhjelp.filformat.vedlegg.VedleggSpesifikasjon
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.jupiter.api.Test

class ToleranseTest {

    @Test
    fun `ukjent hendelsestype kaster`() {
        val json = Fixtures.root
            .resolve("digisos/soker/parts/hendelse/feil-hendelse-eksisterer-ikke.json")
            .readText()

        assertThatThrownBy { filformatJson.decodeFromString(Hendelse.serializer(), json) }
            .isInstanceOf(Exception::class.java)
    }

    @Test
    fun `en ukjent hendelse midt i en stroem kaster`() {
        // The scenario that motivated this library: a municipality starts emitting a hendelse
        // type we have never seen. The surrounding hendelser must still be readable.
        val json = """
            {
              "version": "1.0.0",
              "avsender": { "systemnavn": "Testsystemet", "systemversjon": "1.0.0" },
              "hendelser": [
                { "type": "soknadsStatus", "hendelsestidspunkt": "2018-10-04T13:37:00.134Z", "status": "MOTTATT" },
                { "type": "heltNyKommunalHendelse", "hendelsestidspunkt": "2018-10-05T13:37:00.134Z", "noeNytt": 42 },
                { "type": "soknadsStatus", "hendelsestidspunkt": "2018-10-06T13:37:00.134Z", "status": "FERDIGBEHANDLET" }
              ]
            }
        """.trimIndent()

        assertThatThrownBy { filformatJson.decodeFromString(DigisosSoker.serializer(), json) }
            .isInstanceOf(Exception::class.java)
    }

    @Test
    fun `ukjent enumverdi kaster`() {
        val json = Fixtures.root
            .resolve("digisos/soker/parts/hendelse/soknadsStatus/feil-status-eksisterer-ikke.json")
            .readText()

        assertThatThrownBy { filformatJson.decodeFromString(Hendelse.serializer(), json) }
            .isInstanceOf(Exception::class.java)
    }

    @Test
    fun `ukjent hendelseType i vedleggSpesifikasjon kaster`() {
        val json = Fixtures.root.resolve("vedlegg/ikkegyldig_ugyldigHendelseType.json").readText()

        assertThatThrownBy { filformatJson.decodeFromString(VedleggSpesifikasjon.serializer(), json) }
            .isInstanceOf(Exception::class.java)
    }

    @Test
    fun `nye felter tolereres`() {
        val json = Fixtures.root.resolve("digisos/soker/kan-ha-nye-felter.json").readText()

        val soker = filformatJson.decodeFromString(DigisosSoker.serializer(), json)

        assertThat(soker.version).isEqualTo("1.0.0")
        assertThat(soker.hendelser).hasSize(1)

        // Unlike the Java model, which is generated with includeAdditionalProperties = true
        // and retains them, the KMP model DISCARDS unknown fields. Asserted here so the
        // difference is a documented decision rather than a surprise.
        val roundTripped = filformatJson.encodeToString(DigisosSoker.serializer(), soker)
        assertThat(roundTripped).doesNotContain("tullenavn")
    }
}
