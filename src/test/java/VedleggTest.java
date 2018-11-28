import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.github.fge.jsonschema.core.report.ProcessingReport;

import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;

@RunWith(Parameterized.class)
public class VedleggTest {

    private final String navn;
    private final boolean skalKjore;

    
    public VedleggTest(String navn, boolean skalkjore) {
        this.navn = navn;
        this.skalKjore = skalkjore;
    }

    @Test
    public void jsonValiderer() throws Exception {
        valider(this.navn, this.skalKjore);
    }
    
    @Parameters(name="{0}")
    public static Iterable<Object[]> alleTester(){
        return Arrays.asList(
                testMed("gyldig_minimal", true),
                testMed("gyldig_minimal2", true),
                testMed("gyldig_standard", true),
                testMed("gyldig_ekstrafelter", true),
                testMed("ikkegyldig_ugyldigvedlegg", false),
                testMed("ikkegyldig_ugyldigfil", false)
                );
    }

    private static Object[] testMed(String navn, boolean skalKjore) {
        return new Object[] {
            navn, skalKjore
        };
    }
    
    public void valider(String filnavn, boolean forventGyldig) {
        final File testfile = new File("src/test/resources/json/vedlegg/" + filnavn + ".json");
        final String schemaUri = Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toUri().toString();
        
        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);
        
        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertEquals(message, forventGyldig, report.isSuccess());
        if (forventGyldig) {
            assertEquals("Det er warnings for fil " + testfile.getName() + "\n" + report, true, !JsonSosialhjelpValidator.hasWarnings(report));
        }
    }
}
