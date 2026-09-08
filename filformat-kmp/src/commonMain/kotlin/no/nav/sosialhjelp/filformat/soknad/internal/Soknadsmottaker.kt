package no.nav.sosialhjelp.filformat.soknad.`internal`

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Soknadsmottaker
 */
@Serializable
public data class Soknadsmottaker(
  /**
   * Organisasjonsnummer for søknadsmottaker.
   */
  public val organisasjonsnummer: String? = null,
  /**
   * Navn på NAV-enhet som søknaden sendes til.
   */
  public val navEnhetsnavn: String? = null,
)
