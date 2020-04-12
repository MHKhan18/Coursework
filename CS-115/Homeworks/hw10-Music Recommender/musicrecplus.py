'''
Created on Nov 12, 2018

@author: Mohammad Khan
username: mkhan13
I pledge my honor that I have abided by the Stevens Honor System.

'''

PREF_FILE = "musicrecplus.txt"

def loadUsers(fileName):
    ''' Reads in a file of stored users' preferences
        stored in the file 'fileName'.
        Returns a dictionary containing a mapping
        of user names to a list preferred artists
    '''
    try:
        file = open(fileName, 'r')
    except:
        file = open(fileName, 'w')
        file.close
        return {}
    userDict = {}
    for line in file:
        # Read and parse a single line
        userName, bands = line.strip().split(":")
        bandList = bands.split(",")
        bandList.sort()
        userDict[userName] = bandList
    file.close()
    return userDict 

def Menu(userName,userMap):
    """gives the user choices and acts on them"""
    while True:
        userInput=input("Enter a letter to choose an option:\ne - Enter preferences\n"+\
                        "r - Get recommendations\np - Show most popular artists\n"+\
                        "h - How popular is the most popular\n" +\
                        "m - Which user has the most likes\nq - Save and quit\n")
        if userInput=='e':
            getPreferences(userName, userMap, PREF_FILE)
        elif userInput=='r':
            try:
                print_r(getRecommendations(userName, userMap[userName], userMap))
            except:
                print('No recommendations available at this time')
        elif userInput=='p':
            print_p(Most_Popular_Artists(PREF_FILE))
        elif userInput=='h':
            print(num_popular(PREF_FILE))
        elif userInput=='m':
            print_m(user_with_most_likes(PREF_FILE))
        elif userInput=='q':
            try:
                saveUserPreferences(userName, userName[userMap], userMap, PREF_FILE)
                break
            except:
                break
                
def getPreferences(userName, userMap, fileName):
    ''' Collects and saves a list of the uesr's preferred artists.'''
    prefs = []
    newPref= input('Enter an artist that you like (Enter to finish):\n')
    while newPref != "":
        prefs.append(newPref)
        newPref = input('Enter an artist that you like (Enter to finish):\n')

    prefs.sort()
    saveUserPreferences(userName, prefs, userMap, fileName)
    
    
def saveUserPreferences(userName, prefs, userMap, fileName):
    ''' Writes all of the user preferences to the file.
        Returns nothing. '''
    prefs.sort()
    userMap[userName] = prefs
    
    file = open(fileName, "w")
    for user in userMap:
        toSave = str(user) + ":" + ",".join(userMap[user]) + "\n"
        file.write(toSave)
    file.close()
         


def numMatches( list1, list2 ):
    ''' return the number of elements that match between
        two sorted lists '''
    matches = 0
    i = 0
    j = 0
    while i < len(list1) and j < len(list2):
        if list1[i] == list2[j]:
            matches += 1
            i += 1
            j += 1
        elif list1[i] < list2[j]:
            i += 1
        else:
            j += 1
    return matches

def findBestUser(currUser, prefs, userMap):
    ''' Find the user whose tastes are closest to the current
        user.  Return the best user's name (a string) '''
    users = userMap.keys()
    bestUser = None
    bestScore = -1
    for user in users:
        if user[-1]!= '$':
            score = numMatches(prefs, userMap[user])
            if score > bestScore and currUser != user and userMap[currUser] != userMap[user]:
                bestScore = score
                bestUser = user
    return bestUser


def drop(list1, list2):
    ''' Return a new list that contains only the elements in
        list2 that were NOT in list1. '''
    list3 = []
    i = 0
    j = 0
    while i < len(list1) and j < len(list2):
        if list1[i] == list2[j]:
            i += 1
            j += 1
        elif list1[i] < list2[j]:
            i += 1
        else:
            list3.append(list2[j])
            j += 1
    while j<len(list2):
        list3.append(list2[j])
        j+=1
    return list3

def getRecommendations(currUser, prefs, userMap):
    ''' Gets recommendations for a user (currUser) based
        on the users in userMap (a dictionary)        and the user's preferences in pref (a list).
        Returns a list of recommended artists.  '''
    bestUser = findBestUser(currUser, prefs, userMap)
     
    recommendations = drop(prefs, userMap[bestUser])
    return recommendations

def print_r(recs):
    """prints artists recommended"""
    if recs==[]:
        print('No recommendations available at this time')
    else:
        for arts in recs:
            print(arts)
            
def artists(userMap):
    '''Returns a list of artists with all of them appearing only once'''
    artists = []
    filtered = []
    for user in userMap:
        artists += userMap[user]
    for artist in artists:
        if artist not in filtered:
            filtered += [artist]
    return filtered

def convertToDictionary(lst):
    '''Converts list to dictionary'''
    Dictionary = {}
    for i in lst:
        Dictionary[i] = 0
    return Dictionary

def Most_Popular_Artists(file):
    """returns a list of most popular artist/s"""
    ArtistFreq = convertToDictionary(artists(loadUsers(file)))
    UserMap = loadUsers(file)
    
    for user in UserMap:
        if user[-1]!='$':    
            for artist in UserMap[user]:
                ArtistFreq[artist] += 1
    x = 0
    y = []
   
    for artist in ArtistFreq:
        if ArtistFreq[artist] > x :
            x = ArtistFreq[artist]
            y = [artist]
        elif ArtistFreq[artist] == x:
            y+=[artist]
    return y  

def print_p(pop):
    """prints most popular artists"""
    if pop==[]:
        print('Sorry, no artists found')
    else:
        for arts in pop:
            print(arts)
            
def num_popular(file):
    """number of users who prefer the  most popular artist/s"""
    ArtistFreq = convertToDictionary(artists(loadUsers(file)))    
    userMap = loadUsers(file)
    for user in userMap:
        if user[-1]!='$': 
            for artist in userMap[user]:
                ArtistFreq[artist] += 1
    x = 0
    for artist in ArtistFreq:
        if ArtistFreq[artist] > x:
            x = ArtistFreq[artist]
    return x

def user_with_most_likes(file):
    '''returns name of the user/s with the longest preferred list of 
      artists except those whose names end with $'''
    
    userMap = loadUsers(file)
    x = 0
    y = []
    for user in userMap:
        if user[-1]!='$': 
            if len(userMap[user]) > x:
                x = len(userMap[user])
                y = [user]
            elif len(userMap[user])==x:
                y+= [user]  
    return y
        
def print_m(users):
    """prints users with most liked artists"""
    if users==[]:
        print('Sorry, no user found')
    else:
        for user in users:
            print(user)

def main():
    ''' The user should be prompted for their name. If the user is
        a new user (not already in the musicrecplus.txt file), they 
        should be prompted to enter their initial preferences before 
        they can move on to the menu. If the user is not new, they should 
        not be asked their preferences and should immediately be shown the menu'''
    userMap = loadUsers(PREF_FILE)
    userName = input("Enter your name (put a $ symbol after your name if you wish your preferences to remain private):\n")
    if userName not in userMap:
        getPreferences(userName, userMap, PREF_FILE)
    Menu(userName,userMap)
    

main()



         
    
    
                