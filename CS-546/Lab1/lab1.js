

const questionOne = function questionOne(arr) {

    // Implementing Sieve of Eratosthenes
    function primesUpto(n){
        
        if(n < 2){
            return new Array(n).fill(0);
        }

        let is_prime = new Array(n).fill(1);
        is_prime[0] = 0;
        is_prime[1] = 0;

        let i = 2;
        while((i*i) <= n){

            if(is_prime[i] == 0){
                i += 1;
                continue;
            }

            let j = i*i;
            while(j <= n){
                is_prime[j] = 0;
                j += i;
            }
            i += 1;
        }

        return is_prime;
    }
    
    if(arguments.length == 0 ||  arr.length == 0){
        return {};
    }

    const n = Math.max(...arr);
    if(n < 0){
        return {};
    }
    const is_prime = primesUpto(n);
    const res = {};

    arr.forEach((num) => {
        res[num] = is_prime[num] == 0 ? false : true;
    });

    return res;

}

const questionTwo = function questionTwo(arr) { 

    if(arguments.length == 0 ||  arr.length == 0){
        return 0;
    }

    let squaredArray = arr.map(x => x*x);

    let squaredSum = squaredArray.reduce((accumulator , current) => {
        return accumulator + current;
    }, 0);

    let raisedFive = Math.pow(squaredSum , 5);
    let root = Math.sqrt(raisedFive)
    let ans = root.toFixed(2);
    return ans;

}

const questionThree = function questionThree(text) {
    
    const counts = {
        'consonants' : 0,
        'vowels': 0,
        'numbers': 0,
        'spaces' : 0,
        'punctuation' : 0,
        'specialCharacters' : 0,
    }

    if(arguments.length == 0){
        return counts;
    }

    text = text.toLowerCase();
    const VOWELS = "aeiou";
    const CONSONANTS = "bcdfghjklmnpqrstvwxyz";
    const NUMBERS = "0123456789";
    const PUNCTUATIONS = ".,?!;:-–—(){}[]'\"";

    for(let ch of text){
        if(CONSONANTS.includes(ch)){
            counts['consonants']++;
        }
        else if(VOWELS.includes(ch)){
            counts['vowels']++;
        }
        else if(NUMBERS.includes(ch)){
            counts['numbers']++;
        }
        else if(ch === " "){
            counts['spaces']++;
        }
        else if(PUNCTUATIONS.includes(ch)){
            counts['punctuation']++;
        }
        else{
            counts['specialCharacters']++;
        }
    }

    return counts;

}

const questionFour = function questionFour(principal , interestRate , years) {
    
    let i = (interestRate/100)/12;
    let n = years*12;
    let multiplier = Math.pow((1+i) , n);
    let pmt = (principal*i*multiplier)/(multiplier-1);
    let ans =  pmt.toFixed(2);
    return ans;

}

module.exports = {
    firstName: "Mohammad", 
    lastName: "Khan", 
    studentId: "10438167",
    questionOne,
    questionTwo,
    questionThree,
    questionFour
};