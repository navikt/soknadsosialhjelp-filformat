package no.nav.sbl.soknadsosialhjelp.digisos.soker.filreferanse

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
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonFilreferanse

/**
 * Referanse til en fil som har blitt lagret gjennom SvarUt.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "nr")
public open class JsonSvarUtFilreferanse : JsonFilreferanse(), Serializable {
  @get:JsonProperty("id")
  @set:JsonProperty("id")
  @get:JsonPropertyDescription("En UUID som identifiserer SvarUt-forsendelsen.")
  public var id: String? = null

  @get:JsonProperty("nr")
  @set:JsonProperty("nr")
  @get:JsonPropertyDescription("Filnummer i SvarUt-forsendelsen.")
  public var nr: Int? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withId(id: String?): JsonSvarUtFilreferanse {
    this.id = id
    return this
  }

  public open fun withNr(nr: Int?): JsonSvarUtFilreferanse {
    this.nr = nr
    return this
  }

  override fun withType(type: JsonFilreferanse.Type?): JsonSvarUtFilreferanse {
    this.type = type
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonSvarUtFilreferanse {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSvarUtFilreferanse) return false
    return id == other.id &&
        nr == other.nr &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (id?.hashCode() ?: 0)
    result = result * 31 + (nr?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonSvarUtFilreferanse(id=$id, nr=$nr, additionalProperties=$additionalProperties, super=${super.toString()})"
}
