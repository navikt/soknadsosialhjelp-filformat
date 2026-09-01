package no.nav.sosialhjelp.filformat

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Serializer base for enums that must tolerate values we have never seen before.
 *
 * Every enum in the digisos/soker schema is explicitly documented as open-ended, e.g.
 * `soknadsStatus.json`:
 *
 * > "Det kan legges til nye statustyper i enumen, men ingen gamle kan fjernes fra
 * >  valideringsskjemaet (grunnet kompatibilitet)."
 *
 * A municipality can therefore start emitting a status this library does not know about.
 * A strict enum would throw, and since these values sit inside a hendelse stream, a single
 * unknown status would take down an entire case view. Unknown values decode to `UKJENT`
 * instead, which callers can branch on explicitly.
 *
 * DO NOT "clean up" the `UKJENT` members by matching them to the JSON schema's enum list.
 * They are deliberately not in the schema.
 *
 * Known limitation: `UKJENT` does not round-trip. Serializing a value that was decoded from
 * an unrecognised string writes `"UKJENT"`, not the original. This library is an input model
 * for folding a hendelse stream; if a caller ever needs to re-emit untouched hendelser, the
 * enums must be replaced by a data-carrying type. The parity test only asserts round-trip
 * fidelity over fixtures whose enum values are all known.
 */
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
