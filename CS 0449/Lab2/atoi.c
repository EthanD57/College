int my_atoi( const char *str ) {
    int sum = 0;
    int index = 0;
    int isNegative = 0;

    if(str[0] == '-' || str[0] == '+') {
        if(str[0] == '-') {
        isNegative = 1;
        }
        index++;
    }

    while(str[index] != '\0') {
        sum = sum * 10 + str[index] - '0';
        index++;
    }
    
    if(isNegative) {
      return -sum;
    } else {
      return sum;
    }
}