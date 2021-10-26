import React from 'react';

import { characterDetailsUrl } from '../utils/apiURL';
import useAxios from '../utils/useAxios';

import { Link, useHistory } from 'react-router-dom';

const CharacterDetails = (props) => {

    let { data, loading } = useAxios(characterDetailsUrl(props.match.params.id));
    const history = useHistory();


    let series = [];
    let comics = [];
    let urls = [];
    if (!loading) {
        if (!data || data.data.results.length === 0) {
            history.push('/not-found');
        }
        comics = data.data.results[0].comics.items;
        series = data.data.results[0].series.items;
        urls = data.data.results[0].urls;
    }

    return (

        <div>
            {
                loading
                    ? 'Loading...'
                    : data.data.results.map((character) => {
                        let thumbnailPath = character.thumbnail.path;
                        let extension = character.thumbnail.extension;
                        return (
                            <div key={character.id} id={character.id}>
                                <img src={thumbnailPath + "." + extension} className="imageSize" alt="thumbnail" />
                                <h1>{character.name}</h1>
                                <p>{character.description}</p>
                            </div>
                        );
                    })
            }

            {
                comics.length > 0
                    ? <h2 className='sectionBreak'>Associated comics are:</h2>
                    : ''
            }

            {
                comics.length > 0
                    ? comics.map((item, index) => {
                        let details = item.resourceURI;
                        let comicId = details.substring(details.lastIndexOf("/") + 1, details.length);
                        return (
                            <div key={index} id={index}>
                                <Link to={`/comics/${comicId}`}>{item.name}</Link>
                            </div>)
                    })
                    : ''
            }


            {
                series.length > 0
                    ? <h2 className='sectionBreak'>Associated series are:</h2>
                    : ''
            }

            {
                series.length > 0
                    ? series.map((item, index) => {
                        let details = item.resourceURI;
                        let seriesId = details.substring(details.lastIndexOf("/") + 1, details.length);
                        return (
                            <div key={index} id={index}>
                                <Link to={`/series/${seriesId}`}>{item.name}</Link>
                            </div>
                        )
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

export default CharacterDetails;
