'''
Created on Nov 29, 2018

@author: Mohammad Khan
username: mkhan13
Pledge: "I pledge my honor that I have abided by the Stevens Honor System."
'''


class Board(object):
    
    def __init__(self,width=7, height=6):
        '''initializes width, height and an empty board.'''
        self.__width = width 
        self.__height = height 
        self.__board = []
        for row in range(height):
            temp = []
            for col in range(width):
                temp += [' ']
            self.__board.append(temp)
            
        
    def __str__(self):
        '''This method returns a string (it does not print a string) 
           representing the Board object that calls it. Basically,
           each "checker" takes up one space, and all columns are 
           separated by vertical bars (|). The columns are labeled at
            the bottom.'''
        
        form =  '|'
        
        for row in  range(self.__height):
            for col in range(self.__width):
                form+= self.__board[row][col] + '|'
            if col != self.__width:
                if row == self.__height-1:
                    form+=('\n')
                else:
                    form+=('\n'+ '|')
            
                    
                
        form+= '-' * (2 * self.__width + 1) + "\n"
        
        for i in range(self.__width):
            form+= (' '+str(i))
        form+=' '
        
        return form 
    
    
    def allowsMove(self, col):
        '''This method should return True if the calling 
           Board object can allow a move into column c 
           (because there is space available). It returns 
           False if c does not have space available or if 
           it is not a valid column. Thus, this method should 
           check to be sure that c is within the range from 0 
           to the last column and make sure that there is still 
           room left in the column!'''
        
              
        for row in range(self.__height):
            if self.__board[row][col]==' ' and  0<=col<=self.__width:
                return True 
        return False 
    
    def addMove(self,col,ox):
        '''This method should add an ox checker, 
           where ox is a variable holding a string 
           that is either "X" or "O", into column col.
            Note that the code will have to find the 
            highest row number available in the column 
            col and put the checker in that row. The 
            highest row number available is the highest 
            index with a space character ' ' in the 
            column c. Notice that the highest row number 
            corresponds to the lowest physical row on the 
            board.'''
        
        for row in range(self.__height-1,-1,-1):
            if self.__board[row][col] == ' ':
                self.__board[row][col] = ox 
                break  
             
                
    def setBoard( self, moveString ):
        """ takes in a string of columns and places
        alternating checkers in those columns,
        starting with 'X'
        For example, call b.setBoard('012345')
        to see 'X's and 'O's alternate on the
        bottom row, or b.setBoard('000000') to
        see them alternate in the left column.
        moveString must be a string of integers
        """
        nextCh = 'X' # start by playing 'X'
        for colString in moveString:
            col = int(colString)
            if 0 <= col <= self.__width:
                self.addMove(col, nextCh)
            if nextCh == 'X': nextCh = 'O'
            else: nextCh = 'X'
            
    
    def delMove(self,col):
        '''This method should do the "opposite" of addMove. 
           That is, it should remove the top checker from the 
           column col. If the column is empty, then delMove 
           should do nothing.'''
        for row in range(self.__height):
            if self.__board[row][col] == 'X' or \
               self.__board[row][col] == 'O':
                self.__board[row][col] = ' '
                
    def winsFor(self, ox):
        '''This method should return True if the given checker, 
           'X' or 'O', held in ox, has won the calling Board. 
           It should return False otherwise.'''
        
        #horizontal check:
        for row in range(self.__height-1,-1,-1):
            for col in range(self.__width-4+1):
                if self.__board[row][col] == ox and \
                   self.__board[row][col+1] == ox and \
                   self.__board[row][col+2] == ox and \
                   self.__board[row][col+3] == ox :
                    return True 
                
                
                    
        #vertical check:
        for col in range(self.__width):
            for row in range(self.__height-4+1):
                if self.__board[row][col] == ox and \
                   self.__board[row+1][col] == ox and \
                   self.__board[row+2][col] == ox and \
                   self.__board[row+3][col] == ox :
                    return True 
                
        #major diagonal check:
        for row in range(self.__height-1,self.__height-5+1,-1):
            for col in range(self.__width-4+1):
                if self.__board[row][col] == ox and \
                   self.__board[row-1][col+1] == ox and \
                   self.__board[row-2][col+2] == ox and \
                   self.__board[row-3][col+3] == ox :
                    return True 
                
                
                
        #minor diagonal check:
        for row in range(self.__height-4+1):
            for col in range(self.__width-4+1):
                if self.__board[row][col] == ox and \
                   self.__board[row+1][col+1] == ox and \
                   self.__board[row+2][col+2] == ox and \
                   self.__board[row+3][col+3] == ox :
                    return True 
                
        return False 
    
    def hostGame( self ): 
        '''This is a method that, when called from a connect 
           four board object, will run a loop allowing the 
           user(s) to play a game.'''
        
        turn = 0
        
        print("Welcome to Connect Four! ")
        print()
        
        while True:
            print()
            print(self)
            ox = ' '
            if turn % 2 == 0:
                ox = "X"
            else:
                ox = "O"
            print()    
            option = int(input(ox + "'s choice: " ))
            
            if self.allowsMove(option) == True:
                self.addMove(option, ox)
                turn  += 1
                
            if self.winsFor(ox)==True:
                print()
                print(ox + "wins -- Congratulations!")
                print()
                print(self)
                break 
                
                
                
        
        
                    
                    
                
b = Board()

b.hostGame()



                
                
        
        
        
                
                        
                
        
        
        
                
            
                            
                    
            
    
    
            
                
            
            
        
        
        
        
    
        
        
        
    
            
        
    
