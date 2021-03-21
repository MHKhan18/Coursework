const express = require('express');
const router = express.Router();
const data = require('../data');
const bookData = data.books;
let { ObjectId } = require('mongodb');


router.get('/:id', async (req, res) => {
    try {
        const id = req.params.id;
        if (!id){
            res.status(400).json({ error: 'invalid id' });
            return;
        } 
    
        if (typeof id !== 'string' || id.trim().length === 0){
            res.status(400).json({ error: 'invalid id' });
            return;
        }
        let parsedId; 
        try{
            parsedId = ObjectId(id);
        } catch (error){
            res.status(400).json({ error: 'invalid id' });
            return;
        }

        const book = await bookData.getBook(id);
        res.json(book);
      } catch (e) {
        res.status(404).json({ error: 'Book not found' });
      }
});


router.get('/', async (req, res) => {
    try{
        const bookList = await bookData.getAllBookTitles();
        res.json(bookList);
    } catch (e) {
        res.status(500).json({ error: e });
    }
});

router.post('/', async (req, res) => {
    const bookDetails = req.body;
    if(!bookDetails.title){
        res.status(400).json({ error: 'title is required' });
        return;
    }
    if(!bookDetails.author){
        res.status(400).json({ error: 'author is required' });
        return;
    }
    if(!bookDetails.genre){
        res.status(400).json({ error: 'genre is required' });
        return;
    }
    if(!bookDetails.datePublished){
        res.status(400).json({ error: 'datePublished is required' });
        return;
    }
    if(!bookDetails.summary){
        res.status(400).json({ error: 'summary is required' });
        return;
    }

    if(bookDetails.reviews){
        res.status(400).json({ error: 'reviews can not be inserted in post.' });
        return;
    }

    let title = bookDetails.title;
    let author = bookDetails.author;
    let genre = bookDetails.genre;
    let datePublished = bookDetails.datePublished;
    let summary = bookDetails.summary;

    if (typeof title !== 'string' || title.trim().length === 0){
        res.status(400).json({ error: 'title is in invalid format' });
        return;
    }

    if (typeof author !== 'object' || Array.isArray(author)){
        res.status(400).json({ error: 'title is in invalid format' });
        return;
    }
    if (typeof (author.authorFirstName) !== 'string' || author.authorFirstName.trim().length === 0){
        res.status(400).json({ error: 'author.authorFirstName is in invalid format' });
        return;
    }
    if (typeof (author.authorLastName) !== 'string' || author.authorLastName.trim().length === 0){
        res.status(400).json({ error: 'author.authorLastName is in invalid format' });
        return;
    }

    if (!Array.isArray(genre) || genre.length === 0){
        res.status(400).json({ error: 'genre is in invalid format' });
        return;
    }
    genre.forEach((g) => {
        if (typeof g !== 'string' || g.trim().length === 0){
            res.status(400).json({ error: 'genre is in invalid format' });
            return;
        }
    });

    
    const DATE_FORMAT = /(0[1-9]|(1[012])*)[- \/.](0[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
    if (typeof datePublished !== 'string'  || datePublished.trim().length === 0){
        res.status(400).json({ error: 'datePublished is in invalid format' });
        return;
    }
    if(!(datePublished.match(DATE_FORMAT))){
        res.status(400).json({ error: 'datePublished is in invalid format' });
        return;
    }

    if (typeof summary !== 'string' || summary.trim().length === 0){
        res.status(400).json({ error: 'summary is in invalid format' });
        return;
    }
    try {
        
        const newBook = await bookData.insertBook(title, author, genre, datePublished, summary);
        res.json(newBook);
      } catch (e) {
        res.status(500).json({ error: e });
      }
});



router.put('/:id', async (req, res) => {

    try {
        const id = req.params.id;
        if (!id){
            res.status(400).json({ error: 'invalid id' });
            return;
        } 
    
        if (typeof id !== 'string' || id.trim().length === 0){
            res.status(400).json({ error: 'invalid id' });
            return;
        }
        let parsedId; 
        try{
            parsedId = ObjectId(id);
        } catch (error){
            res.status(400).json({ error: 'invalid id' });
            return;
        }

        let oldBook;
        try{
            oldBook = await  bookData.getBook(id);
        }catch(e){
            res.status(404).json({ error: 'Post not found' });
            return;
        }

        const bookDetails = req.body;
        if(!(bookDetails.title)){
            res.status(400).json({ error: 'title is required' });
            return;
        }
        if(!(bookDetails.author)){
            res.status(400).json({ error: 'author is required' });
            return;
        }
        if(!(bookDetails.genre)){
            res.status(400).json({ error: 'genre is required' });
            return;
        }
        if(!(bookDetails.datePublished)){
            res.status(400).json({ error: 'datePublished is required' });
            return;
        }
        if(!(bookDetails.summary)){
            res.status(400).json({ error: 'summary is required' });
            return;
        }
        if(bookDetails.reviews){
            res.status(400).json({ error: 'reviews can not be inserted in put.' });
            return;
        }

        let title = bookDetails.title;
        let author = bookDetails.author;
        let genre = bookDetails.genre;
        let datePublished = bookDetails.datePublished;
        let summary = bookDetails.summary;

        if (typeof title !== 'string' || title.trim().length === 0){
            res.status(400).json({ error: 'title is in invalid format' });
            return;
        }

        if (typeof author !== 'object' || Array.isArray(author)){
            res.status(400).json({ error: 'title is in invalid format' });
            return;
        }
        if (typeof (author.authorFirstName) !== 'string' || author.authorFirstName.trim().length === 0){
            res.status(400).json({ error: 'author.authorFirstName is in invalid format' });
            return;
        }
        if (typeof (author.authorLastName) !== 'string' || author.authorLastName.trim().length === 0){
            res.status(400).json({ error: 'author.authorLastName is in invalid format' });
            return;
        }

        if (!Array.isArray(genre) || genre.length === 0){
            res.status(400).json({ error: 'genre is in invalid format' });
            return;
        }
        genre.forEach((g) => {
            if (typeof g !== 'string' || g.trim().length === 0){
                res.status(400).json({ error: 'genre is in invalid format' });
                return;
            }
        });

        const DATE_FORMAT = /(0*[1-9]|1[012])[- \/.](0*[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
        if (typeof datePublished !== 'string'  || datePublished.trim().length === 0){
            res.status(400).json({ error: 'datePublished is in invalid format' });
            return;
        }
        if(!(datePublished.match(DATE_FORMAT))){
            res.status(400).json({ error: 'datePublished is in invalid format' });
            return;
        }

        if (typeof summary !== 'string' || summary.trim().length === 0){
            res.status(400).json({ error: 'summary is in invalid format' });
            return;
        }

        const updatedBook = {
            title : title,
            author: author,
            genre : genre,
            datePublished : datePublished,
            summary : summary
        };

        try{
            const book = await bookData.updateBook(id , updatedBook);
            res.json(book);
        }catch(e){
            res.status(500).json({ error: `${e}` });
        }
       
    } 
    catch (e) {
        res.status(500).json({ error: e });
    }
});

router.patch('/:id', async (req, res) => {

    const id = req.params.id;
    if (!id){
        res.status(400).json({ error: 'invalid id' });
        return;
    } 

    if (typeof id !== 'string' || id.trim().length === 0){
        res.status(400).json({ error: 'invalid id' });
        return;
    }
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    let oldBook;
    try{
        oldBook = await  bookData.getBook(id);
    }catch(e){
        res.status(404).json({ error: 'Post not found' });
        return;
    }

    let count = 0;
    const updatedBook = {};

    const bookDetails = req.body;
    if(bookDetails.title && bookDetails.title !== oldBook.title){
        updatedBook.title = bookDetails.title;
        count++;
    }
    if(bookDetails.author && bookDetails.author !== oldBook.author){
        updatedBook.author = bookDetails.author;
        count++;
    }
    
    
   
    if(bookDetails.genre){
        for(let newGenre of bookDetails.genre){
            await bookData.addGenre(id , newGenre);
            count++; // bug -> if all ratings in the array are already present, it still increments
        }
    }
    if(bookDetails.datePublished && bookDetails.datePublished !== oldBook.datePublished){
        updatedBook.datePublished = bookDetails.datePublished;
        count++;
        
    }
    if(bookDetails.summary && bookDetails.summary !== oldBook.summary){
        updatedBook.summary = bookDetails.summary;
        count++;
    }

    if(bookDetails.reviews){
        res.status(400).json({ error: 'reviews can not be inserted in patch.' });
        return;
    }

    if(count === 0){
        res.status(400).json({
            error:
              'No fields have been changed from their inital values, so no update has occurred'
          });
    }

    try {
        const newBook = await bookData.updateBook(id, updatedBook);
        res.json(newBook);
    } catch (e) {
        res.status(500).json({ error: e });
    }
});

router.delete('/:id', async (req, res) => {
    
    if (!req.params.id) {
        res.status(400).json({ error: 'You must Supply and ID to delete' });
        return;
    }

    const id = req.params.id;
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }
    
    try {
        await bookData.getBook(id);
    } catch (e) {
        res.status(404).json({ error: 'Post not found' });
        return;
    }
    
    try {
        await bookData.deleteBook(id);
        res.status(200).json({"bookId" : id, "deleted": 'true' });
    } catch (e) {
        res.status(500).json({ error: e });
    }
});

module.exports = router;





