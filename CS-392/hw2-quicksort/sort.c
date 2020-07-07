/*******************************************************************************
 * Name        : sort.c
 * Author      : Mohammad Khan
 * Date        : 6/2/20
 * Description : Uses quicksort to sort a file of either ints, doubles, or
 *               strings.
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <errno.h>
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "quicksort.h"

#define MAX_STRLEN     64 // Not including '\0'
#define MAX_ELEMENTS 1024

typedef enum {
    STRING,
    INT,
    DOUBLE
} elem_t;

/**
 * Basic structure of sort.c:
 *
 * Parses args with getopt.
 * Opens input file for reading.
 * Allocates space in a char** for at least MAX_ELEMENTS strings to be stored,
 * where MAX_ELEMENTS is 1024.
 * Reads in the file
 * - For each line, allocates space in each index of the char** to store the
 *   line.
 * Closes the file, after reading in all the lines.
 * Calls quicksort based on type (int, double, string) supplied on the command
 * line.
 * Frees all data.
 * Ensures there are no memory leaks with valgrind. 
 */

void print_usage(){
    printf("Usage: ./sort [-i|-d] filename\n");
    printf("   -i: Specifies the file contains ints.\n");
    printf("   -d: Specifies the file contains doubles.\n");
    printf("   filename: The file to sort.\n");
    printf("   No flags defaults to sorting strings.\n");
    exit(EXIT_FAILURE);
}



/**
 * Reads data from filename into an already allocated 2D array of chars.
 * Exits the entire program if the file cannot be opened.
 */
size_t read_data(char *filename, char **data) {
    // Open the file.
    FILE *fp = fopen(filename, "r");
    if (fp == NULL) {
        fprintf(stderr, "Error: Cannot open '%s'. %s.\n", filename,
                strerror(errno));
        free(data);
        exit(EXIT_FAILURE);
    }

    // Read in the data.
    size_t index = 0;
    char str[MAX_STRLEN + 2];
    char *eoln;
    while (fgets(str, MAX_STRLEN + 2, fp) != NULL) {
        eoln = strchr(str, '\n');
        if (eoln == NULL) {
            str[MAX_STRLEN] = '\0';
        } else {
            *eoln = '\0';
        }
        // Ignore blank lines.
        if (strlen(str) != 0) {
            data[index] = (char *)malloc((MAX_STRLEN + 1) * sizeof(char));
            strcpy(data[index++], str);
        }
    }

    // Close the file before returning from the function.
    fclose(fp);

    return index;
}


void display_array( int * array, const int length) {
    putchar('[');
    if (length > 0) {
        printf("%d", *array);
    }
    for (int i = 1; i < length; i++) {
        printf(", %d", *(array + i));
    }
    puts("]");
}
void output_array( int * array, const int length) {
    for (int i = 0; i < length; i++) {
        printf( "%d\n", *(array + i));
    }
}



void display_array_string( char **array, const int length) {
    putchar('[');
    if (length > 0) {
        printf("%s", *array);
    }
    for (int i = 1; i < length; i++) {
        printf(", %s", *(array + i));
    }
    puts("]");
}
void output_array_string( char **array, const int length) {
    
    for (int i = 0; i < length; i++) {
        printf("%s\n", *(array + i));
    }
    
}


void display_array_double( double *array, const int length) {
    putchar('[');
    if (length > 0) {
        printf("%lf", *array);
    }
    for (int i = 1; i < length; i++) {
        printf(", %lf", *(array + i));
    }
    puts("]");
}
void output_array_double( double *array, const int length) {
    for (int i = 0; i < length; i++) {
        printf("%lf\n", *(array + i));
    }
}
 

int main(int argc, char **argv) {

    if (argc < 2){
        print_usage();
    }

    elem_t data_type;
    char *filename;

    /** parses args */
    int option;
    int set_int = 0;
    int set_double = 0;
    opterr = 0; // disables error message from getopt
    
    
    while ( (option = getopt(argc,argv,"id") ) != -1 ){
        switch (option){
            
            case 'i':
                set_double++;
                data_type = INT;
                //printf("Option found: %d\n",data_type);
                break;
            
            case 'd':
                set_int++;
                data_type = DOUBLE;
                //printf("Option found: %d\n",data_type);
                break;

            case '?':
                printf( "Error: Unknown option \'-%c\' received.\n" , optopt );
                print_usage();
        }
    }
    
    
    if ( set_int>0 && set_double>0 ){
        printf( "Error: Too many flags specified.\n");
        exit(EXIT_FAILURE);
    }

    if ( (set_int==0 && set_double==0 && argc>=3) || argc > 3  ){
        printf("Error: Too many files specified.\n");
        exit(EXIT_FAILURE);
    }

    if ( set_int==0 && set_double==0 ){
        data_type = STRING;
    }

    filename = argv[argc-1];
    char *check = strchr(filename, '.');
    if (check == NULL ){
        printf("Error: No input file specified.\n");
        exit(EXIT_FAILURE);
    }

    // printf("%s\n",filename);

    /** =================================================================================================== */
    char **data = (char **)calloc(MAX_ELEMENTS,(MAX_STRLEN + 1) * sizeof(char));
    size_t len = read_data(filename, data);

   
    if ( data_type == STRING ){
        
        //display_array_string(data,len);
        // call quicksort
        quicksort( (void *)data, len, sizeof(char *),str_cmp);
        output_array_string(data,len);
        for ( int i=0; i<len; ++i ){
            free(data[i]);
        }
        free(data);
    }
    else if ( data_type == INT){
        int *input = (int*) calloc(len, sizeof(int));
        for ( int i=0; i<len; ++i ){
            int val = atoi(*(data+i));
            free(data[i]);
            *(input+i) = val;
        }
        free(data);
        //display_array(input,len); 
        // call quicksort
        quicksort( (void *)input, len, sizeof(int),int_cmp);
        output_array(input,len);
        free(input);
        
    }

    else if ( data_type == DOUBLE){
        double *input = (double*) calloc(len, sizeof(double));
        for ( int i=0; i<len; ++i ){
            char *string = *(data+i);
            char *ptr;
            double val = strtod(string, &ptr);
            free(data[i]);
            *(input+i) = val;
        }
        free(data);
        //display_array_double(input,len);
        // call quicksort
        quicksort( (void *)input, len, sizeof(double),dbl_cmp);
        output_array_double(input,len);
        free(input);
    }

    return EXIT_SUCCESS;
}