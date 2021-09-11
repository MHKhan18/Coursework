const express = require('express');
const bcrypt = require('bcryptjs');

let { ObjectId } = require('mongodb');
const data = require('../data');

const router = express.Router();

const userData = data.users;
const blogData = data.blogs;
const commentData = data.comments;

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
    const postAuthor = req.session.user;

    if (!title || typeof title !== 'string' || title.trim().length === 0) {
        res.status(400).json({ error: 'title is in invalid format' });
        return;
    }

    if (!body || typeof body !== 'string' || body.trim().length === 0) {
        res.status(400).json({ error: 'body is in invalid format' });
        return;
    }

    if (!postAuthor || typeof postAuthor !== 'object' || Array.isArray(postAuthor)) {
        res.status(400).json({ error: 'postAuthor is in invalid format' });
        return;
    }

    if (!postAuthor.id || !postAuthor.userName) {
        res.status(400).json({ error: 'postAuthor is in invalid format' });
        return;
    }

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

    if (req.query.skip) { skip = parseInt(req.query.skip); }
    if (req.query.take) { take = parseInt(req.query.take); }

    if (req.query.take && (typeof take !== 'number' || (!Number.isInteger(take)) || take <= 0)) {
        res.status(400).json({ error: `Invalid take: ${take}` });
        return;
    }

    if (req.query.skip && (typeof skip !== 'number' || (!Number.isInteger(skip)) || skip < 0)) {
        res.status(400).json({ error: `Invalid skip: ${skip}` });
        return;
    }


    take = Math.min(take, 100);

    try {
        const blogsList = await blogData.getNumBlogs(take, skip);
        res.json(blogsList);
    } catch (e) {
        res.status(500).json({ error: e });
    }


});

router.put('/:id', async (req, res) => {

    const id = req.params.id;

    const blogInfo = req.body;
    const title = blogInfo.title;
    const body = blogInfo.body;


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

    if (!title || typeof title !== 'string' || title.trim().length === 0) {
        res.status(400).json({ error: 'title is in invalid format' });
        return;
    }

    if (!body || typeof body !== 'string' || body.trim().length === 0) {
        res.status(400).json({ error: 'body is in invalid format' });
        return;
    }

    const updatedBlog = {
        title: title,
        body: body
    }

    try {
        const blog = await blogData.updateBlog(id, updatedBlog);
        res.json(blog);
    } catch (e) {
        res.status(500).json({ error: `${e}` });
    }


});

router.patch('/:id', async (req, res) => {

    const id = req.params.id;

    const blogInfo = req.body;
    const title = blogInfo.title;
    const body = blogInfo.body;

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

    if (title && (typeof title !== 'string' || title.trim().length === 0)) {
        res.status(400).json({ error: 'title is in invalid format' });
        return;
    }

    if (body && (typeof body !== 'string' || body.trim().length === 0)) {
        res.status(400).json({ error: 'body is in invalid format' });
        return;
    }

    const oldBlog = await blogData.getBlog(id);
    let changeCount = 0;
    if (body && body !== oldBlog.body) { changeCount++ }
    if (title && title !== oldBlog.title) { changeCount++ }

    if (changeCount === 0) {
        res.status(400).json({
            error:
                'No fields have been changed from their inital values, so no update has occurred'
        });
    }

    const updatedBlog = {}
    if (body) { updatedBlog.body = body }
    if (title) { updatedBlog.title = title }

    try {
        const blog = await blogData.updateBlog(id, updatedBlog);
        res.json(blog);
    } catch (e) {
        res.status(500).json({ error: `${e}` });
    }


});

router.post('/:id/comments', async (req, res) => {

    const blogId = req.params.id;
    const commentBody = req.body.comment;
    const commentAuthor = req.session.user;

    if (!blogId) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    if (typeof blogId !== 'string' || blogId.trim().length === 0) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }
    let parsedId;
    try {
        parsedId = ObjectId(blogId);
    } catch (error) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    if (!commentBody || typeof commentBody !== 'string' || commentBody.trim().length === 0) {
        res.status(400).json({ error: 'commentBody is in invalid format' });
        return;
    }

    if (!commentAuthor || typeof commentAuthor !== 'object' || Array.isArray(commentAuthor)) {
        res.status(400).json({ error: 'commentAuthor is in invalid format' });
        return;
    }

    if (!commentAuthor.id || !commentAuthor.userName) {
        res.status(400).json({ error: 'commentAuthor is in invalid format' });
        return;
    }

    try {
        const commentObj = await commentData.createComment(commentBody, commentAuthor);
        const updatedBlog = await commentData.insertComment(blogId, commentObj);
        res.json(updatedBlog);
    } catch (e) {
        res.status(500).json({ error: `${e}` });
    }

});

router.delete('/:blogId/:commentId', async (req, res) => {

    const blogId = req.params.blogId;
    const commentId = req.params.commentId;

    // TO DO: validation
    if (!blogId) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    if (typeof blogId !== 'string' || blogId.trim().length === 0) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    try {
        ObjectId(blogId);
    } catch (error) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }
    ////////////////
    if (!commentId) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    if (typeof commentId !== 'string' || commentId.trim().length === 0) {
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    try {
        ObjectId(commentId);
    } catch (error) {
        res.status(400).json({ error: 'invalid commentId' });
        return;
    }


    try {
        const commentDeletedBlog = await commentData.deleteComment(blogId, commentId);
        res.json(commentDeletedBlog);
    } catch (e) {
        res.status(500).json({ error: `${e}` });
    }

});

module.exports = router;