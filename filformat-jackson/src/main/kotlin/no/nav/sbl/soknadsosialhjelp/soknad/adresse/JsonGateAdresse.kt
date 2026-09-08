package no.nav.sbl.soknadsosialhjelp.soknad.adresse

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Angir en gateadresse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("landkode", "kommunenummer", "adresselinjer", "bolignummer", "postnummer", "poststed", "gatenavn", "husnummer", "husbokstav")
public open class JsonGateAdresse : JsonAdresse(), Serializable {
  @get:JsonProperty("landkode")
  @set:JsonProperty("landkode")
  public var landkode: String? = null

  @get:JsonProperty("kommunenummer")
  @set:JsonProperty("kommunenummer")
  public var kommunenummer: String? = null

  @get:JsonProperty("adresselinjer")
  @set:JsonProperty("adresselinjer")
  @get:JsonPropertyDescription("Inneholder adresseringsinformasjon som skal presenteres mellom søkers navn og de andre adressefeltene. Eksempler på mulige verdier er \"c/o Ola Nordmann\", \"v/Kari Nordmann\" og \"Melkegården\".")
  public var adresselinjer: List<String>? = mutableListOf()

  @get:JsonProperty("bolignummer")
  @set:JsonProperty("bolignummer")
  public var bolignummer: String? = null

  @get:JsonProperty("postnummer")
  @set:JsonProperty("postnummer")
  public var postnummer: String? = null

  @get:JsonProperty("poststed")
  @set:JsonProperty("poststed")
  public var poststed: String? = null

  @get:JsonProperty("gatenavn")
  @set:JsonProperty("gatenavn")
  public var gatenavn: String? = null

  @get:JsonProperty("husnummer")
  @set:JsonProperty("husnummer")
  public var husnummer: String? = null

  @get:JsonProperty("husbokstav")
  @set:JsonProperty("husbokstav")
  public var husbokstav: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withLandkode(landkode: String?): JsonGateAdresse {
    this.landkode = landkode
    return this
  }

  public open fun withKommunenummer(kommunenummer: String?): JsonGateAdresse {
    this.kommunenummer = kommunenummer
    return this
  }

  public open fun withAdresselinjer(adresselinjer: List<String>?): JsonGateAdresse {
    this.adresselinjer = adresselinjer
    return this
  }

  public open fun withBolignummer(bolignummer: String?): JsonGateAdresse {
    this.bolignummer = bolignummer
    return this
  }

  public open fun withPostnummer(postnummer: String?): JsonGateAdresse {
    this.postnummer = postnummer
    return this
  }

  public open fun withPoststed(poststed: String?): JsonGateAdresse {
    this.poststed = poststed
    return this
  }

  public open fun withGatenavn(gatenavn: String?): JsonGateAdresse {
    this.gatenavn = gatenavn
    return this
  }

  public open fun withHusnummer(husnummer: String?): JsonGateAdresse {
    this.husnummer = husnummer
    return this
  }

  public open fun withHusbokstav(husbokstav: String?): JsonGateAdresse {
    this.husbokstav = husbokstav
    return this
  }

  override fun withKilde(kilde: JsonKilde?): JsonGateAdresse {
    this.kilde = kilde
    return this
  }

  override fun withType(type: JsonAdresse.Type?): JsonGateAdresse {
    this.type = type
    return this
  }

  override fun withAdresseValg(adresseValg: JsonAdresseValg?): JsonGateAdresse {
    this.adresseValg = adresseValg
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonGateAdresse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonGateAdresse) return false
    return landkode == other.landkode &&
        kommunenummer == other.kommunenummer &&
        adresselinjer == other.adresselinjer &&
        bolignummer == other.bolignummer &&
        postnummer == other.postnummer &&
        poststed == other.poststed &&
        gatenavn == other.gatenavn &&
        husnummer == other.husnummer &&
        husbokstav == other.husbokstav &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (landkode?.hashCode() ?: 0)
    result = result * 31 + (kommunenummer?.hashCode() ?: 0)
    result = result * 31 + (adresselinjer?.hashCode() ?: 0)
    result = result * 31 + (bolignummer?.hashCode() ?: 0)
    result = result * 31 + (postnummer?.hashCode() ?: 0)
    result = result * 31 + (poststed?.hashCode() ?: 0)
    result = result * 31 + (gatenavn?.hashCode() ?: 0)
    result = result * 31 + (husnummer?.hashCode() ?: 0)
    result = result * 31 + (husbokstav?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonGateAdresse(landkode=$landkode, kommunenummer=$kommunenummer, adresselinjer=$adresselinjer, bolignummer=$bolignummer, postnummer=$postnummer, poststed=$poststed, gatenavn=$gatenavn, husnummer=$husnummer, husbokstav=$husbokstav, additionalProperties=$additionalProperties, super=${super.toString()})"
}
