const express = require('express');
const configRoutes = require('./routes');

const app = express();
const static = express.static(__dirname + '/public');

app.use('/public', static);

configRoutes(app);

app.listen(3000, () => {
    console.log('CS-554 Lab-2 server is  running on http://localhost:3000');
});
