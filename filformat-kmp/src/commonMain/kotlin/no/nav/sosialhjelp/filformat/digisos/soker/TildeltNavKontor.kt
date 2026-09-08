package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable

/**
 * Nytt NAV-kontor behandler søknaden.
 */
@Serializable
public data class TildeltNavKontor(
  /**
   * En identifikator for NAV-kontor. Vi benytter NORG som har oversikt over alle NAV-kontor i Norge, for hver kommune og bydel.
   */
  public val navKontor: String,
  override val hendelsestidspunkt: String,
  override val type: String = "tildeltNavKontor",
) : Hendelse
