import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;

@RunWith(Parameterized.class)
public class SoknadTest {
    
    private static final String PREFIX_FIL_MED_FEIL = "feil-";
    private static final String SCHEMA_FILE = "json/soknad/soknad.json";
    private static final String TEST_DATA_DIRECTORY = "src/test/resources/json/soknad/";

    private static JsonSchema validator = createValidator();

    private final File testfile; 

    
    public SoknadTest(String testName, File testfile) {
        this.testfile = testfile;
    }
    
    
    @Parameters(name="{0}")
    public static Iterable<Object[]> data() {
        final List<File> testFiles = determineTestFiles(Paths.get(TEST_DATA_DIRECTORY).toFile());
        
        return testFiles.stream()
                .filter(ignoreTempFiles())
                .map(toArrayWithName())
                .collect(Collectors.toList());
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


    private static Function<? super File, ? extends Object[]> toArrayWithName() {
        return f -> {
            return new Object[] {
                f.getName(), f
            };
        };
    }


    private static Predicate<? super File> ignoreTempFiles() {
        return f -> !f.getName().startsWith("_");
    }
    
    
    @Test
    public void jsonValiderer() throws Exception {
        valider(testfile, !testfile.getName().startsWith(PREFIX_FIL_MED_FEIL));
    }

    public static void valider(File testfile, boolean forventGyldig) throws Exception {
        JsonNode json = JsonLoader.fromFile(testfile);

        ProcessingReport report = validator.validate(json);
        System.out.println(report);

        assertEquals("Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report, forventGyldig, report.isSuccess());
    }
    
    private static JsonSchema createValidator() {
        try {
            final JsonNode schemaResource = JsonLoader.fromFile(Paths.get(SCHEMA_FILE).toFile());
            return JsonSchemaFactory.byDefault().getJsonSchema(schemaResource);
        } catch (IOException|ProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
