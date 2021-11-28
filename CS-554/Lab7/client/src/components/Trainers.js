import { useSelector } from 'react-redux';

import AddTrainer from "./AddTrainer";
import Trainer from './Trainer';

const Trainers = () => {

    const allTrainers = useSelector((state) => state.trainers);

    const trainers = allTrainers.trainers.map((trainer) => {
        return (
            <Trainer key={trainer.id} data={trainer} />
        );
    });

    return (
        <div>
            <AddTrainer />
            {
                trainers
            }
        </div>
    );
};

export default Trainers;