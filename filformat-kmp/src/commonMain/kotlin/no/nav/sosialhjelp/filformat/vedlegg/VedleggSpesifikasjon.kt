package no.nav.sosialhjelp.filformat.vedlegg

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/** `json/vedlegg/vedleggSpesifikasjon.json` -- the root document. */
@Serializable
public data class VedleggSpesifikasjon(
    public val vedlegg: List<Vedlegg>? = null,
)

/**
 * `json/vedlegg/vedleggSpesifikasjon.json#/definitions/vedlegg`.
 *
 * NB: not the same concept as [no.nav.sosialhjelp.filformat.digisos.soker.Vedlegg], which is
 * a vedlegg inside a hendelse. The Java model separates the two by package and so do we.
 */
@Serializable
public data class Vedlegg(
    /**
     * Deliberately a plain `String`, not an enum. The schema says: "Det kan komme nye typer
     * og dette må håndteres dynamisk av konsumenter." Do not turn this into an enum.
     */
    public val type: String? = null,
    /** Subtype. Plain `String` for the same reason as [type]. */
    public val tilleggsinfo: String? = null,
    public val klageId: String? = null,
    /**
     * "LastetOpp", "VedleggKreves" or "VedleggAlleredeSendt". Plain `String`: the schema says
     * unknown status values must not prevent display/archiving of any files.
     */
    public val status: String? = null,
    public val filer: List<Filer>? = null,
    public val hendelseType: HendelseType? = null,
    public val hendelseReferanse: String? = null,
) {

    /** The only real enum in this schema. */
    @Serializable(with = HendelseTypeSerializer::class)
    public enum class HendelseType(public val jsonValue: String) {
        DOKUMENTASJON_ETTERSPURT("dokumentasjonEtterspurt"),
        DOKUMENTASJONKRAV("dokumentasjonkrav"),
        SOKNAD("soknad"),
        BRUKER("bruker"),

        /** Not in the schema. Deliberate -- see [UkjentTolerantEnumSerializer]. */
        UKJENT("UKJENT"),
    }

    public object HendelseTypeSerializer : UkjentTolerantEnumSerializer<HendelseType>(
        "Vedlegg.HendelseType",
        HendelseType.entries.toTypedArray(),
        HendelseType.UKJENT,
        HendelseType::jsonValue,
    )
}

/**
 * `json/vedlegg/vedleggSpesifikasjon.json#/definitions/fil`.
 *
 * Named `Filer` (not `Fil`) to match the Java model's `javaType`, which is
 * `no.nav.sbl.soknadsosialhjelp.vedlegg.Filer`.
 */
@Serializable
public data class Filer(
    public val filnavn: String? = null,
    public val sha512: String? = null,
)
