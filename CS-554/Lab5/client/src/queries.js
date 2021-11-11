import { gql } from '@apollo/client';


const GET_IMAGES = gql`
    query GET_IMAGES($pageNum: Int!){
        unsplashImages(pageNum: $pageNum){
            id
            url
            posterName
            description
            binned
        }
    }
`;

const GET_BINNED_IMAGES = gql`
    query GET_BINNED_IMAGES{
        binnedImages{
            id
            url
            posterName
            description
            binned
        }
    }
`

const EDIT_IMAGE = gql`
    mutation changeImage(
        $id: String!
        $url: String!
        $posterName: String
        $description: String
        $userPosted: Boolean!
        $binned: Boolean!
    ){
        updateImage(
            id: $id
            url: $url
            posterName: $posterName
            description: $description
            userPosted: $userPosted
            binned: $binned
        ){
            id
            url
            posterName
            description
            userPosted
            binned
        }
    }
`;

const ADD_IMAGE = gql`
    mutation ADD_IMAGE(
        $url: String!
        $description: String
        $posterName: String
    ){
        uploadImage(
            url: $url
            description: $description
            posterName: $posterName
        ){
            id
            url
            posterName
            description
            userPosted
            binned
        }
    }
`;

const GET_ADDED_IMAGES = gql`
    query GET_ADDED_IMAGES{
        userPostedImages{
            id
            url
            posterName
            description
            userPosted
            binned
        }
    }
`;

const DELETE_POST = gql`
    mutation DELETE_POST(
        $id: String!
    ){
        deleteImage(
            id: $id
        ){
            id
            url
            posterName
            description
            userPosted
            binned
        }
    }
`;

const exported = {
    GET_IMAGES,
    EDIT_IMAGE,
    GET_BINNED_IMAGES,
    ADD_IMAGE,
    GET_ADDED_IMAGES,
    DELETE_POST
};

export default exported;