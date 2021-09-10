const mongoCollections = require('../config/mongoCollections');
const blogs = mongoCollections.blogs;
let { ObjectId } = require('mongodb');


async function getBlog(id) {

    if (!id) {
        throw 'Id parameter must be supplied';
    }

    if (typeof id !== 'string' || id.trim().length === 0) {
        throw "Id must be a non-empty string";
    }

    let parsedId;
    try {
        parsedId = ObjectId(id);
    } catch (error) {
        throw `Received invalid id: ${id}`;
    }

    const blogsCollection = await blogs();
    const blog = await blogsCollection.findOne({ _id: parsedId });

    if (blog === null) {
        throw `No blog with id : ${id}`;
    }

    blog._id = blog._id.toString();

    blog.comments.forEach((comment) => {
        comment._id = comment._id.toString();
    });

    return blog;
}

async function insertBlog(title, body, postAuthor) {


    if (!title || typeof title !== 'string' || title.trim().length === 0) {
        throw 'title is a required non-empty string parameter.';
    }

    if (!body || typeof body !== 'string' || body.trim().length === 0) {
        throw 'title is a required non-empty string parameter.';
    }

    if (!postAuthor || typeof postAuthor !== 'object' || Array.isArray(postAuthor)) {
        throw 'postAuthor is a required object parameter.';
    }

    if (!postAuthor.id || !postAuthor.userName) {
        throw 'postAuthor is missing id or userName';
    }

    const blogsCollection = await blogs();
    const newBlog = {
        title: title,
        body: body,
        userThatPosted: {
            _id: postAuthor.id,
            username: postAuthor.userName
        },
        comments: []
    }

    const insertedBlog = await blogsCollection.insertOne(newBlog);

    if (insertedBlog.insertedCount === 0) {
        throw 'Could not insert blog';
    }

    const newId = insertedBlog.insertedId;
    const blog = await getBlog(newId.toString());

    return blog;
}

async function getAllBlogs() {

    const blogCollection = await blogs();
    const blogsList = await blogCollection.find({}).toArray();

    blogsList.forEach((blog) => {
        blog._id = blog._id.toString();
        blog.comments.forEach((comment) => {
            comment._id = comment._id.toString();
        });
    });

    return blogsList;
}

async function getNumBlogs(num, skip) {

    if (!num || typeof num !== 'number' || (!Number.isInteger(num)) || num <= 0) {
        throw 'num must be a positive integer';
    }

    if (!skip || typeof skip !== 'number' || (!Number.isInteger(skip)) || skip <= 0) {
        throw 'skip must be a positive integer';
    }

    const blogCollection = await blogs();
    const blogsList = await blogCollection.find({}).skip(skip).limit(num).toArray();

    blogsList.forEach((blog) => {
        blog._id = blog._id.toString();
        blog.comments.forEach((comment) => {
            comment._id = comment._id.toString();
        });
    });

    return blogsList;
}

async function updateBlog(id, updatedBlog) {

    if (!id) {
        throw 'Id parameter must be supplied';
    }

    if (typeof id !== 'string' || id.trim().length === 0) {
        throw "Id must be a non-empty string";
    }

    let parsedId;
    try {
        parsedId = ObjectId(id);
    } catch (error) {
        throw `Received invalid id: ${id}`;
    }

    const blogCollection = await blogs();

    const updatedBlogData = {};

    if (updatedBlog.title) {
        if (typeof (updatedBlog.title) !== 'string' || updatedBlog.title.trim().length === 0) {
            throw 'title must be a non-empty string parameter.';
        }
        updatedBlogData.title = updatedBlog.title;
    }

    if (updatedBlog.body) {
        if (typeof (updatedBlog.body) !== 'string' || updatedBlog.body.trim().length === 0) {
            throw 'title must be a non-empty string parameter.';
        }
        updatedBlogData.title = updatedBlog.title;
    }

    await blogCollection.updateOne({ _id: parsedId }, { $set: updatedBlogData });

    const result = await getBlog(id);

    return result;
}



module.exports = {
    getBlog,
    insertBlog,
    getAllBlogs,
    getNumBlogs,
    updateBlog
}