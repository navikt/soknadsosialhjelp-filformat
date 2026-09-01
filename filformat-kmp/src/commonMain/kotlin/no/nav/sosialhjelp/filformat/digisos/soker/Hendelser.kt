package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable
public data class TildeltNavKontor(
    override val hendelsestidspunkt: String,
    public val navKontor: String,
    override val type: String = "tildeltNavKontor",
) : Hendelse

@Serializable
public data class SoknadsStatus(
    override val hendelsestidspunkt: String,
    public val status: Status,
    override val type: String = "soknadsStatus",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        MOTTATT("MOTTATT"),
        UNDER_BEHANDLING("UNDER_BEHANDLING"),
        FERDIGBEHANDLET("FERDIGBEHANDLET"),
        BEHANDLES_IKKE("BEHANDLES_IKKE"),
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "SoknadsStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

@Serializable
public data class SaksStatus(
    override val hendelsestidspunkt: String,
    public val referanse: String,
    public val tittel: String? = null,
    public val status: Status? = null,
    override val type: String = "saksStatus",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        UNDER_BEHANDLING("UNDER_BEHANDLING"),
        IKKE_INNSYN("IKKE_INNSYN"),
        BEHANDLES_IKKE("BEHANDLES_IKKE"),
        FEILREGISTRERT("FEILREGISTRERT"),
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "SaksStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

@Serializable
public data class VedtakFattet(
    override val hendelsestidspunkt: String,
    public val saksreferanse: String,
    public val vedtaksfil: Vedtaksfil,
    public val utfall: Utfall? = null,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "vedtakFattet",
) : Hendelse {

    @Serializable
    public data class Vedtaksfil(
        public val referanse: Filreferanse,
    )

    @Serializable(with = UtfallSerializer::class)
    public enum class Utfall(public val jsonValue: String) {
        INNVILGET("INNVILGET"),
        DELVIS_INNVILGET("DELVIS_INNVILGET"),
        AVSLATT("AVSLATT"),
        AVVIST("AVVIST"),
        UKJENT("UKJENT"),
    }

    public object UtfallSerializer : UkjentTolerantEnumSerializer<Utfall>(
        "VedtakFattet.Utfall", Utfall.entries.toTypedArray(), Utfall.UKJENT, Utfall::jsonValue,
    )
}

@Serializable
public data class DokumentasjonEtterspurt(
    override val hendelsestidspunkt: String,
    public val dokumenter: List<Dokument>,
    public val forvaltningsbrev: Forvaltningsbrev? = null,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "dokumentasjonEtterspurt",
) : Hendelse {

    @Serializable
    public data class Dokument(
        public val dokumenttype: String,
        public val innsendelsesfrist: String,
        public val tilleggsinformasjon: String? = null,
        public val dokumentreferanse: String? = null,
    )
}

@Serializable
public data class ForelopigSvar(
    override val hendelsestidspunkt: String,
    public val forvaltningsbrev: Forvaltningsbrev,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "forelopigSvar",
) : Hendelse

@Serializable
public data class Utbetaling(
    override val hendelsestidspunkt: String,
    public val utbetalingsreferanse: String,
    public val saksreferanse: String? = null,
    public val rammevedtaksreferanse: String? = null,
    public val status: Status? = null,
    public val belop: Double? = null, // Double matches Java model; consumers convert to BigDecimal themselves
    public val beskrivelse: String? = null,
    public val forfallsdato: String? = null,
    public val utbetalingsdato: String? = null,
    public val fom: String? = null,
    public val tom: String? = null,
    public val annenMottaker: Boolean? = null,
    public val mottaker: String? = null,
    public val kontonummer: String? = null,
    public val utbetalingsmetode: String? = null,
    override val type: String = "utbetaling",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        PLANLAGT_UTBETALING("PLANLAGT_UTBETALING"),
        UTBETALT("UTBETALT"),
        STOPPET("STOPPET"),
        ANNULLERT("ANNULLERT"),
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Utbetaling.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

@Serializable
public data class Vilkar(
    override val hendelsestidspunkt: String,
    public val vilkarreferanse: String,
    public val saksreferanse: String? = null,
    public val utbetalingsreferanse: List<String>? = null,
    public val tittel: String? = null,
    public val beskrivelse: String? = null,
    public val status: Status? = null,
    override val type: String = "vilkar",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        RELEVANT("RELEVANT"),
        ANNULLERT("ANNULLERT"),
        OPPFYLT("OPPFYLT"), // deprecated, read as RELEVANT
        IKKE_OPPFYLT("IKKE_OPPFYLT"), // deprecated, read as RELEVANT
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Vilkar.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

@Serializable
public data class Dokumentasjonkrav(
    override val hendelsestidspunkt: String,
    public val dokumentasjonkravreferanse: String,
    public val saksreferanse: String? = null,
    public val utbetalingsreferanse: List<String>? = null,
    public val tittel: String? = null,
    public val beskrivelse: String? = null,
    public val frist: String? = null,
    public val status: Status? = null,
    override val type: String = "dokumentasjonkrav",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        RELEVANT("RELEVANT"),
        LEVERT_TIDLIGERE("LEVERT_TIDLIGERE"),
        ANNULLERT("ANNULLERT"),
        OPPFYLT("OPPFYLT"), // deprecated, read as RELEVANT
        IKKE_OPPFYLT("IKKE_OPPFYLT"), // deprecated, read as RELEVANT
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Dokumentasjonkrav.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

@Serializable
public data class Rammevedtak(
    override val hendelsestidspunkt: String,
    public val rammevedtaksreferanse: String,
    public val saksreferanse: String? = null,
    public val beskrivelse: String? = null,
    public val belop: Double? = null, // Double matches Java model; consumers convert to BigDecimal themselves
    public val fom: String? = null,
    public val tom: String? = null,
    override val type: String = "rammevedtak",
) : Hendelse
