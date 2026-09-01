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

@Serializable(with = HendelseSerializer::class)
public sealed interface Hendelse {
    public val type: String
    public val hendelsestidspunkt: String
}

// Retains raw payload so unknown hendelser round-trip without loss
@Serializable(with = UkjentHendelseSerializer::class)
public data class UkjentHendelse(
    override val type: String,
    override val hendelsestidspunkt: String,
    public val raw: JsonObject,
) : Hendelse

// JsonContentPolymorphicSerializer does not inject a class discriminator on serialization,
// so every subclass must declare `type` as a real serialized property.
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
        // Unknown type is tolerated; missing required fields are not
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
