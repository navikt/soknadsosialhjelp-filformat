package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.Boolean
import kotlin.Double
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Utbetaling
 *
 * Utbetalingsinformasjon
 */
@Serializable
public data class Utbetaling(
  /**
   * Unik referanse per utbetaling slik at utbetalingsinformasjonen kan bli skiftet ut når det kommer ny informasjon
   */
  public val utbetalingsreferanse: String,
  override val hendelsestidspunkt: String,
  /**
   * Referanse utbetalingen skal tilknyttes til (samme som i vedtak fattet og saksstatus)
   */
  public val saksreferanse: String? = null,
  /**
   * Settes dersom utbetalingen er en del av et rammevedtak
   */
  public val rammevedtaksreferanse: String? = null,
  /**
   * Status for utbetalingen
   */
  public val status: Status? = null,
  /**
   * Utbetalingsbeløp i kr
   */
  public val belop: Double? = null,
  /**
   * Stønaden utbetalingen gjelder for (livsopphold, strøm etc.)
   */
  public val beskrivelse: String? = null,
  /**
   * Når betalingen er lagt til forfall
   */
  public val forfallsdato: String? = null,
  /**
   * Når utbetalingen kom inn på konto
   */
  public val utbetalingsdato: String? = null,
  /**
   * Utbetalingsperiode (Fra)
   */
  public val fom: String? = null,
  /**
   * Utbetalingsperiode (Til)
   */
  public val tom: String? = null,
  /**
   * Om en annen mottaker enn brukeren skal ha pengene
   */
  public val annenMottaker: Boolean? = null,
  /**
   * Mottaker (søker eller annen mottaker), fnummer, orgnummer, eller navn
   */
  public val mottaker: String? = null,
  /**
   * Mottakers kontonummer, bank i Norge, blir bare vist dersom mottaker er brukeren
   */
  public val kontonummer: String? = null,
  /**
   * Utbetalingsmetode, eks kontooverføring, kontantkort
   */
  public val utbetalingsmetode: String? = null,
  override val type: String = "utbetaling",
) : Hendelse {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    PLANLAGT_UTBETALING("PLANLAGT_UTBETALING"),
    UTBETALT("UTBETALT"),
    STOPPET("STOPPET"),
    ANNULLERT("ANNULLERT"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("Utbetaling.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
