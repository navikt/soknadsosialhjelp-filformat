package no.nav.sbl.soknadsosialhjelp.soknad.arbeid

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
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * Arbeid
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("forhold", "situasjon", "kommentarTilArbeidsforhold")
public open class JsonArbeid : Serializable {
  @get:JsonProperty("forhold")
  @set:JsonProperty("forhold")
  @get:JsonPropertyDescription("Liste over arbeidsforhold de siste 3 månedene. Hvis \"forhold\" mangler betyr dette at man ikke har fått data fra AA-registeret. Feltet \"situasjon\" vil da istedenfor benyttes.")
  public var forhold: List<JsonArbeidsforhold>? = mutableListOf()

  @get:JsonProperty("situasjon")
  @set:JsonProperty("situasjon")
  public var situasjon: JsonArbeidssituasjon? = null

  @get:JsonProperty("kommentarTilArbeidsforhold")
  @set:JsonProperty("kommentarTilArbeidsforhold")
  public var kommentarTilArbeidsforhold: JsonKommentarTilArbeidsforhold? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withForhold(forhold: List<JsonArbeidsforhold>?): JsonArbeid {
    this.forhold = forhold
    return this
  }

  public open fun withSituasjon(situasjon: JsonArbeidssituasjon?): JsonArbeid {
    this.situasjon = situasjon
    return this
  }

  public open fun withKommentarTilArbeidsforhold(kommentarTilArbeidsforhold: JsonKommentarTilArbeidsforhold?): JsonArbeid {
    this.kommentarTilArbeidsforhold = kommentarTilArbeidsforhold
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonArbeid {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonArbeid) return false
    return forhold == other.forhold &&
        situasjon == other.situasjon &&
        kommentarTilArbeidsforhold == other.kommentarTilArbeidsforhold &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (forhold?.hashCode() ?: 0)
    result = result * 31 + (situasjon?.hashCode() ?: 0)
    result = result * 31 + (kommentarTilArbeidsforhold?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonArbeid(forhold=$forhold, situasjon=$situasjon, kommentarTilArbeidsforhold=$kommentarTilArbeidsforhold, additionalProperties=$additionalProperties)"
}
