import { useState } from 'react';
import { useDispatch } from 'react-redux';
import actions from '../actions';

const AddTrainer = () => {

    const dispatch = useDispatch();
    const [formData, setFormData] = useState({ name: '' });

    const handleChange = (e) => {
        setFormData((prev) => ({ ...prev, [e.target.name]: e.target.value }));
    };

    const addTrainer = () => {
        dispatch(actions.addTrainer(formData.name));
        document.getElementById('name').value = '';
    };

    return (
        <div>
            <h1> Add Trainer </h1>
            <div className="input-selection">
                <label>
                    Trainer:
                    <input
                        onChange={(e) => handleChange(e)}
                        id="name"
                        name="name"
                        placeholder="Trainer name..."
                    />
                </label>
            </div>
            <button onClick={addTrainer}>Add Trainer</button>
        </div>
    );
};

export default AddTrainer;