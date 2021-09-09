const express = require('express');
const session = require('express-session');

const configRoutes = require('./routes');

const app = express();
app.use(express.json()); // for json # url encoded for forms


app.use(session({
    name: 'AuthCookie',
    secret: 'some secret string!',
    resave: false,
    saveUninitialized: true
}));

configRoutes(app);

app.listen(3000, () => {
    console.log("====================================================");
    console.log('CS-554 lab-1 server is running on http://localhost:3000');
});