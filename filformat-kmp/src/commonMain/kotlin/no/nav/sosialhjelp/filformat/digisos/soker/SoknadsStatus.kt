package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Ny status på søknaden.
 */
@Serializable
public data class SoknadsStatus(
  /**
   * Det kan legges til nye statustyper i enumen, men ingen gamle kan fjernes fra valideringsskjemaet (grunnet kompatibilitet).
   */
  public val status: Status,
  override val hendelsestidspunkt: String,
  override val type: String = "soknadsStatus",
) : Hendelse {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    MOTTATT("MOTTATT"),
    UNDER_BEHANDLING("UNDER_BEHANDLING"),
    FERDIGBEHANDLET("FERDIGBEHANDLET"),
    BEHANDLES_IKKE("BEHANDLES_IKKE"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("SoknadsStatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
