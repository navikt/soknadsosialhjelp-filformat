package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Beskrivelsesfelter for inntekter, utgifter og verdier.
 *
 * Disse feltene er overflødige og vil bli tatt bort.
 */
@Serializable
public data class OkonomibeskrivelserAvAnnet(
  public val kilde: KildeBruker,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av økonomisk verdi.
   */
  public val verdi: String,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av innskudd eller sparing.
   */
  public val sparing: String,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av utbetalinger.
   */
  public val utbetaling: String,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift) av annet brukeren har av boutgifter. Feltet er utdatert.
   */
  public val boutgifter: String,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift) av andre utgifter til barn som brukeren har. Feltet er utdatert.
   */
  public val barneutgifter: String,
)
