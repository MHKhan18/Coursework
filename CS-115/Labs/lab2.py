'''
Created on Sep 13, 2018
I pledge my honor that I have abided by the Stevens Honor System.
@author: Mohammad Khan
username: mkhan13

'''
def dot(L,K):
    '''Returns dot product of the lists l and K.'''
    if L == [] or K == []:
        return 0
    return L[0]*K[0] + dot(L[1:],K[1:])


#print(dot([5,3],[6,4]))

def explode(s):
    '''Takes a string as input and returns a list of characters.'''
    if s== '':
        return []
    return [s[0]] + explode(s[1:])
#print(explode("spam"))





def ind(e,L):
    '''Returns the index of e in L.'''
    if L==[] or L =='': 
        return 0
    elif L[0]==e:
        return 0
    return 1+ind(e,L[1:]) 

#print(ind(42,range(1,100)))


def removeAll(e,L):
    '''Removes all e from L.'''
    if L == []:
        return L
    elif L[0]==e:
        return removeAll(e,L[1:])
    return [L[0]] + removeAll(e,L[1:]) 

print(removeAll(42,[55,77,42,11,42,88]))


def myFilter(myFunction,myList):
    '''Returns the elements of myList that returns True.'''
    if myList==[]:
        return []
    elif myFunction(myList[0]) == False:
        return myFilter(myFunction,myList[1:])
    return [myList[0]] + myFilter(myFunction,myList[1:])
    
#def even(x):
#   if x % 2 == 0 :return True
#  else: return False
    
#print(myFilter(even,[0,1,2,3,4,5,6]))
        
def deepReverse(L):
    '''Reverses the elements of the list.'''
    if L==[]:
        return []
    elif isinstance(L[0],list):
        return deepReverse( L[1:]) + [deepReverse(L[0])]
    return deepReverse(L[1:])+[L[0]]

#print(deepReverse([1,[2,3],4]))
    
  