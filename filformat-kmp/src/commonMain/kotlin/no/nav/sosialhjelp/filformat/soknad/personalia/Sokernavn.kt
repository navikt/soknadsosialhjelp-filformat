package no.nav.sosialhjelp.filformat.soknad.personalia

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Søkers navn.
 */
@Serializable
public data class Sokernavn(
  /**
   * Alltid system (og aldri utdatert).
   */
  public val kilde: Kilde,
  /**
   * Feltet kan være blankt.
   */
  public val fornavn: String,
  /**
   * Feltet kan være blankt.
   */
  public val mellomnavn: String,
  /**
   * Feltet kan være blankt.
   */
  public val etternavn: String,
) {
  @Serializable(with = KildeSerializer::class)
  public enum class Kilde(
    public val jsonValue: String,
  ) {
    SYSTEM("system"),
    UKJENT("UKJENT"),
    ;
  }

  public object KildeSerializer : UkjentTolerantEnumSerializer<Kilde>("Sokernavn.Kilde", Kilde.entries.toTypedArray(), Kilde.UKJENT, Kilde::jsonValue)
}
