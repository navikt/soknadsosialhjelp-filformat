package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

public object UkjentHendelseSerializer : KSerializer<UkjentHendelse> {
  override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor("no.nav.sosialhjelp.filformat.digisos.soker.UkjentHendelse")

  override fun deserialize(decoder: Decoder): UkjentHendelse {
    val obj = (decoder as JsonDecoder).decodeJsonElement().jsonObject
    val type = obj["type"]?.jsonPrimitive?.contentOrNull ?: throw SerializationException("UkjentHendelse mangler pakrevd felt 'type'")
    val hendelsestidspunkt = obj["hendelsestidspunkt"]?.jsonPrimitive?.contentOrNull ?: throw SerializationException("UkjentHendelse mangler pakrevd felt 'hendelsestidspunkt'")
    return UkjentHendelse(type = type, hendelsestidspunkt = hendelsestidspunkt, raw = obj)
  }

  override fun serialize(encoder: Encoder, `value`: UkjentHendelse) {
    (encoder as JsonEncoder).encodeJsonElement(value.raw)
  }
}
