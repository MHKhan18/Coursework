const axios = require('axios');
const bluebird = require('bluebird');
const redis = require('redis');

const client = redis.createClient();


bluebird.promisifyAll(redis.RedisClient.prototype);
bluebird.promisifyAll(redis.Multi.prototype);

const PER_PAGE = 20;
const PAGES_KEY = 'pages';
const POK_KEY = 'pokemons';

const constructorMethod = (app) => {


    // middleware to check cache for page
    app.get('/pokemon/page/:page', async (req, res, next) => {
        const page = req.params.page;
        if (!page || typeof page !== 'string' || page.trim().length === 0) {
            res.status(400).json({ error: 'page is in invalid format' });
            return;
        }

        let cachedPage = await client.hgetAsync(PAGES_KEY, page);
        if (cachedPage) {
            console.log(`Sending page ${page} from cache`);
            res.json(JSON.parse(cachedPage));
        } else {
            next();
        }

    });

    app.get('/pokemon/page/:page', async (req, res) => {

        const page = req.params.page;
        if (!page || typeof page !== 'string' || page.trim().length === 0) {
            res.status(400).json({ error: 'page is in invalid format' });
            return;
        }
        const offset = parseInt(page) * PER_PAGE;

        try {
            const { data } = await axios.get(`https://pokeapi.co/api/v2/pokemon?offset=${offset}&limit=${PER_PAGE}`);

            if (data.results.length === 0) {
                res.status(404);
                res.send(`No more Pokemons`);
                return;
            }

            // add to cache
            await client.hsetAsync(PAGES_KEY, page, JSON.stringify(data));
            res.json(data);

        } catch (e) {
            res.status(404); // API Broke
            res.send(`Page: ${page} returned error.`);
        }


    });

    // middleware to check cache for id
    app.get('/pokemon/:id', async (req, res, next) => {

        const id = req.params.id;
        if (!id || typeof id !== 'string' || id.trim().length === 0) {
            res.status(400).json({ error: 'id is in invalid format' });
            return;
        }

        let cachedPokemon = await client.hgetAsync(POK_KEY, id);

        if (cachedPokemon) {
            console.log(`Sending id ${id} from cache`);
            res.json(JSON.parse(cachedPokemon));
        } else {
            next();
        }


    });

    app.get('/pokemon/:id', async (req, res) => {

        const id = req.params.id;
        if (!id || typeof id !== 'string' || id.trim().length === 0) {
            res.status(400).json({ error: 'id is in invalid format' });
            return;
        }

        try {
            const { data } = await axios.get(`https://pokeapi.co/api/v2/pokemon/${id}`);

            // add to cache
            await client.hsetAsync(POK_KEY, id, JSON.stringify(data));
            res.json(data);
        } catch (e) {
            res.status(404); // API Broke
            res.send(`ID: ${id} returned error.`);
        }
    });

    app.get('*', (req, res) => {
        res.status(404).json({ error: 'Not found' });
    });
}


module.exports = constructorMethod;