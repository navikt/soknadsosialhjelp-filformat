package no.nav.sbl.soknadsosialhjelp.json;

import com.google.common.collect.Lists;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonData;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonDriftsinformasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonAnsvar;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonBarnebidrag;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonErFolkeregistrertSammen;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonFamilie;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonForsorgerplikt;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonSamvarsgrad;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomiopplysninger;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomioversikt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomibekreftelse;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonNordiskBorger;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonStatsborgerskap;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.BOSTOTTE_SAMTYKKE;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.FORMUE_BRUKSKONTO;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.STUDIELAN;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTBETALING_HUSBANKEN;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTBETALING_SALG;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTBETALING_SKATTEETATEN_SAMTYKKE;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTGIFTER_BARNEHAGE;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTGIFTER_BOLIGLAN_AVDRAG;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTGIFTER_OPPVARMING;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.UTGIFTER_STROM;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.VERDI_KJORETOY;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedlegg;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForArbeid;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForBosituasjon;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForFamilie;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForOkonomi;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.finnPaakrevdeVedleggForPersonalia;
import static org.assertj.core.api.Assertions.assertThat;

class VedleggsforventningMasterTest {

    @Test
    void finnPaakrevdeVedleggLeggerKunTilSkattemeldingHvisAltAnnetMangler() {
        JsonInternalSoknad internalSoknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withData(new JsonData()
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(new ArrayList<>()))))
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withUtbetalingerFraNavFeilet(Boolean.valueOf(false))
                                .withInntektFraSkatteetatenFeilet(Boolean.valueOf(false))
                                .withStotteFraHusbankenFeilet(Boolean.valueOf(false))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedlegg(internalSoknad);

        assertThat(paakrevdeVedlegg).hasSize(2);
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("skattemelding");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("skattemelding");
        JsonVedlegg vedlegg2 = paakrevdeVedlegg.get(1);
        assertThat(vedlegg2.getType()).isEqualTo("annet");
        assertThat(vedlegg2.getTilleggsinfo()).isEqualTo("annet");
    }

    @Test
    void finnPaakrevdeVedleggForPersonaliaKreverVedleggForIkkeNordiskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(Boolean.valueOf(false)))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("CHN"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("oppholdstillatel");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("oppholdstillatel");
    }

    @Test
    void finnPaakrevdeVedleggForPersonaliaKreverIkkeVedleggForNorskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(Boolean.valueOf(true)))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("NOR"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        assertThat(paakrevdeVedlegg).isEmpty();
    }

    @Test
    void finnPaakrevdeVedleggForInntektNarSkatteetatenFeiler() {
        JsonInternalSoknad soknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withInntektFraSkatteetatenFeilet(Boolean.valueOf(true)))
                        .withData(new JsonData()
                                .withArbeid(new JsonArbeid()
                                        .withForhold(Lists.newArrayList(new JsonArbeidsforhold().withStillingsprosent(Integer.valueOf(100)))))
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(Lists.newArrayList(
                                                        new JsonOkonomibekreftelse()
                                                                .withType(UTBETALING_SKATTEETATEN_SAMTYKKE)
                                                                .withVerdi(Boolean.valueOf(true))
                                                ))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForArbeid(soknad);

        assertThat(paakrevdeVedlegg).hasSize(1);
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("lonnslipp");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("arbeid");
    }

    @Test
    void finnPaakrevdeVedleggForInntektNarViIkkeHarSamtykke() {
        JsonInternalSoknad soknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withInntektFraSkatteetatenFeilet(Boolean.valueOf(false)))
                        .withData(new JsonData()
                                .withArbeid(new JsonArbeid()
                                        .withForhold(Lists.newArrayList(new JsonArbeidsforhold().withStillingsprosent(Integer.valueOf(100)))))
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(Lists.newArrayList(
                                                        new JsonOkonomibekreftelse()
                                                                .withType(UTBETALING_SKATTEETATEN_SAMTYKKE)
                                                                .withVerdi(Boolean.valueOf(false))
                                                ))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForArbeid(soknad);

        assertThat(paakrevdeVedlegg).hasSize(1);
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("lonnslipp");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("arbeid");
    }

    @Test
    void finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.MOTTAR)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("barnebidrag");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("mottar");
    }

    @Test
    void finnPaakrevdeVedleggForFamilieKreverVedleggHvisBetalerBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.BETALER)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("barnebidrag");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("betaler");
    }

    @Test
    void finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarOgBetalerBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.BEGGE)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedleggBetaler = paakrevdeVedlegg.get(0);
        assertThat(vedleggBetaler.getType()).isEqualTo("barnebidrag");
        assertThat(vedleggBetaler.getTilleggsinfo()).isEqualTo("betaler");
        JsonVedlegg vedleggMottar = paakrevdeVedlegg.get(1);
        assertThat(vedleggMottar.getType()).isEqualTo("barnebidrag");
        assertThat(vedleggMottar.getTilleggsinfo()).isEqualTo("mottar");
    }

    @Test
    void finnPaakrevdeVedleggForFamilieKreverVedleggHvisBarnBorMindreEnn50prosentHosForelder() {
        JsonAnsvar barnMedDeltBosted = new JsonAnsvar()
                .withErFolkeregistrertSammen(new JsonErFolkeregistrertSammen().withVerdi(Boolean.valueOf(false)))
                .withSamvarsgrad(new JsonSamvarsgrad().withVerdi(Integer.valueOf(30)));
        List<JsonAnsvar> forsorgerAnsvar = new ArrayList<>();
        forsorgerAnsvar.add(barnMedDeltBosted);
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withAnsvar(forsorgerAnsvar));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("samvarsavtale");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("barn");
    }

    @Test
    void finnPaakrevdeVedleggForBosituasjonKreverVedleggForLeie() {
        JsonBosituasjon bosituasjon = new JsonBosituasjon().withBotype(JsonBosituasjon.Botype.LEIER);

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForBosituasjon(bosituasjon);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("husleiekontrakt");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("husleiekontrakt");
    }

    @Test
    void finnPaakrevdeVedleggForBosituasjonKreverVedleggForKommunalBolig() {
        JsonBosituasjon bosituasjon = new JsonBosituasjon().withBotype(JsonBosituasjon.Botype.KOMMUNAL);

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForBosituasjon(bosituasjon);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType()).isEqualTo("husleiekontrakt");
        assertThat(vedlegg.getTilleggsinfo()).isEqualTo("kommunal");
    }

    @Test
    void finnPaakrevdeVedleggForOkonomiHenterVedleggForAlleTyperInntekterUtgifterOgFormue() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(Boolean.valueOf(false)))
                .withData(new JsonData()
                        .withOkonomi(new JsonOkonomi()
                                .withOversikt(new JsonOkonomioversikt()
                                        .withInntekt(lagInntekter())
                                        .withUtgift(lagUtgifter())
                                        .withFormue(lagFormue()))
                                .withOpplysninger(new JsonOkonomiopplysninger()
                                        .withUtbetaling(lagUtbetalinger())
                                        .withUtgift(lagOpplysningUtgifter()))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(soknad);

        assertThat(paakrevdeVedlegg).hasSize(8);
        assertThat(paakrevdeVedlegg.get(0).getType()).isEqualTo("husbanken");
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo()).isEqualTo("vedtak");
        assertThat(paakrevdeVedlegg.get(1).getType()).isEqualTo("salgsoppgjor");
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo()).isEqualTo("eiendom");
        assertThat(paakrevdeVedlegg.get(2).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo()).isEqualTo("strom");
        assertThat(paakrevdeVedlegg.get(3).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo()).isEqualTo("oppvarming");
        assertThat(paakrevdeVedlegg.get(4).getType()).isEqualTo("student");
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo()).isEqualTo("vedtak");
        assertThat(paakrevdeVedlegg.get(5).getType()).isEqualTo("nedbetalingsplan");
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo()).isEqualTo("avdraglaan");
        assertThat(paakrevdeVedlegg.get(6).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo()).isEqualTo("barnehage");
        assertThat(paakrevdeVedlegg.get(7).getType()).isEqualTo("kontooversikt");
        assertThat(paakrevdeVedlegg.get(7).getTilleggsinfo()).isEqualTo("brukskonto");
    }

    @Test
    void finnPaakrevdeVedleggForOkonomiHarIkkeBostotteDersomViHarSamtykke() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(Boolean.valueOf(false)))
                .withData(new JsonData()
                        .withOkonomi(new JsonOkonomi()
                                .withOversikt(new JsonOkonomioversikt()
                                        .withInntekt(lagInntekter())
                                        .withUtgift(lagUtgifter())
                                        .withFormue(lagFormue()))
                                .withOpplysninger(new JsonOkonomiopplysninger()
                                        .withUtbetaling(lagUtbetalinger())
                                        .withUtgift(lagOpplysningUtgifter())
                                        .withBekreftelse(Lists.newArrayList(new JsonOkonomibekreftelse()
                                                .withType(BOSTOTTE_SAMTYKKE)
                                                .withVerdi(Boolean.valueOf(true)))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(soknad);

        assertThat(paakrevdeVedlegg).hasSize(7);
        assertThat(paakrevdeVedlegg.get(0).getType()).isEqualTo("salgsoppgjor");
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo()).isEqualTo("eiendom");
        assertThat(paakrevdeVedlegg.get(1).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo()).isEqualTo("strom");
        assertThat(paakrevdeVedlegg.get(2).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo()).isEqualTo("oppvarming");
        assertThat(paakrevdeVedlegg.get(3).getType()).isEqualTo("student");
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo()).isEqualTo("vedtak");
        assertThat(paakrevdeVedlegg.get(4).getType()).isEqualTo("nedbetalingsplan");
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo()).isEqualTo("avdraglaan");
        assertThat(paakrevdeVedlegg.get(5).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo()).isEqualTo("barnehage");
        assertThat(paakrevdeVedlegg.get(6).getType()).isEqualTo("kontooversikt");
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo()).isEqualTo("brukskonto");
    }

    @Test
    void finnPaakrevdeVedleggForOkonomiHarBostotteDersomViHarSamtykkeMenHentingenHarFeilet() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(Boolean.valueOf(true)))
                .withData(new JsonData()
                        .withOkonomi(new JsonOkonomi()
                                .withOversikt(new JsonOkonomioversikt()
                                        .withInntekt(lagInntekter())
                                        .withUtgift(lagUtgifter())
                                        .withFormue(lagFormue()))
                                .withOpplysninger(new JsonOkonomiopplysninger()
                                        .withUtbetaling(lagUtbetalinger())
                                        .withUtgift(lagOpplysningUtgifter())
                                        .withBekreftelse(Lists.newArrayList(new JsonOkonomibekreftelse()
                                                .withType(BOSTOTTE_SAMTYKKE)
                                                .withVerdi(Boolean.valueOf(true)))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(soknad);

        assertThat(paakrevdeVedlegg).hasSize(8);
        assertThat(paakrevdeVedlegg.get(0).getType()).isEqualTo("husbanken");
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo()).isEqualTo("vedtak");
        assertThat(paakrevdeVedlegg.get(1).getType()).isEqualTo("salgsoppgjor");
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo()).isEqualTo("eiendom");
        assertThat(paakrevdeVedlegg.get(2).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo()).isEqualTo("strom");
        assertThat(paakrevdeVedlegg.get(3).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo()).isEqualTo("oppvarming");
        assertThat(paakrevdeVedlegg.get(4).getType()).isEqualTo("student");
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo()).isEqualTo("vedtak");
        assertThat(paakrevdeVedlegg.get(5).getType()).isEqualTo("nedbetalingsplan");
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo()).isEqualTo("avdraglaan");
        assertThat(paakrevdeVedlegg.get(6).getType()).isEqualTo("faktura");
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo()).isEqualTo("barnehage");
        assertThat(paakrevdeVedlegg.get(7).getType()).isEqualTo("kontooversikt");
        assertThat(paakrevdeVedlegg.get(7).getTilleggsinfo()).isEqualTo("brukskonto");
    }

    private List<JsonOkonomioversiktInntekt> lagInntekter() {
        List<JsonOkonomioversiktInntekt> inntekter = new ArrayList<>();
        inntekter.add(new JsonOkonomioversiktInntekt().withType(STUDIELAN));
        return inntekter;
    }

    private List<JsonOkonomioversiktUtgift> lagUtgifter() {
        List<JsonOkonomioversiktUtgift> utgifter = new ArrayList<>();
        utgifter.add(new JsonOkonomioversiktUtgift().withType(UTGIFTER_BOLIGLAN_AVDRAG));
        utgifter.add(new JsonOkonomioversiktUtgift().withType(UTGIFTER_BARNEHAGE));
        utgifter.add(new JsonOkonomioversiktUtgift().withType(UTGIFTER_BARNEHAGE));
        return utgifter;
    }

    private List<JsonOkonomioversiktFormue> lagFormue() {
        List<JsonOkonomioversiktFormue> formuer = new ArrayList<>();
        formuer.add(new JsonOkonomioversiktFormue().withType(VERDI_KJORETOY));
        formuer.add(new JsonOkonomioversiktFormue().withType(VERDI_KJORETOY));
        formuer.add(new JsonOkonomioversiktFormue().withType(FORMUE_BRUKSKONTO));
        return formuer;
    }

    private List<JsonOkonomiOpplysningUtbetaling> lagUtbetalinger() {
        List<JsonOkonomiOpplysningUtbetaling> utbetalinger = new ArrayList<>();
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType(UTBETALING_HUSBANKEN));
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType(UTBETALING_HUSBANKEN));
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType(UTBETALING_SALG));
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType(UTBETALING_SALG));
        return utbetalinger;
    }

    private List<JsonOkonomiOpplysningUtgift> lagOpplysningUtgifter() {
        List<JsonOkonomiOpplysningUtgift> utgifter = new ArrayList<>();
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType(UTGIFTER_STROM));
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType(UTGIFTER_OPPVARMING));
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType(UTGIFTER_STROM));
        return utgifter;
    }
}
