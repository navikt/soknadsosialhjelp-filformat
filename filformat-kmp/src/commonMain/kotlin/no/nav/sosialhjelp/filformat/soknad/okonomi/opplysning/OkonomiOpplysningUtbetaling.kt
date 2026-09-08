package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.Boolean
import kotlin.Double
import kotlin.Int
import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Utbetalinger søker har mottatt
 */
@Serializable
public data class OkonomiOpplysningUtbetaling(
  public val kilde: Kilde,
  /**
   * Kodeverdi som angir hva slags type utbetaling det er. Dette feltet kan for eksempel brukes til å filtrere bort utbetalinger man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "utbytte", "salg", "forsikring" og "annen".
   */
  public val type: String,
  /**
   * En tittel som MÅ brukes hvis utbetalingen skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.
   */
  public val tittel: String,
  /**
   * Brukes når en søker overstyrer/endrer. Settes kun til "true" på utbetaling med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).
   */
  public val overstyrtAvBruker: Boolean,
  public val organisasjon: Organisasjon? = null,
  public val belop: Int? = null,
  /**
   * Nettobeløp for utbetalingen.
   */
  public val netto: Double? = null,
  /**
   * Bruttobeløp for utbetalingen.
   */
  public val brutto: Double? = null,
  /**
   * Totalsum for skattetrekk som gjøres for utbetalingen.
   */
  public val skattetrekk: Double? = null,
  /**
   * Totalsum for andre trekk som gjøres for utbetalingen.
   */
  public val andreTrekk: Double? = null,
  public val utbetalingsdato: String? = null,
  /**
   * Ytelsen som gir utbetalingen gjelder fra og med denne datoen.
   */
  public val periodeFom: String? = null,
  /**
   * Ytelsen som gir utbetalingen gjelder til og med denne datoen.
   */
  public val periodeTom: String? = null,
  /**
   * Liste over delutbetalinger hvis utbetalingen består av flere deler.
   */
  public val komponenter: List<OkonomiOpplysningUtbetalingKomponent>? = null,
  /**
   * Hvem som har mottatt utbetalingen. Eksempler: "Husstand", "Kommune".
   */
  public val mottaker: Mottaker? = null,
) {
  @Serializable(with = MottakerSerializer::class)
  public enum class Mottaker(
    public val jsonValue: String,
  ) {
    HUSSTAND("Husstand"),
    KOMMUNE("Kommune"),
    UKJENT("UKJENT"),
    ;
  }

  public object MottakerSerializer : UkjentTolerantEnumSerializer<Mottaker>("OkonomiOpplysningUtbetaling.Mottaker", Mottaker.entries.toTypedArray(), Mottaker.UKJENT, Mottaker::jsonValue)
}
