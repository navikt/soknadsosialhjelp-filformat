package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable(with = UkjentFilreferanseSerializer::class)
public data class UkjentFilreferanse(
  override val type: String,
  public val raw: JsonObject,
) : Filreferanse
