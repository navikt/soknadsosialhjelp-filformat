package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Organisasjon
 */
@Serializable
public data class Organisasjon(
  /**
   * Navn på organisasjonen.
   */
  public val navn: String,
  /**
   * Organisasjonsnummer.
   */
  public val organisasjonsnummer: String,
)
