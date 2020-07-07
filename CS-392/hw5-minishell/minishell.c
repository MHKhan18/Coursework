/*******************************************************************************
 * Name        : minishell.c
 * Author      : Mohammad Khan
 * Date        : 6/23/20
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
#include <limits.h>
#include <signal.h>
#include <setjmp.h>
#include <pwd.h>

#define BRIGHTBLUE "\x1b[34;1m"
#define DEFAULT "\x1b[0m"
#define BUFSIZE 16384

volatile sig_atomic_t sig_val = 0;
sigjmp_buf jmpbuf;

void catch_signal(int sig) {
    printf("\n");
    if (sig_val == 0) {
        siglongjmp(jmpbuf, 1);
    }
}

void print_cwd() {
    char cwd[PATH_MAX];
    
    if (getcwd(cwd, sizeof(cwd)) != NULL) {
        printf("[%s%s%s]$ ", BRIGHTBLUE, cwd, DEFAULT);
    }
    else {
        fprintf(stderr, "Error: Cannot get current working directory. %s\n", strerror(errno));            
        exit(EXIT_FAILURE); 
    }

    fflush(stdout);
}

int get_args_2(char *input, char **args) {
    int num_args = 0;
    char *token = strtok(input, " ");

    while (token != NULL) {
        args[num_args++] = token;
        token = strtok(NULL, " ");
    }

    args[num_args] = NULL;

    return num_args;
}

int get_args(char *input, char **args) {
    int num_args = 0;
    int j = 0;
    bool ignore_spaces = false;
    int quote_count = 0;
    
    char temp[4096];
    for(int i = 0; i < strlen(input)-1; i++) {    
        if (input[i] == '"') {
            quote_count++;
            i++;
            ignore_spaces = true;

            if (input[i] == '"') {
                i++;
            }
        }

        if (!ignore_spaces && input[i] != ' ') {
            temp[j++] = input[i];

            if (input[i+1] == ' ' || input[i+1] == '\n') {
                temp[j] = '\0';
 
                args[num_args] = (char*) malloc(4096 * sizeof(char));
                strcpy(args[num_args++], temp);
                
                j = 0;
                memset(temp, 0, sizeof(temp));
            }
        }
        else if (ignore_spaces && i < strlen(input)-2) {
            temp[j++] = input[i];

            if (input[i+1] == '"' && input[i+2] == '\n') {
                temp[j] = '\0';
 
                args[num_args] = (char*) malloc(4096 * sizeof(char));
                strcpy(args[num_args++], temp);
                
                j = 0;
                ignore_spaces = false;
                memset(temp, 0, sizeof(temp));
            }
        }
    }   

    if (quote_count % 2 != 0) return -1;

    args[num_args] = NULL;
    return num_args;
}

void cd(char **args, int num_args) {
    if (num_args > 2) {
        fprintf(stderr, "Error: Too many arguments to cd.\n");    
        return;        
    }
    
    uid_t uid = getuid();
    if (uid < 0) {
        fprintf(stderr,"Error: Cannot get uid. %s.\n", strerror(errno));
    }
    struct passwd *pass;
    if ((pass = getpwuid(uid)) == NULL) {
        fprintf(stderr,"Error: Cannot get passwd entry. %s.\n", strerror(errno));
    }

    if (num_args == 1 || (args[1][0] == '~' && strlen(args[1]) <= 2)) {
        if (chdir(pass->pw_dir) < 0) {
            fprintf(stderr,"Error: Cannot change directory to '%s'. %s.\n", args[1], strerror(errno));
        }
    }
    else if (num_args > 1 && strlen(args[1]) > 2 && args[1][0] == '~') {
        if (chdir(pass->pw_dir) < 0) {
            fprintf(stderr,"Error: Cannot change directory to '%s'. %s.\n", args[1], strerror(errno));
        }

        char *new_path = strchr(args[1], '~') + 2;

        if (chdir(new_path) < 0) {
            fprintf(stderr, "Error: Cannot change directory to '%s'. %s.\n", args[1], strerror(errno));    
        } 
    }
    else if (chdir(args[1]) < 0) {
        fprintf(stderr, "Error: Cannot change directory to '%s'. %s.\n", args[1], strerror(errno));            
    }
}

void perform_command(char **args) {
    sig_val = 1;

    pid_t pid;
    if ((pid = fork()) < 0) {
        fprintf(stderr, "Error: fork() failed. %s.\n", strerror(errno));   
    } 
    else if (pid > 0) {
        if (wait(NULL) < 0 && errno != EINTR) {
            fprintf(stderr, "Error: wait() failed. %s.\n", strerror(errno));
        }
    }
    else if (pid == 0) {
        if (execvp(args[0], args) < 0) {            
            fprintf(stderr, "Error: exec() failed. %s.\n", strerror(errno)); 
            exit(1);
        }
    }
}

int main(int argc, char **argv) {
    // set up the signal handler
    struct sigaction action;
    if (memset(&action, 0, sizeof(struct sigaction)) == NULL) {
        fprintf(stderr,"Error: Cannot register signal handler. %s.\n", strerror(errno));
        return EXIT_FAILURE;
    }
    action.sa_handler = catch_signal;
    if (sigaction(SIGINT, &action, NULL) < 0) {
        fprintf(stderr,"Error: Cannot do sigaction. %s.\n", strerror(errno));
        return EXIT_FAILURE;
    }
    sigsetjmp(jmpbuf, 1);

    // start the shell
    char input[BUFSIZE];
    char *args[BUFSIZE];
    int num_args;
    while (true) {
        // print cwd
        print_cwd();

        // get user input
        int bytes_read;
        if ((bytes_read = read(STDIN_FILENO, input, BUFSIZE)) < 0 && errno != EINTR) {
            fprintf(stderr,"Error: read() failed. %s.\n", strerror(errno));
            break;
        }
        else if (bytes_read > 1) {
            input[bytes_read] = '\0';
        }
        else if (bytes_read == 1) {
            continue;
        }

        // parse arguments
        if ((num_args = get_args(input, args)) < 0) {
            fprintf(stderr, "Error: Malformed command.\n");
            continue;
        }
       
        // decide what to do with the input
        if (num_args > 0) {
            if (strcmp(args[0], "exit") == 0) {
                break;
            }
            else if (strcmp(args[0], "cd") == 0) {
                cd(args, num_args);
            }
            else {
                perform_command(args);
            }
            
            sig_val = 0;
        }
        
        // free memory
        memset(input, 0, sizeof(input));
        for (int i = 0; i < num_args; i++) free(args[i]);
    }

    // last free
    for (int i = 0; i < num_args; i++) free(args[i]);

    return EXIT_SUCCESS;
}