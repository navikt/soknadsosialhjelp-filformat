import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonAdresse
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonGateAdresse
import no.nav.sbl.soknadsosialhjelp.soknad.common.JsonKilde
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class AdresseSubTypeTest {
    @Test
    fun subtypeSkalBenyttesVedLesing() {
        val jsonAdresse = JsonSosialhjelpObjectMapper.createObjectMapper()
            .readValue(File("src/test/resources/json/soknad/parts/adresse/fullstendig-gateadresse.json"), JsonAdresse::class.java)

        assertThat(jsonAdresse.kilde).describedAs("Skal lese felt som kun finnes på superklasse").isEqualTo(JsonKilde.BRUKER)
        assertThat(jsonAdresse.type).describedAs("Skal lese delt felt").isEqualTo(JsonAdresse.Type.GATEADRESSE)
        assertThat(jsonAdresse).describedAs("Riktig subklasse skal velges").isInstanceOf(JsonGateAdresse::class.java)
        assertThat((jsonAdresse as JsonGateAdresse).gatenavn).describedAs("Skal lese felt som kun finnes på subklasse").isEqualTo("Testeveien")
    }
}
