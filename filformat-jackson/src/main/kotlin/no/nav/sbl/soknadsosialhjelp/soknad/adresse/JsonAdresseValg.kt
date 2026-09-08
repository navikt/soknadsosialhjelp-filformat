package no.nav.sbl.soknadsosialhjelp.soknad.adresse

import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String

public enum class JsonAdresseValg(
  @JsonValue
  public val `value`: String,
) {
  FOLKEREGISTRERT("folkeregistrert"),
  MIDLERTIDIG("midlertidig"),
  SOKNAD("soknad"),
  ;

  public companion object {
    @JsonCreator
    public fun fromValue(`value`: String): JsonAdresseValg = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
  }
}
