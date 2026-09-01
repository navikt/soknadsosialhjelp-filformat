package no.nav.sosialhjelp.filformat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// Decodes unknown enum values to `ukjent` instead of throwing. UKJENT is intentional — do not remove it.
public abstract class UkjentTolerantEnumSerializer<T : Enum<T>>(
    serialName: String,
    entries: Array<T>,
    private val ukjent: T,
    private val jsonValue: (T) -> String,
) : KSerializer<T> {

    private val byJsonValue: Map<String, T> = entries.associateBy(jsonValue)

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName, PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): T =
        byJsonValue[decoder.decodeString()] ?: ukjent

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(jsonValue(value))
    }
}
