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
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonNavn

/**
 * Søkers ektefelle
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("navn", "fodselsdato", "personIdentifikator")
public open class JsonEktefelle : Serializable {
  @get:JsonProperty("navn")
  @set:JsonProperty("navn")
  public var navn: JsonNavn? = null

  @get:JsonProperty("fodselsdato")
  @set:JsonProperty("fodselsdato")
  public var fodselsdato: String? = null

  @get:JsonProperty("personIdentifikator")
  @set:JsonProperty("personIdentifikator")
  public var personIdentifikator: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withNavn(navn: JsonNavn?): JsonEktefelle {
    this.navn = navn
    return this
  }

  public open fun withFodselsdato(fodselsdato: String?): JsonEktefelle {
    this.fodselsdato = fodselsdato
    return this
  }

  public open fun withPersonIdentifikator(personIdentifikator: String?): JsonEktefelle {
    this.personIdentifikator = personIdentifikator
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonEktefelle {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonEktefelle) return false
    return navn == other.navn &&
        fodselsdato == other.fodselsdato &&
        personIdentifikator == other.personIdentifikator &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (navn?.hashCode() ?: 0)
    result = result * 31 + (fodselsdato?.hashCode() ?: 0)
    result = result * 31 + (personIdentifikator?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonEktefelle(navn=$navn, fodselsdato=$fodselsdato, personIdentifikator=$personIdentifikator, additionalProperties=$additionalProperties)"
}
