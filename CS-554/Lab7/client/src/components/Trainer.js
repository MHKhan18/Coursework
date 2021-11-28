import { useDispatch } from 'react-redux';
import actions from '../actions';

const Trainer = (props) => {

    // can get select
    // can get removed

    const dispatch = useDispatch();

    const selectTrainer = () => {
        dispatch(actions.selectTrainer(props.data.id));
    }

    const removeTrainer = () => {
        dispatch(actions.removeTrainer(props.data.id));
    }

    const options = props.data.selected
        ? (<div>Selected</div>)
        : (
            <div>
                <button onClick={selectTrainer}>Select Trainer</button>
                <button onClick={removeTrainer}>Delete Trainer</button>
            </div>
        );

    const team = props.data.team.map((pokemon) => {
        return (
            <div>
                <img src={`https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/${pokemon.id}.png`}
                    alt="thumbnail" />
                <div>{pokemon.name}</div>
            </div>
        )
    })


    return (
        <div>
            <h1>Trainer: {props.data.name}</h1>
            {options}
            {team}

        </div>
    );
};

export default Trainer;