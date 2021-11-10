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

const exported = {
    GET_IMAGES,
    EDIT_IMAGE
};

export default exported;