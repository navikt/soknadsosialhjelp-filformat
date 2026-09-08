package no.nav.sbl.soknadsosialhjelp.soknad.common

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
 * Navn på en person.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("fornavn", "mellomnavn", "etternavn")
public open class JsonNavn : Serializable {
  @get:JsonProperty("fornavn")
  @set:JsonProperty("fornavn")
  @get:JsonPropertyDescription("Feltet kan være blankt.")
  public var fornavn: String? = null

  @get:JsonProperty("mellomnavn")
  @set:JsonProperty("mellomnavn")
  @get:JsonPropertyDescription("Feltet kan være blankt.")
  public var mellomnavn: String? = null

  @get:JsonProperty("etternavn")
  @set:JsonProperty("etternavn")
  @get:JsonPropertyDescription("Feltet kan være blankt.")
  public var etternavn: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withFornavn(fornavn: String?): JsonNavn {
    this.fornavn = fornavn
    return this
  }

  public open fun withMellomnavn(mellomnavn: String?): JsonNavn {
    this.mellomnavn = mellomnavn
    return this
  }

  public open fun withEtternavn(etternavn: String?): JsonNavn {
    this.etternavn = etternavn
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonNavn {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonNavn) return false
    return fornavn == other.fornavn &&
        mellomnavn == other.mellomnavn &&
        etternavn == other.etternavn &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (fornavn?.hashCode() ?: 0)
    result = result * 31 + (mellomnavn?.hashCode() ?: 0)
    result = result * 31 + (etternavn?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonNavn(fornavn=$fornavn, mellomnavn=$mellomnavn, etternavn=$etternavn, additionalProperties=$additionalProperties)"
}
