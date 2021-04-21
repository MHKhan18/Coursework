
const auth = require('./auth');

const constructorMethod = (app) => {


    app.use('/', auth);

    app.use('*', (req, res) => {
        res.status(404).json({ error: 'Not found' });
    });
};

module.exports = constructorMethod;