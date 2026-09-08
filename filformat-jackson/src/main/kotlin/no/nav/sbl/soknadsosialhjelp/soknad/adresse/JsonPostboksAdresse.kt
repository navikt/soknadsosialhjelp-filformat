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
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Angir en postboksadresse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("postboks", "postnummer", "poststed")
public open class JsonPostboksAdresse : JsonAdresse(), Serializable {
  @get:JsonProperty("postboks")
  @set:JsonProperty("postboks")
  public var postboks: String? = null

  @get:JsonProperty("postnummer")
  @set:JsonProperty("postnummer")
  public var postnummer: String? = null

  @get:JsonProperty("poststed")
  @set:JsonProperty("poststed")
  public var poststed: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withPostboks(postboks: String?): JsonPostboksAdresse {
    this.postboks = postboks
    return this
  }

  public open fun withPostnummer(postnummer: String?): JsonPostboksAdresse {
    this.postnummer = postnummer
    return this
  }

  public open fun withPoststed(poststed: String?): JsonPostboksAdresse {
    this.poststed = poststed
    return this
  }

  override fun withKilde(kilde: JsonKilde?): JsonPostboksAdresse {
    this.kilde = kilde
    return this
  }

  override fun withType(type: JsonAdresse.Type?): JsonPostboksAdresse {
    this.type = type
    return this
  }

  override fun withAdresseValg(adresseValg: JsonAdresseValg?): JsonPostboksAdresse {
    this.adresseValg = adresseValg
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonPostboksAdresse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonPostboksAdresse) return false
    return postboks == other.postboks &&
        postnummer == other.postnummer &&
        poststed == other.poststed &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (postboks?.hashCode() ?: 0)
    result = result * 31 + (postnummer?.hashCode() ?: 0)
    result = result * 31 + (poststed?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonPostboksAdresse(postboks=$postboks, postnummer=$postnummer, poststed=$poststed, additionalProperties=$additionalProperties, super=${super.toString()})"
}
