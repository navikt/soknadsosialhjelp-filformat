package no.nav.sosialhjelp.filformat.soknad.okonomi

import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.bostotte.Bostotte
import no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning.OkonomiOpplysningUtbetaling
import no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning.OkonomiOpplysningUtgift
import no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning.Okonomibekreftelse
import no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning.OkonomibeskrivelserAvAnnet

/**
 * Økonomiske opplysninger som ikke inngår i den strukturerte oversikten.
 *
 * Flott hvis saksbehandlers behov/ønsker kan diskuteres på Slack slik at en mer strukturert måte å presentere dataene på kan utarbeides.
 */
@Serializable
public data class Okonomiopplysninger(
  public val utbetaling: List<OkonomiOpplysningUtbetaling>,
  /**
   * Bekreftelser fra bruker.
   *
   * Både ja/nei-svar og samtykker fra bruker.
   */
  public val bekreftelse: List<Okonomibekreftelse>? = null,
  /**
   * Beskrivelsesfelter for inntekter, utgifter og verdier.
   *
   * Disse feltene er overflødige og vil bli tatt bort.
   */
  public val beskrivelseAvAnnet: OkonomibeskrivelserAvAnnet? = null,
  /**
   * Månedlige utgifter
   */
  public val utgift: List<OkonomiOpplysningUtgift>? = null,
  public val bostotte: Bostotte? = null,
)
