package no.nav.sbl.soknadsosialhjelp.digisos.soker

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
 * Data for søkers innsyn
 *
 * Encoding er UTF-8.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("version", "avsender", "hendelser")
public open class JsonDigisosSoker : Serializable {
  @get:JsonProperty("version")
  @set:JsonProperty("version")
  public var version: String? = null

  @get:JsonProperty("avsender")
  @set:JsonProperty("avsender")
  public var avsender: JsonAvsender? = null

  @get:JsonProperty("hendelser")
  @set:JsonProperty("hendelser")
  @get:JsonPropertyDescription("Hendelser")
  public var hendelser: List<JsonHendelse>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withVersion(version: String?): JsonDigisosSoker {
    this.version = version
    return this
  }

  public open fun withAvsender(avsender: JsonAvsender?): JsonDigisosSoker {
    this.avsender = avsender
    return this
  }

  public open fun withHendelser(hendelser: List<JsonHendelse>?): JsonDigisosSoker {
    this.hendelser = hendelser
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonDigisosSoker {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonDigisosSoker) return false
    return version == other.version &&
        avsender == other.avsender &&
        hendelser == other.hendelser &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (version?.hashCode() ?: 0)
    result = result * 31 + (avsender?.hashCode() ?: 0)
    result = result * 31 + (hendelser?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonDigisosSoker(version=$version, avsender=$avsender, hendelser=$hendelser, additionalProperties=$additionalProperties)"
}
