#include <stdio.h>

int main() 
{
    int num1, num2;
    char op;
    int keeprunning = 1;
    
    while (keeprunning){
        printf("Enter your calculation:\n");

        int scanned = scanf("%d %c %d", &num1, &op, &num2);
        
        if (scanned != 3) {
            scanf("%d%c%d", &num1, &op, &num2);
        }

        if(op == '+'){
            printf("> %d + %d = %d\n", num1, num2, num1 + num2);
        }
        else if(op == '-'){
            printf("> %d - %d = %d\n", num1, num2, num1 - num2);
        }
        else if(op == '*'){
            printf("> %d * %d = %d\n", num1, num2, num1 * num2);
        }
        else if(op == '/'){
            printf("> %d / %d = %d\n", num1, num2, num1 / num2);
        }   
        else if(op == '%'){
            printf("> %d %% %d = %d\n", num1, num2, num1 % num2);
        }
        else if(op == '&'){
            printf("> %d & %d = %d\n", num1, num2, num1 & num2);
        }
        else{
            printf("Invalid calculation! \"%d %c %d\"\n", num1, op, num2);
            keeprunning = 0;
        }
    }
}