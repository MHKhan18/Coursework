/*******************************************************************************
 * Name          : quickselect.cpp
 * Author        : Mohammad Khan
 * Pledge        : I pledge my honor that I have abided by the Stevens Honor System.
 * Date          : 10/17/2019
 * Description   : Implements the quickselect algorithm found on page 160 in
 *                 Algorithms, 3e by Anany Levitin.
 ******************************************************************************/
#include <iostream>
#include <sstream>
#include <algorithm>
#include <vector>

using namespace std;

string plurality( int n){
	if ( n == 1){
		return "value." ;
	}
	return "values." ;
}



size_t lomuto_partition(int array[], size_t left, size_t right) {

	//Partitions subarray by Lomuto’s algorithm using first element as pivot
	//Input: A subarray A[l..r] of array A[0..n − 1], defined by its left and right
	// indices l and r (l ≤ r)
	//Output: Partition of A[l..r] and the new position of the pivot

	
    
    int pivot = array[left]; // first element is pivot
    size_t smaller = left; // keeps track of part of array smaller than pivot

    for ( size_t i= left+1; i <= right; ++i ){
    	if ( array[i] < pivot ){
    		smaller++; 
    		swap(array[i],array[smaller]);
    	}
    }
    swap(array[smaller],array[left]);


    return smaller;
}

int quick_select(int array[], size_t left, size_t right, size_t k) {
	
	//Input: Subarray A[l..r] of array A[0..n − 1] of orderable elements and
	// integer k (1≤ k ≤ r − l + 1)
	//Output: The value of the kth smallest element in A[l..r]

	while ( left <= right ){
		size_t s = lomuto_partition(array,left,right);

		if ( s == k-1 ){
			return array[s];
		}

		else if ( s > k-1 ){
			right = s-1;
		}

		else{
			left = s+1;
		}

	}

	return -1;
	

}

int quick_select(int array[], const size_t length, size_t k) {
	//cout<< array[0] << endl;
    return quick_select(array, 0, length - 1, k);
}

int main(int argc, char *argv[]) {
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <k>" << endl;
        return 1;
    }

    int k;
    istringstream iss;
    iss.str(argv[1]);
    if ( !(iss >> k) || k <= 0 ) {
        cerr << "Error: Invalid value '" << argv[1] << "' for k." << endl;
        return 1;
    }

    cout << "Enter sequence of integers, each followed by a space: " << flush;
    int value, index = 0;
    vector<int> values;
    string str;
    str.reserve(11);
    char c;
    iss.clear();
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

    int num_values = values.size();
    if (num_values == 0) {
        cerr << "Error: Sequence of integers not received." << endl;
        return 1;
    }
   
    if ( k > num_values){
    	cerr << "Error: Cannot find smallest element " << k << " with only " << num_values << " " << plurality(num_values) << endl;
    	return 1; 
    }
    cout << "Smallest element " << k << ": " << quick_select(&values[0], num_values, k);
    
    return 0;
}
