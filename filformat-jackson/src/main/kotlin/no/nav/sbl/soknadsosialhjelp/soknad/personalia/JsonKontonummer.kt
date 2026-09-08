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
 * Søkers kontonummer
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "harIkkeKonto", "verdi")
public open class JsonKontonummer : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("harIkkeKonto")
  @set:JsonProperty("harIkkeKonto")
  @get:JsonPropertyDescription("Bruker har eksplisitt sagt at han/hun ikke har noen konto som kan benyttes. Hvis \"harIkkeKonto\" mangler betyr dette at bruker hverken har angitt kontonummer eller huket av for at han/hun mangler konto.")
  public var harIkkeKonto: Boolean? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  @get:JsonPropertyDescription("Hvis \"verdi\" mangler betyr dette at bruker ikke har lagt inn noe kontonummer. Hvis angitt er \"verdi\" et norsk kontnummer, dvs 11-sifret og modulus-11-gyldig. Hvis norsk definisjon av kontonummer endres vil dette formatet også bli endret. Eventuelle konsumenter bør ta høyde for dette.")
  public var verdi: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonKontonummer {
    this.kilde = kilde
    return this
  }

  public open fun withHarIkkeKonto(harIkkeKonto: Boolean?): JsonKontonummer {
    this.harIkkeKonto = harIkkeKonto
    return this
  }

  public open fun withVerdi(verdi: String?): JsonKontonummer {
    this.verdi = verdi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonKontonummer {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonKontonummer) return false
    return kilde == other.kilde &&
        harIkkeKonto == other.harIkkeKonto &&
        verdi == other.verdi &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (harIkkeKonto?.hashCode() ?: 0)
    result = result * 31 + (verdi?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonKontonummer(kilde=$kilde, harIkkeKonto=$harIkkeKonto, verdi=$verdi, additionalProperties=$additionalProperties)"
}
