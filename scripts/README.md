Scripts for generering av testdata
==================================

Scripts for å generere syntetiske testsøknader.

## 1. Slå sammen json schema filene til en schema fil

```
$ node refParser.json > soknad_bundle.json
```

## 2. Generer søknad med "lorem ipsum" data

```
$ node generer_testsoknader.js
```
