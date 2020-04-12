'''
Created on Sep 20, 2018

@author: Mohammad Khan
username: mkhan13

I pledge my honor that I have abided by the Stevens Honor System.
'''
def change(amount,coins):
    '''Returns the smallest number of coins required to make the amount of change.'''
    if amount == 0:
        return 0
    elif coins == []:
        return float("inf")

    elif coins[0]>amount:
        return change(amount,coins[1:])
    
    
    lose_it = change(amount,coins[1:])
    
    use_it = 1 + change(amount-coins[0], coins)
    
    return min(use_it,lose_it)


print(change(48, [1, 5, 10, 25, 50]))   
print(change(48, [1, 7, 24, 42]))

    
    
        





                    
 
 
 
 
 
 
 
 
