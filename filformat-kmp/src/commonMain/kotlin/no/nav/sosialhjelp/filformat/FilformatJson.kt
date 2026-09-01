package no.nav.sosialhjelp.filformat

import kotlinx.serialization.json.Json

/**
 * The [Json] instance this model is designed for. Using a differently-configured [Json]
 * will change behaviour in ways the parity test does not cover.
 *
 * - `ignoreUnknownKeys`: the schemas explicitly permit new fields being added over time
 *   (see the `kan-ha-nye-felter.json` fixture). Note that unlike the Java model, which is
 *   generated with `includeAdditionalProperties = true` and retains unknown fields, this
 *   model discards them.
 * - `explicitNulls = false`: absent optional fields must stay absent on re-serialization
 *   rather than becoming explicit `null`s.
 * - `encodeDefaults = true`: required so the `type` discriminator, which is modelled as a
 *   constructor property with a default, is actually written. Every optional field is
 *   therefore declared nullable with a `null` default, so this does not cause spurious
 *   output.
 */
public val filformatJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    encodeDefaults = true
}
