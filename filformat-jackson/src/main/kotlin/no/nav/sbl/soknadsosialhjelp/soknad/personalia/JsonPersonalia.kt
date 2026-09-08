package no.nav.sbl.soknadsosialhjelp.soknad.personalia

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
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse

/**
 * Personalia
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("personIdentifikator", "navn", "statsborgerskap", "nordiskBorger", "telefonnummer", "kontonummer", "folkeregistrertAdresse", "oppholdsadresse", "postadresse", "fodselsdato")
public open class JsonPersonalia : Serializable {
  @get:JsonProperty("personIdentifikator")
  @set:JsonProperty("personIdentifikator")
  public var personIdentifikator: JsonPersonIdentifikator? = null

  @get:JsonProperty("navn")
  @set:JsonProperty("navn")
  public var navn: JsonSokernavn? = null

  @get:JsonProperty("statsborgerskap")
  @set:JsonProperty("statsborgerskap")
  public var statsborgerskap: JsonStatsborgerskap? = null

  @get:JsonProperty("nordiskBorger")
  @set:JsonProperty("nordiskBorger")
  public var nordiskBorger: JsonNordiskBorger? = null

  @get:JsonProperty("telefonnummer")
  @set:JsonProperty("telefonnummer")
  public var telefonnummer: JsonTelefonnummer? = null

  @get:JsonProperty("kontonummer")
  @set:JsonProperty("kontonummer")
  public var kontonummer: JsonKontonummer? = null

  @get:JsonProperty("folkeregistrertAdresse")
  @set:JsonProperty("folkeregistrertAdresse")
  public var folkeregistrertAdresse: JsonAdresse? = null

  @get:JsonProperty("oppholdsadresse")
  @set:JsonProperty("oppholdsadresse")
  public var oppholdsadresse: JsonAdresse? = null

  @get:JsonProperty("postadresse")
  @set:JsonProperty("postadresse")
  public var postadresse: JsonAdresse? = null

  @get:JsonProperty("fodselsdato")
  @set:JsonProperty("fodselsdato")
  public var fodselsdato: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withPersonIdentifikator(personIdentifikator: JsonPersonIdentifikator?): JsonPersonalia {
    this.personIdentifikator = personIdentifikator
    return this
  }

  public open fun withNavn(navn: JsonSokernavn?): JsonPersonalia {
    this.navn = navn
    return this
  }

  public open fun withStatsborgerskap(statsborgerskap: JsonStatsborgerskap?): JsonPersonalia {
    this.statsborgerskap = statsborgerskap
    return this
  }

  public open fun withNordiskBorger(nordiskBorger: JsonNordiskBorger?): JsonPersonalia {
    this.nordiskBorger = nordiskBorger
    return this
  }

  public open fun withTelefonnummer(telefonnummer: JsonTelefonnummer?): JsonPersonalia {
    this.telefonnummer = telefonnummer
    return this
  }

  public open fun withKontonummer(kontonummer: JsonKontonummer?): JsonPersonalia {
    this.kontonummer = kontonummer
    return this
  }

  public open fun withFolkeregistrertAdresse(folkeregistrertAdresse: JsonAdresse?): JsonPersonalia {
    this.folkeregistrertAdresse = folkeregistrertAdresse
    return this
  }

  public open fun withOppholdsadresse(oppholdsadresse: JsonAdresse?): JsonPersonalia {
    this.oppholdsadresse = oppholdsadresse
    return this
  }

  public open fun withPostadresse(postadresse: JsonAdresse?): JsonPersonalia {
    this.postadresse = postadresse
    return this
  }

  public open fun withFodselsdato(fodselsdato: String?): JsonPersonalia {
    this.fodselsdato = fodselsdato
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonPersonalia {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonPersonalia) return false
    return personIdentifikator == other.personIdentifikator &&
        navn == other.navn &&
        statsborgerskap == other.statsborgerskap &&
        nordiskBorger == other.nordiskBorger &&
        telefonnummer == other.telefonnummer &&
        kontonummer == other.kontonummer &&
        folkeregistrertAdresse == other.folkeregistrertAdresse &&
        oppholdsadresse == other.oppholdsadresse &&
        postadresse == other.postadresse &&
        fodselsdato == other.fodselsdato &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (personIdentifikator?.hashCode() ?: 0)
    result = result * 31 + (navn?.hashCode() ?: 0)
    result = result * 31 + (statsborgerskap?.hashCode() ?: 0)
    result = result * 31 + (nordiskBorger?.hashCode() ?: 0)
    result = result * 31 + (telefonnummer?.hashCode() ?: 0)
    result = result * 31 + (kontonummer?.hashCode() ?: 0)
    result = result * 31 + (folkeregistrertAdresse?.hashCode() ?: 0)
    result = result * 31 + (oppholdsadresse?.hashCode() ?: 0)
    result = result * 31 + (postadresse?.hashCode() ?: 0)
    result = result * 31 + (fodselsdato?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonPersonalia(personIdentifikator=$personIdentifikator, navn=$navn, statsborgerskap=$statsborgerskap, nordiskBorger=$nordiskBorger, telefonnummer=$telefonnummer, kontonummer=$kontonummer, folkeregistrertAdresse=$folkeregistrertAdresse, oppholdsadresse=$oppholdsadresse, postadresse=$postadresse, fodselsdato=$fodselsdato, additionalProperties=$additionalProperties)"
}
