package no.nav.sosialhjelp.filformat.klage

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Autentiseringsinformasjon
 */
@Serializable
public data class Autentisering(
  /**
   * Om klager er autentisert digitalt
   */
  public val autentisertDigitalt: Boolean,
  public val autentiseringsTidspunkt: String,
)
