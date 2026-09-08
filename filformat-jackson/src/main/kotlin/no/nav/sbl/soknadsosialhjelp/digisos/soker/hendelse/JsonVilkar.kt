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
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.List
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse

/**
 * Vilkar
 *
 * Vilkar
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("vilkarreferanse", "saksreferanse", "utbetalingsreferanse", "tittel", "beskrivelse", "status")
public open class JsonVilkar : JsonHendelse(), Serializable {
  @get:JsonProperty("vilkarreferanse")
  @set:JsonProperty("vilkarreferanse")
  @get:JsonPropertyDescription("referansen til vilkåret")
  public var vilkarreferanse: String? = null

  @get:JsonProperty("saksreferanse")
  @set:JsonProperty("saksreferanse")
  @get:JsonPropertyDescription("En referanse til saken vilkåret gjelder for. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.")
  public var saksreferanse: String? = null

  @get:JsonProperty("utbetalingsreferanse")
  @set:JsonProperty("utbetalingsreferanse")
  @get:JsonPropertyDescription("Hvilke utbetalinger vilkåret er knyttet til")
  public var utbetalingsreferanse: List<String>? = mutableListOf()

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("Hva vilkåret gjelder. Er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.")
  public var tittel: String? = null

  @get:JsonProperty("beskrivelse")
  @set:JsonProperty("beskrivelse")
  @get:JsonPropertyDescription("En eventuelt mer detaljert beskrivelse rundt hva vilkåret omhandler. Denne teksten kan også inneholde feks. periode og hvordan en søker kan oppfylle vilkåret.")
  public var beskrivelse: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Status som forteller om vilkåret er relevant eller ikke. \n* RELEVANT - benyttes for alle vilkår som er relevante for saken. \n* ANNULLERT - benyttes dersom et vilkår er feilregistrert, skal fjernes av tekniske årsaker eller er erstattet av nytt vedtak. Annullerte vilkår vil ikke vises, og benyttes i tilfeller der et vilkår skal slettes fra nav.no på grunn av en feilsituasjon. \n* OPPFYLT og IKKE_OPPFYLT  - er deprecated men fjernes ikke, for bakover-kompatibilitet. De blir tolket som RELEVANT.")
  public var status: Status? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withVilkarreferanse(vilkarreferanse: String?): JsonVilkar {
    this.vilkarreferanse = vilkarreferanse
    return this
  }

  public open fun withSaksreferanse(saksreferanse: String?): JsonVilkar {
    this.saksreferanse = saksreferanse
    return this
  }

  public open fun withUtbetalingsreferanse(utbetalingsreferanse: List<String>?): JsonVilkar {
    this.utbetalingsreferanse = utbetalingsreferanse
    return this
  }

  public open fun withTittel(tittel: String?): JsonVilkar {
    this.tittel = tittel
    return this
  }

  public open fun withBeskrivelse(beskrivelse: String?): JsonVilkar {
    this.beskrivelse = beskrivelse
    return this
  }

  public open fun withStatus(status: Status?): JsonVilkar {
    this.status = status
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonVilkar {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonVilkar {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonVilkar {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonVilkar) return false
    return vilkarreferanse == other.vilkarreferanse &&
        saksreferanse == other.saksreferanse &&
        utbetalingsreferanse == other.utbetalingsreferanse &&
        tittel == other.tittel &&
        beskrivelse == other.beskrivelse &&
        status == other.status &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (vilkarreferanse?.hashCode() ?: 0)
    result = result * 31 + (saksreferanse?.hashCode() ?: 0)
    result = result * 31 + (utbetalingsreferanse?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (beskrivelse?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonVilkar(vilkarreferanse=$vilkarreferanse, saksreferanse=$saksreferanse, utbetalingsreferanse=$utbetalingsreferanse, tittel=$tittel, beskrivelse=$beskrivelse, status=$status, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    RELEVANT("RELEVANT"),
    ANNULLERT("ANNULLERT"),
    OPPFYLT("OPPFYLT"),
    IKKE_OPPFYLT("IKKE_OPPFYLT"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Status = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
