package no.nav.sbl.soknadsosialhjelp.soknad.personalia

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap

/**
 * Unik identifikasjon av søker
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "verdi")
public open class JsonPersonIdentifikator : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  @get:JsonPropertyDescription("Alltid system (og aldri utdatert).")
  public var kilde: Kilde? = Kilde.fromValue("system")

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  public var verdi: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: Kilde?): JsonPersonIdentifikator {
    this.kilde = kilde
    return this
  }

  public open fun withVerdi(verdi: String?): JsonPersonIdentifikator {
    this.verdi = verdi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonPersonIdentifikator {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonPersonIdentifikator) return false
    return kilde == other.kilde &&
        verdi == other.verdi &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (verdi?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonPersonIdentifikator(kilde=$kilde, verdi=$verdi, additionalProperties=$additionalProperties)"

  public enum class Kilde(
    @JsonValue
    public val `value`: String,
  ) {
    SYSTEM("system"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Kilde = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
