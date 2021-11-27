const express = require('express');
const configRoutes = require('./routes');

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));

configRoutes(app);

app.listen(3000, () => {
    console.log('CS-554 lab-07 server is running on http://localhost:3000');
});