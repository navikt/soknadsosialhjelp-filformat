package no.nav.sbl.soknadsosialhjelp.json;

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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

public final class JsonSosialhjelpValidator {

    private JsonSosialhjelpValidator() {

    }

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Bruk: java -jar soknadsosialhjelp-filformat-...-shaded.jar [ --soknad SOKNAD_JSON | --vedlegg VEDLEGG_JSON | SCHEMA JSON ]");
            System.out.println();
            System.out.println("--soknad     - Valider med soknad-skjemaet.");
            System.out.println("--vedlegg    - Valider med vedlegg-skjemaet.");
            System.out.println();
            System.out.println("SOKNAD_JSON  - En JSON-fil med som skal følge soknad-formatet.");
            System.out.println("VEDLEGG_JSON - En JSON-fil med som skal følge vedlegg-formatet.");
            System.out.println("SCHEMA       - Et av skjemaene som følger med. F.eks.: \"/json/soknad/parts/version.json\".");
            System.out.println("JSON         - En JSON-fil med som skal følge angitt format.");
            System.exit(1);
        }

        try {
            final String schemaUri;
            switch (args[0]) {
                case "--soknad":
                    schemaUri = toSkjemaUri("/json/soknad/soknad.json");
                    break;
                case "--vedlegg":
                    schemaUri = toSkjemaUri("/json/vedlegg/vedleggSpesifikasjon.json");
                    break;
                case "--internal":
                    schemaUri = toSkjemaUri("/json/internal/internalSoknad.json");
                    break;
                default:
                    schemaUri = toSkjemaUri(args[0]);
                    break;
            }

            final String json = Files.readAllLines(Paths.get(args[1])).stream().reduce((a, b) -> a + b).get();
            ensureValid(json, schemaUri);
        } catch (Exception e) {
            System.err.println("Exception: " + e);
            System.exit(1);
        }
    }


    /**
     * Sikrer at den angitte JSON-strengen er gyldig mot soknadskjemaet til Sosialhjelp.
     */
    public static void ensureValidSoknad(String json) throws JsonSosialhjelpValidationException {
        final String schemaUri = toSkjemaUri("/json/soknad/soknad.json");
        ensureValid(json, schemaUri);
    }

    /**
     * Sikrer at den angitte JSON-strengen er gyldig mot det interne soknadskjemaet til Sosialhjelp.
     */
    public static void ensureValidInternalSoknad(String json) throws JsonSosialhjelpValidationException {
        final String schemaUri = toSkjemaUri("/json/internal/internalSoknad.json");
        ensureValid(json, schemaUri);
    }

    /**
     * Sikrer at den angitte JSON-strengen er gyldig mot vedleggskjemaet til Sosialhjelp.
     */
    public static void ensureValidVedlegg(String json) throws JsonSosialhjelpValidationException {
        final String schemaUri = toSkjemaUri("/json/vedlegg/vedleggSpesifikasjon.json");
        ensureValid(json, schemaUri);
    }

    /**
     * Sikrer at den angitte JSON-strengen er gyldig mot digisos-soker til Sosialhjelp.
     */
    public static void ensureValidInnsyn(String json) throws JsonSosialhjelpValidationException {
        final String schemaUri = toSkjemaUri("/json/digisos/soker/digisos-soker.json");
        ensureValid(json, schemaUri);
    }

    /**
     * Sikrer at den angitte JSON-strengen følger det angitte skjemaet.
     *
     * @param json      The JSON as a <code>String</code>
     * @param schemaUri Plasseringen til skjemaet som blir benyttet til valideringen.
     * @throws JsonSosialhjelpValidationException hvis JSON-strengen ikke følger skjemaet.
     */
    public static void ensureValid(String json, String schemaUri) throws JsonSosialhjelpValidationException {
        try {
            final ProcessingReport report = validate(JsonLoader.fromString(json), schemaUri);
            if (!report.isSuccess() || hasWarnings(report)) {
                throw new JsonSosialhjelpValidationException(report);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Validerer JSON-fil mot et skjema.
     *
     * @param testfile  JSON-filen som skal valideres.
     * @param skjemaUri Plasseringen til skjemaet som blir benyttet til valideringen.
     */
    public static ProcessingReport validateFile(File testfile, String schemaUri) {
        try {
            return validate(JsonLoader.fromFile(testfile), schemaUri);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Sjekker om angitt <code>ProcessingReport</code> inneholder advarsler.
     */
    public static boolean hasWarnings(ProcessingReport report) {
        for (ProcessingMessage pm : report) {
            if (pm.getLogLevel() == LogLevel.WARNING) {
                return true;
            }
        }
        return false;
    }

    private static ProcessingReport validate(JsonNode json, String schemaUri) {
        try {
            final JsonSchema skjema = createValidator(schemaUri);
            return skjema.validate(json);
        } catch (ProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private static String toSkjemaUri(String skjemaFil) {
        final String schemaUri;
        try {
            schemaUri = JsonSosialhjelpValidator.class.getResource(skjemaFil).toURI().toString();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return schemaUri;
    }

    private static JsonSchema createValidator(String schemaUri) {
        try {
            return JsonSchemaFactory
                    .newBuilder()
                    .setValidationConfiguration(createValidationConfiguration())
                    .freeze()
                    .getJsonSchema(schemaUri);
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

    private static ValidationConfiguration createValidationConfiguration() {
        final Library library = withIgnoredKeywords(DraftV4Library.get(), Arrays.asList("javaType", "extends"));

        return ValidationConfiguration.newBuilder()
                .setDefaultLibrary("http://json-schema.org/draft-06/schema", library)
                .freeze();
    }

    private static class IgnoreSyntaxCheck implements SyntaxChecker {
        @Override
        public EnumSet<NodeType> getValidTypes() {
            return null;
        }

        @Override
        public void checkSyntax(Collection<JsonPointer> pointers, MessageBundle bundle, ProcessingReport report,
                                SchemaTree tree) {
            // Ignore
        }
    }
}
