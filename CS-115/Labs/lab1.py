'''
Created on Sep 5, 2018
I pledge my honor that I have abided by the Stevens Honor System.
@author: Mohammad Khan
username: mkhan13

'''
from cs115 import map,range,reduce 
import math 

def inverse(x):
    '''Takes a number x as input and returns its reciprocal.'''
    return 1/x

def add(a,b):
    '''Returns the sum of the two functions.'''
    return a+b 
    

def e(n):
    '''Approximates the mathematical value e using a Taylor expansion.'''
    numberList = range(1,n+1)
    factorialList = map(math.factorial,numberList)
    inverseList = map(inverse,factorialList)
    addedList = reduce(add,inverseList)
    
    return 1+addedList
    
    
def error (x):
    '''Returns the absolute value of the difference between exact and approximate e.'''
    result = abs(e(x)-math.e)
    return result 
   

