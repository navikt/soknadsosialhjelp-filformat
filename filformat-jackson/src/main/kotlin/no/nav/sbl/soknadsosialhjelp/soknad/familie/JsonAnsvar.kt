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

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("barn", "borSammenMed", "erFolkeregistrertSammen", "harDeltBosted", "samvarsgrad")
public open class JsonAnsvar : Serializable {
  @get:JsonProperty("barn")
  @set:JsonProperty("barn")
  public var barn: JsonBarn? = null

  @get:JsonProperty("borSammenMed")
  @set:JsonProperty("borSammenMed")
  public var borSammenMed: JsonBorSammenMed? = null

  @get:JsonProperty("erFolkeregistrertSammen")
  @set:JsonProperty("erFolkeregistrertSammen")
  public var erFolkeregistrertSammen: JsonErFolkeregistrertSammen? = null

  @get:JsonProperty("harDeltBosted")
  @set:JsonProperty("harDeltBosted")
  public var harDeltBosted: JsonHarDeltBosted? = null

  @get:JsonProperty("samvarsgrad")
  @set:JsonProperty("samvarsgrad")
  public var samvarsgrad: JsonSamvarsgrad? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withBarn(barn: JsonBarn?): JsonAnsvar {
    this.barn = barn
    return this
  }

  public open fun withBorSammenMed(borSammenMed: JsonBorSammenMed?): JsonAnsvar {
    this.borSammenMed = borSammenMed
    return this
  }

  public open fun withErFolkeregistrertSammen(erFolkeregistrertSammen: JsonErFolkeregistrertSammen?): JsonAnsvar {
    this.erFolkeregistrertSammen = erFolkeregistrertSammen
    return this
  }

  public open fun withHarDeltBosted(harDeltBosted: JsonHarDeltBosted?): JsonAnsvar {
    this.harDeltBosted = harDeltBosted
    return this
  }

  public open fun withSamvarsgrad(samvarsgrad: JsonSamvarsgrad?): JsonAnsvar {
    this.samvarsgrad = samvarsgrad
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonAnsvar {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonAnsvar) return false
    return barn == other.barn &&
        borSammenMed == other.borSammenMed &&
        erFolkeregistrertSammen == other.erFolkeregistrertSammen &&
        harDeltBosted == other.harDeltBosted &&
        samvarsgrad == other.samvarsgrad &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (barn?.hashCode() ?: 0)
    result = result * 31 + (borSammenMed?.hashCode() ?: 0)
    result = result * 31 + (erFolkeregistrertSammen?.hashCode() ?: 0)
    result = result * 31 + (harDeltBosted?.hashCode() ?: 0)
    result = result * 31 + (samvarsgrad?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonAnsvar(barn=$barn, borSammenMed=$borSammenMed, erFolkeregistrertSammen=$erFolkeregistrertSammen, harDeltBosted=$harDeltBosted, samvarsgrad=$samvarsgrad, additionalProperties=$additionalProperties)"
}
