package no.nav.sbl.soknadsosialhjelp.soknad.okonomi.opplysning

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.List
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Utbetalinger søker har mottatt
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "type", "tittel", "organisasjon", "belop", "netto", "brutto", "skattetrekk", "andreTrekk", "utbetalingsdato", "periodeFom", "periodeTom", "komponenter", "overstyrtAvBruker", "mottaker")
public open class JsonOkonomiOpplysningUtbetaling : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Kodeverdi som angir hva slags type utbetaling det er. Dette feltet kan for eksempel brukes til å filtrere bort utbetalinger man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: \"utbytte\", \"salg\", \"forsikring\" og \"annen\".")
  public var type: String? = null

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("En tittel som MÅ brukes hvis utbetalingen skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.")
  public var tittel: String? = null

  @get:JsonProperty("organisasjon")
  @set:JsonProperty("organisasjon")
  public var organisasjon: JsonOrganisasjon? = null

  @get:JsonProperty("belop")
  @set:JsonProperty("belop")
  public var belop: Int? = null

  @get:JsonProperty("netto")
  @set:JsonProperty("netto")
  @get:JsonPropertyDescription("Nettobeløp for utbetalingen.")
  public var netto: Double? = null

  @get:JsonProperty("brutto")
  @set:JsonProperty("brutto")
  @get:JsonPropertyDescription("Bruttobeløp for utbetalingen.")
  public var brutto: Double? = null

  @get:JsonProperty("skattetrekk")
  @set:JsonProperty("skattetrekk")
  @get:JsonPropertyDescription("Totalsum for skattetrekk som gjøres for utbetalingen.")
  public var skattetrekk: Double? = null

  @get:JsonProperty("andreTrekk")
  @set:JsonProperty("andreTrekk")
  @get:JsonPropertyDescription("Totalsum for andre trekk som gjøres for utbetalingen.")
  public var andreTrekk: Double? = null

  @get:JsonProperty("utbetalingsdato")
  @set:JsonProperty("utbetalingsdato")
  public var utbetalingsdato: String? = null

  @get:JsonProperty("periodeFom")
  @set:JsonProperty("periodeFom")
  @get:JsonPropertyDescription("Ytelsen som gir utbetalingen gjelder fra og med denne datoen.")
  public var periodeFom: String? = null

  @get:JsonProperty("periodeTom")
  @set:JsonProperty("periodeTom")
  @get:JsonPropertyDescription("Ytelsen som gir utbetalingen gjelder til og med denne datoen.")
  public var periodeTom: String? = null

  @get:JsonProperty("komponenter")
  @set:JsonProperty("komponenter")
  @get:JsonPropertyDescription("Liste over delutbetalinger hvis utbetalingen består av flere deler.")
  public var komponenter: List<JsonOkonomiOpplysningUtbetalingKomponent>? = mutableListOf()

  @get:JsonProperty("overstyrtAvBruker")
  @set:JsonProperty("overstyrtAvBruker")
  @get:JsonPropertyDescription("Brukes når en søker overstyrer/endrer. Settes kun til \"true\" på utbetaling med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).")
  public var overstyrtAvBruker: Boolean? = null

  @get:JsonProperty("mottaker")
  @set:JsonProperty("mottaker")
  @get:JsonPropertyDescription("Hvem som har mottatt utbetalingen. Eksempler: \"Husstand\", \"Kommune\".")
  public var mottaker: Mottaker? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonOkonomiOpplysningUtbetaling {
    this.kilde = kilde
    return this
  }

  public open fun withType(type: String?): JsonOkonomiOpplysningUtbetaling {
    this.type = type
    return this
  }

  public open fun withTittel(tittel: String?): JsonOkonomiOpplysningUtbetaling {
    this.tittel = tittel
    return this
  }

  public open fun withOrganisasjon(organisasjon: JsonOrganisasjon?): JsonOkonomiOpplysningUtbetaling {
    this.organisasjon = organisasjon
    return this
  }

  public open fun withBelop(belop: Int?): JsonOkonomiOpplysningUtbetaling {
    this.belop = belop
    return this
  }

  public open fun withNetto(netto: Double?): JsonOkonomiOpplysningUtbetaling {
    this.netto = netto
    return this
  }

  public open fun withBrutto(brutto: Double?): JsonOkonomiOpplysningUtbetaling {
    this.brutto = brutto
    return this
  }

  public open fun withSkattetrekk(skattetrekk: Double?): JsonOkonomiOpplysningUtbetaling {
    this.skattetrekk = skattetrekk
    return this
  }

  public open fun withAndreTrekk(andreTrekk: Double?): JsonOkonomiOpplysningUtbetaling {
    this.andreTrekk = andreTrekk
    return this
  }

  public open fun withUtbetalingsdato(utbetalingsdato: String?): JsonOkonomiOpplysningUtbetaling {
    this.utbetalingsdato = utbetalingsdato
    return this
  }

  public open fun withPeriodeFom(periodeFom: String?): JsonOkonomiOpplysningUtbetaling {
    this.periodeFom = periodeFom
    return this
  }

  public open fun withPeriodeTom(periodeTom: String?): JsonOkonomiOpplysningUtbetaling {
    this.periodeTom = periodeTom
    return this
  }

  public open fun withKomponenter(komponenter: List<JsonOkonomiOpplysningUtbetalingKomponent>?): JsonOkonomiOpplysningUtbetaling {
    this.komponenter = komponenter
    return this
  }

  public open fun withOverstyrtAvBruker(overstyrtAvBruker: Boolean?): JsonOkonomiOpplysningUtbetaling {
    this.overstyrtAvBruker = overstyrtAvBruker
    return this
  }

  public open fun withMottaker(mottaker: Mottaker?): JsonOkonomiOpplysningUtbetaling {
    this.mottaker = mottaker
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomiOpplysningUtbetaling {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomiOpplysningUtbetaling) return false
    return kilde == other.kilde &&
        type == other.type &&
        tittel == other.tittel &&
        organisasjon == other.organisasjon &&
        belop == other.belop &&
        netto == other.netto &&
        brutto == other.brutto &&
        skattetrekk == other.skattetrekk &&
        andreTrekk == other.andreTrekk &&
        utbetalingsdato == other.utbetalingsdato &&
        periodeFom == other.periodeFom &&
        periodeTom == other.periodeTom &&
        komponenter == other.komponenter &&
        overstyrtAvBruker == other.overstyrtAvBruker &&
        mottaker == other.mottaker &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (organisasjon?.hashCode() ?: 0)
    result = result * 31 + (belop?.hashCode() ?: 0)
    result = result * 31 + (netto?.hashCode() ?: 0)
    result = result * 31 + (brutto?.hashCode() ?: 0)
    result = result * 31 + (skattetrekk?.hashCode() ?: 0)
    result = result * 31 + (andreTrekk?.hashCode() ?: 0)
    result = result * 31 + (utbetalingsdato?.hashCode() ?: 0)
    result = result * 31 + (periodeFom?.hashCode() ?: 0)
    result = result * 31 + (periodeTom?.hashCode() ?: 0)
    result = result * 31 + (komponenter?.hashCode() ?: 0)
    result = result * 31 + (overstyrtAvBruker?.hashCode() ?: 0)
    result = result * 31 + (mottaker?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomiOpplysningUtbetaling(kilde=$kilde, type=$type, tittel=$tittel, organisasjon=$organisasjon, belop=$belop, netto=$netto, brutto=$brutto, skattetrekk=$skattetrekk, andreTrekk=$andreTrekk, utbetalingsdato=$utbetalingsdato, periodeFom=$periodeFom, periodeTom=$periodeTom, komponenter=$komponenter, overstyrtAvBruker=$overstyrtAvBruker, mottaker=$mottaker, additionalProperties=$additionalProperties)"

  public enum class Mottaker(
    @JsonValue
    public val `value`: String,
  ) {
    HUSSTAND("Husstand"),
    KOMMUNE("Kommune"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Mottaker = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
