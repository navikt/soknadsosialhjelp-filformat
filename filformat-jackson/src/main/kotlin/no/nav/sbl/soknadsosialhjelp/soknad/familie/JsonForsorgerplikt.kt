package no.nav.sbl.soknadsosialhjelp.soknad.familie

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
 * Forsørgerplikt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("harForsorgerplikt", "barnebidrag", "ansvar")
public open class JsonForsorgerplikt : Serializable {
  @get:JsonProperty("harForsorgerplikt")
  @set:JsonProperty("harForsorgerplikt")
  public var harForsorgerplikt: JsonHarForsorgerplikt? = null

  @get:JsonProperty("barnebidrag")
  @set:JsonProperty("barnebidrag")
  public var barnebidrag: JsonBarnebidrag? = null

  @get:JsonProperty("ansvar")
  @set:JsonProperty("ansvar")
  public var ansvar: List<JsonAnsvar>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withHarForsorgerplikt(harForsorgerplikt: JsonHarForsorgerplikt?): JsonForsorgerplikt {
    this.harForsorgerplikt = harForsorgerplikt
    return this
  }

  public open fun withBarnebidrag(barnebidrag: JsonBarnebidrag?): JsonForsorgerplikt {
    this.barnebidrag = barnebidrag
    return this
  }

  public open fun withAnsvar(ansvar: List<JsonAnsvar>?): JsonForsorgerplikt {
    this.ansvar = ansvar
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonForsorgerplikt {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonForsorgerplikt) return false
    return harForsorgerplikt == other.harForsorgerplikt &&
        barnebidrag == other.barnebidrag &&
        ansvar == other.ansvar &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (harForsorgerplikt?.hashCode() ?: 0)
    result = result * 31 + (barnebidrag?.hashCode() ?: 0)
    result = result * 31 + (ansvar?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonForsorgerplikt(harForsorgerplikt=$harForsorgerplikt, barnebidrag=$barnebidrag, ansvar=$ansvar, additionalProperties=$additionalProperties)"
}
