package no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse

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
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse

/**
 * Nytt NAV-kontor behandler søknaden.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("navKontor")
public open class JsonTildeltNavKontor : JsonHendelse(), Serializable {
  @get:JsonProperty("navKontor")
  @set:JsonProperty("navKontor")
  @get:JsonPropertyDescription("En identifikator for NAV-kontor. Vi benytter NORG som har oversikt over alle NAV-kontor i Norge, for hver kommune og bydel.")
  public var navKontor: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withNavKontor(navKontor: String?): JsonTildeltNavKontor {
    this.navKontor = navKontor
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonTildeltNavKontor {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonTildeltNavKontor {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonTildeltNavKontor {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonTildeltNavKontor) return false
    return navKontor == other.navKontor &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (navKontor?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonTildeltNavKontor(navKontor=$navKontor, additionalProperties=$additionalProperties, super=${super.toString()})"
}
