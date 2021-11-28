
import useAxios from '../utils/useAxios';
import { Link, Navigate, useParams } from 'react-router-dom';

import Pokemon from './Pokemon';


const Pokemons = (props) => {

    const { page } = useParams();
    const pageNum = parseInt(page);
    const prevExists = !isNaN(pageNum) && pageNum > 0;
    let nextExists = !isNaN(pageNum) && true; // Assume until data loads

    const url = `http://localhost:4000/pokemon/page/${pageNum}`;

    let { data, loading } = useAxios(url);


    let pagination = (<div></div>);
    let items = (<div></div>);

    if (!loading) {
        if (!data) { // server returned error
            return (<Navigate to='/not-found' />);
        }

        nextExists = !isNaN(pageNum) && Boolean(data.next);

        pagination = (
            < div className="pagination" >
                {(!loading) && prevExists && <Link to={`/pokemon/page/${pageNum - 1}`}> Previous</Link>
                }
                {(!loading) && nextExists && <Link to={`/pokemon/page/${pageNum + 1}`}>Next</Link>}
            </div >
        )

        items = data.results.map((pokemon, index) => {
            return <Pokemon
                key={index}
                name={pokemon.name}
                id={pokemon.url}
            />
        });
    }


    return (
        <div>
            {pagination}

            {loading
                ? 'Loading...'
                : items
            }
        </div>

    );
};

export default Pokemons;