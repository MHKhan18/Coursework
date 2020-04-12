'''
Created on Sep 11, 2018
I pledge my honor that I have abided by the Stevens Honor System.
@author: Mohammad Khan
username: mkhan13
'''

from cs115 import map, reduce, range
import math


def multiply( x , y):
    return x*y 

def factorial (n):
    return reduce(multiply,range(1,n+1))

def add(x,y):
    '''adds up two numbers'''
    return x+y 

def mean(L):
    '''calculates arithmetic mean of a list of numbers.'''
    return reduce(add,L)/len(L)


def divides(n):  # What does this do?#checks whether n is divisible by k
    """Checks if n is divisible by k."""
    def div(k):
        return n % k == 0
    return div

def sum(x,y):
    return x or y

def prime(n):
    '''detects whether a number is prime or not.'''
    ##is n divisible by any number other than 1 and n??
    '''if True in map(divides(n),range(2,n)) or n == 0 or n == 1: 
        ## divides is taking the variables as argument but it needs to be n and divided by the variables.##
        return False
    return True '''
    possible_divisors = range(2,math.ceil(math.sqrt(n)+1))
    f = divides(n)
    composite_lst = map(f,possible_divisors) 
    return not reduce(sum,composite_lst)

    
    
    

#f = divides(6)
#print( f(2))  n=6 k=2
 
#print( math.ceil(2.5))
#print(math.sqrt(4))

#print(prime(1))
#print(prime(2))
print(prime(3))
print(prime(4))  
print(prime(197)) 
print(prime(198)) 
print( mean([22,87]))
print( factorial(5))
    
    
    
   

