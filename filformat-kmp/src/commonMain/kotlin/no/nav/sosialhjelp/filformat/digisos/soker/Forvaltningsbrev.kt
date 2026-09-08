package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.Serializable

/**
 * Forvaltningsbrevet som søker skal ha mulighet til å se. Det er ingen garanti for at filen blir vist til søker. Filformatet skal være PDF.
 */
@Serializable
public data class Forvaltningsbrev(
  public val referanse: Filreferanse,
)
