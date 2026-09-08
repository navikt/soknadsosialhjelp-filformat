package no.nav.sbl.soknadsosialhjelp.soknad

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

/**
 * Inneholder informasjon om status for henting av opplysninger fra andre tjenester.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("utbetalingerFraNavFeilet", "inntektFraSkatteetatenFeilet", "stotteFraHusbankenFeilet")
public open class JsonDriftsinformasjon : Serializable {
  @get:JsonProperty("utbetalingerFraNavFeilet")
  @set:JsonProperty("utbetalingerFraNavFeilet")
  @get:JsonPropertyDescription("Hvis utbetalinger fra NAV ikke kunne hentes er denne true.")
  public var utbetalingerFraNavFeilet: Boolean? = null

  @get:JsonProperty("inntektFraSkatteetatenFeilet")
  @set:JsonProperty("inntektFraSkatteetatenFeilet")
  @get:JsonPropertyDescription("Hvis skattbar inntekt hos skatteetaten ikke kunne hentes er denne true.")
  public var inntektFraSkatteetatenFeilet: Boolean? = null

  @get:JsonProperty("stotteFraHusbankenFeilet")
  @set:JsonProperty("stotteFraHusbankenFeilet")
  @get:JsonPropertyDescription("Hvis økonomisk støtte fra Husbanken ikke kunne hentes er denne true.")
  public var stotteFraHusbankenFeilet: Boolean? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withUtbetalingerFraNavFeilet(utbetalingerFraNavFeilet: Boolean?): JsonDriftsinformasjon {
    this.utbetalingerFraNavFeilet = utbetalingerFraNavFeilet
    return this
  }

  public open fun withInntektFraSkatteetatenFeilet(inntektFraSkatteetatenFeilet: Boolean?): JsonDriftsinformasjon {
    this.inntektFraSkatteetatenFeilet = inntektFraSkatteetatenFeilet
    return this
  }

  public open fun withStotteFraHusbankenFeilet(stotteFraHusbankenFeilet: Boolean?): JsonDriftsinformasjon {
    this.stotteFraHusbankenFeilet = stotteFraHusbankenFeilet
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonDriftsinformasjon {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonDriftsinformasjon) return false
    return utbetalingerFraNavFeilet == other.utbetalingerFraNavFeilet &&
        inntektFraSkatteetatenFeilet == other.inntektFraSkatteetatenFeilet &&
        stotteFraHusbankenFeilet == other.stotteFraHusbankenFeilet &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (utbetalingerFraNavFeilet?.hashCode() ?: 0)
    result = result * 31 + (inntektFraSkatteetatenFeilet?.hashCode() ?: 0)
    result = result * 31 + (stotteFraHusbankenFeilet?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonDriftsinformasjon(utbetalingerFraNavFeilet=$utbetalingerFraNavFeilet, inntektFraSkatteetatenFeilet=$inntektFraSkatteetatenFeilet, stotteFraHusbankenFeilet=$stotteFraHusbankenFeilet, additionalProperties=$additionalProperties)"
}
