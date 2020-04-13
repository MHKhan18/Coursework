/*******************************************************************************
 * Name        : sieve.cpp
 * Author      : Mohammad Khan
 * Date        : September 10, 2019
 * Description : Sieve of Eratosthenes
 * Pledge      : "I pledge my honor that I have abided by the Stevens Honor System"
 ******************************************************************************/
#include <cmath>
#include <iomanip>
#include <iostream>
#include <sstream>

using namespace std;

class PrimesSieve {
public:
    PrimesSieve(int limit);     //constructor

    ~PrimesSieve() {            //destructor
        delete [] is_prime_;
    }

    int num_primes() const {   // can't modify object
        return num_primes_;
    }

    void display_primes() const;

private:
    // Instance variables
    bool * const is_prime_;   // can't change pointer  // array 
    const int limit_;
    int num_primes_, max_prime_;

    // Method declarations
    int count_num_primes() const;
    void sieve();
    static int num_digits(int num);
};

PrimesSieve::PrimesSieve(int limit) :
        is_prime_{new bool[limit + 1]}, limit_{limit} {
    sieve();
}

void PrimesSieve::display_primes() const {
    // display the primes in the format specified in the
    // requirements document.
    const int max_prime_width = num_digits(max_prime_),
               primes_per_row = 80 / (max_prime_width + 1);
    
    // format for one liner
    if ( num_primes_ <= primes_per_row){
        cout << 2 ;
        for ( int i=3; i <= limit_; i++){
            if ( is_prime_[i] == true){
                cout << " " << i ;
            }
        }
    }

    // fromat for multi-liner
    else{
        // setw right aligns the numbers to fit assigned width
        int trace = 0;
        for ( int j=0; j<= limit_; ++j){
            if ( is_prime_[j] == true ){

                if ( trace != 0) { cout << " " ;}
                cout << setw(max_prime_width) << j ;
                trace++;
            }

            if ( trace == primes_per_row){
                cout << endl ;
                trace = 0;
            }
        }
    }
}

int PrimesSieve::count_num_primes() const {
    // count the number of primes found
    int count = 0;
    for ( int i=0; i <= limit_; ++i){
        if ( is_prime_[i] == true){
            count++;
        }
    }

    return count;
}

void PrimesSieve::sieve() {
    // sieve algorithm
    // have to modify the is_prime_ array 


    is_prime_[0] = false;
    is_prime_[1] = false;
    // initialize the array to true
    for ( int i=2; i <= limit_; ++i ){
        is_prime_[i] = true;
    }

    // algo from txt 
    for ( int i=2; i <= sqrt(limit_); ++i){
        if ( is_prime_[i] == true){
            int j = i*i ;
            while ( j <= limit_ ){
                is_prime_[j] = false ;
                j += i;
            }
        }
    }

    //Now all i such that is_prime[i] is true are prime

    // initializing other data members
    num_primes_ = count_num_primes();

    for ( int i = limit_; i>=0; i-- ){
        if ( is_prime_[i] == true){
            max_prime_ = i;
            break ;
        }
    }
}

int PrimesSieve::num_digits(int num) {
    // determine how many digits are in an integer
    int count = 0;
    while ( num != 0){
        num /= 10;
        count++;
    }
    return count;
}

int main() {
    cout << "**************************** " <<  "Sieve of Eratosthenes" <<
            " ****************************" << endl;
    cout << "Search for primes up to: ";
    string limit_str;
    cin >> limit_str;
    int limit;

    // Use stringstream for conversion. 

    // Check for error.
    if ( !(iss >> limit) ) {
        cerr << "Error: Input is not an integer." << endl;
        return 1;
    }
    if (limit < 2) {
        cerr << "Error: Input must be an integer >= 2." << endl;
        return 1;
    }


    PrimesSieve obj1(limit) ;
    cout << endl;
    cout << "Number of primes found: " << obj1.num_primes() << endl;
    cout << "Primes up to " << limit << ":" << endl;
    obj1.display_primes();

    return 0;
}
