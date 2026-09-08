package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde
import no.nav.sosialhjelp.filformat.soknad.common.Navn

@Serializable
public data class Barn(
  public val kilde: Kilde,
  public val navn: Navn,
  public val fodselsdato: String? = null,
  public val personIdentifikator: String? = null,
  /**
   * Settes til true hvis barn har diskresjonskode 6 eller 7. Kun relevant hvis "kilde" er "system". Feltet er utdatert.
   */
  public val harDiskresjonskode: Boolean? = null,
)
