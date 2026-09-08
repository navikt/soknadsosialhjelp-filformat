package no.nav.sosialhjelp.filformat.soknad.familie

import kotlinx.serialization.Serializable

/**
 * Familie
 */
@Serializable
public data class Familie(
  /**
   * Forsørgerplikt
   */
  public val forsorgerplikt: Forsorgerplikt,
  public val sivilstatus: Sivilstatus? = null,
  public val folkeregistrertMedEktefelleAvviksforklaring:
      FolkeregistrertMedEktefelleAvviksforklaring? = null,
)
