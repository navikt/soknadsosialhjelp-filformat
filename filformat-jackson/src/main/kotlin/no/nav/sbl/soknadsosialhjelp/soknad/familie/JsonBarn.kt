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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonNavn

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "navn", "fodselsdato", "personIdentifikator", "harDiskresjonskode")
public open class JsonBarn : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("navn")
  @set:JsonProperty("navn")
  public var navn: JsonNavn? = null

  @get:JsonProperty("fodselsdato")
  @set:JsonProperty("fodselsdato")
  public var fodselsdato: String? = null

  @get:JsonProperty("personIdentifikator")
  @set:JsonProperty("personIdentifikator")
  public var personIdentifikator: String? = null

  @get:JsonProperty("harDiskresjonskode")
  @set:JsonProperty("harDiskresjonskode")
  @get:JsonPropertyDescription("Settes til true hvis barn har diskresjonskode 6 eller 7. Kun relevant hvis \"kilde\" er \"system\". Feltet er utdatert.")
  public var harDiskresjonskode: Boolean? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonBarn {
    this.kilde = kilde
    return this
  }

  public open fun withNavn(navn: JsonNavn?): JsonBarn {
    this.navn = navn
    return this
  }

  public open fun withFodselsdato(fodselsdato: String?): JsonBarn {
    this.fodselsdato = fodselsdato
    return this
  }

  public open fun withPersonIdentifikator(personIdentifikator: String?): JsonBarn {
    this.personIdentifikator = personIdentifikator
    return this
  }

  public open fun withHarDiskresjonskode(harDiskresjonskode: Boolean?): JsonBarn {
    this.harDiskresjonskode = harDiskresjonskode
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonBarn {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonBarn) return false
    return kilde == other.kilde &&
        navn == other.navn &&
        fodselsdato == other.fodselsdato &&
        personIdentifikator == other.personIdentifikator &&
        harDiskresjonskode == other.harDiskresjonskode &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (navn?.hashCode() ?: 0)
    result = result * 31 + (fodselsdato?.hashCode() ?: 0)
    result = result * 31 + (personIdentifikator?.hashCode() ?: 0)
    result = result * 31 + (harDiskresjonskode?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonBarn(kilde=$kilde, navn=$navn, fodselsdato=$fodselsdato, personIdentifikator=$personIdentifikator, harDiskresjonskode=$harDiskresjonskode, additionalProperties=$additionalProperties)"
}
