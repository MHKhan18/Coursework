
let mainForm = document.getElementById("main-form");
let textInput = document.getElementById("query");
let resList = document.getElementById("results");
let frmLabel = document.getElementById("formLabel");

function getFib(num){
    if (num === undefined || typeof num !== 'number'){
        throw 'num is a required parameter';
    }

    if (num - Math.floor(num) != 0){
        throw 'num must be an integer';
    }

    if(num < 1){
        return 0;
    }

    if(num == 1){
        return 1;
    }

    let prev_two = 0;
    let prev_one = 1;
    let curr;
    for(let i = 2 ; i <= num ; i++){
        curr = prev_one + prev_two;
        prev_two = prev_one;
        prev_one = curr;
    }

    return curr;

}

function checkPrime(num){

    if (num === undefined || typeof num !== 'number'){
        throw 'num is a required parameter';
    }

    if (num - Math.floor(num) != 0){
        throw 'num must be an integer';
    }

    if(num < 2){
        return false;
    }
    
    let curr = 2;

    while(curr * curr <= num){
        if(num%curr == 0){
            return false;
        }
        curr += 1;
    }

    return true;
}

if (mainForm){
    mainForm.addEventListener('submit' , (event) =>{
        event.preventDefault();
        let index = textInput.value.trim();
        index = parseInt(index);

        let fib = getFib(index);
        let prime = checkPrime(fib);
        
        let li = document.createElement('li');
        li.innerHTML = `The Fibonacci of ${index} is ${fib}.`;
        li.className = prime ? "is-prime" : "not-prime";

        resList.appendChild(li);
        mainForm.reset();
        textInput.focus();
    })
}