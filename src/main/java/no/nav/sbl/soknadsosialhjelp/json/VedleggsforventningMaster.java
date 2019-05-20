package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.soknad.JsonData;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.*;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.*;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia;
import no.nav.sbl.soknadsosialhjelp.soknad.utdanning.JsonUtdanning;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;

public class VedleggsforventningMaster {

    public static List<JsonVedlegg> finnPaakrevdeVedlegg(JsonInternalSoknad internalSoknad) {
        final List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (internalSoknad == null || internalSoknad.getSoknad() == null || internalSoknad.getSoknad().getData() == null) {
            return null;
        }
        final JsonData data = internalSoknad.getSoknad().getData();

        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForPersonalia(data.getPersonalia()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForArbeid(data.getArbeid()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForUtdanning(data.getUtdanning()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForFamilie(data.getFamilie()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForBosituasjon(data.getBosituasjon()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomi(data.getOkonomi()));

        paakrevdeVedlegg.add(new JsonVedlegg().withType("skattemelding").withTilleggsinfo("skattemelding"));
        paakrevdeVedlegg.add(new JsonVedlegg().withType("annet").withTilleggsinfo("annet"));

        return paakrevdeVedlegg;
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForPersonalia(JsonPersonalia personalia) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (personalia != null) {
            if (personalia.getNordiskBorger() == null || Boolean.FALSE.equals(personalia.getNordiskBorger().getVerdi())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("oppholdstillatel").withTilleggsinfo("oppholdstillatel"));
            }
        }
        return paakrevdeVedlegg;
    }

    public static List<JsonVedlegg> finnPaakrevdeVedleggForArbeid(JsonArbeid arbeid) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (arbeid != null && arbeid.getForhold() != null && !arbeid.getForhold().isEmpty()) {
            final List<JsonArbeidsforhold> alleArbeidsforhold = arbeid.getForhold();
            for (JsonArbeidsforhold arbeidsforhold : alleArbeidsforhold) {
                String tom = arbeidsforhold.getTom();
                if (tom == null || !isWithinOneMonthAheadInTime(tom)) {
                    paakrevdeVedlegg.add(new JsonVedlegg().withType("lonnslipp").withTilleggsinfo("arbeid"));
                } else if (isWithinOneMonthAheadInTime(tom)) {
                    paakrevdeVedlegg.add(new JsonVedlegg().withType("sluttoppgjor").withTilleggsinfo("arbeid"));
                }
            }
        }
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForUtdanning(JsonUtdanning utdanning) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (utdanning != null && utdanning.getErStudent() != null && utdanning.getErStudent()) {
            paakrevdeVedlegg.add(new JsonVedlegg().withType("student").withTilleggsinfo("vedtak"));
        }
        return paakrevdeVedlegg;
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForFamilie(JsonFamilie familie) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (familie != null && familie.getForsorgerplikt() != null) {
            final JsonBarnebidrag barnebidrag = familie.getForsorgerplikt().getBarnebidrag();
            if (barnebidrag != null && (JsonBarnebidrag.Verdi.BETALER.equals(barnebidrag.getVerdi())
                    || JsonBarnebidrag.Verdi.BEGGE.equals(barnebidrag.getVerdi()))) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("barnebidrag").withTilleggsinfo("betaler"));
            }
            if (barnebidrag != null && (JsonBarnebidrag.Verdi.MOTTAR.equals(barnebidrag.getVerdi())
                    || JsonBarnebidrag.Verdi.BEGGE.equals(barnebidrag.getVerdi()))) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("barnebidrag").withTilleggsinfo("mottar"));
            }
            final List<JsonAnsvar> forsorgerAnsvar = familie.getForsorgerplikt().getAnsvar();
            for (JsonAnsvar ansvar : forsorgerAnsvar) {
                if (ansvar.getErFolkeregistrertSammen() != null && !ansvar.getErFolkeregistrertSammen().getVerdi()) {
                    if (ansvar.getSamvarsgrad() != null && ansvar.getSamvarsgrad().getVerdi() <= 50 &&
                            ansvar.getSamvarsgrad().getVerdi() != 0) {
                        paakrevdeVedlegg.add(new JsonVedlegg().withType("samvarsavtale").withTilleggsinfo("barn"));
                        break;
                    }
                }
            }
        }
        return paakrevdeVedlegg;
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForBosituasjon(JsonBosituasjon bosituasjon) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (bosituasjon != null && bosituasjon.getBotype() != null) {
            if (JsonBosituasjon.Botype.LEIER.equals(bosituasjon.getBotype())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("husleiekontrakt"));
            } else if (JsonBosituasjon.Botype.KOMMUNAL.equals(bosituasjon.getBotype())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("kommunal"));
            }
        }
        return paakrevdeVedlegg;
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomi(JsonOkonomi okonomi) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (okonomi != null) {
            final JsonOkonomiopplysninger opplysninger = okonomi.getOpplysninger();
            if (opplysninger != null) {
                if (opplysninger.getUtbetaling() != null && !opplysninger.getUtbetaling().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(opplysninger.getUtbetaling()));
                }
                if (opplysninger.getUtgift() != null && !opplysninger.getUtgift().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(opplysninger.getUtgift()));
                }
            }

            final JsonOkonomioversikt oversikt = okonomi.getOversikt();
            if (oversikt != null) {
                if (oversikt.getInntekt() != null && !oversikt.getInntekt().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOversiktInntekt(oversikt.getInntekt()));
                }
                if (oversikt.getUtgift() != null && !oversikt.getUtgift().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOversiktUtgift(oversikt.getUtgift()));
                }
                if (oversikt.getFormue() != null && !oversikt.getFormue().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOversiktFormue(oversikt.getFormue()));
                }
            }
        }
        return paakrevdeVedlegg;
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(List<JsonOkonomiOpplysningUtbetaling> okonomiOpplysningUtbetalinger) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
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
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(List<JsonOkonomiOpplysningUtgift> okonomiOpplysningUtgifter) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
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
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOversiktInntekt(List<JsonOkonomioversiktInntekt> okonomioversiktInntekter) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        for (JsonOkonomioversiktInntekt inntekt : okonomioversiktInntekter) {
            if (inntekt == null) {
                continue;
            }
            if ("bostotte".equals(inntekt.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("bostotte").withTilleggsinfo("vedtak"));
            }
        }
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOversiktUtgift(List<JsonOkonomioversiktUtgift> okonomioversiktUtgifter) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
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
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOversiktFormue(List<JsonOkonomioversiktFormue> okonomioversiktFormuer) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        for (JsonOkonomioversiktFormue formue : okonomioversiktFormuer) {
            if (formue == null) {
                continue;
            }
            if ("brukskonto".equals(formue.getType())) {
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
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    private static boolean isWithinOneMonthAheadInTime(String datoSomTekst) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.parse(datoSomTekst, formatter);
        return date.isBefore(now().plusMonths(1).plusDays(1));
    }
}
