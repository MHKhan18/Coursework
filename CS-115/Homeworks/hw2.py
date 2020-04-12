    '''
Created on 19 September 2018
@author:   Mohammad Khan
Pledge:    I pledge my honor that I have abided by the Stevens Honor System.

CS115 - Hw 2
'''
import sys
from cs115 import map, reduce, filter
# Be sure to submit hw2.py.  Remove the '_template' from the file name.

# Allows up to 10000 recursive calls.
# The maximum permitted limit varies from system to system.
sys.setrecursionlimit(10000)

# Leave the following lists in place.
scrabbleScores = \
   [ ['a', 1], ['b', 3], ['c', 3], ['d', 2], ['e', 1], ['f', 4], ['g', 2],
     ['h', 4], ['i', 1], ['j', 8], ['k', 5], ['l', 1], ['m', 3], ['n', 1],
     ['o', 1], ['p', 3], ['q', 10], ['r', 1], ['s', 1], ['t', 1], ['u', 1],
     ['v', 4], ['w', 4], ['x', 8], ['y', 4], ['z', 10] ]

Dictionary = ['a', 'am', 'at', 'apple', 'bat', 'bar', 'babble', 'can', 'foo',
              'spam', 'spammy', 'zzyzva']

# Implement your functions here.


def letterScore(letter,scorelist):
    '''Returns the number associated with scorelist for the letter.'''
    if scorelist == []:
        return 0
    elif scorelist[0][0] == letter:
        return scorelist[0][1]
    return letterScore(letter, scorelist[1:])

print(letterScore("z", scrabbleScores))


def wordScore(S,scorelist):
    '''Returns the sum of the corresponding numbers of S.'''
    if scorelist == [] or  S == '':
        return 0
    return letterScore(S[0], scorelist)+ wordScore(S[1:], scorelist)

print(wordScore("wow", [['o', 10], ['w', 42]]))

def remove(e,L):
    '''Removes  e from L.'''
    if L == []:
        return L
    elif L[0]==e:
        return L[1:]
    return [L[0]] + remove(e,L[1:]) 


def is_possible(word,Rack):
    '''Determines whether or not a given string can be made from 
       the given Rack list.'''
    word.lower()
    if word == '':
        return True
    elif word[0] in Rack:
        return is_possible(word[1:],remove(word[0],Rack))
    return False

print(is_possible('Foot',['a','v','h','f']))


def list_of_words(Dictionary,Rack):
    '''Determines the list of all strings in the dictionary that 
       can be made from from the given Rack.'''
    return filter( lambda word: is_possible(word,Rack),Dictionary)

print(list_of_words(Dictionary,['a','s','m','t','p']))


def scoreList(Rack):
    '''Returns the score of the possible words.'''
    return map(lambda Word:[Word,wordScore(Word,scrabbleScores)],list_of_words(Dictionary,Rack))

print(scoreList(["a", "s", "m", "t", "p"]))
 
def bestWord(Rack):
    '''Returns the highest scoring string with the score'''
    scorelist = scoreList(Rack)
    if scorelist == []:
        return ['',0]
    
    def better_word(x,y):
        if x[1]>y[1]:
            return x 
        return y 
    return reduce(better_word, scorelist)

print(bestWord(["a", "s", "m", "t", "p"]))


     
     

    
    
    
