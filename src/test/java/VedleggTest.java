import com.github.fge.jsonschema.core.report.ProcessingReport;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@RunWith(Parameterized.class)
public class VedleggTest {

    private final String navn;
    private final boolean skalKjore;

    public VedleggTest(String navn, boolean skalkjore) {
        this.navn = navn;
        this.skalKjore = skalkjore;
    }

    @Parameters(name = "{0}")
    public static Iterable<Object[]> alleTester() {
        return Arrays.asList(
                testMed("gyldig_minimal", true),
                testMed("gyldig_minimal2", true),
                testMed("gyldig_standard", true),
                testMed("gyldig_ekstrafelter", true),
                testMed("gyldig_komplett", true),
                testMed("gyldig_hendelseTyper", true),
                testMed("ikkegyldig_ugyldigvedlegg", false),
                testMed("ikkegyldig_ugyldigHendelseType", false),
                testMed("ikkegyldig_ugyldigfil", false)
        );
    }

    private static Object[] testMed(String navn, boolean skalKjore) {
        return new Object[]{
                navn, skalKjore
        };
    }

    @Test
    public void jsonValiderer() {
        valider(this.navn, this.skalKjore);
    }

    public void valider(String filnavn, boolean forventGyldig) {
        final File testfile = new File("src/test/resources/json/vedlegg/" + filnavn + ".json");
        final String schemaUri = Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toUri().toString();

        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);

        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertEquals(message, forventGyldig, report.isSuccess());
        if (forventGyldig) {
            assertFalse("Det er warnings for fil " + testfile.getName() + "\n" + report, JsonSosialhjelpValidator.hasWarnings(report));
        }
    }
}
