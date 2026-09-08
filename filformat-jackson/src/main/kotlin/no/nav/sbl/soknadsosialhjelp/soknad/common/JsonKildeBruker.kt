package no.nav.sbl.soknadsosialhjelp.soknad.common

import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonValue
import kotlin.String

public enum class JsonKildeBruker(
  @JsonValue
  public val `value`: String,
) {
  BRUKER("bruker"),
  UTDATERT("utdatert"),
  ;

  public companion object {
    @JsonCreator
    public fun fromValue(`value`: String): JsonKildeBruker = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
  }
}
