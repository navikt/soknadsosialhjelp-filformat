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
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * JSON-formatert søknad om sosialhjelp.
 *
 * Encoding er UTF-8.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("version", "data", "innsendingstidspunkt", "mottaker", "driftsinformasjon", "kompatibilitet")
public open class JsonSoknad : Serializable {
  @get:JsonProperty("version")
  @set:JsonProperty("version")
  public var version: String? = null

  @get:JsonProperty("data")
  @set:JsonProperty("data")
  @get:JsonPropertyDescription("Inneholder søknadsdataene uten meta- og kompatibilitetsdata.")
  public var `data`: JsonData? = null

  @get:JsonProperty("innsendingstidspunkt")
  @set:JsonProperty("innsendingstidspunkt")
  public var innsendingstidspunkt: String? = null

  @get:JsonProperty("mottaker")
  @set:JsonProperty("mottaker")
  public var mottaker: JsonSoknadsmottaker? = null

  @get:JsonProperty("driftsinformasjon")
  @set:JsonProperty("driftsinformasjon")
  @get:JsonPropertyDescription("Inneholder informasjon om status for henting av opplysninger fra andre tjenester.")
  public var driftsinformasjon: JsonDriftsinformasjon? = null

  @get:JsonProperty("kompatibilitet")
  @set:JsonProperty("kompatibilitet")
  @get:JsonPropertyDescription("Liste med kompatibilitetstekster Det er et MÅ-krav å vise saksbehandler alle kompatibilitetstekster for versjonen man parser soknads-JSON-en med. Det anbefales å ha et avsnitt (eller tilsvarende) mellom hver enkelt kompatibilitetstekst.")
  public var kompatibilitet: List<JsonKompatibilitet>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withVersion(version: String?): JsonSoknad {
    this.version = version
    return this
  }

  public open fun withData(`data`: JsonData?): JsonSoknad {
    this.`data` = `data`
    return this
  }

  public open fun withInnsendingstidspunkt(innsendingstidspunkt: String?): JsonSoknad {
    this.innsendingstidspunkt = innsendingstidspunkt
    return this
  }

  public open fun withMottaker(mottaker: JsonSoknadsmottaker?): JsonSoknad {
    this.mottaker = mottaker
    return this
  }

  public open fun withDriftsinformasjon(driftsinformasjon: JsonDriftsinformasjon?): JsonSoknad {
    this.driftsinformasjon = driftsinformasjon
    return this
  }

  public open fun withKompatibilitet(kompatibilitet: List<JsonKompatibilitet>?): JsonSoknad {
    this.kompatibilitet = kompatibilitet
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonSoknad {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSoknad) return false
    return version == other.version &&
        data == other.data &&
        innsendingstidspunkt == other.innsendingstidspunkt &&
        mottaker == other.mottaker &&
        driftsinformasjon == other.driftsinformasjon &&
        kompatibilitet == other.kompatibilitet &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (version?.hashCode() ?: 0)
    result = result * 31 + (`data`?.hashCode() ?: 0)
    result = result * 31 + (innsendingstidspunkt?.hashCode() ?: 0)
    result = result * 31 + (mottaker?.hashCode() ?: 0)
    result = result * 31 + (driftsinformasjon?.hashCode() ?: 0)
    result = result * 31 + (kompatibilitet?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonSoknad(version=$version, data=$data, innsendingstidspunkt=$innsendingstidspunkt, mottaker=$mottaker, driftsinformasjon=$driftsinformasjon, kompatibilitet=$kompatibilitet, additionalProperties=$additionalProperties)"
}
