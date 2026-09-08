package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * En sak, i tilknytning til søknaden, har blitt opprettet og har status
 *
 * Status på sak som vil resultere i et vedtak.
 */
@Serializable
public data class SaksStatus(
  /**
   * En referanse slik at et vedtak kan tilknyttes på et senere tidspunkt
   */
  public val referanse: String,
  override val hendelsestidspunkt: String,
  /**
   * Tittel på saken, hva saken gjelder
   */
  public val tittel: String? = null,
  /**
   * Det kan legges til nye statustyper i enumen, men ingen gamle kan fjernes fra valideringsskjemaet (grunnet bakoverkompatibilitet). Saker uten innsyn blir behandlet, men søkeren får ikke innsyn i saken (feks. når saken ikke gjelder økonomisk sosialhjelp), Ved feilregistrering vil ikke saken vises.
   */
  public val status: Status? = null,
  override val type: String = "saksStatus",
) : Hendelse {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    UNDER_BEHANDLING("UNDER_BEHANDLING"),
    IKKE_INNSYN("IKKE_INNSYN"),
    BEHANDLES_IKKE("BEHANDLES_IKKE"),
    FEILREGISTRERT("FEILREGISTRERT"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("SaksStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
