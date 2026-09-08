package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Int
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

@Serializable
public data class Samvarsgrad(
  public val kilde: KildeBruker,
  /**
   * Samværsgraden angitt i prosent.
   */
  public val verdi: Int,
)
