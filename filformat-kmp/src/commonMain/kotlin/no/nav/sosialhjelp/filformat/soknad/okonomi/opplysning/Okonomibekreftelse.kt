package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class Okonomibekreftelse(
  public val kilde: Kilde,
  /**
   * Kodeverdi som angir hva slags type bekreftelse det er. Dette feltet kan for eksempel brukes til å filtrere bort bekreftelser man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "bostotte", "verdi", "sparing", "utbetaling", "boutgifter" og "barneutgifter".
   */
  public val type: String,
  /**
   * En tittel som MÅ brukes hvis bekreftelsen skal presenteres til saksbehandler.
   */
  public val tittel: String,
  /**
   * Kan være manglende hvis bruker ikke har besvart spørsmålet.
   */
  public val verdi: Boolean? = null,
  /**
   * Tidspunkt for når denne bekreftelsen ble gitt.
   */
  public val bekreftelsesDato: String? = null,
)
