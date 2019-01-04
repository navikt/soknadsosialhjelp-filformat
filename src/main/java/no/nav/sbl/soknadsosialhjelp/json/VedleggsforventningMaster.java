package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.soknad.*;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.*;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia;
import no.nav.sbl.soknadsosialhjelp.soknad.utdanning.JsonUtdanning;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDate.now;

public class VedleggsforventningMaster {

    public static List<JsonVedlegg> finnPaakrevdeVedlegg(JsonInternalSoknad internalSoknad) {
        final List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (internalSoknad == null || internalSoknad.getSoknad() == null || internalSoknad.getSoknad().getData() == null) {
            return null;
        }
        final JsonData data = internalSoknad.getSoknad().getData();

        finnPaakrevdeVedleggForPersonalia(paakrevdeVedlegg, data.getPersonalia());
        finnPaakrevdeVedleggForArbeid(paakrevdeVedlegg, data.getArbeid());
        finnPaakrevdeVedleggForUtdanning(paakrevdeVedlegg, data.getUtdanning());
        finnPaakrevdeVedleggForFamilie(paakrevdeVedlegg, data.getFamilie());
        finnPaakrevdeVedleggForBosituasjon(paakrevdeVedlegg, data.getBosituasjon());
        finnPaakrevdeVedleggForOkonomi(paakrevdeVedlegg, data.getOkonomi());

        paakrevdeVedlegg.add(new JsonVedlegg().withType("skattemelding").withTilleggsinfo("skattemelding"));

        return paakrevdeVedlegg;
    }

    static void finnPaakrevdeVedleggForPersonalia(List<JsonVedlegg> paakrevdeVedlegg, JsonPersonalia personalia) {
        if (personalia != null) {
            if (personalia.getStatsborgerskap() != null && !"NOR".equals(personalia.getStatsborgerskap().getVerdi())
                    && personalia.getNordiskBorger() != null && Boolean.FALSE.equals(personalia.getNordiskBorger().getVerdi())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("oppholdstillatel").withTilleggsinfo("oppholdstillatel"));
            }
        }
    }

    static void finnPaakrevdeVedleggForArbeid(List<JsonVedlegg> paakrevdeVedlegg, JsonArbeid arbeid) {
        if (arbeid != null && arbeid.getForhold() != null && !arbeid.getForhold().isEmpty()) {
            final List<JsonArbeidsforhold> alleArbeidsforhold = arbeid.getForhold();
            for (JsonArbeidsforhold arbeidsforhold : alleArbeidsforhold) {
                String tom = arbeidsforhold.getTom();
                if (tom == null) {
                    paakrevdeVedlegg.add(new JsonVedlegg().withType("lonnslipp").withTilleggsinfo("arbeid"));
                } else if (!tom.isEmpty() && isBeforeOneMonthAheadInTime(tom)) {
                    paakrevdeVedlegg.add(new JsonVedlegg().withType("sluttoppgjor").withTilleggsinfo("arbeid"));
                }
            }
        }
    }

    static void finnPaakrevdeVedleggForUtdanning(List<JsonVedlegg> paakrevdeVedlegg, JsonUtdanning utdanning) {
        if (utdanning != null && utdanning.getErStudent()) {
            paakrevdeVedlegg.add(new JsonVedlegg().withType("student").withTilleggsinfo("vedtak"));
        }
    }

    static void finnPaakrevdeVedleggForFamilie(List<JsonVedlegg> paakrevdeVedlegg, JsonFamilie familie) {
        if (familie != null && familie.getForsorgerplikt() != null) {
            final JsonBarnebidrag barnebidrag = familie.getForsorgerplikt().getBarnebidrag();
            if (barnebidrag != null && (JsonBarnebidrag.Verdi.BETALER.equals(barnebidrag.getVerdi())
                    || JsonBarnebidrag.Verdi.BEGGE.equals(barnebidrag.getVerdi()))) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("barnebidrag").withTilleggsinfo("betaler"));
            } else if (barnebidrag != null && (JsonBarnebidrag.Verdi.MOTTAR.equals(barnebidrag.getVerdi())
                    || JsonBarnebidrag.Verdi.BEGGE.equals(barnebidrag.getVerdi()))) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("barnebidrag").withTilleggsinfo("mottar"));
            }
            final List<JsonAnsvar> forsorgerAnsvar = familie.getForsorgerplikt().getAnsvar();
            for (JsonAnsvar ansvar : forsorgerAnsvar) {
                if (ansvar.getErFolkeregistrertSammen() != null && !ansvar.getErFolkeregistrertSammen().getVerdi()) {
                    if (ansvar.getSamvarsgrad() != null && ansvar.getSamvarsgrad().getVerdi() <= 50) {
                        paakrevdeVedlegg.add(new JsonVedlegg().withType("samvarsavtale").withTilleggsinfo("barn"));
                    }
                }
            }
        }
    }

    static void finnPaakrevdeVedleggForBosituasjon(List<JsonVedlegg> paakrevdeVedlegg, JsonBosituasjon bosituasjon) {
        if (bosituasjon != null && bosituasjon.getBotype() != null) {
            if (JsonBosituasjon.Botype.LEIER.equals(bosituasjon.getBotype())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("husleiekontrakt"));
            } else if (JsonBosituasjon.Botype.KOMMUNAL.equals(bosituasjon.getBotype())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("kommunal"));
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomi(List<JsonVedlegg> paakrevdeVedlegg, JsonOkonomi okonomi) {
        if (okonomi != null) {
            final JsonOkonomiopplysninger opplysninger = okonomi.getOpplysninger();
            if (opplysninger != null) {
                if (opplysninger.getUtbetaling() != null && !opplysninger.getUtbetaling().isEmpty()) {
                    finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(paakrevdeVedlegg, opplysninger.getUtbetaling());
                }
                if (opplysninger.getUtgift() != null && !opplysninger.getUtgift().isEmpty()) {
                    finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(paakrevdeVedlegg, opplysninger.getUtgift());
                }
            }

            final JsonOkonomioversikt oversikt = okonomi.getOversikt();
            if (oversikt != null) {
                if (oversikt.getInntekt() != null && !oversikt.getInntekt().isEmpty()) {
                    finnPaakrevdeVedleggForOkonomiOversiktInntekt(paakrevdeVedlegg, oversikt.getInntekt());
                }
                if (oversikt.getUtgift() != null && !oversikt.getUtgift().isEmpty()) {
                    finnPaakrevdeVedleggForOkonomiOversiktUtgift(paakrevdeVedlegg, oversikt.getUtgift());
                }
                if (oversikt.getFormue() != null && !oversikt.getFormue().isEmpty()) {
                    finnPaakrevdeVedleggForOkonomiOversiktFormue(paakrevdeVedlegg, oversikt.getFormue());
                }
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(List<JsonVedlegg> paakrevdeVedlegg, List<JsonOkonomiOpplysningUtbetaling> okonomiOpplysningUtbetalinger) {
        for (JsonOkonomiOpplysningUtbetaling utbetaling : okonomiOpplysningUtbetalinger) {
            if (utbetaling == null) {
                continue;
            }
            if ("utbytte".equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("utbytte"));
            } else if ("salg".equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("salgsoppgjor").withTilleggsinfo("eiendom"));
            } else if ("forsikring".equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("forsikringsutbetaling"));
            } else if ("annen".equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetinntekter"));
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(List<JsonVedlegg> paakrevdeVedlegg, List<JsonOkonomiOpplysningUtgift> okonomiOpplysningUtgifter) {
        for (JsonOkonomiOpplysningUtgift utgift : okonomiOpplysningUtgifter) {
            if (utgift == null) {
                continue;
            }
            if ("strom".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("strom"));
            } else if ("kommunalAvgift".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("kommunaleavgifter"));
            } else if ("oppvarming".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("oppvarming"));
            } else if ("annenBoutgift".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetboutgift"));
            } else if ("barnFritidsaktiviteter".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("fritidsaktivitet"));
            } else if ("barnTannregulering".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("tannbehandling"));
            } else if ("annenBarneutgift".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("annetbarnutgift"));
            } else if ("annen".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("annet").withTilleggsinfo("annet"));
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomiOversiktInntekt(List<JsonVedlegg> paakrevdeVedlegg, List<JsonOkonomioversiktInntekt> okonomioversiktInntekter) {
        for (JsonOkonomioversiktInntekt inntekt : okonomioversiktInntekter) {
            if (inntekt == null) {
                continue;
            }
            if ("bostotte".equals(inntekt.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("bostotte").withTilleggsinfo("vedtak"));
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomiOversiktUtgift(List<JsonVedlegg> paakrevdeVedlegg, List<JsonOkonomioversiktUtgift> okonomioversiktUtgifter) {
        for (JsonOkonomioversiktUtgift utgift : okonomioversiktUtgifter) {
            if (utgift == null) {
                continue;
            }
            if ("husleie".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("husleie"));
            } else if ("boliglanAvdrag".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("nedbetalingsplan").withTilleggsinfo("avdraglaan"));
            } else if ("barnehage".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("barnehage"));
            } else if ("sfo".equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("sfo"));
            }
        }
    }

    static void finnPaakrevdeVedleggForOkonomiOversiktFormue(List<JsonVedlegg> paakrevdeVedlegg, List<JsonOkonomioversiktFormue> okonomioversiktFormuer) {
        for (JsonOkonomioversiktFormue formue : okonomioversiktFormuer) {
            if (formue == null) {
                continue;
            }
            if ("bolig".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kjopekontrakt").withTilleggsinfo("kjopekontrakt"));
            } else if ("kjoretoy".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("kjoretoy"));
            } else if ("campingvogn".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("campingvogn"));
            } else if ("fritidseiendom".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("fritidseiendom"));
            } else if ("annet".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetverdi"));
            } else if ("brukskonto".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("brukskonto"));
            } else if ("bsu".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("bsu"));
            } else if ("sparekonto".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("sparekonto"));
            } else if ("livsforsikringssparedel".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("livsforsikring"));
            } else if ("verdipapirer".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("aksjer"));
            } else if ("belop".equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("annet"));
            }
        }
    }

    private static boolean isBeforeOneMonthAheadInTime(String datoSomTekst) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(datoSomTekst, formatter);
        return date.isBefore(now().plusMonths(1));
    }
}
