package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Dokumentasjonkrav
 *
 * Dokumentasjonkrav
 */
@Serializable
public data class Dokumentasjonkrav(
  /**
   * referansen til dokumentasjonkravet
   */
  public val dokumentasjonkravreferanse: String,
  override val hendelsestidspunkt: String,
  /**
   * En referanse til saken dokumentasjonkravet gjelder for. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.
   */
  public val saksreferanse: String? = null,
  /**
   * Hvilke utbetalinger dokumentasjonkravet er knyttet til
   */
  public val utbetalingsreferanse: List<String>? = null,
  /**
   * Hva dokumentasjonkravet gjelder. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.
   */
  public val tittel: String? = null,
  /**
   * En eventuelt mer detaljert beskrivelse rundt hva dokumentasjonkravet omhandler. Denne teksten kan også inneholde feks. periode og hvordan en søker kan oppfylle kravet.
   */
  public val beskrivelse: String? = null,
  public val frist: String? = null,
  /**
   * Status som forteller om dokumentasjonkravet er relevant eller ikke. 
   * * RELEVANT - benyttes for alle dokumentasjonkrav som er relevante for saken. 
   * * LEVERT_TIDLIGERE - benyttes dersom dokumentasjonkravet er levert via andre kanaler, eller er oppfylt av et annet dokument søker har sendt inn. 
   * * ANNULLERT - benyttes dersom et dokumentasjonkrav er feilregistrert, skal fjernes av tekniske årsaker eller er erstattet av nytt vedtak. Annullerte dokumentasjonkrav vil ikke vises, og benyttes i tilfeller der et dokumentasjonkrav skal slettes fra nav.no på grunn av en feilsituasjon. 
   * * OPPFYLT og IKKE_OPPFYLT - er deprecated men fjernes ikke, for bakover-kompatibilitet. De blir tolket som RELEVANT.
   */
  public val status: Status? = null,
  override val type: String = "dokumentasjonkrav",
) : Hendelse {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    RELEVANT("RELEVANT"),
    LEVERT_TIDLIGERE("LEVERT_TIDLIGERE"),
    ANNULLERT("ANNULLERT"),
    OPPFYLT("OPPFYLT"),
    IKKE_OPPFYLT("IKKE_OPPFYLT"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("Dokumentasjonkrav.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
