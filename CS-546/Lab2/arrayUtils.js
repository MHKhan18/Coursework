
const {MissingArgumentError , ArgumentTypeError, InvalidRangeError} = require('./errors');

function sortArray(arr){
    if(!Array.isArray(arr[0])){
        if (typeof arr[0] === 'number'){
            arr.sort((a, b) => { return a - b });
        }
        else{
            arr.sort();
        }
        
    }else{ // array of arrays
        arr.forEach((subarr) => {
            sortArray(subarr);
        });

        arr.sort((a , b) => {
            if(a.length !== b.length){
                return a.length - b.length;
            }
            const bound = a.length;
            let i = 0;
            while(i < bound && a[i]==b[i]){
                i++;
            }
            if(i === bound){
                return 0;
            }
            return a[i]-b[i];
        });

    }
}

function arrayEquality(arr1 , arr2){

    if(arr1.length === 0 && arr2.length === 0){
        return true;
    }

    if(arr1.length !== arr2.length){
        return false;
    }

    if(Array.isArray(arr1[0])){
        if(!Array.isArray(arr2[0])){
            return false;
        }
        if(!arrayEquality(arr1[0] , arr2[0])){
            return false;
        }
        arr1.shift();
        arr2.shift();
        return arrayEquality(arr1 , arr2);
    }

    if(arr1[0] !== arr2[0]){
        return false;
    }
    arr1.shift();
    arr2.shift();
    return arrayEquality(arr1 , arr2);
}


const mean = (arr) => {
    
    if (arr === undefined){
        throw new MissingArgumentError('arr');
    }

    if(!Array.isArray(arr)){
        throw new ArgumentTypeError('Input must be an array.');
    }

    if(arr.length === 0){
        throw new ArgumentTypeError('Input array can not be empty');
    }

    arr.forEach((val) => {
        if (typeof val !== 'number'){
            throw new ArgumentTypeError('Input array must only contain integers.');
        }
    });

    let sum = arr.reduce((accumulator , current) => {
        return accumulator + current;
    }, 0);

    return sum/arr.length ; 
}

const medianSquared = (arr) => {
    
    if (arr === undefined){
        throw new MissingArgumentError('arr');
    }

    if(!Array.isArray(arr)){
        throw new ArgumentTypeError('Input must be an array.');
    }

    if(arr.length === 0){
        throw new ArgumentTypeError('Input array can not be empty');
    }

    if(arr.some(isNaN)){
        throw new ArgumentTypeError('All elements in input array must be number.');
    }
    
    arr.sort((a, b) => { return a - b });

    let median;
    const mid = Math.floor(arr.length / 2);
    if(arr.length % 2 === 1){
        median = arr[mid];
    }else{
        const val1 = arr[mid];
        const val2 = arr[mid - 1];
        median = ((val1 + val2) / 2);
    }

    const ans = (median*median);
    return ans;

}

const maxElement = (arr) => {
    
    if (arr === undefined){
        throw new MissingArgumentError('arr');
    }

    if(!Array.isArray(arr)){
        throw new ArgumentTypeError('Input must be an array.');
    }

    if(arr.length === 0){
        throw new ArgumentTypeError('Input array can not be empty');
    }

    if(arr.some(isNaN)){
        throw new ArgumentTypeError('All elements in input array must be number.');
    }

    let maxVal = arr[0];
    let maxIndex = 0;

    arr.forEach((val , index) => {
        if(val > maxVal){
            maxVal = val;
            maxIndex = index;
        }
    });

    const ans = {};
    ans[maxVal] = maxIndex;
    return ans;
}

const fill = (end , value) => {

    if (end === undefined){
        throw new MissingArgumentError('end');
    }

    if(typeof end != 'number'){
        throw new ArgumentTypeError('end must be a number.');
    }

    if (end <= 0){
        throw new InvalidRangeError('end must be greater equal to zero.')
    }

    let res = [];
    for(let i = 0 ; i < end ; i++){
        res[i] = value === undefined ? i : value;
    }

    return res;
}

const countRepeating = (arr) => {

    if (arr === undefined){
        throw new MissingArgumentError('arr');
    }

    if(!Array.isArray(arr)){
        throw new ArgumentTypeError('Input must be an array.');
    }

    arr.forEach((val) => {
        if( (typeof val != 'number') && (typeof val != 'string')){
            throw new ArgumentTypeError('arr must contain either string or number.');
        }
    });

    const counter = {};

    arr.forEach((val) => {
        if(!(val in counter)){
            counter[val] = 1;
        }else{
            counter[val] = 1 + counter[val];
        }
    });

    arr.forEach((val) => {
        if(counter[val] < 2){
            delete counter[val];
        }
    });

    return counter;
}

const isEqual = (arr1 , arr2) => {

    if (arr1 === undefined){
        throw new MissingArgumentError('arr1');
    }

    if (arr2 === undefined){
        throw new MissingArgumentError('arr2');
    }

    if(!Array.isArray(arr1)){
        throw new ArgumentTypeError('arr1 must be an array.');
    }

    if(!Array.isArray(arr2)){
        throw new ArgumentTypeError('arr2 must be an array.');
    }

    if(arr1.length === 0 && arr2.length === 0){
        return true;
    }

    if(arr1.length !== arr2.length){
        return false;
    }

    sortArray(arr1);
    sortArray(arr2);

    return arrayEquality(arr1 , arr2);
}


module.exports = {
    mean,
    medianSquared,
    maxElement,
    fill,
    countRepeating,
    isEqual
}