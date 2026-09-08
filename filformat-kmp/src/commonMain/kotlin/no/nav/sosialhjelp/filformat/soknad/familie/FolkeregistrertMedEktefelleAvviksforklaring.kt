package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Forklaring
 *
 * Feltet er utdatert.
 */
@Serializable
public data class FolkeregistrertMedEktefelleAvviksforklaring(
  public val kilde: KildeBruker,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift).
   */
  public val verdi: String? = null,
)
