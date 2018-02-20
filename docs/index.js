const express = require('express');
//const pathToSwaggerUi = require('swagger-ui-dist').absolutePath();

const app = express();

app.use(express.static('.'));
app.use(express.static('../json'));
app.listen(3000, 'localhost', function() {
    console.log("Running on http://localhost:3000");
});
