package no.nav.sosialhjelp.filformat.digisos.soker

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Serializable(with = FilreferanseSerializer::class)
public sealed interface Filreferanse {
    public val type: String
}

@Serializable(with = UkjentFilreferanseSerializer::class)
public data class UkjentFilreferanse(
    override val type: String,
    public val raw: JsonObject,
) : Filreferanse

// JsonContentPolymorphicSerializer does not inject a class discriminator on serialization,
// so every subclass must declare `type` as a real serialized property.
public object FilreferanseSerializer :
    JsonContentPolymorphicSerializer<Filreferanse>(Filreferanse::class) {

    override fun selectDeserializer(element: kotlinx.serialization.json.JsonElement): KSerializer<out Filreferanse> =
        when (element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
            "dokumentlager" -> DokumentlagerFilreferanse.serializer()
            "svarut" -> SvarUtFilreferanse.serializer()
            else -> UkjentFilreferanse.serializer()
        }
}

public object UkjentFilreferanseSerializer : KSerializer<UkjentFilreferanse> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("no.nav.sosialhjelp.filformat.digisos.soker.UkjentFilreferanse")

    override fun deserialize(decoder: Decoder): UkjentFilreferanse {
        val obj = (decoder as JsonDecoder).decodeJsonElement().jsonObject
        // Unknown type is tolerated; missing type is not
        val type = obj["type"]?.jsonPrimitive?.contentOrNull
            ?: throw SerializationException("Filreferanse mangler pakrevd felt 'type'")
        return UkjentFilreferanse(type = type, raw = obj)
    }

    override fun serialize(encoder: Encoder, value: UkjentFilreferanse) {
        (encoder as JsonEncoder).encodeJsonElement(value.raw)
    }
}
