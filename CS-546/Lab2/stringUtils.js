const {MissingArgumentError , ArgumentTypeError, InvalidRangeError} = require('./errors');


const camelCase = (str) => {

    if(str === undefined){
        throw new MissingArgumentError('str');
    }

    if(typeof str !== 'string'){
        throw new ArgumentTypeError('str must be type string.');
    }

    str = str.trim();
    if(str.length === 0){
        throw new InvalidRangeError('str can not be empty.');
    }

    str = str.toLowerCase();
    str = str.split(' ');
    for(let i = 1; i < str.length; i++){
        str[i] = str[i][0].toUpperCase() + str[i].slice(1);
    }
    str = str.join('');
    return str;

}

const replaceChar = (str) => {

    if(str === undefined){
        throw new MissingArgumentError('str');
    }

    if(typeof str !== 'string'){
        throw new ArgumentTypeError('str must be type string.');
    }

    str = str.trim();
    if(str.length === 0){
        throw new InvalidRangeError('str can not be empty.');
    }

    const firstChar = str[0].toLowerCase();
    let first = true;
    let res = str[0];
    for(let i = 1; i < str.length; i++){
        const curr = str[i].toLowerCase();
        if(curr === firstChar){
            res +=  (first ? "*" : "$");
            first = !first;
        }else{
            res += str[i];
        }
    }

    return res;
}

const mashUp = (str1 , str2) => {

    if(str1 === undefined){
        throw new MissingArgumentError('str1');
    }

    if(typeof str1 !== 'string'){
        throw new ArgumentTypeError('str1 must be of type string.');
    }

    str1 = str1.trim();
    if(str1.length < 2){
        throw new InvalidRangeError('str1 must contain at least two letters.');
    }

    if(str2 === undefined){
        throw new MissingArgumentError('str2');
    }

    if(typeof str2 !== 'string'){
        throw new ArgumentTypeError('str2 must be of type string.');
    }

    str2 = str2.trim();
    if(str2.length < 2){
        throw new InvalidRangeError('str2 must contain at least two letters.');
    }

    const part1 = str2[0] + str2[1] + str1.slice(2);
    const part2 = str1[0] + str1[1] + str2.slice(2);
    const res = `${part1} ${part2}`;
    return res;

}


module.exports = {
    camelCase,
    replaceChar,
    mashUp
}