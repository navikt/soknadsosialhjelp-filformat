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
 * Ny status på søknaden.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("status")
public open class JsonSoknadsStatus : JsonHendelse(), Serializable {
  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Det kan legges til nye statustyper i enumen, men ingen gamle kan fjernes fra valideringsskjemaet (grunnet kompatibilitet).")
  public var status: Status? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withStatus(status: Status?): JsonSoknadsStatus {
    this.status = status
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonSoknadsStatus {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonSoknadsStatus {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonSoknadsStatus {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSoknadsStatus) return false
    return status == other.status &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonSoknadsStatus(status=$status, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    MOTTATT("MOTTATT"),
    UNDER_BEHANDLING("UNDER_BEHANDLING"),
    FERDIGBEHANDLET("FERDIGBEHANDLET"),
    BEHANDLES_IKKE("BEHANDLES_IKKE"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Status = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
