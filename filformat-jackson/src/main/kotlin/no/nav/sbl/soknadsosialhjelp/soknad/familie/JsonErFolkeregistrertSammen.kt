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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeSystem

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "verdi")
public open class JsonErFolkeregistrertSammen : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeSystem? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  public var verdi: Boolean? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeSystem?): JsonErFolkeregistrertSammen {
    this.kilde = kilde
    return this
  }

  public open fun withVerdi(verdi: Boolean?): JsonErFolkeregistrertSammen {
    this.verdi = verdi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonErFolkeregistrertSammen {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonErFolkeregistrertSammen) return false
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

  override fun toString(): String = "JsonErFolkeregistrertSammen(kilde=$kilde, verdi=$verdi, additionalProperties=$additionalProperties)"
}
