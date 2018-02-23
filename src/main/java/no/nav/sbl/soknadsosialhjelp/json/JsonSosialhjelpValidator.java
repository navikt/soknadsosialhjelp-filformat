package no.nav.sbl.soknadsosialhjelp.json;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

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

public final class JsonSosialhjelpValidator {
    
    private JsonSosialhjelpValidator() {
        
    }
    
    
    /**
     * Sikrer at den angitte JSON-strengen er gyldig mot soknadskjemaet til Sosialhjelp.
     */
    public static void ensureValidSoknad(String json) throws JsonSosialhjelpValidationException {
        final String schemaUri = toSkjemaUri("/json/soknad/soknad.json");
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
     * Sikrer at den angitte JSON-strengen følger det angitte skjemaet.
     * 
     * @param json The JSON as a <code>String</code>
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
     * @param testfile JSON-filen som skal valideres.
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
            final ProcessingReport report = skjema.validate(json);
            return report;
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
        final ValidationConfiguration config = ValidationConfiguration.newBuilder()
                .setDefaultLibrary("http://json-schema.org/draft-06/schema", library)
                .freeze();
                
        return config;
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
}
