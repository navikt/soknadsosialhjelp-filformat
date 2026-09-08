package no.nav.sbl.soknadsosialhjelp.soknad.personalia

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

/**
 * Søkers telefonnummer.
 *
 * Hvis "telefonnummer" mangler i en søknad betyr dette at søker ikke har angitt noe telefonnummer.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "verdi")
public open class JsonTelefonnummer : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  @get:JsonPropertyDescription("Telefonnummer som følger E.164. Telefonnummeret vil i utgangspunktet kun være åttesifrede norske telefonnumre, men det er et krav å støtte standarden E.164. Dette for å muliggjøre fremtidig endring av hvilke telefonnummere som er tillatt uten å måtte gjøre tekniske endringer.")
  public var verdi: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonTelefonnummer {
    this.kilde = kilde
    return this
  }

  public open fun withVerdi(verdi: String?): JsonTelefonnummer {
    this.verdi = verdi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonTelefonnummer {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonTelefonnummer) return false
    return kilde == other.kilde &&
        verdi == other.verdi &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (verdi?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonTelefonnummer(kilde=$kilde, verdi=$verdi, additionalProperties=$additionalProperties)"
}
