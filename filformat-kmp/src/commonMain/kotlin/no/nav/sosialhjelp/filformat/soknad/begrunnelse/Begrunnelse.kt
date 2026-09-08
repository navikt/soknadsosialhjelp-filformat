package no.nav.sosialhjelp.filformat.soknad.begrunnelse

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Begrunnelse
 */
@Serializable
public data class Begrunnelse(
  public val kilde: KildeBruker,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift).
   */
  public val hvaSokesOm: String,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift).
   */
  public val hvorforSoke: String? = null,
)
