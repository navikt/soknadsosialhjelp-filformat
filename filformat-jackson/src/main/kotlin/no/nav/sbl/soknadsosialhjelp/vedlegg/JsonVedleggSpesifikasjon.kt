package no.nav.sbl.soknadsosialhjelp.vedlegg

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
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * JSON-formatert oversikt over vedlegg til søknad om sosialhjelp.
 *
 * Encoding er UTF-8.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("vedlegg")
public open class JsonVedleggSpesifikasjon : Serializable {
  @get:JsonProperty("vedlegg")
  @set:JsonProperty("vedlegg")
  @get:JsonPropertyDescription("Array Hvert enkelt vedlegg er en samling med sider som hører sammen - for eksempel en fil for hver enkelt side i en kontoutskrift.")
  public var vedlegg: List<JsonVedlegg>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withVedlegg(vedlegg: List<JsonVedlegg>?): JsonVedleggSpesifikasjon {
    this.vedlegg = vedlegg
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonVedleggSpesifikasjon {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonVedleggSpesifikasjon) return false
    return vedlegg == other.vedlegg &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (vedlegg?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonVedleggSpesifikasjon(vedlegg=$vedlegg, additionalProperties=$additionalProperties)"
}
