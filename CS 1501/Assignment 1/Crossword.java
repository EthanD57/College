import java.util.HashMap;

public class Crossword implements WordPuzzleInterface  {
    private StringBuilder rowString = new StringBuilder();
    private StringBuilder colString = new StringBuilder();
    private Character[] order = {'e','a','r','i','o','t','n','s','l','c','u','d','p','m','h','g','b','f','y','w','k','v','x','z','j','q'};
    private int row = 0;
    private int col = 0;
    private HashMap<Character, Integer> CountofAllLetters = new HashMap<Character, Integer>();
    //TreeSet<String> badWords = new TreeSet<String>();  IF search times become an issue, implement this
    MyDictionary dictionary = new MyDictionary();
    
    public char[][] fillPuzzle(char[][] board, DictInterface dictionary) {
        
        switch (board[row][col]) {
            case '+': 

                buildStrings(board);
               
                for (int i = 0; i <= 25; i++) {            //Iterate through the alphabet and add valid characters to the board
                
                    if (CountofAllLetters.containsKey(order[i])) {
                            if (countCurrLetter(board, order[i]) >= CountofAllLetters.get(order[i])) continue;
                    }   
                    rowString.insert(col, order[i]);
                    colString.insert(row, order[i]);

                    if (isValid(board, dictionary)){
                        board[row][col] = order[i];

                        if (row == board.length-1 && col == board.length-1) //If the board is full, return the board
                        { 
                            return board;
                        }
                        row = row + (col + 1) / board[0].length;
                        col = (col + 1) % board[0].length;               //If the row isn't full, move onto the next column
                       
                        if(fillPuzzle(board, dictionary) != null) return board;
                        
                    }
                    if (i == 25) //If we reach the end of the loop and haven't found a valid letter, we need to backtrack
                    {
                        
                        if (col != 0)
                        {
                            board[row][col] = '+';
                            col--;
                            buildStrings(board);
                        }
                        else if (row == 0 && col == 0) return null;
                        else if (col == 0)
                        {
                            board[row][col] = '+';
                            col += board.length-1;
                            row--;
                            buildStrings(board);
                        }

                        return null;
                    }
                    buildStrings(board);
                }

                break;

            case '-':   //FOR WHEN IT IS A NEGATIVE.  Basically, we just skip over it. 
                if (row == board.length-1 && col == board.length-1) //If the board is full, return the board
                    { 
                        return board;
                    }
                    row = row + (col + 1) / board[0].length;
                    col = (col + 1) % board[0].length;             //If the row isn't full, move onto the next column   

                if(fillPuzzle(board, dictionary) != null) break;
                else if (col != 0)                  //Move back if we failed the previous recursive call. 
                    {
                        col--;
                        buildStrings(board);
                        return null;
                    }
                    else if (row == 0 && col == 0) return null;
                    else if (col == 0)
                    {
                        col += board.length-1;
                        row--;
                        buildStrings(board);
                        return null;
                    }
                break;
            default:
                if (Character.isLetter(board[row][col])){   //FOR WHEN IT IS A LETTER ALREADY ON THE EMPTY BOARD
                    
                    buildStrings(board);
                    
                    rowString.append(board[row][col]);
                    colString.append(board[row][col]);

                    if (isValid(board, dictionary))         //Test it with the current row and column strings
                    {
                        if (row == board.length-1 && col == board.length-1) //If the board is full, return the board
                        { 
                            return board;
                        }
                        row = row + (col + 1) / board[0].length;
                        col = (col + 1) % board[0].length;               //If the row isn't full, move onto the next column
                       
                        if(fillPuzzle(board, dictionary) != null) return board;   //Recurse
                    }
                        
                    if (col != 0)       //If we are not at the beginning of the row, we move back one column
                    {
                        col--;
                        buildStrings(board);
                    }
                    else if (row == 0 && col == 0) return null;         //If we are at the beginning of the board and failed every letter, the board is unsolvable
                    else if (col == 0)                        //If we are at the beginning of the row, we move back one row and to the end of the row
                    {
                        col += board.length-1;
                        row--;
                        buildStrings(board);
                    }
                    return null;
                }

                else{   //FOR WHEN IT IS A NUMBER
                    
                    buildStrings(board);
                    int num = Character.getNumericValue(board[row][col]);
                    for (int i = 0; i <= 25; i++) {            //Iterate through the alphabet and add valid characters to the board


                        if (CountofAllLetters.containsKey(order[i])) {              //If we have a limit for the current letter and the  
                            if (countCurrLetter(board, order[i]) >= num) continue;  //number of times it appears is greater than the limit, don't try it
                        }
                        else if (!CountofAllLetters.containsKey(order[i])) {            //If we don't have a limit for the current letter and the number of 
                            if (countCurrLetter(board, order[i]) >= num) continue;      //times it appears is less than the current number, add it to the hashmap and continue to test it
                            CountofAllLetters.put(order[i], num);
                        }

                        rowString.insert(col, order[i]);
                        colString.insert(row, order[i]);

                        if (isValid(board, dictionary)){
                            board[row][col] = order[i];

                            if (row == board.length-1 && col == board.length-1) //If the board is full, return the board
                            { 
                                return board;
                            }
                            row = row + (col + 1) / board[0].length;
                            col = (col + 1) % board[0].length;               //If the row isn't full, move onto the next column
                            
                            CountofAllLetters.put(order[i], CountofAllLetters.get(order[i]) + 1);       //Increment the number of the letter we just added

                            if(fillPuzzle(board, dictionary) != null) return board;     //Dive deeper into the recursion

                            CountofAllLetters.put(order[i], CountofAllLetters.get(order[i]) - 1);       //Decrement the number of the letter we just added if the recursion fails
                            
                        }
                        if (i == 25) //If we reach the end of the loop and haven't found a valid letter, we need to backtrack
                        {
                            
                            if (col != 0)       //If we are not at the beginning of the row, we move back one column
                            {
                                String temp = Integer.toString(i);
                                board[row][col] = temp.charAt(0);
                                col--;
                                buildStrings(board);
                            }
                            else if (row == 0 && col == 0) return null;         //If we are at the beginning of the board and failed every letter, the board is unsolvable
                            else if (col == 0)                                  //If we are at the beginning of the row, we move back one row and to the end of the row
                            {
                                String temp = Integer.toString(i);
                                board[row][col] = temp.charAt(0);
                                col += board.length-1;
                                row--;
                                buildStrings(board);
                            }
                            return null;
                        }
                        buildStrings(board);
                    }
                    break;
                }
        }
        return board;
    }

    public boolean isValid(char[][]board, DictInterface dictionary) {

        String[] RowArray = rowString.toString().split("-");                //Splits the row into arrays of words ignoring the "-" 
        String[] ColArray = colString.toString().split("-");                //Splits the col into arrays of words ignoring the "-"  

        StringBuilder tempRow = new StringBuilder();                //StringBuilder to build the words we will search for
        StringBuilder tempCol = new StringBuilder();                //StringBuilder to build the words we will search for
        
        //CHECKING THE ROWS
        for (int i = 0; i < RowArray.length; i++) {
            if (RowArray[i].isEmpty()) continue;        //If the "-" comes first, it gets counted as an empty string, so we have to ignore it. 
            if (i < RowArray.length-1) {     
                if (dictionary.searchPrefix(tempRow.append(RowArray[i])) < 2) {     //If we are not at the end of the row, we MUST be at the end of a WORD.
                    rowString.deleteCharAt(rowString.length()-1);                   //If we fail, we delete the last letter we added and return false.
                    return false;
                }
            }
            else if (col == board.length-1 || board[row][col+1] == '-'){        //Otherwise, if we are at the end of the row, we must be at the end of a WORD
                if (dictionary.searchPrefix(tempRow.append(RowArray[i])) < 2) {
                    rowString.deleteCharAt(rowString.length()-1);               //If we fail, we delete the last letter we added and return false.
                    return false;
                }
            }
            else if (dictionary.searchPrefix(tempRow.append(RowArray[i])) < 1) {    //If we are not at the end of the row, we must at least be a prefix of a word
                rowString.deleteCharAt(rowString.length()-1);                    //If we fail, we delete the last letter we added and return false.
                return false;       
            }
            tempRow = new StringBuilder();          //Reset the StringBuilder
        }
        //CHECKING THE COLUMNS
        for (int i = 0; i < ColArray.length; i++) {
            if (ColArray[i].isEmpty()) continue;        //If the "-" comes first, it gets counted as an empty string, so we have to ignore it.
            if (i < ColArray.length-1) {
                if (dictionary.searchPrefix(tempCol.append(ColArray[i])) < 2) {    //If we are not at the end of the row, we MUST be at the end of a WORD.
                    colString.deleteCharAt(colString.length()-1);               //If we fail, we delete the last letter we added and return false.
                    return false;
                }
            }
            else if (row == board.length-1 || board[row+1][col] == '-'){
                if (dictionary.searchPrefix(tempCol.append(ColArray[i])) < 2) {   //Otherwise, if we are at the end of a col or right before a "-", we MUST be a WORD.
                    colString.deleteCharAt(colString.length()-1);                    
                    return false;
                }
            }
            else if (dictionary.searchPrefix(tempCol.append(ColArray[i])) < 1) {       //Otherwise, we need to be a prefix
                colString.deleteCharAt(colString.length()-1);
                return false;
            }
            tempCol = new StringBuilder();      //Reset the StringBuilder
        }

        return true;
    }

    public void buildStrings(char[][] board){           //Builds the strings that we will use to check if the board is valid
        rowString = new StringBuilder();
        colString = new StringBuilder();

        for (int j = 0; j < col; j++) {
            if (Character.isLetter(board[row][j]) || board[row][j] == '-') {         //Checks if what we are adding is a letter or "-"", if not, we move on
                rowString.append(board[row][j]);
            }
        }
        for (int k = 0; k < row; k++){
            if (Character.isLetter(board[k][col]) || board[k][col] == '-') {         //Checks if what we are adding is a letter or "-"", if not, we move on
                colString.append(board[k][col]);
            }
        }
    }

    public int countCurrLetter (char[][]board, char letter) {  //Iterates through the board and counts the number of times letters occur.
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            for (int j=0; j < board.length; j++) {
                if (board[i][j] == letter) count++;
            }
        }
        return count;
    }


    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary) {
        HashMap<Character, Integer> CountofAllLetters = new HashMap<Character, Integer>();  //HashMap to keep track of the number of each letter in the board
        for(char letter = 'a'; letter <= 'z'; letter++) {  //Initialize the hashmap with all letters and a value of 0
            CountofAllLetters.put(letter, 0);
        }

        for (int i = 0; i < emptyBoard.length; i++) {    //Iterate through the board and add the number of each letter to the hashmap
            for (int j = 0; j < emptyBoard.length; j++) {   
                if (filledBoard[i][j] == '-') continue;
                CountofAllLetters.put(filledBoard[i][j], CountofAllLetters.get(filledBoard[i][j]) + 1);

            }
        }

        for (int i = 0; i < emptyBoard.length; i++) {     //Iterate through the board again
            for (int j = 0; j < emptyBoard.length; j++) {
                col = i;
                row = j;
                if (emptyBoard[i][j] == '-') {          //If we changed any negatives, return false
                    if (filledBoard[i][j] != '-') return false;
                }
                if (emptyBoard[i][j] == '+') {              //If we have any pluses that didn't become letters, return false
                    if (!Character.isLetter(filledBoard[i][j])) return false;
                }
                if (emptyBoard[i][j] >= '1' && emptyBoard[i][j] <= '9') {     //If we have any numbers that didn't become letters, return false
                    if (!Character.isLetter(filledBoard[i][j])) return false;
                    if (emptyBoard[i][j] == '-') continue;                      //Or if the numbers became negatives, return false
                    if (CountofAllLetters.get(filledBoard[i][j]) > (int)emptyBoard[i][j]) return false;   //OR if the number of a letter is greater than it's limit, return false
                }
                if (emptyBoard[i][j] >= 'A' && emptyBoard[i][j] <= 'Z') {   //If any of the numbers on the empty board got changed, return false.
                    if (!String.valueOf(filledBoard[i][j]).equalsIgnoreCase(String.valueOf(emptyBoard[i][j]))) return false;
                }
                if(isValid(filledBoard, dictionary) == false) return false;         //If the rows or columns are not valid, return false
            }
        }

        return true;
    }
}


