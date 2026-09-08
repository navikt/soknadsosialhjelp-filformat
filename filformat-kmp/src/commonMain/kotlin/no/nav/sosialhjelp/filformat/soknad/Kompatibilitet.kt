package no.nav.sosialhjelp.filformat.soknad

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Inneholder en Markdown-formatert kompatibilitetstekst.
 *
 * Kompatibilitetsteksten skal vises saksbehandler gitt at versjonen man forventer ved parsing av JSON er innenfor intervallet bestemt av "minVersion" og "maxVersion".
 */
@Serializable
public data class Kompatibilitet(
  /**
   * Minimumsversjon for å vise kompatibilitetsteksten (inclusive).
   */
  public val minVersion: String? = null,
  /**
   * Maksimumsversjon for å vise kompatibilitetsteksten (inclusive).
   */
  public val maxVersion: String? = null,
  /**
   * Markdown-formatert tekststreng som kan vises saksbehandler.
   *
   * Formatet som brukes er [CommonMark](https://spec.commonmark.org/0.28/) men med utvidelse for å støtte tabeller slik det er definert i [GitHub Flavored Markdown](https://github.github.com/gfm/). Et eksempel på en implementasjon som fungerer er [markdown-it](https://github.com/markdown-it/markdown-it). Se [eksempelfil](https://raw.githubusercontent.com/navikt/soknadsosialhjelp-filformat/master/src/test/resources/markdown/format.md) og [test kompatibilitet](https://babelmark.github.io/?text=Stor+overskrift%0A%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%3D%0A%0AUnderoverskrift%0A---------------%0A%0AVanlig+tekst+med+**fet+skrift**+og+_kursiv_%2C+_samt+**kombinasjonen**+av+begge_.%0A%0A%3E+Sitattekst%0A%3E+uten+linjeskift.%0A%0A%23%23%23+Liten+overskrift%0A1.+Nummerert+liste%0A+++*+Underpunkt%0A+++*+Underpunkt%0A%0A+++++Tekst+som+er+rykket+inn+p%C3%A5+samme+niv%C3%A5+som+underpunktet+over.%0A%0A2.+Nummerert+liste%0A+++1.+Underliste%0A+++2.+Underliste%0A%0A%23%23%23%23+Enda+mindre+overskrift%0A*+Punkt%0A*+Punkt%0A++*+Underpunkt%0A*+Punkt%0A%0A%23+Alternativ+stor+overskrift%0A%23%23+Alternativ+underoverskrift%0A%0A%7C+Midtstilte+tall++%7CVenstrestilt++++++%7CH%C3%B8yrestilt+%7C%0A%7C+%3A--------------%3A+%7C%3A-----------------%7C----------%3A%7C%0A%7C+++++++42+++++++++%7CMeningen+med+livet%7C1++++++++++%7C%0A%7C+++++++47+++++++++%7CStar+Trek+TM++++++%7C2++++++++++%7C%0A%7C+++++++21+++++++++%7CMed+**fet+skrift**%7C+++++++++++%7C) på Markdownimplementasjoner.
   */
  public val text: String? = null,
)
