package no.nav.sbl.soknadsosialhjelp.klage

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

/**
 * Autentiseringsinformasjon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("autentisertDigitalt", "autentiseringsTidspunkt")
public open class JsonAutentisering : Serializable {
  @get:JsonProperty("autentisertDigitalt")
  @set:JsonProperty("autentisertDigitalt")
  @get:JsonPropertyDescription("Om klager er autentisert digitalt")
  public var autentisertDigitalt: Boolean? = null

  @get:JsonProperty("autentiseringsTidspunkt")
  @set:JsonProperty("autentiseringsTidspunkt")
  public var autentiseringsTidspunkt: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withAutentisertDigitalt(autentisertDigitalt: Boolean?): JsonAutentisering {
    this.autentisertDigitalt = autentisertDigitalt
    return this
  }

  public open fun withAutentiseringsTidspunkt(autentiseringsTidspunkt: String?): JsonAutentisering {
    this.autentiseringsTidspunkt = autentiseringsTidspunkt
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonAutentisering {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonAutentisering) return false
    return autentisertDigitalt == other.autentisertDigitalt &&
        autentiseringsTidspunkt == other.autentiseringsTidspunkt &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (autentisertDigitalt?.hashCode() ?: 0)
    result = result * 31 + (autentiseringsTidspunkt?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonAutentisering(autentisertDigitalt=$autentisertDigitalt, autentiseringsTidspunkt=$autentiseringsTidspunkt, additionalProperties=$additionalProperties)"
}
