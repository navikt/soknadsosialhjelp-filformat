# Flyt
## Send klage
```mermaid
sequenceDiagram
    title Send klage
    actor klager
    participant NAV
    participant Fiks Digisos
    participant FSL
    
    klager->>NAV: POST /{fiksdigisosid}/klage/{uuid}/send
    Note right of NAV: klage.pdf
    Note right of NAV: vedlegg.pdf
    NAV->>Fiks Digisos: POST /mellomlagring/{navEksternRefId}
    Fiks Digisos->>NAV: 200 OK
    Note right of NAV: vedlegg.json
    Note right of NAV: klage.json
    NAV->>Fiks Digisos: POST /digisos/klage/api/v1/{digisosId}/{kommunenummer}/{navEksternRefId}/{klageId}
    Fiks Digisos->>NAV: 200 OK
    Fiks Digisos->>FSL: SvarUt
    FSL->>Fiks Digisos: 200 OK
    Fiks Digisos->>NAV: 200 OK
    NAV->>klager: 200 OK

```
---
## Hent klager (digisosId) - valg 1
```mermaid
sequenceDiagram
    title Hent klager på digisosId
    actor klager
    participant NAV
    participant Fiks Digisos

    klager->>NAV: GET /{fiksdigisosid}/klage
    NAV->>Fiks Digisos: GET /digisos/klage/api/v1/klager?digisosId
    Fiks Digisos->>NAV: 200 OK
    Note left of Fiks Digisos: List<MongoOriginalKlageNAV> klager <br/>
    NAV->>NAV: klager.klageDokument.dokumentlagerDokumentId
    NAV->>NAV: klager.vedlegg.dokumentlagerDokumentId
    NAV->>NAV: klager.klageMetadata
    NAV->>NAV: klager.vedleggMetadata
    NAV->>klager: 200 OK
```
---
## Hent klager (pid) - valg 2
```mermaid
sequenceDiagram
    title Hent klager på pid
    actor klager
    participant NAV
    participant Fiks Digisos

    klager->>NAV: GET /{fiksdigisosid}/klage
    NAV->>Fiks Digisos: GET /digisos/klage/api/v1/klager
    Fiks Digisos->>NAV: 200 OK
    Note left of Fiks Digisos: List<MongoOriginalKlageNAV> klager <br/>
    NAV->>NAV: klager.klageDokument.dokumentlagerDokumentId
    NAV->>NAV: klager.vedlegg.dokumentlagerDokumentId
    NAV->>NAV: klager.klageMetadata
    NAV->>NAV: klager.vedleggMetadata
    NAV->>klager: 200 OK
```
## Ettersendelse på klage
```mermaid
sequenceDiagram
    title Ettersend på klage
    actor klager
    participant NAV
    participant Fiks Digisos
    participant FSL
    
    klager->>NAV: POST /{fiksDigisosId}/klage/{klageId}/vedlegg
    NAV->>NAV: Assemble vedlegg
    Note right of NAV: ettersendelse.pdf
    NAV->>Fiks Digisos: POST /mellomlagring/{navEksternRefId}
    Fiks Digisos->>NAV: 200 OK
    Note right of NAV: vedlegg.json
    NAV->>Fiks Digisos: POST /digisos/klage/api/v1/{digisosId}/{kommunenummer}/{navEksternRefId}/{klageId}
    Fiks Digisos->>NAV: 200 OK
    Fiks Digisos->>FSL: SvarUt
    FSL->>Fiks Digisos: 200 OK
    Fiks Digisos->>NAV: 200 OK
    NAV->>klager: 200 OK
```
 
## Trekk klage
```mermaid
sequenceDiagram
    title Trekk klage
    actor klager
    participant NAV
    participant Fiks Digisos
    participant FSL
    
    klager->>NAV: POST /{fiksDigisosId}/klage/{klageId}/trekk
    Note right of NAV: trekk-klage.pdf
    NAV->>Fiks Digisos: POST /mellomlagring/{navEksternRefId}
    Fiks Digisos->>NAV: 200 OK
    note right of NAV: trekk-klage.json
    NAV->>Fiks Digisos: POST /digisos/klage/api/v1/{digisosId}/{kommunenummer}/{navEksternRefId}/{klageId}/trekk
    note right of Fiks Digisos: trukket = true
    note right of Fiks Digisos: TrekkKlageInfo
    Fiks Digisos->>Fiks Digisos: Lagre metadata
    Fiks Digisos->>FSL: SvarUt
    FSL->>Fiks Digisos: 200 OK
    Fiks Digisos->>NAV: 200 OK
    NAV->>klager: 200 OK
```
# Klasser/filformat


## Innsending av klage
```mermaid
classDiagram
    class KlageJson {
        // Id på original søknad
        +String fiksDigisosId
        
        // Referanse til klage.pdf i dokumentlager
        +String klageId
        
        /* Referanse til vedleggSpesifikasjon.json i dokumentlager
         * Her kan vi sikkert gjenbruke en god del av det som funker
        */ søknasvedlegg / ettersendelser?
        +String vedleggSpesifikasjonId
        
        // Påklaget vedtak
        +List~String~ vedtakIder
    }
```

## Eksempel:
### klage.json
```json
{
    "fiksDigisosId": "190923e4-330b-4ff7-97bd-05fe91ba2e41",
    "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
    "vedleggSpesifikasjonId": "3a3343a9-b7b9-4f83-b41c-ccd05239ae7d",
    "vedtakIder": ["77332878-8e66-420d-90f1-320a3a9fdc35", "2551f24e-df96-44c9-9b16-2614699fa1a3"]
}
```

### klage.pdf
```text
Hei, jeg klager på vedtaket deres. Jeg synes at begrunnelsen er fordi dårlig pga. ditten og datten. 
Se vedlegg som viser dette ytterligere. 

Mvh. Ola Nordmann
```

### vedlegg-spesifikasjon.json
```json
{
  "vedlegg": [
    {
      "type": "klage",
      "tilleggsinfo": "klage",
      "status": "LastetOpp",
      "filer": [
        {
          "navn": "klage.pdf",
          "dokumentlagerId": "43ec1b22-0449-4f55-bf00-6188268da3ac"
        },
        {
          "navn": "bevis.jpg",
          "dokumentlagerId": "3a3343a9-b7b9-4f83-b41c-ccd05239ae7d"
        }
      ],
      "hendelseType": null,
      "hendelseReferanse": null
    }
  ]
}
```
---

# Trekk klage
```mermaid
classDiagram
    class TrekkKlageJson {
        +String klageId
        +String vedleggSpesifikasjonId
    }
```
## Eksempel:
### trekk-klage.json
```json
{
    "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
    "vedleggSpesifikasjonId": "3a3343a9-b7b9-4f83-b41c-ccd05239ae7d",
    "timestamp": "12:34:56 1.aug.2020"
}
```

### vedlegg-spesifikasjon.json
```json
{
  "vedlegg": [
    {
      "type": "klage",
      "tilleggsinfo": "trekkKlage",
      "status": "LastetOpp",
      "filer": [
        {
          "navn": "trekk-klage.pdf",
          "dokumentlagerId": "43ec1b22-0449-4f55-bf00-6188268da3ac"
        }
      ],
      "hendelseType": null,
      "hendelseReferanse": null
    }
  ]
}

```

### trekk-klage.pdf
````text
Ola Nordmann trekker herved klagen sin. Handlingen ble utført på nav.no kl 13:37 den 13. august 2021.
```` 

---
# Hendelser klage
```mermaid
classDiagram
    JsonDigisosSoker --* JsonKlage: has
    class JsonDigisosSoker {
        +JsonAvsender avsender
        +List~JsonHendelse~ hendelser
        +List~JsonKlage~ klager
    }

    JsonKlage --* KlageHendelse: has
    class JsonKlage {
        +String klageId
        +String tittel
        +List~KlageHendelse~ hendelser
    }

    class KlageHendelse {
        +String hendelseId
        +String hendelseType
        +String hendelseTekst
        +String hendelseDato
    }

    UtfallHendelse ..|> KlageHendelse: extends
    class UtfallHendelse {
        +UtfallEnum utfall
    }

    Utfall *-- UtfallHendelse: has
    class Utfall {
        MEDHOLD,
        // Ikke alle FSL vil sette denne
        DELVIS_MEDHOLD,
        OPPRETTHOLDT,
        AVVIST
    }

    KlageStatusHendelse ..|> KlageHendelse: extends
    KlageStatus *-- KlageStatusHendelse: has
    class KlageStatusHendelse {
        +Status: status
    }

    note for KlageStatus "Nye statuser for klage"
    class KlageStatus {
        SENDT,
        MOTTATT,
        UNDER_BEHANDLING,
        TRUKKET,
        FERDIGBEHANDLET,
        VIDERESENDT,
        FORELOPIG_SVAR
    }

    note for StatsforvalterUtfallHendelse "Denne er avhengig av manuelle rutiner,</br> og vil ikke alltid bli registrert"
    StatsforvalterUtfallHendelse ..|> KlageHendelse: extends
    class StatsforvalterUtfallHendelse {
        +StatsforvalterUtfall utfall
    }

    StatsforvalterUtfall *-- StatsforvalterUtfallHendelse: has
    class StatsforvalterUtfall {
        STADFESTER,
        NYTT_VEDTAK,
        AVVIST,
        OPPHEVER
    }

    KlageDokumentasjonHendelse ..|> KlageHendelse: extends
    class KlageDokumentasjonHendelse {
        +Forvaltningsbrev forvaltningsbrev
        +List~JsonVedlegg~ vedlegg
        +List~Dokument~ dokumenter
        +DokumentasjonStatus status
    }

    Dokument *-- KlageDokumentasjonHendelse: has
    class Dokument {
        +String dokumenttype
        +String tilleggsinformasjon
        +String innsendelsesfrist
        +String dokumentreferanse
    }

    DokumentasjonStatus *-- KlageDokumentasjonHendelse: has
    class DokumentasjonStatus {
        RELEVANT,
        LEVERT_TIDLIGERE,
        ANNULLERT
    }

    KlageHendelse <|-- SaksfremleggHendelse: extends
    class SaksfremleggHendelse {
        +String saksreferanse
        +String dokumentasjonsreferanse
        +String kommentarfrist
    }
```

## Eksempler:
### Klage mottatt
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageStatusHendelse",
      "hendelseTekst": "Klage mottatt",
      "hendelseDato": "2021-08-13T13:37:00",
      "status": "MOTTATT"
    }
  ]
}
```

### Utfall registrert (medhold)
```json
{
    "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
    "tittel": "Klage på vedtak",
    "hendelser": [
        {
            "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
            "hendelseType": "UtfallHendelse",
            "hendelseTekst": "Medhold i klage",
            "hendelseDato": "2021-08-13T13:37:00",
            "utfall": "MEDHOLD"
        },
      {
        "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
        "hendelseType": "JsonVedtakFattet",
        "hendelseTekst": "Nytt vedtak fattet",
        "hendelseDato": "2021-08-13T13:37:00",
        "saksreferanse": "123456789",
        "utfall": "INNVILGET",
        "vedtaksfil": {
          "filreferanse": {
            "type": "DOKUMENTLAGER",
            "id": "776a8991-d778-4d77-828c-dd1126cf6e95"
          }
        },
        "vedlegg": []
      },
      {
        "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
        "hendelseType": "KlageStatusHendelse",
        "hendelseTekst": "Klage ferdigbehandlet",
        "hendelseDato": "2021-08-13T13:37:00",
        "status": "FERDIGBEHANDLET"
      }
    ]
}
```

### Utfall registrert (ikke medhold)
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "UtfallHendelse",
      "hendelseTekst": "Vedtak opprettholdt",
      "hendelseDato": "2021-08-13T13:37:00",
      "utfall": "OPPRETTHOLDT"
    },
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageStatusHendelse",
      "hendelseTekst": "Klage videresendt til statsforvalter",
      "hendelseDato": "2021-08-13T13:37:00",
      "status": "VIDERESENDT"
    }
  ]
}
```

#### Når statsforvalter er ferdig med klagen:
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "StatsforvalterUtfallHendelse",
      "hendelseTekst": "Statsforvalter stadfester vedtaket",
      "hendelseDato": "2021-08-13T13:37:00",
      "utfall": "STADFESTER"
    },
    {
        "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
        "hendelseType": "KlageStatusHendelse",
        "hendelseTekst": "Klage ferdigbehandlet",
        "hendelseDato": "2021-08-13T13:37:00",
        "status": "FERDIGBEHANDLET"
    }
  ]
}
```

### Utfall registrert (ikke medhold, og statsforvalter fatter nytt vedtak)
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "UtfallHendelse",
      "hendelseTekst": "Vedtak opprettholdt",
      "hendelseDato": "2021-08-13T13:37:00",
      "utfall": "OPPRETTHOLDT"
    },
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageStatusHendelse",
      "hendelseTekst": "Klage videresendt til statsforvalter",
      "hendelseDato": "2021-08-13T13:37:00",
      "status": "VIDERESENDT"
    }
  ]
}
```

#### Når klage kommer tilbake fra statsforvalter:

```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "StatsforvalterUtfallHendelse",
      "hendelseTekst": "Statsforvalter har fattet nytt vedtak",
      "hendelseDato": "2021-08-13T13:37:00",
      "utfall": "NYTT_VEDTAK"
    },
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "JsonVedtakFattet",
      "hendelseTekst": "Nytt vedtak fattet",
      "hendelseDato": "2021-08-13T13:37:00",
      "saksreferanse": "123456789",
      "utfall": "INNVILGET",
      "vedtaksfil": {
        "filreferanse": {
          "type": "DOKUMENTLAGER",
          "id": "776a8991-d778-4d77-828c-dd1126cf6e94"
        }
      },
      "vedlegg": []
    },
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageStatusHendelse",
      "hendelseTekst": "Klage ferdigbehandlet",
      "hendelseDato": "2021-08-13T13:37:00",
      "status": "FERDIGBEHANDLET"
    }
  ]
}
```

### Klage trukket (Bekreftelse på mottatt forsendelse fra NAV)
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageStatusHendelse",
      "hendelseTekst": "Klage trukket",
      "hendelseDato": "2021-08-13T13:37:00",
      "status": "TRUKKET"
    }
  ]
}
```

### Klagedokumentasjon etterspurt
```json
{
  "klageId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
  "tittel": "Klage på vedtak",
  "hendelser": [
    {
      "hendelseId": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "hendelseType": "KlageDokumentasjonHendelse",
      "hendelseTekst": "Dokumentasjon vedrørende klagen er etterspurt",
      "hendelseDato": "2021-08-13T13:37:00",
      "klagedokumentasjonReferanse": "4c68bce6-c392-4f2c-a858-9c4ad626a3d5",
      "klageReferanse": "43ec1b22-0449-4f55-bf00-6188268da3ac",
      "tittel": "Dokumentasjon av klage",
      "beskrivelse": "Vi trenger mer dokumentasjon for å kunne behandle klagen din",
      "frist": "2021-08-20T13:37:00",
      "status": "RELEVANT"
    }
  ]
}
```
