package no.nav.sbl.soknadsosialhjelp.klage

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
import no.nav.sbl.soknadsosialhjelp.soknad.JsonSoknadsmottaker
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonPersonIdentifikator
import no.nav.sbl.soknadsosialhjelp.soknad.personalia.JsonSokernavn

/**
 * Json-struktur for Klage på Vedtak
 *
 * Encoding er UTF-8.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("klageId", "vedtakId", "digisosId", "personIdentifikator", "navn", "mottaker", "autentisering", "innsendingstidspunkt", "begrunnelse")
public open class JsonKlage : Serializable {
  @get:JsonProperty("klageId")
  @set:JsonProperty("klageId")
  @get:JsonPropertyDescription("Unik identifikator for klagen (UUID)")
  public var klageId: String? = null

  @get:JsonProperty("vedtakId")
  @set:JsonProperty("vedtakId")
  @get:JsonPropertyDescription("Referanse til vedtak som det klages på (UUID)")
  public var vedtakId: String? = null

  @get:JsonProperty("digisosId")
  @set:JsonProperty("digisosId")
  @get:JsonPropertyDescription("Referanse til original søknad (UUID)")
  public var digisosId: String? = null

  @get:JsonProperty("personIdentifikator")
  @set:JsonProperty("personIdentifikator")
  public var personIdentifikator: JsonPersonIdentifikator? = null

  @get:JsonProperty("navn")
  @set:JsonProperty("navn")
  public var navn: JsonSokernavn? = null

  @get:JsonProperty("mottaker")
  @set:JsonProperty("mottaker")
  public var mottaker: JsonSoknadsmottaker? = null

  @get:JsonProperty("autentisering")
  @set:JsonProperty("autentisering")
  @get:JsonPropertyDescription("Autentiseringsinformasjon")
  public var autentisering: JsonAutentisering? = null

  @get:JsonProperty("innsendingstidspunkt")
  @set:JsonProperty("innsendingstidspunkt")
  public var innsendingstidspunkt: String? = null

  @get:JsonProperty("begrunnelse")
  @set:JsonProperty("begrunnelse")
  @get:JsonPropertyDescription("Begrunnelse for klagen")
  public var begrunnelse: JsonBegrunnelse? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withKlageId(klageId: String?): JsonKlage {
    this.klageId = klageId
    return this
  }

  public open fun withVedtakId(vedtakId: String?): JsonKlage {
    this.vedtakId = vedtakId
    return this
  }

  public open fun withDigisosId(digisosId: String?): JsonKlage {
    this.digisosId = digisosId
    return this
  }

  public open fun withPersonIdentifikator(personIdentifikator: JsonPersonIdentifikator?): JsonKlage {
    this.personIdentifikator = personIdentifikator
    return this
  }

  public open fun withNavn(navn: JsonSokernavn?): JsonKlage {
    this.navn = navn
    return this
  }

  public open fun withMottaker(mottaker: JsonSoknadsmottaker?): JsonKlage {
    this.mottaker = mottaker
    return this
  }

  public open fun withAutentisering(autentisering: JsonAutentisering?): JsonKlage {
    this.autentisering = autentisering
    return this
  }

  public open fun withInnsendingstidspunkt(innsendingstidspunkt: String?): JsonKlage {
    this.innsendingstidspunkt = innsendingstidspunkt
    return this
  }

  public open fun withBegrunnelse(begrunnelse: JsonBegrunnelse?): JsonKlage {
    this.begrunnelse = begrunnelse
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonKlage {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonKlage) return false
    return klageId == other.klageId &&
        vedtakId == other.vedtakId &&
        digisosId == other.digisosId &&
        personIdentifikator == other.personIdentifikator &&
        navn == other.navn &&
        mottaker == other.mottaker &&
        autentisering == other.autentisering &&
        innsendingstidspunkt == other.innsendingstidspunkt &&
        begrunnelse == other.begrunnelse &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (klageId?.hashCode() ?: 0)
    result = result * 31 + (vedtakId?.hashCode() ?: 0)
    result = result * 31 + (digisosId?.hashCode() ?: 0)
    result = result * 31 + (personIdentifikator?.hashCode() ?: 0)
    result = result * 31 + (navn?.hashCode() ?: 0)
    result = result * 31 + (mottaker?.hashCode() ?: 0)
    result = result * 31 + (autentisering?.hashCode() ?: 0)
    result = result * 31 + (innsendingstidspunkt?.hashCode() ?: 0)
    result = result * 31 + (begrunnelse?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonKlage(klageId=$klageId, vedtakId=$vedtakId, digisosId=$digisosId, personIdentifikator=$personIdentifikator, navn=$navn, mottaker=$mottaker, autentisering=$autentisering, innsendingstidspunkt=$innsendingstidspunkt, begrunnelse=$begrunnelse, additionalProperties=$additionalProperties)"
}
