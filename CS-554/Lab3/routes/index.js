const axios = require('axios');


const constructorMethod = (app) => {

    app.get('/', async (req, res) => {

        try {
            const { data } = await axios.get("http://api.tvmaze.com/shows");

            if (data.length === 0) {
                console.log("no data");
                res.status(404);
                res.render('shows/error');
                return;
            }

            let shows = [];
            for (let i = 0; i < data.length; i++) {
                shows.push({ id: data[i].id, name: data[i].name });
            }

            let context = {
                shows: shows
            };

            res.render('shows/showsList', context);

        } catch (e) {
            res.status(500);
            res.render('shows/error');
        }

    });

    app.get('/show/:id', async (req, res) => {

        const showId = Number(req.params.id);
        if (!(Number.isInteger(showId)) || showId < 0) {
            res.status(400);
            res.render('shows/error');
            return;
        }
        try {
            const { data } = await axios.get(`http://api.tvmaze.com/shows/${showId}`);

            if (data.length === 0) {
                res.status(404);
                res.render('shows/error');
                return;
            }

            let summary = data.summary;
            summary = summary.replace(/(<([^>]+)>)/gi, ""); // sanitizing

            const context = {
                title: data.name,
                image: data.image.medium,
                language: data.language,
                genres: data.genres,
                rating: data.rating.average,
                network: data.network.name,
                summary: summary
            };

            res.render('shows/showDetails', context);

        } catch (e) {
            res.status(500);
            res.render('shows/error');
        }

    });

    app.post('/search', async (req, res) => {

        const userQuery = req.body.searchTerm;


        if (userQuery === undefined || userQuery.trim().length == 0) {
            res.status(400);
            res.render('shows/error');
            return;
        }

        try {
            const { data } = await axios.get(`http://api.tvmaze.com/search/shows?q=${userQuery}`);


            if (data.length === 0) {
                res.status(404);
                res.render('shows/error');
                return;
            }

            let shows = [];
            for (let i = 0; i < data.length; i++) {
                shows.push({ id: data[i].show.id, name: data[i].show.name });
            }

            let context = {
                shows: shows
            };

            res.render('shows/showsList', context);

        } catch (e) {
            res.status(500);
            res.render('shows/error');
        }

    });

    app.get('/popularsearches', async (req, res) => {

    });

    app.get('*', (req, res) => {
        res.status(404).json({ error: 'Not found' });
    });
};

module.exports = constructorMethod;
