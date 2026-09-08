package no.nav.sbl.soknadsosialhjelp.soknad.familie

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
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde

/**
 * Sivilstatus og informasjon om eventuell ektefelle.
 *
 * Hvis "sivilstatus" mangler betyr dette at søker ikke har svart på spørsmålet.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("kilde", "status", "ektefelle", "ektefelleHarDiskresjonskode", "folkeregistrertMedEktefelle", "borSammenMed", "borIkkeSammenMedBegrunnelse")
public open class JsonSivilstatus : Serializable {
  @get:JsonProperty("kilde")
  @set:JsonProperty("kilde")
  public var kilde: JsonKilde? = null

  @get:JsonProperty("status")
  @set:JsonProperty("status")
  @get:JsonPropertyDescription("Sivilstatus til søker. Sivilstatus til søker. Med \"gift\" menes både gift og registrert partner.")
  public var status: Status? = null

  @get:JsonProperty("ektefelle")
  @set:JsonProperty("ektefelle")
  @get:JsonPropertyDescription("Søkers ektefelle")
  public var ektefelle: JsonEktefelle? = null

  @get:JsonProperty("ektefelleHarDiskresjonskode")
  @set:JsonProperty("ektefelleHarDiskresjonskode")
  @get:JsonPropertyDescription("Settes til true hvis ektefelle/registrert partner har diskresjonskode 6 eller 7. Kun relevant hvis \"kilde\" er \"system\".")
  public var ektefelleHarDiskresjonskode: Boolean? = null

  @get:JsonProperty("folkeregistrertMedEktefelle")
  @set:JsonProperty("folkeregistrertMedEktefelle")
  @get:JsonPropertyDescription("Kun relevant hvis \"kilde\" er \"system\".")
  public var folkeregistrertMedEktefelle: Boolean? = null

  @get:JsonProperty("borSammenMed")
  @set:JsonProperty("borSammenMed")
  @get:JsonPropertyDescription("Kun relevant hvis \"kilde\" er \"bruker\".")
  public var borSammenMed: Boolean? = null

  @get:JsonProperty("borIkkeSammenMedBegrunnelse")
  @set:JsonProperty("borIkkeSammenMedBegrunnelse")
  @get:JsonPropertyDescription("Brukerskrevet tekstlig forklaring (inkl. linjeskift). Kun relevant \"kilde\" er \"bruker\".")
  public var borIkkeSammenMedBegrunnelse: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKilde(kilde: JsonKilde?): JsonSivilstatus {
    this.kilde = kilde
    return this
  }

  public open fun withStatus(status: Status?): JsonSivilstatus {
    this.status = status
    return this
  }

  public open fun withEktefelle(ektefelle: JsonEktefelle?): JsonSivilstatus {
    this.ektefelle = ektefelle
    return this
  }

  public open fun withEktefelleHarDiskresjonskode(ektefelleHarDiskresjonskode: Boolean?): JsonSivilstatus {
    this.ektefelleHarDiskresjonskode = ektefelleHarDiskresjonskode
    return this
  }

  public open fun withFolkeregistrertMedEktefelle(folkeregistrertMedEktefelle: Boolean?): JsonSivilstatus {
    this.folkeregistrertMedEktefelle = folkeregistrertMedEktefelle
    return this
  }

  public open fun withBorSammenMed(borSammenMed: Boolean?): JsonSivilstatus {
    this.borSammenMed = borSammenMed
    return this
  }

  public open fun withBorIkkeSammenMedBegrunnelse(borIkkeSammenMedBegrunnelse: String?): JsonSivilstatus {
    this.borIkkeSammenMedBegrunnelse = borIkkeSammenMedBegrunnelse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonSivilstatus {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonSivilstatus) return false
    return kilde == other.kilde &&
        status == other.status &&
        ektefelle == other.ektefelle &&
        ektefelleHarDiskresjonskode == other.ektefelleHarDiskresjonskode &&
        folkeregistrertMedEktefelle == other.folkeregistrertMedEktefelle &&
        borSammenMed == other.borSammenMed &&
        borIkkeSammenMedBegrunnelse == other.borIkkeSammenMedBegrunnelse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (kilde?.hashCode() ?: 0)
    result = result * 31 + (status?.hashCode() ?: 0)
    result = result * 31 + (ektefelle?.hashCode() ?: 0)
    result = result * 31 + (ektefelleHarDiskresjonskode?.hashCode() ?: 0)
    result = result * 31 + (folkeregistrertMedEktefelle?.hashCode() ?: 0)
    result = result * 31 + (borSammenMed?.hashCode() ?: 0)
    result = result * 31 + (borIkkeSammenMedBegrunnelse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonSivilstatus(kilde=$kilde, status=$status, ektefelle=$ektefelle, ektefelleHarDiskresjonskode=$ektefelleHarDiskresjonskode, folkeregistrertMedEktefelle=$folkeregistrertMedEktefelle, borSammenMed=$borSammenMed, borIkkeSammenMedBegrunnelse=$borIkkeSammenMedBegrunnelse, additionalProperties=$additionalProperties)"

  public enum class Status(
    @JsonValue
    public val `value`: String,
  ) {
    GIFT("gift"),
    UGIFT("ugift"),
    SAMBOER("samboer"),
    ENKE("enke"),
    SKILT("skilt"),
    SEPARERT("separert"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Status = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
