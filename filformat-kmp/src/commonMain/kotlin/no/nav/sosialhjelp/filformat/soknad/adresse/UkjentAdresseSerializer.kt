package no.nav.sosialhjelp.filformat.soknad.adresse

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import no.nav.sosialhjelp.filformat.soknad.common.Kilde

public object UkjentAdresseSerializer : KSerializer<UkjentAdresse> {
  override val descriptor: SerialDescriptor =
      buildClassSerialDescriptor("no.nav.sosialhjelp.filformat.soknad.adresse.UkjentAdresse")

  override fun deserialize(decoder: Decoder): UkjentAdresse {
    val obj = (decoder as JsonDecoder).decodeJsonElement().jsonObject
    val kilde = (decoder as JsonDecoder).json.decodeFromJsonElement<Kilde>(obj.getValue("kilde"))
    val type = obj["type"]?.jsonPrimitive?.contentOrNull ?: throw SerializationException("UkjentAdresse mangler pakrevd felt 'type'")
    val adresseValg = obj["adresseValg"]?.let { (decoder as JsonDecoder).json.decodeFromJsonElement<AdresseValg>(it) }
    return UkjentAdresse(kilde = kilde, type = type, adresseValg = adresseValg, raw = obj)
  }

  override fun serialize(encoder: Encoder, `value`: UkjentAdresse) {
    (encoder as JsonEncoder).encodeJsonElement(value.raw)
  }
}
