
const connection = require('./config/mongoConnection');
const {create , getAll , get , remove , rename} = require('./data/movies');

async function main() {

    let firstMovie;
    try{
        firstMovie= await create(
            "Bill and Ted Face the Music",
            "Once told they'd save the universe during a time-traveling adventure, 2 would-be rockers from San Dimas, California find themselves as middle-aged dads still trying to crank out a hit song and fulfill their destiny.",
            "PG-13", 
            "1hr 31min",
            "Comedy",
            ["Keanu Reeves","Alex Winter"],
            {director: "Dean Parisot", yearReleased: 2020}
        );   
    } catch(error){
        console.log(error);
        console.error('create failed testcase');
    }

    console.log('First movie inserted:');
    console.log(firstMovie);
    console.log("==================================================");

    let secondMovie;
    try{
        secondMovie = await create(
            "The Dark Knight",
            "When the menace known as the Joker wreaks havoc and chaos on the people of Gotham, Batman must accept one of the greatest psychological and physical tests of his ability to fight injustice.",
            "R", 
            "2hr 32min",
            "Action",
            ["Christian Bale","Heath Ledger"],
            {director: "Christopher Nolan", yearReleased: 2008}
        );
    } catch(error){
        console.log(error);
        console.error('create failed testcase');
    }
   

    let allMovies;
    try{
        allMovies = await getAll();
    }catch(error){
        console.log(error);
        console.error('getAll failed testcase');
    }
    console.log("All movies after inserting two:");
    console.log(allMovies);
    console.log("==================================================");

    let thirdMovie;
    try{
        thirdMovie = await create(
            "Hidden Figures",
            "The story of a team of female African-American mathematicians who served a vital role in NASA during the early years of the U.S. space program.",
            "PG", 
            "2hr 7min",
            "Drama",
            ["Taraji P. Henson","Octavia Spencer", "Janelle Monáe"],
            {director: "Theodore Melfi", yearReleased: 2016}
        );
    } catch(error){
        console.log(error);
        console.error('create failed testcase');
    }
    console.log("Third movie inserted:");
    console.log(thirdMovie);
    console.log("==================================================");

    let renameFirst;
    try{
        renameFirst = await rename(firstMovie._id , "Name after ranaming once");
    }catch(error){
        console.log(error);
        console.error('create failed testcase');
    }
    console.log("First movie after renaming:");
    console.log(renameFirst);
    console.log("==================================================");

    try{
        const removeSecond = await remove(secondMovie._id);
        console.log(removeSecond);
    }catch(error){
        console.log(error);
        console.error('remove failed testcase');
    }

    try{
        allMovies = await getAll();
    }catch(error){
        console.log(error);
        console.error('getAll failed testcase');
    }
    console.log("All movies after deleting two:");
    console.log(allMovies);
    console.log("==================================================");

    try{
        const badInsert = await create(
            "Hidden Figures",
            "The story of a team of female African-American mathematicians who served a vital role in NASA during the early years of the U.S. space program.",
            "PG", 
            "2hr 7min",
            "Drama",
            ["Taraji P. Henson","Octavia Spencer", "Janelle Monáe"],
            {director: "Theodore Melfi", yearReleased: 1920}
        );
        console.error('create did not error.');
    }catch(error){
        console.log(error);
        console.log('create failed successfully.');
    }

    console.log("==================================================");

    try{
        const badRemove = await remove(secondMovie._id); 
        console.log('remove did not error.');
    }catch(error){
        console.log(error);
        console.error('remove failed successfully.');
    }

    console.log("==================================================");

    try{
        const badRename = await rename(secondMovie._id, "corrupt"); 
        console.log('rename did not error.');
    }catch(error){
        console.log(error);
        console.error('rename failed successfully.');
    }

    console.log("==================================================");

    try{
        const badRename2 = await rename(firstMovie._id , "   "); 
        console.log('rename did not error.');
    }catch(error){
        console.log(error);
        console.error('rename failed successfully.');
    }

    console.log("==================================================");

    try{
        const badGet = await get(secondMovie._id); 
        console.log('get did not error.');
    }catch(error){
        console.log(error);
        console.error('get failed successfully.');
    }

    const db = await connection();
    await db.serverConfig.close();

    console.log('Done!');
    
}

main().catch((error) => {
    console.log(error);
});
  