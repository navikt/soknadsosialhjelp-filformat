package no.nav.sbl.soknadsosialhjelp.digisos.soker

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyDescription
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonSubTypes
import com.fasterxml.jackson.`annotation`.JsonTypeInfo
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonDokumentasjonEtterspurt
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonDokumentasjonkrav
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonForelopigSvar
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonRammevedtak
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSaksStatus
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonTildeltNavKontor
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonUtbetaling
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVedtakFattet
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVilkar

/**
 * Hendelse
 *
 * Feltet "type" angir hvilken type hendelse det er. Se egen definisjon per hendelse. Det som er dokumentert direkte under er kun det som er felles for alle hendelser.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("type", "hendelsestidspunkt")
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true,
)
@JsonSubTypes(value = [JsonSubTypes.Type(value = JsonTildeltNavKontor::class, name = "tildeltNavKontor"), JsonSubTypes.Type(value = JsonSoknadsStatus::class, name = "soknadsStatus"), JsonSubTypes.Type(value = JsonVedtakFattet::class, name = "vedtakFattet"), JsonSubTypes.Type(value = JsonDokumentasjonEtterspurt::class, name = "dokumentasjonEtterspurt"), JsonSubTypes.Type(value = JsonForelopigSvar::class, name = "forelopigSvar"), JsonSubTypes.Type(value = JsonSaksStatus::class, name = "saksStatus"), JsonSubTypes.Type(value = JsonUtbetaling::class, name = "utbetaling"), JsonSubTypes.Type(value = JsonVilkar::class, name = "vilkar"), JsonSubTypes.Type(value = JsonDokumentasjonkrav::class, name = "dokumentasjonkrav"), JsonSubTypes.Type(value = JsonRammevedtak::class, name = "rammevedtak")])
public open class JsonHendelse : Serializable {
  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Angir hendelsestypen og dermed også hvilke felter som må settes.. Det kan legges til nye hendelsestyper i enumen, men ingen gamle kan fjernes fra valideringsskjemaet (grunnet kompatibilitet).")
  public var type: Type? = null

  @get:JsonProperty("hendelsestidspunkt")
  @set:JsonProperty("hendelsestidspunkt")
  public var hendelsestidspunkt: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withType(type: Type?): JsonHendelse {
    this.type = type
    return this
  }

  public open fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonHendelse {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonHendelse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonHendelse) return false
    return type == other.type &&
        hendelsestidspunkt == other.hendelsestidspunkt &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (hendelsestidspunkt?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonHendelse(type=$type, hendelsestidspunkt=$hendelsestidspunkt, additionalProperties=$additionalProperties)"

  public enum class Type(
    @JsonValue
    public val `value`: String,
  ) {
    TILDELT_NAV_KONTOR("tildeltNavKontor"),
    SOKNADS_STATUS("soknadsStatus"),
    VEDTAK_FATTET("vedtakFattet"),
    DOKUMENTASJON_ETTERSPURT("dokumentasjonEtterspurt"),
    FORELOPIG_SVAR("forelopigSvar"),
    SAKS_STATUS("saksStatus"),
    UTBETALING("utbetaling"),
    VILKAR("vilkar"),
    DOKUMENTASJONKRAV("dokumentasjonkrav"),
    RAMMEVEDTAK("rammevedtak"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Type = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
