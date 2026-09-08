package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Filreferanse til FIKS
 */
@Serializable(with = FilreferanseSerializer::class)
public sealed interface Filreferanse {
  public val type: String
}
