/*******************************************************************************
 * Name        : unique.cpp
 * Author      : Mohammad Khan
 * Date        : 9/20/2019
 * Description : Determining uniqueness of chars with int as bit vector.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <iostream>
#include <cctype>

using namespace std;

bool is_all_lowercase(const string &s) {
	/* for bad input check */
    // returns true if all characters in string are lowercase
    // letters in the English alphabet; false otherwise.
    for ( size_t i=0; i<s.size(); ++i){
    	if ( s[i] < 'a' || s[i]>'z' ){
    		return false;
    	}
    }
    return true;
}



bool all_unique_letters(const string &s) {

	/* O(1) of way of checking for unique letters */

    // returns true if all letters in string are unique, that is
    // no duplicates are found; false otherwise.
    

    unsigned int vector = 0;  // represents  z - a in last 26 bits
    unsigned int new_vector;

    // takes at  most 26 pass
    for (size_t i=0; i<s.size(); ++i){
    	int difference = s[i] - 'a' ;
    	unsigned int char_setter = 1 << difference;   // shifting the 1 to the left 
    	new_vector = vector ^ char_setter ;  // value increses if seen first time, decreases second time
    	if ( new_vector < vector){
    		return false;                  // must execute if the input string is more than 26 letters long
    	}
    	vector = new_vector; 
    }

    return true;
}

int main(int argc, char * const argv[]) {
    // reads and parses command line arguments.
    // Calls other functions to produce correct output.

     if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <string>" << endl;
        return 1;
    }

    if ( !(is_all_lowercase(argv[1])) ){
    	cerr << "Error: " << "String must contain only lowercase letters." << endl;
    	return 1;
    }

    if (all_unique_letters(argv[1])){
    	cout << "All letters are unique." << endl ;
    	return 0;
    }

    else{
    	cout << "Duplicate letters found." << endl;
    	return 0;
    }

}
