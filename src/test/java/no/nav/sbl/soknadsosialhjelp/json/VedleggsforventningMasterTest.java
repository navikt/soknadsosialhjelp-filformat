package no.nav.sbl.soknadsosialhjelp.json;

import com.google.common.collect.Lists;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonData;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonDriftsinformasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.*;
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
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.*;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VedleggsforventningMasterTest {

    @Test
    public void finnPaakrevdeVedleggLeggerKunTilSkattemeldingHvisAltAnnetMangler() {
        JsonInternalSoknad internalSoknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withData(new JsonData()
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(Collections.EMPTY_LIST))))
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withUtbetalingerFraNavFeilet(false)
                                .withInntektFraSkatteetatenFeilet(false)
                                .withStotteFraHusbankenFeilet(false)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedlegg(internalSoknad);

        assertThat(paakrevdeVedlegg.size(), is(2));
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("skattemelding"));
        assertThat(vedlegg.getTilleggsinfo(), is("skattemelding"));
        JsonVedlegg vedlegg2 = paakrevdeVedlegg.get(1);
        assertThat(vedlegg2.getType(), is("annet"));
        assertThat(vedlegg2.getTilleggsinfo(), is("annet"));
    }

    @Test
    public void finnPaakrevdeVedleggForPersonaliaKreverVedleggForIkkeNordiskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(false))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("CHN"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("oppholdstillatel"));
        assertThat(vedlegg.getTilleggsinfo(), is("oppholdstillatel"));
    }

    @Test
    public void finnPaakrevdeVedleggForPersonaliaKreverIkkeVedleggForNorskStatsborger() {
        JsonPersonalia personalia = new JsonPersonalia()
                .withNordiskBorger(new JsonNordiskBorger().withVerdi(true))
                .withStatsborgerskap(new JsonStatsborgerskap().withVerdi("NOR"));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForPersonalia(personalia);

        assertThat(paakrevdeVedlegg.size(), is(0));
    }

    @Test
    public void finnPaakrevdeVedleggForInntektNarSkatteetatenFeiler() {
        JsonInternalSoknad soknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withInntektFraSkatteetatenFeilet(true))
                        .withData(new JsonData()
                                .withArbeid(new JsonArbeid()
                                        .withForhold(Lists.newArrayList(new JsonArbeidsforhold().withStillingsprosent(100))))
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(Lists.newArrayList(
                                                        new JsonOkonomibekreftelse()
                                                                .withType(UTBETALING_SKATTEETATEN_SAMTYKKE)
                                                                .withVerdi(true)
                                                ))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForArbeid(soknad);

        assertThat(paakrevdeVedlegg.size(), is(1));
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("lonnslipp"));
        assertThat(vedlegg.getTilleggsinfo(), is("arbeid"));
    }

    @Test
    public void finnPaakrevdeVedleggForInntektNarViIkkeHarSamtykke() {
        JsonInternalSoknad soknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withDriftsinformasjon(new JsonDriftsinformasjon()
                                .withInntektFraSkatteetatenFeilet(false))
                        .withData(new JsonData()
                                .withArbeid(new JsonArbeid()
                                        .withForhold(Lists.newArrayList(new JsonArbeidsforhold().withStillingsprosent(100))))
                                .withOkonomi(new JsonOkonomi()
                                        .withOpplysninger(new JsonOkonomiopplysninger()
                                                .withBekreftelse(Lists.newArrayList(
                                                        new JsonOkonomibekreftelse()
                                                                .withType(UTBETALING_SKATTEETATEN_SAMTYKKE)
                                                                .withVerdi(false)
                                                ))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForArbeid(soknad);

        assertThat(paakrevdeVedlegg.size(), is(1));
        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("lonnslipp"));
        assertThat(vedlegg.getTilleggsinfo(), is("arbeid"));
    }

    @Test
    public void finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.MOTTAR)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("barnebidrag"));
        assertThat(vedlegg.getTilleggsinfo(), is("mottar"));
    }

    @Test
    public void finnPaakrevdeVedleggForFamilieKreverVedleggHvisBetalerBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.BETALER)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("barnebidrag"));
        assertThat(vedlegg.getTilleggsinfo(), is("betaler"));
    }

    @Test
    public void finnPaakrevdeVedleggForFamilieKreverVedleggHvisMottarOgBetalerBarnebidrag() {
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withBarnebidrag(new JsonBarnebidrag().withVerdi(JsonBarnebidrag.Verdi.BEGGE)));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedleggBetaler = paakrevdeVedlegg.get(0);
        assertThat(vedleggBetaler.getType(), is("barnebidrag"));
        assertThat(vedleggBetaler.getTilleggsinfo(), is("betaler"));
        JsonVedlegg vedleggMottar = paakrevdeVedlegg.get(1);
        assertThat(vedleggMottar.getType(), is("barnebidrag"));
        assertThat(vedleggMottar.getTilleggsinfo(), is("mottar"));
    }

    @Test
    public void finnPaakrevdeVedleggForFamilieKreverVedleggHvisBarnBorMindreEnn50prosentHosForelder() {
        JsonAnsvar barnMedDeltBosted = new JsonAnsvar()
                .withErFolkeregistrertSammen(new JsonErFolkeregistrertSammen().withVerdi(false))
                .withSamvarsgrad(new JsonSamvarsgrad().withVerdi(30));
        List<JsonAnsvar> forsorgerAnsvar = new ArrayList<>();
        forsorgerAnsvar.add(barnMedDeltBosted);
        JsonFamilie familie = new JsonFamilie()
                .withForsorgerplikt(new JsonForsorgerplikt()
                        .withAnsvar(forsorgerAnsvar));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForFamilie(familie);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("samvarsavtale"));
        assertThat(vedlegg.getTilleggsinfo(), is("barn"));
    }

    @Test
    public void finnPaakrevdeVedleggForBosituasjonKreverVedleggForLeie() {
        JsonBosituasjon bosituasjon = new JsonBosituasjon().withBotype(JsonBosituasjon.Botype.LEIER);

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForBosituasjon(bosituasjon);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("husleiekontrakt"));
        assertThat(vedlegg.getTilleggsinfo(), is("husleiekontrakt"));
    }

    @Test
    public void finnPaakrevdeVedleggForBosituasjonKreverVedleggForKommunalBolig() {
        JsonBosituasjon bosituasjon = new JsonBosituasjon().withBotype(JsonBosituasjon.Botype.KOMMUNAL);

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForBosituasjon(bosituasjon);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("husleiekontrakt"));
        assertThat(vedlegg.getTilleggsinfo(), is("kommunal"));
    }

    @Test
    public void finnPaakrevdeVedleggForOkonomiHenterVedleggForAlleTyperInntekterUtgifterOgFormue() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(false))
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

        assertThat(paakrevdeVedlegg.size(), is(8));
        assertThat(paakrevdeVedlegg.get(0).getType(), is("salgsoppgjor"));
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo(), is("eiendom"));
        assertThat(paakrevdeVedlegg.get(1).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo(), is("strom"));
        assertThat(paakrevdeVedlegg.get(2).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo(), is("oppvarming"));
        assertThat(paakrevdeVedlegg.get(3).getType(), is("bostotte"));
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(4).getType(), is("student"));
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(5).getType(), is("nedbetalingsplan"));
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo(), is("avdraglaan"));
        assertThat(paakrevdeVedlegg.get(6).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo(), is("barnehage"));
        assertThat(paakrevdeVedlegg.get(7).getType(), is("kontooversikt"));
        assertThat(paakrevdeVedlegg.get(7).getTilleggsinfo(), is("brukskonto"));
    }

    @Test
    public void finnPaakrevdeVedleggForOkonomiHarIkkeBostotteDersomViHarSamtykke() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(false))
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
                                                .withVerdi(true))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(soknad);

        assertThat(paakrevdeVedlegg.size(), is(7));
        assertThat(paakrevdeVedlegg.get(0).getType(), is("salgsoppgjor"));
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo(), is("eiendom"));
        assertThat(paakrevdeVedlegg.get(1).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo(), is("strom"));
        assertThat(paakrevdeVedlegg.get(2).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo(), is("oppvarming"));
        assertThat(paakrevdeVedlegg.get(3).getType(), is("student"));
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(4).getType(), is("nedbetalingsplan"));
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo(), is("avdraglaan"));
        assertThat(paakrevdeVedlegg.get(5).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo(), is("barnehage"));
        assertThat(paakrevdeVedlegg.get(6).getType(), is("kontooversikt"));
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo(), is("brukskonto"));
    }

    @Test
    public void finnPaakrevdeVedleggForOkonomiHarBostotteDersomViHarSamtykkeMenHentingenHarFeilet() {
        JsonSoknad soknad = new JsonSoknad()
                .withDriftsinformasjon(new JsonDriftsinformasjon()
                        .withStotteFraHusbankenFeilet(true))
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
                                                .withVerdi(true))))));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(soknad);

        assertThat(paakrevdeVedlegg.size(), is(8));
        assertThat(paakrevdeVedlegg.get(0).getType(), is("salgsoppgjor"));
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo(), is("eiendom"));
        assertThat(paakrevdeVedlegg.get(1).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo(), is("strom"));
        assertThat(paakrevdeVedlegg.get(2).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo(), is("oppvarming"));
        assertThat(paakrevdeVedlegg.get(3).getType(), is("bostotte"));
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(4).getType(), is("student"));
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(5).getType(), is("nedbetalingsplan"));
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo(), is("avdraglaan"));
        assertThat(paakrevdeVedlegg.get(6).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo(), is("barnehage"));
        assertThat(paakrevdeVedlegg.get(7).getType(), is("kontooversikt"));
        assertThat(paakrevdeVedlegg.get(7).getTilleggsinfo(), is("brukskonto"));
    }

    private List<JsonOkonomioversiktInntekt> lagInntekter() {
        List<JsonOkonomioversiktInntekt> inntekter = new ArrayList<>();
        inntekter.add(new JsonOkonomioversiktInntekt().withType(BOSTOTTE));
        inntekter.add(new JsonOkonomioversiktInntekt().withType(BOSTOTTE));
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