/*******************************************************************************
 * Name        : quicksort.c
 * Author      : Mohammad Khan
 * Date        : 6/2/20
 * Description : Quicksort implementation.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <stdio.h>
#include <string.h>
#include "quicksort.h"

/* Static (private to this file) function prototypes. */
static void swap(void *a, void *b, size_t size);
static int lomuto(void *array, int left, int right, size_t elem_sz,
                  int (*comp) (const void*, const void*));
static void quicksort_helper(void *array, int left, int right, size_t elem_sz,
                             int (*comp) (const void*, const void*));

/**
 * Compares two integers passed in as void pointers and returns an integer
 * representing their ordering.
 * First casts the void pointers to int pointers.
 * Returns:
 * -- 0 if the integers are equal
 * -- a positive number if the first integer is greater
 * -- a negative if the second integer is greater
 */
int int_cmp(const void *a, const void *b) {
    int x =  *((int *)a);
    int y = *((int *)b);
    return x-y ;
}

/**
 * Compares two doubles passed in as void pointers and returns an integer
 * representing their ordering.
 * First casts the void pointers to double pointers.
 * Returns:
 * -- 0 if the doubles are equal
 * -- 1 if the first double is greater
 * -- -1 if the second double is greater
 */
int dbl_cmp(const void *a, const void *b) {
    double x =  *((double *)a);
    double y = *((double *)b);
    
    int ans=0;
    if ( x > y ){ ans = 1;}
    else if ( x < y ){ ans = -1;}

    return ans;
}

/**
 * Compares two char arrays passed in as void pointers and returns an integer
 * representing their ordering.
 * First casts the void pointers to char* pointers (i.e. char**).
 * Returns the result of calling strcmp on them.
 */
int str_cmp(const void *a, const void *b) { 
    char *x =  *(char **)a;
    char *y = *(char **)b;
    return strcmp(x,y);
}

/**
 * Swaps the values in two pointers.
 *
 * Casts the void pointers to character types and works with them as char
 * pointers for the remainder of the function.
 * Swaps one byte at a time, until all 'size' bytes have been swapped.
 * For example, if ints are passed in, size will be 4. Therefore, this function
 * swaps 4 bytes in a and b character pointers.
 */
 // https://stackoverflow.com/questions/29596151/swap-function-using-void-pointers 
  static void swap(void *a, void *b, size_t size) {

    char * p = a, * q = b, tmp;
    for (size_t i = 0; i < size; ++i){
        tmp = p[i];
        p[i] = q[i];
        q[i] = tmp;
    }
} 

/**
 * Partitions array around a pivot, utilizing the swap function.
 * Each time the function runs, the pivot is placed into the correct index of
 * the array in sorted order. All elements less than the pivot should
 * be to its left, and all elements greater than or equal to the pivot should be
 * to its right.
 * The function pointer is dereferenced when it is used.
 * Indexing into void *array does not work. All work must be performed with
 * pointer arithmetic.
 */
// implemented as in CLRS
static int lomuto(void *array, int left, int right, size_t elem_sz,
                  int (*comp) (const void*, const void*)) {
    
    char *base = (char *)array; 
    char *pivot = base+(right*elem_sz);
    int less = left-1;

    for ( int greater=left; greater<right; ++greater ){
        
        char *curr = base + (greater*elem_sz);
        
        
        if ( comp( (void *) curr, (void *)pivot ) <= 0  ){
            less++;
            char *to_swap = base+(less*elem_sz);
            swap( (void *)curr,(void *)to_swap, elem_sz);
        }   
    }

    char *last_swap = base + ((less+1)*elem_sz);
    swap( (void *)pivot,(void *)last_swap,elem_sz);
    return less+1;
}

/**
 * Sorts with lomuto partitioning, with recursive calls on each side of the
 * pivot.
 * This is the function that does the work, since it takes in both left and
 * right index values.
 */
static void quicksort_helper(void *array, int left, int right, size_t elem_sz,
                             int (*comp) (const void*, const void*)) {
    
    if ( left < right ){
        int partition = lomuto(array,left,right,elem_sz,comp);
        quicksort_helper(array,left,partition-1,elem_sz,comp);
        quicksort_helper(array,partition+1,right,elem_sz,comp);
    }
}

/**
 * Quicksort function exposed to the user.
 * Calls quicksort_helper with left = 0 and right = len - 1.
 */
void quicksort(void *array, size_t len, size_t elem_sz,
               int (*comp) (const void*, const void*)) {
    quicksort_helper(array,0,len-1,elem_sz,comp);
}
