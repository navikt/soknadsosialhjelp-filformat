package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir en adresse.
 */
@Serializable(with = AdresseSerializer::class)
public sealed interface Adresse {
  public val kilde: Kilde

  public val type: String

  public val adresseValg: AdresseValg?
}
