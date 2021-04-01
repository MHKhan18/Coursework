const finderRoutes = require('./finder');

const constructorMethod = (app) => {
    app.use('/', finderRoutes);
    
    app.use('*', (req, res) => {
        res.status(404).json({ error: 'Not found' });
    });
};

module.exports = constructorMethod;
