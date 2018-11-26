import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class TestDataFiles {

    private static final String PREFIX_IGNORE = "_";
    private static final String PREFIX_FIL_MED_FEIL = "feil-";


    public static List<Object[]> list(Config config) {
        final List<File> testFiles = allFilesRecursively(Paths.get(config.testDataDirectory).toFile());
        
        return testFiles.stream()
                .filter(shouldIncludeFile())
                .map(toTestParameters(config))
                .collect(Collectors.toList());
    }

    
    private static List<File> allFilesRecursively(File parent) {
        final List<File> result = new ArrayList<>();
        if (parent.isDirectory()) {
            for (File child : parent.listFiles()) {
                result.addAll(allFilesRecursively(child));
            }
        } else {
            result.add(parent);
        }
        return result;
    }

    private static Function<? super File, ? extends Object[]> toTestParameters(Config config) {
        return f -> {
            return new Object[] {
                    f.getName(),
                    new TestData(
                        f,
                        determineSchemaUri(config, f.toPath()),
                        !f.getName().startsWith(PREFIX_FIL_MED_FEIL)
                    )};
        };
    }

    private static String determineSchemaUri(Config config, Path testdataFile) {
        final Path path = Paths.get(config.testDataDirectory).relativize(testdataFile).getParent();
        if (path == null) {
            return Paths.get(config.getToplevelSchemaFile()).toUri().toString();
        }
        final Path schema = Paths.get(config.schemaDirectory,
                (path.getParent() != null) ? path.getParent().toString() : "",
                path.getFileName().toString() + ".json");
        if (!Files.exists(schema)) {
            throw new IllegalStateException("Kunne ikke finne skjemafil med navn: " + schema.toString());
        }
        return schema.toUri().toString();
    }
    
    private static Predicate<? super File> shouldIncludeFile() {
        return f -> f.getName().endsWith(".json") && !f.getName().startsWith(PREFIX_IGNORE);
    }
    
    
    public static final class Config {
        private String schemaDirectory;
        private String toplevelSchemaFilename;
        private String testDataDirectory;
        
        public Config withSchemaDirectory(String schemaDirectory) {
            this.schemaDirectory = schemaDirectory;
            return this;
        }
        
        public Config withToplevelSchemaFilename(String toplevelSchemaFilename) {
            this.toplevelSchemaFilename = toplevelSchemaFilename;
            return this;
        }
        
        public Config withTestDataDirectory(String testDataDirectory) {
            this.testDataDirectory = testDataDirectory;
            return this;
        }
        
        private String getToplevelSchemaFile() {
            return schemaDirectory + "/" + toplevelSchemaFilename;
        }
    }
}
