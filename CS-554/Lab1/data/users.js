const mongoCollections = require('../config/mongoCollections');
const users = mongoCollections.users;
let { ObjectId } = require('mongodb');


async function getUser(id) {

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

    const usersCollection = await users();
    const user = await usersCollection.findOne({ _id: parsedId });

    if (user === null) {
        throw `No user with id : ${id}`;
    }

    user._id = user._id.toString();

    return user;


}

async function getAllUsers() {

    const usersCollection = await users();
    const usersList = await usersCollection.find({}).toArray();

    usersList.forEach((user) => {
        user._id = user._id.toString();
    });

    return usersList;

}

async function createUser(name, userName, hashedPswd) {

    if (!name || typeof name !== 'string' || name.trim().length === 0) {
        throw 'name is a required non-empty string parameter.';
    }

    if (!userName || typeof userName !== 'string' || userName.trim().length === 0) {
        throw 'userName is a required non-empty string parameter.';
    }

    if (!hashedPswd || typeof hashedPswd !== 'string' || hashedPswd.trim().length === 0) {
        throw 'hashedPswd is a required non-empty string parameter.';
    }

    const usersCollection = await users();
    const newUser = {
        name: name,
        username: userName,
        password: hashedPswd
    }

    const insertedUser = await usersCollection.insertOne(newUser);

    if (insertedUser.insertedCount === 0) {
        throw 'Could not create user';
    }

    const newId = insertedUser.insertedId;

    const user = await getUser(newId.toString());

    return user;

}

module.exports = {
    getUser,
    getAllUsers,
    createUser
}