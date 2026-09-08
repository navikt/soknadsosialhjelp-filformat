package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Vedlegg til vedtaksfil, forvaltningsbrev eller dokumentasjonEtterspurt
 */
@Serializable
public data class Vedlegg(
  /**
   * En tittel som kan brukes som lenke/forklaringstekst til hva vedlegget er.
   */
  public val tittel: String,
  public val referanse: Filreferanse,
)
