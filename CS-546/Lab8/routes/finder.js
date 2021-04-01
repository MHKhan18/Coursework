const express = require('express');
const router = express.Router();
const axios = require('axios');


// display form
router.get('/', async (req, res) => {
    res.render('shows/form' , {title: "Show Finder"});
});

// form submission
router.post('/search', async (req, res) => {

    const userQuery = req.body.searchTerm;

    if(userQuery === undefined || userQuery.trim().length == 0){
        res.status(400);
        res.render('shows/error' , {class: 'error' , error_message:'No input received.'});
        return ;
    }

    try {
        const {data} = await axios.get(`http://api.tvmaze.com/search/shows?q=${userQuery}`);

        if(data.length === 0){
            res.status(404);
            res.render('shows/error' , {class: 'not-found' , error_message: `We're sorry, but no results were found for ${userQuery}.`});
            return ;
        }
        
        let shows = [];
        const limit = Math.min(data.length , 20);
        for(let i = 0; i < limit; i++){
            shows.push({id: data[i].show.id , name: data[i].show.name});
        }

        let context = {
            title : "Shows Found",
            query : userQuery,
            shows : shows
        };

        res.render('shows/search' , context);
      
    } catch (e) {
        res.status(500); 
        res.render('shows/error' , {class: 'error' , error_message:'Server failed to process your request.'});
    }
});

// display show details
router.get('/shows/:id', async (req, res) => {

    const showId = Number(req.params.id);
    if (!(Number.isInteger(showId)) || showId < 0){
        res.status(400);
        res.render('shows/error' , {class: 'error' , error_message:'id must be a positive integer.'});
        return ;
    }
    try {
        const {data} = await axios.get(`http://api.tvmaze.com/shows/${showId}`); 

        if(data.length === 0){
            res.status(404);
            res.render('shows/error' , {class: 'not-found' , error_message: `We're sorry, but no show found with id ${showId}.`});
            return ;
        }

        let summary = data.summary;
        summary = summary.replace(/(<([^>]+)>)/gi, "");

        const context = {
            title : data.name,
            image : data.image.medium,
            language : data.language,
            genres : data.genres,
            rating : data.rating.average,
            network : data.network.name,
            summary : summary
        };

        res.render('shows/show' , context);
        
    } catch (e) {
        res.status(500); 
        res.render('shows/error' , {class: 'error' , error_message:'Server failed to process your request.'});
    }
});


module.exports = router;
