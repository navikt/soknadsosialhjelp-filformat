const jsf = require('json-schema-faker');
const fs = require("fs");

const fileName = "soknad_bundle.json";
// const fileName = "navn.json";
const schema = JSON.parse(fs.readFileSync(fileName, "utf8"));

jsf.resolve(schema).then(function(result) {
  console.log(JSON.stringify(result, null, 2));
});
