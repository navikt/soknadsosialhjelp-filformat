package no.nav.sbl.soknadsosialhjelp.soknad.common

import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String

public enum class JsonKildeSystem(
  @JsonValue
  public val `value`: String,
) {
  SYSTEM("system"),
  UTDATERT("utdatert"),
  ;

  public companion object {
    @JsonCreator
    public fun fromValue(`value`: String): JsonKildeSystem = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
  }
}
