package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.soknad.JsonData;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomiopplysninger;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomioversikt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonNordiskBorger;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonStatsborgerskap;
import no.nav.sbl.soknadsosialhjelp.soknad.utdanning.JsonUtdanning;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;
import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;
import static no.nav.sbl.soknadsosialhjelp.json.VedleggsforventningMaster.*;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class VedleggsforventningMasterTest {

    @Test
    public void finnPaakrevdeVedleggLeggerKunTilSkattemeldingHvisAltAnnetMangler() {
        JsonInternalSoknad internalSoknad = new JsonInternalSoknad()
                .withSoknad(new JsonSoknad()
                        .withData(new JsonData())
                        .withDriftsinformasjon(""));

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
    public void finnPaakrevdeVedleggForUtdanningKreverVedleggHvisStudent() {
        JsonUtdanning utdanning = new JsonUtdanning().withErStudent(true);

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForUtdanning(utdanning);

        JsonVedlegg vedlegg = paakrevdeVedlegg.get(0);
        assertThat(vedlegg.getType(), is("student"));
        assertThat(vedlegg.getTilleggsinfo(), is("vedtak"));
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
        JsonOkonomi okonomi = new JsonOkonomi()
                .withOversikt(new JsonOkonomioversikt()
                        .withInntekt(lagInntekter())
                        .withUtgift(lagUtgifter())
                        .withFormue(lagFormue()))
                .withOpplysninger(new JsonOkonomiopplysninger()
                        .withUtbetaling(lagUtbetalinger())
                        .withUtgift(lagOpplysningUtgifter()));

        List<JsonVedlegg> paakrevdeVedlegg = finnPaakrevdeVedleggForOkonomi(okonomi);

        assertThat(paakrevdeVedlegg.size(), is(7));
        assertThat(paakrevdeVedlegg.get(0).getType(), is("salgsoppgjor"));
        assertThat(paakrevdeVedlegg.get(0).getTilleggsinfo(), is("eiendom"));
        assertThat(paakrevdeVedlegg.get(1).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(1).getTilleggsinfo(), is("strom"));
        assertThat(paakrevdeVedlegg.get(2).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(2).getTilleggsinfo(), is("oppvarming"));
        assertThat(paakrevdeVedlegg.get(3).getType(), is("bostotte"));
        assertThat(paakrevdeVedlegg.get(3).getTilleggsinfo(), is("vedtak"));
        assertThat(paakrevdeVedlegg.get(4).getType(), is("nedbetalingsplan"));
        assertThat(paakrevdeVedlegg.get(4).getTilleggsinfo(), is("avdraglaan"));
        assertThat(paakrevdeVedlegg.get(5).getType(), is("faktura"));
        assertThat(paakrevdeVedlegg.get(5).getTilleggsinfo(), is("barnehage"));
        assertThat(paakrevdeVedlegg.get(6).getType(), is("kontooversikt"));
        assertThat(paakrevdeVedlegg.get(6).getTilleggsinfo(), is("brukskonto"));
    }

    private JsonArbeidsforhold opprettArbeidsforholdMedTom() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate tom = now().plusDays(7);
        return new JsonArbeidsforhold()
                .withFom("2005-05-01")
                .withTom(tom.format(formatter));
    }

    private List<JsonOkonomioversiktInntekt> lagInntekter() {
        List<JsonOkonomioversiktInntekt> inntekter = new ArrayList<>();
        inntekter.add(new JsonOkonomioversiktInntekt().withType("bostotte"));
        inntekter.add(new JsonOkonomioversiktInntekt().withType("bostotte"));
        return inntekter;
    }

    private List<JsonOkonomioversiktUtgift> lagUtgifter() {
        List<JsonOkonomioversiktUtgift> utgifter = new ArrayList<>();
        utgifter.add(new JsonOkonomioversiktUtgift().withType("boliglanAvdrag"));
        utgifter.add(new JsonOkonomioversiktUtgift().withType("barnehage"));
        utgifter.add(new JsonOkonomioversiktUtgift().withType("barnehage"));
        return utgifter;
    }

    private List<JsonOkonomioversiktFormue> lagFormue() {
        List<JsonOkonomioversiktFormue> formuer = new ArrayList<>();
        formuer.add(new JsonOkonomioversiktFormue().withType("kjoretoy"));
        formuer.add(new JsonOkonomioversiktFormue().withType("kjoretoy"));
        formuer.add(new JsonOkonomioversiktFormue().withType("brukskonto"));
        return formuer;
    }

    private List<JsonOkonomiOpplysningUtbetaling> lagUtbetalinger() {
        List<JsonOkonomiOpplysningUtbetaling> utbetalinger = new ArrayList<>();
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType("salg"));
        utbetalinger.add(new JsonOkonomiOpplysningUtbetaling().withType("salg"));
        return utbetalinger;
    }

    private List<JsonOkonomiOpplysningUtgift> lagOpplysningUtgifter() {
        List<JsonOkonomiOpplysningUtgift> utgifter = new ArrayList<>();
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType("strom"));
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType("oppvarming"));
        utgifter.add(new JsonOkonomiOpplysningUtgift().withType("strom"));
        return utgifter;
    }
}