package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

// Every subclass declares `type` as a real serialized property with a default. This is
// required: HendelseSerializer is a JsonContentPolymorphicSerializer, which does not inject
// a class discriminator on serialization. See the note on HendelseSerializer.

/** `parts/hendelse/tildeltNavKontor.json` */
@Serializable
public data class TildeltNavKontor(
    override val hendelsestidspunkt: String,
    /** NORG identifier for the NAV office. The schema constrains this to 4 digits. */
    public val navKontor: String,
    override val type: String = "tildeltNavKontor",
) : Hendelse

/** `parts/hendelse/soknadsStatus.json` */
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

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "SoknadsStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

/** `parts/hendelse/saksStatus.json` */
@Serializable
public data class SaksStatus(
    override val hendelsestidspunkt: String,
    /** Reference so a vedtak can be attached later. */
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

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "SaksStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

/** `parts/hendelse/vedtakFattet.json` */
@Serializable
public data class VedtakFattet(
    override val hendelsestidspunkt: String,
    public val saksreferanse: String,
    public val vedtaksfil: Vedtaksfil,
    public val utfall: Utfall? = null,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "vedtakFattet",
) : Hendelse {

    /** Inline anonymous object in the schema; named here. */
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

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object UtfallSerializer : UkjentTolerantEnumSerializer<Utfall>(
        "VedtakFattet.Utfall", Utfall.entries.toTypedArray(), Utfall.UKJENT, Utfall::jsonValue,
    )
}

/** `parts/hendelse/dokumentasjonEtterspurt.json` */
@Serializable
public data class DokumentasjonEtterspurt(
    override val hendelsestidspunkt: String,
    public val dokumenter: List<Dokument>,
    public val forvaltningsbrev: Forvaltningsbrev? = null,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "dokumentasjonEtterspurt",
) : Hendelse {

    /** Inline anonymous object in the schema; named here. */
    @Serializable
    public data class Dokument(
        /** Predefined or free-text document type, e.g. "strømfaktura". */
        public val dokumenttype: String,
        /** ISO8601 UTC timestamp with millisecond precision. */
        public val innsendelsesfrist: String,
        public val tilleggsinformasjon: String? = null,
        public val dokumentreferanse: String? = null,
    )
}

/** `parts/hendelse/forelopigSvar.json` */
@Serializable
public data class ForelopigSvar(
    override val hendelsestidspunkt: String,
    public val forvaltningsbrev: Forvaltningsbrev,
    public val vedlegg: List<Vedlegg>? = null,
    override val type: String = "forelopigSvar",
) : Hendelse

/** `parts/hendelse/utbetaling.json` */
@Serializable
public data class Utbetaling(
    override val hendelsestidspunkt: String,
    /** Unique per utbetaling, so the information can be replaced when updated. */
    public val utbetalingsreferanse: String,
    public val saksreferanse: String? = null,
    public val rammevedtaksreferanse: String? = null,
    public val status: Status? = null,
    /**
     * Amount in NOK.
     *
     * `Double`, not a decimal type: the schema says `"type": "number"` and the Java model's
     * `JsonUtbetaling.getBelop()` returns `java.lang.Double`. Consumers do their own
     * `Double -> BigDecimal` conversion. Changing this would break parity with the Java model.
     */
    public val belop: Double? = null,
    public val beskrivelse: String? = null,
    /** YYYY-MM-DD. */
    public val forfallsdato: String? = null,
    /** YYYY-MM-DD. */
    public val utbetalingsdato: String? = null,
    /** YYYY-MM-DD. */
    public val fom: String? = null,
    /** YYYY-MM-DD. */
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

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Utbetaling.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

/** `parts/hendelse/vilkar.json` */
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

        /** Deprecated in the schema, kept for backwards compatibility. Read as RELEVANT. */
        OPPFYLT("OPPFYLT"),

        /** Deprecated in the schema, kept for backwards compatibility. Read as RELEVANT. */
        IKKE_OPPFYLT("IKKE_OPPFYLT"),

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Vilkar.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

/** `parts/hendelse/dokumentasjonkrav.json` */
@Serializable
public data class Dokumentasjonkrav(
    override val hendelsestidspunkt: String,
    public val dokumentasjonkravreferanse: String,
    public val saksreferanse: String? = null,
    public val utbetalingsreferanse: List<String>? = null,
    public val tittel: String? = null,
    public val beskrivelse: String? = null,
    /** ISO8601 UTC timestamp with millisecond precision. */
    public val frist: String? = null,
    public val status: Status? = null,
    override val type: String = "dokumentasjonkrav",
) : Hendelse {

    @Serializable(with = StatusSerializer::class)
    public enum class Status(public val jsonValue: String) {
        RELEVANT("RELEVANT"),
        LEVERT_TIDLIGERE("LEVERT_TIDLIGERE"),
        ANNULLERT("ANNULLERT"),

        /** Deprecated in the schema, kept for backwards compatibility. Read as RELEVANT. */
        OPPFYLT("OPPFYLT"),

        /** Deprecated in the schema, kept for backwards compatibility. Read as RELEVANT. */
        IKKE_OPPFYLT("IKKE_OPPFYLT"),

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object StatusSerializer : UkjentTolerantEnumSerializer<Status>(
        "Dokumentasjonkrav.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue,
    )
}

/** `parts/hendelse/rammevedtak.json` */
@Serializable
public data class Rammevedtak(
    override val hendelsestidspunkt: String,
    public val rammevedtaksreferanse: String,
    public val saksreferanse: String? = null,
    public val beskrivelse: String? = null,
    /** Amount in NOK. `Double` for the same reason as [Utbetaling.belop]. */
    public val belop: Double? = null,
    /** YYYY-MM-DD. */
    public val fom: String? = null,
    /** YYYY-MM-DD. */
    public val tom: String? = null,
    override val type: String = "rammevedtak",
) : Hendelse
