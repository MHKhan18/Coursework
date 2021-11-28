import { v4 as uuid } from 'uuid';

const initialState = {
    isPartyFull: false,
    trainers: [
        {
            id: uuid(),
            name: 'Red',
            selected: true,
            team: [
                {
                    id: '1',
                    name: 'bulbasaur'
                }
            ]
        }
    ],
    catchedPokemons: [
        {
            id: '1',
            name: 'bulbasaur'
        }
    ]
};

const trainerReducer = (state = initialState, action) => {

    const { type, payload } = action;

    let copiedState = null;
    let trainers = null;
    let updatedTrainers = null;

    switch (type) {

        case 'ADD_TRAINER': // ARGS: payload.trainerName
            copiedState = JSON.parse(JSON.stringify(state));
            copiedState.trainers.push({
                id: uuid(),
                name: payload.trainerName,
                selected: false,
                team: []
            });
            return copiedState;

        case 'REMOVE_TRAINER': // ARGS: payload.trainerID
            copiedState = JSON.parse(JSON.stringify(state));
            trainers = copiedState.trainers;
            let remainingTrainers = trainers.filter(trainer => trainer.id !== payload.trainerID);
            copiedState.trainers = remainingTrainers;
            return copiedState;


        case 'SELECT_TRAINER': // ARGS: payload.trainerID
            copiedState = JSON.parse(JSON.stringify(state));
            trainers = copiedState.trainers;


            updatedTrainers = trainers.map((trainer) => {
                trainer.selected = false; // deselect previous
                if (trainer.id === payload.trainerID) {
                    trainer.selected = true;
                    copiedState.isPartyFull = trainer.team.length >= 6;
                    copiedState.catchedPokemons = trainer.team;
                }
                return trainer;
            });

            copiedState.trainers = updatedTrainers;

            return copiedState;



        case 'CATCH_POKEMON': // ARGS: payload.pokemonId , payload.pokemonName
            copiedState = JSON.parse(JSON.stringify(state));
            trainers = copiedState.trainers;

            updatedTrainers = trainers.map((trainer) => {
                if (trainer.selected) {
                    trainer.team.push({
                        id: payload.pokemonId,
                        name: payload.pokemonName
                    });
                    copiedState.isPartyFull = trainer.team.length >= 6;
                    copiedState.catchedPokemons = trainer.team;
                }
                return trainer;
            });

            copiedState.trainers = updatedTrainers;
            return copiedState;


        case 'RELEASE_POKEMON': // ARGS: payload.pokemonId
            copiedState = JSON.parse(JSON.stringify(state));
            trainers = copiedState.trainers;

            updatedTrainers = trainers.map((trainer) => {
                if (trainer.selected) {
                    let team = trainer.team;
                    let filteredTeam = team.filter(pokemon => pokemon.id !== payload.pokemonId);
                    trainer.team = filteredTeam;
                    copiedState.isPartyFull = trainer.team.length >= 6;
                    copiedState.catchedPokemons = trainer.team;
                }
                return trainer;
            });

            copiedState.trainers = updatedTrainers;
            return copiedState;

        default:
            return state;
    }
};

export default trainerReducer;