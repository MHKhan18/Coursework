import useAxios from '../utils/useAxios';
import { Navigate, useParams } from 'react-router-dom';

import { useSelector, useDispatch } from 'react-redux';
import actions from '../actions';


const PokemonDetail = () => {

    const { id } = useParams();
    const url = `http://localhost:4000/pokemon/${id}`;

    let { data, loading } = useAxios(url);
    const fullState = useSelector((state) => state.trainers);
    const dispatch = useDispatch();



    if (loading) {
        return (<div>Loading...</div>)
    }

    if (!data) { // server returned error
        return (<Navigate to='/not-found' />);
    }

    const catchPokemon = () => {
        dispatch(actions.catchPokemon(parseInt(id), data.name));
    }

    const releasePokemon = () => {
        dispatch(actions.releasePokemon(parseInt(id)));
    }

    let isFull = fullState.isPartyFull;
    let catchedPokemons = fullState.catchedPokemons;
    let filtered = catchedPokemons.filter(pokemon => parseInt(pokemon.id) === parseInt(id));
    let notCatched = filtered.length === 0;

    return (
        <div>

            <h1>{data.name}</h1>
            {isFull ? (<div>Party Full</div>) : ''}
            {
                (!notCatched) // catched
                    ?
                    (<button onClick={releasePokemon}>Release</button>)
                    :
                    (!isFull)
                        ?
                        (<button onClick={catchPokemon}>Catch</button>)
                        :
                        ''
            }

            <img src={`https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${id}.png`}
                className="imageSize" alt="thumbnail"
            />

            <dl>
                <dt>Base Experience:</dt>
                <dd>{data.base_experience}</dd>
                <dt>Height:</dt>
                <dd>{data.height}</dd>
                <dt>Weight:</dt>
                <dd>{data.weight}</dd>
            </dl>


            {(data.abilities && data.abilities.length > 0) ? <h5>Abilities:</h5> : ''}

            {
                (data.abilities && data.abilities.length > 0)
                    ?
                    data.abilities.map((ability, index) => {
                        return (
                            <div key={index}>
                                {ability.ability.name}
                            </div>
                        )
                    })
                    :
                    ''
            }

            <br />
            <br />

            {(data.moves && data.moves.length > 0) ? <h5>Moves:</h5> : ''}

            {
                (data.moves && data.moves.length > 0)
                    ?
                    data.moves.map((move, index) => {
                        return (
                            <div key={index}>
                                {move.move.name}
                            </div>
                        )
                    })
                    :
                    ''
            }

            <br />
            <br />

            {(data.types && data.types.length > 0) ? <h5>Types:</h5> : ''}

            {
                (data.types && data.types.length > 0)
                    ?
                    data.types.map((type, index) => {
                        return (
                            <div key={index}>
                                {type.type.name}
                            </div>
                        )
                    })
                    :
                    ''
            }



        </div >
    );
};

export default PokemonDetail;