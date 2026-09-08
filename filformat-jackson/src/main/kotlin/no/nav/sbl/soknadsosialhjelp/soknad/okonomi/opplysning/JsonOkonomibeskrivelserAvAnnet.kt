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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * Beskrivelsesfelter for inntekter, utgifter og verdier.
 *
 * Disse feltene er overflødige og vil bli tatt bort.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "verdi", "sparing", "utbetaling", "boutgifter", "barneutgifter")
public open class JsonOkonomibeskrivelserAvAnnet : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av økonomisk verdi.")
  public var verdi: String? = null

  @get:JsonProperty("sparing")
  @set:JsonProperty("sparing")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av innskudd eller sparing.")
  public var sparing: String? = null

  @get:JsonProperty("utbetaling")
  @set:JsonProperty("utbetaling")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av utbetalinger.")
  public var utbetaling: String? = null

  @get:JsonProperty("boutgifter")
  @set:JsonProperty("boutgifter")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av boutgifter. Feltet er utdatert.")
  public var boutgifter: String? = null

  @get:JsonProperty("barneutgifter")
  @set:JsonProperty("barneutgifter")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift) av andre utgifter til barn som brukeren har. Feltet er utdatert.")
  public var barneutgifter: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonOkonomibeskrivelserAvAnnet {
    this.kilde = kilde
    return this
  }

  public open fun withVerdi(verdi: String?): JsonOkonomibeskrivelserAvAnnet {
    this.verdi = verdi
    return this
  }

  public open fun withSparing(sparing: String?): JsonOkonomibeskrivelserAvAnnet {
    this.sparing = sparing
    return this
  }

  public open fun withUtbetaling(utbetaling: String?): JsonOkonomibeskrivelserAvAnnet {
    this.utbetaling = utbetaling
    return this
  }

  public open fun withBoutgifter(boutgifter: String?): JsonOkonomibeskrivelserAvAnnet {
    this.boutgifter = boutgifter
    return this
  }

  public open fun withBarneutgifter(barneutgifter: String?): JsonOkonomibeskrivelserAvAnnet {
    this.barneutgifter = barneutgifter
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomibeskrivelserAvAnnet {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomibeskrivelserAvAnnet) return false
    return kilde == other.kilde &&
        verdi == other.verdi &&
        sparing == other.sparing &&
        utbetaling == other.utbetaling &&
        boutgifter == other.boutgifter &&
        barneutgifter == other.barneutgifter &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (verdi?.hashCode() ?: 0)
    result = result * 31 + (sparing?.hashCode() ?: 0)
    result = result * 31 + (utbetaling?.hashCode() ?: 0)
    result = result * 31 + (boutgifter?.hashCode() ?: 0)
    result = result * 31 + (barneutgifter?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomibeskrivelserAvAnnet(kilde=$kilde, verdi=$verdi, sparing=$sparing, utbetaling=$utbetaling, boutgifter=$boutgifter, barneutgifter=$barneutgifter, additionalProperties=$additionalProperties)"
}
