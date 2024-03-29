const path = require('path');

const constructorMethod = (app) => {
    app.use('/', (req, res) => {
        res.sendFile(path.resolve('static/main.html'));
      });
  
    app.use('*', (req, res) => {
      res.redirect('/');
    });
  };
  
module.exports = constructorMethod;
  