import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.io.File
import java.nio.file.Paths

class VedleggTest {
    @ParameterizedTest
    @MethodSource("alleTester")
    fun jsonValiderer(navn: String, skalKjore: Boolean) = valider(navn, skalKjore)

    private fun valider(filnavn: String, forventGyldig: Boolean) {
        val testfile = File("src/test/resources/json/vedlegg/$filnavn.json")
        val report = JsonSosialhjelpValidator.validateFile(testfile, Paths.get("json/vedlegg/vedleggSpesifikasjon.json").toUri().toString())
        val message = "Fil ${testfile.name} forventes ${if (forventGyldig) "gyldig" else "ugyldig"}\n$report"
        assertThat(report.isSuccess).describedAs(message).isEqualTo(forventGyldig)
        if (forventGyldig) assertThat(JsonSosialhjelpValidator.hasWarnings(report)).describedAs("Det er warnings for fil ${testfile.name}\n$report").isFalse()
    }

    companion object {
        @JvmStatic
        fun alleTester(): List<Array<Any>> = listOf(
            arrayOf("gyldig_minimal", true), arrayOf("gyldig_minimal2", true), arrayOf("gyldig_standard", true),
            arrayOf("gyldig_ekstrafelter", true), arrayOf("gyldig_komplett", true), arrayOf("gyldig_hendelseTyper", true),
            arrayOf("ikkegyldig_ugyldigvedlegg", false), arrayOf("ikkegyldig_ugyldigHendelseType", false), arrayOf("ikkegyldig_ugyldigfil", false),
        )
    }
}
