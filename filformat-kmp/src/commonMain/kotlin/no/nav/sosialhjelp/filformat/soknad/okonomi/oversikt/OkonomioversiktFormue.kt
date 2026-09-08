package no.nav.sosialhjelp.filformat.soknad.okonomi.oversikt

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class OkonomioversiktFormue(
  public val kilde: Kilde,
  /**
   * Kodeverdi som angir hva slags type formue det er. Dette feltet kan for eksempel brukes til å filtrere bort formue man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "brukskonto", "bsu", "sparekonto", "livsforsikringssparedel", "verdipapirer" og "belop".
   */
  public val type: String,
  /**
   * En tittel som MÅ brukes hvis formuen skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.
   */
  public val tittel: String,
  /**
   * Brukes når en søker overstyrer/endrer. Settes kun til "true" på formue med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).
   */
  public val overstyrtAvBruker: Boolean,
  /**
   * Kan mangle hvis bruker har sagt at han/hun har en gitt type formue, men beløp mangler.
   */
  public val belop: Int? = null,
)
