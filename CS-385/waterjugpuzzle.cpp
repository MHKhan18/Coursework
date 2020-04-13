/*******************************************************************************
 * Name        : waterjugpuzzle.cpp
 * Author      : Mohammad Khan 
 * Date        : 10/15/19
 * Description : Solves the famous water jug puzzle using breadth-first search.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/



#include <iostream>
#include <sstream>
#include <vector>
#include <queue>
#include <string>
#include <array>


using namespace std;


// initilaize regular_queue with the initial state (0,0,capC)
// function valid_moves to put all adjacent states in the regular_queue unless they are already in visited_queue and regular_queue 
// ==^^ visited
// compare method to check if two states are equal 
// deque the first state in the visited_queue
// loop until regular_queue is empty



// Struct to represent state of water in the jugs.

struct State {
    int a, b, c;
    vector<string> directions;
    
    
    State(int _a, int _b, int _c) : a(_a), b(_b), c(_c) { }
    
    // String representation of state in tuple form.
    string to_string() {
        ostringstream oss;
        oss << "(" << a << ", " << b << ", " << c << ")";
        return oss.str();
    }

    //comparison method

    bool operator==( const State &rhs){
    	if ( a == rhs.a && b == rhs.b && c == rhs.c){
    		return true;
    	}

    	return false; 
    }

};

string plurality( int n){
	if ( n == 1){
		return "gallon" ;
	}
	return "gallons" ;
}

void possible_move( State curr, int capA, int capB, int capC, bool** matrix, queue < State > &regular_queue ){
	/// Creates all possible new states from the given state and puts them in the regular_queue
	int a = curr.a;
	int b = curr.b;
	int c = curr.c;

	int newA;
	int newB;
	int newC;


    // if matrix[newA][newB] == 0
	// declare new state( newA, newB, newC)
	// set the string 
	// put it in the queue and the list
	// mark the place in the array with the counter+
	
	// C -> A
	if ( a < capA  && c>0){

		 // find new state
		 newA = a;
		 newC = c;
		while ( newA < capA && newC > 0){
			newA++;
			newC--;
		}
		newB = capC - newC - newA ; 
		int diff = newA - a; 
		
		// update the queue
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State CtoA( newA, newB, newC); 
			//cout << CtoA->to_string() << endl;
			CtoA.directions = curr.directions;
			CtoA.directions.push_back("Pour " + to_string(diff) + " " + plurality(diff) + " from C to A. " + CtoA.to_string());
			//cout<< CtoA -> directions[0];
			matrix[newA][newB] = true;
			regular_queue.push(CtoA);
			
		}
	}

	// B-> A
	if ( a < capA  && b>0){

		 // find new state
		 newB = b;
		 newA = a;
		while ( newA < capA && newB > 0){
			newA++;
			newB--;
		}

		newC = capC - newA - newB ; 
		int diff = newA - a;
		
		
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State BtoA( newA, newB, newC); 
			BtoA.directions = curr.directions;
			BtoA.directions.push_back("Pour " + to_string(diff) + " " + plurality(diff) +  " from B to A. " + BtoA.to_string());
			//cout << BtoA->to_string() << endl;
			matrix[newA][newB] = true ;
			regular_queue.push(BtoA);
			
		}
	}

	// C->B

	if ( b < capB  && c>0){

		 // find new state
		 newB = b;
		 newC = c;
		while ( newB < capB && newC > 0){
			newB++;
			newC--;
		}

		newA = capC - newB - newC ; 
		int diff = newB - b; 
		
		

		// update the matrix 
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State CtoB( newA, newB, newC); 
			CtoB.directions = curr.directions; // copy the hisotry
			//cout << CtoB->to_string() << endl;
			CtoB.directions.push_back("Pour " + to_string(diff) +  " " + plurality(diff) + " from C to B. " + CtoB.to_string()); // add new string
			matrix[newA][newB] = true ;
			regular_queue.push(CtoB);
			

		}
	}


	// A->B
	if ( b < capB  && a>0){

		 // find new state
		 newB = b;
		 newA = a;
		while ( newB < capB && newA > 0){
			newB++;
			newA--;
		}

		newC = capC - newA - newB ; 
		int diff = newB - b;
		
		// update the matrix 
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State AtoB( newA, newB, newC); 
			//cout << AtoB->to_string()<<endl;
			AtoB.directions = curr.directions ;
			AtoB.directions.push_back("Pour " + to_string(diff) + " " + plurality(diff) + " from A to B. " + AtoB.to_string());
			matrix[newA][newB] = true ;
			regular_queue.push(AtoB);
			

		}
	}

	//B->C
	if ( c < capC  && b>0){

		 // find new state
		 newB = b;
		 newC = c;
		while ( newC < capC && newB > 0){
			newB--;
			newC++;
		}

		newA = capC - newC - newB ; 
		int diff = newC - c;
		
		
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State BtoC( newA, newB, newC); 
			;
			BtoC.directions = curr.directions;
			BtoC.directions.push_back("Pour " + to_string(diff)+ " " + plurality(diff) + " from B to C. " + BtoC.to_string());
			matrix[newA][newB] = true ;
			regular_queue.push(BtoC);
			

		}
	}

	//A->C
	if ( c < capC  && a>0){

		 // find new state
		 newA = a;
		 newC = c;
		while ( newC < capC && newA > 0){
			newA--;
			newC++;
		}

		newB = capC - newA - newC ; 
		int diff = newC - c;
		
		
		if ( matrix[newA][newB] == false ){ // if the state is not already visited;
			State AtoC( newA, newB, newC); 
			AtoC.directions = curr.directions;
			AtoC.directions.push_back("Pour " + to_string(diff) + " " + plurality(diff) + " from A to C. " + AtoC.to_string());
			matrix[newA][newB] = true ;
			regular_queue.push(AtoC);
			

		}
	}

	
}







bool solution_checker(int capA, int capB, int capC, int goalA, int goalB, int goalC ){

	queue < State > regular_queue; 
	State initial_state(0,0,capC);
	State goal_state(goalA,goalB,goalC);

	regular_queue.push(initial_state);


	bool **matrix = new bool*[capA+1];
	for (int i = 0; i < capA + 1; ++i) {
	    matrix[i] = new bool[capB+1];
	    // Fill the array with zeros.
	    fill(matrix[i], matrix[i] + capB+1, false);
	}


	while ( !regular_queue.empty()){
		State curr = regular_queue.front();
		if ( curr == goal_state){
			cout<< "Initial state. " << initial_state.to_string() << endl;
			for ( int i=0; i< static_cast<int>(curr.directions.size()); ++i){
				cout<< curr.directions[i] << endl ;
			}
			
			for (int i = 0; i < capA+1; ++i) {
	    			delete [] matrix[i];
	    			//matrix[i] = NULL;
	    		} 
			// Delete the array itself.
			delete [] matrix;
			
			return true;
		}
		// else explore 
		possible_move(curr, capA, capB, capC,matrix,regular_queue) ;
		
		
		
		regular_queue.pop();
		 
	}

	
	// Delete each array, that is, all the columns, first.
	for (int i = 0; i < capA+1; ++i) {
	    delete [] matrix[i];
	    
	}
	// Delete the array itself.
	delete [] matrix;
	


	return false;

}




int main(int argc, char * const argv[]) {

	int capA, capB, capC, goalA, goalB, goalC ;
	istringstream iss;

	if (argc != 7) {
        cerr << "Usage: " << argv[0] << " <cap A> <cap B> <cap C> <goal A> <goal B> <goal C>"
             << endl;
        return 1;
    }

    // invlid input format
    iss.str(argv[1]);
    if ( !(iss >> capA) || capA<=0 ) {
        cerr << "Error: Invalid capacity " << "\'" << argv[1] << "\'" <<
        		" for jug A."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code
   
    iss.str(argv[2]);
    if ( !(iss >> capB) || capB<=0 ) {
        cerr << "Error: Invalid capacity " << "\'" << argv[2] << "\'" <<
        		" for jug B."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code
    
    iss.str(argv[3]);
    if ( !(iss >> capC) || capC<=0 ) {
        cerr << "Error: Invalid capacity " << "\'" << argv[3] << "\'" <<
        		" for jug C."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code
   
    iss.str(argv[4]);
    if ( !(iss >> goalA) || goalA<0 ) {
        cerr << "Error: Invalid goal " << "\'" << argv[4] << "\'" <<
        		" for jug A."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code
    
    
    iss.str(argv[5]);
    if ( !(iss >> goalB) || goalB<0 ) {
        cerr << "Error: Invalid goal " << "\'" << argv[5] << "\'" <<
        		" for jug B."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code
    
   	
	iss.str(argv[6]);
    if ( !(iss >> goalC) || goalC<0 ) {
        cerr << "Error: Invalid goal " << "\'" << argv[6] << "\'" <<
        		" for jug C."
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code

    // bad input 
    if ( goalA > capA){
    	cerr << "Error: Goal cannot exceed capacity of jug A." << endl ;
    	return 1;
    }
    iss.clear(); // clear the error code

    if ( goalB > capB){
    	cerr << "Error: Goal cannot exceed capacity of jug B." << endl ;
    	return 1;
    }
    iss.clear(); // clear the error code

    if ( goalC > capC){
    	cerr << "Error: Goal cannot exceed capacity of jug C." << endl ;
    	return 1;
    }
    iss.clear(); // clear the error code

    if ( goalA+goalB+goalC > capA + capB + capC){
    	cerr << "Usage: " << argv[0] << " <cap A> <cap B> <cap C> <goal A> <goal B> <goal C>"
             << endl;
        return 1;
    }
    iss.clear(); // clear the error code

    if ( goalA+goalB+goalC != capC){
    	cerr << "Error: Total gallons in goal state must be equal to the capacity of jug C." << endl ;
    	return 1;
    }


    
    iss.clear(); // clear the error code
    //--------------------------------------------------------------------------------------------------------------------------------

   
    if ( ! solution_checker(capA, capB, capC, goalA, goalB, goalC) ){
    	cout << "No solution." << endl;
    }
    
    return 0;
    
}