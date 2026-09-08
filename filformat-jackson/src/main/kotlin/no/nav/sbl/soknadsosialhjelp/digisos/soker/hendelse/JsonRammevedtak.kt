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
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.LinkedHashMap
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse

/**
 * Rammevedtak
 *
 * Rammevedtak
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("rammevedtaksreferanse", "saksreferanse", "beskrivelse", "belop", "fom", "tom")
public open class JsonRammevedtak : JsonHendelse(), Serializable {
  @get:JsonProperty("rammevedtaksreferanse")
  @set:JsonProperty("rammevedtaksreferanse")
  @get:JsonPropertyDescription("Unik referanse per rammevedtak.")
  public var rammevedtaksreferanse: String? = null

  @get:JsonProperty("saksreferanse")
  @set:JsonProperty("saksreferanse")
  @get:JsonPropertyDescription("Referanse rammevedtaket skal tilknyttes til")
  public var saksreferanse: String? = null

  @get:JsonProperty("beskrivelse")
  @set:JsonProperty("beskrivelse")
  @get:JsonPropertyDescription("Hva er rammevedtaket for, eks strøm, legeregning")
  public var beskrivelse: String? = null

  @get:JsonProperty("belop")
  @set:JsonProperty("belop")
  @get:JsonPropertyDescription("Hvor mye, i kr")
  public var belop: Double? = null

  @get:JsonProperty("fom")
  @set:JsonProperty("fom")
  @get:JsonPropertyDescription("Utbetalingsperiode (Fra)")
  public var fom: String? = null

  @get:JsonProperty("tom")
  @set:JsonProperty("tom")
  @get:JsonPropertyDescription("Utbetalingsperiode (Til)")
  public var tom: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withRammevedtaksreferanse(rammevedtaksreferanse: String?): JsonRammevedtak {
    this.rammevedtaksreferanse = rammevedtaksreferanse
    return this
  }

  public open fun withSaksreferanse(saksreferanse: String?): JsonRammevedtak {
    this.saksreferanse = saksreferanse
    return this
  }

  public open fun withBeskrivelse(beskrivelse: String?): JsonRammevedtak {
    this.beskrivelse = beskrivelse
    return this
  }

  public open fun withBelop(belop: Double?): JsonRammevedtak {
    this.belop = belop
    return this
  }

  public open fun withFom(fom: String?): JsonRammevedtak {
    this.fom = fom
    return this
  }

  public open fun withTom(tom: String?): JsonRammevedtak {
    this.tom = tom
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonRammevedtak {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonRammevedtak {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonRammevedtak {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonRammevedtak) return false
    return rammevedtaksreferanse == other.rammevedtaksreferanse &&
        saksreferanse == other.saksreferanse &&
        beskrivelse == other.beskrivelse &&
        belop == other.belop &&
        fom == other.fom &&
        tom == other.tom &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (rammevedtaksreferanse?.hashCode() ?: 0)
    result = result * 31 + (saksreferanse?.hashCode() ?: 0)
    result = result * 31 + (beskrivelse?.hashCode() ?: 0)
    result = result * 31 + (belop?.hashCode() ?: 0)
    result = result * 31 + (fom?.hashCode() ?: 0)
    result = result * 31 + (tom?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonRammevedtak(rammevedtaksreferanse=$rammevedtaksreferanse, saksreferanse=$saksreferanse, beskrivelse=$beskrivelse, belop=$belop, fom=$fom, tom=$tom, additionalProperties=$additionalProperties, super=${super.toString()})"
}
