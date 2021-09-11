const dbConnection = require('../config/mongoConnection');
const bcrypt = require('bcryptjs');
const data = require('../data/');

const usersData = data.users;
const blogsData = data.blogs;
const commentsData = data.comments;



async function main() {

    const db = await dbConnection();
    await db.dropDatabase();

    const NUM_USERS = 5;
    const password = await bcrypt.hash("secret", 16);

    const users = [];
    for (let i = 0; i < NUM_USERS; i++) {
        users.push(await usersData.createUser(`user-${i + 1}`, `userName-${i + 1}`, password));
    }


    const NUM_BLOGS = 105;

    for (let i = 0; i < NUM_BLOGS; i++) {

        let title = `Title - ${i + 1}`;
        let body = `Body - ${i + 1}`;
        let blogAuthor = { id: users[i % 5]._id, userName: users[i % 5].username };

        let blog = await blogsData.insertBlog(title, body, blogAuthor);

        for (let j = 1; j < 6; j++) {
            let comment = await commentsData.createComment(`Comment - ${j}`, { id: users[i % 5]._id, userName: users[i % 5].username });
            await commentsData.insertComment(blog._id, comment);

        }

    }


    console.log('Done seeding database');
    // await db.serverConfig.close();
    process.exit(0);
}

main();