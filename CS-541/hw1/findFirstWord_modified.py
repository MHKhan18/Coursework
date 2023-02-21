from typing import List

WORD_SIZE_LIMIT = 20

def findAlphabeticallyFirstWord(string: str) -> str:
    '''Find the first word in alphabetical order'''
    
    words: List[str] = string.split(',') if "," in string else string.split(' ')
    words = list(map(lambda word: word.strip(), words))
    words = list(filter(lambda word: len(word) > 0, words))
    
    if len(words) == 0:
        return ''
    
    found = False
    word_index, res_word = 0 , words[0]
    while (not found) and word_index < WORD_SIZE_LIMIT:
        diff_char = words[0][word_index]
        cur_chars = set(diff_char)
        for word in words:
            if not (word_index < len(word)):
                res_word = word
                found = True
            
            cur_chars.add(word[word_index])
            
            if word[word_index] < diff_char:
                res_word = word
                diff_char = word[word_index] 
                
        
        word_index += 1
        
        if len(cur_chars) > 1:
            found = True

    return res_word




def test():
    
    assert findAlphabeticallyFirstWord('the brown fox also jumped') == 'also', "test-1"
    assert findAlphabeticallyFirstWord('parrot pathway pass picture') == 'parrot', "test-2"
    assert findAlphabeticallyFirstWord('cdab dcba bcad adbc') == 'adbc', "test-3"


if __name__ == "__main__":
    # test()
    while True:
        user_string = input("Input:\n").strip()
        first_word = findAlphabeticallyFirstWord(user_string)
        print(f"Output:\n{first_word}\n")