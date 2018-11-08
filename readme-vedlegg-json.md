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
      "status": "LastetOpp",
      "filer": [
        {"filnavn": "endaenfil-u95sdfght.png"},
        {"filnavn": "DCIM6666-ae94343bc.jpg"}
      ]
    },
    {
      "type": "faktura",
      "tilleggsinfo": "tannregulering",
      "status": "LastetOpp",
      "filer": [
        {"filnavn": "yeyeye-u95sdfght.png"},
        {"filnavn": "alalla-ae94343bc.jpg"}
      ]
    },
    {
      "type": "faktura",
      "tilleggsinfo": "oppvarming",
      "status": "VedleggAlleredeSendt",
      "filer": []
    }
  ]
}
```

Kort oppsummert har hvert vedlegg en "type", samt "tilleggsinfo" som gir litt mer kontekst. Vedleggene har også en status som sier om vedlegget allerede er sendt, eller om det er lastet opp. Deretter er det en liste over filnavn som bruker har lagt ved. Hvis vedlegget har status "VedleggAlleredeSendt" eller "VedleggKreves" så vil listen over filer være tom. 

Det vil bli publisert en liste over mulige typer, men mottakere må uansett støtte å motta ukjente type/tilleggsinfo-kombinasjoner.

[Se fullstendig dokumentasjon av vedlegg](https://navikt.github.io/soknadsosialhjelp-filformat/#/vedlegg/getvedlegg_json)
