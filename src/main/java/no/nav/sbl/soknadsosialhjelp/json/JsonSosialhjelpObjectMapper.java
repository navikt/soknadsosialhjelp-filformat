package no.nav.sbl.soknadsosialhjelp.json;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.sbl.soknadsosialhjelp.innsyn.soker.JsonFilreferanse;
import no.nav.sbl.soknadsosialhjelp.innsyn.soker.JsonHendelse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse;

public final class JsonSosialhjelpObjectMapper {

    private JsonSosialhjelpObjectMapper() {}
    
    public static ObjectMapper createObjectMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(JsonAdresse.class, AdresseMixIn.class);
        mapper.addMixIn(JsonHendelse.class, HendelseMixIn.class);
        mapper.addMixIn(JsonFilreferanse.class, FilreferanseMixIn.class);
        return mapper;
    }
}
