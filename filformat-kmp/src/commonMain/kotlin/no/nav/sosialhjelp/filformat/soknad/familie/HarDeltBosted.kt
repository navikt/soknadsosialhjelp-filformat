package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Om barn som er folkeregistrert sammen med søker har delt bosted. Kun relevant hvis "erFolkeregistrertSammen" er "true".
 */
@Serializable
public data class HarDeltBosted(
  public val kilde: KildeBruker,
  public val verdi: Boolean,
)
