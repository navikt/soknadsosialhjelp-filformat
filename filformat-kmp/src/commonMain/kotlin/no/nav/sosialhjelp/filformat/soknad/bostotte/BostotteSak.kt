package no.nav.sosialhjelp.filformat.soknad.bostotte

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.KildeSystem

/**
 * Saker som bruker har
 */
@Serializable
public data class BostotteSak(
  public val kilde: KildeSystem,
  /**
   * Kodeverdi som angir hva slags type sak det er. Dette feltet kan for eksempel brukes til å filtrere bort saker man ikke ønsker å vise til saksbehandler. Det er et MÅ-krav for konsumenter å dynamisk støtte nye typer. Eksempler: "husbanken" og "annet".
   */
  public val type: String,
  /**
   * Dato som denne saken er registrert på.
   */
  public val dato: String,
  /**
   * Saksstatus. Eksempler: "UNDER_BEHANDLING", "VEDTATT"
   */
  public val status: String,
  /**
   * En tilleggsbeskrivelse til status. Kan være tom.
   */
  public val beskrivelse: String? = null,
  /**
   * Vedtaksstatus. Eksempler: "INNVILGET", "AVSLAG", "AVVIST".
   */
  public val vedtaksstatus: Vedtaksstatus? = null,
) {
  @Serializable(with = VedtaksstatusSerializer::class)
  public enum class Vedtaksstatus(
    public val jsonValue: String,
  ) {
    INNVILGET("INNVILGET"),
    AVSLAG("AVSLAG"),
    AVVIST("AVVIST"),
    UKJENT("UKJENT"),
    ;
  }

  public object VedtaksstatusSerializer : UkjentTolerantEnumSerializer<Vedtaksstatus>("BostotteSak.Vedtaksstatus", Vedtaksstatus.entries.toTypedArray(), Vedtaksstatus.UKJENT, Vedtaksstatus::jsonValue)
}
