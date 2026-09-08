package no.nav.sbl.soknadsosialhjelp.soknad.familie

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
 * Familie
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("sivilstatus", "folkeregistrertMedEktefelleAvviksforklaring", "forsorgerplikt")
public open class JsonFamilie : Serializable {
  @get:JsonProperty("sivilstatus")
  @set:JsonProperty("sivilstatus")
  public var sivilstatus: JsonSivilstatus? = null

  @get:JsonProperty("folkeregistrertMedEktefelleAvviksforklaring")
  @set:JsonProperty("folkeregistrertMedEktefelleAvviksforklaring")
  public var folkeregistrertMedEktefelleAvviksforklaring:
      JsonFolkeregistrertMedEktefelleAvviksforklaring? = null

  @get:JsonProperty("forsorgerplikt")
  @set:JsonProperty("forsorgerplikt")
  @get:JsonPropertyDescription("Forsørgerplikt")
  public var forsorgerplikt: JsonForsorgerplikt? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSivilstatus(sivilstatus: JsonSivilstatus?): JsonFamilie {
    this.sivilstatus = sivilstatus
    return this
  }

  public open fun withFolkeregistrertMedEktefelleAvviksforklaring(folkeregistrertMedEktefelleAvviksforklaring: JsonFolkeregistrertMedEktefelleAvviksforklaring?): JsonFamilie {
    this.folkeregistrertMedEktefelleAvviksforklaring = folkeregistrertMedEktefelleAvviksforklaring
    return this
  }

  public open fun withForsorgerplikt(forsorgerplikt: JsonForsorgerplikt?): JsonFamilie {
    this.forsorgerplikt = forsorgerplikt
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonFamilie {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonFamilie) return false
    return sivilstatus == other.sivilstatus &&
        folkeregistrertMedEktefelleAvviksforklaring == other.folkeregistrertMedEktefelleAvviksforklaring &&
        forsorgerplikt == other.forsorgerplikt &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (sivilstatus?.hashCode() ?: 0)
    result = result * 31 + (folkeregistrertMedEktefelleAvviksforklaring?.hashCode() ?: 0)
    result = result * 31 + (forsorgerplikt?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonFamilie(sivilstatus=$sivilstatus, folkeregistrertMedEktefelleAvviksforklaring=$folkeregistrertMedEktefelleAvviksforklaring, forsorgerplikt=$forsorgerplikt, additionalProperties=$additionalProperties)"
}
