
const mongoCollections = require('../config/mongoCollections');
let { ObjectId } = require('mongodb');
const movies = mongoCollections.movies;


async function get(id){
    
    if (!id){
        throw 'Id parameter must be supplied';
    } 

    if (typeof id !== 'string' || id.trim().length === 0){
        throw "Id must be a non-empty string";
    }

    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        throw `Received invalid id: ${id}`;
    }
    
    const movieCollection = await movies();
    const movie = await movieCollection.findOne({_id : parsedId});

    if (movie === null){
        throw `No movie with id : ${id}`;
    }

    movie._id = movie._id.toString();

    return movie;
}

async function create(title, plot, rating, runtime, genre, cast, info){

    if (!title || typeof title !== 'string' || title.trim().length === 0){
        throw 'title is a required non-empty string parameter.';
    }

    if (!plot || typeof plot !== 'string' || plot.trim().length === 0){
        throw 'plot is a required non-empty string parameter.';
    }

    if (!rating || typeof rating !== 'string' || rating.trim().length === 0){
        throw 'rating is a required non-empty string parameter.';
    }

    if (!runtime || typeof runtime !== 'string' || runtime.trim().length === 0){
        throw 'runtime is a required non-empty string parameter.';
    }

    if (!genre || typeof genre !== 'string' || genre.trim().length === 0){
        throw 'title is a required non-empty string parameter.';
    }

    if (!cast || !Array.isArray(cast) || cast.length === 0){
        throw 'cast is a required non-empty string array parameter.';
    }

    cast.forEach((artist) => {
        if (typeof artist !== 'string' || artist.trim().length === 0){
            throw 'cast must be an array of non-empty strings.';
        }
    });

    if (!info || typeof info !== 'object' || Array.isArray(info)){
        throw 'info is a required object parameter.';
    }

    if (!(info.director) || typeof (info.director) !== 'string' || info.director.trim().length === 0){
        throw 'info.director is a required non-empty string parameter.';
    }

    const PATTERN = /^\d{4}$/;

    if (!(info.yearReleased) || typeof (info.yearReleased) !== 'number' || !(info.yearReleased.toString().match(PATTERN))){
        throw 'info.yearReleased is a required four digit parameter.';
    }

    const curYear = new Date().getFullYear();

    if (info.yearReleased < 1930 || info.yearReleased > curYear+5){
        throw `yearReleased must be between 1930 and ${curYear+5}`;
    }

    const movieCollection = await movies();
    const newMovie = {
        title : title,
        plot: plot,
        rating: rating,
        runtime: runtime,
        genre: genre,
        cast: cast,
        info: info
    };
    const insertedMovie = await movieCollection.insertOne(newMovie);

    if (insertedMovie.insertedCount === 0){
        throw 'Could not insert movie';
    }

    const newId = insertedMovie.insertedId;
    const movie = await get(newId.toString());

    return movie;

}

async function getAll(){

    const movieCollection = await movies();
    const movieList = await movieCollection.find({}).toArray();

    movieList.forEach((movie) => {
        movie._id = movie._id.toString();
    });

    return movieList;
}



async function remove(id){

    if (!id){
        throw 'Id parameter must be supplied';
    } 

    if (typeof id !== 'string' || id.trim().length === 0){
        throw "Id must be a non-empty string";
    }

    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        throw `Received invalid id: ${id}`;
    }
    
    const movieCollection = await movies();

    let movieInfo;
    try{
        movieInfo = await get(id);
    }catch(error){
        throw `${error}`;
    }
    
    const deletionInfo = await movieCollection.deleteOne({ _id : parsedId});

    if (deletionInfo.deletedCount === 0) {
        throw `Could not delete movie with id of ${id}`;
    }

    return `${movieInfo.title} has been successfully deleted.`;

    
}

async function rename(id, newTitle){
    
    if (!id){
        throw 'Id parameter must be supplied';
    } 

    if (typeof id !== 'string' || id.trim().length === 0){
        throw "Id must be a non-empty string";
    }

    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        throw `Received invalid id: ${id}`;
    }

    if (!newTitle){
        throw 'newTitle parameter must be supplied';
    } 

    if (typeof newTitle !== 'string' || newTitle.trim().length === 0){
        throw "newTitle must be a non-empty string";
    }

    const movieCollection = await movies();

    const updatedMovie = {
        title : newTitle
    }

    const updatedInfo = await movieCollection.updateOne(
        { _id : parsedId },
        { $set : updatedMovie }
    );

    if (updatedInfo.modifiedCount === 0) {
        throw 'could not update movie successfully';
    }

    return await get(id);

}

module.exports = {
    create,
    getAll,
    get,
    remove,
    rename
}