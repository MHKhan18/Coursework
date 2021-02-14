const {MissingArgumentError , ArgumentTypeError, InvalidRangeError} = require('./errors');

const makeArrays = (objs) => {

    if (objs === undefined){
        throw new MissingArgumentError('objs');
    }

    if(!Array.isArray(objs)){
        throw new ArgumentTypeError('Input must be an array.');
    }

    objs.forEach((obj) => {
        if (typeof obj !== 'object'){
            throw ArgumentTypeError('All elements in input array must be objects.');
        }

        if (Object.keys(obj).length === 0){
            throw InvalidRangeError('Input objects can not be empty.');
        }
    });

    if(objs.length < 2){
        throw new InvalidRangeError('Input array must contain at least two objects');
    }

    const res = [];
    objs.forEach((obj) => {
        for (let key in obj){
            res.push([key , obj[key]]);
        }
    });

    return res;

}

const isDeepEqual = (obj1, obj2) => {

    if (obj1 === undefined){
        throw new MissingArgumentError('obj1');
    }

    if ( (typeof obj1 !== 'object') || (Array.isArray(obj1)) ){
        throw new ArgumentTypeError('obj1 must be an object.');
    }

    if (obj2 === undefined){
        throw new MissingArgumentError('obj2');
    }

    if ( (typeof obj2 !== 'object') || (Array.isArray(obj2)) ){
        throw new ArgumentTypeError('obj2 must be an object.');
    }

    if (Object.keys(obj1).length !== Object.keys(obj2).length){
        return false;
    }

    for (let key in obj1){
        if (!(key in obj2)){
            return false;
        }
        const val1 = obj1[key];
        const val2 = obj2[key];

        if (typeof val1 !== typeof val2){
            return false;
        }

        if (typeof val1 === 'object'){
            if (!isDeepEqual(val1 , val2)){
                return false;
            }
        } else {
            if (val1 !== val2){
                return false;
            }
        }
    }

    return true;

}

const computeObject = (obj , func) => {

    if (obj === undefined){
        throw new MissingArgumentError('obj');
    }

    if ( (typeof obj !== 'object') || (Array.isArray(obj)) ){
        throw new ArgumentTypeError('obj must be an object.');
    }

    if (typeof func !== 'function'){
        throw new ArgumentTypeError('func must be a function.');
    }

    const res = {};
    for(let key in obj){
        const val = obj[key];
        if (typeof val !== 'number'){
            throw new ArgumentTypeError('Input object can only contain number object.');
        }
        res[key] = func(val);
    }

    return res;
}

module.exports = {
    makeArrays,
    isDeepEqual,
    computeObject
}