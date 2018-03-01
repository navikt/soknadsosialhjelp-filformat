const jsf = require('json-schema-faker');
const fs = require("fs");
const dateFormat = require('dateformat');
const flatten = require('flat'); // Flattens json structures
const spawn = require('child_process').spawn; // Execute shell command

function pickRandom(myArray) {
	return myArray[Math.floor(Math.random() * myArray.length)];
}

function randomInteger(min, max) {
	return Math.floor(Math.random() * (max+1)) + min;
}

function tilfeldigGuttennavn() {
	var guttenavn = [
		"Jan", "Per", "Bjørn",
		"Ole", "Lars", "Kjell", "Knut",
		"Svein", "Thomas", "Arne"
	];
	return pickRandom(guttenavn)
}

function tilfeldigEtternavn() {
	const etternavn = [
		"Hansen", "Johansen", "Olsen", "Larsen", "Andersen",
		"Pedersen", "Nilsen", "Kristiansen", "Jensen", "Karlsen"
	];
	return pickRandom(etternavn)
}

function tilfeldigPersonNavn() {
	const navn = {
			fornavn: tilfeldigGuttennavn(),
			mellomnavn: tilfeldigGuttennavn(),
			etternavn: tilfeldigEtternavn(),
			kilde: "system"
	};
	return JSON.stringify(navn)
}
function tilfeldigArbeidsgiver() {
	const companyNames = ["Group", "Energy", "International",
		"Financial", "Holdings", "Health",
		"Services", "Insurance", "of",
		"Insurance", "Corp.", "Foods",
		"General", "Resources", "Norwegian",
		"United", "Systems", "Communications",
		"Stores", "Automotive", "Data",
		"Holding", "Norway"];
	return pickRandom(companyNames) + ' ' + pickRandom(companyNames) + pickRandom([" AS", " ASA", " NUF", ""]);
}

function randomDate() {
	const d = new Date();
	const randomDaysAgo = randomInteger(7, 60);
	d.setDate(d.getDate() - randomDaysAgo);
	return dateFormat(d, "yyyy-mm-dd");
}

function generateRandomSoknad() {
	const jsonSchemaFileName = "soknad_bundle.json";
	const schema = JSON.parse(fs.readFileSync(jsonSchemaFileName, "utf8"));

	// Generer random "lorem ipsum" data med "json-schema-faker"
	jsf.resolve(schema).then(function (result) {
		const flattened = flatten(result);

		// Rett opp feil som gjør at json schema validering gir feilmelding:
		Object.keys(flattened).forEach(function (key) {
			// const val = flattened[key];
			key = "result." + key;
			var matches = key.match(/\.(\d+)\./);
			if (matches) {
				key = key.replace("." + matches[1] + ".", "[" + matches[1] + "].");
			}
			if (key.match(/kilde$/)) {
				eval(key + " = \"" + pickRandom(["bruker", "utdatert"]) + "\"");
			}
			if (key.match(/fom/)) {
				eval(key + " = \"" + randomDate() + "\"");
			}
			if (key.match(/navn$/)) {
				eval(key + ' = ' + tilfeldigPersonNavn());
			}
			if (key.match(/arbeidsgivernavn$/)) {
				eval(key + ' = "' + tilfeldigArbeidsgiver() + '"');
			}

			if (key.match(/samvarsgrad\.verdi$/)) {
				eval(key + ' = ' + randomInteger(0,100));
			}

			// Disse feiler foreløpig ikke i json valideringen, men rettes likevel:
			if (key.match(/stillingsprosent/)) {
				eval(key + ' = ' + randomInteger(0,100));
			}

		});

		// eval("result.data.personalia.navn = {err: true}"); //  + tilfeldigPersonNavn());
		eval("result.data.personalia.navn = " + tilfeldigPersonNavn());
		eval('result.data.personalia.personIdentifikator.kilde = "system"');

		validerSoknader(result);
	});
}

function validerSoknader(randomSoknad) {
	var soknadFilename = "random_generated_soknad.json";
	fs.writeFileSync(soknadFilename, JSON.stringify(randomSoknad, null, 4));

	const validateProcess = spawn('java', [
		'-jar',
		'../target/soknadsosialhjelp-filformat-1.0-SNAPSHOT-shaded.jar', '--soknad', soknadFilename
	]);
	var errors = false;
	validateProcess.stdout.setEncoding('utf8');

	validateProcess.stdout.on('data', (chunk) => {
		console.log("stdout ==> \n" + chunk);
		errors = true;
	});

	validateProcess.stderr.on('data', (chunk) => {
		console.log("stderr ==> \n " + chunk);
		errors = true;
	});

	validateProcess.on('close', (code) => {
		if(errors === false) {
			skrivTestSoknad(randomSoknad);
		}
	});
}

function skrivTestSoknad(randomSoknad) {
	lopenummer = lopenummer + 1;
	var soknadFilename = "..\\examples\\" + lopenummer + ".json";
	console.log("Skriver testfile " + soknadFilename);
	fs.writeFileSync(soknadFilename, JSON.stringify(randomSoknad, null, 4));
}

var lopenummer = 0;
for(var i=1;i<20;i++) {
	generateRandomSoknad();
}

