package no.nav.sosialhjelp.filformat.soknad.okonomi.oversikt

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class OkonomioversiktInntekt(
  public val kilde: Kilde,
  /**
   * Kodeverdi som angir hva slags type inntekt det er. Dette feltet kan for eksempel brukes til å filtrere bort inntekter man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "jobb", "studielanOgStipend", "barnebidrag" og "bostotte".
   */
  public val type: String,
  /**
   * En tittel som MÅ brukes hvis inntekten skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.
   */
  public val tittel: String,
  /**
   * Brukes når en søker overstyrer/endrer. Settes kun til "true" på inntekt med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).
   */
  public val overstyrtAvBruker: Boolean,
  /**
   * Kan mangle hvis bruker har sagt at han/hun har en gitt type inntekt, men beløp mangler.
   */
  public val brutto: Int? = null,
  /**
   * Kan mangle hvis bruker har sagt at han/hun har en gitt type inntekt, men beløp mangler.
   */
  public val netto: Int? = null,
)
