import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import com.github.fge.jackson.NodeType;
import com.github.fge.jackson.jsonpointer.JsonPointer;
import com.github.fge.jsonschema.cfg.ValidationConfiguration;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.keyword.syntax.checkers.SyntaxChecker;
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

public final class JsonValidatorUtils {
    
    static JsonSchema createValidator(String schemaFile) {
        try {
            return JsonSchemaFactory
                    .newBuilder()
                    .setValidationConfiguration(createValidationConfiguration())
                    .freeze()
                    .getJsonSchema(Paths.get(schemaFile).toUri().toString());
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
