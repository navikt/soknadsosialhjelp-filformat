package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.soknad.JsonData;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid;
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeidsforhold;
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonAnsvar;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonBarnebidrag;
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonFamilie;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomiopplysninger;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomioversikt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomibekreftelse;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt;
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift;
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia;
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.time.LocalDate.now;
import static no.nav.sbl.soknadsosialhjelp.json.SoknadJsonTyper.*;

@SuppressWarnings("WeakerAccess")
public class VedleggsforventningMaster {

    public static List<JsonVedlegg> finnPaakrevdeVedlegg(JsonInternalSoknad internalSoknad) {
        final List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        if (internalSoknad == null || internalSoknad.getSoknad() == null || internalSoknad.getSoknad().getData() == null) {
            return null;
        }
        final JsonData data = internalSoknad.getSoknad().getData();

        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForPersonalia(data.getPersonalia()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForArbeid(internalSoknad));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForFamilie(data.getFamilie()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForBosituasjon(data.getBosituasjon()));
        paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomi(internalSoknad.getSoknad()));

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

    public static List<JsonVedlegg> finnPaakrevdeVedleggForArbeid(JsonInternalSoknad jsonInternalSoknad) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        JsonArbeid arbeid = jsonInternalSoknad.getSoknad().getData().getArbeid();
        boolean utbetalingerFeiletFraSkatt = jsonInternalSoknad.getSoknad().getDriftsinformasjon().getInntektFraSkatteetatenFeilet();
        boolean manglerSamtykke = !sjekkOmViHarSamtykke(jsonInternalSoknad.getSoknad().getData().getOkonomi(), UTBETALING_SKATTEETATEN_SAMTYKKE);
        if ((utbetalingerFeiletFraSkatt || manglerSamtykke) && arbeid != null && arbeid.getForhold() != null && !arbeid.getForhold().isEmpty()) {
            List<JsonArbeidsforhold> alleArbeidsforhold = arbeid.getForhold();
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

    private static boolean sjekkOmViHarSamtykke(JsonOkonomi okonomi, String key) {
        return okonomi.getOpplysninger().getBekreftelse().stream()
                .filter(bekreftelse -> bekreftelse.getType().equals(key))
                .anyMatch(JsonOkonomibekreftelse::getVerdi);

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

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomi(JsonSoknad soknad) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        JsonOkonomi okonomi = soknad.getData().getOkonomi();
        if (okonomi != null) {
            final JsonOkonomiopplysninger opplysninger = okonomi.getOpplysninger();
            if (opplysninger != null) {
                if (opplysninger.getUtbetaling() != null && !opplysninger.getUtbetaling().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(soknad));
                }
                if (opplysninger.getUtgift() != null && !opplysninger.getUtgift().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(opplysninger.getUtgift()));
                }
            }

            final JsonOkonomioversikt oversikt = okonomi.getOversikt();
            if (oversikt != null) {
                if (oversikt.getInntekt() != null && !oversikt.getInntekt().isEmpty()) {
                    paakrevdeVedlegg.addAll(finnPaakrevdeVedleggForOkonomiOversiktInntekt(soknad));
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

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(JsonSoknad soknad) {
        List<JsonOkonomiOpplysningUtbetaling> utbetalinger = soknad.getData().getOkonomi().getOpplysninger().getUtbetaling();
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        for (JsonOkonomiOpplysningUtbetaling utbetaling : utbetalinger) {
            if (utbetaling == null) {
                continue;
            }
            if (UTBETALING_UTBYTTE.equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("utbytte"));
            } else if (UTBETALING_SALG.equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("salgsoppgjor").withTilleggsinfo("eiendom"));
            } else if (UTBETALING_FORSIKRING.equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("forsikringsutbetaling"));
            } else if (UTBETALING_ANNET.equals(utbetaling.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetinntekter"));
            } else if (UTBETALING_HUSBANKEN.equals(utbetaling.getType())) {
                boolean harBostotteSamtykke = sjekkOmViHarSamtykke(soknad.getData().getOkonomi(), BOSTOTTE_SAMTYKKE);
                Boolean harBostotteFeilet = soknad.getDriftsinformasjon().getStotteFraHusbankenFeilet();
                if (!harBostotteSamtykke || harBostotteFeilet) {
                    paakrevdeVedlegg.add(new JsonVedlegg().withType("bostotte").withTilleggsinfo("vedtak"));
                }
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
            if (UTGIFTER_STROM.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("strom"));
            } else if (UTGIFTER_KOMMUNAL_AVGIFT.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("kommunaleavgifter"));
            } else if (UTGIFTER_OPPVARMING.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("oppvarming"));
            } else if (UTGIFTER_ANNET_BO.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetboutgift"));
            } else if (UTGIFTER_BARN_FRITIDSAKTIVITETER.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("fritidsaktivitet"));
            } else if (UTGIFTER_BARN_TANNREGULERING.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("tannbehandling"));
            } else if (UTGIFTER_ANNET_BARN.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("annetbarnutgift"));
            } else if (UTGIFTER_ANDRE_UTGIFTER.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("annet").withTilleggsinfo("annet"));
            }
        }
        return paakrevdeVedlegg.stream()
                .distinct()
                .collect(Collectors.toList());
    }

    static List<JsonVedlegg> finnPaakrevdeVedleggForOkonomiOversiktInntekt(JsonSoknad soknad) {
        List<JsonVedlegg> paakrevdeVedlegg = new ArrayList<>();
        List<JsonOkonomioversiktInntekt> okonomioversiktInntekter = soknad.getData().getOkonomi().getOversikt().getInntekt();
        for (JsonOkonomioversiktInntekt inntekt : okonomioversiktInntekter) {
            if (inntekt == null) {
                continue;
            }
            if (STUDIELAN.equals(inntekt.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("student").withTilleggsinfo("vedtak"));
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
            if (UTGIFTER_HUSLEIE.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("husleie"));
            } else if (UTGIFTER_BOLIGLAN_AVDRAG.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("nedbetalingsplan").withTilleggsinfo("avdraglaan"));
            } else if (UTGIFTER_BARNEHAGE.equals(utgift.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("faktura").withTilleggsinfo("barnehage"));
            } else if (UTGIFTER_SFO.equals(utgift.getType())) {
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
            if (FORMUE_BRUKSKONTO.equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("brukskonto"));
            } else if (FORMUE_BSU.equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("bsu"));
            } else if (FORMUE_SPAREKONTO.equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("sparekonto"));
            } else if (FORMUE_LIVSFORSIKRING.equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("livsforsikring"));
            } else if (FORMUE_VERDIPAPIRER.equals(formue.getType())) {
                paakrevdeVedlegg.add(new JsonVedlegg().withType("kontooversikt").withTilleggsinfo("aksjer"));
            } else if (FORMUE_ANNET.equals(formue.getType())) {
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
