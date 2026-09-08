package no.nav.sosialhjelp.filformat.soknad

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.arbeid.Arbeid
import no.nav.sosialhjelp.filformat.soknad.begrunnelse.Begrunnelse
import no.nav.sosialhjelp.filformat.soknad.bosituasjon.Bosituasjon
import no.nav.sosialhjelp.filformat.soknad.familie.Familie
import no.nav.sosialhjelp.filformat.soknad.okonomi.Okonomi
import no.nav.sosialhjelp.filformat.soknad.personalia.Personalia
import no.nav.sosialhjelp.filformat.soknad.situasjonendring.Situasjonendring
import no.nav.sosialhjelp.filformat.soknad.utdanning.Utdanning

/**
 * Inneholder søknadsdataene uten meta- og kompatibilitetsdata.
 */
@Serializable
public data class Data(
  /**
   * Personalia
   */
  public val personalia: Personalia,
  /**
   * Begrunnelse
   */
  public val begrunnelse: Begrunnelse,
  /**
   * Økonomiske data.
   */
  public val okonomi: Okonomi,
  /**
   * Angir hvilken type søknad det er. PT er det to typer: "kort" og "standard".
   */
  public val soknadstype: Soknadstype? = null,
  /**
   * Arbeid
   */
  public val arbeid: Arbeid? = null,
  /**
   * Utdanning
   */
  public val utdanning: Utdanning? = null,
  /**
   * Familie
   */
  public val familie: Familie? = null,
  /**
   * Endring i situasjon
   */
  public val situasjonendring: Situasjonendring? = null,
  /**
   * Bosituasjon
   */
  public val bosituasjon: Bosituasjon? = null,
) {
  @Serializable(with = SoknadstypeSerializer::class)
  public enum class Soknadstype(
    public val jsonValue: String,
  ) {
    KORT("kort"),
    STANDARD("standard"),
    UKJENT("UKJENT"),
    ;
  }

  public object SoknadstypeSerializer : UkjentTolerantEnumSerializer<Soknadstype>("Data.Soknadstype", Soknadstype.entries.toTypedArray(), Soknadstype.UKJENT, Soknadstype::jsonValue)
}
