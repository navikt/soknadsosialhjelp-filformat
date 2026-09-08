package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Data for søkers innsyn
 *
 * Encoding er UTF-8.
 */
@Serializable
public data class DigisosSoker(
  public val version: String,
  public val avsender: Avsender,
  /**
   * Hendelser
   */
  public val hendelser: List<Hendelse>,
)
