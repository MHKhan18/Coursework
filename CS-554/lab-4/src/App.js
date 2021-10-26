import './App.css';

import Home from './components/Home';

import CharactersList from './components/CharactersList';
import ComicsList from './components/ComicsList';
import SeriesList from './components/SeriesList';

import CharacterDetails from './components/CharacterDetails';
import ComicDetails from './components/ComicDetails';
import SeriesDetails from './components/SeriesDetails';

import NotFound from './components/NotFound';

import { BrowserRouter as Router, Route, Link } from 'react-router-dom';




function App() {
  return (
    <Router>
      <div className="App">
        <header className="App-header">
          <p>This is a React.js exercise with Marvel API</p>
          <Link className='showlink' to='/'>
            Home
          </Link>
        </header>
        <br />
        <br />
        <div className='App-body'>
          <Route exact path='/' component={Home} />

          <Route exact path='/characters/page/:page' component={CharactersList} />
          <Route exact path='/comics/page/:page' component={ComicsList} />
          <Route exact path='/series/page/:page' component={SeriesList} />

          <Route exact path='/characters/:id' component={CharacterDetails} />
          <Route exact path='/comics/:id' component={ComicDetails} />
          <Route exact path='/series/:id' component={SeriesDetails} />

          <Route exact path='/not-found' component={NotFound} />

        </div>
      </div>

    </Router>

  );
}

export default App;
