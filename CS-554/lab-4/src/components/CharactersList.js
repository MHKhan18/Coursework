import React from 'react';
import { charactersUrl } from '../utils/apiURL';

import useAxios from '../utils/useAxios';
import { Link, useHistory } from 'react-router-dom';


const resultsPerPage = 20;

const CharactersList = (props) => {


    const pageOffset = props.match.params.page * resultsPerPage;
    let { data, loading } = useAxios(`${charactersUrl}&offset=${pageOffset}`);
    const history = useHistory();

    let nextExist = true; // assume till fetch finishes
    if (!loading) {
        if (!data || data.data.results.length === 0) {
            history.push('/not-found');
        }
        let numPages = Math.ceil(data.data.total / resultsPerPage);
        console.log(numPages);
        nextExist = props.match.params.page < numPages - 1;
        if (data.data.results.length === 0) {
            history.push('/not-found');
        }
    }

    return (
        <div>
            <div className="pagination">
                {(!loading) && parseInt(props.match.params.page) > 0 && <Link to={`/characters/page/${parseInt(props.match.params.page) - 1}`}> Previous</Link>}
                {(!loading) && nextExist && <Link to={`/characters/page/${parseInt(props.match.params.page) + 1}`}>Next</Link>}
            </div>

            {loading
                ? 'Loading...'
                : data.data.results.map((character) => {
                    let thumbnailPath = character.thumbnail.path;
                    let extension = character.thumbnail.extension;
                    let details = character.resourceURI;
                    let charId = details.substring(details.lastIndexOf("/") + 1, details.length);
                    return (

                        <div key={character.id} id={character.id} className="card myCard">
                            <img src={thumbnailPath + "." + extension} className="card-img-top imageSize" alt="thumbnail" />
                            <Link to={`/characters/${charId}`}>{character.name}</Link>
                        </div>


                    );
                })}
        </div>

    );
};

export default CharactersList;
