package no.nav.sbl.soknadsosialhjelp.digisos.soker

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap

/**
 * Forvaltningsbrevet som søker skal ha mulighet til å se. Det er ingen garanti for at filen blir vist til søker. Filformatet skal være PDF.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("referanse")
public open class JsonForvaltningsbrev : Serializable {
  @get:JsonProperty("referanse")
  @set:JsonProperty("referanse")
  public var referanse: JsonFilreferanse? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withReferanse(referanse: JsonFilreferanse?): JsonForvaltningsbrev {
    this.referanse = referanse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonForvaltningsbrev {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonForvaltningsbrev) return false
    return referanse == other.referanse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (referanse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonForvaltningsbrev(referanse=$referanse, additionalProperties=$additionalProperties)"
}
