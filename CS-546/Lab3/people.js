
const axios = require('axios');

async function getPeople(){
    try{
        const { data } = await axios.get('https://gist.githubusercontent.com/graffixnyc/31e9ef8b7d7caa742f56dc5f5649a57f/raw/43356c676c2cdc81f81ca77b2b7f7c5105b53d7f/people.json');
        return data;
    }catch(error){
        console.error(error);
        throw 'API call failed.';
    }
}

function getAge(dob){
    let [month , day , year] = dob.split('/');
    dob = new Date(`${year}-${month}-${day}`);
    const today = new Date();
    let age = today.getFullYear() - dob.getFullYear();
    const months = today.getMonth() - dob.getMonth();
    if (months < 0 || (months === 0 && today.getDate() < dob.getDate())){
        age--;
    }
    return age;
}


async function getPersonById(id){

    if (id === undefined){
        throw 'Missing Paramenter: id';
    }

    if (typeof id !== 'number'){
        throw 'id must be a number.';
    }

    if (id < 0){
        throw 'id must be positive.';
    }

    let people;
    try{
        people = await getPeople();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }

    if(!Array.isArray(people)){
        throw 'Did not receive an array from the API.';
    }

    for (let person of people){
        if (person.id === id){
            return person;
        }
    }

    throw `id:${id} does not exist.`;
    
}

async function howManyPerState(state){

    if (state === undefined){
        throw 'Missing Paramenter: state';
    }

    if (typeof state !== 'string'){
        throw 'state must be a string.';
    }

    state = state.trim();

    let people;
    try{
        people = await getPeople();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }

    if(!Array.isArray(people)){
        throw 'Did not receive an array from the API.';
    }
    

    let count = 0;
    for (let person of people){
        if(person.address){
            if(person.address.state.toLowerCase() === state.toLocaleLowerCase()){
                count++;
            }
        }else{
            throw 'person object does not have any address.';
        }
    }

    if (count === 0){
        throw `No person found in the state ${state}.`;
    }

    return count;
}




async function personByAge(index){

    if (index === undefined){
        throw 'Missing Paramenter: index';
    }

    if (typeof index !== 'number'){
        throw 'index must be a number.';
    }

    if (index < 0){
        throw 'index must be greater or equal to 0';
    }

    let people;
    try{
        people = await getPeople();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }

    if(!Array.isArray(people)){
        throw 'Did not receive an array from the API.';
    }

    if (index >= people.length){
        throw `index must be less than ${people.length}.`;
    }

    const DATE_FORMAT = /(0[1-9]|1[012])[- \/.](0[1-9]|[12][0-9]|3[01])[- \/.](19|20)\d\d/;

    for(let person of people){
        let dob = person.date_of_birth;
        if (typeof dob !== 'string'){
            throw `Did not receive date_of_birth string from API data.`;
        }
        if(!(dob.match(DATE_FORMAT))){
            throw `Invalid date_of_birth format received: ${dob}`;
        }
    }
    
    people.sort(function(a , b){
        const date_a = Date.parse(a.date_of_birth);
        const date_b = Date.parse(b.date_of_birth);
        return date_a - date_b;
    });

    const person = people[index];
    const dob = person.date_of_birth;
    const age = getAge(dob);

    let res = {
        'first_name' : person.first_name,
        'last_name' : person.last_name,
        'date_of_birth': person.date_of_birth,
        'age': age
    };
    return res;
}


async function peopleMetrics(){

    let people;
    try{
        people = await getPeople();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }

    if(!Array.isArray(people) || people.length < 1000){
        throw 'Unexpected data received from API.';
    }

    let totalLetters = 0;
    let totalVowels = 0;
    let first_name = people[0].first_name.trim();
    let last_name = people[0].last_name.trim();
    let longest_len = first_name.replace(/[^a-zA-Z]/g, "").length + last_name.replace(/[^a-zA-Z]/g, "").length;
    let shortest_len = longest_len;
    let longest_name = `${first_name} ${last_name}`;
    let shortest_name = longest_name;
    const city_count = {};
    let combined_age = 0;

    for (let person of people){
        first_name = person.first_name.trim();
        last_name = person.last_name.trim();
        let cur_len = first_name.replace(/[^a-zA-Z]/g, "").length + last_name.replace(/[^a-zA-Z]/g, "").length;
        if(cur_len > longest_len){
            longest_len = cur_len;
            longest_name = `${first_name} ${last_name}`;
        }
        else if(cur_len < shortest_len){
            shortest_len = cur_len;
            shortest_name = `${first_name} ${last_name}`;
        }

        totalLetters += cur_len;
        let fullName = first_name.concat(last_name);
        let vowels = fullName.match(/[aeiou]/gi);
        let num_vowels = (vowels === null ? 0 : vowels.length);
        totalVowels += num_vowels;

        combined_age += getAge(person.date_of_birth);

        let city;
        if (person.address){
            city = person.address.city;
        }else{
            throw 'person object does not have any address.';
        }
        if (!(city in city_count)){
            city_count[city] = 1;
        } else{
            city_count[city] += 1;
        }
    }

    let highestCount = 0;
    let highestCity;

    for(let city in city_count){
        if (city_count[city] > highestCount){
            highestCity = city;
            highestCount = city_count[city];
        }
    }

    const res = {
        'totalLetters' : totalLetters,
        'totalVowels' : totalVowels,
        'totalConsonants': (totalLetters - totalVowels),
        'longestName' : longest_name,
        'shortestName' : shortest_name,
        'mostRepeatingCity': highestCity,
        'averageAge': (combined_age / people.length)

    }

    return res;
}


module.exports = {
    getPersonById,
    howManyPerState,
    personByAge,
    peopleMetrics
}