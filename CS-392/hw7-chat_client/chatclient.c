/*******************************************************************************
 * Name        : chatclient.c
 * Author      : Mohammad Khan
 * Date        : 7/6/20
 * Description : communicates with a chat server using TCP connections
 ******************************************************************************/

#include <arpa/inet.h>
#include <errno.h>
#include <fcntl.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/socket.h>
#include <unistd.h>
#include "util.h"

int client_socket = -1;
char username[MAX_NAME_LEN + 1];
char inbuf[BUFLEN + 1];
char outbuf[MAX_MSG_LEN + 1];


int handle_stdin() {
    
    int status = get_string(outbuf, MAX_MSG_LEN + 1 );
    
    if ( status == TOO_LONG ){
        printf("Sorry, limit your message to %d characters.\n", MAX_MSG_LEN );
    }
    else{
         if (send(client_socket, outbuf, strlen(outbuf), 0) < 0) {
            fprintf(stderr, "Error: Failed to send message to server. %s.\n", strerror(errno));
            return EXIT_FAILURE;
        }  
    }
    
    if ( strcmp(outbuf, "bye")==0 ){
        printf("Goodbye.\n");
        return EXIT_FAILURE;
    }

    return EXIT_SUCCESS;
}


int handle_client_socket() {

    int bytes_recvd;
    if ((bytes_recvd = recv(client_socket, inbuf, BUFLEN, 0)) < 0) {  
            printf("Warning: Failed to receive incoming message. %s.\n", strerror(errno));
        }
    else if (bytes_recvd == 0) {
        fprintf(stderr, "\nConnection to the server has been lost.\n");
        return  EXIT_FAILURE ;
    }
    else {
        inbuf[bytes_recvd] = '\0';
        if (strcmp(inbuf, "bye") == 0) {
            printf("\nServer initiated shutdown.\n");
            return  EXIT_FAILURE ;
        }
        else {
            printf("\n%s\n", inbuf);
            return  EXIT_SUCCESS ;
        }
    }

    return EXIT_SUCCESS;
}


int main(int argc, char *argv[]) {

   if (argc != 3) {
        fprintf(stderr, "Usage: %s <server IP> <port>\n" , argv[0]);
        return EXIT_FAILURE;
    }

    int bytes_recvd, ip_conversion, retval = EXIT_SUCCESS;
    struct sockaddr_in server_addr;
    socklen_t addrlen = sizeof(struct sockaddr_in);

    // get ip address
    ip_conversion = inet_pton(AF_INET, argv[1] , &server_addr.sin_addr);
    if (ip_conversion == 0) {
        fprintf(stderr, "Error: Invalid IP address '%s'.\n", argv[1]);
        return EXIT_FAILURE;
    } 
    else if (ip_conversion < 0) {
        fprintf(stderr, "Error: Failed to convert IP address. %s.\n", strerror(errno));
        return EXIT_FAILURE;
    }

    
    // get port number
    int port ;
    if (!parse_int( argv[2], &port , "port number") ){
        return EXIT_FAILURE;
    }
    else if ( port < 1024 || port > 65535 ){
        fprintf(stderr, "Error: Port must be in range [1024, 65535].\n") ;
        return EXIT_FAILURE;
    }

    server_addr.sin_family = AF_INET;   // Internet address family
    server_addr.sin_port = htons(port); // Server port, 16 bits.

    // get username from stdin
    int  status = -1;
    do {
        printf("Enter your username: ");
        fflush(stdout);
        
        status = get_string(username, MAX_NAME_LEN+1 );

        if ( status == TOO_LONG ){
            printf("Sorry, limit your username to %d characters.\n",MAX_NAME_LEN);
        }
        if ( status > 0 ){
            continue ;
        }

        printf("Hello, %s. Let's try to connect to the server.\n", username);
    } while ( status != 0);


    // Create a reliable, stream socket using TCP.
    if ((client_socket = socket(AF_INET, SOCK_STREAM, 0)) < 0) {
        fprintf(stderr, "Error: Failed to create socket. %s.\n", strerror(errno));
        return EXIT_FAILURE;
    }

    // connect to server
    if (connect(client_socket, (struct sockaddr *)&server_addr, addrlen) < 0) {
        fprintf(stderr, "Error: Failed to connect to server. %s.\n", strerror(errno));
        retval = EXIT_FAILURE;
        goto EXIT;
    }


    // receive and print wlecome message from server
    if ((bytes_recvd = recv(client_socket, inbuf , BUFLEN , 0)) < 0) {
        fprintf(stderr, "Error: Failed to receive message from server. %s.\n",
                strerror(errno));
        retval = EXIT_FAILURE;
        goto EXIT;
    }
    else if (bytes_recvd == 0) {
        fprintf(stderr, "All connections are busy. Try again later.\n");
        retval = EXIT_FAILURE;
        goto EXIT;
    }
    else {
        inbuf[bytes_recvd] = '\0';
        printf("\n%s\n\n", inbuf);
    }

    // send username to server
    if (send(client_socket, username, strlen(username), 0) < 0) {        
        fprintf(stderr, "Error: Failed to send username to server. %s.\n", strerror(errno));        
        retval = EXIT_FAILURE;        
        goto EXIT;    
    }

    fd_set sockset;
    int max_socket;
    while (true) {
        
        printf("[%s]: ", username);
        fflush(stdout);

        // select is destructive
        FD_ZERO(&sockset);
        FD_SET(STDIN_FILENO, &sockset);
        FD_SET(client_socket, &sockset);

        max_socket = STDIN_FILENO;
        if (client_socket > max_socket){ max_socket = client_socket; }

        // Wait for activity on one of the sockets.
        // Timeout is NULL, so wait indefinitely.
        if (select(max_socket + 1, &sockset, NULL, NULL, NULL) < 0 && errno != EINTR) {
            fprintf(stderr, "Error: select() failed. %s.\n", strerror(errno));
            retval = EXIT_FAILURE;
            goto EXIT;
        }

        // If there is user activity, send it to the server
        if ( FD_ISSET(STDIN_FILENO , &sockset) ) {
            if ( handle_stdin() == EXIT_FAILURE) {
                retval = EXIT_FAILURE;
                goto EXIT;
            }
        }

        // If there is response from server, receive and print the message
        if ( FD_ISSET( client_socket, &sockset) ) {
            if ( handle_client_socket() == EXIT_FAILURE) {
                retval = EXIT_FAILURE;
                goto EXIT;
            }
        }
    }

    EXIT:
        if (fcntl(client_socket, F_GETFD) >= 0) {
            close(client_socket);
        }
        return retval;


}