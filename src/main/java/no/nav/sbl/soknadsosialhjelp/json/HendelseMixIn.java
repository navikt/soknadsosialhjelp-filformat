package no.nav.sbl.soknadsosialhjelp.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonDokumentasjonEtterspurt;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonDokumentasjonkrav;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonForelopigSvar;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonRammevedtak;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSaksStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonTildeltNavKontor;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonUtbetaling;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVedtakFattet;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVilkar;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = JsonTildeltNavKontor.class, name = "tildeltNavKontor"),
        @JsonSubTypes.Type(value = JsonSoknadsStatus.class, name = "soknadsStatus"),
        @JsonSubTypes.Type(value = JsonVedtakFattet.class, name = "vedtakFattet"),
        @JsonSubTypes.Type(value = JsonSaksStatus.class, name = "saksStatus"),
        @JsonSubTypes.Type(value = JsonDokumentasjonEtterspurt.class, name = "dokumentasjonEtterspurt"),
        @JsonSubTypes.Type(value = JsonUtbetaling.class, name = "utbetaling"),
        @JsonSubTypes.Type(value = JsonVilkar.class, name = "vilkar"),
        @JsonSubTypes.Type(value = JsonRammevedtak.class, name = "rammevedtak"),
        @JsonSubTypes.Type(value = JsonForelopigSvar.class, name = "forelopigSvar"),
        @JsonSubTypes.Type(value = JsonDokumentasjonkrav.class, name = "dokumentasjonkrav")
})
public interface HendelseMixIn {
}