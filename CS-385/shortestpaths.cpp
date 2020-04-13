
/*******************************************************************************
 * Name        : shortestpaths.cpp
 * Author      : Mohammad Khan 
 * Date        : 11/29/2019
 * Description : Solves the all pair shortest path problem using Floyd's algorithm.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/


#include <bits/stdc++.h>
using namespace std;

/** 
* implements Floyd's algorithm and keeps track of the vertices used to build the path for backtracing the path
*/
void Floyd(long **path_lengths, long **intermediate_vertex, int num_vert){

	for ( int k=0; k<num_vert; ++k){
		for ( int i=0; i<num_vert; ++i){
			for ( int j=0; j<num_vert; ++j){
				long temp = -1;
				if ( path_lengths[i][k] != -1 && path_lengths[k][j] != -1 ){
					temp = path_lengths[i][k] + path_lengths[k][j];
				}
				if ( temp != -1 && ( path_lengths[i][j]==-1 || temp < path_lengths[i][j] ) ){  
					path_lengths[i][j] = temp;
					intermediate_vertex[i][j] = k;
				}
			}
		}
	}
}


/** 
* returns all intermediate vertices from->to(inclusive if not same as from)
*/
vector<long> path_generator( long from, long to, long **intermediate_vertex){
	vector<long>res;

	long int_vert = intermediate_vertex[from][to] ;


	if ( from == to ){
		return res;
	}
	else if (  int_vert == -1){
		res.push_back(to);
		return res;
	}
	else{
		vector<long>left = path_generator(from,int_vert,intermediate_vertex);
		vector<long>right = path_generator(int_vert,to,intermediate_vertex);
		// concatenating two vectors
		res.reserve( left.size()+right.size() );
		res.insert( res.end(), left.begin(), left.end() );
		res.insert( res.end(), right.begin(), right.end());
		return res;
	}
}


/** 
* displays the shortest path and distance between all vertices in the specified format
*/
void display_paths( int num_vert, long **intermediate_vertex, long **path_lengths ){

	for ( long from=0; from < num_vert; ++from ){
		for ( long to=0; to < num_vert; ++to ){
			vector<long> path = path_generator(from, to, intermediate_vertex );

			ostringstream oss ;
			for ( int i=0; i< static_cast<int>(path.size()); ++i ){
				oss << " -> " <<  char(path.at(i)+'A');
			}
			cout << char(from+'A') << " -> " << char(to+'A') << ", distance: "  ;

			if ( path_lengths[from][to] == -1 ){
				cout << "infinity" << ", path: " << "none" << endl;
			}
			else{
			    cout << path_lengths[from][to] << ", path: " << char(from+'A') << oss.str() << endl ;
			}

		}
	}
}


/**
*  returns the number of characters in the value
*/
int len(long val){
	int count = 0;
	while ( val > 0){
		val /= 10;
		count++ ;
	}
	return count;
}


/**
* Displays the matrix on the screen formatted as a table.
*/
void display_table(long** const matrix, int num_vert, const string &label, const bool use_letters = false) {
	cout << label << endl;
	long max_val = 0;
	for (int i = 0; i < num_vert; i++) {
		for (int j = 0; j < num_vert; j++) {
			long cell = matrix[i][j];
			if (cell > -1 && cell > max_val) {
				max_val = matrix[i][j];
			}
		}
	}
	int max_cell_width = use_letters ? len(max_val) : len(max(static_cast<long>(num_vert), max_val));
	cout << ' ';
	if ( use_letters && num_vert==1)  { cout << ' '; } 
	for (int j = 0; j < num_vert; j++) {
		cout << setw(max_cell_width + 1) << static_cast<char>(j + 'A');
	}
	cout << endl;
	for (int i = 0; i < num_vert; i++) {
		cout << static_cast<char>(i + 'A');
		for (int j = 0; j < num_vert; j++) {
			cout << " " << setw(max_cell_width);
			if (matrix[i][j] == -1) {
				cout << "-";
			} else if (use_letters) {
				cout << static_cast<char>(matrix[i][j] + 'A');
			} else {
				cout << matrix[i][j];
			}
		}
		cout << endl;
	}
	cout << endl;
}



int main(int argc, const char *argv[]) {
    
    // Make sure the right number of command line arguments exist.
    if (argc != 2) {
        cerr << "Usage: " << argv[0] << " <filename>" << endl;
        return 1;
    }
    // Create an ifstream object.
    ifstream input_file(argv[1]);
    
    // If the file does not exist, print an error message.
    if (!input_file) {
        cerr << "Error: Cannot open file '" << argv[1] << "'." << endl;
        return 1;
    }
    // Add read errors to the list of exceptions the ifstream will handle.
    input_file.exceptions(ifstream::badbit);
   
    string line;
    int num_vert ;
    long **matrix ;
    try {
        unsigned int line_number = 1;
        
        // Use getline to read in a line.
        while (getline(input_file, line)) { 
        	
        	istringstream iss(line);

        	if ( line_number == 1 ){
        		
        		if ( !(iss >> num_vert) ) {
        			cerr << "Error: Invalid number of vertices '" << line << "' on line 1." << endl;
        			return 1;
        		}
        		iss.clear(); 
        		if ( num_vert<1 || num_vert>26){
        			cerr << "Error: Invalid number of vertices '" << num_vert << "' on line 1." << endl;
        			return 1;
        		}

        		// initialize the matrix once num_vert is decided 
        		matrix = new long*[num_vert];
        		for (int i = 0; i < num_vert; ++i) {
	    			matrix[i] = new long[num_vert];
	    			// fill in the array with -1s
	    			// INFINITY = -1
	    			fill(matrix[i], matrix[i] + num_vert, -1);
	    		}

	    		// 0s for diagonals
	    		for ( int i=0; i<num_vert; ++i){
	    			for ( int j=0; j<num_vert; ++j){
	    				if ( i == j ){ matrix[i][j] = 0 ;}
	    			}
	    		}
	    	}

        	//cout << line_number << endl;
        	else{
        		vector<string>line_contents ;
	        	
	        	// splitting line by whitespace
	        	for ( string line; iss >> line; ){
	        		line_contents.push_back(line);
	        	}

	        	if ( line_contents.size() != 3){
	        		//cout << "hyp" << endl;
	        		cerr << "Error: Invalid edge data '" << line << "' on line " << line_number << "." << endl;
	        		return 1;
	        	}

	        	if ( line_contents.at(0).size() > 1 ){
	        		cerr << "Error: Starting vertex '" << line_contents.at(0) << "' on line " << line_number << " is not among valid values " << char(65) << "-" << char(num_vert+64) << "." << endl;
	        		return 1;
	        	}

	        	if ( line_contents.at(1).size() > 1 ){
	        		cerr << "Error: Ending vertex '" << line_contents.at(1) << "' on line " << line_number << " is not among valid values " << char(65) << "-" << char(num_vert+64) << "." << endl;
	        		return 1;
	        	}

	        	char c1[1] , c2[1];
	        	c1[0] = line_contents[0][0]; // form vertex
	        	c2[0] = line_contents[1][0]; // to vertex

	        	if ( c1[0] > 64+num_vert || c1[0] < 65 ){
	        		cerr << "Error: Starting vertex '" << line_contents.at(0) << "' on line " << line_number << " is not among valid values " << char(65) << "-" << char(num_vert+64) << "." << endl;
	        		return 1;
	        	}
	        	if ( c2[0] > 64+num_vert || c2[0] < 65 ){
	        		cerr << "Error: Ending vertex '" << line_contents.at(1) << "' on line " << line_number << " is not among valid values " << char(65) << "-" << char(num_vert+64) << "." << endl;
	        		return 1;
	        	}

	        	int edge_weight;

	        	istringstream is2(line_contents.at(2));

	        	//cout << "there" << endl ;
	        	if ( !(is2 >> edge_weight ) ) {
	        		cerr << "Error: Invalid edge weight '" << line_contents.at(2) << "' on line " << line_number << "." << endl ;
	    			return 1;
	    		}
	    		is2.clear();

	    		if ( edge_weight <= 0 ){
	    			cerr << "Error: Invalid edge weight '" << edge_weight << "' on line " << line_number << "." << endl ;
	    			return 1;
	    		}
 				
 				// input validation done

	    		// put values inside the matrix
	    		matrix[c1[0]-65][c2[0]-65] = edge_weight ;
	    	}
	    	line_number+=1 ;
        } // end of file and file data is processed
		// close the file.
        input_file.close();
    } 
    catch (const ifstream::failure &f) {
        cerr << "Error: An I/O error occurred reading '" << argv[1] << "'.";
        return 1;
    }

    display_table(matrix, num_vert, "Distance matrix:");

    //initializing the path-lengths matrix
    long **path_lengths = new long*[num_vert];
	for (int i = 0; i < num_vert ; ++i) {
	    path_lengths[i] = new long[num_vert];
	    // Fill the array with -1s.
	    fill(path_lengths[i], path_lengths[i] + num_vert, -1);
	} 
	// making a deepcopy of matrix 
	for ( int i=0; i<num_vert; ++i){
		for ( int j=0; j<num_vert; ++j){
			path_lengths[i][j] = matrix[i][j];
		}
	}

	//initializing the intermediate_vertex matrix
    long **intermediate_vertex = new long*[num_vert];
	for (int i = 0; i < num_vert ; ++i) {
	    intermediate_vertex[i] = new long[num_vert];
	    // Fill the array with -1s.
	    fill(intermediate_vertex[i], intermediate_vertex[i] + num_vert, -1);
	} 

	Floyd(path_lengths, intermediate_vertex, num_vert);
	display_table( path_lengths, num_vert, "Path lengths:" );
	display_table( intermediate_vertex, num_vert, "Intermediate vertices:", true);
	display_paths(num_vert, intermediate_vertex, path_lengths);


	// clearing memory

	// delete intermdeiate vertex
	for (int i = 0; i < num_vert; ++i) {
	    delete [] intermediate_vertex[i];
	}
	delete [] intermediate_vertex;


	// delete path lengths
	for (int i = 0; i < num_vert; ++i) {
	    delete []  path_lengths[i];
	}
	delete [] path_lengths;


	// delete matrix
	for (int i = 0; i < num_vert; ++i) {
	    delete []  matrix[i];
	}
	delete [] matrix;

	return 0;
}