package no.nav.sosialhjelp.filformat.vedlegg

import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * JSON-formatert oversikt over vedlegg til søknad om sosialhjelp.
 *
 * Encoding er UTF-8.
 */
@Serializable
public data class VedleggSpesifikasjon(
  /**
   * Array
   *
   * Hvert enkelt vedlegg er en samling med sider som hører sammen - for eksempel en fil for hver enkelt side i en kontoutskrift.
   */
  public val vedlegg: List<Vedlegg>? = null,
)
