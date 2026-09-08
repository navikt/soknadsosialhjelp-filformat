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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "type", "tittel", "verdi", "bekreftelsesDato")
public open class JsonOkonomibekreftelse : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Kodeverdi som angir hva slags type bekreftelse det er. Dette feltet kan for eksempel brukes til å filtrere bort bekreftelser man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: \"bostotte\", \"verdi\", \"sparing\", \"utbetaling\", \"boutgifter\" og \"barneutgifter\".")
  public var type: String? = null

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("En tittel som MÅ brukes hvis bekreftelsen skal presenteres til saksbehandler.")
  public var tittel: String? = null

  @get:JsonProperty("verdi")
  @set:JsonProperty("verdi")
  @get:JsonPropertyDescription("Kan være manglende hvis bruker ikke har besvart spørsmålet.")
  public var verdi: Boolean? = null

  @get:JsonProperty("bekreftelsesDato")
  @set:JsonProperty("bekreftelsesDato")
  @get:JsonPropertyDescription("Tidspunkt for når denne bekreftelsen ble gitt.")
  public var bekreftelsesDato: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonOkonomibekreftelse {
    this.kilde = kilde
    return this
  }

  public open fun withType(type: String?): JsonOkonomibekreftelse {
    this.type = type
    return this
  }

  public open fun withTittel(tittel: String?): JsonOkonomibekreftelse {
    this.tittel = tittel
    return this
  }

  public open fun withVerdi(verdi: Boolean?): JsonOkonomibekreftelse {
    this.verdi = verdi
    return this
  }

  public open fun withBekreftelsesDato(bekreftelsesDato: String?): JsonOkonomibekreftelse {
    this.bekreftelsesDato = bekreftelsesDato
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomibekreftelse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomibekreftelse) return false
    return kilde == other.kilde &&
        type == other.type &&
        tittel == other.tittel &&
        verdi == other.verdi &&
        bekreftelsesDato == other.bekreftelsesDato &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (verdi?.hashCode() ?: 0)
    result = result * 31 + (bekreftelsesDato?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomibekreftelse(kilde=$kilde, type=$type, tittel=$tittel, verdi=$verdi, bekreftelsesDato=$bekreftelsesDato, additionalProperties=$additionalProperties)"
}
