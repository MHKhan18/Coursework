'''
Created on Oct 2, 2018

@author: Mohammad Khan
username: mkhan13

I pledge my honor that I have abided by the Stevens Honor System.

'''

 

from cs115 import map

def pascal_row(n):
    '''Returns the list of elements found in the nth row of Pascal's triangle.'''
    if n==0:
        return [1]
    previous_row = pascal_row(n-1)
    def pascal_row_helper(previous_row):
        if len(previous_row)<=1:
            return []
        return [previous_row[0]+previous_row[1]]+pascal_row_helper(previous_row[1:])
    return [1]+pascal_row_helper(previous_row)+[1]
    


def pascal_triangle(n):
    '''Returns the list of elements found in the nth row of Pascal's triangle.'''
    return map(pascal_row,range(0,n+1))




    