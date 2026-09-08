package no.nav.sbl.soknadsosialhjelp.soknad

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
import no.nav.sbl.soknadsosialhjelp.soknad.`internal`.JsonSoknadsmottaker
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse
import no.nav.sbl.soknadsosialhjelp.vedlegg.JsonVedleggSpesifikasjon

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("soknad", "vedlegg", "mottaker", "midlertidigAdresse")
public open class JsonInternalSoknad : Serializable {
  @get:JsonProperty("soknad")
  @set:JsonProperty("soknad")
  public var soknad: JsonSoknad? = null

  @get:JsonProperty("vedlegg")
  @set:JsonProperty("vedlegg")
  public var vedlegg: JsonVedleggSpesifikasjon? = null

  @get:JsonProperty("mottaker")
  @set:JsonProperty("mottaker")
  @get:JsonPropertyDescription("Soknadsmottaker")
  public var mottaker: JsonSoknadsmottaker? = null

  @get:JsonProperty("midlertidigAdresse")
  @set:JsonProperty("midlertidigAdresse")
  public var midlertidigAdresse: JsonAdresse? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSoknad(soknad: JsonSoknad?): JsonInternalSoknad {
    this.soknad = soknad
    return this
  }

  public open fun withVedlegg(vedlegg: JsonVedleggSpesifikasjon?): JsonInternalSoknad {
    this.vedlegg = vedlegg
    return this
  }

  public open fun withMottaker(mottaker: JsonSoknadsmottaker?): JsonInternalSoknad {
    this.mottaker = mottaker
    return this
  }

  public open fun withMidlertidigAdresse(midlertidigAdresse: JsonAdresse?): JsonInternalSoknad {
    this.midlertidigAdresse = midlertidigAdresse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonInternalSoknad {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonInternalSoknad) return false
    return soknad == other.soknad &&
        vedlegg == other.vedlegg &&
        mottaker == other.mottaker &&
        midlertidigAdresse == other.midlertidigAdresse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (soknad?.hashCode() ?: 0)
    result = result * 31 + (vedlegg?.hashCode() ?: 0)
    result = result * 31 + (mottaker?.hashCode() ?: 0)
    result = result * 31 + (midlertidigAdresse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonInternalSoknad(soknad=$soknad, vedlegg=$vedlegg, mottaker=$mottaker, midlertidigAdresse=$midlertidigAdresse, additionalProperties=$additionalProperties)"
}
