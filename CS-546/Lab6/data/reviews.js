
let { ObjectId } = require('mongodb');
const books = require('./books');
const mongoCollections = require('../config/mongoCollections');
const booksCollection = mongoCollections.books;

async function getReview(id){ // retrieves all reviews for book with id

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

    const allBooks = await books.getAllBooks();
    for(let book of allBooks){
        if (book._id === id){
            let revs =  book.reviews;
            if(revs.length === 0){
                throw `No review for book with id: ${id}`;
            }
            return revs;
        }
    }

    throw `No book with id: ${id}`;

}

async function createReview(title , reviewer , rating , dateOfReview , review){

    if (!title || typeof title !== 'string' || title.trim().length === 0){
        throw 'title is a required non-empty string parameter.';
    }

    if (!reviewer || typeof reviewer !== 'string' || reviewer.trim().length === 0){
        throw 'reviewer is a required non-empty string parameter.';
    }
    
    if(rating === undefined || typeof rating !== 'number' || rating < 1 || rating > 5){
        throw 'rating is a required positive number parameter.';
    }

    const DATE_FORMAT = /(0*[1-9]|1[012])[- \/.](0*[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
    if (typeof dateOfReview !== 'string'  || dateOfReview.trim().length === 0){
        throw `dateOfReview is a required non-empty string parameter.`;
    }
    if(!(dateOfReview.match(DATE_FORMAT))){
        throw `Invalid dateOfReview format received: ${dateOfReview}`;
    }

    if (!review || typeof review !== 'string' || review.trim().length === 0){
        throw 'review is a required non-empty string parameter.';
    }

    const review_id = ObjectId();

    const newReview = {
        _id : review_id,
        title : title,
        reviewer : reviewer,
        rating : rating,
        dateOfReview : dateOfReview, // since no operation on dates required, store as string
        review : review
    };

    return newReview;
}

async function getAllReviews(){

    const reviews = [];
    const allBooks = await books.getAllBooks();
    for(let book of allBooks){
        let allReviews = book.reviews;
        for(let review of allReviews){
            reviews.push(review);
        }
    }
    return reviews;
}


async function deleteReview(id){ // reviewId
   

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

    let bookId;
    let reviewsList;
    let reviewIndex;
    
    const allBooks = await books.getAllBooks();
    for(let book of allBooks){
        for(let review of book.reviews){
            let i = 0;
            if(review._id === id){
                bookId = book._id;
                reviewsList = book.reviews;
                reviewIndex = i;
                break;
            }
            i++;
        }
    }

    if(bookId === undefined){ // not found
        throw `No review with id: ${id}`;
    }

    reviewsList.splice(reviewIndex , 1);

    try{
        const bookCollection = await booksCollection();
        bookId = ObjectId(bookId);
        await bookCollection.updateOne({ _id: bookId }, { $set: { reviews: reviewsList } });
        const book = await books.getBook(bookId.toString());
        return book;
    } catch(e){
        throw `${e}`;
    }
    
}


async function addReview(bookId , review){

    
    if (!bookId){
        throw 'Id parameter must be supplied';
    } 

    if (typeof bookId !== 'string' || bookId.trim().length === 0){
        throw "Id must be a non-empty string";
    }

    let parsedId; 
    try{
        parsedId = ObjectId(bookId);
    } catch (error){
        throw `Received invalid id: ${bookId}`;
    }

    if(review === undefined || typeof review !== 'object' || Array.isArray(review)){
        throw 'review is a required parameter.';
    }

    if(!(review._id)){
        throw `Review must contain id`;
    }

    const bookCollection = await booksCollection();
    await bookCollection.updateOne({ _id: parsedId}, { $addToSet: { reviews: review } });

    const book = await books.getBook(bookId);
    return book;

}


module.exports = {
    getReview,
    createReview,
    getAllReviews,
    deleteReview,
    addReview
}