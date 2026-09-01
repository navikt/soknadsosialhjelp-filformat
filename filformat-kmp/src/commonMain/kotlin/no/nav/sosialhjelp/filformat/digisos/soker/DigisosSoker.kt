package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable

/**
 * `json/digisos/soker/digisos-soker.json` -- the root document.
 */
@Serializable
public data class DigisosSoker(
    /** Semver-ish schema version, see `parts/version.json`. */
    public val version: String,
    public val avsender: Avsender,
    public val hendelser: List<Hendelse>,
)

/** `json/digisos/soker/parts/avsender.json` */
@Serializable
public data class Avsender(
    public val systemnavn: String,
    public val systemversjon: String,
)

/** `json/digisos/soker/parts/forvaltningsbrev.json` */
@Serializable
public data class Forvaltningsbrev(
    public val referanse: Filreferanse,
)

/**
 * `json/digisos/soker/parts/vedlegg.json` -- a vedlegg *inside a hendelse*.
 *
 * NB: not the same concept as [no.nav.sosialhjelp.filformat.vedlegg.Vedlegg], which comes
 * from `json/vedlegg/vedleggSpesifikasjon.json`. The Java model separates the two by package
 * and so do we. Do not merge them.
 */
@Serializable
public data class Vedlegg(
    /** Link/label text describing what the vedlegg is. */
    public val tittel: String,
    public val referanse: Filreferanse,
)
