package no.nav.sosialhjelp.filformat.soknad.okonomi

import kotlinx.serialization.Serializable

/**
 * Økonomiske data.
 */
@Serializable
public data class Okonomi(
  /**
   * Økonomiske opplysninger som ikke inngår i den strukturerte oversikten.
   *
   * Flott hvis saksbehandlers behov/ønsker kan diskuteres på Slack slik at en mer strukturert måte å presentere dataene på kan utarbeides.
   */
  public val opplysninger: Okonomiopplysninger,
  /**
   * Strukturert økonomisk oversikt.
   */
  public val oversikt: Okonomioversikt? = null,
)
