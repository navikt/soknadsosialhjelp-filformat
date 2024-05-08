package no.nav.sbl.soknadsosialhjelp.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonDokumentasjonkrav;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.klage.hendelse.JsonKlageStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.klage.hendelse.JsonSaksfremlegg;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.klage.hendelse.JsonKlageUtfall;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.klage.hendelse.JsonStatsforvalterUtfall;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JsonKlageStatus.class, name = "klageStatus"),
    @JsonSubTypes.Type(value = JsonDokumentasjonkrav.class, name = "klageDokumentasjonEtterspurt"),
    @JsonSubTypes.Type(value = JsonKlageUtfall.class, name = "klageUtfall"),
    @JsonSubTypes.Type(value = JsonStatsforvalterUtfall.class, name = "statsforvalterUtfall"),
    @JsonSubTypes.Type(value = JsonSaksfremlegg.class, name = "saksfremlegg"),
})
public interface KlageHendelseMixIn {
}
