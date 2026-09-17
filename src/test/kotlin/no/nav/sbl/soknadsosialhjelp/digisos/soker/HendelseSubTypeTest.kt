package no.nav.sbl.soknadsosialhjelp.digisos.soker

import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.io.File

class HendelseSubTypeTest {
    @Test
    fun subtypeSkalBenyttesVedLesing() {
        val jsonHendelse = JsonSosialhjelpObjectMapper.createObjectMapper()
            .readValue(File("src/test/resources/json/digisos/soker/parts/hendelse/minimal.json"), JsonHendelse::class.java)

        assertThat(jsonHendelse.hendelsestidspunkt).describedAs("Skal lese felt som kun finnes på superklasse").isEqualTo("2018-10-04T13:37:00.134Z")
        assertThat(jsonHendelse.type).describedAs("Skal lese delt felt").isEqualTo(JsonHendelse.Type.SOKNADS_STATUS)
        assertThat(jsonHendelse::class.java).describedAs("Riktig subklasse skal velges").isEqualTo(JsonSoknadsStatus::class.java)
        assertThat((jsonHendelse as JsonSoknadsStatus).status).describedAs("Skal lese felt som kun finnes på subklasse").isEqualTo(JsonSoknadsStatus.Status.MOTTATT)
    }
}
