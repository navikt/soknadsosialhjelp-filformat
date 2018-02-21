import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;

import org.junit.Test;

import com.github.fge.jsonschema.core.report.ProcessingReport;

import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;

public class VedleggTest {

    @Test
    public void jsonValiderer() throws Exception {
        valider("gyldig_minimal", true);
        valider("gyldig_minimal2", true);
        valider("gyldig_standard", true);
        valider("gyldig_ekstrafelter", true);
        valider("ikkegyldig_ugyldigvedlegg", false);
        valider("ikkegyldig_ugyldigfil", false);
    }

    
    public void valider(String filnavn, boolean forventGyldig) {
        final File testfile = new File("src/test/resources/json/vedlegg/" + filnavn + ".json");
        final String schemaUri = Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toUri().toString();
        
        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);
        
        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertEquals(message, forventGyldig, report.isSuccess());
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !JsonSosialhjelpValidator.hasWarnings(report));
        }
    }
}
