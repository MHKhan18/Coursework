import React from 'react';
import { Link } from 'react-router-dom';

const Home = () => {

    return (
        <div>
            <ul>

                <li><Link className='showlink' to='/characters/page/0'>
                    Characters List
                </Link></li>
                <li><Link className='showlink' to='/comics/page/0'>
                    Comics List
                </Link></li>
                <li><Link className='showlink' to='/series/page/0'>
                    Series List
                </Link></li>
            </ul>
        </div >
    );
};

export default Home;
