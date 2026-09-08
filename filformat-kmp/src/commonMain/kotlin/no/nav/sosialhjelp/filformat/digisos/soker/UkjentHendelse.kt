package no.nav.sosialhjelp.filformat.digisos.soker

import kotlin.String
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable(with = UkjentHendelseSerializer::class)
public data class UkjentHendelse(
  override val type: String,
  override val hendelsestidspunkt: String,
  public val raw: JsonObject,
) : Hendelse
