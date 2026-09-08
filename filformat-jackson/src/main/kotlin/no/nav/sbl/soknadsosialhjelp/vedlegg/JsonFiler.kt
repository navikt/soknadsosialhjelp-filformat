package no.nav.sbl.soknadsosialhjelp.vedlegg

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
@JsonPropertyOrder("filnavn", "sha512")
public open class JsonFiler : Serializable {
  @get:JsonProperty("filnavn")
  @set:JsonProperty("filnavn")
  public var filnavn: String? = null

  @get:JsonProperty("sha512")
  @set:JsonProperty("sha512")
  public var sha512: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withFilnavn(filnavn: String?): JsonFiler {
    this.filnavn = filnavn
    return this
  }

  public open fun withSha512(sha512: String?): JsonFiler {
    this.sha512 = sha512
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonFiler {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonFiler) return false
    return filnavn == other.filnavn &&
        sha512 == other.sha512 &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (filnavn?.hashCode() ?: 0)
    result = result * 31 + (sha512?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonFiler(filnavn=$filnavn, sha512=$sha512, additionalProperties=$additionalProperties)"
}
