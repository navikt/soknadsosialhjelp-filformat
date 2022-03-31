package no.nav.sbl.soknadsosialhjelp.digisos.soker;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus.Status;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

public class HendelseSubTypeTest {

    @Test
    public void subtypeSkalBenyttesVedLesing() throws Exception {
        final File testfile = new File("src/test/resources/json/digisos/soker/parts/hendelse/minimal.json");

        final ObjectMapper mapper = JsonSosialhjelpObjectMapper.createObjectMapper();

        final JsonHendelse jsonHendelse = mapper.readValue(testfile, JsonHendelse.class);
        assertThat(jsonHendelse.getHendelsestidspunkt()).describedAs("Skal lese felt som kun finnes på superklasse").isEqualTo("2018-10-04T13:37:00.134Z");
        assertThat(jsonHendelse.getType()).describedAs("Skal lese delt felt").isEqualTo(JsonHendelse.Type.SOKNADS_STATUS);


        assertThat(jsonHendelse.getClass()).describedAs("Riktig subklasse skal velges").isEqualTo(JsonSoknadsStatus.class);
        final JsonSoknadsStatus soknadsStatus = (JsonSoknadsStatus) jsonHendelse;
        assertThat(soknadsStatus.getStatus()).describedAs("Skal lese felt som kun finnes på subklasse").isEqualTo(Status.MOTTATT);
    }
}
