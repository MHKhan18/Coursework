
import useAxios from '../utils/useAxios';
import { Link, Navigate, useParams } from 'react-router-dom';
import { useSelector } from 'react-redux';

import Pokemon from './Pokemon';


const Pokemons = () => {

    const { page } = useParams();
    const pageNum = parseInt(page);
    const prevExists = !isNaN(pageNum) && pageNum > 0;
    let nextExists = !isNaN(pageNum) && true; // Assume until data loads

    const url = `http://localhost:4000/pokemon/page/${pageNum}`;

    let { data, loading } = useAxios(url);
    const fullState = useSelector((state) => state.trainers);
    let isFull = fullState.isPartyFull;
    let catchedPokemons = fullState.catchedPokemons;


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
            let parts = pokemon.url.split("/");
            let id = parseInt(parts[parts.length - 2]);
            let filtered = catchedPokemons.filter(pokemon => parseInt(pokemon.id) === parseInt(id));
            let notCatched = filtered.length === 0;
            return (<Pokemon
                key={index}
                name={pokemon.name}
                id={id}
                isFull={isFull}
                notCatched={notCatched}
            />)
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