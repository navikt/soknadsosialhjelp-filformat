package no.nav.sbl.soknadsosialhjelp.soknad.bostotte

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
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * Bostøtte informasjon om bruker.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("saker")
public open class JsonBostotte : Serializable {
  @get:JsonProperty("saker")
  @set:JsonProperty("saker")
  public var saker: List<JsonBostotteSak>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSaker(saker: List<JsonBostotteSak>?): JsonBostotte {
    this.saker = saker
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonBostotte {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonBostotte) return false
    return saker == other.saker &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (saker?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonBostotte(saker=$saker, additionalProperties=$additionalProperties)"
}
