package no.nav.sbl.soknadsosialhjelp.digisos.soker;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.sbl.soknadsosialhjelp.digisos.soker.JsonHendelse;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus.Status;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper;

public class HendelseSubTypeTest {

    @Test
    public void subtypeSkalBenyttesVedLesing() throws Exception {
        final File testfile = new File("src/test/resources/json/digisos/soker/parts/hendelse/minimal.json");
        
        final ObjectMapper mapper = JsonSosialhjelpObjectMapper.createObjectMapper();

        final JsonHendelse jsonHendelse = mapper.readValue(testfile, JsonHendelse.class);
        assertEquals("Skal lese felt som kun finnes på superklasse", "2018-10-04T13:37:00.134Z", jsonHendelse.getHendelsestidspunkt());
        assertEquals("Skal lese delt felt", JsonHendelse.Type.SOKNADS_STATUS, jsonHendelse.getType());
        
        assertEquals("Riktig subklasse skal velges", JsonSoknadsStatus.class, jsonHendelse.getClass());
        final JsonSoknadsStatus soknadsStatus = (JsonSoknadsStatus) jsonHendelse;
        assertEquals("Skal lese felt som kun finnes på subklasse", Status.MOTTATT, soknadsStatus.getStatus());
    }
}
