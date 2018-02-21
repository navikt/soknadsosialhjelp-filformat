import static org.junit.Assert.assertEquals;

import java.io.File;
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

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;

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
    private static final String TOPLEVEL_SCHEMA_FILE = "json/soknad/soknad.json";
    private static final String TEST_DATA_DIRECTORY = "src/test/resources/json/soknad/";

    private final File testfile; 
    private final String schemaLocation;

    
    public SoknadTest(String testName, File testfile, String schemaLocation) {
        this.testfile = testfile;
        this.schemaLocation = schemaLocation;
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
    public void jsonValiderer() throws Exception {
        final JsonSchema validator = JsonValidatorUtils.createValidator(schemaLocation);
        valider(testfile, !testfile.getName().startsWith(PREFIX_FIL_MED_FEIL), validator);
    }
    
    
    private static void valider(File testfile, boolean forventGyldig, JsonSchema validator) throws Exception {
        final JsonNode json = JsonLoader.fromFile(testfile);
        final ProcessingReport report = validator.validate(json);

        assertEquals("Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report, forventGyldig, report.isSuccess());
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !hasWarnings(report));
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
                f.getName(), f, determineSchemaLocation(f)
            };
        };
    }

    private static String determineSchemaLocation(File f) {
        final String schemaName = new File(TEST_DATA_DIRECTORY).toPath().relativize(f.toPath()).toFile().getParent();
        if (schemaName == null) {
            return TOPLEVEL_SCHEMA_FILE;
        }
        final File schemaFile = new File(SCHEMA_DIRECTORY, schemaName + ".json");
        if (!schemaFile.exists()) {
            throw new IllegalStateException("Kunne ikke finne skjemafil med navn: " + schemaFile.getAbsolutePath());
        }
        return schemaFile.getAbsolutePath();
    }

    private static boolean hasWarnings(ProcessingReport report) {
        for (ProcessingMessage pm : report) {
            if (pm.getLogLevel() == LogLevel.WARNING) {
                return true;
            }
        }
        return false;
    }
    
    private static Predicate<? super File> ignoreTempFiles() {
        return f -> !f.getName().startsWith("_");
    }
}
