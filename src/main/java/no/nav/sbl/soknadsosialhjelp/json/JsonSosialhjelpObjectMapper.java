package no.nav.sbl.soknadsosialhjelp.json;

import tools.jackson.databind.json.JsonMapper;

public final class JsonSosialhjelpObjectMapper {

    private JsonSosialhjelpObjectMapper() {
    }

    public static JsonMapper createObjectMapper() {
        return createJsonMapperBuilder().build();
    }

    public static JsonMapper.Builder createJsonMapperBuilder() {
        return JsonMapper.builder();
    }
}
