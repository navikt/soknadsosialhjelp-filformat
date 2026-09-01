package no.nav.sosialhjelp.filformat

import kotlinx.serialization.json.Json

// ignoreUnknownKeys: schema allows new fields; explicitNulls=false: absent fields stay absent; encodeDefaults=true: writes the `type` discriminator
public val filformatJson: Json = Json {
    ignoreUnknownKeys = true
    explicitNulls = false
    encodeDefaults = true
}
