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
 * Dokumentasjonkrav
 *
 * Dokumentasjonkrav
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("dokumentasjonkravreferanse", "saksreferanse", "utbetalingsreferanse", "tittel", "beskrivelse", "frist", "status")
public open class JsonDokumentasjonkrav : JsonHendelse(), Serializable {
  @get:JsonProperty("dokumentasjonkravreferanse")
  @set:JsonProperty("dokumentasjonkravreferanse")
  @get:JsonPropertyDescription("referansen til dokumentasjonkravet")
  public var dokumentasjonkravreferanse: String? = null

  @get:JsonProperty("saksreferanse")
  @set:JsonProperty("saksreferanse")
  @get:JsonPropertyDescription("En referanse til saken dokumentasjonkravet gjelder for. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.")
  public var saksreferanse: String? = null

  @get:JsonProperty("utbetalingsreferanse")
  @set:JsonProperty("utbetalingsreferanse")
  @get:JsonPropertyDescription("Hvilke utbetalinger dokumentasjonkravet er knyttet til")
  public var utbetalingsreferanse: List<String>? = mutableListOf()

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("Hva dokumentasjonkravet gjelder. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.")
  public var tittel: String? = null

  @get:JsonProperty("beskrivelse")
  @set:JsonProperty("beskrivelse")
  @get:JsonPropertyDescription("En eventuelt mer detaljert beskrivelse rundt hva dokumentasjonkravet omhandler. Denne teksten kan også inneholde feks. periode og hvordan en søker kan oppfylle kravet.")
  public var beskrivelse: String? = null

  @get:JsonProperty("frist")
  @set:JsonProperty("frist")
  public var frist: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Status som forteller om dokumentasjonkravet er relevant eller ikke. \n* RELEVANT - benyttes for alle dokumentasjonkrav som er relevante for saken. \n* LEVERT_TIDLIGERE - benyttes dersom dokumentasjonkravet er levert via andre kanaler, eller er oppfylt av et annet dokument søker har sendt inn. \n* ANNULLERT - benyttes dersom et dokumentasjonkrav er feilregistrert, skal fjernes av tekniske årsaker eller er erstattet av nytt vedtak. Annullerte dokumentasjonkrav vil ikke vises, og benyttes i tilfeller der et dokumentasjonkrav skal slettes fra nav.no på grunn av en feilsituasjon. \n* OPPFYLT og IKKE_OPPFYLT - er deprecated men fjernes ikke, for bakover-kompatibilitet. De blir tolket som RELEVANT.")
  public var status: Status? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withDokumentasjonkravreferanse(dokumentasjonkravreferanse: String?): JsonDokumentasjonkrav {
    this.dokumentasjonkravreferanse = dokumentasjonkravreferanse
    return this
  }

  public open fun withSaksreferanse(saksreferanse: String?): JsonDokumentasjonkrav {
    this.saksreferanse = saksreferanse
    return this
  }

  public open fun withUtbetalingsreferanse(utbetalingsreferanse: List<String>?): JsonDokumentasjonkrav {
    this.utbetalingsreferanse = utbetalingsreferanse
    return this
  }

  public open fun withTittel(tittel: String?): JsonDokumentasjonkrav {
    this.tittel = tittel
    return this
  }

  public open fun withBeskrivelse(beskrivelse: String?): JsonDokumentasjonkrav {
    this.beskrivelse = beskrivelse
    return this
  }

  public open fun withFrist(frist: String?): JsonDokumentasjonkrav {
    this.frist = frist
    return this
  }

  public open fun withStatus(status: Status?): JsonDokumentasjonkrav {
    this.status = status
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonDokumentasjonkrav {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonDokumentasjonkrav {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonDokumentasjonkrav {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonDokumentasjonkrav) return false
    return dokumentasjonkravreferanse == other.dokumentasjonkravreferanse &&
        saksreferanse == other.saksreferanse &&
        utbetalingsreferanse == other.utbetalingsreferanse &&
        tittel == other.tittel &&
        beskrivelse == other.beskrivelse &&
        frist == other.frist &&
        status == other.status &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (dokumentasjonkravreferanse?.hashCode() ?: 0)
    result = result * 31 + (saksreferanse?.hashCode() ?: 0)
    result = result * 31 + (utbetalingsreferanse?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (beskrivelse?.hashCode() ?: 0)
    result = result * 31 + (frist?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonDokumentasjonkrav(dokumentasjonkravreferanse=$dokumentasjonkravreferanse, saksreferanse=$saksreferanse, utbetalingsreferanse=$utbetalingsreferanse, tittel=$tittel, beskrivelse=$beskrivelse, frist=$frist, status=$status, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    RELEVANT("RELEVANT"),
    LEVERT_TIDLIGERE("LEVERT_TIDLIGERE"),
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
