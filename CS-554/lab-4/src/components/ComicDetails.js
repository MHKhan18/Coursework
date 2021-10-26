import React from 'react';
import { comicDetailsUrl } from '../utils/apiURL';
import useAxios from '../utils/useAxios';

import { Link, useHistory } from 'react-router-dom';


function getId(url) {
    const id = url.substring(url.lastIndexOf("/") + 1, url.length);
    return id;
}

const ComicDetails = (props) => {

    let { data, loading } = useAxios(comicDetailsUrl(props.match.params.id));
    const history = useHistory();


    let series = {};
    let variants = [];


    if (!loading) {
        if (!data || data.data.results.length === 0) {
            history.push('/not-found');
        }
        series = data.data.results[0].series;
        variants = data.data.results[0].variants;
    }

    return (

        <div>
            {
                loading
                    ? 'Loading...'
                    : data.data.results.map((comic) => {
                        let thumbnailPath = comic.thumbnail.path;
                        let extension = comic.thumbnail.extension;
                        return (
                            <div key={comic.id} id={comic.id}>
                                <img src={thumbnailPath + "." + extension} className="imageSize" alt="thumbnail" />
                                <h1>{comic.title}</h1>
                                <p>{comic.description}</p>
                            </div>
                        );
                    })
            }

            {
                (series.name && series.name.length > 0)
                    ?
                    <div>
                        <h2 className='sectionBreak'>Associated with the series:</h2>
                        <Link to={`/series/${getId(series.resourceURI)}`}>{series.name}</Link>
                    </div>
                    : ''
            }

            {
                variants.length > 0
                    ? <h2 className='sectionBreak'>Variants are:</h2>
                    : ''
            }

            {
                variants.length > 0
                    ? variants.map((item, index) => {
                        return (
                            <div key={index} id={index}>
                                <Link to={`/comics/${getId(item.resourceURI)}`}>{item.name}</Link>
                            </div>
                        )
                    })
                    : ''
            }
        </div >

    );
};

export default ComicDetails;
