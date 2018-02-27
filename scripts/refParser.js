// Dette scriptet leser json schema og ekspanderer referanser ($ref),
// så vi får en stor json fil.

var $RefParser = require('json-schema-ref-parser');
var parser = new $RefParser();

parser.$refs.paths();

process.chdir('../json/soknad/');

var output = parser.bundle("soknad.json")
.then(function(data) {
    parser.$refs.paths();
    console.log(JSON.stringify(data, null, 4));
});

