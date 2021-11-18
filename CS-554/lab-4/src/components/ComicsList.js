import React from 'react';

import { comicsUrl } from '../utils/apiURL';
import { Link, Redirect } from 'react-router-dom';
import useAxios from '../utils/useAxios';

const resultsPerPage = 20;

const ComicsList = (props) => {

    const pageOffset = props.match.params.page * resultsPerPage;
    let { data, loading } = useAxios(`${comicsUrl}&offset=${pageOffset}`);


    let nextExist = true; // assume till fetch finishes
    if (!loading) {
        if (!data || data.data.results.length === 0) {
            return (<Redirect to='/not-found' />);
        }
        let numPages = Math.ceil(data.data.total / resultsPerPage);
        console.log(numPages);
        nextExist = props.match.params.page < (numPages - 2);
    }

    return (
        <div>
            <div className="pagination">
                {(!loading) && parseInt(props.match.params.page) > 0 && <Link to={`/comics/page/${parseInt(props.match.params.page) - 1}`}>Previous</Link>}
                {(!loading) && nextExist && <Link to={`/comics/page/${parseInt(props.match.params.page) + 1}`}>Next</Link>}
            </div>

            {loading
                ? 'Loading...'
                : data.data.results.map((comic) => {
                    let thumbnailPath = comic.thumbnail.path;
                    let extension = comic.thumbnail.extension;
                    let details = comic.resourceURI;
                    let charId = details.substring(details.lastIndexOf("/") + 1, details.length);
                    return (
                        <div key={comic.id} id={comic.id} className="card myCard">
                            <img src={thumbnailPath + "." + extension} className="card-img-top imageSize" alt="thumbnail" />
                            <Link to={`/comics/${charId}`}>{comic.title}</Link>

                        </div>
                    );
                })}
        </div>
    );
};

export default ComicsList;
