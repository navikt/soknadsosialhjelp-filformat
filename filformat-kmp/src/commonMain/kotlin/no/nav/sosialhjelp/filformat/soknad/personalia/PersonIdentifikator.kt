package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Unik identifikasjon av søker
 */
@Serializable
public data class PersonIdentifikator(
  /**
   * Alltid system (og aldri utdatert).
   */
  public val kilde: Kilde,
  public val verdi: String,
) {
  @Serializable(with = KildeSerializer::class)
  public enum class Kilde(
    public val jsonValue: String,
  ) {
    SYSTEM("system"),
    UKJENT("UKJENT"),
    ;
  }

  public object KildeSerializer : UkjentTolerantEnumSerializer<Kilde>("PersonIdentifikator.Kilde", Kilde.entries.toTypedArray(), Kilde.UKJENT, Kilde::jsonValue)
}
