import React from 'react';
import { seriesDetailsUrl } from '../utils/apiURL';
import useAxios from '../utils/useAxios';

import { Link, Redirect } from 'react-router-dom';


function getId(url) {
    const id = url.substring(url.lastIndexOf("/") + 1, url.length);
    return id;
}

const SeriesDetails = (props) => {

    let { data, loading } = useAxios(seriesDetailsUrl(props.match.params.id));



    let characters = [];
    let comics = [];
    let urls = [];

    if (!loading) {
        if (!data || data.data.results.length === 0) {
            return (<Redirect to='/not-found' />);
        }
        comics = data.data.results[0].comics.items;
        characters = data.data.results[0].characters.items;
        urls = data.data.results[0].urls;
    }

    return (
        <div>
            {
                loading
                    ? 'Loading...'
                    : data.data.results.map((series) => {
                        let thumbnailPath = series.thumbnail.path;
                        let extension = series.thumbnail.extension;
                        return (
                            <div key={series.id} id={series.id}>
                                <img src={thumbnailPath + "." + extension} className="imageSize" alt="thumbnail" />
                                <h1>{series.title}</h1>
                                <p>{series.description}</p>
                            </div>
                        );
                    })
            }

            {
                comics.length > 0
                    ? <h2 className='sectionBreak'>Comics in the series are:</h2>
                    : ''
            }

            {
                comics.length > 0
                    ? comics.map((item, index) => {
                        return (
                            <div key={index} id={index}>
                                <Link to={`/comics/${getId(item.resourceURI)}`}>{item.name}</Link>
                            </div>)
                    })
                    : ''
            }

            {
                characters.length > 0
                    ? <h2 className='sectionBreak'>Characters in the series are:</h2>
                    : ''
            }

            {
                characters.length > 0
                    ? characters.map((item, index) => {
                        return (
                            <div key={index} id={index}>
                                <Link to={`/characters/${getId(item.resourceURI)}`}>{item.name}</Link>
                            </div>)
                    })
                    : ''
            }

            {
                urls.length > 0
                    ? <h2 className='sectionBreak'>Check out:</h2>
                    : ''
            }

            {
                urls.length > 0
                    ? urls.map((item, index) => {
                        return (
                            <div key={index} id={index}>
                                <a href={item.url} target="_blank" rel="noreferrer noopener">{item.type}</a>
                            </div>
                        )
                    })
                    : ''
            }
        </div>


    );
};

export default SeriesDetails;
