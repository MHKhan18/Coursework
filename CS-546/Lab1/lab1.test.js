
const lab1 = require("./lab1");

function question1Tests(){
    console.log(lab1.questionOne([1, 2, 3])); 
    // should return and output {'1': false, '2': true, '3': true}
    
    console.log(lab1.questionOne([5, 3, 10])); 
    //returns and outputs: {'5':true, '3': true, '10': false} 

    console.log(lab1.questionOne([2])); 
    // returns and outputs: {'2': true} 

    console.log(lab1.questionOne([5, 10, 9])); 
    //returns and outputs: {'5': true, '10': false, '9': false}

    console.log(lab1.questionOne([2, 7, 9, 1013])); 
    // returns and outputs: {'2': true, '7': true, '9': false, '1013': true}

    console.log(lab1.questionOne([])); 
    // returns and outputs: {}

    console.log(lab1.questionOne()); 
    // returns and outputs: {}
}



function question2Tests(){
    console.log(lab1.questionTwo([5, 3, 10])); 
    //returns and outputs: 207855.73

    console.log(lab1.questionTwo([1,2,3])); 
    // returns and outputs: 733.36 

    console.log(lab1.questionTwo([5, 10, 9])); 
    // returns and outputs: 609071.18

    console.log(lab1.questionTwo([2, 7, 9])); 
    //  returns and outputs: 207855.73

    console.log(lab1.questionTwo([])); 
    //returns and outputs: 0
}


function question3Tests(){
    console.log(lab1.questionThree("The quick brown fox jumps over the lazy dog.")); 
    // should return and output {consonants: 24, vowels: 11, numbers: 0, spaces: 8, punctuation: 1, specialCharacters: 0}
    
    console.log(lab1.questionThree("How now brown cow!!!"));
    // returns and outputs: {consonants: 10, vowels: 4, numbers: 0, spaces: 3, punctuation: 3, specialCharacters: 0}
    
    console.log(lab1.questionThree("One day, the kids from the neighborhood carried my mother's groceries all the way home. You know why? It was out of respect."));
    //  returns and outputs: {consonants: 61, vowels: 36, numbers: 0, spaces: 22, punctuation: 5, specialCharacters: 0}
    
    console.log(lab1.questionThree("CS 546 is going to be fun & I'm looking forward to working with you all this semester!!" )); 
    // returns and outputs: {consonants: 40, vowels: 23, numbers: 3, spaces: 17, punctuation: 3, specialCharacters: 1}
    
    console.log(lab1.questionThree("")); 
    // returns and outputs: {consonants: 0, vowels: 0, numbers:0, spaces: 0, punctuation: 0, specialCharacters: 0}
}


function question4Tests(){
    console.log(lab1.questionFour(25000, 3.11, 5)); 
    // should return and output: 450.44
    
    console.log(lab1.questionFour(30000, 5, 6)); 
    //returns and outputs: 483.15

    console.log(lab1.questionFour(19500, 7, 3)); 
    //returns and outputs: 602.10

    console.log(lab1.questionFour(55000, 2, 6)); 
    //returns and outputs: 811.27

    console.log(lab1.questionFour(33000, 4.5, 2)); 
    //returns and outputs: 1440.38
}


question1Tests();
console.log("=======================================================================================");
question2Tests();
console.log("=======================================================================================");
question3Tests();
console.log("=======================================================================================");
question4Tests();






 