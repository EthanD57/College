#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <dlfcn.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <unistd.h>

void read_command(char *input, char *command, char parameters[][21]);

int main() {
    void *plugin;
    char userInput [230];
    char command[21];
    char plugin_name[27];
    char plugins[10][21];
    void *plugin_handles[10];
    int plugin_count = 0;


    for (;;) {
        char parameters[20][21] = {0};
        printf("> ");
        read_command(userInput, command, parameters);

        if (strcmp(command, "exit") == 0) {  // If the user enters exit, exit the program
            exit(0);
        } else if (strcmp(command, "load") == 0) { // If the user enters load, load the plugin
            
            char* temp = strdup(parameters[1]);
            sprintf(plugin_name, "./%s.so", parameters[1]);     //Syntax for plugin name 
            plugin = dlopen(plugin_name, RTLD_LAZY);            //Try to open the plugin

            if (!plugin) {
                printf("Error: Plugin %s initialization failed!", temp);
                printf("\n");
                continue;
            }


            for (int i = 0; i <= plugin_count; i++) { // Check if the plugin has already been loaded
                if (strcmp(temp, plugins[i]) == 0) {
                    printf("Error: Plugin %s initialization failed!", temp);
                    break;
                }
                else if (i == plugin_count) {    // If the plugin hasn't been loaded, we initialize it
                    int (*initialize)() = dlsym(plugin, "initialize");

                    if (initialize() != 0) {   // Check to see if the plugin initialized correctly
                    printf("Error: Plugin %s initialization failed!\n", temp);
                    }
                    strcpy(plugins[plugin_count], parameters[1]);  //Add the plugin to the list of plugins
                    plugin_handles[plugin_count] = plugin;      //Add the plugin handle to the list of plugin handles
                    plugin_count++;
                    break;
                }
            }


        } 
        else {   //If the command wasn't built in, check if it is a plugin
            int plugin_found = 0;
            for (int i = 0; i < plugin_count; i++) {
                if (strcmp(command, plugins[i]) == 0) {
                    plugin_found = 1;
                    void (*run)(const char **) = dlsym(plugin_handles[i], "run");
                    
                    char *arguments[21] = {NULL}; // Initialize arguments array with NULL
                    int counter = 0;
                    for (int j = 0; j < 20; j++) {
                        if (parameters[j][0] == '\0') {
                            break;
                        }
                        arguments[counter] = strdup(parameters[j]);
                        counter++;
                    }
                    
                    run((const char **)arguments);
                    break;
                }
            }
            if (!plugin_found) {
                pid_t pid = fork();
                if (pid == 0) { 
                    char* arguments[401] = {NULL}; // Initialize arguments array with NULL
                    int counter = 0;
                    for (int i = 0; i <= 20; i++) {
                            if (parameters[i][0] == '\0') {
                                break;
                            }
                            arguments[counter] = strdup(parameters[i]);
                            counter++;
                        }
                    execvp(command, arguments);
                    exit(42);
                } else {
                    waitpid(pid, NULL, 0);
                }

            }
            
            
        }
    }
}

void read_command(char *input, char *command, char parameters[][21]) {
    fgets(input, 230, stdin);

    if (input[0] == '\n') {    // If the user just presses enter, we return
        return;
    }

    for (int i = 0;;i++) {          // Iterate through the input to find the command
        if (input[i] == ' ' || input[i] == '\n') {
            command[i] = '\0';
            break;
        }
        command[i] = input[i];
    }

    // Iterate through the input to find the parameters and add them to the parameters array
    int inputIter = 0;      // Iterates through the input
    int pIter = 0;          // Iterates through the parameters
    int pChar = 0;          // Iterates through the characters of the current parameter
    for (;;) {
        if (input[inputIter] == '\n') {   // If we reach the end of the input, break the loop
            parameters[pIter][pChar] = NULL;
            break;
        }
        else if (input[inputIter] == ' ') {     // If we reach a space, we move to the next parameter
            parameters[pIter][pChar] = NULL;
            pIter++;
            pChar = 0;
            inputIter++;
        }
        else {                        // Otherwise, add the character to the current parameter
            parameters[pIter][pChar] = input[inputIter];
            pChar++;
            inputIter++;
        }
    }
}
