import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator
import org.assertj.core.api.Assertions.assertThat
import java.io.File

class TestData(private val testfile: File, private val schemaUri: String, private val forventGyldig: Boolean) {
    fun valider() {
        val report = JsonSosialhjelpValidator.validateFile(testfile, schemaUri)
        assertThat(report.isSuccess).describedAs("Fil ${testfile.name} forventes ${if (forventGyldig) "gyldig" else "ugyldig"}\n$report").isEqualTo(forventGyldig)
        if (forventGyldig) assertThat(!JsonSosialhjelpValidator.hasWarnings(report)).describedAs("Det er warnings for fil ${testfile.name}\n$report").isTrue()
    }
}
