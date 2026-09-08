package no.nav.sbl.soknadsosialhjelp.soknad.arbeid

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
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "arbeidsgivernavn", "fom", "tom", "stillingsprosent", "stillingstype", "overstyrtAvBruker")
public open class JsonArbeidsforhold : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("arbeidsgivernavn")
  @set:JsonProperty("arbeidsgivernavn")
  @get:JsonPropertyDescription("Navn på arbeidsgiver. Kan være navn på privatperson ved forenklet oppgjørsordning. Navnet kan være blankt.")
  public var arbeidsgivernavn: String? = null

  @get:JsonProperty("fom")
  @set:JsonProperty("fom")
  public var fom: String? = null

  @get:JsonProperty("tom")
  @set:JsonProperty("tom")
  public var tom: String? = null

  @get:JsonProperty("stillingsprosent")
  @set:JsonProperty("stillingsprosent")
  public var stillingsprosent: Int? = null

  @get:JsonProperty("stillingstype")
  @set:JsonProperty("stillingstype")
  @get:JsonPropertyDescription("Feltet er utdatert.")
  public var stillingstype: Stillingstype? = null

  @get:JsonProperty("overstyrtAvBruker")
  @set:JsonProperty("overstyrtAvBruker")
  @get:JsonPropertyDescription("Brukes når en søker overstyrer/endrer på et arbeidsforhold. Settes kun til \"true\" på arbeidsforhold med systemkilde. Anbefaler at man likevel viser dataene fra AA-registeret til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).")
  public var overstyrtAvBruker: Boolean? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonArbeidsforhold {
    this.kilde = kilde
    return this
  }

  public open fun withArbeidsgivernavn(arbeidsgivernavn: String?): JsonArbeidsforhold {
    this.arbeidsgivernavn = arbeidsgivernavn
    return this
  }

  public open fun withFom(fom: String?): JsonArbeidsforhold {
    this.fom = fom
    return this
  }

  public open fun withTom(tom: String?): JsonArbeidsforhold {
    this.tom = tom
    return this
  }

  public open fun withStillingsprosent(stillingsprosent: Int?): JsonArbeidsforhold {
    this.stillingsprosent = stillingsprosent
    return this
  }

  public open fun withStillingstype(stillingstype: Stillingstype?): JsonArbeidsforhold {
    this.stillingstype = stillingstype
    return this
  }

  public open fun withOverstyrtAvBruker(overstyrtAvBruker: Boolean?): JsonArbeidsforhold {
    this.overstyrtAvBruker = overstyrtAvBruker
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonArbeidsforhold {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonArbeidsforhold) return false
    return kilde == other.kilde &&
        arbeidsgivernavn == other.arbeidsgivernavn &&
        fom == other.fom &&
        tom == other.tom &&
        stillingsprosent == other.stillingsprosent &&
        stillingstype == other.stillingstype &&
        overstyrtAvBruker == other.overstyrtAvBruker &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (arbeidsgivernavn?.hashCode() ?: 0)
    result = result * 31 + (fom?.hashCode() ?: 0)
    result = result * 31 + (tom?.hashCode() ?: 0)
    result = result * 31 + (stillingsprosent?.hashCode() ?: 0)
    result = result * 31 + (stillingstype?.hashCode() ?: 0)
    result = result * 31 + (overstyrtAvBruker?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonArbeidsforhold(kilde=$kilde, arbeidsgivernavn=$arbeidsgivernavn, fom=$fom, tom=$tom, stillingsprosent=$stillingsprosent, stillingstype=$stillingstype, overstyrtAvBruker=$overstyrtAvBruker, additionalProperties=$additionalProperties)"

  public enum class Stillingstype(
    @JsonValue
    public val `value`: String,
  ) {
    VARIABEL("variabel"),
    FAST("fast"),
    FAST_OG_VARIABEL("fastOgVariabel"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Stillingstype = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
