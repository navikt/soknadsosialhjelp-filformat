package no.nav.sbl.soknadsosialhjelp.vedlegg

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
import kotlin.collections.List
import kotlin.collections.MutableMap

/**
 * Vedlegg
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("type", "tilleggsinfo", "klageId", "status", "filer", "hendelseType", "hendelseReferanse")
public open class JsonVedlegg : Serializable {
  @get:JsonProperty("type")
  @set:JsonProperty("type")
  @get:JsonPropertyDescription("Angir hvilken type vedlegg det gjelder. Det kan komme nye typer og dette må håndteres dynamisk av konsumenter. Her er noen av de mulige typene:\n* annet\n * barnebidrag\n * bostotte\n * dokumentasjon\n * faktura\n * husleiekontrakt\n * kjopekontrakt\n * kontooversikt\n * lonnslipp\n * nedbetalingsplan\n * oppholdstillatel\n * salgsoppgjor\n * samvarsavtale\n * skattemelding\n * student\n")
  public var type: String? = null

  @get:JsonProperty("tilleggsinfo")
  @set:JsonProperty("tilleggsinfo")
  @get:JsonPropertyDescription("Angir subtype til vedlegg det gjelder. Det vil jevnlig komme nye subtyper og dette må håndteres dynamisk av konsumenter. Her er noen av de mulige typene:\n* aksjer\n * annet\n * annetbarnutgift\n * annetboutgift\n * annetinntekter\n * annetverdi\n * arbeid\n * avdraglaan\n * barn\n * barnehage\n * betaler\n * brukskonto\n * bsu\n * campingvogn\n * eiendom\n * forsikringsutbetaling\n * fritidsaktivitet\n * fritidseiendom\n * husleie\n * husleiekontrakt\n * kjopekontrakt\n * kjoretoy\n * kommunaleavgifter\n * livsforsikring\n * mottar\n * oppholdstillatel\n * oppvarming\n * sfo\n * skattemelding\n * sparekonto\n * strom\n * tannbehandling\n * utbytte\n * vedtak")
  public var tilleggsinfo: String? = null

  @get:JsonProperty("klageId")
  @set:JsonProperty("klageId")
  @get:JsonPropertyDescription("Id til klagen som vedlegget hører til, hvis det er tilfelle. Optional.")
  public var klageId: String? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Status for vedlegget. Kan være \"LastetOpp\", \"VedleggKreves\" eller \"VedleggAlleredeSendt\". Hvis \"status\" har blitt satt til \"VedleggAlleredeSendt\" eller \"VedleggKreves\" vil \"filer\" være en tom array. Ukjente statusverdier skal ikke hindre visning/arkivering av eventuelle filer.")
  public var status: String? = null

  @get:JsonProperty("filer")
  @set:JsonProperty("filer")
  @get:JsonPropertyDescription("Array Liste med filer som hører til det samme vedlegget.")
  public var filer: List<JsonFiler>? = mutableListOf()

  @get:JsonProperty("hendelseType")
  @set:JsonProperty("hendelseType")
  @get:JsonPropertyDescription("Angir typen til hendelsen som fikk brukeren til å laste opp dette vedlegget. Mulige typer:\n* dokumentasjonEtterspurt - om vedlegget ble lastet opp på grunn av hendelsen dokumentasjonEtterspurt \n* dokumentasjonkrav - om vedlegget ble lastet opp på grunn av hendelsen dokumentasjonkrav \n* soknad - om vedlegget ble lastet opp på grunn av vedleggskrav generert av søknaden \n* bruker - når bruker selv velger å laste opp annen dokumentasjon")
  public var hendelseType: HendelseType? = null

  @get:JsonProperty("hendelseReferanse")
  @set:JsonProperty("hendelseReferanse")
  @get:JsonPropertyDescription("Peker på referansen til hendelsen som fikk brukeren til å laste opp dette vedlegget.")
  public var hendelseReferanse: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withType(type: String?): JsonVedlegg {
    this.type = type
    return this
  }

  public open fun withTilleggsinfo(tilleggsinfo: String?): JsonVedlegg {
    this.tilleggsinfo = tilleggsinfo
    return this
  }

  public open fun withKlageId(klageId: String?): JsonVedlegg {
    this.klageId = klageId
    return this
  }

  public open fun withStatus(status: String?): JsonVedlegg {
    this.status = status
    return this
  }

  public open fun withFiler(filer: List<JsonFiler>?): JsonVedlegg {
    this.filer = filer
    return this
  }

  public open fun withHendelseType(hendelseType: HendelseType?): JsonVedlegg {
    this.hendelseType = hendelseType
    return this
  }

  public open fun withHendelseReferanse(hendelseReferanse: String?): JsonVedlegg {
    this.hendelseReferanse = hendelseReferanse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonVedlegg {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonVedlegg) return false
    return type == other.type &&
        tilleggsinfo == other.tilleggsinfo &&
        klageId == other.klageId &&
        status == other.status &&
        filer == other.filer &&
        hendelseType == other.hendelseType &&
        hendelseReferanse == other.hendelseReferanse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (type?.hashCode() ?: 0)
    result = result * 31 + (tilleggsinfo?.hashCode() ?: 0)
    result = result * 31 + (klageId?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + (filer?.hashCode() ?: 0)
    result = result * 31 + (hendelseType?.hashCode() ?: 0)
    result = result * 31 + (hendelseReferanse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonVedlegg(type=$type, tilleggsinfo=$tilleggsinfo, klageId=$klageId, status=$status, filer=$filer, hendelseType=$hendelseType, hendelseReferanse=$hendelseReferanse, additionalProperties=$additionalProperties)"

  public enum class HendelseType(
    @JsonValue
    public val `value`: String,
  ) {
    DOKUMENTASJON_ETTERSPURT("dokumentasjonEtterspurt"),
    DOKUMENTASJONKRAV("dokumentasjonkrav"),
    SOKNAD("soknad"),
    BRUKER("bruker"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): HendelseType = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
