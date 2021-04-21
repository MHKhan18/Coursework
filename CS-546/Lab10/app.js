const express = require('express');
const app = express();

const configRoutes = require('./routes');
const exphbs = require('express-handlebars');
const session = require('express-session');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));




app.engine('handlebars', exphbs({ defaultLayout: 'main' }));
app.set('view engine', 'handlebars');

app.use(session({
    name: 'AuthCookie',
    secret: 'some secret string!',
    resave: false,
    saveUninitialized: true
  }));

app.use(async (req, res, next) => {
    let authStatus;
    if(req.session.user){
        authStatus = '(Authenticated User)';
    }else{
        authStatus = '(Non-Authenticated User)';
    }
    const display = `[${new Date().toUTCString()}]: ${req.method} ${req.originalUrl} ${authStatus}`;
    console.log(display);
    next();
});


app.use('/private', (req, res, next) => {
    if (!req.session.user) {
        res.status(403);
        res.render('auth/error');
        return; 
    } else {
      next();
    }
  });

configRoutes(app);

app.listen(3000, () => {
  console.log('CS-546 lab-10 server is running on http://localhost:3000');
});