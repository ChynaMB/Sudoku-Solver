import java.util.List;
import java.util.ArrayList;
import java.util.Random;

public class SudokuBoard {
    private int[][] board;
    private final Random random = new Random();

    //constructor - generate an empty 9x9 sudoku board
    public SudokuBoard(){
        this.board = new int[9][9];
    }

    //constructor - take a pre-existing board and copy it to this board
    public SudokuBoard(int[][] newBoard){
        this.board = newBoard;
    }

    //getter and setter methods
    public SudokuBoard getCopyOfSudokuBoard(){
        int[][] copyArray = new int[9][9];
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                copyArray[i][j] = this.board[i][j];
            }
        }
        return new SudokuBoard(copyArray);
    }
    public int getCell(int row, int col){
        return this.board[row][col];
    }
    public void setCell(int row, int col, int value){
        this.board[row][col] = value;
    }
    public int[][] getBoard(){
        return this.board;
    }

    // Check if a number entered already exists within the row, column or 3x3 box
    public boolean isValidEntry(int row, int col, int value){
        //row check
        for(int i=0; i<9; i++){
            if (this.board[row][i] == value && i != col){
                return false;
            }
        }
        //column check
        for(int i=0; i<9; i++){
            if(this.board[i][col] == value && i != row){
                return false;
            }
        }
        //3x3 grid check
            //start by finding the upper left corner of the grid
        int startRow = (row/3)*3; //row 0,1,2 -> 0, row 3,4,5 -> 3, row 6,7,8 -> 6
        int startCol = (col/3)*3; //col 0,1,2 -> 0, col 3,4,5 -> 3, col 6,7,8 -> 6
            //then loop through the 3x3 grid and compare the value to the number in the grid
        for (int i=0; i<3; i++){
            for (int j=0; j<3; j++){
                if (this.board[startRow + i][startCol + j] == value
                        //make sure we don't compare the cell to itself
                        && (startRow + i != row)
                        && (startCol + j != col)){
                    return false;
                }
            }
        }

        return true;
    }

    //return a list of potential numbers that can go inside a sudoku cell
    public List<Integer> potentialEntries(int row, int col){
        List<Integer> potentialEntries = new ArrayList<>();
        for(int i=1; i<10; i++){
            if (isValidEntry(row, col, i)){
                potentialEntries.add(i);
            }
        }
        return potentialEntries;
    }

    //backtracking algorithm to fill the board
    public boolean fillBoard() {
        for (int row=0; row<9; row++) {
            for (int col=0; col<9; col++) {
                if (this.getCell(row, col) == 0) {
                    int[] numbers = shuffleNumbers();
                    for (int num : numbers) {
                        if (this.isValidEntry(row, col, num)) {
                            this.setCell(row, col, num);
                            if (fillBoard()) {
                                return true;
                            } else {
                                this.setCell(row, col, 0); // backtrack
                            }
                        }
                    }
                    return false;
                }
            }
        }
        return true;
    }

    public void emptyBoard() {
        for (int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                this.board[i][j] = 0;
            }
        }
    }

    //Fisher-Yates shuffle - algorithm that maximises randomness
    private int[] shuffleNumbers() {
        int[] nums = {1,2,3,4,5,6,7,8,9};
        for (int i = nums.length - 1; i > 0; i--) {
            int j = random.nextInt(i + 1);
            int temp = nums[i];
            nums[i] = nums[j];
            nums[j] = temp;
        }
        return nums;
    }

    //check if the board is filled in
    public boolean boardIsFilled() {
        for (int i=0; i<9; i++) {
            for (int j = 0; j < 9; j++) {
                if (this.board[i][j] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean checkSolution(){
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                int value = this.getCell(row, col);
                if (value == 0 || !this.isValidEntry(row, col, value)){
                    return false;
                }
            }
        }
        return true;
    }

}//end of class

