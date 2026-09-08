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

public object UkjentFilreferanseSerializer : KSerializer<UkjentFilreferanse> {
  override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor("no.nav.sosialhjelp.filformat.digisos.soker.UkjentFilreferanse")

  override fun deserialize(decoder: Decoder): UkjentFilreferanse {
    val obj = (decoder as JsonDecoder).decodeJsonElement().jsonObject
    val type = obj["type"]?.jsonPrimitive?.contentOrNull ?: throw SerializationException("UkjentFilreferanse mangler pakrevd felt 'type'")
    return UkjentFilreferanse(type = type, raw = obj)
  }

  override fun serialize(encoder: Encoder, `value`: UkjentFilreferanse) {
    (encoder as JsonEncoder).encodeJsonElement(value.raw)
  }
}
