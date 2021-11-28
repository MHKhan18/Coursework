import { Link } from 'react-router-dom';
import { useDispatch } from 'react-redux';
import actions from '../actions';


const Pokemon = (props) => {

    const dispatch = useDispatch();

    const catchPokemon = () => {
        dispatch(actions.catchPokemon(props.id, props.name));
    }

    const releasePokemon = () => {
        dispatch(actions.releasePokemon(props.id));
    }


    return (

        <div className="card">
            <div className="card-body">
                <img src={`https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${props.id}.png`}
                    className="card-img-top imageSize" alt="thumbnail" />
                <p><Link to={`/pokemon/${props.id}`}>{props.name || "N/A"}</Link></p>
                {props.isFull ? (<div>Party Full</div>) : ''}


                {
                    (!props.notCatched) // catched
                        ?
                        (<button onClick={releasePokemon}>Release</button>)
                        :
                        (!props.isFull)
                            ?
                            (<button onClick={catchPokemon}>Catch</button>)
                            :
                            ''

                }
            </div>
        </div>

    );
};

export default Pokemon;