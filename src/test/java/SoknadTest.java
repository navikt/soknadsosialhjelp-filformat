import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
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
import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.SyntaxChecker;
import com.github.fge.jsonschema.core.report.LogLevel;
import com.github.fge.jsonschema.core.report.ProcessingMessage;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.core.tree.SchemaTree;
import com.github.fge.jsonschema.library.DraftV4Library;
import com.github.fge.jsonschema.library.Keyword;
import com.github.fge.jsonschema.library.KeywordBuilder;
import com.github.fge.jsonschema.library.Library;
import com.github.fge.jsonschema.library.LibraryBuilder;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import com.github.fge.msgsimple.bundle.MessageBundle;

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

    private static boolean hasWarnings(ProcessingReport report) {
        for (ProcessingMessage pm : report) {
            if (pm.getLogLevel() == LogLevel.WARNING) {
                return true;
            }
        }
        return false;
    }
    
    public static void valider(File testfile, boolean forventGyldig) throws Exception {
        final JsonNode json = JsonLoader.fromFile(testfile);
        final ProcessingReport report = validator.validate(json);

        assertEquals("Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report, forventGyldig, report.isSuccess());
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !hasWarnings(report));
        }
    }
    
    private static JsonSchema createValidator() {
        try {
            return JsonSchemaFactory
                    .newBuilder()
                    .setValidationConfiguration(createValidationConfiguration())
                    .freeze()
                    .getJsonSchema(Paths.get(SCHEMA_FILE).toUri().toString());
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static Library withIgnoredKeywords(Library library, List<String> keywords) {
        final LibraryBuilder builder = library.thaw();
        for (String keyword : keywords) {
            final KeywordBuilder keywordBuilder = Keyword.newBuilder(keyword);
            keywordBuilder.withSyntaxChecker(new IgnoreSyntaxCheck());
            builder.addKeyword(keywordBuilder.freeze());
        }
        return builder.freeze();
    }
    
    private static class IgnoreSyntaxCheck implements SyntaxChecker {
        @Override
        public EnumSet<NodeType> getValidTypes() {
            return null;
        }
        
        @Override
        public void checkSyntax(Collection<JsonPointer> pointers, MessageBundle bundle, ProcessingReport report,
                SchemaTree tree) throws ProcessingException {
            // Ignore
        }
    }
    
    private static ValidationConfiguration createValidationConfiguration() {
        final Library library = withIgnoredKeywords(DraftV4Library.get(), Arrays.asList("javaType", "extends"));
        final ValidationConfiguration config = ValidationConfiguration.newBuilder()
                .setDefaultLibrary("http://json-schema.org/draft-06/schema", library)
                .freeze();
                
        return config;
    }
}
