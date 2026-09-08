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

/**
 * Dokument
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("dokumenttype", "tilleggsinformasjon", "innsendelsesfrist", "dokumentreferanse")
public open class JsonDokumenter : Serializable {
  @get:JsonProperty("dokumenttype")
  @set:JsonProperty("dokumenttype")
  @get:JsonPropertyDescription("Dokumenttype En predefinert eller fritekstbasert dokumenttype som veileder etterspør fra søker, eks: \"strømfaktura\" eller \"kontoutskrift\"")
  public var dokumenttype: String? = null

  @get:JsonProperty("tilleggsinformasjon")
  @set:JsonProperty("tilleggsinformasjon")
  @get:JsonPropertyDescription("Tilleggsinformasjon Tilleggsinformasjon til dokumenttypen veileder etterspør")
  public var tilleggsinformasjon: String? = null

  @get:JsonProperty("innsendelsesfrist")
  @set:JsonProperty("innsendelsesfrist")
  public var innsendelsesfrist: String? = null

  @get:JsonProperty("dokumentreferanse")
  @set:JsonProperty("dokumentreferanse")
  @get:JsonPropertyDescription("Dokumentreferanse Referansen til dette dokumentet som etterspørres. Vil bli sendt tilbake i vedlegg.json")
  public var dokumentreferanse: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withDokumenttype(dokumenttype: String?): JsonDokumenter {
    this.dokumenttype = dokumenttype
    return this
  }

  public open fun withTilleggsinformasjon(tilleggsinformasjon: String?): JsonDokumenter {
    this.tilleggsinformasjon = tilleggsinformasjon
    return this
  }

  public open fun withInnsendelsesfrist(innsendelsesfrist: String?): JsonDokumenter {
    this.innsendelsesfrist = innsendelsesfrist
    return this
  }

  public open fun withDokumentreferanse(dokumentreferanse: String?): JsonDokumenter {
    this.dokumentreferanse = dokumentreferanse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonDokumenter {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonDokumenter) return false
    return dokumenttype == other.dokumenttype &&
        tilleggsinformasjon == other.tilleggsinformasjon &&
        innsendelsesfrist == other.innsendelsesfrist &&
        dokumentreferanse == other.dokumentreferanse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (dokumenttype?.hashCode() ?: 0)
    result = result * 31 + (tilleggsinformasjon?.hashCode() ?: 0)
    result = result * 31 + (innsendelsesfrist?.hashCode() ?: 0)
    result = result * 31 + (dokumentreferanse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonDokumenter(dokumenttype=$dokumenttype, tilleggsinformasjon=$tilleggsinformasjon, innsendelsesfrist=$innsendelsesfrist, dokumentreferanse=$dokumentreferanse, additionalProperties=$additionalProperties)"
}
