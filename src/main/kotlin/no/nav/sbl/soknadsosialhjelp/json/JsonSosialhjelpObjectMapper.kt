package no.nav.sbl.soknadsosialhjelp.json

import tools.jackson.databind.json.JsonMapper

object JsonSosialhjelpObjectMapper {
    @JvmStatic fun createObjectMapper(): JsonMapper = createJsonMapperBuilder().build()
    @JvmStatic fun createJsonMapperBuilder(): JsonMapper.Builder = JsonMapper.builder()
}
