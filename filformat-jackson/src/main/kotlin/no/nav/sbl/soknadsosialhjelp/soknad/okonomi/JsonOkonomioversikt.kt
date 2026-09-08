package no.nav.sbl.soknadsosialhjelp.soknad.okonomi

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
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktFormue
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktInntekt
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt.JsonOkonomioversiktUtgift

/**
 * Strukturert økonomisk oversikt.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("inntekt", "utgift", "formue")
public open class JsonOkonomioversikt : Serializable {
  @get:JsonProperty("inntekt")
  @set:JsonProperty("inntekt")
  @get:JsonPropertyDescription("Månedlige inntekter")
  public var inntekt: List<JsonOkonomioversiktInntekt>? = mutableListOf()

  @get:JsonProperty("utgift")
  @set:JsonProperty("utgift")
  @get:JsonPropertyDescription("Månedlige utgifter")
  public var utgift: List<JsonOkonomioversiktUtgift>? = mutableListOf()

  @get:JsonProperty("formue")
  @set:JsonProperty("formue")
  @get:JsonPropertyDescription("Formue")
  public var formue: List<JsonOkonomioversiktFormue>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withInntekt(inntekt: List<JsonOkonomioversiktInntekt>?): JsonOkonomioversikt {
    this.inntekt = inntekt
    return this
  }

  public open fun withUtgift(utgift: List<JsonOkonomioversiktUtgift>?): JsonOkonomioversikt {
    this.utgift = utgift
    return this
  }

  public open fun withFormue(formue: List<JsonOkonomioversiktFormue>?): JsonOkonomioversikt {
    this.formue = formue
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomioversikt {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomioversikt) return false
    return inntekt == other.inntekt &&
        utgift == other.utgift &&
        formue == other.formue &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (inntekt?.hashCode() ?: 0)
    result = result * 31 + (utgift?.hashCode() ?: 0)
    result = result * 31 + (formue?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomioversikt(inntekt=$inntekt, utgift=$utgift, formue=$formue, additionalProperties=$additionalProperties)"
}
