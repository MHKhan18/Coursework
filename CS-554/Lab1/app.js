const express = require('express');
const session = require('express-session');

const configRoutes = require('./routes');

const data = require('./data');
const blogData = data.blogs;
const commentData = data.comments;


const app = express();
app.use(express.json()); // for json # url encoded for forms


app.use(session({
    name: 'AuthCookie',
    secret: 'some secret string!',
    resave: false,
    saveUninitialized: true
}));

app.use('/blog', (req, res, next) => {

    if (req.path === "/" && req.method === 'POST' && !(req.session.user)) {
        res.status(403).json({ error: 'Login Required' });
        return;
    } else {
        next();
    }
});

app.use('/blog/:id', async (req, res, next) => {

    if (req.path === `/${req.params.id}` && (req.method === 'PUT' || req.method === 'PATCH')) {
        if (!(req.session.user)) {
            res.status(403).json({ error: 'Login Required' });
            return;
        }

        try {
            const blog = await blogData.getBlog(req.params.id);
        } catch (e) {
            res.status(500).json({ error: e });
            return;
        }


        if (blog.userThatPosted._id !== req.session.user.id) {
            res.status(403).json({ error: 'Unauthorized' });
            return;
        }

        else { next(); }
    } else {
        next();
    }
});

app.use('/blog/:id/comments', (req, res, next) => {

    if (req.path === `/${req.params.id}/comments` && (req.method === 'POST' && !(req.session.user))) {
        res.status(403).json({ error: 'Login Required' });
        return;
    } else {
        next();
    }
});

app.use('/blog/:blogId/:commentId', async (req, res, next) => {

    if (req.path === `/${req.params.blogId}/${req.params.commentId}` && req.method === 'DELETE') {
        if (!(req.session.user)) {
            res.status(403).json({ error: 'Login Required' });
            return;
        }

        const blogId = req.params.blogId;
        const commentId = req.params.commentId;

        try {
            const commentCreator = await commentData.getCommentCreator(blogId, commentId);
        } catch (e) {
            res.status(500).json({ error: e });
            return;
        }


        if (!commentCreator || (commentCreator._id !== req.session.user.id)) {
            res.status(403).json({ error: 'Unauthorized' });
            return;
        }

        else { next(); }
    } else {
        next();
    }
});

configRoutes(app);

app.listen(3000, () => {
    console.log("====================================================");
    console.log('CS-554 lab-1 server is running on http://localhost:3000');
});