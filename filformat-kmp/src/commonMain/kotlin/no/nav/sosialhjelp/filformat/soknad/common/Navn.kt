package no.nav.sosialhjelp.filformat.soknad.common

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Navn på en person.
 */
@Serializable
public data class Navn(
  /**
   * Feltet kan være blankt.
   */
  public val fornavn: String,
  /**
   * Feltet kan være blankt.
   */
  public val mellomnavn: String,
  /**
   * Feltet kan være blankt.
   */
  public val etternavn: String,
)
