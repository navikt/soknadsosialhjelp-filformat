package no.nav.sosialhjelp.filformat

import kotlinx.serialization.json.Json
import no.nav.sosialhjelp.filformat.digisos.soker.DigisosSoker
import no.nav.sosialhjelp.filformat.digisos.soker.DokumentasjonEtterspurt
import no.nav.sosialhjelp.filformat.digisos.soker.DokumentlagerFilreferanse
import no.nav.sosialhjelp.filformat.digisos.soker.SoknadsStatus
import no.nav.sosialhjelp.filformat.digisos.soker.SvarUtFilreferanse
import no.nav.sosialhjelp.filformat.digisos.soker.UkjentHendelse
import no.nav.sosialhjelp.filformat.digisos.soker.Utbetaling
import no.nav.sosialhjelp.filformat.digisos.soker.VedtakFattet
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertIs
import kotlin.test.assertTrue

/**
 * Runs on every target.
 *
 * The parity suite is JVM-only by necessity -- it compares against the Java model -- so this
 * is what actually exercises the JS/Node build. If the JS target regresses, this fails.
 */
class ModellTest {

    private val komplettNok = """
        {
          "version": "1.0.0",
          "avsender": { "systemnavn": "Testsystemet", "systemversjon": "1.0.0" },
          "hendelser": [
            { "type": "soknadsStatus", "hendelsestidspunkt": "2018-10-04T13:37:00.134Z", "status": "MOTTATT" },
            { "type": "tildeltNavKontor", "hendelsestidspunkt": "2018-10-04T13:42:00.134Z", "navKontor": "0314" },
            {
              "type": "vedtakFattet",
              "hendelsestidspunkt": "2018-10-12T13:37:00.134Z",
              "saksreferanse": "SAK1",
              "utfall": "INNVILGET",
              "vedtaksfil": { "referanse": { "type": "dokumentlager", "id": "12345678-9abc-def0-1234-56789abcdef0" } },
              "vedlegg": [
                { "tittel": "Test", "referanse": { "type": "svarut", "id": "12345678-9abc-def0-1234-56789abcdef0", "nr": 1 } }
              ]
            },
            {
              "type": "utbetaling",
              "hendelsestidspunkt": "2018-10-12T13:37:00.134Z",
              "utbetalingsreferanse": "UTBETALINGNR1SAK2",
              "belop": 5000.5,
              "status": "PLANLAGT_UTBETALING",
              "annenMottaker": true
            },
            {
              "type": "dokumentasjonEtterspurt",
              "hendelsestidspunkt": "2018-10-11T13:42:00.134Z",
              "dokumenter": [
                { "dokumenttype": "Strømfaktura", "innsendelsesfrist": "2018-10-20T07:37:00.134Z" }
              ]
            }
          ]
        }
    """.trimIndent()

    @Test
    fun `leser en komplett digisos-soker`() {
        val soker = filformatJson.decodeFromString(DigisosSoker.serializer(), komplettNok)

        assertEquals("1.0.0", soker.version)
        assertEquals("Testsystemet", soker.avsender.systemnavn)
        assertEquals(5, soker.hendelser.size)

        assertEquals(SoknadsStatus.Status.MOTTATT, assertIs<SoknadsStatus>(soker.hendelser[0]).status)

        val vedtak = assertIs<VedtakFattet>(soker.hendelser[2])
        assertEquals(VedtakFattet.Utfall.INNVILGET, vedtak.utfall)
        assertEquals(
            "12345678-9abc-def0-1234-56789abcdef0",
            assertIs<DokumentlagerFilreferanse>(vedtak.vedtaksfil.referanse).id,
        )
        assertEquals(1, assertIs<SvarUtFilreferanse>(vedtak.vedlegg!![0].referanse).nr)

        val utbetaling = assertIs<Utbetaling>(soker.hendelser[3])
        // Double, not a decimal type -- matches the Java model. See Utbetaling.belop.
        assertEquals(5000.5, utbetaling.belop)
        assertEquals(true, utbetaling.annenMottaker)

        assertEquals(1, assertIs<DokumentasjonEtterspurt>(soker.hendelser[4]).dokumenter.size)
    }

    @Test
    fun `round-trip bevarer innholdet`() {
        val soker = filformatJson.decodeFromString(DigisosSoker.serializer(), komplettNok)
        val encoded = filformatJson.encodeToString(DigisosSoker.serializer(), soker)

        assertEquals(
            Json.parseToJsonElement(komplettNok),
            Json.parseToJsonElement(encoded),
        )
    }

    @Test
    fun `ukjent hendelsestype blir UkjentHendelse og bevarer payloaden`() {
        val json = """
            {"type":"heltNyKommunalHendelse","hendelsestidspunkt":"2018-10-04T13:37:00.134Z","noeNytt":42}
        """.trimIndent()

        val hendelse = filformatJson.decodeFromString(
            no.nav.sosialhjelp.filformat.digisos.soker.Hendelse.serializer(),
            json,
        )

        val ukjent = assertIs<UkjentHendelse>(hendelse)
        assertEquals("heltNyKommunalHendelse", ukjent.type)
        assertEquals(
            Json.parseToJsonElement(json),
            Json.parseToJsonElement(
                filformatJson.encodeToString(
                    no.nav.sosialhjelp.filformat.digisos.soker.Hendelse.serializer(),
                    hendelse,
                ),
            ),
        )
    }

    @Test
    fun `ukjent enumverdi blir UKJENT`() {
        val json = """
            {"type":"soknadsStatus","hendelsestidspunkt":"2018-10-04T13:37:00.134Z","status":"HELT_NY_STATUS"}
        """.trimIndent()

        val hendelse = filformatJson.decodeFromString(
            no.nav.sosialhjelp.filformat.digisos.soker.Hendelse.serializer(),
            json,
        )

        assertEquals(SoknadsStatus.Status.UKJENT, assertIs<SoknadsStatus>(hendelse).status)
    }

    @Test
    fun `hendelse uten hendelsestidspunkt kastes`() {
        // Tolerating an unknown `type` is deliberate. Tolerating a structurally invalid
        // hendelse is not.
        assertFailsWith<Exception> {
            filformatJson.decodeFromString(
                no.nav.sosialhjelp.filformat.digisos.soker.Hendelse.serializer(),
                """{"type":"heltNyKommunalHendelse"}""",
            )
        }
    }

    @Test
    fun `ukjente felter forkastes uten aa kaste`() {
        val json = """
            {
              "version": "1.0.0",
              "avsender": { "systemnavn": "T", "systemversjon": "1" },
              "hendelser": [],
              "heltNyttFelt": { "a": 1 }
            }
        """.trimIndent()

        val soker = filformatJson.decodeFromString(DigisosSoker.serializer(), json)
        val encoded = filformatJson.encodeToString(DigisosSoker.serializer(), soker)

        assertTrue("heltNyttFelt" !in encoded)
    }
}
