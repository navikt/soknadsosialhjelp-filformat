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
import no.nav.sbl.soknadsosialhjelp.soknad.bostotte.JsonBostotte
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtbetaling
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomiOpplysningUtgift
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomibekreftelse
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning.JsonOkonomibeskrivelserAvAnnet

/**
 * Økonomiske opplysninger som ikke inngår i den strukturerte oversikten.
 *
 * Flott hvis saksbehandlers behov/ønsker kan diskuteres på Slack slik at en mer strukturert måte å presentere dataene på kan utarbeides.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("bekreftelse", "beskrivelseAvAnnet", "utbetaling", "utgift", "bostotte")
public open class JsonOkonomiopplysninger : Serializable {
  @get:JsonProperty("bekreftelse")
  @set:JsonProperty("bekreftelse")
  @get:JsonPropertyDescription("Bekreftelser fra bruker. Både ja/nei-svar og samtykker fra bruker.")
  public var bekreftelse: List<JsonOkonomibekreftelse>? = mutableListOf()

  @get:JsonProperty("beskrivelseAvAnnet")
  @set:JsonProperty("beskrivelseAvAnnet")
  @get:JsonPropertyDescription("Beskrivelsesfelter for inntekter, utgifter og verdier. Disse feltene er overflødige og vil bli tatt bort.")
  public var beskrivelseAvAnnet: JsonOkonomibeskrivelserAvAnnet? = null

  @get:JsonProperty("utbetaling")
  @set:JsonProperty("utbetaling")
  public var utbetaling: List<JsonOkonomiOpplysningUtbetaling>? = mutableListOf()

  @get:JsonProperty("utgift")
  @set:JsonProperty("utgift")
  @get:JsonPropertyDescription("Månedlige utgifter")
  public var utgift: List<JsonOkonomiOpplysningUtgift>? = mutableListOf()

  @get:JsonProperty("bostotte")
  @set:JsonProperty("bostotte")
  public var bostotte: JsonBostotte? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withBekreftelse(bekreftelse: List<JsonOkonomibekreftelse>?): JsonOkonomiopplysninger {
    this.bekreftelse = bekreftelse
    return this
  }

  public open fun withBeskrivelseAvAnnet(beskrivelseAvAnnet: JsonOkonomibeskrivelserAvAnnet?): JsonOkonomiopplysninger {
    this.beskrivelseAvAnnet = beskrivelseAvAnnet
    return this
  }

  public open fun withUtbetaling(utbetaling: List<JsonOkonomiOpplysningUtbetaling>?): JsonOkonomiopplysninger {
    this.utbetaling = utbetaling
    return this
  }

  public open fun withUtgift(utgift: List<JsonOkonomiOpplysningUtgift>?): JsonOkonomiopplysninger {
    this.utgift = utgift
    return this
  }

  public open fun withBostotte(bostotte: JsonBostotte?): JsonOkonomiopplysninger {
    this.bostotte = bostotte
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomiopplysninger {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomiopplysninger) return false
    return bekreftelse == other.bekreftelse &&
        beskrivelseAvAnnet == other.beskrivelseAvAnnet &&
        utbetaling == other.utbetaling &&
        utgift == other.utgift &&
        bostotte == other.bostotte &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (bekreftelse?.hashCode() ?: 0)
    result = result * 31 + (beskrivelseAvAnnet?.hashCode() ?: 0)
    result = result * 31 + (utbetaling?.hashCode() ?: 0)
    result = result * 31 + (utgift?.hashCode() ?: 0)
    result = result * 31 + (bostotte?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomiopplysninger(bekreftelse=$bekreftelse, beskrivelseAvAnnet=$beskrivelseAvAnnet, utbetaling=$utbetaling, utgift=$utgift, bostotte=$bostotte, additionalProperties=$additionalProperties)"
}
