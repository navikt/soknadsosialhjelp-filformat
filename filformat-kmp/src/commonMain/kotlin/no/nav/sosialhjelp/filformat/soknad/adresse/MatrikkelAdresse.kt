package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir en matrikkeladresse.
 */
@Serializable
public data class MatrikkelAdresse(
  override val kilde: Kilde,
  public val kommunenummer: String? = null,
  public val gaardsnummer: String? = null,
  public val bruksnummer: String? = null,
  public val festenummer: String? = null,
  public val seksjonsnummer: String? = null,
  public val undernummer: String? = null,
  override val adresseValg: AdresseValg? = null,
  override val type: String = "matrikkeladresse",
) : Adresse
