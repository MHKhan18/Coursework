import React from 'react';
import './App.css';

import queries from '../queries';
import { useQuery, useMutation } from '@apollo/client';

import { useState } from 'react';

import DisplayImage from './DisplayImage';


function Home() {

    const [pageFetched, setPageFetched] = useState(2);
    const [editImage, { editData, editLoading, editError }] = useMutation(queries.EDIT_IMAGE, {
        refetchQueries: [
            'GET_BINNED_IMAGES', 'GET_IMAGES', 'GET_ADDED_IMAGES'
        ]
    });

    const { loading, error, data, fetchMore } = useQuery(queries.GET_IMAGES,
        {
            nextFetchPolicy: "cache-first",
            variables: {
                'pageNum': 1
            }
        });

    if (error) {
        console.log(error);
    }

    if (editError) {
        console.log(editError);
    }

    function getMore() {
        setPageFetched(pageFetched + 1);
        fetchMore({
            variables: {
                pageNum: pageFetched
            }
        });
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

        const { unsplashImages } = data;

        let body = unsplashImages.map((image) =>
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
                canDel={false}
                deleteHandler={() => null}
            />
        );

        return (
            <div>
                <ul>
                    {body}
                </ul>
                <button onClick={() => getMore()}>Get More</button>
            </div >
        );
    }

    else if (loading) {
        return <div>Loading</div>;
    } else if (error) {
        return <div>{error.message}</div>;
    }

}

export default Home;