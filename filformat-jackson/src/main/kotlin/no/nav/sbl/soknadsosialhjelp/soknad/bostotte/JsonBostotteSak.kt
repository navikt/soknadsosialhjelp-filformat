package no.nav.sbl.soknadsosialhjelp.soknad.bostotte

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeSystem

/**
 * Saker som bruker har
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "type", "dato", "status", "beskrivelse", "vedtaksstatus")
public open class JsonBostotteSak : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeSystem? = null

  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Kodeverdi som angir hva slags type sak det er. Dette feltet kan for eksempel brukes til å filtrere bort saker man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: \"husbanken\" og \"annet\".")
  public var type: String? = null

  @get:JsonProperty("dato")
  @set:JsonProperty("dato")
  @get:JsonPropertyDescription("Dato som denne saken er registrert på.")
  public var dato: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Saksstatus. Eksempler: \"UNDER_BEHANDLING\", \"VEDTATT\"")
  public var status: String? = null

  @get:JsonProperty("beskrivelse")
  @set:JsonProperty("beskrivelse")
  @get:JsonPropertyDescription("En tilleggsbeskrivelse til status. Kan være tom.")
  public var beskrivelse: String? = null

  @get:JsonProperty("vedtaksstatus")
  @set:JsonProperty("vedtaksstatus")
  @get:JsonPropertyDescription("Vedtaksstatus. Eksempler: \"INNVILGET\", \"AVSLAG\", \"AVVIST\".")
  public var vedtaksstatus: Vedtaksstatus? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeSystem?): JsonBostotteSak {
    this.kilde = kilde
    return this
  }

  public open fun withType(type: String?): JsonBostotteSak {
    this.type = type
    return this
  }

  public open fun withDato(dato: String?): JsonBostotteSak {
    this.dato = dato
    return this
  }

  public open fun withStatus(status: String?): JsonBostotteSak {
    this.status = status
    return this
  }

  public open fun withBeskrivelse(beskrivelse: String?): JsonBostotteSak {
    this.beskrivelse = beskrivelse
    return this
  }

  public open fun withVedtaksstatus(vedtaksstatus: Vedtaksstatus?): JsonBostotteSak {
    this.vedtaksstatus = vedtaksstatus
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonBostotteSak {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonBostotteSak) return false
    return kilde == other.kilde &&
        type == other.type &&
        dato == other.dato &&
        status == other.status &&
        beskrivelse == other.beskrivelse &&
        vedtaksstatus == other.vedtaksstatus &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (dato?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + (beskrivelse?.hashCode() ?: 0)
    result = result * 31 + (vedtaksstatus?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonBostotteSak(kilde=$kilde, type=$type, dato=$dato, status=$status, beskrivelse=$beskrivelse, vedtaksstatus=$vedtaksstatus, additionalProperties=$additionalProperties)"

  public enum class Vedtaksstatus(
    @JsonValue
    public val `value`: String,
  ) {
    INNVILGET("INNVILGET"),
    AVSLAG("AVSLAG"),
    AVVIST("AVVIST"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Vedtaksstatus = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
