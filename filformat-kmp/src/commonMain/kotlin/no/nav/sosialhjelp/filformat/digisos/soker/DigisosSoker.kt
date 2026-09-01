package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable

@Serializable
public data class DigisosSoker(
    public val version: String,
    public val avsender: Avsender,
    public val hendelser: List<Hendelse>,
)

@Serializable
public data class Avsender(
    public val systemnavn: String,
    public val systemversjon: String,
)

@Serializable
public data class Forvaltningsbrev(
    public val referanse: Filreferanse,
)

// Not the same concept as no.nav.sosialhjelp.filformat.vedlegg.Vedlegg — do not merge them
@Serializable
public data class Vedlegg(
    public val tittel: String,
    public val referanse: Filreferanse,
)
