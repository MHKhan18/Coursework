
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

async function getWork(){
    try{
        const { data } = await axios.get('https://gist.githubusercontent.com/graffixnyc/febcdd2ca91ddc685c163158ee126b4f/raw/c9494f59261f655a24019d3b94dab4db9346da6e/work.json');
        return data;
    }catch(error){
        console.error(error);
        throw 'API call failed.';
    }
}


async function listEmployees(){

    let work;
    try{
        work = await getWork();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }
    if(!Array.isArray(work)){
        throw 'Did not receive an array from the API.';
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

    const id_employee = {};
    for(let person of people){
        id_employee[person.id] = {
            "first_name" : person.first_name,
            "last_name" : person.last_name
        }  
    }

    const res = [];
    for(let company of work){
        
        if(!Array.isArray(company.employees)){
            throw 'Did not receive employees array from the API.';
        }

        let employees = [];
        for(let employee of company.employees){
            employees.push(id_employee[employee]);
        }
        res.push({
            "company_name" : company.company_name,
            "employees" : employees
        })
    }

    return res;
}

async function fourOneOne(phoneNumber){

    if (phoneNumber === undefined){
        throw 'Missing parameter: phoneNumber.';
    }

    if (typeof phoneNumber !== 'string'){
        throw 'phoneNumber must be a string';
    }

    const PHONENO = /^\(?([0-9]{3})\)?[-]{1}([0-9]{3})[-]{1}([0-9]{4})$/;
    if(!(phoneNumber.match(PHONENO))){
        throw 'phoneNumber must be of format: ###-###-####';
    }

    let work;
    try{
        work = await getWork();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }
    if(!Array.isArray(work)){
        throw 'Did not receive an array from the API.';
    }

    for(let company of work){
        if (company.company_phone === phoneNumber){
            const res = {
                'company_name' : company.company_name,
                'company_address' : company.company_address
            };
            return res;
        }
    }

    throw `No company can be found with phone number ${phoneNumber}`;

}


async function whereDoTheyWork(ssn){

    if (ssn === undefined){
        throw 'Missing Parameter: ssn';
    }

    if (typeof ssn !== 'string'){
        throw 'ssn must be a string.';
    }
    const SSN_FORMAT = /^\(?([0-9]{3})\)?[-]{1}([0-9]{2})[-]{1}([0-9]{4})$/;
    if(!(ssn.match(SSN_FORMAT))){
        throw 'ssn must be of format: ###-##-####';
    }

    let work;
    try{
        work = await getWork();
    } catch (error){
        console.error(error);
        throw 'API call failed.';
    }
    if(!Array.isArray(work)){
        throw 'Did not receive an array from the API.';
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

    let id;
    let first_name;
    let last_name;
    let company_name;

    for (let person of people){
        if (person.ssn === ssn){
            id = person.id;
            first_name = person.first_name;
            last_name = person.last_name;
            break;
        }
    }

    if (id === undefined){
        throw `No person found with ssn ${ssn}.`;
    }

    let found = false;
    
    for(let company of work){
        
        const employees = company.employees;
        if(!Array.isArray(employees)){
            throw 'Did not receive employees array from the API.';
        }

        for(let employee of employees){
            if (employee === id){
                company_name = company.company_name;
                found = true;
                break;
            }
        }

        if (found){
            break;
        }
    }

    if(!found){
        throw `No employee exists with id ${id}`;
    }

    const res = `${first_name} ${last_name} works at ${company_name}.`;
    return res;

}

module.exports = {
    listEmployees,
    fourOneOne,
    whereDoTheyWork
}