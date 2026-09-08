package no.nav.sosialhjelp.filformat.soknad.common

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable(with = KildeSerializer::class)
public enum class Kilde(
  public val jsonValue: String,
) {
  BRUKER("bruker"),
  SYSTEM("system"),
  UTDATERT("utdatert"),
  UKJENT("UKJENT"),
  ;
}

public object KildeSerializer : UkjentTolerantEnumSerializer<Kilde>("Kilde", Kilde.entries.toTypedArray(), Kilde.UKJENT, Kilde::jsonValue)
