import com.github.fge.jsonschema.core.report.ProcessingReport;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;

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
        assertThat(report.isSuccess()).describedAs(message).isEqualTo(forventGyldig);

        if (forventGyldig) {
            assertThat(!JsonSosialhjelpValidator.hasWarnings(report)).describedAs("Det er warnings for fil " + testfile.getName() + "\n" + report).isTrue();
        }
    }
}
