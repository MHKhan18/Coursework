import React from 'react';
import './App.css';
import queries from '../queries';
import { useMutation } from '@apollo/client';

function AddPost() {

    let url;
    let desc;
    let author;

    const [addPost, { data, loading, error }] = useMutation(queries.ADD_IMAGE, {
        refetchQueries: [
            'GET_ADDED_IMAGES'
        ]
    });

    let body = (
        <form className="form" id="add-post" onSubmit={(e) => {
            e.preventDefault();
            addPost({
                variables: {
                    url: url.value,
                    description: desc.value,
                    posterName: author.value
                }
            });
            url.value = '';
            desc.value = '';
            author.value = '';
            alert('Post Added');
        }}
        >
            <div className="form-group">
                <label>
                    URL:
                    <br />
                    <input
                        ref={(node) => {
                            url = node;
                        }}
                        required
                        autoFocus={true}
                    />
                </label>
            </div>
            <br />
            <div className="form-group">
                <label>
                    Description:
                    <br />
                    <input
                        ref={(node) => {
                            desc = node;
                        }}
                        required
                    />
                </label>
            </div>
            <br />
            <div className="form-group">
                <label>
                    Poster Name:
                    <br />
                    <input
                        ref={(node) => {
                            author = node;
                        }}
                        required
                    />
                </label>
            </div>

            <button className="button" type="submit">
                Add Post
            </button>
        </form >
    );

    return (
        <div>
            {body}
        </div>
    );
}

export default AddPost;