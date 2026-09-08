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
 * Fagsystemet
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("systemnavn", "systemversjon")
public open class JsonAvsender : Serializable {
  @get:JsonProperty("systemnavn")
  @set:JsonProperty("systemnavn")
  @get:JsonPropertyDescription("Navnet på avsendersystemet. Brukes ved eventuell feilsøking av ugyldige data.")
  public var systemnavn: String? = null

  @get:JsonProperty("systemversjon")
  @set:JsonProperty("systemversjon")
  @get:JsonPropertyDescription("Versjonen til avsendersystemet. Brukes ved eventuell feilsøking av ugyldige data.")
  public var systemversjon: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSystemnavn(systemnavn: String?): JsonAvsender {
    this.systemnavn = systemnavn
    return this
  }

  public open fun withSystemversjon(systemversjon: String?): JsonAvsender {
    this.systemversjon = systemversjon
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonAvsender {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonAvsender) return false
    return systemnavn == other.systemnavn &&
        systemversjon == other.systemversjon &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (systemnavn?.hashCode() ?: 0)
    result = result * 31 + (systemversjon?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonAvsender(systemnavn=$systemnavn, systemversjon=$systemversjon, additionalProperties=$additionalProperties)"
}
