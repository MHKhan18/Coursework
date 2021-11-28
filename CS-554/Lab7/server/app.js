const express = require('express');
const configRoutes = require('./routes');
const cors = require('cors')

const app = express();

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors())

configRoutes(app);

app.listen(4000, () => {
    console.log('CS-554 lab-07 server is running on http://localhost:4000');
});