package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Søkers kontonummer
 */
@Serializable
public data class Kontonummer(
  public val kilde: Kilde,
  /**
   * Bruker har eksplisitt sagt at han/hun ikke har noen konto som kan benyttes.
   *
   * Hvis "harIkkeKonto" mangler betyr dette at bruker hverken har angitt kontonummer eller huket av for at han/hun mangler konto.
   */
  public val harIkkeKonto: Boolean? = null,
  /**
   * Hvis "verdi" mangler betyr dette at bruker ikke har lagt inn noe kontonummer. Hvis angitt er "verdi" et norsk kontnummer, dvs 11-sifret og modulus-11-gyldig. Hvis norsk definisjon av kontonummer endres vil dette formatet også bli endret. Eventuelle konsumenter bør ta høyde for dette.
   */
  public val verdi: String? = null,
)
