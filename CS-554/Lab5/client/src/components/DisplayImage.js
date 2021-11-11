import React from 'react';
import './App.css';
import { useState } from 'react';

function DisplayImage(props) {

    const [showAddBin, setAddBin] = useState(props.addBin);
    const [showRemoveBin, setRemoveBin] = useState(props.removeBin);
    const [showDelete, setShowDelete] = useState(props.canDel);

    const addBinHandler = () => {
        props.addHandler();
        setAddBin(false);
        setRemoveBin(true);
    }

    const removeBinHandler = () => {
        props.removeHandler();
        setRemoveBin(false);
        setAddBin(true);
    }

    const deletePostHandler = () => {
        props.deleteHandler();
        setShowDelete(false);
    }

    if (props.canDel) {
        if (showDelete) {
            return (
                < li >
                    <div className="card">
                        <div className="card-body">
                            <img src={props.link} className="card-img-top imageSize" alt="thumbnail" />
                            <p>{props.desc || "N/A"}</p>
                            <p>By: {props.author}</p>
                            {
                                showAddBin &&
                                <button onClick={addBinHandler}>
                                    Add To Bin
                                </button>
                            }
                            {
                                showRemoveBin &&
                                <button onClick={removeBinHandler}>
                                    Remove From Bin
                                </button>
                            }
                            <button onClick={deletePostHandler}>
                                Delete Post
                            </button>
                        </div>
                    </div>
                </li >
            );

        } else {
            return (
                <li></li>
            )
        }
    }
    else if (!props.binRoute || showRemoveBin) {
        return (
            < li >
                <div className="card">
                    <div className="card-body">
                        <img src={props.link} className="card-img-top imageSize" alt="thumbnail" />
                        <p>{props.desc || "N/A"}</p>
                        <p>By: {props.author}</p>
                        {
                            showAddBin &&
                            <button onClick={addBinHandler}>
                                Add To Bin
                            </button>
                        }
                        {
                            showRemoveBin &&
                            <button onClick={removeBinHandler}>
                                Remove From Bin
                            </button>
                        }
                    </div>
                </div>
            </li >
        );
    } else {
        return (
            <li></li>
        )
    }
}

export default DisplayImage;