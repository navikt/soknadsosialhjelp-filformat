package no.nav.sosialhjelp.filformat.soknad.bosituasjon

import kotlin.Int
import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer
import no.nav.sosialhjelp.filformat.soknad.common.KildeBruker

/**
 * Bosituasjon
 */
@Serializable
public data class Bosituasjon(
  public val kilde: KildeBruker? = null,
  public val botype: Botype? = null,
  public val antallPersoner: Int? = null,
) {
  @Serializable(with = BotypeSerializer::class)
  public enum class Botype(
    public val jsonValue: String,
  ) {
    EIER("eier"),
    LEIER("leier"),
    KOMMUNAL("kommunal"),
    INGEN("ingen"),
    INSTITUSJON("institusjon"),
    KRISESENTER("krisesenter"),
    FENGSEL("fengsel"),
    VENNER("venner"),
    FORELDRE("foreldre"),
    FAMILIE("familie"),
    ANNET("annet"),
    UKJENT("UKJENT"),
    ;
  }

  public object BotypeSerializer : UkjentTolerantEnumSerializer<Botype>("Bosituasjon.Botype", Botype.entries.toTypedArray(), Botype.UKJENT, Botype::jsonValue)
}
