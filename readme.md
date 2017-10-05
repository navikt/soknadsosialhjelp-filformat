XSD for søknad om økonomisk sosialhjelp
=======================================

**OBS: Fortsatt under arbeid, det kan og vil komme justeringer frem til v1 er endelig**

Eventuelle endringer vil dokumenteres lenger ned. Etter at v1 er lansert vil kompatabilitet etterstreves i mye større grad.

Designkriterier
----------

1. Forward compatibility. At v1 konsumenter kan ta i mot v2 dokumenter, da ikke alle kommuner/fagsystemer kan oppdateres samtidig
2. Endringsevne. Kunne jobbe smidig med søknaden med en MVP tilnærming der vi produksjonssetter tidlig og oppdaterer hyppig basert på tilbakemeldinger.
3. Kilde. Vite om data er oppgitt av bruker eller innhentet fra systemer.
4. Spørsmål er ikke påkrevd å svare på

Designvalg
----------

For å kunne gjøre til tider store endringer på xml underveis og fortsatt validere for eldre konsumenter (krav 1 og 2), er i hovedsak to ting gjort:
* Bruk av `xs:any` i de fleste `complexType`s. Det gjør at vi kan legge til nye elementer i fremtiden, uten at det knekker validering mot eldre versjoner av xsd.
Dette fungerer tort sett greit, men på grunn av "unique particle attribution" begrenser det hvilke felter som kan være optional, og i tilfeller vi ønsker
vilkårlig antall elementer må det wrappes i et ekstra objekt.

* Ting er stringly typed i stedet for bruk av enums. Dette fordi vi ikke kan sende xml med nye enumverdier til eldre konsumenter, da de ikke kan mappe det til noe.
For at det fortsatt skal være mulig å vite hvilke verdier som kan komme, inkluderer vi alle enums som et "kodeverk", vi bruker bare ikke de som typer direkte.
For konsumenter betyr det at `Kilde.BRUKER.value().equals(verdiFraXml)` er ok, mens `Kilde.fromValue(verdifraXml)` vil kunne feile om verdien fra xml er nyere enn Kilde i xsden som ble brukt
for å lage konsumenten.

* En annen ting som er gjort mtp endringsevne er at xmlen ikke matcher det bruker svarer i søknaden fult ut, men heller *intensjonen* bak svarene.
F. eks. kan vi muligens stille ja/nei spørsmål for å få lettere interaksjon, uten at akkurat det svaret blir med, men heller oppfølgingsspørsmålene som kommer av det svaret. 
*Her blir det troligvis en del endringer frem til endelig versjon*.

Da ting ikke er påkrevd å svare på har vi gått bort fra å bruke booleans, da vi nå kan ha true/false/ikkesvart/osv.
For integers kan vi heller ikke bruke `xs:integer`, så bruker nå en egen type som også kan være tom-

CHANGELOG
---------


### Endringer underveis før v1
* Booleans er endret til strings, som kan få verdi "true", "false" eller ingenting foreløbig.
* Integers er egen stringtype som bare kan være ints, men også tom
* true/false på en del spørsmål, da vi ikke lenger krever bruker
* bosituasjon er ikke lenger en liste
* bosituasjon spør vi bare om antall personer
* fjernet listen over bostøtte
* samværsgrad har blitt endret fra heltid/deltid til integer (prosent)
* fjernet navytelser
* bruker kan ønske annen utbetaling enn kontonr

Ikke alt er på plass for v1 enda, ting vi vet kommer:
* Adresser/adressetyper
* Arbeidsforhold
* Noen nye ja/nei svar da underspørsmålene ikke blir påkrevd
* Opplysninger-delen er fortsatt litt usikker, kom gjerne med tilbakemeldinger