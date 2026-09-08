package no.nav.sbl.soknadsosialhjelp.soknad

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
import no.nav.sbl.soknadsosialhjelp.soknad.arbeid.JsonArbeid
import no.nav.sbl.soknadsosialhjelp.soknad.begrunnelse.JsonBegrunnelse
import no.nav.sbl.soknadsosialhjelp.soknad.bosituasjon.JsonBosituasjon
import no.nav.sbl.soknadsosialhjelp.soknad.familie.JsonFamilie
import no.nav.sbl.soknadsosialhjelp.soknad.okonomi.JsonOkonomi
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonalia
import no.nav.sbl.soknadsosialhjelp.soknad.situasjonendring.JsonSituasjonendring
import no.nav.sbl.soknadsosialhjelp.soknad.utdanning.JsonUtdanning

/**
 * Inneholder søknadsdataene uten meta- og kompatibilitetsdata.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("soknadstype", "personalia", "arbeid", "utdanning", "familie", "begrunnelse", "situasjonendring", "bosituasjon", "okonomi")
public open class JsonData : Serializable {
  @get:JsonProperty("soknadstype")
  @set:JsonProperty("soknadstype")
  @get:JsonPropertyDescription("Angir hvilken type søknad det er. PT er det to typer: \"kort\" og \"standard\".")
  public var soknadstype: Soknadstype? = null

  @get:JsonProperty("personalia")
  @set:JsonProperty("personalia")
  @get:JsonPropertyDescription("Personalia")
  public var personalia: JsonPersonalia? = null

  @get:JsonProperty("arbeid")
  @set:JsonProperty("arbeid")
  @get:JsonPropertyDescription("Arbeid")
  public var arbeid: JsonArbeid? = null

  @get:JsonProperty("utdanning")
  @set:JsonProperty("utdanning")
  @get:JsonPropertyDescription("Utdanning")
  public var utdanning: JsonUtdanning? = null

  @get:JsonProperty("familie")
  @set:JsonProperty("familie")
  @get:JsonPropertyDescription("Familie")
  public var familie: JsonFamilie? = null

  @get:JsonProperty("begrunnelse")
  @set:JsonProperty("begrunnelse")
  @get:JsonPropertyDescription("Begrunnelse")
  public var begrunnelse: JsonBegrunnelse? = null

  @get:JsonProperty("situasjonendring")
  @set:JsonProperty("situasjonendring")
  @get:JsonPropertyDescription("Endring i situasjon")
  public var situasjonendring: JsonSituasjonendring? = null

  @get:JsonProperty("bosituasjon")
  @set:JsonProperty("bosituasjon")
  @get:JsonPropertyDescription("Bosituasjon")
  public var bosituasjon: JsonBosituasjon? = null

  @get:JsonProperty("okonomi")
  @set:JsonProperty("okonomi")
  @get:JsonPropertyDescription("Økonomiske data.")
  public var okonomi: JsonOkonomi? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSoknadstype(soknadstype: Soknadstype?): JsonData {
    this.soknadstype = soknadstype
    return this
  }

  public open fun withPersonalia(personalia: JsonPersonalia?): JsonData {
    this.personalia = personalia
    return this
  }

  public open fun withArbeid(arbeid: JsonArbeid?): JsonData {
    this.arbeid = arbeid
    return this
  }

  public open fun withUtdanning(utdanning: JsonUtdanning?): JsonData {
    this.utdanning = utdanning
    return this
  }

  public open fun withFamilie(familie: JsonFamilie?): JsonData {
    this.familie = familie
    return this
  }

  public open fun withBegrunnelse(begrunnelse: JsonBegrunnelse?): JsonData {
    this.begrunnelse = begrunnelse
    return this
  }

  public open fun withSituasjonendring(situasjonendring: JsonSituasjonendring?): JsonData {
    this.situasjonendring = situasjonendring
    return this
  }

  public open fun withBosituasjon(bosituasjon: JsonBosituasjon?): JsonData {
    this.bosituasjon = bosituasjon
    return this
  }

  public open fun withOkonomi(okonomi: JsonOkonomi?): JsonData {
    this.okonomi = okonomi
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonData {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonData) return false
    return soknadstype == other.soknadstype &&
        personalia == other.personalia &&
        arbeid == other.arbeid &&
        utdanning == other.utdanning &&
        familie == other.familie &&
        begrunnelse == other.begrunnelse &&
        situasjonendring == other.situasjonendring &&
        bosituasjon == other.bosituasjon &&
        okonomi == other.okonomi &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (soknadstype?.hashCode() ?: 0)
    result = result * 31 + (personalia?.hashCode() ?: 0)
    result = result * 31 + (arbeid?.hashCode() ?: 0)
    result = result * 31 + (utdanning?.hashCode() ?: 0)
    result = result * 31 + (familie?.hashCode() ?: 0)
    result = result * 31 + (begrunnelse?.hashCode() ?: 0)
    result = result * 31 + (situasjonendring?.hashCode() ?: 0)
    result = result * 31 + (bosituasjon?.hashCode() ?: 0)
    result = result * 31 + (okonomi?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonData(soknadstype=$soknadstype, personalia=$personalia, arbeid=$arbeid, utdanning=$utdanning, familie=$familie, begrunnelse=$begrunnelse, situasjonendring=$situasjonendring, bosituasjon=$bosituasjon, okonomi=$okonomi, additionalProperties=$additionalProperties)"

  public enum class Soknadstype(
    @JsonValue
    public val `value`: String,
  ) {
    KORT("kort"),
    STANDARD("standard"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Soknadstype = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
