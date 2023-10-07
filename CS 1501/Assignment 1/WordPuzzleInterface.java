/**
 * An interface for operations on a word puzzle
 */
public interface WordPuzzleInterface {

    /*
     * Fill out a word puzzle defined by an empty board. 
     * 
     *  @param board is a 2-d array representing the empty board to be filled
     *  The characters in the empty board can be:
     *    '+': any letter can go here
     *    '-': no letter is allowed to go here
     *    a letter: this letter has to remain as-is at the same position in the filled puzzle
     *    a value between '1' and '9': any letter can go here and the provided value is an upper bound on the number 
     *                                  of times the letter can appear in the filled board. If a letter has multiple 
     *                                  upper bounds, the largest bound is the effective one.
     *  @param dictionary is the dictinary to be used for filling out the puzzle. Check DictInterface for
     *                    more details on the operations provided by the dictionary
     *  @return a 2-d array representing the filled out puzzle or null if the puzzle has no solution
     */
    public char[][] fillPuzzle(char[][] board, DictInterface dictionary);

    /*
     * Check if filledBoard is a correct filling for a given empty board
     * 
     * @param emptyBoard is a 2-d array representing an empty board
     * @param filledBoard is a 2-d array representing a filled out board
     * @param dictionary is the dictinary to be used for checking the filled out board
     * @return true if rules defined in fillPuzzle has been followed and 
     *  every row and column is a valid word in the dictionary. If a row
     *  or a column includes one or more '-', then each segment should be 
     *  a valid word in the dictionary; the method returns false otherwise
     */
    public boolean checkPuzzle(char[][] emptyBoard, char[][] filledBoard, DictInterface dictionary);
    
}