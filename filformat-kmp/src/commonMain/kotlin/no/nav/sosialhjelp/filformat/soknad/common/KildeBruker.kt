package no.nav.sosialhjelp.filformat.soknad.common

import kotlin.String
import kotlinx.serialization.Serializable
import no.nav.sosialhjelp.filformat.UkjentTolerantEnumSerializer

@Serializable(with = KildeBrukerSerializer::class)
public enum class KildeBruker(
  public val jsonValue: String,
) {
  BRUKER("bruker"),
  UTDATERT("utdatert"),
  UKJENT("UKJENT"),
  ;
}

public object KildeBrukerSerializer : UkjentTolerantEnumSerializer<KildeBruker>("KildeBruker", KildeBruker.entries.toTypedArray(), KildeBruker.UKJENT, KildeBruker::jsonValue)
