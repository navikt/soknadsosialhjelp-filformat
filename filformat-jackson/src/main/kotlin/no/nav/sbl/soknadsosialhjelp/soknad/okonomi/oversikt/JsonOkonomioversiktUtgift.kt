package no.nav.sbl.soknadsosialhjelp.soknad.okonomi.oversikt

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "type", "tittel", "belop", "overstyrtAvBruker")
public open class JsonOkonomioversiktUtgift : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Kodeverdi som angir hva slags type utgift det er. Dette feltet kan for eksempel brukes til å filtrere bort utgifter man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: \"barnebidrag\", \"husleie\", \"boliglanAvdrag\", \"boliglanRenter\", \"barnehage\" og \"sfo\".")
  public var type: String? = null

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("En tittel som MÅ brukes hvis utgiften skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.")
  public var tittel: String? = null

  @get:JsonProperty("belop")
  @set:JsonProperty("belop")
  @get:JsonPropertyDescription("Kan mangle hvis bruker har sagt at han/hun har en gitt type utgift, men beløp mangler.")
  public var belop: Int? = null

  @get:JsonProperty("overstyrtAvBruker")
  @set:JsonProperty("overstyrtAvBruker")
  @get:JsonPropertyDescription("Brukes når en søker overstyrer/endrer. Settes kun til \"true\" på utgift med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).")
  public var overstyrtAvBruker: Boolean? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonOkonomioversiktUtgift {
    this.kilde = kilde
    return this
  }

  public open fun withType(type: String?): JsonOkonomioversiktUtgift {
    this.type = type
    return this
  }

  public open fun withTittel(tittel: String?): JsonOkonomioversiktUtgift {
    this.tittel = tittel
    return this
  }

  public open fun withBelop(belop: Int?): JsonOkonomioversiktUtgift {
    this.belop = belop
    return this
  }

  public open fun withOverstyrtAvBruker(overstyrtAvBruker: Boolean?): JsonOkonomioversiktUtgift {
    this.overstyrtAvBruker = overstyrtAvBruker
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomioversiktUtgift {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomioversiktUtgift) return false
    return kilde == other.kilde &&
        type == other.type &&
        tittel == other.tittel &&
        belop == other.belop &&
        overstyrtAvBruker == other.overstyrtAvBruker &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (belop?.hashCode() ?: 0)
    result = result * 31 + (overstyrtAvBruker?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomioversiktUtgift(kilde=$kilde, type=$type, tittel=$tittel, belop=$belop, overstyrtAvBruker=$overstyrtAvBruker, additionalProperties=$additionalProperties)"
}
