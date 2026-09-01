package no.nav.sosialhjelp.filformat.vedlegg

import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable
public data class VedleggSpesifikasjon(
    public val vedlegg: List<Vedlegg>? = null,
)

// Not the same concept as no.nav.sosialhjelp.filformat.digisos.soker.Vedlegg — do not merge them
@Serializable
public data class Vedlegg(
    public val type: String? = null, // open-ended per schema, do not make this an enum
    public val tilleggsinfo: String? = null,
    public val klageId: String? = null,
    public val status: String? = null, // open-ended per schema, do not make this an enum
    public val filer: List<Filer>? = null,
    public val hendelseType: HendelseType? = null,
    public val hendelseReferanse: String? = null,
) {

    @Serializable(with = HendelseTypeSerializer::class)
    public enum class HendelseType(public val jsonValue: String) {
        DOKUMENTASJON_ETTERSPURT("dokumentasjonEtterspurt"),
        DOKUMENTASJONKRAV("dokumentasjonkrav"),
        SOKNAD("soknad"),
        BRUKER("bruker"),
        UKJENT("UKJENT"),
    }

    public object HendelseTypeSerializer : UkjentTolerantEnumSerializer<HendelseType>(
        "Vedlegg.HendelseType",
        HendelseType.entries.toTypedArray(),
        HendelseType.UKJENT,
        HendelseType::jsonValue,
    )
}

// Named Filer (not Fil) to match the Java model
@Serializable
public data class Filer(
    public val filnavn: String? = null,
    public val sha512: String? = null,
)
