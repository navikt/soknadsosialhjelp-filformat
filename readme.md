[![Build and release](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/release.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/release.yml)
[![Published on Maven](https://img.shields.io/maven-metadata/v/https/repo1.maven.org/maven2/no/nav/sbl/dialogarena/soknadsosialhjelp-filformat/maven-metadata.xml.svg)](https://repo1.maven.org/maven2/no/nav/sbl/dialogarena/soknadsosialhjelp-filformat/)

# Filformater for økonomisk sosialhjelp

I dette repoet finner du definisjoner av dataformatene brukt til informasjonsutveksling mellom NAV Digisos og kommunale fagsystemer.
Gjeldende filformat er definert i JSON Schema av [hoved-branchen](https://github.com/navikt/soknadsosialhjelp-filformat/).

Se **[definisjoner med endringshistorikk](https://navikt.github.io/soknadsosialhjelp-filformat/)** *(direktelenker: [Søknad](https://navikt.github.io/soknadsosialhjelp-filformat/#/soknad/getsoknad_json), [Vedlegg](readme-vedlegg-json.md), [Brukerinnsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json))*.

I tillegg rommer repoet Java-DTO-er, Java-kode for validering, og generatorer for testsøknader.

## Henvendelser

Spørsmål knyttet til koden eller teamet kan stilles til teamdigisos@nav.no.

### For NAV-ansatte

NAV-interne henvendelser kan sendes via Slack til [#team_digisos](https://nav-it.slack.com/archives/C6LDFTJP2).

## Teknisk

`doc/`-katalogen er publisert på [GitHub Pages](https://github.com/navikt/soknadsosialhjelp-filformat/).

`json/`-katalogen inneholder JSON Schema-definisjonene.

Java-klassene ligger under `no.nav.sbl.soknadsosialhjelp`.

### Bygging

Prosjektet bruker [Gradle](https://gradle.org/) som byggesystem.

Prosjektet inkluderer [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), slik at man ikke er avhengig av å installere Gradle lokalt.

Kjør `./gradlew [kommando]` (Unix/Mac) eller `gradlew.bat [kommando]` (Windows).

#### Nyttige kommandoer:
- `./gradlew build` - Bygger prosjektet
- `./gradlew test` - Kjører tester
- `./gradlew clean` - Renser byggekataloger
- `./gradlew publish` - Publiserer til Maven repository
- `./gradlew generateJsonSchema2Pojo` - Genererer Java-klasser fra JSON Schema

### Release (maven central/sonatype nexus)
Vi publiserer pakkene på maven central via sonatype nexus. Pakkene signeres før publisering, se punkt under.

#### Signering
Pakkene signeres med GPG-nøkkelen til NAV (se [nøkkel på keyserver](https://keyserver.ubuntu.com/pks/lookup?search=a511889134f13602&fingerprint=on&op=index)). 
Passphrase og privatnøkkel ligger som secret i github actions. Ved forrige rullering på nøklene tok vi kontakt med Tommy Trøen for nye nøkler som ble lagt inn i secrets i github. 

### Teknologier

* Java 21
* Gradle
* Swagger UI
* [JSON Schema](https://json-schema.org/)
* [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo) for Java-klasser fra JSON Schema

## Planlagte fremtidige versjoner/endringer:

* [Ny fil for data fra fagsystem for søkers innsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json)
