package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable

/**
 * Foreløpig svar
 */
@Serializable
public data class ForelopigSvar(
  public val forvaltningsbrev: Forvaltningsbrev,
  override val hendelsestidspunkt: String,
  /**
   * Vedlegg til forvaltningsbrev
   *
   * En liste med vedlegg til forvaltningsbrevet som søker skal ha mulighet til å se. Det er ingen garanti for at filene blir vist til søker. Filformatet skal være PDF.
   */
  public val vedlegg: List<Vedlegg>? = null,
  override val type: String = "forelopigSvar",
) : Hendelse
