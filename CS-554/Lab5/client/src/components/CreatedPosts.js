import React from 'react';
import './App.css';

import queries from '../queries';
import { useQuery, useMutation } from '@apollo/client';

import { NavLink } from 'react-router-dom';

import DisplayImage from './DisplayImage';


function CreatedPost() {

    const [editImage, { editData, editLoading, editError }] = useMutation(queries.EDIT_IMAGE, {
        refetchQueries: [
            queries.GET_BINNED_IMAGES, queries.GET_IMAGES, queries.GET_ADDED_IMAGES
        ]
    });


    const [deletePost] = useMutation(queries.DELETE_POST, {
        refetchQueries: [
            'GET_BINNED_IMAGES', 'GET_IMAGES', 'GET_ADDED_IMAGES'
        ]
    });


    const { loading, error, data } = useQuery(queries.GET_ADDED_IMAGES, { fetchPolicy: 'cache-and-network' });

    const addBinHandler = (id, url, posterName, description, userPosted) => {
        editImage({
            variables: {
                id: id,
                url: url,
                posterName: posterName,
                description: description,
                userPosted: Boolean(userPosted),
                binned: true
            }
        });
    }

    const removeBinHandler = (id, url, posterName, description, userPosted) => {
        editImage({
            variables: {
                id: id,
                url: url,
                posterName: posterName,
                description: description,
                userPosted: Boolean(userPosted),
                binned: false
            }
        });
    }

    const deletePostHandler = (id) => {
        deletePost({
            variables: {
                id: id
            }
        });
    }


    if (data) {

        const { userPostedImages } = data;

        let body = userPostedImages.map((image) =>
            <DisplayImage
                key={image.id}
                link={image.url}
                desc={image.description}
                author={image.posterName}
                addBin={!image.binned}
                removeBin={image.binned}
                addHandler={() => addBinHandler(image.id, image.url, image.posterName, image.description, image.userPosted)}
                removeHandler={() => removeBinHandler(image.id, image.url, image.posterName, image.description, image.userPosted)}
                binRoute={false}
                canDel={true}
                deleteHandler={() => deletePostHandler(image.id)}
            />
        );

        return (
            <div>
                <ul>
                    {body}
                </ul>
                <NavLink className="formLink" to="/new-post">
                    Create Post
                </NavLink>
            </div >
        );
    }

    else if (loading) {
        return <div>Loading</div>;
    } else if (error) {
        return <div>{error.message}</div>;
    }
}

export default CreatedPost;