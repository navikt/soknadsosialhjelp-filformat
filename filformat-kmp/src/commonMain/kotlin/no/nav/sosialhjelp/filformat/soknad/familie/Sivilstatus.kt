package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.Boolean
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

/**
 * Sivilstatus og informasjon om eventuell ektefelle.
 *
 * Hvis "sivilstatus" mangler betyr dette at søker ikke har svart på spørsmålet.
 */
@Serializable
public data class Sivilstatus(
  public val kilde: Kilde,
  /**
   * Sivilstatus til søker.
   *
   * Sivilstatus til søker. Med "gift" menes både gift og registrert partner.
   */
  public val status: Status,
  /**
   * Søkers ektefelle
   */
  public val ektefelle: Ektefelle? = null,
  /**
   * Settes til true hvis ektefelle/registrert partner har diskresjonskode 6 eller 7. Kun relevant hvis "kilde" er "system".
   */
  public val ektefelleHarDiskresjonskode: Boolean? = null,
  /**
   * Kun relevant hvis "kilde" er "system".
   */
  public val folkeregistrertMedEktefelle: Boolean? = null,
  /**
   * Kun relevant hvis "kilde" er "bruker".
   */
  public val borSammenMed: Boolean? = null,
  /**
   * Brukerskrevet tekstlig forklaring (inkl. linjeskift). Kun relevant "kilde" er "bruker".
   */
  public val borIkkeSammenMedBegrunnelse: String? = null,
) {
  @Serializable(with = StatusSerializer::class)
  public enum class Status(
    public val jsonValue: String,
  ) {
    GIFT("gift"),
    UGIFT("ugift"),
    SAMBOER("samboer"),
    ENKE("enke"),
    SKILT("skilt"),
    SEPARERT("separert"),
    UKJENT("UKJENT"),
    ;
  }

  public object StatusSerializer : UkjentTolerantEnumSerializer<Status>("Sivilstatus.Status", Status.entries.toTypedArray(), Status.UKJENT, Status::jsonValue)
}
