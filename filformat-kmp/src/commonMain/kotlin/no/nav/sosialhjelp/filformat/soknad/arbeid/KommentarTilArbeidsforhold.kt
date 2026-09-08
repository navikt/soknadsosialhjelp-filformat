package no.nav.sosialhjelp.filformat.soknad.arbeid

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Kommentar
 */
@Serializable
public data class KommentarTilArbeidsforhold(
  public val kilde: KildeBruker? = null,
  public val verdi: String? = null,
)
