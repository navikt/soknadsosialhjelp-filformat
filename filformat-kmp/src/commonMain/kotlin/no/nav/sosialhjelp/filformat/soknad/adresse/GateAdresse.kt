package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Angir en gateadresse.
 */
@Serializable
public data class GateAdresse(
  override val kilde: Kilde,
  public val landkode: String? = null,
  public val kommunenummer: String? = null,
  /**
   * Inneholder adresseringsinformasjon som skal presenteres mellom søkers navn og de andre adressefeltene. Eksempler på mulige verdier er "c/o Ola Nordmann", "v/Kari Nordmann" og "Melkegården".
   */
  public val adresselinjer: List<String>? = null,
  public val bolignummer: String? = null,
  public val postnummer: String? = null,
  public val poststed: String? = null,
  public val gatenavn: String? = null,
  public val husnummer: String? = null,
  public val husbokstav: String? = null,
  override val adresseValg: AdresseValg? = null,
  override val type: String = "gateadresse",
) : Adresse
