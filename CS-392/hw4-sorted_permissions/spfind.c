/*******************************************************************************
 * Name        : spfind.c
 * Author      : Mohammad Khan
 * Date        : 6/16/20
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/
#include <ctype.h>
#include <errno.h>
#include <stdbool.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <wait.h>

int main(int argc, char **argv) {
    
    int pfind_to_sort[2], sort_to_parent[2];
    pid_t pid[2];

    if (pipe(pfind_to_sort) < 0) {
        fprintf(stderr, "Error: Failed to create pipe. %s\n", strerror(errno));
        return EXIT_FAILURE;
    }
    if (pipe(sort_to_parent) < 0) {
        fprintf(stderr, "Error: Failed to create pipe. %s\n", strerror(errno));
        return EXIT_FAILURE;
    }


    pid[0] = fork() ;
    if ( pid[0] < 0) {
        fprintf(stderr, "Error: Failed to fork. %s\n", strerror(errno));
        return EXIT_FAILURE;
    }
    if ( pid[0] == 0) {
        // pfind
        if (dup2(pfind_to_sort[1], STDOUT_FILENO) < 0) {
            fprintf(stderr, "Error: dup2(pfind_to_sort[0]) failed.\n");         
            return EXIT_FAILURE; 
        }


        
        if (close(pfind_to_sort[0]) < 0) {
            fprintf(stderr, "Error: close(pfind_to_sort[0]) failed.\n");         
            return EXIT_FAILURE; 
        }


        
        if (close(sort_to_parent[0]) < 0) {
            fprintf(stderr, "Error: close(sort_to_parent[0]) failed.\n");         
            return EXIT_FAILURE; 
        }

       
        if (close(sort_to_parent[1]) < 0) {
            fprintf(stderr, "Error: close(sort_to_parent[1]) failed.\n");         
            return EXIT_FAILURE; 
        }

        if (execvp("./pfind", argv) == -1) {            
            fprintf(stderr, "Error: pfind failed.\n");         
            return EXIT_FAILURE;        
        }
    } 
    
    pid[1] = fork() ;
    if ( pid[1] < 0) {
        fprintf(stderr, "Error: Failed to fork. %s\n", strerror(errno));
        return EXIT_FAILURE;
    }

    if ( pid[1] == 0 ) {
        // sort
        if (dup2(pfind_to_sort[0], STDIN_FILENO) < 0) {
            fprintf(stderr, "Error: dup2(pfind_to_sort[0]) failed.\n");         
            return EXIT_FAILURE; 
        }

        

        if (close(pfind_to_sort[1]) < 0) {
            fprintf(stderr, "Error: close(pfind_to_sort[1]) failed.\n");         
            return EXIT_FAILURE; 
        }

        
        if (close(sort_to_parent[0]) < 0) {
            fprintf(stderr, "Error: close(sort_to_parent[0]) failed.\n");         
            return EXIT_FAILURE; 
        }

        
        if (dup2(sort_to_parent[1], STDOUT_FILENO) < 0) {
            fprintf(stderr, "Error: dup2(pfind_to_sort[0]) failed.\n");         
            return EXIT_FAILURE; 
        }
        
        if (execlp("sort", "sort", NULL) == -1) {             
            fprintf(stderr, "Error: sort failed.\n");            
            return EXIT_FAILURE;        
        }
        
    } 

   
    if (dup2(sort_to_parent[0], STDIN_FILENO) < 0) {
        fprintf(stderr, "Error: dup2(pfind_to_sort[0]) failed.\n");         
        return EXIT_FAILURE; 
    }

    
    if (close(sort_to_parent[1]) < 0) {
        fprintf(stderr, "Error: close(sort_to_parent[1]) failed.\n");         
        return EXIT_FAILURE; 
    }

    
    if (close(pfind_to_sort[0]) < 0) {
        fprintf(stderr, "Error: close(pfind_to_sort[0]) failed.\n");         
        return EXIT_FAILURE; 
    }

    if (close(pfind_to_sort[1]) < 0) {
        fprintf(stderr, "Error: close(pfind_to_sort[1]) failed.\n");         
        return EXIT_FAILURE; 
    }


    int matches = 0;
    char buffer[4096];
    bool print_matches = true;
    while (1) {
        ssize_t count = read(STDIN_FILENO, buffer, sizeof(buffer));
        if (count == -1) {
            if (errno == EINTR) {
                continue;
            } 
            else {
                fprintf(stderr, "Error: read() failed. %s\n", strerror(errno));            
                return EXIT_FAILURE; 
            }
        } 
        else if (count == 0) {
            break;
        } 
        else {
            for (int i = 0; i < count; i++) {
                if (buffer[i] == '\n') {
                    matches++;
                }
            }

            if (write(STDOUT_FILENO, buffer, count) < 0) {
                fprintf(stderr, "Error: write() failed. %s\n", strerror(errno));            
                return EXIT_FAILURE; 
            }
        }
    }

    if ((strstr(buffer, "Usage: ") == NULL) && (strlen(buffer) != 0) && print_matches) {
            printf("Total matches: %d\n", matches);
    }


    int status;

    wait(&status);
    if (!(WIFEXITED(status) && WEXITSTATUS(status) == EXIT_SUCCESS)){
        return EXIT_FAILURE;
    }
    wait(&status);

    return !(WIFEXITED(status) && WEXITSTATUS(status) == EXIT_SUCCESS);

}
    





