'''
Created on 26 September 2018
@author: Mohammad Khan
username: mkhan13
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

CS115 - Hw 3
'''
# Be sure to submit hw3.py.  Remove the '_template' from the file name.

'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' PROBLEM 0
' Implement the function giveChange() here:
' See the PDF in Canvas for more details.
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
# your code goes here


def giveChange(amount,coins):
    '''Returns the minimum number of coins necessary and 
        the list of coins to make the change.'''
    if amount == 0:
        return [0,[]]
    if coins == []:
        return [float("inf"),[]]
    if coins[0]>amount:
        return giveChange(amount,coins[1:])
    lose_it = giveChange(amount,coins[1:])
    use_it = giveChange(amount-coins[0],coins)##need to add to every time use_it is executed
    if use_it[0] < lose_it[0]:
        return [1+use_it[0],[coins[0]]+use_it[1]]##need to return the list
    return [lose_it[0], lose_it[1]]

                

print(giveChange(35, [1, 3, 16, 30, 50]))
print(giveChange(48, [1, 5, 10, 25, 50]))
print(giveChange(48, [1, 7, 24, 42]))

      

# Here's the list of letter values and a small dictionary to use.
# Leave the following lists in place.
scrabbleScores = \
   [ ['a', 1], ['b', 3], ['c', 3], ['d', 2], ['e', 1], ['f', 4], ['g', 2],
     ['h', 4], ['i', 1], ['j', 8], ['k', 5], ['l', 1], ['m', 3], ['n', 1],
     ['o', 1], ['p', 3], ['q', 10], ['r', 1], ['s', 1], ['t', 1], ['u', 1],
     ['v', 4], ['w', 4], ['x', 8], ['y', 4], ['z', 10] ]

Dictionary = ['a', 'am', 'at', 'apple', 'bat', 'bar', 'babble', 'can', 'foo',
              'spam', 'spammy', 'zzyzva']

'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' PROBLEM 1
' Implement wordsWithScore() which is specified below.
' Hints: Use map. Feel free to use some of the functions you did for
' homework 2 (Scrabble Scoring). As always, include any helper
' functions in this file, so we can practice it.
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
def letterScore(letter,scorelist):
    '''Returns the number associated with scorelist for the letter.'''
    if scorelist == []:
        return 0
    elif scorelist[0][0] == letter:
        return scorelist[0][1]
    return letterScore(letter, scorelist[1:])

def wordScore(S,scorelist):
    '''Returns the sum of the corresponding numbers of S.'''
    if scorelist == [] or  S == '':
        return 0
    return letterScore(S[0], scorelist)+ wordScore(S[1:], scorelist)

def wordsWithScore(dct, scores):
    '''List of words in dct, with their Scrabble score.

    Assume dct is a list of words and scores is a list of [letter,number]
    pairs. Return the dictionary annotated so each word is paired with its
    value. For example, wordsWithScore(Dictionary, scrabbleScores) should
    return [['a', 1], ['am', 4], ['at', 2] ...etc... ]
    '''
    return list(map(lambda word:[word, wordScore(word,scores)],dct))


#print(wordsWithScore(Dictionary,scrabbleScores))



'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' PROBLEM 2
' For the sake of an exercise, we will implement a function
' th-at does a kind of slice. You must use recursion for this
' one. Your code is allowed to refer to list index L[0] and
' also use slice notation L[1:] but no other slices.
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''

    
def take(n, L):
    '''Returns the list L[0:n].'''# your code goes here
    if L == []:
        return []
    if n>len(L):
        return L
    if n<0:
        return take((len(L)+n),L)
    if n>0:
        return [L[0]]+take(n-1,L[1:])
    return []
      
    
#print(take(-3,['a','b','c','d','e']))
#print(take(3,['a','b','c','d','e']))
#print(take(2,[]))
#print(take(-2,[]))
#print(take(0,['a','b','c','d','e']))
#print(take(0,[]))

    
    
      



'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
' PROBLEM 3
' Similar to problem 2, will implement another function
' that does a kind of slice. You must use recursion for this
' one. Your code is allowed to refer to list index L[0] and
' also use slice notation L[1:] but no other slices.
'''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''''
def drop(n, L):
    '''Returns the list L[n:].'''
    if  L == []:
        return []
    if n == 0:
        return L
    if n<0:
        return drop((len(L)+n),L)
    return drop(n-1,L[1:])

#print(drop(3,['a','b','c','d','e','f']))
#print(drop(-4,['a','b','c','d','e','f']))
#print(drop(-2,[]))
#print(drop(2,[]))
#print(drop(0,[]))
#print(drop(0,['a','b','c','d','e','f']))
        
   


