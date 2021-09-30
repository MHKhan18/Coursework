const express = require('express');
const exphbs = require('express-handlebars');
const configRoutes = require('./routes');


const app = express();
const static = express.static(__dirname + '/public');

app.use('/public', static);
app.use(express.json());
app.use(express.urlencoded({ extended: true }));

app.engine('handlebars', exphbs({ defaultLayout: 'main' }));
app.set('view engine', 'handlebars');

configRoutes(app);

app.listen(3000, () => {
    console.log('CS-554 lab-03 server is running on http://localhost:3000');
});