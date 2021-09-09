const express = require('express');
const bcrypt = require('bcryptjs');

let { ObjectId } = require('mongodb');
const data = require('../data');

const router = express.Router();
const userData = data.users;

const saltRounds = 16;


router.post('/signup', async (req, res) => {

    const userInfo = req.body;

    const name = userInfo.name;
    const userName = userInfo.username;
    const plainTextPswd = userInfo.password;

    if (!name || typeof name !== 'string' || name.trim().length === 0) {
        res.status(400).json({ error: 'name is in invalid format' });
        return;
    }

    if (!userName || typeof userName !== 'string' || userName.trim().length === 0) {
        res.status(400).json({ error: 'userName is in invalid format' });
        return;
    }

    if (!plainTextPswd || typeof plainTextPswd !== 'string' || plainTextPswd.trim().length === 0) {
        res.status(400).json({ error: 'plainTextPswd is in invalid format' });
        return;
    }

    const hashedPswd = await bcrypt.hash(plainTextPswd, saltRounds);

    try {
        const newUser = await userData.createUser(name, userName, hashedPswd);
        delete newUser.password; // sanitizing
        res.json(newUser);
    } catch (e) {
        res.status(500).json({ error: e });
    }

});

router.post('/login', async (req, res) => {

    const userInfo = req.body;
    const userName = userInfo.username;
    const plainTextPswd = userInfo.password;

    if (!userName || typeof userName !== 'string' || userName.trim().length === 0) {
        res.status(400).json({ error: 'userName is in invalid format' });
        return;
    }

    if (!plainTextPswd || typeof plainTextPswd !== 'string' || plainTextPswd.trim().length === 0) {
        res.status(400).json({ error: 'plainTextPswd is in invalid format' });
        return;
    }

    try {

        let users = await userData.getAllUsers();

        let match = false;
        let matchedUser;

        for (let user of users) {
            if (user.username === userName) {
                match = await bcrypt.compare(plainTextPswd, user.password);
                matchedUser = user;
                break;
            }
        }


        if (match) {
            req.session.user = { userName: matchedUser.username, id: matchedUser._id };
            delete matchedUser.password;
            res.json(matchedUser);
        } else {
            res.status(401).json({ error: 'Invalid credentials' });
            return;
        }
    } catch (e) {
        res.status(500).json({ error: e });
    }

});

router.get('/logout', async (req, res) => {
    req.session.destroy();
    res.json({ message: "You have been successfully logged out!" });
});

module.exports = router;