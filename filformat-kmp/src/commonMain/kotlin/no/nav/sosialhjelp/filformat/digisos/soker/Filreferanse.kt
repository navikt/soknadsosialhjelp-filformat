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

/**
 * `json/digisos/soker/parts/filreferanse.json`.
 *
 * Discriminated on `type`, matching `FilreferanseMixIn` in the Java model.
 */
@Serializable(with = FilreferanseSerializer::class)
public sealed interface Filreferanse {
    public val type: String
}

/**
 * Fallback for a `type` value this library does not know about. See [UkjentHendelse] for
 * the rationale; the same argument applies here, one level down.
 */
@Serializable(with = UkjentFilreferanseSerializer::class)
public data class UkjentFilreferanse(
    override val type: String,
    /** The untouched input object, so that this value round-trips without loss. */
    public val raw: JsonObject,
) : Filreferanse

/**
 * NB: this is a [JsonContentPolymorphicSerializer], not kotlinx's built-in sealed
 * polymorphism. That has a consequence worth stating explicitly, because it is the
 * opposite of what the usual kotlinx guidance says:
 *
 * `JsonContentPolymorphicSerializer.serialize` delegates straight to the concrete
 * subclass serializer and does NOT inject a class discriminator. So `type` MUST be
 * declared as a real serialized property on each subclass, otherwise it silently
 * disappears on serialization. The familiar "a sealed class cannot have a property named
 * `type`" error does not apply here, since we never use the built-in mechanism.
 */
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
        // `type` is required by parts/filreferanse.json. An unknown value is tolerated;
        // a missing one is not.
        val type = obj["type"]?.jsonPrimitive?.contentOrNull
            ?: throw SerializationException("Filreferanse mangler pakrevd felt 'type'")
        return UkjentFilreferanse(type = type, raw = obj)
    }

    override fun serialize(encoder: Encoder, value: UkjentFilreferanse) {
        (encoder as JsonEncoder).encodeJsonElement(value.raw)
    }
}
