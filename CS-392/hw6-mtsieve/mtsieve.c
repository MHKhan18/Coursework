/*******************************************************************************
 * Name        : mtsieve.c
 * Author      : Mohammad Khan
 * Date        : 6/28/20
 * Description : implements segmented sieve using multi-threading
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/

#include <errno.h>
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <math.h>
#include <pthread.h>
#include <ctype.h>
#include <sys/sysinfo.h>

#define USAGE "Usage: ./mtsieve -s <starting value> -e <ending value> -t <num threads>\n"
#define BUFSIZE 16384

typedef struct arg_struct {
    int start;
    int end;
} thread_args;

int total_count = 0;
pthread_mutex_t lock;


/**
 * Determines whether or not the input string represents a valid integer.
 */
bool is_integer(char *input) {
    int start = 0, len = strlen(input);

    if (len >= 1 && input[0] == '-') {
        if (len < 2) {
            return false;
        }
        start = 1;
    }
    for (int i = start; i < len; i++) {
        if (!isdigit(input[i])) {
            return false;
        }
    }
    return true;
}

/**
 * Takes as input a string and an in-out parameter value.
 * If the string can be parsed, the integer value is assigned to the value
 * parameter and true is returned.
 * Otherwise, false is returned and the best attempt to modify the value
 * parameter is made.
 */
bool get_integer(char *input, int *value) {
    long long long_long_i;
    if (sscanf(input, "%lld", &long_long_i) != 1) {
        return false;
    }
    *value = (int)long_long_i;
    if (long_long_i != (long long)*value) {
        return false;
    }
    return true;
}

/**
 * Tests if a number is prime or not 
 */
bool is_prime(int n) {
    for (int i = 2; i <= n / 2; ++i)
        if (n % i == 0) return false;

    return true;
}

/**
 * Returns an array of prime numbers up to a certain limit 
 */
int* sieve(int limit) {
    int *primes;

    if (( primes = (int *)malloc(limit * sizeof(int))) == NULL ){
        fprintf(stderr, "Error: malloc() failed. Cannot allocate memory.\n");
        exit(EXIT_FAILURE);
    }
    memset(primes, 0, limit); 
    
    int p_ind = 0;
    for (int i = 2; i <= limit; i++)
        if (is_prime(i)) primes[p_ind++] = i;

    return primes;
}

/**
 * Tests whether a given prime is valid or not
 * (contains two or more 3's)
 */ 
bool is_valid(int prime) {
    int count = 0;
    while (prime > 0) {
        if (prime % 10 == 3) count++;
        prime /= 10;
    }
    
    if (count >= 2) 
        return true;
    else 
        return false;
}

/**
 * Finds the number of valid primes between 2 certain values 
 */
void *seg_sieve(void *ptr) {
    thread_args *targs = (thread_args*) ptr;
    int low = targs->start;
    int high = targs->end;

    int *low_primes = sieve((int)sqrt(high));
    int hp_len = high - low + 1;
    bool *high_primes ;
    if ( (high_primes = (bool*) malloc(hp_len * sizeof(bool))) == NULL ){
        fprintf(stderr, "Error: malloc() failed. Cannot allocate memory.\n");
        exit(EXIT_FAILURE);
    }
    memset(high_primes, true, hp_len); 

    for (int i = 0; low_primes[i] != 0; i++) {
        int ind = ceil((double)low / low_primes[i]) * low_primes[i] - low;
        
        if (low <= low_primes[i]) {
            ind += low_primes[i];
        }

        for (int j = ind; j < hp_len; j += low_primes[i]) {
            high_primes[j] = false;
        }
    }

    int temp = 0;
    for (int i = 0; i < hp_len; i++) {
        if (high_primes[i] && is_valid(i+low)) 
            temp++;
    }
    free(high_primes);
    free(low_primes);

    int retval;
    if ((retval = pthread_mutex_lock(&lock)) != 0) {
        printf("Warning: Cannot lock mutex. %s.\n", strerror(retval));
    }
    total_count += temp;
    if ((retval = pthread_mutex_unlock(&lock)) != 0) {
        printf("Warning: Cannot unlock mutex. %s.\n", strerror(retval));
    }

    pthread_exit(NULL);
}

int main(int argc, char **argv) {
    if (argc < 2){
        printf(USAGE);
        return EXIT_FAILURE ;
    }
    
    int start;
    int end;
    int num_threads = -1;
    bool set_s = false;
    bool set_e = false;
    bool set_t = false;
    int opt;
    opterr = 0; // disables error message from getopt

    // parse the arguments   
    while ((opt = getopt(argc,argv,":s:e:t:")) != -1) {
        switch (opt) {
            case 's':
                set_s = true;
                if (is_integer(optarg)) {
                    int val;
                    if (get_integer(optarg, &val)) {
                       start = val;
                    }
                    else {
                        fprintf(stderr, "Error: Integer overflow for parameter '-s'.\n");
                        return EXIT_FAILURE;
                    }
                }
                else {
                    fprintf(stderr, "Error: Invalid input '%s' received for parameter '-s'.\n" , optarg);
                    return EXIT_FAILURE;
                }
                break;
            case 'e':
                set_e = true;
                if (is_integer(optarg)) {
                    int val;
                    if (get_integer(optarg, &val)) {
                       end = val;
                    }
                    else {
                        fprintf(stderr, "Error: Integer overflow for parameter '-e'.\n");
                        return EXIT_FAILURE;
                    }
                }
                else {
                    fprintf(stderr, "Error: Invalid input '%s' received for parameter '-e'.\n" , optarg);
                    return EXIT_FAILURE;
                }
                break;
            case 't':
                set_t = true;
                if (is_integer(optarg)) {
                    int val;
                    if (get_integer(optarg, &val)) {
                       num_threads = val;
                    }
                    else {
                        fprintf(stderr, "Error: Integer overflow for parameter '-t'.\n");
                        return EXIT_FAILURE;
                    }
                }
                else {
                    fprintf(stderr, "Error: Invalid input '%s' received for parameter '-t'.\n" , optarg);
                    return EXIT_FAILURE;
                }
                break;
            case ':':
                if (!set_s) {
                    fprintf(stderr, "Error: Option -s requires an argument.\n");
                    return EXIT_FAILURE;
                }
                else if (!set_e) {
                    fprintf(stderr, "Error: Option -e requires an argument.\n");
                    return EXIT_FAILURE;
                }
                else if (!set_t) {
                    fprintf(stderr, "Error: Option -t requires an argument.\n");
                    return EXIT_FAILURE;
                }
            case '?':
                if (optopt == 'e' || optopt == 's' || optopt == 't') {
                    fprintf(stderr, "Error: Option -%c requires an argument.\n", optopt);
                
                } else if (isprint(optopt)) {
                    fprintf(stderr, "Error: Unknown option '-%c'.\n", optopt);
                
                } else {
                    fprintf(stderr, "Error: Unknown option character '\\x%x'.\n",optopt);
                }
                return EXIT_FAILURE;
        }
    }

    // check for errors
    if (!set_s) {
        fprintf(stderr, "Error: Required argument <starting value> is missing.\n");
        return EXIT_FAILURE;
    }
    if (!set_e) {
        fprintf(stderr, "Error: Required argument <ending value> is missing.\n");
        return EXIT_FAILURE;
    }
    if (!set_t) {
        fprintf(stderr, "Error: Required argument <num threads> is missing.\n");
        return EXIT_FAILURE;
    }
    if (optind < argc) {
        fprintf(stderr, "Error: Non-option argument '%s' supplied.\n", argv[optind]);
        return EXIT_FAILURE;
    }
    if (start < 2) {
        fprintf(stderr, "Error: Starting value must be >= 2.\n");
        return EXIT_FAILURE;
    }
    if (end < 2) {
        fprintf(stderr, "Error: Ending value must be >= 2.\n");
        return EXIT_FAILURE;
    }
    if (end < start) {
        fprintf(stderr, "Error: Ending value must be >= starting value.\n");
        return EXIT_FAILURE;
    }
    if (num_threads < 1) {
        fprintf(stderr, "Error: Number of threads cannot be less than 1.\n");
        return EXIT_FAILURE;
    }
    if (num_threads > 2*get_nprocs_conf()) {
        fprintf(stderr, "Error: Number of threads cannot exceed twice the number of processors(%d).\n", get_nprocs_conf());
        return EXIT_FAILURE;
    }
    
    // make segments
    int num_nums = end-start+1;
    if (num_threads > num_nums) num_threads = num_nums;

    int seg_length = num_nums/num_threads - 1;
    int rem = num_nums % num_threads;
    int **segments ;
     ;
    if (  (segments =  (int**) malloc(num_threads * 2 * sizeof(int *))) == NULL ){
        fprintf(stderr, "Error: malloc() failed. Cannot allocate memory.\n");
        return EXIT_FAILURE;
    }
    

    for (int i = 0; i < num_threads; ++i ){
        *(segments+i) = (int*) malloc(2 * sizeof(int)) ;
        if (  (*(segments+i)) == NULL){
            fprintf(stderr, "Error: malloc() failed. Cannot allocate memory.\n");
            return EXIT_FAILURE;
        }
    } 

    

    segments[0][0] = start;
    for (int i = 0; i < num_threads-1; i++) {
        segments[i][1] = segments[i][0] + seg_length;
        if (rem-- > 0) segments[i][1]++;
        
        segments[i+1][0] = segments[i][1]+1;
    }
    segments[num_threads-1][1] = end;

    // print segments
    char s = '\0';
    if (num_threads > 1) s = 's';
    printf("Finding all prime numbers between %d and %d.\n", start, end);
    printf("%d segment%c:\n", num_threads, s);
    for (int i = 0; i < num_threads; i++) {
        printf("   [%d, %d]\n", segments[i][0], segments[i][1]);
    }

    // multithread
    pthread_t *threads;
    if (   (threads = (pthread_t *) malloc(num_threads * sizeof(pthread_t)) ) == NULL) {
        fprintf(stderr, "Error: malloc() failed. Cannot allocate memory.\n");
        for (int i = 0; i < num_threads; i++) free(segments[i]);
        free(segments);
        return EXIT_FAILURE;
    }


    thread_args targs[num_threads];
    
    
    int retval;

    if ((retval = pthread_mutex_init(&lock, NULL)) != 0) {
        fprintf(stderr, "Error: Cannot initalize mutex. %s.\n", strerror(retval));
        for (int i = 0; i < num_threads; i++) free(segments[i]);
        free(segments);
        free(threads);
        return EXIT_FAILURE;
    }

    for (int i = 0; i < num_threads; i++) {
        targs[i].start = segments[i][0];
        targs[i].end = segments[i][1];

        if ((retval = pthread_create(&threads[i], NULL, seg_sieve, &targs[i])) != 0) {
            fprintf(stderr, "Error: Cannot create thread %d. %s.\n", i+1, strerror(retval));
            for (int i = 0; i < num_threads; i++) free(segments[i]);
            free(segments);
            free(threads);
            return EXIT_FAILURE;
        }
    }

    for (int i = 0; i < num_threads; i++) {
        if ((retval = pthread_join(threads[i], NULL)) != 0) {
            printf("Warning: Thread %d did not join properly. %s.\n", i+1, strerror(retval));
        }
    }

    if ((retval = pthread_mutex_destroy(&lock)) != 0) {
        printf("Warning: Cannot destroy mutex. %s.\n", strerror(retval));
    }

    // print result
    printf("Total primes between %d and %d with two or more '3' digits: %d\n", start, end, total_count);

    // free data
    free(threads);
    for (int i = 0; i < num_threads; i++) free(segments[i]);
    free(segments);

    
    return EXIT_SUCCESS;
}