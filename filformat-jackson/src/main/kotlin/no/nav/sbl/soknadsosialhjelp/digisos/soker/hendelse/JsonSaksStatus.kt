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
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse

/**
 * En sak, i tilknytning til søknaden, har blitt opprettet og har status
 *
 * Status på sak som vil resultere i et vedtak.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("referanse", "tittel", "status")
public open class JsonSaksStatus : JsonHendelse(), Serializable {
  @get:JsonProperty("referanse")
  @set:JsonProperty("referanse")
  @get:JsonPropertyDescription("En referanse slik at et vedtak kan tilknyttes på et senere tidspunkt")
  public var referanse: String? = null

  @get:JsonProperty("tittel")
  @set:JsonProperty("tittel")
  @get:JsonPropertyDescription("Tittel på saken, hva saken gjelder")
  public var tittel: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Det kan legges til nye statustyper i enumen, men ingen gamle kan fjernes fra valideringsskjemaet (grunnet bakoverkompatibilitet). Saker uten innsyn blir behandlet, men søkeren får ikke innsyn i saken (feks. når saken ikke gjelder økonomisk sosialhjelp), Ved feilregistrering vil ikke saken vises.")
  public var status: Status? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withReferanse(referanse: String?): JsonSaksStatus {
    this.referanse = referanse
    return this
  }

  public open fun withTittel(tittel: String?): JsonSaksStatus {
    this.tittel = tittel
    return this
  }

  public open fun withStatus(status: Status?): JsonSaksStatus {
    this.status = status
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonSaksStatus {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonSaksStatus {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonSaksStatus {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSaksStatus) return false
    return referanse == other.referanse &&
        tittel == other.tittel &&
        status == other.status &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (referanse?.hashCode() ?: 0)
    result = result * 31 + (tittel?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonSaksStatus(referanse=$referanse, tittel=$tittel, status=$status, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    UNDER_BEHANDLING("UNDER_BEHANDLING"),
    IKKE_INNSYN("IKKE_INNSYN"),
    BEHANDLES_IKKE("BEHANDLES_IKKE"),
    FEILREGISTRERT("FEILREGISTRERT"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Status = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
