import React from 'react';
import './App.css';

import queries from '../queries';
import { useQuery, useMutation } from '@apollo/client';


import DisplayImage from './DisplayImage';


function BinnedPost() {

    const [editImage, { editData, editLoading, editError }] = useMutation(queries.EDIT_IMAGE, {
        refetchQueries: [
            queries.GET_BINNED_IMAGES, queries.GET_IMAGES, queries.GET_ADDED_IMAGES
        ]
    });
    const { loading, error, data } = useQuery(queries.GET_BINNED_IMAGES, { fetchPolicy: 'cache-and-network' });

    if (error) {
        console.log(error);
    }

    if (editError) {
        console.log(editError);
    }

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

    if (data) {

        const { binnedImages } = data;

        let body = binnedImages.map((image) =>
            <DisplayImage
                key={image.id}
                link={image.url}
                desc={image.description}
                author={image.posterName}
                addBin={!image.binned}
                removeBin={image.binned}
                addHandler={() => addBinHandler(image.id, image.url, image.posterName, image.description, image.userPosted)}
                removeHandler={() => removeBinHandler(image.id, image.url, image.posterName, image.description, image.userPosted)}
                binRoute={true}
                canDel={false}
                deleteHandler={() => null}
            />
        );

        return (
            <div>
                <ul>
                    {body}
                </ul>
            </div >
        );
    }

    else if (loading) {
        return <div>Loading</div>;
    } else if (error) {
        return <div>{error.message}</div>;
    }
}

export default BinnedPost;