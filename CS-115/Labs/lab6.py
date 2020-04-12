'''
Created on 11 October 2018
@author: Mohammad Khan    
username: mkhan13
Pledge:I pledge my honor that I have abided by the Stevens Honor System.

CS115 - Lab 6
'''

from cs115 import map,reduce 
def isOdd(n):
    '''Returns whether or not the integer argument is odd.'''
    if n%2 != 0:
        return True
    return False


print(isOdd( 43))

# 42 = 0010 1010
# IF EVEN, rightmost binary is 0
# IF ODD, rightmost binary is 1
# eliminating the last-bit from binary gives half (n/2)
# if N is even, add 0 to the right
# if N odd, add 1 to the right

def numToBinary(n):
    '''Precondition: integer argument is non-negative.
    Returns the string with the binary representation of non-negative integer n.
    If n is 0, the empty string is returned.'''
    
    if n == 0:
        return str('')
    elif isOdd(n):
        return str( numToBinary(n//2) + '1')
    return str(numToBinary(n//2) + '0')

print(numToBinary(100))
 
 
    
    
    

def binaryToNum(s):
    '''Precondition: s is a string of 0s and 1s.
    Returns the integer corresponding to the binary representation in s.
    Note: the empty string represents 0.'''
    
   
    if s == "" or s=="0":
        return 0
    else:
        return 2**(len(s)-1)*int(s[0]) + binaryToNum(s[1:])
        
        
   

print(binaryToNum("1000"))
        

def increment(s):
    '''Precondition: s is a string of 8 bits.
    Returns the binary representation of binaryToNum(s) + 1.'''
    
    result =  numToBinary(binaryToNum(s) + 1)
    
    if len(result)<8 :
        return (8-len(result))*'0' + result 
    elif len(result) > 8:
        diff = len(result) - 8
        return result[diff:]
    return result 
    

print(increment('11111111'))
    

def count(s, n):
    '''Precondition: s is an 8-bit string and n >= 0.
    Prints s and its n successors.'''
    
    def count_helper(s,n,size):
        if n==0:
            print(s) 
            return 
        elif size > n:
            return  
        else:
            print(s)
            result  = increment(s)
            return count_helper(result,n,size+1)
    return count_helper(s,n,0)

print(count("00000000", 4))



            
#2012
def numToTernary(n):
    '''Precondition: integer argument is non-negative.
    Returns the string with the ternary representation of non-negative integer
    n. If n is 0, the empty string is returned.'''
    if n == 0:
        return ''
    elif n%3 == 0:
        return numToTernary(n//3) + '0'
    elif n%3 == 1:
        return numToTernary(n//3) + '1'
    elif n%3 == 2:
        return numToTernary(n//3) + '2'
    
print(numToTernary(42))

def ternaryToNum(s):
    '''Precondition: s is a string of 0s, 1s, and 2s.
    Returns the integer corresponding to the ternary representation in s.
    Note: the empty string represents 0.'''
    if s == "" or s=="0":
        return 0
    else:
        return 3**(len(s)-1)*int(s[0]) + ternaryToNum(s[1:]) 

print(ternaryToNum('12211010'))
        
