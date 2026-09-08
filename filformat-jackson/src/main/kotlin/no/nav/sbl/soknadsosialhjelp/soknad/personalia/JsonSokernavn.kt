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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonNavn

/**
 * Søkers navn.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde")
public open class JsonSokernavn : JsonNavn(), Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  @get:JsonPropertyDescription("Alltid system (og aldri utdatert).")
  public var kilde: Kilde? = Kilde.fromValue("system")

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: Kilde?): JsonSokernavn {
    this.kilde = kilde
    return this
  }

  override fun withFornavn(fornavn: String?): JsonSokernavn {
    this.fornavn = fornavn
    return this
  }

  override fun withMellomnavn(mellomnavn: String?): JsonSokernavn {
    this.mellomnavn = mellomnavn
    return this
  }

  override fun withEtternavn(etternavn: String?): JsonSokernavn {
    this.etternavn = etternavn
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonSokernavn {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSokernavn) return false
    return kilde == other.kilde &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonSokernavn(kilde=$kilde, additionalProperties=$additionalProperties, super=${super.toString()})"

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
