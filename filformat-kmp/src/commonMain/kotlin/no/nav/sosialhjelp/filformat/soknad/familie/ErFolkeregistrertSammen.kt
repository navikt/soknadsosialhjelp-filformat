package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeSystem

@Serializable
public data class ErFolkeregistrertSammen(
  public val kilde: KildeSystem,
  public val verdi: Boolean,
)
