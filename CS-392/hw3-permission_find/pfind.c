/*******************************************************************************
 * Name        : pfind.c
 * Author      : Mohammad Khan
 * Date        : 6/8/20
 * Description : prints all file with specified permission string
 * Pledge      : I pledge my honor that I have abided by the Stevens Honor System.
 ******************************************************************************/

#include <stdbool.h>
#include <errno.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/stat.h>
#include <getopt.h>
#include <dirent.h>
#include <limits.h>

#define BUFSIZE 10



int perms[] = {S_IRUSR, S_IWUSR, S_IXUSR,
               S_IRGRP, S_IWGRP, S_IXGRP,
               S_IROTH, S_IWOTH, S_IXOTH};

char* permission_string(struct stat *statbuf) {

    char *permission_string = (char *)malloc((BUFSIZE) * sizeof(char));
    int index = 0;
    
    int permission_valid;
    
    
    for (int i = 0; i < 9; i += 3) {

        // read permit
        permission_valid = statbuf -> st_mode & perms[i];
        if (permission_valid) {
            *(permission_string+index) = 'r' ;
            index++;
        } else {
            *(permission_string+index) = '-' ;
            index++;
        }
        
        // write permit
        permission_valid = statbuf -> st_mode & perms[i+1];
        if (permission_valid) {
            *(permission_string+index) = 'w' ;
            index++;
        } else {
            *(permission_string+index) = '-' ;
            index++;
        }
        
        // execute permit
        permission_valid = statbuf -> st_mode & perms[i+2];
        if (permission_valid) {
            *(permission_string+index) = 'x' ;
            index++;
        } else {
           *(permission_string+index) = '-' ;
            index++;
        }
    }

    *(permission_string+index) = '\0' ;
    
    return permission_string ;
}



void print_files(char *full_filename, char *permissions, int pathlen){

    DIR *dir;
    if ((dir = opendir(full_filename)) == NULL) {  

        if (full_filename[strlen(full_filename)-1] == '/' ){
               full_filename[strlen(full_filename)-1] = '\0' ;
        }
        fprintf(stderr, "Error: Cannot open directory '%s'. %s.\n",
                full_filename, strerror(errno));
        return;
    }

    struct dirent *entry;
    struct stat sb;
   
    
    while ((entry = readdir(dir)) != NULL) {
        if (strcmp(entry->d_name, ".") == 0 || strcmp(entry->d_name, "..") == 0) {
            continue;
        }

        strncpy(full_filename + pathlen, entry->d_name, PATH_MAX - pathlen);

        if (lstat(full_filename, &sb) < 0) {
            fprintf(stderr, "Error: Cannot stat file '%s'. %s.\n",
                    full_filename, strerror(errno));
            continue;
        }

        
        char *file_permissions = permission_string(&sb);
        
        if (strcmp(file_permissions,permissions) == 0 ){
           printf("%s\n", full_filename);
        }

        if (S_ISDIR(sb.st_mode)) {
            int new_pathlen = strlen(full_filename) + 1;
            full_filename[new_pathlen - 1] = '/';
            full_filename[new_pathlen] = '\0';
            print_files(full_filename,permissions,new_pathlen);
        } 
        

        free(file_permissions);
        
    }

    closedir(dir);

}



/** ================ Validate Permission String =================== */
bool validate_permissions( char *pString ){
    
    if ( strlen(pString) != 9 ){ return false ;}

    int index = 0;

    for ( int j=0; j<3; ++j ){
        if ( *(pString+index) != 'r' && *(pString+index) != '-' ){
            return false;
        }
        else{ index++ ;}

        if ( *(pString+index) != 'w' && *(pString+index) != '-' ){
            return false;
        }
        else{ index++ ;}

        if ( *(pString+index) != 'x' && *(pString+index) != '-' ){
            return false;
        }
        else{ index++ ;}
    }

    return true;

}

void display_usage() {
    printf("Usage: ./pfind -d <directory> -p <permissions string> [-h]\n");
}

int main(int argc, char *argv[]) {

    if (argc < 2 ){
        display_usage();
        return EXIT_FAILURE;
    }

    char *directory;
    char *pString;

    /** ================================== parses args ================================ */
    int option;
    int set_d = 0;
    int set_p = 0;
    int set_h = 0;
    opterr = 0; // disables error message from getopt
    
    
    while ( ((option = getopt(argc,argv,":d:p:h") ) != -1)  && set_h==0 ){
        
        switch (option){
            case 'd':
                set_d++;
                directory = optarg;
                //printf("Directory found: %s\n",directory);
                break;
            
            case 'p':
                set_p++;
                pString = optarg;
                //printf("String found: %s\n", pString );
                break;

            case 'h':
                set_h++;
                break;

            case ':':
                if (set_d==0){
                    printf("Error: Required argument -d <directory> not found.\n");
                    return EXIT_FAILURE;
                }
                else if (set_p==0){
                    printf("Error: Required argument -p <permissions string> not found.\n");
                    return EXIT_FAILURE;
                }
            
            case '?':
                printf( "Error: Unknown option \'-%c\' received.\n" , optopt );
                return EXIT_FAILURE;
        }
    }

    if ( set_h > 0 ){
        display_usage();
        return EXIT_SUCCESS ;
    }

    if ( (set_d==0 && set_p==0) || ( (set_d>0 || set_p>0) && set_h>0) ){
        display_usage();
        if ( set_h==0 ) { return EXIT_FAILURE; }
        return EXIT_SUCCESS;
    }

    else if (set_d==0){
        printf("Error: Required argument -d <directory> not found.\n");
        return EXIT_FAILURE;
    }
    else if (set_p==0){
        printf("Error: Required argument -p <permissions string> not found.\n");
        return EXIT_FAILURE;
    }

    
    // valid file
    struct stat buf;
    if (stat(directory, &buf) < 0) {
        fprintf(stderr, "Error: Cannot stat '%s'. %s.\n", directory, strerror(errno));
        return EXIT_FAILURE;
    }

    char path[PATH_MAX];
    if (realpath(directory, path) == NULL) {
        fprintf(stderr, "Error: Cannot get full path of file '%s'. %s.\n",
                directory, strerror(errno));
        return EXIT_FAILURE;
    }

    char full_filename[PATH_MAX];
    size_t pathlen = 0;

    
    full_filename[0] = '\0';
    if (strcmp(path, "/")) {
        strncpy(full_filename, path, PATH_MAX);
    }
    

    pathlen = strlen(full_filename) + 1;
    full_filename[pathlen - 1] = '/';
    full_filename[pathlen] = '\0';
    
    if (S_ISDIR(buf.st_mode) || (S_ISLNK(buf.st_mode)) ){  // directory or symbolic link

        if ( ! validate_permissions( pString ) ){    // string validation
            fprintf(stderr, "Error: Permissions string '%s' is invalid.\n", pString );
            return EXIT_FAILURE;
        }

        print_files(full_filename,pString, pathlen);
        

    }
    else{
        fprintf(stderr, "Error: '%s' is not a directory.\n",directory );
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS ;
    
}