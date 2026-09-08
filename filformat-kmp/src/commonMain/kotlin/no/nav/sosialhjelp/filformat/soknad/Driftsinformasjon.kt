package no.nav.sosialhjelp.filformat.soknad

import kotlin.Boolean
import kotlinx.serialization.Serializable

/**
 * Inneholder informasjon om status for henting av opplysninger fra andre tjenester.
 */
@Serializable
public data class Driftsinformasjon(
  /**
   * Hvis skattbar inntekt hos skatteetaten ikke kunne hentes er denne true.
   */
  public val inntektFraSkatteetatenFeilet: Boolean,
  /**
   * Hvis utbetalinger fra NAV ikke kunne hentes er denne true.
   */
  public val utbetalingerFraNavFeilet: Boolean? = null,
  /**
   * Hvis økonomisk støtte fra Husbanken ikke kunne hentes er denne true.
   */
  public val stotteFraHusbankenFeilet: Boolean? = null,
)
