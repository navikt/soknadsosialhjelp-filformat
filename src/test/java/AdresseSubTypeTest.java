import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import no.nav.sbl.soknadsosialhjelp.json.AdresseMixIn;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonGateAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde;

public class AdresseSubTypeTest {

    @Test
    public void subtypeSkalBenyttesVedLesing() throws Exception {
        final File testfile = new File("src/test/resources/json/soknad/parts/adresse/fullstendig-gateadresse.json");
        
        final ObjectMapper mapper = new ObjectMapper();
        mapper.addMixIn(JsonAdresse.class, AdresseMixIn.class);
        
        final JsonAdresse jsonAdresse = mapper.readValue(testfile, JsonAdresse.class);
        assertEquals("Skal lese felt som kun finnes på superklasse", JsonKilde.BRUKER, jsonAdresse.getKilde());
        assertEquals("Skal lese delt felt", JsonAdresse.Type.GATEADRESSE, jsonAdresse.getType());
        
        assertEquals("Riktig subklasse skal velges", JsonGateAdresse.class, jsonAdresse.getClass());
        final JsonGateAdresse gateadresse = (JsonGateAdresse) jsonAdresse; 
        assertEquals("Skal lese felt som kun finnes på subklasse", "Testeveien", gateadresse.getGatenavn());
    }
}
