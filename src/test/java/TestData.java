import static org.junit.Assert.assertEquals;

import java.io.File;

import com.github.fge.jsonschema.core.report.ProcessingReport;

import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;

public final class TestData {

    private final File testfile; 
    private final boolean forventGyldig;
    private final String schemaUri;

    public TestData(File testfile, String schemaUri, boolean forventGyldig) {
        this.testfile = testfile;
        this.schemaUri = schemaUri;
        this.forventGyldig = forventGyldig;
    }
    
    
    public void valider() {
        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);

        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertEquals(message, forventGyldig, report.isSuccess());
        
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !JsonSosialhjelpValidator.hasWarnings(report));
        }
    }
}
