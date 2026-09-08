package no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse

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
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonVedlegg

/**
 * Et vedtak, i tilknytning til søknaden, har blitt fattet.
 *
 * Det er kun vedtak som ligger inn under Sosialtjenesteloven som skal sendes.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("saksreferanse", "utfall", "vedtaksfil", "vedlegg")
public open class JsonVedtakFattet : JsonHendelse(), Serializable {
  @get:JsonProperty("saksreferanse")
  @set:JsonProperty("saksreferanse")
  @get:JsonPropertyDescription("Referanse vedtaket skal tilknyttes til")
  public var saksreferanse: String? = null

  @get:JsonProperty("utfall")
  @set:JsonProperty("utfall")
  @get:JsonPropertyDescription("Utfallet i vedtaket")
  public var utfall: Utfall? = null

  @get:JsonProperty("vedtaksfil")
  @set:JsonProperty("vedtaksfil")
  @get:JsonPropertyDescription("Vedtaksfil Vedtaksfilen som søker skal ha mulighet til å se. Det er ingen garanti for at filen blir vist til søker. Filformatet skal være PDF.")
  public var vedtaksfil: JsonVedtaksfil? = null

  @get:JsonProperty("vedlegg")
  @set:JsonProperty("vedlegg")
  @get:JsonPropertyDescription("Vedlegg til vedtaksfil En liste med vedlegg til vedtaksfilen som søker skal ha mulighet til å se. Det er ingen garanti for at filene blir vist til søker. Filformatet skal være PDF.")
  public var vedlegg: List<JsonVedlegg>? = mutableListOf()

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  override fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  override fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withSaksreferanse(saksreferanse: String?): JsonVedtakFattet {
    this.saksreferanse = saksreferanse
    return this
  }

  public open fun withUtfall(utfall: Utfall?): JsonVedtakFattet {
    this.utfall = utfall
    return this
  }

  public open fun withVedtaksfil(vedtaksfil: JsonVedtaksfil?): JsonVedtakFattet {
    this.vedtaksfil = vedtaksfil
    return this
  }

  public open fun withVedlegg(vedlegg: List<JsonVedlegg>?): JsonVedtakFattet {
    this.vedlegg = vedlegg
    return this
  }

  override fun withType(type: JsonHendelse.Type?): JsonVedtakFattet {
    this.type = type
    return this
  }

  override fun withHendelsestidspunkt(hendelsestidspunkt: String?): JsonVedtakFattet {
    this.hendelsestidspunkt = hendelsestidspunkt
    return this
  }

  override fun withAdditionalProperty(name: String, `value`: Any?): JsonVedtakFattet {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonVedtakFattet) return false
    return saksreferanse == other.saksreferanse &&
        utfall == other.utfall &&
        vedtaksfil == other.vedtaksfil &&
        vedlegg == other.vedlegg &&
        additionalProperties == other.additionalProperties &&
        super.equals(other)
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (saksreferanse?.hashCode() ?: 0)
    result = result * 31 + (utfall?.hashCode() ?: 0)
    result = result * 31 + (vedtaksfil?.hashCode() ?: 0)
    result = result * 31 + (vedlegg?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    result = result * 31 + super.hashCode()
    return result
  }

  override fun toString(): String = "JsonVedtakFattet(saksreferanse=$saksreferanse, utfall=$utfall, vedtaksfil=$vedtaksfil, vedlegg=$vedlegg, additionalProperties=$additionalProperties, super=${super.toString()})"

  public enum class Utfall(
    @JsonValue
    public val `value`: String,
  ) {
    INNVILGET("INNVILGET"),
    DELVIS_INNVILGET("DELVIS_INNVILGET"),
    AVSLATT("AVSLATT"),
    AVVIST("AVVIST"),
    ;

    public companion object {
      @JsonCreator
      public fun fromValue(`value`: String): Utfall = entries.firstOrNull { it.value == value } ?: throw IllegalArgumentException(value)
    }
  }
}
