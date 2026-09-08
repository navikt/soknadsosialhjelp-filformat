package no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning

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
 * Organisasjon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("navn", "organisasjonsnummer")
public open class JsonOrganisasjon : Serializable {
  @get:JsonProperty("navn")
  @set:JsonProperty("navn")
  @get:JsonPropertyDescription("Navn på organisasjonen.")
  public var navn: String? = null

  @get:JsonProperty("organisasjonsnummer")
  @set:JsonProperty("organisasjonsnummer")
  @get:JsonPropertyDescription("Organisasjonsnummer.")
  public var organisasjonsnummer: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withNavn(navn: String?): JsonOrganisasjon {
    this.navn = navn
    return this
  }

  public open fun withOrganisasjonsnummer(organisasjonsnummer: String?): JsonOrganisasjon {
    this.organisasjonsnummer = organisasjonsnummer
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOrganisasjon {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOrganisasjon) return false
    return navn == other.navn &&
        organisasjonsnummer == other.organisasjonsnummer &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (navn?.hashCode() ?: 0)
    result = result * 31 + (organisasjonsnummer?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOrganisasjon(navn=$navn, organisasjonsnummer=$organisasjonsnummer, additionalProperties=$additionalProperties)"
}
