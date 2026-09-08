package no.nav.sosialhjelp.filformat.soknad.familie

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

@Serializable
public data class Barnebidrag(
  public val kilde: KildeBruker? = null,
  public val verdi: Verdi? = null,
) {
  @Serializable(with = VerdiSerializer::class)
  public enum class Verdi(
    public val jsonValue: String,
  ) {
    BETALER("betaler"),
    MOTTAR("mottar"),
    BEGGE("begge"),
    INGEN("ingen"),
    UKJENT("UKJENT"),
    ;
  }

  public object VerdiSerializer : UkjentTolerantEnumSerializer<Verdi>("Barnebidrag.Verdi", Verdi.entries.toTypedArray(), Verdi.UKJENT, Verdi::jsonValue)
}
