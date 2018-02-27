const fs = require("fs");
const path = require("path");

function jsonReadAndParse(fileName) {
    return JSON.parse(fs.readFileSync(fileName, "utf8"));
}

function process(key,value) {
    console.log(key + " : "+value);
    if ( key === '$ref' ) {

	var fileName = path.join("../json/soknad/",value);
	console.log("===> fileName : " + fileName);
	var parts = jsonReadAndParse(fileName)
	// process.exit(0);
    }
}

function traverse(o,func) {
    for (var i in o) {
        func.apply(this,[i,o[i]]);  
        if (o[i] !== null && typeof(o[i])=="object") {
            // Recurse one step down in the object tree
            traverse(o[i],func);
        }
    }
}

console.log("*** Start ***");
    
var fileName = "../json/soknad/soknad.json";
var jsonSpec = JSON.parse(fs.readFileSync(fileName, "utf8"));
    
traverse(jsonSpec, process);
