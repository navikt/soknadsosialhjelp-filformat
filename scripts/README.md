Scripts for generering av testdata
==================================

Scripts for å generere syntetiske testsøknader.

## 1. Slå sammen json schema filene til en schema fil

```
$ node refParser.js > soknad_bundle.json
```

## 2. Generer søknad med "lorem ipsum" data

```
$ node generer_testsoknader.js
```

**Note:** Make sure to run `gradlew build` first to generate the required Java classes. 
