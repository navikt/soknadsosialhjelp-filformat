[![Build Code](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/buildCode.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/buildCode.yml)
[![Build and release package in Github Packages](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/releaseGithub.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/releaseGithub.yml)

# Filformater for økonomisk sosialhjelp

I dette repoet finner du definisjoner av dataformatene brukt til informasjonsutveksling mellom NAV Digisos og kommunale fagsystemer.
Gjeldende filformat er definert i JSON Schema av [hoved-branchen](https://github.com/navikt/soknadsosialhjelp-filformat/).

Se **[definisjoner med endringshistorikk](https://navikt.github.io/soknadsosialhjelp-filformat/)** *(direktelenker: [Søknad](https://navikt.github.io/soknadsosialhjelp-filformat/#/soknad/getsoknad_json), [Vedlegg](readme-vedlegg-json.md), [Brukerinnsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json))*.

I tillegg rommer repoet Java-DTO-er, en Kotlin Multiplatform-modell, Java-kode for validering, og generatorer for testsøknader.

## Artefakter

Repoet publiserer to artefakter til **GitHub Packages**. Begge bygges fra de samme JSON Schema-definisjonene i `json/`.

| Artefakt | Innhold | Brukes av |
|---|---|---|
| `no.nav.sbl.dialogarena:soknadsosialhjelp-filformat` | Java-DTO-er (jsonschema2pojo), Jackson-mixins, validator, og selve JSON Schema-filene som ressurser | Eksisterende JVM-konsumenter |
| `no.nav.sbl.dialogarena:soknadsosialhjelp-filformat-kmp` | Kotlin Multiplatform-modell for `digisos/soker`-hendelser (kotlinx.serialization), JVM + JS | `sosialhjelp-innsyn-api`, `sosialhjelp-modia-api` |
| `@navikt/soknadsosialhjelp-filformat` (npm) | Kotlin/JS-varianten av samme modell | `sosialhjelp-adminpanel` (Next.js, server-side) |

De to modellene er ikke generert fra hverandre. `ParityTest` deserialiserer alle fixtures i `src/test/resources/json/` med begge modellene og krever at resultatet er likt, slik at modellene ikke kan drifte fra hverandre uten at bygget blir rødt.

### Bruk av KMP-modellen på JVM

Bruk `filformatJson`, **ikke** Jackson:

```kotlin
import no.nav.sosialhjelp.filformat.filformatJson

val soker = filformatJson.decodeFromString<DigisosSoker>(json)
```

KMP-modellen er med vilje mer tolerant enn Java-modellen: ukjente `type`-verdier blir `UkjentHendelse`, og ukjente enum-verdier blir `UKJENT`. Dette er implementert med `JsonContentPolymorphicSerializer` og `UkjentTolerantEnumSerializer`, som **kun** virker gjennom kotlinx.serialization.

Leser man de samme klassene med Jackson (`jackson-module-kotlin`), forsvinner hele denne toleransen, og en ny kommunal hendelsestype vil kaste exception i stedet for å bli absorbert. Det er nettopp det modellen finnes for å unngå.

## Henvendelser

Spørsmål knyttet til koden eller teamet kan stilles til teamdigisos@nav.no.

### For NAV-ansatte

NAV-interne henvendelser kan sendes via Slack til [#team_digisos](https://nav-it.slack.com/archives/C6LDFTJP2).

## Teknisk

`doc/`-katalogen er publisert på [GitHub Pages](https://github.com/navikt/soknadsosialhjelp-filformat/).

`json/`-katalogen inneholder JSON Schema-definisjonene.

Java-klassene ligger under `no.nav.sbl.soknadsosialhjelp`. Kotlin-modellen ligger under `no.nav.sosialhjelp.filformat`.

### Fallgruver

Ting som ikke er åpenbare fra koden, og som har brutt ting før:

- **`rootProject.name` i `settings.gradle.kts` er kritisk.** Publisert `artifactId` utledes fra prosjektnavnet. Tidligere kom navnet fra katalognavnet ved et sammentreff. Fjernes eller endres linjen, bytter artefakten navn i stillhet og alle konsumenter brekker. Det samme gjelder `artifactId`-omskrivingen i `filformat-kmp/build.gradle.kts`.
- **`ONLY_CODEGEN$ref` i `json/`-filene.** `replaceTokensInJson` i `build.gradle.kts` bytter ut dette tokenet med `$ref` før kodegenerering. Det gir Java-arv (`extends`) uten at runtime-validatoren ser en `$ref` som ville brutt valideringen. Schemaene som ligger i jar-en beholder tokenet.
- **`javaType` i schemaene styrer pakkeplassering** av de genererte Java-klassene.
- **Polymorfi er ikke generert.** jsonschema2pojo lager bare «dumme» klasser; `type`-diskriminatoren håndteres av håndskrevne mixins i `src/main/java/no/nav/sbl/soknadsosialhjelp/json/`. En ny hendelsestype krever redigering av `HendelseMixIn.java`.
- **`belop` er `Double`, ikke `BigDecimal`.** Schemaet sier bare `"type": "number"`. Konsumenter konverterer selv. KMP-modellen bruker `Double` for å matche Java-modellen eksakt — paritetstesten håndhever dette.
- **To ulike `Vedlegg`-begreper finnes:** `digisos/soker/parts/vedlegg.json` (inne i hendelser) og `vedlegg/vedleggSpesifikasjon.json`. De holdes fra hverandre med pakkenavn, ikke klassenavn.
- **Alle datoer og tidspunkter er `String`.** `types/dato.json` og `types/tidspunkt.json` er strenger med ISO-mønstre, så KMP-modellen trenger ikke `kotlinx-datetime`.
- **`kotlin-js-store/` må sjekkes inn.** Uten yarn-lockfilen blir JS-byggene ikke reproduserbare.

### Bygging

Prosjektet bruker [Gradle](https://gradle.org/) som byggesystem.

Prosjektet inkluderer [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), slik at man ikke er avhengig av å installere Gradle lokalt.

Kjør `./gradlew [kommando]` (Unix/Mac) eller `gradlew.bat [kommando]` (Windows).

#### Nyttige kommandoer:
- `./gradlew build` - Bygger prosjektet, inkludert KMP-modulen og paritetstestene
- `./gradlew test` - Kjører testene i rotprosjektet
- `./gradlew :filformat-kmp:allTests` - Kjører KMP-testene (JVM + JS)
- `./gradlew clean` - Renser byggekataloger
- `./gradlew generateJsonSchema2Pojo` - Genererer Java-klasser fra JSON Schema

> **Merk:** `./gradlew publish` forsøker også å publisere npm-pakken, og feiler uten
> `NPM_AUTH_TOKEN`. Bruk `publishAllPublicationsToGitHubPackagesRepository` for Maven og
> `publishJsPackageToGithubPackagesRegistry` for npm, slik `releaseGithub.yml` gjør.

### Release (GitHub Packages)

`.github/workflows/releaseGithub.yml` kjører automatisk etter en grønn `Build Code` på `main`, og kan startes manuelt.

Versjonen settes ved at workflowen kjører `sed` på `version = "..."` i rot-`build.gradle.kts` (format: `1.<dato>-<tid>-<commit>`). `filformat-kmp` arver den via `version = rootProject.version`.

Maven-artefaktene og npm-pakken publiseres i hvert sitt steg, slik at en feil mot npm-registeret ikke blokkerer Java-artefakten.

> Prosjektet publiserte tidligere til Maven Central via Sonatype. Den kjeden hadde vært
> ødelagt siden mars 2025 (nexus-pluginen manglet i `plugins {}`), og er fjernet.
> GitHub Packages er nå eneste publiseringskanal, og signering med GPG er derfor ikke
> lenger nødvendig.

### Teknologier

* Java 21
* Kotlin Multiplatform 2.4 med kotlinx.serialization
* Gradle
* Swagger UI
* [JSON Schema](https://json-schema.org/)
* [jsonschema2pojo](https://github.com/joelittlejohn/jsonschema2pojo) for Java-klasser fra JSON Schema

## Planlagte fremtidige versjoner/endringer:

* [Ny fil for data fra fagsystem for søkers innsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json)
