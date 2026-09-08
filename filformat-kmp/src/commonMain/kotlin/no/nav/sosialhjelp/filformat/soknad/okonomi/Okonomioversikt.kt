package no.nav.sosialhjelp.filformat.soknad.okonomi

import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.okonomi.oversikt.OkonomioversiktFormue
import no.nav.sosialhjelp.filformat.soknad.okonomi.oversikt.OkonomioversiktInntekt
import no.nav.sosialhjelp.filformat.soknad.okonomi.oversikt.OkonomioversiktUtgift

/**
 * Strukturert økonomisk oversikt.
 */
@Serializable
public data class Okonomioversikt(
  /**
   * Månedlige inntekter
   */
  public val inntekt: List<OkonomioversiktInntekt>,
  /**
   * Månedlige utgifter
   */
  public val utgift: List<OkonomioversiktUtgift>? = null,
  /**
   * Formue
   */
  public val formue: List<OkonomioversiktFormue>? = null,
)
