[![Build Code](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/buildCode.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/buildCode.yml)
[![Build and release package in Github Packages](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/releaseGithub.yml/badge.svg)](https://github.com/navikt/soknadsosialhjelp-filformat/actions/workflows/releaseGithub.yml)

# Filformater for økonomisk sosialhjelp

I dette repoet finner du definisjoner av dataformatene brukt til informasjonsutveksling mellom NAV Digisos og kommunale fagsystemer.
Gjeldende filformat er definert i JSON Schema av [hoved-branchen](https://github.com/navikt/soknadsosialhjelp-filformat/).

Se **[definisjoner med endringshistorikk](https://navikt.github.io/soknadsosialhjelp-filformat/)** *(direktelenker: [Søknad](https://navikt.github.io/soknadsosialhjelp-filformat/#/soknad/getsoknad_json), [Vedlegg](readme-vedlegg-json.md), [Brukerinnsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json))*.

I tillegg rommer repoet en generator som lager Kotlin-modeller fra JSON Schema-definisjonene, Java-kode for validering, og generatorer for testsøknader.

## Artefakter

Repoet publiserer tre artefakter til **GitHub Packages**. Alle genereres fra de samme JSON Schema-definisjonene i `json/`, av en felles kodegenerator i `buildSrc/` (`filformat.codegen`).

| Artefakt | Innhold | Brukes av |
|---|---|---|
| `no.nav.sbl.dialogarena:soknadsosialhjelp-filformat` | Validator og selve JSON Schema-filene som ressurser | Eksisterende JVM-konsumenter |
| `no.nav.sbl.dialogarena:soknadsosialhjelp-filformat-jackson` | Jackson-annotert Kotlin-modell, samme pakkenavn og klassenavn som den tidligere jsonschema2pojo-genererte Java-modellen | Drop-in for eksisterende JVM-konsumenter av modellen |
| `no.nav.sosialhjelp.filformat:soknadsosialhjelp-filformat-kmp{,-jvm,-js}` | Kotlin Multiplatform-modell (kotlinx.serialization), JVM + JS, hele skjemaet | `sosialhjelp-innsyn-api`, `sosialhjelp-modia-api` |
| `@navikt/soknadsosialhjelp-filformat` (npm) | Kotlin/JS-varianten av samme modell | `sosialhjelp-adminpanel` (Next.js, server-side) |

Begge modellene genereres fra samme mellomrepresentasjon (`SchemaParser`/`SchemaModel` i `buildSrc/`), så de kan ikke drifte fra hverandre navnemessig. `ModelSnapshotTest` i rotprosjektet fanger utilsiktede navne-/strukturendringer i Jackson-modellen; `ModellTest`/`ToleranseTest` i `filformat-kmp` er de tilsvarende akseptansetestene for kotlinx-modellen.

De genererte kildene sjekkes inn (`filformat-jackson/src/main/kotlin`, `filformat-kmp/src/commonMain/kotlin`), slik at modellendringer er synlige i pull request-differ. `verifyGeneratedSources` i begge moduler feiler bygget dersom de er ute av synk med `json/`.

### Bruk av KMP-modellen på JVM

Bruk `filformatJson`, **ikke** Jackson:

```kotlin
import no.nav.sosialhjelp.filformat.filformatJson

val soker = filformatJson.decodeFromString<DigisosSoker>(json)
```

KMP-modellen er med vilje mer tolerant enn Jackson-modellen: ukjente `type`-verdier blir `Ukjent<Type>` (f.eks. `UkjentHendelse`), og ukjente enum-verdier blir `UKJENT`. Dette er implementert med `JsonContentPolymorphicSerializer` og `UkjentTolerantEnumSerializer`, som **kun** virker gjennom kotlinx.serialization.

Leser man de samme klassene med Jackson, forsvinner hele denne toleransen, og en ny kommunal hendelsestype vil kaste exception i stedet for å bli absorbert. Det er nettopp det KMP-modellen finnes for å unngå.

## Henvendelser

Spørsmål knyttet til koden eller teamet kan stilles til teamdigisos@nav.no.

### For NAV-ansatte

NAV-interne henvendelser kan sendes via Slack til [#team_digisos](https://nav-it.slack.com/archives/C6LDFTJP2).

## Teknisk

`doc/`-katalogen er publisert på [GitHub Pages](https://github.com/navikt/soknadsosialhjelp-filformat/).

`json/`-katalogen inneholder JSON Schema-definisjonene, som er kilden til sannhet for begge modellene.

Kodegeneratoren ligger i `buildSrc/src/main/kotlin/filformat/codegen/`: `SchemaParser` bygger en mellomrepresentasjon (`SchemaModel`) fra `json/`, og `JacksonEmitter`/`KotlinxEmitter` emitterer hver sin Kotlin-modell fra den. Regenerer med `./gradlew :filformat-jackson:generateJacksonModel :filformat-kmp:generateKotlinxModel` etter endringer i `json/`.

Jackson-modellen ligger under `no.nav.sbl.soknadsosialhjelp` (samme pakkenavn og `Json`-prefiks som den gamle jsonschema2pojo-modellen). Kotlin-modellen ligger under `no.nav.sosialhjelp.filformat` (samme pakkenavn som den tidligere håndskrevne KMP-modellen; union-varianter som `SoknadsStatus` ligger flatt sammen med sin `sealed interface`, ikke i en dypere pakke, for å matche det som allerede var publisert).

### Fallgruver

Ting som ikke er åpenbare fra koden, og som har brutt ting før:

- **`rootProject.name` i `settings.gradle.kts` er kritisk.** Publisert `artifactId` utledes fra prosjektnavnet. Tidligere kom navnet fra katalognavnet ved et sammentreff. Fjernes eller endres linjen, bytter artefakten navn i stillhet og alle konsumenter brekker. Det samme gjelder `artifactId`-omskrivingen i `filformat-kmp/build.gradle.kts`.
- **`ONLY_CODEGEN$ref` i `json/`-filene.** Kodegeneratoren bruker dette som signal på at et objekt arver fra et annet (`extends`) uten at runtime-validatoren ser en `$ref` som ville brutt valideringen.
- **`javaType` i schemaene styrer pakkeplassering og klassenavn** i begge genererte modeller. Alle objekt-/enum-noder som skal bli egne klasser må ha en eksplisitt `javaType` — kodegeneratoren feiler høyt (`error(...)`) hvis den finner en som mangler det, i stedet for å gjette et navn slik jsonschema2pojo gjorde.
- **Polymorfi er generert, ikke håndskrevet.** `oneOf` + en `allOf`-gren som låser `type` til én verdi tolkes som en diskriminert union. `SchemaParser` finner disse automatisk; en ny hendelsestype krever ingen endring i generatoren, kun en ny schema-fil.
- **`belop` er `Double`, ikke `BigDecimal`.** Schemaet sier bare `"type": "number"`, som begge generatorene mapper til `Double`. Konsumenter konverterer selv.
- **To ulike `Vedlegg`-begreper finnes:** `digisos/soker/parts/vedlegg.json` (inne i hendelser) og `vedlegg/vedleggSpesifikasjon.json`. De holdes fra hverandre med pakkenavn, ikke klassenavn.
- **Alle datoer og tidspunkter er `String`.** `types/dato.json` og `types/tidspunkt.json` er strenger med ISO-mønstre, så ingen av modellene trenger `kotlinx-datetime`/`java.time`.
- **`kotlin-js-store/` må sjekkes inn.** Uten yarn-lockfilen blir JS-byggene ikke reproduserbare.
- **Genererte kilder er commitet.** Ikke rediger `filformat-jackson/src/main/kotlin/**` eller `filformat-kmp/src/commonMain/kotlin/**` for hånd — endringer forsvinner ved neste `generate*Model`-kjøring, og `verifyGeneratedSources` fanger drift i CI.

### Bygging

Prosjektet bruker [Gradle](https://gradle.org/) som byggesystem.

Prosjektet inkluderer [Gradle wrapper](https://docs.gradle.org/current/userguide/gradle_wrapper.html), slik at man ikke er avhengig av å installere Gradle lokalt.

Kjør `./gradlew [kommando]` (Unix/Mac) eller `gradlew.bat [kommando]` (Windows).

#### Nyttige kommandoer:
- `./gradlew build` - Bygger alle modulene, inkludert KMP-modulen
- `./gradlew test` - Kjører testene i rotprosjektet
- `./gradlew :filformat-kmp:allTests` - Kjører KMP-testene (JVM + JS)
- `./gradlew clean` - Renser byggekataloger
- `./gradlew :filformat-jackson:generateJacksonModel` - Genererer Jackson-modellen fra JSON Schema
- `./gradlew :filformat-kmp:generateKotlinxModel` - Genererer kotlinx-modellen fra JSON Schema
- `./gradlew :filformat-jackson:verifyGeneratedSources :filformat-kmp:verifyGeneratedSources` - Feiler hvis committede genererte kilder er ute av synk med `json/`

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
* Jackson 3 (`tools.jackson`)
* Gradle
* Swagger UI
* [JSON Schema](https://json-schema.org/)
* [KotlinPoet](https://square.github.io/kotlinpoet/) for kodegenerering fra JSON Schema (`buildSrc/`)

## Planlagte fremtidige versjoner/endringer:

* [Ny fil for data fra fagsystem for søkers innsyn](https://navikt.github.io/soknadsosialhjelp-filformat/#/data%20fra%20fagsystem/getdigisos_soker_json)
