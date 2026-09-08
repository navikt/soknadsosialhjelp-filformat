package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir en postboksadresse.
 */
@Serializable
public data class PostboksAdresse(
  override val kilde: Kilde,
  public val postboks: String? = null,
  public val postnummer: String? = null,
  public val poststed: String? = null,
  override val adresseValg: AdresseValg? = null,
  override val type: String = "postboks",
) : Adresse
