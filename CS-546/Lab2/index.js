
const assert = require('assert');
const {mean , medianSquared , maxElement , fill , countRepeating , isEqual} = require('./arrayUtils');
const {camelCase , replaceChar , mashUp} = require('./stringUtils');
const {makeArrays , isDeepEqual , computeObject} = require('./objUtils');


// mean Tests
try {
    // Should Pass
    const meanOne = mean([2, 3, 4]);
    assert.deepStrictEqual(meanOne , 3);
    console.log('mean passed successfully');
 } catch (e) {
    console.error('mean failed test case');
 }
 try {
    // Should Fail
    const meanTwo = mean(1234);
    console.error('mean did not error');
 } catch (e) {
    console.log('mean failed successfully');
 }
 try {
    // Should Fail
    const mean3 = mean([]);
    console.error('mean did not error');
 } catch (e) {
    console.log('mean failed successfully');
 }

 try {
    // Should Fail
    const mean4 = mean("banana");
    console.error('mean did not error');
 } catch (e) {
    console.log('mean failed successfully');
 }

 try {
    // Should Fail
    const mean5 = mean(["guitar", 1, 3, "apple"]);
    console.error('mean did not error');
 } catch (e) {
    console.log('mean failed successfully');
 }

 try {
    // Should Fail
    const mean6 = mean();
    console.error('mean did not error');
 } catch (e) {
    console.log('mean failed successfully');
 }

 console.log("==================================================");

 // medianSquared tests

 try {
    // Should Pass
    const median1 = medianSquared([3, 13, 7, 5, 21, 23, 39, 23, 40, 23, 14, 12, 56, 23, 29]);
    //console.log(median1);
    assert.deepStrictEqual(median1 , 529);
    console.log('medianSquared passed successfully');
 } catch (e) {
    console.error('medianSquared failed test case');
 }

 try {
    // Should Pass
    const median2 = medianSquared([7 , 9 , 5 , 3]);
    assert.deepStrictEqual(median2 , 36);
    console.log('medianSquared passed successfully');
 } catch (e) {
    console.error('medianSquared failed test case');
 }

 try {
    // Should Fail
    const median3 = medianSquared();
    console.error('medianSquared did not error');
 } catch (e) {
    console.log('medianSquared failed successfully');
 }

 try {
    // Should Pass
    const median4 = medianSquared([3, 13, 7, 5, 21, 23, 23, 40, 23, 14, 12, 56, 23, 29]);
    assert.deepStrictEqual(median4 , 484);
    console.log('medianSquared passed successfully');
 } catch (e) {
    console.error('medianSquared failed test case');
 }

 console.log("==================================================");

 // maxElement tests

 try{
     // should pass
    const max1 = maxElement([5, 6, 7]);
    assert.deepStrictEqual(max1 , {7 : 2});
    console.log('maxElement passed successfully');
 }catch (e) {
    console.error('maxElement failed test case');
 }

 try {
    // Should Fail
    const max2 = maxElement("test");;
    console.error('maxElement did not error');
 } catch (e) {
    console.log('maxElement failed successfully');
 }

console.log("==================================================");

// fill tests

try{
    const fill1 = fill(6);
    assert.deepStrictEqual(fill1 , [0, 1, 2, 3, 4, 5]);
    console.log('fill passed successfully');
}catch (e) {
    console.log('fill failed successfully');
}

try{
    const fill2 = fill(3, 'Welcome');
    assert.deepStrictEqual(fill2 , ['Welcome', 'Welcome', 'Welcome']);
    console.log('fill passed successfully');
}catch (e) {
    console.log('fill failed successfully');
}

try{
    const fill3 = fill(0);
    console.error('fill did not error');
} catch (e) {
    console.log('fill failed successfully');
}

console.log("==================================================");

// countRepeating tests

try{
    const counter1 = countRepeating([7, '7', 13, "Hello","Hello", "hello"]);
    assert.deepStrictEqual(counter1 , {'7': 2, Hello: 2});
    console.log('countRepeating passed successfully');
}catch (e) {
    console.log('countRepeating failed testcase');
}

try{
    const counter2 = countRepeating([]);
    assert.deepStrictEqual(counter2 , {});
    console.log('countRepeating passed successfully');
}catch (e) {
    console.log('countRepeating failed testcase');
}

try{
    const counter3 = countRepeating([(message)=>message, true, undefined, null]);
    console.error('countRepeating did not error');
} catch (e) {
    console.log('countRepeating failed successfully');
}

console.log("==================================================");

// isEqual tests
try{
    const equal1 = isEqual([1, 2, 3], [3, 1, 2]);
    assert.deepStrictEqual(equal1 , true);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal2 = isEqual([ 'Z', 'R', 'B', 'C', 'A' ], ['R', 'B', 'C', 'A', 'Z']);
    assert.deepStrictEqual(equal2 , true);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal3 = isEqual([1, 2, 3], [4, 5, 6]);
    assert.deepStrictEqual(equal3 , false);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal4 = isEqual([1, 3, 2], [1, 2, 3, 4]);
    assert.deepStrictEqual(equal4 , false);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal5 = isEqual([1, 2], [1, 2, 3]);
    assert.deepStrictEqual(equal5 , false);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal6 = isEqual([[ 1, 2, 3 ], [ 4, 5, 6 ], [ 7, 8, 9 ]], [[ 3, 1, 2 ], [ 5, 4, 6 ], [ 9, 7, 8 ]]);
    assert.deepStrictEqual(equal6 , true);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal7 = isEqual([[ 1, 2, 3 ], [ 4, 5, 6 ], [ 7, 8, 9 ]], [[ 3, 1, 2 ], [ 5, 4, 11 ], [ 9, 7, 8 ]]);
    assert.deepStrictEqual(equal7 , false);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal8 = isEqual([null, null, null], [null, null, null]);
    assert.deepStrictEqual(equal8 , true);
    console.log('isEqual passed successfully');
}catch (e) {
    console.log('isEqual failed testcase');
}

try{
    const equal9 = isEqual((message)=>message , undefined);
    console.error('isEqual did not error');
} catch (e) {
    console.log('isEqual failed successfully');
}

console.log("==================================================");
// camelCase tests

try{
    const case1 = camelCase('my function rocks');
    assert.deepStrictEqual(case1 , "myFunctionRocks");
    console.log('camelCase passed successfully');
}catch (e) {
    console.log('camelCase failed testcase');
}

try{
    const case2 = camelCase('FOO BAR');
    assert.deepStrictEqual(case2 , "fooBar");
    console.log('camelCase passed successfully');
}catch (e) {
    console.log('camelCase failed testcase');
}

try{
    const case3 = camelCase("How now brown cow");
    assert.deepStrictEqual(case3 , "howNowBrownCow");
    console.log('camelCase passed successfully');
}catch (e) {
    console.log('camelCase failed testcase');
}

try{
    const case4 = camelCase(["Hello", "World"]);
    console.error('camelCase did not error');
} catch (e) {
    console.log('camelCase failed successfully');
}

console.log("==================================================");
// replaceChar tests

try{
    const replace1 = replaceChar("Daddy");
    assert.deepStrictEqual(replace1 , "Da*$y");
    console.log('replaceChar passed successfully');
}catch (e) {
    console.log('replaceChar failed testcase');
}

try{
    const replace2 = replaceChar("Hello, How are you? I hope you are well");
    assert.deepStrictEqual(replace2 , "Hello, *ow are you? I $ope you are well");
    console.log('replaceChar passed successfully');
}catch (e) {
    console.log('replaceChar failed testcase');
}

try{
    const replace3 = replaceChar("babbbbble"); 
    assert.deepStrictEqual(replace3 , "ba*$*$*le");
    console.log('replaceChar passed successfully');
}catch (e) {
    console.log('replaceChar failed testcase');
}

try{
    const replace4 = replaceChar(123);
    console.error('replaceChar did not error');
} catch (e) {
    console.log('replaceChar failed successfully');
}

console.log("==================================================");
// mashUp tests

try{
    const mash1 = mashUp("Patrick", "Hill");
    assert.deepStrictEqual(mash1 , "Hitrick Pall");
    console.log('mashUp passed successfully');
}catch (e) {
    console.log('mashUp failed testcase');
}

try{
    const mash2 = mashUp("hello", "world");
    assert.deepStrictEqual(mash2 , "wollo herld");
    console.log('mashUp passed successfully');
}catch (e) {
    console.log('mashUp failed testcase');
}

try{
    const mash3 = mashUp("Patrick", "");
    console.error('mashUp did not error');
} catch (e) {
    console.log('mashUp failed successfully');
}

console.log("==================================================");
// makeArrays tests

const first = { x: 2, y: 3}; 
const second = { a: 70, x: 4, z: 5 }; 
const third = { x: 0, y: 9, q: 10 };
const dummy = {};

try{
    const make1 = makeArrays([first, second, third]);
    assert.deepStrictEqual(make1 , 
        [ ['x',2],['y',3], ['a',70], ['x', 4], ['z', 5], ['x',0], ['y',9], ['q',10] ]);
    console.log('makeArrays passed successfully');
}catch (e) {
    console.log('makeArrays failed testcase');
}

try{
    const make2 = makeArrays([second, third]);
    assert.deepStrictEqual(make2 , 
        [ ['a',70], ['x', 4], ['z', 5], ['x',0], ['y',9], ['q',10] ]);
    console.log('makeArrays passed successfully');
}catch (e) {
    console.log('makeArrays failed testcase');
}


try{
    const make3 = makeArrays([third, first, second]);
    assert.deepStrictEqual(make3 , 
        [ ['x',0], ['y',9], ['q',10], ['x',2],['y',3], ['a',70], ['x', 4], ['z', 5] ]);
    console.log('makeArrays passed successfully');
}catch (e) {
    console.log('makeArrays failed testcase');
}

try{
    const make4 = makeArrays([third, dummy]);
    console.log('makeArrays did not error.');
}catch (e) {
    console.log('makeArrays failed successfully');
}

console.log("==================================================");
// isDeepEqual tests

const first2 = {a: 2, b: 3}; 
const second2 = {a: 2, b: 4}; 
const third2 = {a: 2, b: 3};
const forth2 = {a: {sA: "Hello", sB: "There", sC: "Class"}, b: 7, c: true, d: "Test"}
const fifth2 = {c: true, b: 7, d: "Test", a: {sB: "There", sC: "Class", sA: "Hello"}}

try{
    const deep1 = isDeepEqual(first2 , second2);
    assert.deepStrictEqual(deep1 , false);
    console.log('isDeepEqual passed successfully');
}catch (e) {
    console.log('isDeepEqual failed testcase');
}

try{
    const deep2 = isDeepEqual(forth2, fifth2);
    assert.deepStrictEqual(deep2 , true);
    console.log('isDeepEqual passed successfully');
}catch (e) {
    console.log('isDeepEqual failed testcase');
}

try{
    const deep3 = isDeepEqual(forth2, third2);
    assert.deepStrictEqual(deep3 , false);
    console.log('isDeepEqual passed successfully');
}catch (e) {
    console.log('isDeepEqual failed testcase');
}

try{
    const deep4 = isDeepEqual({}, {});
    assert.deepStrictEqual(deep4 , true);
    console.log('isDeepEqual passed successfully');
}catch (e) {
    console.log('isDeepEqual failed testcase');
}

try{
    const deep5 = isDeepEqual(123 , null);
    console.log('isDeepEqual did not error.');
}catch (e) {
    console.log('isDeepEqual failed successfully');
}

console.log("==================================================");
// computeObject tests

try{
    const compute1 = computeObject({ a: 3, b: 7, c: 5 }, n => n * 2);
    assert.deepStrictEqual(compute1 , { a: 6, b: 14, c: 10 });
    console.log('computeObject passed successfully');
}catch (e) {
    console.log('computeObject failed testcase');
}

try{
    const compute2 = computeObject({ a: 3, b: 7, c: 5 }, 123);
    console.log('computeObject did not error.');
}catch (e) {
    console.log('computeObject failed successfully');
}