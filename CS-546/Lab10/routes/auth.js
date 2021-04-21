const express = require('express');
const router = express.Router();
const bcrypt = require('bcryptjs');
const users = require('../data/users');

router.get('/', async (req, res) => {

    if (req.session.user) { // user is authenticated
        res.redirect('/private');
        return ;
    }else {
        res.render('auth/login' , {error: false });
        return;
    }
    
});

router.post('/login', async (req, res) => {

    const { username, password } = req.body; // guaranteed to be received due to required on front end
    let match = false;
    let matchedUser;
    for(let user of users){
        if (user.username === username){
            match = await bcrypt.compare(password, user.hashedPassword);
            matchedUser = user;
            break;
        }
    }
    if (match){
        req.session.user = { firstName: matchedUser.firstName, lastName: matchedUser.lastName, userId: matchedUser._id };
        res.redirect('/private');
        return ; 
    }else{
        res.status(401);
        res.render('auth/login' , {error: true , error_message:'Please provide valid username and password.'});
        return ;
    }
});

router.get('/private', async (req, res) => {

    // assume authenticated
    let curUser;
    for(let user of users){
        if(user._id === req.session.user.userId){
            curUser = user;
            break;
        }
    }
    res.render('auth/details' , {
        username: curUser.username,
        firstName: curUser.firstName,
        lastName: curUser.lastName,
        profession: curUser.profession,
        bio: curUser.bio
    });
    return ;
    
});

router.get('/logout', async (req, res) => {
    req.session.destroy();
    res.render('auth/logout');
    return;
});

module.exports = router;

