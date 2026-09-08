package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Søkers statsborgerskap.
 */
@Serializable
public data class Statsborgerskap(
  public val kilde: Kilde,
  /**
   * Definert med ISO 3166-1 (alpha-3).
   */
  public val verdi: String,
)
