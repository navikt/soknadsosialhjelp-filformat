package no.nav.sbl.soknadsosialhjelp.digisos.soker;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSaksStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonUtbetaling;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVilkar;
import no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpObjectMapper;
import org.junit.Test;

import static java.util.Arrays.asList;

public class FasitCaseTest {

    @Test
    public void lagFasitJson() throws JsonProcessingException {
        System.out.println("test");
        ObjectMapper mapper = JsonSosialhjelpObjectMapper.createObjectMapper();
        JsonDigisosSoker jsonDigisosSoker = new JsonDigisosSoker();
        jsonDigisosSoker.setHendelser(asList(
                new JsonSaksStatus()
                        .withStatus(JsonSaksStatus.Status.UNDER_BEHANDLING)
                        .withTittel("Strøm og Husleie")
                        .withReferanse("SAK1")
                        .withType(JsonHendelse.Type.SAKS_STATUS),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK1")
                        .withUtbetalingsreferanse("Betaling 1")
                        .withBeskrivelse("Strøm Juli")
                        .withUtbetalingsdato("2019-07-01")
                        .withStatus(JsonUtbetaling.Status.UTBETALT)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK1")
                        .withUtbetalingsreferanse("Betaling 2")
                        .withBeskrivelse("Strøm August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK1")
                        .withUtbetalingsreferanse("Betaling 3")
                        .withBeskrivelse("Husleie Juli")
                        .withUtbetalingsdato("2019-07-01")
                        .withStatus(JsonUtbetaling.Status.UTBETALT)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK1")
                        .withUtbetalingsreferanse("Betaling 4")
                        .withBeskrivelse("Husleie August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK1")
                        .withUtbetalingsreferanse("Betaling 5")
                        .withBeskrivelse("Husleie August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.STOPPET)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonVilkar().withBeskrivelse("Oppheving 1").withType(JsonHendelse.Type.VILKAR).withUtbetalingsreferanse(asList("Betaling 5")),

                new JsonSaksStatus()
                        .withStatus(JsonSaksStatus.Status.UNDER_BEHANDLING)
                        .withTittel("Basis")
                        .withReferanse("SAK2")
                        .withType(JsonHendelse.Type.SAKS_STATUS),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK2")
                        .withUtbetalingsreferanse("Betaling 6")
                        .withBeskrivelse("Basis Juli")
                        .withUtbetalingsdato("2019-07-01")
                        .withStatus(JsonUtbetaling.Status.UTBETALT)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK2")
                        .withUtbetalingsreferanse("Betaling 7")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.ANNULERT)
                        .withType(JsonHendelse.Type.UTBETALING),

                new JsonSaksStatus()
                        .withStatus(JsonSaksStatus.Status.UNDER_BEHANDLING)
                        .withTittel("Basis Omgjøring")
                        .withReferanse("SAK3")
                        .withType(JsonHendelse.Type.SAKS_STATUS),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 8")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 8")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.ANNULERT)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 9")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-01")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 10")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-08")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 11")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-15")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING),
                new JsonUtbetaling()
                        .withSaksreferanse("SAK3")
                        .withUtbetalingsreferanse("Betaling 12")
                        .withBeskrivelse("Basis August")
                        .withUtbetalingsdato("2019-08-22")
                        .withStatus(JsonUtbetaling.Status.PLANLAGT_UTBETALING)
                        .withType(JsonHendelse.Type.UTBETALING)
                ));

        System.out.println(mapper.writeValueAsString(jsonDigisosSoker));
    }
}
