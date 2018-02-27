Scripts for generering av testdata
==================================

Scripts for å generere syntetiske testsøknader.

# 1. Slå sammen json schema filene til en schema fil

```
$ node refParser.json > soknad_bundle.json
```

# 2. Generer søknad med "lorem ipsum" data

```
$ node fake_data.js
```

# 3. Valider den genererte søknaden

```
$ node fake_data.js > slettmeg.tmp ; cat slettmeg.tmp | java -cp 'C:\Users\F150282\.m2\repository\junit\junit\4.12\junit-4.12.jar;C:\Users\F150282\.m2\repository\org\hamcrest\hamcrest-core\1.3\hamcrest-core-1.3.jar;C:\Users\F150282\.m2\repository\com\github\fge\json-schema-validator\2.2.6\json-schema-validator-2.2.6.jar;C:\Users\F150282\.m2\repository\com\google\code\findbugs\jsr305\3.0.0\jsr305-3.0.0.jar;C:\Users\F150282\.m2\repository\joda-time\joda-time\2.3\joda-time-2.3.jar;C:\Users\F150282\.m2\repository\com\googlecode\libphonenumber\libphonenumber\6.2\libphonenumber-6.2.jar;C:\Users\F150282\.m2\repository\javax\mail\mailapi\1.4.3\mailapi-1.4.3.jar;C:\Users\F150282\.m2\repository\javax\activation\activation\1.1\activation-1.1.jar;C:\Users\F150282\.m2\repository\net\sf\jopt-simple\jopt-simple\4.6\jopt-simple-4.6.jar;C:\Users\F150282\.m2\repository\com\github\fge\json-schema-core\1.2.5\json-schema-core-1.2.5.jar;C:\Users\F150282\.m2\repository\com\github\fge\uri-template\0.9\uri-template-0.9.jar;C:\Users\F150282\.m2\repository\org\mozilla\rhino\1.7R4\rhino-1.7R4.jar;C:\Users\F150282\.m2\repository\com\github\fge\jackson-coreutils\1.8\jackson-coreutils-1.8.jar;C:\Users\F150282\.m2\repository\com\google\guava\guava\16.0.1\guava-16.0.1.jar;C:\Users\F150282\.m2\repository\com\github\fge\msg-simple\1.1\msg-simple-1.1.jar;C:\Users\F150282\.m2\repository\com\github\fge\btf\1.2\btf-1.2.jar;C:\Users\F150282\.m2\repository\com\fasterxml\jackson\core\jackson-databind\2.2.3\jackson-databind-2.2.3.jar;C:\Users\F150282\.m2\repository\com\fasterxml\jackson\core\jackson-annotations\2.2.3\jackson-annotations-2.2.3.jar;C:\Users\F150282\.m2\repository\com\fasterxml\jackson\core\jackson-core\2.2.3\jackson-core-2.2.3.jar;C:\Users\F150282\workspace\soknadsosialhjelp-filformat\target\soknadsosialhjelp-filformat-1.0-SNAPSHOT.jar' no.nav.sbl.soknadsosialhjelp.json.JsonSosialhjelpValidator && echo OK || echo FAIL
```

