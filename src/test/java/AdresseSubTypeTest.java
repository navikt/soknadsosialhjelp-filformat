import java.io.File;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonGateAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;


import static org.assertj.core.api.Assertions.assertThat;

class AdresseSubTypeTest {

    @Test
    void subtypeSkalBenyttesVedLesing() {
        final File testfile = new File("src/test/resources/json/soknad/parts/adresse/fullstendig-gateadresse.json");

        final ObjectMapper mapper = JsonSosialhjelpObjectMapper.createObjectMapper();

        final JsonAdresse jsonAdresse = mapper.readValue(testfile, JsonAdresse.class);
        assertThat(jsonAdresse.getKilde()).describedAs("Skal lese felt som kun finnes på superklasse").isEqualTo(JsonKilde.BRUKER);
        assertThat(jsonAdresse.getType()).describedAs("Skal lese delt felt").isEqualTo(JsonAdresse.Type.GATEADRESSE);

        assertThat(jsonAdresse).describedAs("Riktig subklasse skal velges").isInstanceOf(JsonGateAdresse.class);
        final JsonGateAdresse gateadresse = (JsonGateAdresse) jsonAdresse;
        assertThat(gateadresse.getGatenavn()).describedAs("Skal lese felt som kun finnes på subklasse").isEqualTo("Testeveien");
    }
}
