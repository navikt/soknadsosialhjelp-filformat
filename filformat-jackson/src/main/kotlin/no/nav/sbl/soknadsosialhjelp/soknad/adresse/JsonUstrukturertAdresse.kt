package no.nav.sbl.soknadsosialhjelp.soknad.adresse

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Angir ustrukturert adresse (liste med tekststrenger).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("adresse")
public open class JsonUstrukturertAdresse : JsonAdresse(), Serializable {
  @get:JsonProperty("adresse")
  @set:JsonProperty("adresse")
  public var adresse: List<String>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withAdresse(adresse: List<String>?): JsonUstrukturertAdresse {
    this.adresse = adresse
    return this
  }

  override fun withKilde(kilde: JsonKilde?): JsonUstrukturertAdresse {
    this.kilde = kilde
    return this
  }

  override fun withType(type: JsonAdresse.Type?): JsonUstrukturertAdresse {
    this.type = type
    return this
  }

  override fun withAdresseValg(adresseValg: JsonAdresseValg?): JsonUstrukturertAdresse {
    this.adresseValg = adresseValg
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonUstrukturertAdresse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonUstrukturertAdresse) return false
    return adresse == other.adresse &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (adresse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonUstrukturertAdresse(adresse=$adresse, additionalProperties=$additionalProperties, super=${super.toString()})"
}
