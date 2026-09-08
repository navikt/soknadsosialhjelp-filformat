package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlin.collections.List
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.digisos.soker.hendelse.Vedtaksfil

/**
 * Et vedtak, i tilknytning til søknaden, har blitt fattet.
 *
 * Det er kun vedtak som ligger inn under Sosialtjenesteloven som skal sendes.
 */
@Serializable
public data class VedtakFattet(
  /**
   * Referanse vedtaket skal tilknyttes til
   */
  public val saksreferanse: String,
  /**
   * Vedtaksfil
   *
   * Vedtaksfilen som søker skal ha mulighet til å se. Det er ingen garanti for at filen blir vist til søker. Filformatet skal være PDF.
   */
  public val vedtaksfil: Vedtaksfil,
  override val hendelsestidspunkt: String,
  /**
   * Utfallet i vedtaket
   */
  public val utfall: Utfall? = null,
  /**
   * Vedlegg til vedtaksfil
   *
   * En liste med vedlegg til vedtaksfilen som søker skal ha mulighet til å se. Det er ingen garanti for at filene blir vist til søker. Filformatet skal være PDF.
   */
  public val vedlegg: List<Vedlegg>? = null,
  override val type: String = "vedtakFattet",
) : Hendelse {
  @Serializable(with = UtfallSerializer::class)
  public enum class Utfall(
    public val jsonValue: String,
  ) {
    INNVILGET("INNVILGET"),
    DELVIS_INNVILGET("DELVIS_INNVILGET"),
    AVSLATT("AVSLATT"),
    AVVIST("AVVIST"),
    UKJENT("UKJENT"),
    ;
  }

  public object UtfallSerializer : UkjentTolerantEnumSerializer<Utfall>("VedtakFattet.Utfall", Utfall.entries.toTypedArray(), Utfall.UKJENT, Utfall::jsonValue)
}
