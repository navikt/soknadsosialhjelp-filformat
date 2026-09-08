package no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse

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
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse

/**
 * Utbetaling
 *
 * Utbetalingsinformasjon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("utbetalingsreferanse", "saksreferanse", "rammevedtaksreferanse", "status", "belop", "beskrivelse", "forfallsdato", "utbetalingsdato", "fom", "tom", "annenMottaker", "mottaker", "kontonummer", "utbetalingsmetode")
public open class JsonUtbetaling : JsonHendelse(), Serializable {
  @get:JsonProperty("utbetalingsreferanse")
  @set:JsonProperty("utbetalingsreferanse")
  @get:JsonPropertyDescription("Unik referanse per utbetaling slik at utbetalingsinformasjonen kan bli skiftet ut når det kommer ny informasjon")
  public var utbetalingsreferanse: String? = null

  @get:JsonProperty("saksreferanse")
  @set:JsonProperty("saksreferanse")
  @get:JsonPropertyDescription("Referanse utbetalingen skal tilknyttes til (samme som i vedtak fattet og saksstatus)")
  public var saksreferanse: String? = null

  @get:JsonProperty("rammevedtaksreferanse")
  @set:JsonProperty("rammevedtaksreferanse")
  @get:JsonPropertyDescription("Settes dersom utbetalingen er en del av et rammevedtak")
  public var rammevedtaksreferanse: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Status for utbetalingen")
  public var status: Status? = null

  @get:JsonProperty("belop")
  @set:JsonProperty("belop")
  @get:JsonPropertyDescription("Utbetalingsbeløp i kr")
  public var belop: Double? = null

  @get:JsonProperty("beskrivelse")
  @set:JsonProperty("beskrivelse")
  @get:JsonPropertyDescription("Stønaden utbetalingen gjelder for (livsopphold, strøm etc.)")
  public var beskrivelse: String? = null

  @get:JsonProperty("forfallsdato")
  @set:JsonProperty("forfallsdato")
  @get:JsonPropertyDescription("Når betalingen er lagt til forfall")
  public var forfallsdato: String? = null

  @get:JsonProperty("utbetalingsdato")
  @set:JsonProperty("utbetalingsdato")
  @get:JsonPropertyDescription("Når utbetalingen kom inn på konto")
  public var utbetalingsdato: String? = null

  @get:JsonProperty("fom")
  @set:JsonProperty("fom")
  @get:JsonPropertyDescription("Utbetalingsperiode (Fra)")
  public var fom: String? = null

  @get:JsonProperty("tom")
  @set:JsonProperty("tom")
  @get:JsonPropertyDescription("Utbetalingsperiode (Til)")
  public var tom: String? = null

  @get:JsonProperty("annenMottaker")
  @set:JsonProperty("annenMottaker")
  @get:JsonPropertyDescription("Om en annen mottaker enn brukeren skal ha pengene")
  public var annenMottaker: Boolean? = null

  @get:JsonProperty("mottaker")
  @set:JsonProperty("mottaker")
  @get:JsonPropertyDescription("Mottaker (søker eller annen mottaker), fnummer, orgnummer, eller navn")
  public var mottaker: String? = null

  @get:JsonProperty("kontonummer")
  @set:JsonProperty("kontonummer")
  @get:JsonPropertyDescription("Mottakers kontonummer, bank i Norge, blir bare vist dersom mottaker er brukeren")
  public var kontonummer: String? = null

  @get:JsonProperty("utbetalingsmetode")
  @set:JsonProperty("utbetalingsmetode")
  @get:JsonPropertyDescription("Utbetalingsmetode, eks kontooverføring, kontantkort")
  public var utbetalingsmetode: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withUtbetalingsreferanse(utbetalingsreferanse: String?): JsonUtbetaling {
    this.utbetalingsreferanse = utbetalingsreferanse
    return this
  }

  public open fun withSaksreferanse(saksreferanse: String?): JsonUtbetaling {
    this.saksreferanse = saksreferanse
    return this
  }

  public open fun withRammevedtaksreferanse(rammevedtaksreferanse: String?): JsonUtbetaling {
    this.rammevedtaksreferanse = rammevedtaksreferanse
    return this
  }

  public open fun withStatus(status: Status?): JsonUtbetaling {
    this.status = status
    return this
  }

  public open fun withBelop(belop: Double?): JsonUtbetaling {
    this.belop = belop
    return this
  }

  public open fun withBeskrivelse(beskrivelse: String?): JsonUtbetaling {
    this.beskrivelse = beskrivelse
    return this
  }

  public open fun withForfallsdato(forfallsdato: String?): JsonUtbetaling {
    this.forfallsdato = forfallsdato
    return this
  }

  public open fun withUtbetalingsdato(utbetalingsdato: String?): JsonUtbetaling {
    this.utbetalingsdato = utbetalingsdato
    return this
  }

  public open fun withFom(fom: String?): JsonUtbetaling {
    this.fom = fom
    return this
  }

  public open fun withTom(tom: String?): JsonUtbetaling {
    this.tom = tom
    return this
  }

  public open fun withAnnenMottaker(annenMottaker: Boolean?): JsonUtbetaling {
    this.annenMottaker = annenMottaker
    return this
  }

  public open fun withMottaker(mottaker: String?): JsonUtbetaling {
    this.mottaker = mottaker
    return this
  }

  public open fun withKontonummer(kontonummer: String?): JsonUtbetaling {
    this.kontonummer = kontonummer
    return this
  }

  public open fun withUtbetalingsmetode(utbetalingsmetode: String?): JsonUtbetaling {
    this.utbetalingsmetode = utbetalingsmetode
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonUtbetaling {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonUtbetaling {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonUtbetaling {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonUtbetaling) return false
    return utbetalingsreferanse == other.utbetalingsreferanse &&
        saksreferanse == other.saksreferanse &&
        rammevedtaksreferanse == other.rammevedtaksreferanse &&
        status == other.status &&
        belop == other.belop &&
        beskrivelse == other.beskrivelse &&
        forfallsdato == other.forfallsdato &&
        utbetalingsdato == other.utbetalingsdato &&
        fom == other.fom &&
        tom == other.tom &&
        annenMottaker == other.annenMottaker &&
        mottaker == other.mottaker &&
        kontonummer == other.kontonummer &&
        utbetalingsmetode == other.utbetalingsmetode &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (utbetalingsreferanse?.hashCode() ?: 0)
    result = result * 31 + (saksreferanse?.hashCode() ?: 0)
    result = result * 31 + (rammevedtaksreferanse?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + (belop?.hashCode() ?: 0)
    result = result * 31 + (beskrivelse?.hashCode() ?: 0)
    result = result * 31 + (forfallsdato?.hashCode() ?: 0)
    result = result * 31 + (utbetalingsdato?.hashCode() ?: 0)
    result = result * 31 + (fom?.hashCode() ?: 0)
    result = result * 31 + (tom?.hashCode() ?: 0)
    result = result * 31 + (annenMottaker?.hashCode() ?: 0)
    result = result * 31 + (mottaker?.hashCode() ?: 0)
    result = result * 31 + (kontonummer?.hashCode() ?: 0)
    result = result * 31 + (utbetalingsmetode?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonUtbetaling(utbetalingsreferanse=$utbetalingsreferanse, saksreferanse=$saksreferanse, rammevedtaksreferanse=$rammevedtaksreferanse, status=$status, belop=$belop, beskrivelse=$beskrivelse, forfallsdato=$forfallsdato, utbetalingsdato=$utbetalingsdato, fom=$fom, tom=$tom, annenMottaker=$annenMottaker, mottaker=$mottaker, kontonummer=$kontonummer, utbetalingsmetode=$utbetalingsmetode, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    PLANLAGT_UTBETALING("PLANLAGT_UTBETALING"),
    UTBETALT("UTBETALT"),
    STOPPET("STOPPET"),
    ANNULLERT("ANNULLERT"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Status = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
