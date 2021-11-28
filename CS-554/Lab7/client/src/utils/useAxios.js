import { useEffect, useState } from 'react';
import axios from 'axios';

const useAxios = (url) => {
    const [state, setState] = useState({ data: null, loading: true });

    useEffect(() => {


        async function getData() {
            try {
                const { data } = await axios.get(url);
                setState({ data: data, loading: false });
            } catch (e) {
                console.log("errro in useAxios")
                setState({ data: null, loading: false });
            }
        }
        getData();
    }, [url, setState]);

    return state;
};

export default useAxios;