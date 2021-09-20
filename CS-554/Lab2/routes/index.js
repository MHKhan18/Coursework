const path = require('path');

const constructorMethod = (app) => {
    app.get('/', (req, res) => {
        res.sendFile(path.resolve('static/main.html'));
    });

    app.get('*', (req, res) => {
        res.status(404).sendFile(path.resolve('static/error.html'));
    });
};

module.exports = constructorMethod;
