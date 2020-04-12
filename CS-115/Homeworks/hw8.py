'''
Created on Oct 27, 2018

@author: Mohammad Khan
username: mkhan13
Pledge: I pledge my honor that I have abided by the Stevens Honor System.
'''


def binaryToNum(positive_part):
    '''returns positive binary representation of a number'''
    if  positive_part== "" or positive_part=="0":
        return 0
    else:
        return 2**(len(positive_part)-1)*int(positive_part[0]) + binaryToNum(positive_part[1:])
        
        
def TcToNum(string):
    '''takes as input a string of 8 bits representing an integer in 
       two's-complement,and returns the corresponding integer'''
    positive_part = string[1:]
    return -128*int(string[0]) + binaryToNum(positive_part)

#===============================================================================
# print(TcToNum("00000001"))
# print(TcToNum("11111111"))
# print(TcToNum("10000000"))
# print(TcToNum("01000000"))
#===============================================================================






def NumToTc(N):
    '''returns a string representing the two's-complement 
       representation of N'''
    
    # extraneous range for eight bit representation results in error
    if N<-128 or N>127:
        return 'Error'
    
    Positive_N= abs(N)
    
    def NumToBin(x):
        '''Precondition: integer argument is non-negative.
           Returns the string with the binary representation of non-negative integer n.
          If n is 0, the empty string is returned.'''
        if x == 0:
            return ''
        return NumToBin(x//2) + str(x%2)
    
    def Eight_Bitter(y):
        '''Pads 0s in front of smaller strings''' 
        if len(y)<8:
            return (8-len(y))*'0' + y 
        else:
            return  y 
    
    result = Eight_Bitter(NumToBin(Positive_N)) 
        
    #Returns the string if N is positive    
    if N>=0:
        return result 
    # toggles and adds 1 if N is negative 
    else:
        def toggler(st):
            '''Changes 0s to 1s and 1s to 0s in the string.'''
            if st == '':
                return ''
            if st[0] == '0':
                return '1' + toggler(st[1:])
            if st[0] == '1':
                return '0' + toggler(st[1:])
            
        def add_one(z):
            '''increment the toggled string by 1'''
            final  = NumToBin(binaryToNum(z)+1)
            if len(final)>8:
                return 'Error'
            return final 
        
        return add_one(toggler(result))


#===============================================================================
# print(NumToTc(1))
# print(NumToTc(128))
# print(NumToTc(200))
# print(NumToTc(-56))
#===============================================================================


        
    

     
        
            
    

