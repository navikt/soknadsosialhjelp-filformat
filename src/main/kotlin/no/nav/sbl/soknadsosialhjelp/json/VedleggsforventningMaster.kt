package no.nav.sbl.soknadsosialhjelp.json

import no.nav.sbl.soknadsosialhjelp.soknad.JsonData
import no.nav.sbl.soknadsosialhjelp.soknad.JsonInternalSoknad
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknad
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonBarnebidrag
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonFamilie
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedlegg
import java.time.LocalDate
import java.time.format.DateTimeFormatter

object VedleggsforventningMaster {
    @JvmStatic
    fun finnPaakrevdeVedlegg(internalSoknad: JsonInternalSoknad?): List<JsonVedlegg>? {
        val soknad = internalSoknad?.soknad ?: return null
        val data = soknad.data ?: return null
        if (data.soknadstype == JsonData.Soknadstype.KORT) return null
        return mutableListOf<JsonVedlegg>().apply {
            addAll(finnPaakrevdeVedleggForPersonalia(data.personalia))
            addAll(finnPaakrevdeVedleggForArbeid(internalSoknad))
            addAll(finnPaakrevdeVedleggForFamilie(data.familie))
            addAll(finnPaakrevdeVedleggForBosituasjon(data.bosituasjon))
            addAll(finnPaakrevdeVedleggForOkonomi(soknad))
            add(JsonVedlegg().withType("skattemelding").withTilleggsinfo("skattemelding"))
            add(JsonVedlegg().withType("annet").withTilleggsinfo("annet"))
        }
    }

    @JvmStatic
    fun finnPaakrevdeVedleggForPersonalia(personalia: JsonPersonalia?): List<JsonVedlegg> {
        if (personalia == null) return emptyList()
        val nordiskBorger = personalia?.nordiskBorger
        return if (nordiskBorger == null || nordiskBorger.verdi == false) {
            listOf(JsonVedlegg().withType("oppholdstillatel").withTilleggsinfo("oppholdstillatel"))
        } else {
            emptyList()
        }
    }

    @JvmStatic
    fun finnPaakrevdeVedleggForArbeid(jsonInternalSoknad: JsonInternalSoknad): List<JsonVedlegg> {
        val soknad = jsonInternalSoknad.soknad!!
        val data = soknad.data!!
        val arbeid: JsonArbeid? = data.arbeid
        val requiresDocumentation = soknad.driftsinformasjon!!.inntektFraSkatteetatenFeilet == true ||
            !sjekkOmViHarSamtykke(data.okonomi!!, SoknadJsonTyper.UTBETALING_SKATTEETATEN_SAMTYKKE)
        return if (requiresDocumentation && !arbeid?.forhold.isNullOrEmpty()) {
            arbeid!!.forhold!!.map { arbeidsforhold ->
                if (arbeidsforhold.tom == null || !isWithinOneMonthAheadInTime(arbeidsforhold.tom!!)) {
                    JsonVedlegg().withType("lonnslipp").withTilleggsinfo("arbeid")
                } else {
                    JsonVedlegg().withType("sluttoppgjor").withTilleggsinfo("arbeid")
                }
            }.distinct()
        } else {
            emptyList()
        }
    }

    private fun sjekkOmViHarSamtykke(okonomi: JsonOkonomi, key: String): Boolean =
        okonomi.opplysninger!!.bekreftelse!!.any { it.type == key && it.verdi == true }

    @JvmStatic
    fun finnPaakrevdeVedleggForFamilie(familie: JsonFamilie?): List<JsonVedlegg> {
        val forsorgerplikt = familie?.forsorgerplikt ?: return emptyList()
        return mutableListOf<JsonVedlegg>().apply {
            when (forsorgerplikt.barnebidrag?.verdi) {
                JsonBarnebidrag.Verdi.BETALER -> add(JsonVedlegg().withType("barnebidrag").withTilleggsinfo("betaler"))
                JsonBarnebidrag.Verdi.MOTTAR -> add(JsonVedlegg().withType("barnebidrag").withTilleggsinfo("mottar"))
                JsonBarnebidrag.Verdi.BEGGE -> {
                    add(JsonVedlegg().withType("barnebidrag").withTilleggsinfo("betaler"))
                    add(JsonVedlegg().withType("barnebidrag").withTilleggsinfo("mottar"))
                }
                else -> Unit
            }
            if (forsorgerplikt.ansvar!!.any {
                    it.erFolkeregistrertSammen?.verdi == false &&
                        it.samvarsgrad?.verdi?.let { verdi -> verdi in 1..50 } == true
                }
            ) {
                add(JsonVedlegg().withType("samvarsavtale").withTilleggsinfo("barn"))
            }
        }
    }

    @JvmStatic
    fun finnPaakrevdeVedleggForBosituasjon(bosituasjon: JsonBosituasjon?): List<JsonVedlegg> = when (bosituasjon?.botype) {
        JsonBosituasjon.Botype.LEIER -> listOf(JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("husleiekontrakt"))
        JsonBosituasjon.Botype.KOMMUNAL -> listOf(JsonVedlegg().withType("husleiekontrakt").withTilleggsinfo("kommunal"))
        else -> emptyList()
    }

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomi(soknad: JsonSoknad): List<JsonVedlegg> {
        val okonomi = soknad.data!!.okonomi ?: return emptyList()
        return mutableListOf<JsonVedlegg>().apply {
            okonomi.opplysninger?.let { opplysninger ->
                if (!opplysninger.utbetaling.isNullOrEmpty()) addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(soknad))
                if (!opplysninger.utgift.isNullOrEmpty()) addAll(finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(opplysninger.utgift!!))
            }
            okonomi.oversikt?.let { oversikt ->
                if (!oversikt.inntekt.isNullOrEmpty()) addAll(finnPaakrevdeVedleggForOkonomiOversiktInntekt(soknad))
                if (!oversikt.utgift.isNullOrEmpty()) addAll(finnPaakrevdeVedleggForOkonomiOversiktUtgift(oversikt.utgift!!))
                if (!oversikt.formue.isNullOrEmpty()) addAll(finnPaakrevdeVedleggForOkonomiOversiktFormue(oversikt.formue!!))
            }
        }
    }

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomiOpplysningerUtbetaling(soknad: JsonSoknad): List<JsonVedlegg> =
        soknad.data!!.okonomi!!.opplysninger!!.utbetaling!!.mapNotNull { utbetaling: JsonOkonomiOpplysningUtbetaling? ->
            when (utbetaling?.type) {
                SoknadJsonTyper.UTBETALING_UTBYTTE -> JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("utbytte")
                SoknadJsonTyper.UTBETALING_SALG -> JsonVedlegg().withType("salgsoppgjor").withTilleggsinfo("eiendom")
                SoknadJsonTyper.UTBETALING_FORSIKRING -> JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("forsikringsutbetaling")
                SoknadJsonTyper.UTBETALING_ANNET -> JsonVedlegg().withType("dokumentasjon").withTilleggsinfo("annetinntekter")
                SoknadJsonTyper.UTBETALING_HUSBANKEN -> {
                    if (!sjekkOmViHarSamtykke(soknad.data!!.okonomi!!, SoknadJsonTyper.BOSTOTTE_SAMTYKKE) ||
                        soknad.driftsinformasjon!!.stotteFraHusbankenFeilet == true
                    ) JsonVedlegg().withType(SoknadJsonTyper.UTBETALING_HUSBANKEN).withTilleggsinfo("vedtak") else null
                }
                else -> null
            }
        }.distinct()

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomiOpplysningerUtgift(utgifter: List<JsonOkonomiOpplysningUtgift>): List<JsonVedlegg> =
        utgifter.mapNotNull { utgift ->
            val tilleggsinfo = when (utgift?.type) {
                SoknadJsonTyper.UTGIFTER_STROM -> "strom"
                SoknadJsonTyper.UTGIFTER_KOMMUNAL_AVGIFT -> "kommunaleavgifter"
                SoknadJsonTyper.UTGIFTER_OPPVARMING -> "oppvarming"
                SoknadJsonTyper.UTGIFTER_ANNET_BO -> "annetboutgift"
                SoknadJsonTyper.UTGIFTER_BARN_FRITIDSAKTIVITETER -> "fritidsaktivitet"
                SoknadJsonTyper.UTGIFTER_BARN_TANNREGULERING -> "tannbehandling"
                SoknadJsonTyper.UTGIFTER_ANNET_BARN -> "annetbarnutgift"
                else -> null
            }
            if (utgift?.type == SoknadJsonTyper.UTGIFTER_ANDRE_UTGIFTER) JsonVedlegg().withType("annet").withTilleggsinfo("annet")
            else tilleggsinfo?.let { JsonVedlegg().withType(if (it == "annetboutgift") "dokumentasjon" else "faktura").withTilleggsinfo(it) }
        }.distinct()

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomiOversiktInntekt(soknad: JsonSoknad): List<JsonVedlegg> =
        soknad.data!!.okonomi!!.oversikt!!.inntekt!!.mapNotNull {
            if (it?.type == SoknadJsonTyper.STUDIELAN) JsonVedlegg().withType("student").withTilleggsinfo("vedtak") else null
        }.distinct()

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomiOversiktUtgift(utgifter: List<JsonOkonomioversiktUtgift>): List<JsonVedlegg> =
        utgifter.mapNotNull { utgift ->
            when (utgift?.type) {
                SoknadJsonTyper.UTGIFTER_HUSLEIE -> JsonVedlegg().withType("faktura").withTilleggsinfo("husleie")
                SoknadJsonTyper.UTGIFTER_BOLIGLAN_AVDRAG -> JsonVedlegg().withType("nedbetalingsplan").withTilleggsinfo("avdraglaan")
                SoknadJsonTyper.UTGIFTER_BARNEHAGE -> JsonVedlegg().withType("faktura").withTilleggsinfo("barnehage")
                SoknadJsonTyper.UTGIFTER_SFO -> JsonVedlegg().withType("faktura").withTilleggsinfo("sfo")
                else -> null
            }
        }.distinct()

    @JvmStatic
    fun finnPaakrevdeVedleggForOkonomiOversiktFormue(formuer: List<JsonOkonomioversiktFormue>): List<JsonVedlegg> =
        formuer.mapNotNull { formue ->
            when (formue?.type) {
                SoknadJsonTyper.FORMUE_BRUKSKONTO -> "brukskonto"
                SoknadJsonTyper.FORMUE_BSU -> "bsu"
                SoknadJsonTyper.FORMUE_SPAREKONTO -> "sparekonto"
                SoknadJsonTyper.FORMUE_LIVSFORSIKRING -> "livsforsikring"
                SoknadJsonTyper.FORMUE_VERDIPAPIRER -> "aksjer"
                SoknadJsonTyper.FORMUE_ANNET -> "annet"
                else -> null
            }?.let { JsonVedlegg().withType("kontooversikt").withTilleggsinfo(it) }
        }.distinct()

    private fun isWithinOneMonthAheadInTime(datoSomTekst: String): Boolean =
        LocalDate.parse(datoSomTekst, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            .isBefore(LocalDate.now().plusMonths(1).plusDays(1))
}
