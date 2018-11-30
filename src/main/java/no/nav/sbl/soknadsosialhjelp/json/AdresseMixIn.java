package no.nav.sbl.soknadsosialhjelp.json;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonGateAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonMatrikkelAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonPostboksAdresse;
import no.nav.sbl.soknadsosialhjelp.soknad.adresse.JsonUstrukturertAdresse;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type", visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = JsonPostboksAdresse.class, name = "postboks"),
    @JsonSubTypes.Type(value = JsonGateAdresse.class, name = "gateadresse"),
    @JsonSubTypes.Type(value = JsonMatrikkelAdresse.class, name = "matrikkeladresse"),
    @JsonSubTypes.Type(value = JsonUstrukturertAdresse.class, name = "ustrukturert"),
})
public interface AdresseMixIn {}