const mongoCollections = require('../config/mongoCollections');
const books = mongoCollections.books;
let { ObjectId } = require('mongodb');


async function getBook(id){

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

    const bookCollection = await books();
    const book = await bookCollection.findOne({_id : parsedId});

    if (book === null){
        throw `No book with id : ${id}`;
    }

    book._id = book._id.toString();

    // also stringify the ids all reviews
    book.reviews.forEach((review) => {
        review._id = review._id.toString();
    });

    return book;

}



async function insertBook(title, author, genre, datePublished, summary){

    if (!title || typeof title !== 'string' || title.trim().length === 0){
        throw 'title is a required non-empty string parameter.';
    }

    if (!author || typeof author !== 'object' || Array.isArray(author)){
        throw 'author is a required object parameter.';
    }
    if (!(author.authorFirstName) || typeof (author.authorFirstName) !== 'string' || author.authorFirstName.trim().length === 0){
        throw 'author.authorFirstName is a required non-empty string parameter.';
    }
    if (!(author.authorLastName) || typeof (author.authorLastName) !== 'string' || author.authorLastName.trim().length === 0){
        throw 'author.authorFirstName is a required non-empty string parameter.';
    }

    if (!genre || !Array.isArray(genre) || genre.length === 0){
        throw 'genre is a required non-empty string array parameter.';
    }
    genre.forEach((g) => {
        if (typeof g !== 'string' || g.trim().length === 0){
            throw 'genre must be an array of non-empty strings.';
        }
    });

    if(!datePublished){
        throw 'datePublished is a required parameter.';
    }
    const DATE_FORMAT = /(0*[1-9]|1[012])[- \/.](0*[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
    if (typeof datePublished !== 'string'  || datePublished.trim().length === 0){
        throw `datePublished is a required non-empty string parameter.`;
    }
    if(!(datePublished.match(DATE_FORMAT))){
        throw `Invalid datePublished format received: ${datePublished}`;
    }

    if (!summary || typeof summary !== 'string' || summary.trim().length === 0){
        throw 'summary is a required non-empty string parameter.';
    }

    const bookCollection = await books();
    const newBook = {
        title : title,
        author : author,
        genre : genre,
        datePublished : datePublished,
        summary : summary,
        reviews : []
    };

    const insertedBook = await bookCollection.insertOne(newBook);

    if (insertedBook.insertedCount === 0){
        throw 'Could not insert book';
    }

    const newId = insertedBook.insertedId;
    const book = await getBook(newId.toString()); // make sure getBook() calls made after insert include the new book 

    return book;

}


// returns full books -- good for debugging
async function getAllBooks(){

    const bookCollection = await books();
    const booksList = await bookCollection.find({}).toArray();

    booksList.forEach((book) => {
        book._id = book._id.toString();
        book.reviews.forEach((review) => {
            review._id = review._id.toString();
        });    
    });

    return booksList;

}

// returns only book id and title
async function getAllBookTitles(){

    const bookCollection = await books();
    const booksList = await bookCollection.find({} , { projection: { _id: 1, title: 1} }).toArray();

    booksList.forEach((book) => {
        book._id = book._id.toString();
    });

    return booksList;

}


async function deleteBook(id){

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

    const bookCollection = await books();

    // check if it exists
    let bookInfo;
    try{
        bookInfo = await getBook(id);
    }catch(error){
        throw `${error}`;
    }
    
    const deletionInfo = await bookCollection.deleteOne({ _id : parsedId});

    if (deletionInfo.deletedCount === 0) {
        throw `Could not delete movie with id of ${id}`;
    }

    return `${bookInfo.title} has been successfully deleted.`;

}


async function updateBook(id, updatedBook){

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
    

    const bookCollection = await books();

    const updatedBookData = {};

    if(updatedBook.title){
        if (typeof (updatedBook.title) !== 'string' || updatedBook.title.trim().length === 0){
            throw 'title must be a non-empty string parameter.';
        }
        updatedBookData.title = updatedBook.title;
    }

    if(updatedBook.author){

        let author = updatedBook.author;
        if (typeof author !== 'object' || Array.isArray(author)){
            throw 'author is a required object parameter.';
        }
        if (!(author.authorFirstName) || typeof (author.authorFirstName) !== 'string' || author.authorFirstName.trim().length === 0){
            throw 'author.authorFirstName is a required non-empty string parameter.';
        }
        if (!(author.authorLastName) || typeof (author.authorLastName) !== 'string' || author.authorLastName.trim().length === 0){
            throw 'author.authorFirstName is a required non-empty string parameter.';
        }
        updatedBookData.author = author;
    }

    if(updatedBook.genre){

        let genre = updatedBook.genre;
        if (!Array.isArray(genre) || genre.length === 0){
            throw 'genre is a required non-empty string array parameter.';
        }
        genre.forEach((g) => {
            if (typeof g !== 'string' || g.trim().length === 0){
                throw 'genre must be an array of non-empty strings.';
            }
        });
        updatedBookData.genre = genre;
    }

    if(updatedBook.datePublished){

        let datePublished = updatedBook.datePublished;
        const DATE_FORMAT = /(0[1-9]|(1[012])*)[- \/.](0[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
        if (typeof datePublished !== 'string'  || datePublished.trim().length === 0){
            throw `datePublished is a required non-empty string parameter.`;
        }
        if(!(datePublished.match(DATE_FORMAT))){
            throw `Invalid datePublished format received: ${datePublished}`;
        }
        updatedBookData.datePublished = datePublished;
    }

    if(updatedBook.summary){

        let summary = updatedBook.summary;
        if (typeof summary !== 'string' || summary.trim().length === 0){
            throw 'summary is a required non-empty string parameter.';
        }
        updatedBookData.summary = summary;
    }

    try{
        await bookCollection.updateOne({ _id: parsedId }, { $set: updatedBookData});
    }catch(e){
        throw `Error: Update failed.`;
    }
    
    let res;
    try{
        res = await getBook(id);
    }catch(e){
        throw `Error message: ${e}`
    }
    return res;
}


async function addGenre(id , newGenre){

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

    if(!newGenre || typeof newGenre !== 'string'){
        throw "newGenre is a required string parameter.";
    }

    const bookCollection = await books();
    await bookCollection.updateOne({ _id: parsedId }, { $addToSet: { genre: newGenre } })

    const book = await getBook(id);
    return book;

}


module.exports = {
    getBook,
    insertBook,
    getAllBooks,
    deleteBook,
    updateBook,
    getAllBookTitles,
    addGenre
}