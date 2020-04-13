/*******************************************************************************
 * Name        : stairclimber.cpp
 * Author      : Mohammad Khan
 * Date        : 9/26/2019
 * Description : Lists all ways to climb n stairs.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/


#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream>
#include <iomanip>
#include <string>

using namespace std;

// dynamic programming solution

vector< vector<int> > get_ways(int num_stairs) {

	// begin with result for the basic cases
	vector < vector < vector <int> > > container {  {{}} ,{ {1} }, 
													{ {1,1} , {2} }, 
													{ {1,1,1}, {1,2}, {2,1}, {3} } };

	// build solution for each integer upto the required # of stairs												
	for ( int i=4; i<= num_stairs ; ++i){
		vector< vector< int > > ways;
		for ( int j=1; j<=3; ++j){
			vector< vector< int > > result = container.at(i-j) ;
			int bound = static_cast<int>(result.size());
			for ( int k=0; k< bound ; ++k ){
				result.at(k).insert(result.at(k).begin(),j);
			}
			ways.insert(ways.end(),result.begin(),result.end() );
		}
		container.push_back(ways);

	}

	vector < vector <int> > result = container.at(num_stairs);
	return result;
}


// helper function for diaplaying purposes
int num_digits(int num) {
    // determine how many digits are in an integer
    int count = 0;
    while ( num != 0){
        num /= 10;
        count++;
    }
    return count;

}

void display_ways(const vector< vector<int> > &ways) {
    // Display the ways to climb stairs by iterating over
    // the vector of vectors and printing each combination.

    for ( size_t i=0; i< ways.size(); ++i){
    	vector<int> permutation = ways.at(i);
    	int count = static_cast<int>(ways.size());
    	
    	int space_size = num_digits(count) - num_digits(i+1) ;
    	for ( int i=0; i<space_size; ++i){
    		cout << " ";
    	}

    	cout << i+1 << ". ";
    	cout << "[" ;
    	for ( size_t j=0; j<permutation.size(); ++j){
    		if ( j == permutation.size()-1 ){
    			cout << permutation.at(j) ;
    		}
    		else{
    			cout << permutation.at(j) << ", " ;
     		}
    	}

    	cout << "]" << endl;
    }

}




int main(int argc, char * const argv[]) {

	int m;
    istringstream iss;

    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <number of stairs>" << endl;
        return 1;
    }

    iss.str(argv[1]);
    if ( !(iss >> m) || ( m<= 0) ) {
        cerr << "Error: Number of stairs must be a positive integer." << endl;
        return 1;
    }



    else{

    	int n = stoi(argv[1]);
    	//cout << n ;
    	//display_ways(get_ways());
    	vector< vector <int> > ways = get_ways(n);
    	//size_t count = ways.size();
    	int count = static_cast<int>(ways.size());
    	//int space_size = num_digits(count) - num_digits(n);

    	cout << count  ;
    	if ( n==1){
    		cout << " way ";
    	}
    	else{
    		cout << " ways ";
    	}

    	cout << "to climb " << n ;
    	if ( n==1){
    		cout << " stair.";
    	}
    	else{
    		cout << " stairs.";
    	}
    	 cout << endl ;
    	display_ways(get_ways(n));
    	return 0;
    }
}
