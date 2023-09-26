[![Build and release](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/release.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/release.yml)
[![Published on Maven](https://img.shields.io/maven-metadata/v/https/repo1.maven.org/maven2/no/nav/sbl/dialogarena/soknadsosialhjelp-filformat/maven-metadata.xml.svg)](https://repo1.maven.org/maven2/no/nav/sbl/dialogarena/soknadsosialhjelp-filformat/)

# Filformater for økonomisk sosialhjelp

I dette repoet finner du definisjoner av dataformatene brukt til informasjonsutveksling mellom NAV Digisos og kommunale fagsystemer, samt DTO-er i Java for å representere disse formatene.

Gjeldende filformat er definert i JSON Schema av [hoved-branchen](https://github.com/navikt/soknadsosialhjelp-filformat/).

Herfra publiseres [definisjoner og endringshistorikk](https://navikt.github.io/soknadsosialhjelp-filformat/).

*Direktelenker: [Søknad](https://navikt.github.io/soknadsosialhjelp-filformat/#/soknad/getsoknad_json), [Vedlegg](readme-vedlegg-json.md), [Brukerinnsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json).*

## Henvendelser

Spørsmål knyttet til koden eller teamet kan stilles til teamdigisos@nav.no.

### For NAV-ansatte

NAV-interne henvendelser kan sendes via Slack til [#team_digisos](https://nav-it.slack.com/archives/C6LDFTJP2).

## Teknisk

`doc/`-katalogen er publisert på [GitHub Pages](https://github.com/navikt/soknadsosialhjelp-filformat/).

`json/`-katalogen inneholder JSON Schema-definisjonene.

Java-klassene ligger under `no.nav.sbl.soknadsosialhjelp`.

### Bygging (maven)

Prosjektet inkluderer [maven wrapper](https://maven.apache.org/wrapper/), slik at man ikke er avhengig av å installere maven lokalt.

Kjør `./mvnw [kommando]` fremfor `mvn [kommando]`.

### Teknologier

* Java
* JDK 11
* Maven
* Swagger UI
* [JSON Schema](https://json-schema.org/)
* [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo) for Java-klasser fra JSON Schema

## Planlagte fremtidige versjoner/endringer:

* [Ny fil for data fra fagsystem for søkers innsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json)