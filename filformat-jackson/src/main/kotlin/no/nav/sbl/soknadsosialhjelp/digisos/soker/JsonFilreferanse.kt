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
import no.nav.sbl.soknadsosialhjelp.digisos.soker.filreferanse.JsonDokumentlagerFilreferanse
import no.nav.sbl.soknadsosialhjelp.digisos.soker.filreferanse.JsonSvarUtFilreferanse

/**
 * Filreferanse til FIKS
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("type")
@JsonTypeInfo(
  use = JsonTypeInfo.Id.NAME,
  include = JsonTypeInfo.As.EXISTING_PROPERTY,
  property = "type",
  visible = true,
)
@JsonSubTypes(value = [JsonSubTypes.Type(value = JsonSvarUtFilreferanse::class, name = "svarut"), JsonSubTypes.Type(value = JsonDokumentlagerFilreferanse::class, name = "dokumentlager")])
public open class JsonFilreferanse : Serializable {
  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Typen filreferanse.")
  public var type: Type? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withType(type: Type?): JsonFilreferanse {
    this.type = type
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonFilreferanse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonFilreferanse) return false
    return type == other.type &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonFilreferanse(type=$type, additionalProperties=$additionalProperties)"

  public enum class Type(
    @JsonValue
    public val `value`: String,
  ) {
    SVARUT("svarut"),
    DOKUMENTLAGER("dokumentlager"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Type = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
