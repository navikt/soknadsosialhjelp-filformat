package no.nav.sosialhjelp.filformat.soknad.utdanning

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Utdanning
 */
@Serializable
public data class Utdanning(
  public val kilde: Kilde? = null,
  /**
   * Hvis "erStudent" mangler betyr dette at søker ikke har svart på spørsmålet.
   */
  public val erStudent: Boolean? = null,
  /**
   * Hvis "studentgrad" mangler betyr dette at søker ikke har svart på spørsmålet.
   */
  public val studentgrad: Studentgrad? = null,
) {
  @Serializable(with = StudentgradSerializer::class)
  public enum class Studentgrad(
    public val jsonValue: String,
  ) {
    HELTID("heltid"),
    DELTID("deltid"),
    UKJENT("UKJENT"),
    ;
  }

  public object StudentgradSerializer : UkjentTolerantEnumSerializer<Studentgrad>("Utdanning.Studentgrad", Studentgrad.entries.toTypedArray(), Studentgrad.UKJENT, Studentgrad::jsonValue)
}
