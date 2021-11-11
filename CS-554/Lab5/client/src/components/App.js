import React from 'react';
import './App.css';
import { NavLink, BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Home from './Home';
import BinnedPost from './BinnedPosts';
import CreatedPost from './CreatedPosts';
import AddPost from './AddPost';

import {
  ApolloClient,
  HttpLink,
  InMemoryCache,
  ApolloProvider
} from '@apollo/client';
const client = new ApolloClient({
  cache: new InMemoryCache(
    {
      typePolicies: {
        Query: {
          fields: {
            unsplashImages: {
              // Don't cache separate results based on
              // any of this field's arguments.
              keyArgs: false,
              // Concatenate the incoming list items with
              // the existing list items.
              merge(existing = [], incoming) {
                return [...existing, ...incoming];
              },
            }
          }
        }
      }
    }
  ),
  link: new HttpLink({
    uri: 'http://localhost:4000'
  })
});

function App() {
  return (
    <ApolloProvider client={client}>
      <Router>
        <div>
          <header className="App-header">
            <h1 className="App-title">
              Binterest
            </h1>
            <nav>
              <NavLink className="navlink" to="/">
                Images
              </NavLink>
              <NavLink className="navlink" to="/my-bin">
                My Bin
              </NavLink>

              <NavLink className="navlink" to="/my-posts">
                My Posts
              </NavLink>
            </nav>
          </header>

          <Routes>
            <Route exact path="/" element={<Home />} />
            <Route path="/my-bin" element={<BinnedPost />} />
            <Route path="/my-posts" element={<CreatedPost />} />
            <Route path="/new-post" element={<AddPost />} />
          </Routes>
        </div>
      </Router>
    </ApolloProvider>
  );
}

export default App;
