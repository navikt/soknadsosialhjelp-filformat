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
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

/**
 * `json/digisos/soker/parts/hendelse.json`.
 *
 * Discriminated on `type`, matching `HendelseMixIn` in the Java model
 * (`As.EXISTING_PROPERTY, property = "type"`).
 */
@Serializable(with = HendelseSerializer::class)
public sealed interface Hendelse {
    public val type: String
    public val hendelsestidspunkt: String
}

/**
 * Fallback for a hendelse `type` this library does not know about.
 *
 * The schema states that new hendelse types may be added at any time. Both consuming
 * applications currently throw `RuntimeException("Hendelsetype ... mangler mapping")`, which
 * means a single new municipal hendelse type takes down an entire case view. Decoding to
 * [UkjentHendelse] instead lets a fold skip what it does not understand and keep going.
 *
 * The original payload is retained in [raw] so the value round-trips byte-for-byte. Without
 * that, re-serializing would emit whatever discriminator this class declares rather than the
 * `type` that was actually read.
 */
@Serializable(with = UkjentHendelseSerializer::class)
public data class UkjentHendelse(
    override val type: String,
    override val hendelsestidspunkt: String,
    /** The untouched input object. */
    public val raw: JsonObject,
) : Hendelse

/**
 * See the note on [FilreferanseSerializer]: because this is a
 * [JsonContentPolymorphicSerializer], no class discriminator is injected on serialization,
 * so every subclass declares `type` as a real serialized property.
 */
public object HendelseSerializer : JsonContentPolymorphicSerializer<Hendelse>(Hendelse::class) {

    override fun selectDeserializer(element: JsonElement): KSerializer<out Hendelse> =
        when (element.jsonObject["type"]?.jsonPrimitive?.contentOrNull) {
            "tildeltNavKontor" -> TildeltNavKontor.serializer()
            "soknadsStatus" -> SoknadsStatus.serializer()
            "vedtakFattet" -> VedtakFattet.serializer()
            "dokumentasjonEtterspurt" -> DokumentasjonEtterspurt.serializer()
            "forelopigSvar" -> ForelopigSvar.serializer()
            "saksStatus" -> SaksStatus.serializer()
            "utbetaling" -> Utbetaling.serializer()
            "vilkar" -> Vilkar.serializer()
            "dokumentasjonkrav" -> Dokumentasjonkrav.serializer()
            "rammevedtak" -> Rammevedtak.serializer()
            else -> UkjentHendelse.serializer()
        }
}

public object UkjentHendelseSerializer : KSerializer<UkjentHendelse> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("no.nav.sosialhjelp.filformat.digisos.soker.UkjentHendelse")

    override fun deserialize(decoder: Decoder): UkjentHendelse {
        val obj = (decoder as JsonDecoder).decodeJsonElement().jsonObject
        // `type` and `hendelsestidspunkt` are required by parts/hendelse.json for EVERY
        // hendelse, known or not. Tolerating an unknown `type` is deliberate; tolerating a
        // structurally invalid hendelse is not, so these still throw.
        val type = obj["type"]?.jsonPrimitive?.contentOrNull
            ?: throw SerializationException("Hendelse mangler pakrevd felt 'type'")
        val hendelsestidspunkt = obj["hendelsestidspunkt"]?.jsonPrimitive?.contentOrNull
            ?: throw SerializationException(
                "Hendelse av type '$type' mangler pakrevd felt 'hendelsestidspunkt'",
            )
        return UkjentHendelse(type = type, hendelsestidspunkt = hendelsestidspunkt, raw = obj)
    }

    override fun serialize(encoder: Encoder, value: UkjentHendelse) {
        (encoder as JsonEncoder).encodeJsonElement(value.raw)
    }
}
