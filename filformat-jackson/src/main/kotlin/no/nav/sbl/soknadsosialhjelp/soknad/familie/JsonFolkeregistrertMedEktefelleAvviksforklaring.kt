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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * Forklaring
 *
 * Feltet er utdatert.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "verdi")
public open class JsonFolkeregistrertMedEktefelleAvviksforklaring : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift).")
  public var verdi: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonFolkeregistrertMedEktefelleAvviksforklaring {
    this.kilde = kilde
    return this
  }

  public open fun withVerdi(verdi: String?): JsonFolkeregistrertMedEktefelleAvviksforklaring {
    this.verdi = verdi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonFolkeregistrertMedEktefelleAvviksforklaring {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonFolkeregistrertMedEktefelleAvviksforklaring) return false
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

  override fun toString(): String = "JsonFolkeregistrertMedEktefelleAvviksforklaring(kilde=$kilde, verdi=$verdi, additionalProperties=$additionalProperties)"
}
