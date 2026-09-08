package no.nav.sosialhjelp.filformat.soknad

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Soknadsmottaker
 */
@Serializable
public data class Soknadsmottaker(
  /**
   * Kommunenummer for søknadsmottaker.
   */
  public val kommunenummer: String? = null,
  /**
   * Enhetssnummer for søknadsmottaker.
   */
  public val enhetsnummer: String? = null,
  /**
   * Navn på NAV-enhet som søknaden sendes til.
   */
  public val navEnhetsnavn: String? = null,
)
