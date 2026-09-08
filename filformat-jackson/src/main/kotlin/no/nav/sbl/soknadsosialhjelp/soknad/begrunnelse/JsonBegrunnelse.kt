package no.nav.sbl.soknadsosialhjelp.soknad.begrunnelse

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * Begrunnelse
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "hvorforSoke", "hvaSokesOm")
public open class JsonBegrunnelse : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("hvorforSoke")
  @set:JsonProperty("hvorforSoke")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift).")
  public var hvorforSoke: String? = null

  @get:JsonProperty("hvaSokesOm")
  @set:JsonProperty("hvaSokesOm")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift).")
  public var hvaSokesOm: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonBegrunnelse {
    this.kilde = kilde
    return this
  }

  public open fun withHvorforSoke(hvorforSoke: String?): JsonBegrunnelse {
    this.hvorforSoke = hvorforSoke
    return this
  }

  public open fun withHvaSokesOm(hvaSokesOm: String?): JsonBegrunnelse {
    this.hvaSokesOm = hvaSokesOm
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonBegrunnelse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonBegrunnelse) return false
    return kilde == other.kilde &&
        hvorforSoke == other.hvorforSoke &&
        hvaSokesOm == other.hvaSokesOm &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (hvorforSoke?.hashCode() ?: 0)
    result = result * 31 + (hvaSokesOm?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonBegrunnelse(kilde=$kilde, hvorforSoke=$hvorforSoke, hvaSokesOm=$hvaSokesOm, additionalProperties=$additionalProperties)"
}
