
const addTrainer = (name) => ({
    type: 'ADD_TRAINER',
    payload: {
        trainerName: name
    }
});

const removeTrainer = (id) => ({
    type: 'REMOVE_TRAINER',
    payload: {
        trainerID: id
    }
});

const selectTrainer = (id) => ({
    type: 'SELECT_TRAINER',
    payload: {
        trainerID: id
    }
});

const catchPokemon = (id, name) => ({
    type: 'CATCH_POKEMON',
    payload: {
        pokemonId: id,
        pokemonName: name
    }
});

const releasePokemon = (id) => ({
    type: 'RELEASE_POKEMON',
    payload: {
        pokemonId: id
    }
});

module.exports = {
    addTrainer,
    removeTrainer,
    selectTrainer,
    catchPokemon,
    releasePokemon
}