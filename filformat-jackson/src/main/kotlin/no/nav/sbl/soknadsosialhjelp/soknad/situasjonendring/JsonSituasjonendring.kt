package no.nav.sbl.soknadsosialhjelp.soknad.situasjonendring

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * Endring i situasjon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "harNoeEndretSeg", "hvaHarEndretSeg")
public open class JsonSituasjonendring : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("harNoeEndretSeg")
  @set:JsonProperty("harNoeEndretSeg")
  @get:JsonPropertyDescription("Brukers svar på om noe har endret seg siden forrige søknad (gjelder kort søknad).")
  public var harNoeEndretSeg: Boolean? = null

  @get:JsonProperty("hvaHarEndretSeg")
  @set:JsonProperty("hvaHarEndretSeg")
  @get:JsonPropertyDescription("Brukerskrevet forklaring på hva som har endret seg siden forrige søknad (gjelder kort søknad).")
  public var hvaHarEndretSeg: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonSituasjonendring {
    this.kilde = kilde
    return this
  }

  public open fun withHarNoeEndretSeg(harNoeEndretSeg: Boolean?): JsonSituasjonendring {
    this.harNoeEndretSeg = harNoeEndretSeg
    return this
  }

  public open fun withHvaHarEndretSeg(hvaHarEndretSeg: String?): JsonSituasjonendring {
    this.hvaHarEndretSeg = hvaHarEndretSeg
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonSituasjonendring {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSituasjonendring) return false
    return kilde == other.kilde &&
        harNoeEndretSeg == other.harNoeEndretSeg &&
        hvaHarEndretSeg == other.hvaHarEndretSeg &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (harNoeEndretSeg?.hashCode() ?: 0)
    result = result * 31 + (hvaHarEndretSeg?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonSituasjonendring(kilde=$kilde, harNoeEndretSeg=$harNoeEndretSeg, hvaHarEndretSeg=$hvaHarEndretSeg, additionalProperties=$additionalProperties)"
}
