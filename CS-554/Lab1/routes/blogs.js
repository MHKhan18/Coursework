const express = require('express');
const bcrypt = require('bcryptjs');

let { ObjectId } = require('mongodb');
const data = require('../data');

const router = express.Router();
const userData = data.users;
const blogData = data.blogs;

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

router.post('/', async (req, res) => {

    const blogInfo = req.body;

    const title = blogInfo.title;
    const body = blogInfo.body;

    // TODO : validation

    const postAuthor = req.session.user;

    try {
        const newBlog = await blogData.insertBlog(title, body, postAuthor);
        res.json(newBlog);
    } catch (e) {
        res.status(500).json({ error: e });
    }
});

router.get('/:id', async (req, res) => {
    try {
        const id = req.params.id;
        if (!id) {
            res.status(400).json({ error: 'invalid id' });
            return;
        }

        if (typeof id !== 'string' || id.trim().length === 0) {
            res.status(400).json({ error: 'invalid id' });
            return;
        }
        let parsedId;
        try {
            parsedId = ObjectId(id);
        } catch (error) {
            res.status(400).json({ error: 'invalid id' });
            return;
        }

        const blog = await blogData.getBlog(id);
        res.json(blog);

    } catch (e) {
        res.status(404).json({ error: 'Blog not found' });
    }
});

router.get('/', async (req, res) => {

    let skip = 0;
    let take = 20;

    skip = req.query.skip;
    take = req.query.take;

    //  TO DO : validation

    take = Math.min(take, 100);

    try {
        const blogsList = await blogData.getNumBlogs(take, skip);
        res.json(blogsList);
    } catch (e) {
        res.status(500).json({ error: e });
    }


});

module.exports = router;