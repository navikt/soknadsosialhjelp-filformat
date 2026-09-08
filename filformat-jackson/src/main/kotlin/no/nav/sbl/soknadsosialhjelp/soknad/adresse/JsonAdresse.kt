package no.nav.sbl.soknadsosialhjelp.soknad.adresse

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonSubTypes
import com.fasterxml.jackson.`annotation`.JsonTypeInfo
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Angir en adresse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "type", "adresseValg")
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true,
)
@JsonSubTypes(value = [JsonSubTypes.Type(value = JsonGateAdresse::class, name = "gateadresse"), JsonSubTypes.Type(value = JsonMatrikkelAdresse::class, name = "matrikkeladresse"), JsonSubTypes.Type(value = JsonPostboksAdresse::class, name = "postboks"), JsonSubTypes.Type(value = JsonUstrukturertAdresse::class, name = "ustrukturert")])
public open class JsonAdresse : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Angir adressetypen og dermed hvilke felter som er satt. Eventuelle nye adressetyper vil enten bli lagt til som en ny major-version eller som nye felt under søknaden (for eksempel \"folkeregistrertAdresseV2\").")
  public var type: Type? = null

  @get:JsonProperty("adresseValg")
  @set:JsonProperty("adresseValg")
  public var adresseValg: JsonAdresseValg? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonAdresse {
    this.kilde = kilde
    return this
  }

  public open fun withType(type: Type?): JsonAdresse {
    this.type = type
    return this
  }

  public open fun withAdresseValg(adresseValg: JsonAdresseValg?): JsonAdresse {
    this.adresseValg = adresseValg
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonAdresse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonAdresse) return false
    return kilde == other.kilde &&
        type == other.type &&
        adresseValg == other.adresseValg &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (adresseValg?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonAdresse(kilde=$kilde, type=$type, adresseValg=$adresseValg, additionalProperties=$additionalProperties)"

  public enum class Type(
    @JsonValue
    public val `value`: String,
  ) {
    GATEADRESSE("gateadresse"),
    MATRIKKELADRESSE("matrikkeladresse"),
    POSTBOKS("postboks"),
    USTRUKTURERT("ustrukturert"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Type = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
