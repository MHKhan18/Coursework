/*******************************************************************************
 * Name        : anagramfinder.cpp
 * Author      : Mohammad Khan 
 * Date        : 11/11/19
 * Description : Returns the list of all anagrams in a text file of words ( one word/ line)
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <iostream>
#include <sstream>
#include <fstream>
#include <string>
#include <vector>
#include <unordered_map>
#include <algorithm>

using namespace std;


// calculates the integer value ( key) of a word
// returns an array 
// first value of array indicates whether the word is a valid english word
// second value od array gives the ascii value of the lowercase word 
int* get_val( string word,  unordered_map< char, int > dict ){

	static int res[2];   // static so that the array can be accessed outside the function

	int val = 1;
	for ( int i=0; i < static_cast<int>(word.length()); ++i){
		if ( word[i] == '\r' ||  word[i] == '\n' ){ break;  }  // /r/n in windows while /n in linux
		if ( (word[i] >= 'A' && word[i] <= 'Z') || ( word[i] >= 'a' && word[i] <= 'z') ){

			if ( word[i] >= 'A' && word[i] <= 'Z' ){ // upper case letter
				val *=  dict[word[i]+32];   // treat as lowercase equivalent
			} 
			else{
				val *= dict[word[i]];
			}

		}
		else{ // non-ascii character found
			res[0] = 0;
			res[1] = 0;
			return res;  // the word should not be processed
		}
			
	}

	// valid word
	res[0] = 1;
	res[1] = val;
	return res;
}

// function that builds the hash table of anagrams --> average O(1) operations
void anagram_builder(unordered_map< int, vector<string> > &anagrams, vector<string> words,  unordered_map< char, int > dict ){

	for ( int i=0; i< static_cast<int>(words.size()); ++i ){
		int *eval = get_val( words.at(i), dict );

		if ( eval[0] == 0){
			continue;      // disregard if non-ascii value
		}

		// key -> ascii value of lower case letter
		// value -> the words with this value [ since all words containing same lettres must have same total val]

		anagrams[eval[1]].push_back( words.at(i) ); // updates anagram list or inserts new key/value
	}

}

// function for proper output formatting
void formatter ( unordered_map< int, vector<string> > &anagrams ){
	
	int count= 0;
	vector< string > sorter ;
	vector<int> sizes;

	for ( auto & pair: anagrams ){
		if ( pair.second.size() > 1 ){
			sizes.push_back( pair.second.size() );
			count++;
			// sorting one group of anagram
			sort( pair.second.begin(), pair.second.end() );
			sorter.push_back(pair.second.at(0)); // get first element of sorted array
		}
	}

	if ( count == 0){
		cout << "No anagrams found." << endl;
		return ;
	}

	sort( sizes.begin(), sizes.end());
	int max = sizes.at(static_cast<int>(sizes.size()) - 1)  ;

	cout << "Max anagrams: " << max << endl ;


	// for sorting the groups of anagrams

	sort( sorter.begin(), sorter.end() );  // acoounting for case mix

	for ( int i=0; i< static_cast<int>(sorter.size()) ; ++i ){  
		for ( auto const& pair: anagrams ){
			if ( sorter.at(i).compare(pair.second.at(0)) == 0 && static_cast<int>(pair.second.size())== max ){
				for ( int j=0; j< static_cast<int>(pair.second.size()); ++j ){
					cout << pair.second.at(j) << endl;
				}
				cout<< endl;
				break;
			}
		}
		
	}


}



int main(int argc, char* const argv[]) {

	// incorrect argument
	if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <dictionary file>" << endl;
        return 1;
    }

    string ending = ".txt" ;
    string filename = argv[1];

     if ( (filename.length() < ending.length() ) || 
     		( filename.compare(filename.length() - ending.length(), ending.length(), ending) != 0 )  ){
     	cerr << "Error: input must be a .txt file" << endl ;
     	return 1;
     }

    ifstream input(argv[1]);

    if (!input){
    	cout<< "Error: File " << "\'" << argv[1] << "\'"  << " not found." <<endl;
    	return 1;
    }

    // make a dictionary of eng characters to prime numbers
    // take product of the numbers as they give unique number for each combination of letters

    unordered_map< char, int > dict ;

    dict.insert( {'a',2 });
    dict.insert( {'b',3 });
    dict.insert( {'c',5 });
    dict.insert( {'d',7 });
    dict.insert( {'e', 11 });
    dict.insert( {'f', 13 });
    dict.insert( {'g', 17 });
    dict.insert( {'h', 19 });
    dict.insert( {'i',23 });
    dict.insert( {'j',29 });
    dict.insert( {'k', 31 });
    dict.insert( {'l', 37 });
    dict.insert( {'m', 41 });
    dict.insert( {'n', 43 });
    dict.insert( {'o', 47 });
    dict.insert( {'p', 53 });
    dict.insert( {'q', 59 });
    dict.insert( {'r', 61 });
    dict.insert( {'s', 67 });
    dict.insert( {'t', 71 });
    dict.insert( {'u', 73 });
    dict.insert( {'v', 79 });
    dict.insert( {'w',83 });
    dict.insert( {'x', 89 });
    dict.insert( {'y', 97 });
    dict.insert( {'z', 101 });

    vector<string> words ;
    string line;

    while( getline(input, line) ){
    	words.push_back(line);
    }

    unordered_map< int, vector<string> > anagrams;
    anagram_builder(anagrams, words, dict);
    formatter(anagrams);
    return 0;
}

	
