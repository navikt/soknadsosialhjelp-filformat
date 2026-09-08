package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Fagsystemet
 */
@Serializable
public data class Avsender(
  /**
   * Navnet på avsendersystemet. Brukes ved eventuell feilsøking av ugyldige data.
   */
  public val systemnavn: String,
  /**
   * Versjonen til avsendersystemet. Brukes ved eventuell feilsøking av ugyldige data.
   */
  public val systemversjon: String,
)
