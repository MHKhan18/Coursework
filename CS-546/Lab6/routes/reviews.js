const express = require('express');
const router = express.Router();
const data = require('../data');
const reviewData = data.reviews;
let { ObjectId } = require('mongodb');

router.get('/:bookId', async (req, res) => {

    if (!req.params.bookId) {
        res.status(400).json({ error: 'bookId is required.' });
        return;
    }

    const id = req.params.bookId;
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    try{
        const review = await reviewData.getReview(id);
        res.json(review);
    }catch (e) {
        res.status(404).json({ error: `${e}`});
    }
});

router.post('/:bookId', async (req, res) => {

    if (!req.params.bookId) {
        res.status(400).json({ error: 'bookId is required.' });
        return;
    }

    const id = req.params.bookId;
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    const newReview = req.body;
    const title = newReview.title;
    const reviewer = newReview.reviewer;
    const rating = newReview.rating;
    const dateOfReview = newReview.dateOfReview;
    const review = newReview.review;

    if (!title || typeof title !== 'string' || title.trim().length === 0){
        res.status(400).json({ error: 'title is a required non-empty string parameter.' });
        return;
    }

    if (!reviewer || typeof reviewer !== 'string' || reviewer.trim().length === 0){
        res.status(400).json({ error: 'reviewer is a required non-empty string parameter.' });
        return;
    }
    
    if(rating === undefined || typeof rating !== 'number' || rating < 1 || rating > 5){
        res.status(400).json({ error: 'rating is a required positive number parameter.'});
        return;
    }

    const DATE_FORMAT = /(0*[1-9]|1[012])[- \/.](0*[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;
    if (typeof dateOfReview !== 'string'  || dateOfReview.trim().length === 0){
        res.status(400).json({ error: `dateOfReview is a required non-empty string parameter.` });
        return;
    }
    if(!(dateOfReview.match(DATE_FORMAT))){
        res.status(400).json({ error: `Invalid dateOfReview format received: ${dateOfReview}` });
        return;
    }

    if (!review || typeof review !== 'string' || review.trim().length === 0){
        res.status(400).json({ error: 'review is a required non-empty string parameter.' });
        return;
    }

    try{
        const theReview = await reviewData.createReview(title , reviewer , rating , dateOfReview , review);
        const newBook = await reviewData.addReview(id , theReview);
        res.json(newBook);
    }catch (e) {
        res.status(500).json({ error: `${e}` });
      }
});

router.get('/review/:reviewId', async (req, res) => {

    if (!req.params.reviewId) {
        res.status(400).json({ error: 'reviewId is required.' });
        return;
    }

    const id = req.params.reviewId;
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    try{
        const allReviews = await reviewData.getAllReviews();
        let theReview;
        for(let review of allReviews){
            if(review._id === id){
                theReview = review;
                break;
            }
        }
        if(!theReview){
            res.status(400).json({ error: `No review with id: ${id}` });
            return;
        }
        res.json(theReview);
    }catch (e) {
        res.status(500).json({ error: `${e}` });
      }

});

router.delete('/:reviewId', async (req, res) => {
    if (!req.params.reviewId) {
        res.status(400).json({ error: 'reviewId is required.' });
        return;
    }

    const id = req.params.reviewId;
    let parsedId; 
    try{
        parsedId = ObjectId(id);
    } catch (error){
        res.status(400).json({ error: 'invalid id' });
        return;
    }

    try{
        await reviewData.deleteReview(id);
        res.json({"reviewId": id, "deleted": 'true'});
    }catch (e) {
        res.status(500).json({ error: `${e}` });
      }
});

module.exports = router;

