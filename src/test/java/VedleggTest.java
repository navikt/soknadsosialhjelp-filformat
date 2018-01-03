import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchema;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

public class VedleggTest {

    JsonSchema validator;

    @Test
    public void jsonValiderer() throws Exception {
        JsonNode schemaResource = JsonLoader.fromFile(Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toFile());
        validator = JsonSchemaFactory.byDefault().getJsonSchema(schemaResource);

        valider("gyldig_minimal", true);
        valider("gyldig_minimal2", true);
        valider("gyldig_standard", true);
        valider("gyldig_ekstrafelter", true);
        valider("ikkegyldig_ugyldigvedlegg", false);
        valider("ikkegyldig_ugyldigfil", false);
    }

    public void valider(String filnavn, boolean forventGyldig) throws Exception {
        JsonNode json = JsonLoader.fromFile(Paths.get("src/test/resources/json/vedlegg/" + filnavn + ".json").toFile());

        ProcessingReport report = validator.validate(json);
        System.out.println(report);

        assertEquals("Fil " + filnavn + " forventes " + (forventGyldig ? "gyldig" : "ugyldig"), forventGyldig, report.isSuccess());
    }
}
