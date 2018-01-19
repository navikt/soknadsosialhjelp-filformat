import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class SoknadTest {

    JsonSchema validator;

    @Test
    public void jsonValiderer() throws Exception {
        JsonNode schemaResource = JsonLoader.fromFile(Paths.get("json/soknad/soknadSpesifikasjon.json").toFile());
        validator = JsonSchemaFactory.byDefault().getJsonSchema(schemaResource);
        
        valider("minimal", true);
        valider("brukerdata", true);
        
        valider("feil-minimal-mangler-version", false);
    }

    public void valider(String filnavn, boolean forventGyldig) throws Exception {
        JsonNode json = JsonLoader.fromFile(Paths.get("src/test/resources/json/soknad/" + filnavn + ".json").toFile());

        ProcessingReport report = validator.validate(json);
        System.out.println(report);

        assertEquals("Fil " + filnavn + " forventes " + (forventGyldig ? "gyldig" : "ugyldig"), forventGyldig, report.isSuccess());
    }
}
