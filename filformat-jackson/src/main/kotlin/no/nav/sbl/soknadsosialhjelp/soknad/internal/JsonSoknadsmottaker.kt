package no.nav.sbl.soknadsosialhjelp.soknad.`internal`

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
 * Soknadsmottaker
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("organisasjonsnummer", "navEnhetsnavn")
public open class JsonSoknadsmottaker : Serializable {
  @get:JsonProperty("organisasjonsnummer")
  @set:JsonProperty("organisasjonsnummer")
  @get:JsonPropertyDescription("Organisasjonsnummer for søknadsmottaker.")
  public var organisasjonsnummer: String? = null

  @get:JsonProperty("navEnhetsnavn")
  @set:JsonProperty("navEnhetsnavn")
  @get:JsonPropertyDescription("Navn på NAV-enhet som søknaden sendes til.")
  public var navEnhetsnavn: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withOrganisasjonsnummer(organisasjonsnummer: String?): JsonSoknadsmottaker {
    this.organisasjonsnummer = organisasjonsnummer
    return this
  }

  public open fun withNavEnhetsnavn(navEnhetsnavn: String?): JsonSoknadsmottaker {
    this.navEnhetsnavn = navEnhetsnavn
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonSoknadsmottaker {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSoknadsmottaker) return false
    return organisasjonsnummer == other.organisasjonsnummer &&
        navEnhetsnavn == other.navEnhetsnavn &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (organisasjonsnummer?.hashCode() ?: 0)
    result = result * 31 + (navEnhetsnavn?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonSoknadsmottaker(organisasjonsnummer=$organisasjonsnummer, navEnhetsnavn=$navEnhetsnavn, additionalProperties=$additionalProperties)"
}
