package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable

@Serializable
public data class OkonomiOpplysningUtbetalingKomponent(
  /**
   * Beskrivelse av hva slags type delutbetaling det er. Eksempler: "Arbeidstaker", "Grunnpensjon" og "Tilleggspensjon".
   */
  public val type: String? = null,
  /**
   * Beløp for delutbetalingen. Resultat av satsType, satsAntall og satsBelop
   */
  public val belop: Double? = null,
  /**
   * Beskrivelse av hva slags type sats det er. Eksempel: "Dag" og "Prosent".
   */
  public val satsType: String? = null,
  /**
   * Antall enheter av satstypen for delutbetalingen.
   */
  public val satsAntall: Double? = null,
  /**
   * Satsbeløpet for delutbetalingen
   */
  public val satsBelop: Double? = null,
)
