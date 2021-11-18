import React from 'react';

import { seriesUrl } from '../utils/apiURL';
import { Link, Redirect } from 'react-router-dom';
import useAxios from '../utils/useAxios';

const resultsPerPage = 20;

const SeriesList = (props) => {

    const pageOffset = props.match.params.page * resultsPerPage;
    let { data, loading } = useAxios(`${seriesUrl}&offset=${pageOffset}`);

    let nextExist = true; // assume till fetch finishes
    if (!loading) {
        if (!data || data.data.results.length === 0) {
            return (<Redirect to='/not-found' />);
        }
        let numPages = Math.ceil(data.data.total / resultsPerPage);
        console.log(numPages);
        nextExist = props.match.params.page < numPages - 1;

    }

    return (
        <div>
            <div className="pagination">
                {(!loading) && parseInt(props.match.params.page) > 0 && <Link to={`/series/page/${parseInt(props.match.params.page) - 1}`}>Previous</Link>}
                {(!loading) && nextExist && <Link to={`/series/page/${parseInt(props.match.params.page) + 1}`}>Next</Link>}
            </div>

            {loading
                ? 'Loading...'
                : data.data.results.map((series) => {
                    let thumbnailPath = series.thumbnail.path;
                    let extension = series.thumbnail.extension;
                    let details = series.resourceURI;
                    let charId = details.substring(details.lastIndexOf("/") + 1, details.length);
                    return (
                        <div key={series.id} id={series.id} className="card myCard">
                            <img src={thumbnailPath + "." + extension} className="card-img-top imageSize" alt="thumbnail" />
                            <Link to={`/series/${charId}`}>{series.title}</Link>

                        </div>
                    );
                })}
        </div>
    );
};

export default SeriesList;
