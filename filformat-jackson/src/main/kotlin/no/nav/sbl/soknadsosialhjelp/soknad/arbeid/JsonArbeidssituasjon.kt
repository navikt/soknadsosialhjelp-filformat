package no.nav.sbl.soknadsosialhjelp.soknad.arbeid

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKildeBruker

/**
 * En oppsummering av arbeidssituasjonen.
 *
 * Hvis situasjon mangler vil "forhold" være satt (og mottsatt).
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "erIJobb", "jobbGrad")
public open class JsonArbeidssituasjon : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKildeBruker? = null

  @get:JsonProperty("erIJobb")
  @set:JsonProperty("erIJobb")
  @get:JsonPropertyDescription("Hvis \"erIJobb\" mangler betyr dette at bruker ikke har besvart spørsmålet i skjemaet.")
  public var erIJobb: Boolean? = null

  @get:JsonProperty("jobbGrad")
  @set:JsonProperty("jobbGrad")
  @get:JsonPropertyDescription("Hvis \"jobbGrad\" mangler betyr dette at bruker ikke har besvart spørsmålet i skjemaet.")
  public var jobbGrad: JobbGrad? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKildeBruker?): JsonArbeidssituasjon {
    this.kilde = kilde
    return this
  }

  public open fun withErIJobb(erIJobb: Boolean?): JsonArbeidssituasjon {
    this.erIJobb = erIJobb
    return this
  }

  public open fun withJobbGrad(jobbGrad: JobbGrad?): JsonArbeidssituasjon {
    this.jobbGrad = jobbGrad
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonArbeidssituasjon {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonArbeidssituasjon) return false
    return kilde == other.kilde &&
        erIJobb == other.erIJobb &&
        jobbGrad == other.jobbGrad &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (erIJobb?.hashCode() ?: 0)
    result = result * 31 + (jobbGrad?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonArbeidssituasjon(kilde=$kilde, erIJobb=$erIJobb, jobbGrad=$jobbGrad, additionalProperties=$additionalProperties)"

  public enum class JobbGrad(
    @JsonValue
    public val `value`: String,
  ) {
    HELTID("heltid"),
    DELTID("deltid"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): JobbGrad = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
