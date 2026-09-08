package no.nav.sosialhjelp.filformat.klage

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Begrunnelse for klagen
 */
@Serializable
public data class Begrunnelse(
  public val kilde: KildeBruker,
  /**
   * Fritekst begrunnelse for klagen
   */
  public val klageTekst: String,
)
