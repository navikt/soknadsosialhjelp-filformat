package no.nav.sosialhjelp.filformat.parity

import kotlinx.serialization.json.Json
import no.nav.sosialhjelp.filformat.digisos.soker.DigisosSoker
import no.nav.sosialhjelp.filformat.digisos.soker.Hendelse
import no.nav.sosialhjelp.filformat.digisos.soker.UkjentHendelse
import no.nav.sosialhjelp.filformat.digisos.soker.SoknadsStatus
import no.nav.sosialhjelp.filformat.filformatJson
import no.nav.sosialhjelp.filformat.vedlegg.Vedlegg
import no.nav.sosialhjelp.filformat.vedlegg.VedleggSpesifikasjon
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

/**
 * Dedicated assertions for the two behaviours this library was created to provide, and which
 * the bulk parity sweep can only assert indirectly.
 */
class ToleranseTest {

    @Test
    fun `ukjent hendelsestype gir UkjentHendelse i stedet for aa kaste`() {
        val json = Fixtures.root
            .resolve("digisos/soker/parts/hendelse/feil-hendelse-eksisterer-ikke.json")
            .readText()

        val hendelse = filformatJson.decodeFromString(Hendelse.serializer(), json)

        assertThat(hendelse).isInstanceOf(UkjentHendelse::class.java)
        hendelse as UkjentHendelse
        assertThat(hendelse.type).isEqualTo("foobar")
        assertThat(hendelse.hendelsestidspunkt).isEqualTo("2018-10-04T13:37:00.134Z")

        // The raw payload is retained so the value round-trips without loss. Without this,
        // re-serializing would emit UkjentHendelse's own discriminator instead of "foobar".
        val roundTripped = filformatJson.encodeToString(Hendelse.serializer(), hendelse)
        assertThat(Json.parseToJsonElement(roundTripped))
            .isEqualTo(Json.parseToJsonElement(json))
    }

    @Test
    fun `en ukjent hendelse midt i en stroem stopper ikke de oevrige`() {
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

        val soker = filformatJson.decodeFromString(DigisosSoker.serializer(), json)

        assertThat(soker.hendelser).hasSize(3)
        assertThat(soker.hendelser[0]).isInstanceOf(SoknadsStatus::class.java)
        assertThat(soker.hendelser[1]).isInstanceOf(UkjentHendelse::class.java)
        assertThat(soker.hendelser[2]).isInstanceOf(SoknadsStatus::class.java)
        assertThat((soker.hendelser[2] as SoknadsStatus).status)
            .isEqualTo(SoknadsStatus.Status.FERDIGBEHANDLET)
    }

    @Test
    fun `ukjent enumverdi gir UKJENT i stedet for aa kaste`() {
        val json = Fixtures.root
            .resolve("digisos/soker/parts/hendelse/soknadsStatus/feil-status-eksisterer-ikke.json")
            .readText()

        val hendelse = filformatJson.decodeFromString(Hendelse.serializer(), json)

        assertThat(hendelse).isInstanceOf(SoknadsStatus::class.java)
        assertThat((hendelse as SoknadsStatus).status).isEqualTo(SoknadsStatus.Status.UKJENT)
    }

    @Test
    fun `ukjent hendelseType i vedleggSpesifikasjon gir UKJENT`() {
        val json = Fixtures.root.resolve("vedlegg/ikkegyldig_ugyldigHendelseType.json").readText()

        val spec = filformatJson.decodeFromString(VedleggSpesifikasjon.serializer(), json)

        assertThat(spec.vedlegg).hasSize(1)
        assertThat(spec.vedlegg!![0].hendelseType).isEqualTo(Vedlegg.HendelseType.UKJENT)
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
