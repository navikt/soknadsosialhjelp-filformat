package no.nav.sbl.soknadsosialhjelp.json

import no.nav.sbl.soknadsosialhjelp.soknad.JsonData
import no.nav.sbl.soknadsosialhjelp.soknad.JsonDriftsinformasjon
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknadsmottaker
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeSystem
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonAnsvar
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonBarnebidrag
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonErFolkeregistrertSammen
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonFamilie
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonForsorgerplikt
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonSamvarsgrad
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomiopplysninger
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomioversikt
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomibekreftelse
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonNordiskBorger
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonStatsborgerskap
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class VedleggsforventningMasterTest {
    @Test
    fun finnPaakrevdeVedleggLeggerKunTilSkattemeldingHvisAltAnnetMangler() {
        val internalSoknad = JsonInternalSoknad(soknad = JsonSoknad(
            version = "", data = testData(okonomi = JsonOkonomi(opplysninger = JsonOkonomiopplysninger(utbetaling = emptyList()))), mottaker = JsonSoknadsmottaker(),
            driftsinformasjon = JsonDriftsinformasjon(inntektFraSkatteetatenFeilet = false, utbetalingerFraNavFeilet = false, stotteFraHusbankenFeilet = false), kompatibilitet = emptyList(),
        ))

        val paakrevdeVedlegg = VedleggsforventningMaster.finnPaakrevdeVedlegg(internalSoknad)!!

        assertThat(paakrevdeVedlegg).hasSize(2)
        assertVedlegg(paakrevdeVedlegg[0], "skattemelding", "skattemelding")
        assertVedlegg(paakrevdeVedlegg[1], "annet", "annet")
    }

    @Test
    fun finnPaakrevdeVedleggForPersonaliaKreverVedleggForIkkeNordiskStatsborger() {
        val personalia = testPersonalia().copy(nordiskBorger = JsonNordiskBorger(JsonKilde.BRUKER, false), statsborgerskap = JsonStatsborgerskap(JsonKilde.BRUKER, "CHN"))
        assertVedlegg(VedleggsforventningMaster.finnPaakrevdeVedleggForPersonalia(personalia).first(), "oppholdstillatel", "oppholdstillatel")
    }

    @Test
    fun finnPaakrevdeVedleggForPersonaliaKreverIkkeVedleggForNorskStatsborger() {
        val personalia = testPersonalia().copy(nordiskBorger = JsonNordiskBorger(JsonKilde.BRUKER, true), statsborgerskap = JsonStatsborgerskap(JsonKilde.BRUKER, "NOR"))
        assertThat(VedleggsforventningMaster.finnPaakrevdeVedleggForPersonalia(personalia)).isEmpty()
    }

    @Test
    fun finnPaakrevdeVedleggForInntektNarSkatteetatenFeiler() = assertLonnslipp(inntektFraSkatteetatenFeilet = true, samtykke = true)

    @Test
    fun finnPaakrevdeVedleggForInntektNarViIkkeHarSamtykke() = assertLonnslipp(inntektFraSkatteetatenFeilet = false, samtykke = false)

    @Test
    fun finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarBarnebidrag() = assertBarnebidrag(JsonBarnebidrag.Verdi.MOTTAR, listOf("mottar"))

    @Test
    fun finnPaakrevdeVedleggForFamilieKreverVedleggHvisBetalerBarnebidrag() = assertBarnebidrag(JsonBarnebidrag.Verdi.BETALER, listOf("betaler"))

    @Test
    fun finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarOgBetalerBarnebidrag() = assertBarnebidrag(JsonBarnebidrag.Verdi.BEGGE, listOf("betaler", "mottar"))

    @Test
    fun finnPaakrevdeVedleggForFamilieKreverVedleggHvisBarnBorMindreEnn50prosentHosForelder() {
        val familie = JsonFamilie(JsonForsorgerplikt(ansvar = listOf(JsonAnsvar(erFolkeregistrertSammen = JsonErFolkeregistrertSammen(JsonKildeSystem.SYSTEM, false), samvarsgrad = JsonSamvarsgrad(JsonKildeBruker.BRUKER, 30)))))
        assertVedlegg(VedleggsforventningMaster.finnPaakrevdeVedleggForFamilie(familie).first(), "samvarsavtale", "barn")
    }

    @Test
    fun finnPaakrevdeVedleggForBosituasjonKreverVedleggForLeie() = assertVedlegg(VedleggsforventningMaster.finnPaakrevdeVedleggForBosituasjon(JsonBosituasjon(botype = JsonBosituasjon.Botype.LEIER)).first(), "husleiekontrakt", "husleiekontrakt")

    @Test
    fun finnPaakrevdeVedleggForBosituasjonKreverVedleggForKommunalBolig() = assertVedlegg(VedleggsforventningMaster.finnPaakrevdeVedleggForBosituasjon(JsonBosituasjon(botype = JsonBosituasjon.Botype.KOMMUNAL)).first(), "husleiekontrakt", "kommunal")

    @Test
    fun finnPaakrevdeVedleggForOkonomiHenterVedleggForAlleTyperInntekterUtgifterOgFormue() = assertOkonomiVedlegg(samtykke = null, husbankenFeilet = false, expected = listOf("husbanken" to "vedtak", "salgsoppgjor" to "eiendom", "faktura" to "strom", "faktura" to "oppvarming", "student" to "vedtak", "nedbetalingsplan" to "avdraglaan", "faktura" to "barnehage", "kontooversikt" to "brukskonto"))

    @Test
    fun finnPaakrevdeVedleggForOkonomiHarIkkeBostotteDersomViHarSamtykke() = assertOkonomiVedlegg(samtykke = true, husbankenFeilet = false, expected = listOf("salgsoppgjor" to "eiendom", "faktura" to "strom", "faktura" to "oppvarming", "student" to "vedtak", "nedbetalingsplan" to "avdraglaan", "faktura" to "barnehage", "kontooversikt" to "brukskonto"))

    @Test
    fun finnPaakrevdeVedleggForOkonomiHarBostotteDersomViHarSamtykkeMenHentingenHarFeilet() = assertOkonomiVedlegg(samtykke = true, husbankenFeilet = true, expected = listOf("husbanken" to "vedtak", "salgsoppgjor" to "eiendom", "faktura" to "strom", "faktura" to "oppvarming", "student" to "vedtak", "nedbetalingsplan" to "avdraglaan", "faktura" to "barnehage", "kontooversikt" to "brukskonto"))

    private fun assertLonnslipp(inntektFraSkatteetatenFeilet: Boolean, samtykke: Boolean) {
        val soknad = JsonInternalSoknad(soknad = testSoknad(
            driftsinformasjon = JsonDriftsinformasjon(inntektFraSkatteetatenFeilet),
            data = testData(
                arbeid = JsonArbeid(forhold = listOf(testArbeidsforhold())),
                okonomi = JsonOkonomi(opplysninger = JsonOkonomiopplysninger(
                    utbetaling = emptyList(),
                    bekreftelse = listOf(JsonOkonomibekreftelse(
                        kilde = JsonKilde.BRUKER,
                        type = SoknadJsonTyper.UTBETALING_SKATTEETATEN_SAMTYKKE,
                        tittel = "",
                        verdi = samtykke,
                    )),
                )),
            ),
        ))
        assertVedlegg(VedleggsforventningMaster.finnPaakrevdeVedleggForArbeid(soknad).first(), "lonnslipp", "arbeid")
    }

    private fun assertBarnebidrag(verdi: JsonBarnebidrag.Verdi, expected: List<String>) {
        val vedlegg = VedleggsforventningMaster.finnPaakrevdeVedleggForFamilie(JsonFamilie(JsonForsorgerplikt(barnebidrag = JsonBarnebidrag(verdi = verdi))))
        assertThat(vedlegg.map { it.type to it.tilleggsinfo }).containsExactly(*expected.map { "barnebidrag" to it }.toTypedArray())
    }

    private fun assertOkonomiVedlegg(samtykke: Boolean?, husbankenFeilet: Boolean, expected: List<Pair<String, String>>) {
        val bekreftelser = samtykke?.let {
            listOf(JsonOkonomibekreftelse(JsonKilde.BRUKER, SoknadJsonTyper.BOSTOTTE_SAMTYKKE, "", it))
        } ?: emptyList()
        val soknad = testSoknad(
            driftsinformasjon = JsonDriftsinformasjon(inntektFraSkatteetatenFeilet = false, stotteFraHusbankenFeilet = husbankenFeilet),
            data = testData(okonomi = JsonOkonomi(
                oversikt = JsonOkonomioversikt(inntekt = lagInntekter(), utgift = lagUtgifter(), formue = lagFormue()),
                opplysninger = JsonOkonomiopplysninger(utbetaling = lagUtbetalinger(), utgift = lagOpplysningUtgifter(), bekreftelse = bekreftelser),
            )),
        )
        assertThat(VedleggsforventningMaster.finnPaakrevdeVedleggForOkonomi(soknad).map { it.type to it.tilleggsinfo }).containsExactly(*expected.toTypedArray())
    }

    private fun lagInntekter() = listOf(testInntekt(SoknadJsonTyper.STUDIELAN))
    private fun lagUtgifter() = listOf(testUtgift(SoknadJsonTyper.UTGIFTER_BOLIGLAN_AVDRAG), testUtgift(SoknadJsonTyper.UTGIFTER_BARNEHAGE), testUtgift(SoknadJsonTyper.UTGIFTER_BARNEHAGE))
    private fun lagFormue() = listOf(testFormue(SoknadJsonTyper.VERDI_KJORETOY), testFormue(SoknadJsonTyper.VERDI_KJORETOY), testFormue(SoknadJsonTyper.FORMUE_BRUKSKONTO))
    private fun lagUtbetalinger() = listOf(testUtbetaling(SoknadJsonTyper.UTBETALING_HUSBANKEN), testUtbetaling(SoknadJsonTyper.UTBETALING_HUSBANKEN), testUtbetaling(SoknadJsonTyper.UTBETALING_SALG), testUtbetaling(SoknadJsonTyper.UTBETALING_SALG))
    private fun lagOpplysningUtgifter() = listOf(testOpplysningUtgift(SoknadJsonTyper.UTGIFTER_STROM), testOpplysningUtgift(SoknadJsonTyper.UTGIFTER_OPPVARMING), testOpplysningUtgift(SoknadJsonTyper.UTGIFTER_STROM))

    private fun testData(okonomi: JsonOkonomi, arbeid: JsonArbeid? = null) = JsonData(
        personalia = testPersonalia(),
        begrunnelse = no.nav.sbl.soknadsosialhjelp.soknad.begrunnelse.JsonBegrunnelse(JsonKildeBruker.BRUKER, ""),
        okonomi = okonomi,
        arbeid = arbeid,
    )

    private fun testPersonalia() = JsonPersonalia(
        personIdentifikator = no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonIdentifikator(no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonIdentifikator.Kilde.SYSTEM, ""),
        navn = no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonSokernavn(no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonSokernavn.Kilde.SYSTEM, "", "", ""),
        kontonummer = no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonKontonummer(kilde = JsonKilde.BRUKER, verdi = ""),
        nordiskBorger = JsonNordiskBorger(JsonKilde.BRUKER, true),
    )

    private fun testSoknad(data: JsonData, driftsinformasjon: JsonDriftsinformasjon = JsonDriftsinformasjon(false)) = JsonSoknad(
        version = "",
        data = data,
        mottaker = JsonSoknadsmottaker(),
        driftsinformasjon = driftsinformasjon,
        kompatibilitet = emptyList(),
    )

    private fun testArbeidsforhold() = JsonArbeidsforhold(kilde = JsonKilde.BRUKER, arbeidsgivernavn = "", fom = "", stillingsprosent = 100, overstyrtAvBruker = false)
    private fun testInntekt(type: String) = JsonOkonomioversiktInntekt(kilde = JsonKilde.BRUKER, type = type, tittel = "", overstyrtAvBruker = false)
    private fun testUtgift(type: String) = JsonOkonomioversiktUtgift(kilde = JsonKilde.BRUKER, type = type, tittel = "", overstyrtAvBruker = false)
    private fun testFormue(type: String) = JsonOkonomioversiktFormue(kilde = JsonKilde.BRUKER, type = type, tittel = "", overstyrtAvBruker = false)
    private fun testUtbetaling(type: String) = JsonOkonomiOpplysningUtbetaling(
        kilde = JsonKilde.BRUKER,
        type = type,
        tittel = "",
        overstyrtAvBruker = false,
    )
    private fun testOpplysningUtgift(type: String) = JsonOkonomiOpplysningUtgift(kilde = JsonKilde.BRUKER, type = type, tittel = "", overstyrtAvBruker = false)

    private fun assertVedlegg(vedlegg: JsonVedlegg, type: String, tilleggsinfo: String) {
        assertThat(vedlegg.type).isEqualTo(type)
        assertThat(vedlegg.tilleggsinfo).isEqualTo(tilleggsinfo)
    }

}
