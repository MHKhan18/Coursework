
const express = require('express');
const router = express.Router();


router.get('/' , (req, res) => {
    try{
        const bio = {
            "name" : "Mohammad Khan",
            "cwid" : "10438167",
            "biography" : "I'm currently a 3/5 CS major at Stevens. \n My favorite sports are cricket and soccer.",
            "favoriteShows" : ["show1" , "show2" , "show3" , "show4"]
        };
        console.log('responding to myself route');
        res.json(bio);
    }catch(error){
        console.log(error);
        res.status(500).send();
    }
});


module.exports = router;