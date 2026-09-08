package no.nav.sosialhjelp.filformat.soknad.okonomi.opplysning

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class OkonomiOpplysningUtgift(
  public val kilde: Kilde,
  /**
   * Kodeverdi som angir hva slags type utgift det er. Dette feltet kan for eksempel brukes til å filtrere bort utgifter man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "strom", "kommunalAvgift", "oppvarming", "annenBoutgift", "barnFritidsaktiviteter", "barnTannregulering", "annenBarneutgift" og "annen".
   */
  public val type: String,
  /**
   * En tittel som MÅ brukes hvis utgiften skal presenteres til saksbehandler. Brukerangitt tekst kan være inkludert i tittelen.
   */
  public val tittel: String,
  /**
   * Brukes når en søker overstyrer/endrer. Settes kun til "true" på utgift med systemkilde. Anbefaler at man likevel viser dataene til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).
   */
  public val overstyrtAvBruker: Boolean,
  public val belop: Int? = null,
)
