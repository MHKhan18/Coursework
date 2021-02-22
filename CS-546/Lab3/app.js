const assert = require('assert');
const util = require('util');

const {getPersonById , howManyPerState , personByAge , peopleMetrics} = require('./people');
const {listEmployees , fourOneOne , whereDoTheyWork} = require('./work');

async function testGetPersonByID(){
    try{
        const byId1 = await getPersonById(43);
        const expected1 = {  
            id: 43,  
            first_name: "Tades",  
            last_name: "Paskell",  
            date_of_birth: "01/02/1987",  
            ssn: "648-97-2273",  
            email: "tpaskell16@cam.ac.uk",  
            ip_address: "72.63.45.5",  
            address: {   
              street_address: "6 South Plaza",   
              city: "Huntsville",   
              state: "AL",   
              zip_code: "35810"  
            } 
          };
        assert.deepStrictEqual(byId1 , expected1);
        console.log('getPersonById passed successfully.')
    }catch(error){
        console.error('getPersonById failed test case.');
    }
    
    try{
        const byId2 = await getPersonById(1001);
        console.log('getPersonById did not error.');
    }catch(error){
        console.log(error);
        console.log('getPersonById failed successfully.');
    }
}

async function testHowManyPerState(){

    try{
        const perState1 = await howManyPerState("NY");
        assert.deepStrictEqual(perState1 , 64);
        console.log("howManyPerState passed successfully.");
    }catch(error){
        console.error('howManyPerState failed test case.');
    }

    try{
        const perState2 = await howManyPerState("CO");
        assert.deepStrictEqual(perState2 , 27);
        console.log("howManyPerState passed successfully.");
    }catch(error){
        console.error('howManyPerState failed test case.');
    }

    try{
        const perState2 = await howManyPerState("WY");
        console.log("howManyPerState did not error.");
    }catch(error){
        console.log(error);
        console.error('howManyPerState failed successfully.');
    }
}


async function testPersonByAge(){

    try{
        const byAge1 = await personByAge(0);
        const exp1 = {  
            first_name: 'Chaim', 
            last_name: 'Giovannacci', 
            date_of_birth: '01/30/1930', 
            age: 91
        };
        assert.deepStrictEqual(byAge1 , exp1);
        console.log('personByAge passed successfully.');
    }catch(error){
        console.log(error);
        console.error('personByAge failed testcase.');
    }

    try{
        const byAge2 = await personByAge(43);
        const exp2 = {  
            first_name: 'Chen', 
            last_name: 'Sowthcote', 
            date_of_birth: '10/01/1934', 
            age: 86
        };
        assert.deepStrictEqual(byAge2 , exp2);
        console.log('personByAge passed successfully.');
    }catch(error){
        console.log(error);
        console.error('personByAge failed testcase.');
    }

    try{
        const byAge3 = await personByAge(500);
        const exp3 = { 
            first_name: 'Guglielmo', 
            last_name: 'Kubera', 
            date_of_birth: '05/07/1974', 
            age: 46, 
        };
        assert.deepStrictEqual(byAge3 , exp3);
        console.log('personByAge passed successfully.');
    }catch(error){
        console.log(error);
        console.error('personByAge failed testcase.');
    }

    try{
        const byAge4 = await personByAge(1000);
        console.log('personByAge did not error.');
    }catch(error){
        console.log(error);
        console.error('personByAge failed successfully.');
    }
}

async function testPeopleMetrics(){
    const metrics = await peopleMetrics();
    console.log(metrics);
}

async function testListEmployees(){
    const lstEmp = await listEmployees();
    console.log(util.inspect(lstEmp, {showHidden: false, depth: null}));
}

async function testFourOneOne(){
    
    try{
        const phone1 = await fourOneOne('240-144-7553'); 
        const exp1 = { 
            company_name: "Kassulke, Towne and Davis",  
            company_address: 
            {   
            street_address: "1 Claremont Plaza",   
            city: "Frederick",   
            state: "MD",   
            zip_code: "21705"  
            }
        };
        assert.deepStrictEqual(phone1 , exp1);
        console.log('fourOneOne passed successfully.');
    }catch(error){
        console.error('fourOneOne failed testcase.');
    }

    try{
        const phone2 = await fourOneOne('212-208-8374');
        console.log('fourOneOne did not error.');
    }catch(error){
        console.log(error);
        console.error('fourOneOne failed successfully.');
    }

    try{
        const phone3 = await fourOneOne('5045890047');
        console.log('fourOneOne did not error.');
    }catch(error){
        console.log(error);
        console.error('fourOneOne failed successfully.');
    }
}

async function testWhereDoTheyWork(){

    try{
        const ssn1 = await whereDoTheyWork('299-63-8866');
        const exp1 = "Marga Dawidowitsch works at Durgan LLC.";
        assert.deepStrictEqual(ssn1 , exp1);
        console.log('whereDoTheyWork passed successfully.');
    }catch(error){
        console.error('whereDoTheyWork failed testcase.');
    }

    try{
        const ssn2 = await whereDoTheyWork('277-85-0056');
        const exp2 = "Toby Ginsie works at Hirthe, Adams and Reilly.";
        assert.deepStrictEqual(ssn2 , exp2);
        console.log('whereDoTheyWork passed successfully.');
    }catch(error){
        console.error('whereDoTheyWork failed testcase.');
    }

    try{
        const ssn3 = await whereDoTheyWork("123456789"); 
        console.log('whereDoTheyWork did not error.');
    }catch(error){
        console.log(error);
        console.error('whereDoTheyWork failed successfully.');
    }

    try{
        const ssn4 = await whereDoTheyWork("264-67-0084");
        console.log('whereDoTheyWork did not error.');
    }catch(error){
        console.log(error);
        console.error('whereDoTheyWork failed successfully.');
    }
}

async function main(){

    await testGetPersonByID();
    console.log("========================");
    await testHowManyPerState();
    console.log("========================");
    await testPersonByAge();
    console.log("========================");
    await testPeopleMetrics();
    console.log("========================");
    await testListEmployees();
    console.log("========================");
    await testFourOneOne();
    console.log("========================");
    await testWhereDoTheyWork();

}

main();
