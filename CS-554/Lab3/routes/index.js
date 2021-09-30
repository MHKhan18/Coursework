const axios = require('axios');
const bluebird = require('bluebird');
const redis = require('redis');
const flat = require('flat');

const unflatten = flat.unflatten;
const client = redis.createClient();


bluebird.promisifyAll(redis.RedisClient.prototype);
bluebird.promisifyAll(redis.Multi.prototype);


const constructorMethod = (app) => {

    // middleware to check cache
    app.get('/', async (req, res, next) => {
        let cacheForHomePageExists = await client.getAsync('homePage');
        if (cacheForHomePageExists) {
            console.log("sending homepage from cache")
            res.send(cacheForHomePageExists);
        } else {
            next();
        }
    });

    app.get('/', async (req, res) => {
        try {
            const { data } = await axios.get("http://api.tvmaze.com/shows");

            if (data.length === 0) {
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

            res.render('shows/showsList',
                context,
                async function (err, html) {
                    if (err) {
                        res.status(404);
                        res.render('shows/error');
                        return;
                    }
                    else { // add to cache
                        let _ = await client.setAsync(
                            'homePage',
                            html
                        );
                        console.log("set homepage entry in redis");
                        res.send(html);
                    }
                }
            );

        } catch (e) {
            res.status(500);
            res.render('shows/error');
        }
    });

    // middleware to check cache
    app.get('/show/:id', async (req, res, next) => {

        const showId = Number(req.params.id);
        if (!(Number.isInteger(showId)) || showId < 0) {
            res.status(404);
            res.render('shows/error');
            return;
        }

        const cachedDetailsPage = await client.hgetAsync('detailsPage', showId);
        if (cachedDetailsPage) {
            res.send(cachedDetailsPage);
            console.log('Sent cached details page');
        } else {
            next();
        }
    });

    app.get('/show/:id', async (req, res) => {

        const showId = Number(req.params.id);
        if (!(Number.isInteger(showId)) || showId < 0) {
            res.status(404);
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

            res.render('shows/showDetails',
                context,
                async function (err, html) {
                    if (err) {
                        res.status(404);
                        res.render('shows/error');
                        return;
                    }
                    else { // add to cache

                        const _ = await client.hsetAsync(
                            'detailsPage',
                            showId,
                            html
                        );
                        console.log("adding show details to cache");
                        res.send(html);
                    }
                }
            );

        } catch (e) {
            res.status(500);
            res.render('shows/error');
        }

    });

    // middleware to check cache
    app.post('/search', async (req, res, next) => {

        let userQuery = req.body.searchTerm;

        if (userQuery === undefined || userQuery.trim().length == 0) {
            res.status(400);
            res.render('shows/error');
            return;
        }

        userQuery = userQuery.toLowerCase().trim();

        const cachedSearchDetails = await client.hgetAsync('searchResults', userQuery);
        if (cachedSearchDetails) {
            res.send(cachedSearchDetails);
            console.log('Sent cached search result');
        } else {
            next();
        }


    });


    app.post('/search', async (req, res) => {

        let userQuery = req.body.searchTerm;

        if (userQuery === undefined || userQuery.trim().length == 0) {
            res.status(400);
            res.render('shows/error');
            return;
        }

        userQuery = userQuery.toLowerCase().trim();

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

            res.render('shows/showsList',
                context,
                async function (err, html) {
                    if (err) {
                        res.status(404);
                        res.render('shows/error');
                        return;
                    }
                    else { // add to cache

                        const _ = await client.hsetAsync(
                            'searchResults',
                            userQuery,
                            html
                        );
                        console.log("adding search results to cache");
                        res.send(html);
                    }
                }
            );

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
