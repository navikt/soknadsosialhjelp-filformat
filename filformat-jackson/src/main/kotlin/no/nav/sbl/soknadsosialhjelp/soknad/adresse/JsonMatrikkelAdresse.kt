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
 * Angir en matrikkeladresse.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kommunenummer", "gaardsnummer", "bruksnummer", "festenummer", "seksjonsnummer", "undernummer")
public open class JsonMatrikkelAdresse : JsonAdresse(), Serializable {
  @get:JsonProperty("kommunenummer")
  @set:JsonProperty("kommunenummer")
  public var kommunenummer: String? = null

  @get:JsonProperty("gaardsnummer")
  @set:JsonProperty("gaardsnummer")
  public var gaardsnummer: String? = null

  @get:JsonProperty("bruksnummer")
  @set:JsonProperty("bruksnummer")
  public var bruksnummer: String? = null

  @get:JsonProperty("festenummer")
  @set:JsonProperty("festenummer")
  public var festenummer: String? = null

  @get:JsonProperty("seksjonsnummer")
  @set:JsonProperty("seksjonsnummer")
  public var seksjonsnummer: String? = null

  @get:JsonProperty("undernummer")
  @set:JsonProperty("undernummer")
  public var undernummer: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKommunenummer(kommunenummer: String?): JsonMatrikkelAdresse {
    this.kommunenummer = kommunenummer
    return this
  }

  public open fun withGaardsnummer(gaardsnummer: String?): JsonMatrikkelAdresse {
    this.gaardsnummer = gaardsnummer
    return this
  }

  public open fun withBruksnummer(bruksnummer: String?): JsonMatrikkelAdresse {
    this.bruksnummer = bruksnummer
    return this
  }

  public open fun withFestenummer(festenummer: String?): JsonMatrikkelAdresse {
    this.festenummer = festenummer
    return this
  }

  public open fun withSeksjonsnummer(seksjonsnummer: String?): JsonMatrikkelAdresse {
    this.seksjonsnummer = seksjonsnummer
    return this
  }

  public open fun withUndernummer(undernummer: String?): JsonMatrikkelAdresse {
    this.undernummer = undernummer
    return this
  }

  override fun withKilde(kilde: JsonKilde?): JsonMatrikkelAdresse {
    this.kilde = kilde
    return this
  }

  override fun withType(type: JsonAdresse.Type?): JsonMatrikkelAdresse {
    this.type = type
    return this
  }

  override fun withAdresseValg(adresseValg: JsonAdresseValg?): JsonMatrikkelAdresse {
    this.adresseValg = adresseValg
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonMatrikkelAdresse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonMatrikkelAdresse) return false
    return kommunenummer == other.kommunenummer &&
        gaardsnummer == other.gaardsnummer &&
        bruksnummer == other.bruksnummer &&
        festenummer == other.festenummer &&
        seksjonsnummer == other.seksjonsnummer &&
        undernummer == other.undernummer &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kommunenummer?.hashCode() ?: 0)
    result = result * 31 + (gaardsnummer?.hashCode() ?: 0)
    result = result * 31 + (bruksnummer?.hashCode() ?: 0)
    result = result * 31 + (festenummer?.hashCode() ?: 0)
    result = result * 31 + (seksjonsnummer?.hashCode() ?: 0)
    result = result * 31 + (undernummer?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonMatrikkelAdresse(kommunenummer=$kommunenummer, gaardsnummer=$gaardsnummer, bruksnummer=$bruksnummer, festenummer=$festenummer, seksjonsnummer=$seksjonsnummer, undernummer=$undernummer, additionalProperties=$additionalProperties, super=${super.toString()})"
}
