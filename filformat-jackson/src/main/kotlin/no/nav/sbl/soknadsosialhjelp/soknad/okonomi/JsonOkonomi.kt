package no.nav.sbl.soknadsosialhjelp.soknad.okonomi

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
 * Økonomiske data.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("opplysninger", "oversikt")
public open class JsonOkonomi : Serializable {
  @get:JsonProperty("opplysninger")
  @set:JsonProperty("opplysninger")
  @get:JsonPropertyDescription("Økonomiske opplysninger som ikke inngår i den strukturerte oversikten. Flott hvis saksbehandlers behov/ønsker kan diskuteres på Slack slik at en mer strukturert måte å presentere dataene på kan utarbeides.")
  public var opplysninger: JsonOkonomiopplysninger? = null

  @get:JsonProperty("oversikt")
  @set:JsonProperty("oversikt")
  @get:JsonPropertyDescription("Strukturert økonomisk oversikt.")
  public var oversikt: JsonOkonomioversikt? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withOpplysninger(opplysninger: JsonOkonomiopplysninger?): JsonOkonomi {
    this.opplysninger = opplysninger
    return this
  }

  public open fun withOversikt(oversikt: JsonOkonomioversikt?): JsonOkonomi {
    this.oversikt = oversikt
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomi {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomi) return false
    return opplysninger == other.opplysninger &&
        oversikt == other.oversikt &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (opplysninger?.hashCode() ?: 0)
    result = result * 31 + (oversikt?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomi(opplysninger=$opplysninger, oversikt=$oversikt, additionalProperties=$additionalProperties)"
}
