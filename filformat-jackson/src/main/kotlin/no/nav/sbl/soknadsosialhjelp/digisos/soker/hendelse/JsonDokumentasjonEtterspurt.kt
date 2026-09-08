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
import kotlin.collections.List
import kotlin.collections.MutableMap
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonForvaltningsbrev
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonVedlegg

/**
 * Veileder ber om mer dokumentasjon fra søker
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("forvaltningsbrev", "vedlegg", "dokumenter")
public open class JsonDokumentasjonEtterspurt : JsonHendelse(), Serializable {
  @get:JsonProperty("forvaltningsbrev")
  @set:JsonProperty("forvaltningsbrev")
  public var forvaltningsbrev: JsonForvaltningsbrev? = null

  @get:JsonProperty("vedlegg")
  @set:JsonProperty("vedlegg")
  @get:JsonPropertyDescription("Vedlegg til forvaltningsbrev En liste med vedlegg til forvaltningsbrevet som søker skal ha mulighet til å se. Det er ingen garanti for at filene blir vist til søker. Filformatet skal være PDF.")
  public var vedlegg: List<JsonVedlegg>? = mutableListOf()

  @get:JsonProperty("dokumenter")
  @set:JsonProperty("dokumenter")
  @get:JsonPropertyDescription("Liste over dokumentene etterspurt")
  public var dokumenter: List<JsonDokumenter>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withForvaltningsbrev(forvaltningsbrev: JsonForvaltningsbrev?): JsonDokumentasjonEtterspurt {
    this.forvaltningsbrev = forvaltningsbrev
    return this
  }

  public open fun withVedlegg(vedlegg: List<JsonVedlegg>?): JsonDokumentasjonEtterspurt {
    this.vedlegg = vedlegg
    return this
  }

  public open fun withDokumenter(dokumenter: List<JsonDokumenter>?): JsonDokumentasjonEtterspurt {
    this.dokumenter = dokumenter
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonDokumentasjonEtterspurt {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonDokumentasjonEtterspurt {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonDokumentasjonEtterspurt {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonDokumentasjonEtterspurt) return false
    return forvaltningsbrev == other.forvaltningsbrev &&
        vedlegg == other.vedlegg &&
        dokumenter == other.dokumenter &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (forvaltningsbrev?.hashCode() ?: 0)
    result = result * 31 + (vedlegg?.hashCode() ?: 0)
    result = result * 31 + (dokumenter?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonDokumentasjonEtterspurt(forvaltningsbrev=$forvaltningsbrev, vedlegg=$vedlegg, dokumenter=$dokumenter, additionalProperties=$additionalProperties, super=${super.toString()})"
}
