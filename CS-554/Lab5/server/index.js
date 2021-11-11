require('dotenv').config()
const axios = require('axios');
const bluebird = require('bluebird');
const redis = require('redis');
const uuid = require('uuid');

const client = redis.createClient();

bluebird.promisifyAll(redis.RedisClient.prototype);
bluebird.promisifyAll(redis.Multi.prototype);

const ACCESS_KEY = process.env.UNSPLASH_ACCESS_KEY;

const { ApolloServer, gql } = require('apollo-server');


const typeDefs = gql`

    type Query{
        unsplashImages(pageNum: Int): [ImagePost]
        binnedImages: [ImagePost]
        userPostedImages: [ImagePost]
    }

    type ImagePost {
        id: String!
        url: String!
        posterName: String!
        description: String
        userPosted: Boolean!
        binned: Boolean!
    }

    type Mutation {
        uploadImage(
            url: String!
            description: String
            posterName: String
        ): ImagePost

        updateImage(
            id: String!
            url: String
            posterName: String
            description: String
            userPosted: Boolean
            binned: Boolean
        ): ImagePost

        deleteImage(id: String!): ImagePost    
    }
`;

const resolvers = {
    Query: {
        unsplashImages: async (_, args) => {
            const page = args.pageNum;
            const url = `https://api.unsplash.com/photos?page=${page}&client_id=${ACCESS_KEY}`;
            const { data } = await axios.get(url);
            // console.log(data);
            console.log(`Requesting page: ${page}`);
            const result = data.map((image) => {
                let imagePost = {
                    id: image.id,
                    url: image.urls.thumb,
                    posterName: image.user.username,
                    description: image.description,
                    userPosted: false
                }
                return imagePost
            });

            // console.log(result);
            return result;
        },
        binnedImages: async () => {
            let binnedImages = await client.lrangeAsync('binnedImages', 0, -1);
            binnedImages = binnedImages.map((imagePost) => {
                return JSON.parse(imagePost);
            });
            return binnedImages;

        },
        userPostedImages: async () => {
            let postedImages = await client.lrangeAsync('postedImages', 0, -1);
            postedImages = postedImages.map((imagePost) => {
                return JSON.parse(imagePost);
            });
            return postedImages;
        }
    },

    ImagePost: {
        binned: async (parentValue) => {
            let binnedImages = await client.lrangeAsync('binnedImages', 0, -1);
            binnedImages = binnedImages.map((imagePost) => {
                return JSON.parse(imagePost);
            })
            return binnedImages.filter((image) => image.id === parentValue.id).length > 0;
        }
    },

    Mutation: {

        uploadImage: async (_, args) => {

            console.log("args", args);
            const desc = args.description ? args.description : "";
            const author = args.posterName ? args.posterName : "Your self"
            const imagePost = {
                id: uuid.v4(),
                url: args.url,
                posterName: author,
                description: desc,
                userPosted: true,
                binned: false
            }
            await client.rpushAsync('postedImages', JSON.stringify(imagePost));
            // await client.rpushAsync('postedImages', imagePost);
            return imagePost;
        },

        updateImage: async (_, args) => {

            console.log("update reached")
            // console.log(args);

            let binnedImages;
            try {
                binnedImages = await client.lrangeAsync('binnedImages', 0, -1);
            } catch (e) {
                console.log(`Redis failed: ${e}`)
            }
            // let binnedImages = await client.lrangeAsync('binnedImages', 0, -1);
            binnedImages = binnedImages.map((imagePost) => {
                return JSON.parse(imagePost);
            })
            let isBinned = false;
            let result;
            binnedImages.map(async (imagePost, index) => {
                if (imagePost.id === args.id) {
                    console.log("Removing from Bin");
                    isBinned = true
                    if (!args.binned) { // unbinned
                        try {
                            await client.lsetAsync('binnedImages', index, "DELETED");
                            await client.lremAsync('binnedImages', 1, "DELETED");
                        } catch (e) {
                            console.log(`Redis failed: ${e}`)
                        }
                    }
                    result = imagePost;
                }
            })

            if (result) {
                console.log("result", result);
                return result;
            }

            if (args.binned && (!isBinned)) {
                console.log("adding to bin");
                const imagePost = {
                    id: args.id,
                    url: args.url,
                    posterName: args.posterName,
                    description: args.description,
                    userPosted: args.userPosted,
                    binned: true
                }
                try {
                    await client.rpushAsync('binnedImages', JSON.stringify(imagePost));
                } catch (e) {
                    console.log(`Redis failed: ${e}`)
                }

                let binnedImages = await client.lrangeAsync('binnedImages', 0, -1);
                binnedImages = binnedImages.map((imagePost) => {
                    return JSON.parse(imagePost);
                })
                result = binnedImages.filter((image) => image.id === args.id)[0];


                // await client.rpushAsync('binnedImages', imagePost);
                console.log(result);
                return imagePost;
            }

        },

        deleteImage: async (_, args) => {
            const postedImages = await client.lrangeAsync('postedImages', 0, -1);
            postedImages.map(async (imagePost, index) => {
                imagePost = JSON.parse(imagePost);
                if (imagePost.id === args.id) {
                    await client.lsetAsync('postedImages', index, "DELETED");
                    await client.lremAsync('postedImages', 1, "DELETED");
                }
                return imagePost;
            })

        }
    }
}

const server = new ApolloServer({ typeDefs, resolvers });

server.listen().then(({ url }) => {
    console.log(`🚀  Server ready at ${url} 🚀`);
});