package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.Boolean
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir om søker er statsborger i et nordisk land.
 *
 * Hvis "nordiskBorger" mangler i en søknad betyr dette at søker ikke har svart på spørsmålet.
 */
@Serializable
public data class NordiskBorger(
  public val kilde: Kilde,
  public val verdi: Boolean,
)
