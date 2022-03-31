import com.github.fge.jsonschema.core.report.ProcessingReport;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.File;
import java.nio.file.Paths;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

class VedleggTest {

    private static Iterable<Object[]> alleTester() {
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

    @ParameterizedTest
    @MethodSource({"alleTester"})
    void jsonValiderer(String navn, boolean skalKjore) {
        valider(navn, skalKjore);
    }

    private void valider(String filnavn, boolean forventGyldig) {
        final File testfile = new File("src/test/resources/json/vedlegg/" + filnavn + ".json");
        final String schemaUri = Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toUri().toString();

        final ProcessingReport report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri);

        final String message = "Fil " + testfile.getName() + " forventes " + (forventGyldig ? "gyldig" : "ugyldig") + "\n" + report;
        assertThat(report.isSuccess()).describedAs(message).isEqualTo(forventGyldig);
        if (forventGyldig) {
            assertThat(JsonSosialhjelpValidator.hasWarnings(report)).describedAs("Det er warnings for fil " + testfile.getName() + "\n" + report).isFalse();
        }
    }
}
