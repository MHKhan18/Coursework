const dbConnection = require('../config/mongoConnection');
const data = require('../data/');
const books = data.books;
const reviews = data.reviews;



async function main(){
    const db = await dbConnection();
    await db.dropDatabase();

    const NUM_BOOKS = 10;

    for(let i = 0 ; i < NUM_BOOKS; i++){

        let author = {authorFirstName : `First${i}` , authorLastName : `Last${i}`};
        let book = await books.insertBook(`Book${i}` , author , [`genre${i}`] , `3/${i+1}/2021` , `Sumaary-${i}`);

        for(let j = 1 ; j < i ; j++){
            let review = await reviews.createReview(`Review-${j}` , `Gunea${j}` , (j%5)+1 , `3/${j}/2021` , `some comment - ${j}`);
            await reviews.addReview(book._id.toString() , review);  
        }
       
    }

    const allBooks = await books.getAllBooks();
    console.log(allBooks);

    const allReviews = await reviews.getAllReviews();
    console.log(allReviews);
    


    console.log('Done seeding database');
    await db.serverConfig.close();
}

main();