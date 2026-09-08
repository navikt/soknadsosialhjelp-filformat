package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

/**
 * Vilkar
 *
 * Vilkar
 */
@Serializable
public data class Vilkar(
  /**
   * referansen til vilkåret
   */
  public val vilkarreferanse: String,
  override val hendelsestidspunkt: String,
  /**
   * En referanse til saken vilkåret gjelder for. Denne er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.
   */
  public val saksreferanse: String? = null,
  /**
   * Hvilke utbetalinger vilkåret er knyttet til
   */
  public val utbetalingsreferanse: List<String>? = null,
  /**
   * Hva vilkåret gjelder. Er påkrevd, men blir lagt til som optional for bakover-kompatibilitet.
   */
  public val tittel: String? = null,
  /**
   * En eventuelt mer detaljert beskrivelse rundt hva vilkåret omhandler. Denne teksten kan også inneholde feks. periode og hvordan en søker kan oppfylle vilkåret.
   */
  public val beskrivelse: String? = null,
  /**
   * Status som forteller om vilkåret er relevant eller ikke. 
   * * RELEVANT - benyttes for alle vilkår som er relevante for saken. 
   * * ANNULLERT - benyttes dersom et vilkår er feilregistrert, skal fjernes av tekniske årsaker eller er erstattet av nytt vedtak. Annullerte vilkår vil ikke vises, og benyttes i tilfeller der et vilkår skal slettes fra nav.no på grunn av en feilsituasjon. 
   * * OPPFYLT og IKKE_OPPFYLT  - er deprecated men fjernes ikke, for bakover-kompatibilitet. De blir tolket som RELEVANT.
   */
  public val status: Status? = null,
  override val type: String = "vilkar",
) : Hendelse {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    RELEVANT("RELEVANT"),
    ANNULLERT("ANNULLERT"),
    OPPFYLT("OPPFYLT"),
    IKKE_OPPFYLT("IKKE_OPPFYLT"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("Vilkar.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
