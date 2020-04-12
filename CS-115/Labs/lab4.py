'''
Created on Sep 26, 2018

@author: Mohammad Khan
username: mkhan13

I pledge my honor that I have abided by the Stevens Honor System.
'''


def test_knapsack(capacity,itemList):
    
    if capacity == 0 or itemList == []:
        return 0
    if itemList[0][0]>capacity:
        return test_knapsack(capacity,itemList[1:])
    
    lose_it = test_knapsack(capacity,itemList[1:])
    use_it = test_knapsack(capacity-itemList[0][0],itemList[1:]) + itemList[0][1]
    
    return max(use_it,lose_it)

print(test_knapsack(76, [[36, 35], [10, 28], [39, 47], [8, 1], [7, 24]])) 



        
    
    

def knapsack(capacity,itemList):
    '''Returns the maximum value and the list of items that make that value
        from the itemList without exceeding the capacity.
        i.e. [max.value,[elements of the itemList]]'''
    
    if capacity == 0 or itemList == []:
        return [0,[]]
    
    
    if itemList[0][0]>capacity:
        return knapsack(capacity,itemList[1:])
    
    lose_it = knapsack(capacity,itemList[1:])
    use_it = knapsack(capacity-itemList[0][0],itemList[1:]) 
    new_sum = itemList[0][1] + use_it[0]
    if new_sum > lose_it[0]:
        return [new_sum,[itemList[0]]+use_it[1]]##need to return the list
    return lose_it

    
    
    
    
    
   


print(knapsack(76, [[36, 35], [10, 28], [39, 47], [8, 1], [7, 24]])) 


##[[
    
    
