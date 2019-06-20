package no.nav.sbl.soknadsosialhjelp.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSoknadsStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonSaksStatus;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonTildeltNavKontor;
import no.nav.sbl.soknadsosialhjelp.digisos.soker.hendelse.JsonVedtakFattet;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JsonTildeltNavKontor.class, name = "tildeltNavKontor"),
    @JsonSubTypes.Type(value = JsonSoknadsStatus.class, name = "soknadsStatus"),
    @JsonSubTypes.Type(value = JsonVedtakFattet.class, name = "vedtakFattet"),
    @JsonSubTypes.Type(value = JsonSaksStatus.class, name = "saksStatus")
})
public interface HendelseMixIn {}