import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.fge.jsonschema.core.report.ProcessingReport;

import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;

/**
 * Kjører skjemavalidering av alle json-filer i "src/test/resouces/json/soknad".
 * 
 * <ol>
 *     <li>Filer som starter på "feil-" skal feile validering mens alle andre filer skal passere.</li>
 *     <li>Filer i underkataloger blir kjørt mot delskjemaene med samme navn som underkatalogen testfilen ligger i.</li>
 * </ol>
 */
@RunWith(Parameterized.class)
public class SoknadTest {
    
    private static final String PREFIX_FIL_MED_FEIL = "feil-";
    private static final String SCHEMA_DIRECTORY = "json/soknad";
    private static final String TOPLEVEL_SCHEMA_FILE = SCHEMA_DIRECTORY + "/soknad.json";
    private static final String TEST_DATA_DIRECTORY = "src/test/resources/json/soknad/";

    private final File testfile; 
    private final boolean forventGyldig;
    private final String schemaUri;

    
    public SoknadTest(String testName, File testfile, String schemaUri, boolean forventGyldig) {
        this.testfile = testfile;
        this.schemaUri = schemaUri;
        this.forventGyldig = forventGyldig;
    }
    
    
    @Parameters(name="{0}")
    public static Iterable<Object[]> finnAlleTestdatafiler() {
        final List<File> testFiles = determineTestFiles(Paths.get(TEST_DATA_DIRECTORY).toFile());
        
        return testFiles.stream()
                .filter(ignoreTempFiles())
                .map(toTestParameters())
                .collect(Collectors.toList());
    }
    
    @Test
    public void jsonValiderer() {       
        valider(testfile, schemaUri, forventGyldig);
    }
    
    
    private static void valider(File testfile, String schemaUri, boolean forventGyldig) {
        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);

        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertEquals(message, forventGyldig, report.isSuccess());
        
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !JsonSosialhjelpValidator.hasWarnings(report));
        }
    }

    private static List<File> determineTestFiles(File parent) {
        final List<File> result = new ArrayList<>();
        if (parent.isDirectory()) {
            for (File child : parent.listFiles()) {
                result.addAll(determineTestFiles(child));
            }
        } else {
            result.add(parent);
        }
        return result;
    }

    private static Function<? super File, ? extends Object[]> toTestParameters() {
        return f -> {
            return new Object[] {
                f.getName(),
                f,
                determineSchemaUri(f.toPath()),
                !f.getName().startsWith(PREFIX_FIL_MED_FEIL)
            };
        };
    }

    private static String determineSchemaUri(Path testdataFile) {
        final Path path = Paths.get(TEST_DATA_DIRECTORY).relativize(testdataFile).getParent();
        if (path == null) {
            return Paths.get(TOPLEVEL_SCHEMA_FILE).toUri().toString();
        }
        final Path schema = Paths.get(SCHEMA_DIRECTORY,
                (path.getParent() != null) ? path.getParent().toString() : "",
                path.getFileName().toString() + ".json");
        if (!Files.exists(schema)) {
            throw new IllegalStateException("Kunne ikke finne skjemafil med navn: " + schema.toString());
        }
        return schema.toUri().toString();
    }
    
    private static Predicate<? super File> ignoreTempFiles() {
        return f -> !f.getName().startsWith("_");
    }
}
