package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Søkers telefonnummer.
 *
 * Hvis "telefonnummer" mangler i en søknad betyr dette at søker ikke har angitt noe telefonnummer.
 */
@Serializable
public data class Telefonnummer(
  public val kilde: Kilde,
  /**
   * Telefonnummer som følger E.164.
   *
   * Telefonnummeret vil i utgangspunktet kun være åttesifrede norske telefonnumre, men det er et krav å støtte standarden E.164. Dette for å muliggjøre fremtidig endring av hvilke telefonnummere som er tillatt uten å måtte gjøre tekniske endringer.
   */
  public val verdi: String,
)
