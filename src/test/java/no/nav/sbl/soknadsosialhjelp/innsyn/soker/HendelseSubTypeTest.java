package no.nav.sbl.soknadsosialhjelp.innsyn.soker;
import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.sbl.soknadsosialhjelp.innsyn.soker.hendelse.JsonNyStatus;
import no.nav.sbl.soknadsosialhjelp.innsyn.soker.hendelse.JsonNyStatus.Status;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper;

public class HendelseSubTypeTest {

    @Test
    public void subtypeSkalBenyttesVedLesing() throws Exception {
        final File testfile = new File("src/test/resources/json/innsyn/soker/parts/hendelse/minimal.json");
        
        final ObjectMapper mapper = JsonSosialhjelpObjectMapper.createObjectMapper();

        final JsonHendelse jsonHendelse = mapper.readValue(testfile, JsonHendelse.class);
        assertEquals("Skal lese felt som kun finnes på superklasse", "2018-10-04T13:37:00.134Z", jsonHendelse.getHendelsestidspunkt());
        assertEquals("Skal lese delt felt", JsonHendelse.Type.NY_STATUS, jsonHendelse.getType());
        
        assertEquals("Riktig subklasse skal velges", JsonNyStatus.class, jsonHendelse.getClass());
        final JsonNyStatus nyStatus = (JsonNyStatus) jsonHendelse; 
        assertEquals("Skal lese felt som kun finnes på subklasse", Status.MOTTATT, nyStatus.getStatus());
    }
}
