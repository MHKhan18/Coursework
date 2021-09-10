const mongoCollections = require('../config/mongoCollections');
const blogs = mongoCollections.blogs;
let { ObjectId } = require('mongodb');

const blogsData = require('./blogs');

function validateId(id) {
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

    return parsedId;
}

async function createComment(commentBody, commentAuthor) {

    if (!commentBody || typeof commentBody !== 'string' || commentBody.trim().length === 0) {
        throw 'commentBody is a required non-empty string parameter.';
    }

    if (!commentAuthor || typeof commentAuthor !== 'object' || Array.isArray(commentAuthor)) {
        throw 'commentAuthor is a required object parameter.';
    }

    if (!commentAuthor.id || !commentAuthor.userName) {
        throw 'commentAuthor is missing id or userName';
    }


    const comment_id = ObjectId();

    const newComment = {
        _id: comment_id,
        userThatPostedComment: {
            _id: commentAuthor.id,
            username: commentAuthor.userName
        },
        comment: commentBody
    }

    return newComment;
}


async function insertComment(blogId, comment) {


    parsedId = validateId(blogId);

    if (!comment || typeof comment !== 'object' || Array.isArray(comment)) {
        throw 'comment is in invalid format';
    }
    const blogsCollection = await blogs();
    const blog = await blogsCollection.findOne({ _id: parsedId });

    if (blog === null) {
        throw `No blog with id : ${blogId}`;
    }

    await blogsCollection.updateOne({ _id: parsedId }, { $addToSet: { comments: comment } });

    return await blogsCollection.findOne({ _id: parsedId });

}

async function deleteComment(blogId, commentId) {

    const parsedBlogId = validateId(blogId);
    const parsedCommentId = validateId(commentId);

    const blog = await blogsData.getBlog(blogId);

    const comments = blog.comments;

    const filteredComments = comments.filter(comment => comment._id !== commentId);

    const blogsCollection = await blogs();
    await blogsCollection.updateOne({ _id: parsedBlogId }, { $set: { comments: filteredComments } });

    return await blogsData.getBlog(blogId);

}

module.exports = {
    createComment,
    insertComment,
    deleteComment
}