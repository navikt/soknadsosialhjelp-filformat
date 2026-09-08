package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class HarForsorgerplikt(
  public val kilde: Kilde? = null,
  public val verdi: Boolean? = null,
)
