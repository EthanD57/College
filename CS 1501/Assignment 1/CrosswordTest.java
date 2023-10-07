import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class CrosswordTest {
    public static void main(String[] args) throws FileNotFoundException {
        if(args.length < 2){
            System.out.println("Usage: java CrosswordTest <dictionary file name> <puzzle file name>");
        } else {
            new CrosswordTest(args);
        }      
    }
    
    public CrosswordTest(String[] args) throws FileNotFoundException {
        //Read the dictionary
        Scanner fileScan = new Scanner(new FileInputStream(args[0]));
        String st;
        DictInterface D = new MyDictionary();
    
        while (fileScan.hasNext())
        {
            st = fileScan.nextLine();
            D.add(st);
        }
        fileScan.close();
    
        // Parse input file of the Crossword board to create 2-d grid of characters
    
        Scanner fReader = new Scanner(new FileInputStream(args[1]));
        String input = fReader.nextLine();
        int boardSize = Integer.parseInt(input);
    
        char[][] theBoard = new char[boardSize][boardSize];
    
        for (int i = 0; i < boardSize; i++)
        {
            String rowString = fReader.nextLine();
            for (int j = 0; j < boardSize; j++)
            {
                theBoard[i][j] = Character.toLowerCase(rowString.charAt(j));
            }
        }
        fReader.close();

        Crossword cr = new Crossword();
    
        char[][] solution = cr.fillPuzzle(theBoard, D);
        if(solution != null) {
            printSolution(solution);
            if(cr.checkPuzzle(theBoard, solution, D)){
                System.out.println("Solution is correct");    
            } else {
                System.out.println("Solution is incorrect");    
            }
        } else {
            System.out.println("No solutions found.");
        }
    }


    private void printSolution(char[][] board){
        int boardSize = board[0].length;
        for(int i=0; i<boardSize; i++){
            for(int j=0; j<boardSize; j++){
                System.out.print(Character.toUpperCase(board[i][j]));
            }
            System.out.println();
        }
    }
}
