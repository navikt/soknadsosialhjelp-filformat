package no.nav.sosialhjelp.filformat.soknad.arbeid

import kotlin.Boolean
import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

@Serializable
public data class Arbeidsforhold(
  public val kilde: Kilde,
  /**
   * Navn på arbeidsgiver. Kan være navn på privatperson ved forenklet oppgjørsordning.
   *
   * Navnet kan være blankt.
   */
  public val arbeidsgivernavn: String,
  public val fom: String,
  public val stillingsprosent: Int,
  /**
   * Brukes når en søker overstyrer/endrer på et arbeidsforhold. Settes kun til "true" på arbeidsforhold med systemkilde. Anbefaler at man likevel viser dataene fra AA-registeret til saksbehandler men markert som overskrevet av bruker (for eksempel å vise med overstrykning).
   */
  public val overstyrtAvBruker: Boolean,
  public val tom: String? = null,
  /**
   * Feltet er utdatert.
   */
  public val stillingstype: Stillingstype? = null,
) {
  @Serializable(with = StillingstypeSerializer::class)
  public enum class Stillingstype(
    public val jsonValue: String,
  ) {
    VARIABEL("variabel"),
    FAST("fast"),
    FAST_OG_VARIABEL("fastOgVariabel"),
    UKJENT("UKJENT"),
    ;
  }

  public object StillingstypeSerializer : UkjentTolerantEnumSerializer<Stillingstype>("Arbeidsforhold.Stillingstype", Stillingstype.entries.toTypedArray(), Stillingstype.UKJENT, Stillingstype::jsonValue)
}
