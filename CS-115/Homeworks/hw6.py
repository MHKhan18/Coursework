'''
Created on Oct 21, 2018

@author: Mohammad Khan
username: mkhan13
Pledge: I pledge my honor that I have abided by the Stevens Honor System.
'''
def numToBaseB(N,B):
    '''takes as input a non-negative integer N and a base B 
        and returns a string representing the number N in base B.'''
    if N==0:
        return '0'
    else:
        def numToBaseB_helper(N,B,accum):
            if N==0:
                return accum 
            return numToBaseB_helper(N//B,B,str(N%B)+accum) 
        return numToBaseB_helper(N,B,'')

print(numToBaseB(100, 2))
print(numToBaseB(4, 3))
print(numToBaseB(4, 4))
print(numToBaseB(0, 4))
print(numToBaseB(0, 2))

def baseBToNum(S,B):
    '''return an integer in base 10 representing the same number as S.'''
    if S == '':
        return 0
    return baseBToNum(S[1:],B) + (B**(len(S)-1))*int(S[0])


#===============================================================================
# print(baseBToNum("11", 2))
# print(baseBToNum("11", 3))
# print(baseBToNum("11", 10))
# print(baseBToNum("", 10))
#===============================================================================

def baseToBase(B1,B2,SinB1):
    '''return a string representing the same number in base B2.'''
    return numToBaseB(baseBToNum(SinB1,B1),B2)

#===============================================================================
# print(baseToBase(2, 10, "11"))
# print(baseToBase(10, 2, "3"))
# print(baseToBase(3, 5, "11"))
#===============================================================================

def add(S,T):
    '''takes two binary strings S and T as input and returns their sum'''
    return numToBaseB((baseBToNum(S,2) + baseBToNum(T,2)),2)

#===============================================================================
# print(add("11", "01"))
# print(add("011", "100"))
# print(add("110", "011"))
#===============================================================================

def addB(A,B):
    '''return a new string representing the sum of the two input strings
       using the binary addition algorithm.'''
    def addB_helper(A,B,carryIn):
        if A == '' and B == '':
            return carryIn 
        FullAdder={
            ('0','0','0'):('0','0'),
            ('0','0','1'): ('1','0'),
            ('0','1','0') :('1','0'),
            ('0','1','1'):('0','1'),
            ('1','0','0'):('1','0'),
            ('1','0','1'):('0','1'),
            ('1','1','0'):('0','1'),
            ('1','1','1'):('1','1')}
        if A == '' and B != '':
            sumBit,carryBit  = FullAdder[('0',B[-1],carryIn)]
        elif B == '' and A != '':
            sumBit,carryBit  = FullAdder[(A[-1],'0',carryIn)]
        else:
            sumBit,carryBit  = FullAdder[(A[-1],B[-1],carryIn)]
        return   addB_helper(A[:-1],B[:-1],carryBit) + sumBit
    
    result =  addB_helper(A,B,'0')
    
    
    def trimmer(result):
        if len(result)> 1 and result[0] == '0':
            return trimmer(result[1:])
        return result 
    return trimmer(result)


#===============================================================================
# print(addB("11", "1"))
# print(addB("011", "100"))
# print(addB("0111010", "1"))
# print(addB("0", "1001110101"))
#     
#     
#===============================================================================
    


