import './App.css';

import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';

import Home from './components/Home';
import Pokemons from './components/Pokemons';
import Pokemon from './components/Pokemon';
import Trainers from './components/Trainers';
import NotFound from './components/NotFound';

function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <p>CS-554 Lab-7: React-Redux</p>
          <Link className='showlink' to='/'>
            Home
          </Link>
          <Link className='showlink' to='/pokemon/page/0'>
            Pokemons
          </Link>
          <Link className='showlink' to='/pokemon/trainers'>
            Trainers
          </Link>
        </header>
        <br />
        <br />
        <div className='App-body'>
          <Routes>
            <Route exact path='/' element={<Home />} />
            <Route exact path='/pokemon/page/:page' element={<Pokemons />} />
            <Route exact path='/pokemon/:id' element={<Pokemon />} />
            <Route exact path='/pokemon/trainers' element={<Trainers />} />
            <Route exact path='/not-found' element={<NotFound />} />
          </Routes>

        </div>
      </div>

    </Router >

  );
}

export default App;
