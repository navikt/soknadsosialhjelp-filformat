package no.nav.sbl.soknadsosialhjelp.json;

import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonFilreferanse;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.json.JsonMapper;

public final class JsonSosialhjelpObjectMapper {

    private JsonSosialhjelpObjectMapper() {
    }

    public static ObjectMapper createObjectMapper() {
        return JsonMapper.builder()
                .addMixIn(JsonAdresse.class, AdresseMixIn.class)
                .addMixIn(JsonHendelse.class, HendelseMixIn.class)
                .addMixIn(JsonFilreferanse.class, FilreferanseMixIn.class)
                .build();
    }
}
