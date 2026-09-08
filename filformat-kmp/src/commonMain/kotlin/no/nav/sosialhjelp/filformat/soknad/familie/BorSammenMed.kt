package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

@Serializable
public data class BorSammenMed(
  public val kilde: KildeBruker,
  public val verdi: Boolean,
)
