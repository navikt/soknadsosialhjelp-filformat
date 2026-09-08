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
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("type", "belop", "satsType", "satsAntall", "satsBelop")
public open class JsonOkonomiOpplysningUtbetalingKomponent : Serializable {
  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Beskrivelse av hva slags type delutbetaling det er. Eksempler: \"Arbeidstaker\", \"Grunnpensjon\" og \"Tilleggspensjon\".")
  public var type: String? = null

  @get:JsonProperty("belop")
  @set:JsonProperty("belop")
  @get:JsonPropertyDescription("Beløp for delutbetalingen. Resultat av satsType, satsAntall og satsBelop")
  public var belop: Double? = null

  @get:JsonProperty("satsType")
  @set:JsonProperty("satsType")
  @get:JsonPropertyDescription("Beskrivelse av hva slags type sats det er. Eksempel: \"Dag\" og \"Prosent\".")
  public var satsType: String? = null

  @get:JsonProperty("satsAntall")
  @set:JsonProperty("satsAntall")
  @get:JsonPropertyDescription("Antall enheter av satstypen for delutbetalingen.")
  public var satsAntall: Double? = null

  @get:JsonProperty("satsBelop")
  @set:JsonProperty("satsBelop")
  @get:JsonPropertyDescription("Satsbeløpet for delutbetalingen")
  public var satsBelop: Double? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withType(type: String?): JsonOkonomiOpplysningUtbetalingKomponent {
    this.type = type
    return this
  }

  public open fun withBelop(belop: Double?): JsonOkonomiOpplysningUtbetalingKomponent {
    this.belop = belop
    return this
  }

  public open fun withSatsType(satsType: String?): JsonOkonomiOpplysningUtbetalingKomponent {
    this.satsType = satsType
    return this
  }

  public open fun withSatsAntall(satsAntall: Double?): JsonOkonomiOpplysningUtbetalingKomponent {
    this.satsAntall = satsAntall
    return this
  }

  public open fun withSatsBelop(satsBelop: Double?): JsonOkonomiOpplysningUtbetalingKomponent {
    this.satsBelop = satsBelop
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonOkonomiOpplysningUtbetalingKomponent {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonOkonomiOpplysningUtbetalingKomponent) return false
    return type == other.type &&
        belop == other.belop &&
        satsType == other.satsType &&
        satsAntall == other.satsAntall &&
        satsBelop == other.satsBelop &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (belop?.hashCode() ?: 0)
    result = result * 31 + (satsType?.hashCode() ?: 0)
    result = result * 31 + (satsAntall?.hashCode() ?: 0)
    result = result * 31 + (satsBelop?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonOkonomiOpplysningUtbetalingKomponent(type=$type, belop=$belop, satsType=$satsType, satsAntall=$satsAntall, satsBelop=$satsBelop, additionalProperties=$additionalProperties)"
}
