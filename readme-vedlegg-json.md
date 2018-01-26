Vedlegg for søknad om økonomisk sosialhjelp
===========================================

Vedlegg kommer som egne filer i meldingen. Spesifikasjonen over hvilke vedlegg som er hva ligger i en JSON-fil i meldingen, ved navn
`vedlegg.json`. Den er på dette formatet:

```json
{
  "vedlegg": [
    {
      "type": "faktura",
      "tilleggsinfo": "sfo",
      "filer": [
        {"filnavn": "endaenfil-u95sdfght.png"},
        {"filnavn": "DCIM6666-ae94343bc.jpg"}
      ]
    },
    {
      "type": "faktura",
      "tilleggsinfo": "tannregulering",
      "filer": [
        {"filnavn": "yeyeye-u95sdfght.png"},
        {"filnavn": "alalla-ae94343bc.jpg"}
      ]
    }
  ]
}
```

Kort oppsummert har hvert vedlegg en "type", samt "tilleggsinfo" som gir litt mer kontekst. Deretter er det en liste over filnavn som bruker har lagt ved.

Det vil bli publisert en liste over mulige typer, men mottakere må uansett støtte å motta ukjente type/tilleggsinfo-kombinasjoner.

[Se fullstendig dokumentasjon av vedlegg](https://raw.githubusercontent.com/navikt/soknadsosialhjelp-filformat/master/json/vedlegg/vedleggSpesifikasjon.json)
