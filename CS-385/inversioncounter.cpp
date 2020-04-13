/*******************************************************************************
 * Name        : inversioncounter.cpp
 * Author      : Mohammad Khan
 * Version     : 1.0
 * Date        : 10/23/2019
 * Description : Counts the number of inversions in an array.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <iostream>
#include <algorithm>
#include <sstream>
#include <vector>
#include <cstdio>
#include <cctype>
#include <cstring>

using namespace std;

static long merge(int array[], int scratch[], int lo, int hi){

	long count = 0;
	int mid = lo + (hi-lo)/2; // either side of mid is sorted
	
	int L = lo;
	int H = mid+1;

	for ( int k=lo; k<=hi; ++k){
		if ( L<= mid && (H>hi || array[L] <= array[H]) ){ // H>hi means the second array is processed
			scratch[k] = array[L];
			L++;
		}
		else{
			scratch[k] = array[H];
			H++;
			if ( L <= mid){
				count += (mid+1-L);
			}
			
		}
	}

	for ( int k=lo; k<=hi; ++k){
		array[k] = scratch[k];
	}

	return count;

	
	

}

// Function prototype.
static long mergesort(int array[], int scratch[], int low, int high){

	long count = 0;

	if ( low < high){
		int mid = low + (high-low)/2;
		count+=mergesort(array,scratch,low,mid);
		count+= mergesort(array,scratch,mid+1,high);
		count+=merge(array,scratch,low,high);
	}

	return count; 

}


// inversion: when a larger number precedes a smaller number in the array
/**
 * Counts the number of inversions in an array in theta(n^2) time.
 */
long count_inversions_slow(int array[], int length) {
    // TODO
    long count = 0;
    for(int i=0; i<length; ++i){
    	for ( int j=i+1; j < length; ++j){
    		if ( array[i] > array[j] )
    			count++;
    	}
    }
    return count;
}

/**
 * Counts the number of inversions in an array in theta(n lg n) time.
 */
long count_inversions_fast(int array[], int length) {
    // TODO
    // Hint: Use mergesort!
    int *scratch = new int[length-1];
	long result =  mergesort( array,scratch, 0, length-1);
	delete[] scratch ;

    return result;
}

/*static long mergesort(int array[], int scratch[], int low, int high) {   // needs to be static 
    // TODO
    return 0;
} */

int main(int argc, char *argv[]) {
    // TODO: parse command-line argument

    if ( argc > 2){
    	 cerr << "Usage: " << argv[0] << " [slow]"
             << endl;
        return 1;
    }


    if ( argc == 2){
    	if ( string(argv[1]) != "slow" ){
    	cerr << "Error: Unrecognized option " << "\'" << argv[1] << "\'" << "." << endl; 
    	return 1;
    }
    }
    

    cout << "Enter sequence of integers, each followed by a space: " << flush;

    istringstream iss;
    int value, index = 0;
    vector<int> values;
    string str;
    str.reserve(11);
    char c;
    while (true) {
        c = getchar();
        const bool eoln = c == '\r' || c == '\n';
        if (isspace(c) || eoln) {
            if (str.length() > 0) {
                iss.str(str);
                if (iss >> value) {
                    values.push_back(value);
                } else {
                    cerr << "Error: Non-integer value '" << str
                         << "' received at index " << index << "." << endl;
                    return 1;
                }
                iss.clear();
                ++index;
            }
            if (eoln) {
                break;
            }
            str.clear();
        } else {
            str += c;
        }
    }

    // TODO: produce output

    int length = values.size();

    if ( length == 0){
    	cerr << "Error: Sequence of integers not received." << endl;
    	return 1;
    }

    if (  argc == 2 && string(argv[1]) == "slow" ){
    	cout << "Number of inversions: " << count_inversions_slow(&values[0],length) << endl;
    }
    else{
    	cout << "Number of inversions: " <<  count_inversions_fast(&values[0],length) << endl ;
    }

    return 0;
}
