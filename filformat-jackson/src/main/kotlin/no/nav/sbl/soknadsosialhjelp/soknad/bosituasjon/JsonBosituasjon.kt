package no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon

import com.fasterxml.jackson.`annotation`.JsonAnyGetter
import com.fasterxml.jackson.`annotation`.JsonAnySetter
import com.fasterxml.jackson.`annotation`.JsonCreator
import com.fasterxml.jackson.`annotation`.JsonIgnore
import com.fasterxml.jackson.`annotation`.JsonInclude
import com.fasterxml.jackson.`annotation`.JsonProperty
import com.fasterxml.jackson.`annotation`.JsonPropertyOrder
import com.fasterxml.jackson.`annotation`.JsonValue
import java.io.Serializable
import kotlin.Any
import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * Bosituasjon
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "botype", "antallPersoner")
public open class JsonBosituasjon : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("botype")
  @set:JsonProperty("botype")
  public var botype: Botype? = null

  @get:JsonProperty("antallPersoner")
  @set:JsonProperty("antallPersoner")
  public var antallPersoner: Int? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonBosituasjon {
    this.kilde = kilde
    return this
  }

  public open fun withBotype(botype: Botype?): JsonBosituasjon {
    this.botype = botype
    return this
  }

  public open fun withAntallPersoner(antallPersoner: Int?): JsonBosituasjon {
    this.antallPersoner = antallPersoner
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonBosituasjon {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonBosituasjon) return false
    return kilde == other.kilde &&
        botype == other.botype &&
        antallPersoner == other.antallPersoner &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (botype?.hashCode() ?: 0)
    result = result * 31 + (antallPersoner?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonBosituasjon(kilde=$kilde, botype=$botype, antallPersoner=$antallPersoner, additionalProperties=$additionalProperties)"

  public enum class Botype(
    @JsonValue
    public val `value`: String,
  ) {
    EIER("eier"),
    LEIER("leier"),
    KOMMUNAL("kommunal"),
    INGEN("ingen"),
    INSTITUSJON("institusjon"),
    KRISESENTER("krisesenter"),
    FENGSEL("fengsel"),
    VENNER("venner"),
    FORELDRE("foreldre"),
    FAMILIE("familie"),
    ANNET("annet"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Botype = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
