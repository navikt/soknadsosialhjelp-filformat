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
 * Inneholder en Markdown-formatert kompatibilitetstekst.
 *
 * Kompatibilitetsteksten skal vises saksbehandler gitt at versjonen man forventer ved parsing av JSON er innenfor intervallet bestemt av "minVersion" og "maxVersion".
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("minVersion", "maxVersion", "text")
public open class JsonKompatibilitet : Serializable {
  @get:JsonProperty("minVersion")
  @set:JsonProperty("minVersion")
  @get:JsonPropertyDescription("Minimumsversjon for å vise kompatibilitetsteksten (inclusive).")
  public var minVersion: String? = null

  @get:JsonProperty("maxVersion")
  @set:JsonProperty("maxVersion")
  @get:JsonPropertyDescription("Maksimumsversjon for å vise kompatibilitetsteksten (inclusive).")
  public var maxVersion: String? = null

  @get:JsonProperty("text")
  @set:JsonProperty("text")
  @get:JsonPropertyDescription("Markdown-formatert tekststreng som kan vises saksbehandler. Formatet som brukes er [CommonMark](https://spec.commonmark.org/0.28/) men med utvidelse for å støtte tabeller slik det er definert i [GitHub Flavored Markdown](https://github.github.com/gfm/). Et eksempel på en implementasjon som fungerer er [markdown-it](https://github.com/markdown-it/markdown-it). Se [eksempelfil](https://raw.githubusercontent.com/navikt/soknadsosialhjelp-filformat/master/src/test/resources/markdown/format.md) og [test kompatibilitet](https://babelmark.github.io/?text=Stor+overskrift%0A%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%0A%0AUnderoverskrift%0A---------------%0A%0AVanlig+tekst+med+**fet+skrift**+og+_kursiv_%2C+_samt+**kombinasjonen**+av+begge_.%0A%0A%3E+Sitattekst%0A%3E+uten+linjeskift.%0A%0A%23%23%23+Liten+overskrift%0A1.+Nummerert+liste%0A+++*+Underpunkt%0A+++*+Underpunkt%0A%0A+++++Tekst+som+er+rykket+inn+p%C3%A5+samme+niv%C3%A5+som+underpunktet+over.%0A%0A2.+Nummerert+liste%0A+++1.+Underliste%0A+++2.+Underliste%0A%0A%23%23%23%23+Enda+mindre+overskrift%0A*+Punkt%0A*+Punkt%0A++*+Underpunkt%0A*+Punkt%0A%0A%23+Alternativ+stor+overskrift%0A%23%23+Alternativ+underoverskrift%0A%0A%7C+Midtstilte+tall++%7CVenstrestilt++++++%7CH%C3%B8yrestilt+%7C%0A%7C+%3A--------------%3A+%7C%3A-----------------%7C----------%3A%7C%0A%7C+++++++42+++++++++%7CMeningen+med+livet%7C1++++++++++%7C%0A%7C+++++++47+++++++++%7CStar+Trek+TM++++++%7C2++++++++++%7C%0A%7C+++++++21+++++++++%7CMed+**fet+skrift**%7C+++++++++++%7C) på Markdownimplementasjoner.")
  public var text: String? = null

  @JsonIgnore
  private val additionalProperties: MutableMap<String, Any?> = LinkedHashMap()

  @JsonAnyGetter
  public open fun getAdditionalProperties(): MutableMap<String, Any?> = additionalProperties

  @JsonAnySetter
  public open fun setAdditionalProperty(name: String, `value`: Any?) {
    additionalProperties[name] = value
  }

  public open fun withMinVersion(minVersion: String?): JsonKompatibilitet {
    this.minVersion = minVersion
    return this
  }

  public open fun withMaxVersion(maxVersion: String?): JsonKompatibilitet {
    this.maxVersion = maxVersion
    return this
  }

  public open fun withText(text: String?): JsonKompatibilitet {
    this.text = text
    return this
  }

  public open fun withAdditionalProperty(name: String, `value`: Any?): JsonKompatibilitet {
    additionalProperties[name] = value
    return this
  }

  override fun equals(other: Any?): Boolean {
    if (other === this) return true
    if (other !is JsonKompatibilitet) return false
    return minVersion == other.minVersion &&
        maxVersion == other.maxVersion &&
        text == other.text &&
        additionalProperties == other.additionalProperties
  }

  override fun hashCode(): Int {
    var result = 1
    result = result * 31 + (minVersion?.hashCode() ?: 0)
    result = result * 31 + (maxVersion?.hashCode() ?: 0)
    result = result * 31 + (text?.hashCode() ?: 0)
    result = result * 31 + additionalProperties.hashCode()
    return result
  }

  override fun toString(): String = "JsonKompatibilitet(minVersion=$minVersion, maxVersion=$maxVersion, text=$text, additionalProperties=$additionalProperties)"
}
