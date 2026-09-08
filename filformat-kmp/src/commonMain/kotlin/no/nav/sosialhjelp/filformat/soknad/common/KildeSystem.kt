package no.nav.sosialhjelp.filformat.soknad.common

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable(with = KildeSystemSerializer::class)
public enum class KildeSystem(
  public val jsonValue: String,
) {
  SYSTEM("system"),
  UTDATERT("utdatert"),
  UKJENT("UKJENT"),
  ;
}

public object KildeSystemSerializer : UkjentTolerantEnumSerializer<KildeSystem>("KildeSystem", KildeSystem.entries.toTypedArray(), KildeSystem.UKJENT, KildeSystem::jsonValue)
