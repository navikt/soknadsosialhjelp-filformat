package no.nav.sbl.soknadsosialhjelp.digisos.soker

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

/**
 * Vedlegg til vedtaksfil, forvaltningsbrev eller dokumentasjonEtterspurt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("tittel", "referanse")
public open class JsonVedlegg : Serializable {
  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("En tittel som kan brukes som lenke/forklaringstekst til hva vedlegget er.")
  public var tittel: String? = null

  @get:JsonProperty("referanse")
  @set:JsonProperty("referanse")
  public var referanse: JsonFilreferanse? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withTittel(tittel: String?): JsonVedlegg {
    this.tittel = tittel
    return this
  }

  public open fun withReferanse(referanse: JsonFilreferanse?): JsonVedlegg {
    this.referanse = referanse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonVedlegg {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonVedlegg) return false
    return tittel == other.tittel &&
        referanse == other.referanse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (referanse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonVedlegg(tittel=$tittel, referanse=$referanse, additionalProperties=$additionalProperties)"
}
