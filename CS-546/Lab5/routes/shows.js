
const express = require('express');
const router = express.Router();
const axios = require('axios');



router.get('/' , async (req, res) => {
    try{
        const {data} = await axios.get('http://api.tvmaze.com/shows'); 
        console.log('responding to request for all shows');
        res.send(data);
    } catch(e){
        console.log(e);
        console.error('API call failed.');
        res.status(500).send();
    }
});

router.get('/:id' , async (req , res) => {

    const showId = Number(req.params.id);
    if ( !(Number.isInteger(showId)) || showId < 0){
        console.log(`Invalid request for show with id: ${req.params.id}`);
        res.status(404).json({ message: 'id must be a non-negative whole number.'});
    }else{
        try{
            const {data} = await axios.get(`http://api.tvmaze.com/shows/${showId}`); 
            console.log(`responding to request for show id : ${req.params.id}`);
            res.json(data);
        }catch (e) {
            console.log(`no data found for requested show id : ${req.params.id}`);
            res.status(404).json({ message: `No show found with id ${req.params.id}` });
        }
    }
});


module.exports = router;