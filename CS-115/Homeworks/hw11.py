'''
Created on 21 November 2018
@author: Mohammad Khan
username: mkhan13
Pledge: I pledge my honor that I have abided by the Stevens Honor System.

CS115 - Hw 11 - Date class
'''

DAYS_IN_MONTH = (0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

class Date(object):
    '''A user-defined data structure that stores and manipulates dates.'''

    # The constructor is always named __init__.
    def __init__(self, month, day, year):
        '''The constructor for objects of type Date.'''
        self.month = month
        self.day = day
        self.year = year

    # The 'printing' function is always named __str__.
    def __str__(self):
        '''This method returns a string representation for the
           object of type Date that calls it (named self).

             ** Note that this _can_ be called explicitly, but
                it more often is used implicitly via the print
                statement or simply by expressing self's value.'''
        return '%02d/%02d/%04d' % (self.month, self.day, self.year)

    # Here is an example of a 'method' of the Date class.
    def isLeapYear(self):
        '''Returns True if the calling object is in a leap year; False
        otherwise.'''
        if self.year % 400 == 0:
            return True
        if self.year % 100 == 0:
            return False
        if self.year % 4 == 0:
            return True
        return False
    
    def copy(self):
        '''Returns a new object with the same month, day, year
           as the calling object (self).'''
        dnew = Date(self.month, self.day, self.year)
        return dnew
    
    
    def equals(self, d2):
        '''Decides if self and d2 represent the same calendar date,
        whether or not they are the in the same place in memory.'''
        return self.year == d2.year and self.month == d2.month and \
            self.day == d2.day
            
    def tomorrow(self):
        '''Changes the calling object so that it represents one calendar 
            day after the date it originally represented'''
        
        if self.isLeapYear() is True and self.month==2 and self.day==28:
            self.month=2
            self.day = 29
            
        else:
            self.day += 1
        
            if self.day > DAYS_IN_MONTH[self.month]: 
                self.month += 1
                if self.month > 12:
                    self.year += 1
                    self.month = 1
                self.day = 1 
            
    def yesterday(self):
        '''Changes the calling object so that it represents one calendar 
            day before the date it originally represented'''
        if self.isLeapYear() is True and self.month==3 and self.day==1:
            self.month=2
            self.day = 29
             
        else:
            self.day -= 1
         
            if self.day==0: 
                self.month -= 1
                if self.month==0:
                    self.year -= 1
                    self.month = 12
                self.day = DAYS_IN_MONTH[self.month]
            
    
    def addNDays(self, N):
        '''change the calling object so that it represents N calendar 
           days after the date it originally represented.(end points inclusive)'''
        
        for i in range(N+1):
            print (self)
            if i<N:
                self.tomorrow()
            
    def subNDays(self, N):
        '''change the calling object so that it represents N calendar 
           days before the date it originally represented.(end points inclusive)'''
        
        for i in range(N+1):
            print (self)
            if i < N:
                self.yesterday()
                
    def isBefore(self, d2):
        '''This method should return True if the calling object is a calendar 
          date before the input named d2 (which will always be an object of type 
          Date). If self and d2 represent the same day, this method should return 
          False. Similarly, if self is after d2, this should return False.'''
        
        if self.year < d2.year:
            return True 
        elif self.year == d2.year and self.month < d2.month:
            return True
        elif self.year == d2.year and self.month == d2.month and self.day<d2.day:
            return True 
        else:
            return False 
        
    def isAfter(self,d2):
        '''This method should return True if the calling object is a calendar 
          date after the input named d2 (which will always be an object of type Date).
          If self and d2 represent the same day, this method should return False.
          Similarly, if self is before d2, this should returnFalse.'''
        
        if self.isBefore(d2) is True or self.equals(d2) is True:
            return False
        else:
            return True 
        
    def diff(self, d2):
        '''return an integer representing the number of days between self and d2'''
        
        day1 = self.copy()
        day2 = d2.copy()
        
        diff = 0
        
        while day1.isBefore( day2 ):
            day1.tomorrow()
            diff -= 1
            
        while day1.isAfter( day2 ):
            day1.yesterday()
            diff += 1 
            
        return diff 
    
    
    def dow(self):
        '''return a string that indicates the day of the week (dow) 
           of the object (of type Date) that calls it.'''
        
        dKnown = Date(11,9,2011)
        
        val = (self.diff(dKnown) % 7)
        
        if val==0:
            return 'Wednesday'
        elif val==1:
            return 'Thursday'
        elif val==2:
            return 'Friday'
        elif val==3:
            return 'Saturday'
        elif val==4:
            return 'Sunday'
        elif val==5:
            return 'Monday'
        elif val==6:
            return 'Tuesday'
        
        
        
        
    
         
           
           
           
           
           
   
        
        
         
        
            
            
            
            
            
            
        
        
        
        
        
        
            
        
        
            
            
            
        
        
                
        
        
        
        
                
                
        
        
            
        
        
    
#===============================================================================
# d = Date(1, 1, 2011)
# d2 = d.copy()
# print( d==d2)
# print(d.equals(d2))
# print(d.equals(Date(1, 1, 2011)))
# print(d == Date(1, 1, 2011))
#  
# d = Date(12, 31, 2010)
# d.tomorrow()
# print(d)
#  
# d = Date(2, 28, 2012)
# d.tomorrow()
# print(d)
# d.tomorrow()
# print(d)
#  
# 
#  
# d = Date(11, 9, 2011)
# print(d.addNDays(3))
#  
# d = Date(11, 11, 2011)
# #print(d.addNDays(1283))
#  
# d=Date(11,12,2011)
# print(d.subNDays(3))
#  
# d = Date(11, 11, 2011)
# d2 = Date(1, 1, 2012)
# print(d.isBefore(d2))
# print(d2.isBefore(d))
# print(d.isBefore(d))
#  
# d = Date(11, 11, 2011)
# d2 = Date(1, 1, 2012)
# print(d.isAfter(d2))
# print(d2.isAfter(d))
# print(d.isAfter(d))
#  
#  
# 
#  
# d = Date(11,9,2011)
# d3 = Date(5, 18, 2012)
# print(d3.diff(d))
#  
# d = Date(11, 9, 2011)
# print(d.diff(Date(1, 1, 1899)))
# print(d.diff(Date(1, 1, 2101)))
#  
# d = Date(12, 7, 1941)
# print(d.dow())
#  
#  
# print(Date(10, 28, 1929).dow())
#  
# d = Date(1, 1, 2100)
# print(d.dow())
# 
# 
# d = Date(1, 1, 2011)
# d.yesterday()
# print(d)
# d = Date(3, 1, 2012)
# d.yesterday()
# print(d)
# d.yesterday()
# print(d)
# 
# d = Date(11,9,2011)
# d2 = Date(12,16,2011)
# print(d2.diff(d))
# print(d.diff(d2))
#             
#             
#===============================================================================