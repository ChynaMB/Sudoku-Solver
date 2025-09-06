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
    public SudokuBoard(int[][] board){
        this.board = new int[9][9]; //create empty board
        for(int i = 0; i < 9; i++) { //loop through each row
            for (int j = 0; i < 9; i++) { //loop through each column
                this.board[i][j] = board[i][j]; //copy the number over
            }
        }
    }

    //getter and setter methods
    public SudokuBoard getCopyOfBoard(){
        SudokuBoard boardCopy = new SudokuBoard(this.board);
        return boardCopy;
    }
    public int getCell(int row, int col){
        return this.board[row][col];
    }
    public void setCell(int row, int col, int value){
        this.board[row][col] = value;
    }

    // Check if a number entered already exists within the row, column or 3x3 box
    public boolean isValidEntry(int row, int col, int value){
        //row check
        for(int i=0; i<9; i++){
            if (this.board[row][i] == value){
                return false;
            }
        }

        //column check
        for(int i=0; i<9; i++){
            if(this.board[i][col] == value){
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
                if (this.board[startRow + i][startCol + j] == value){
                    return false;
                }
            }
        }

        return true;
    }

    //return a list of potential numbers that can go inside a sudoku cell
    public List<Integer> potentialEntries(int row, int col){
        List<Integer> potentialEntries = new ArrayList<>();
        for(int i=0; i<9; i++){
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

    public boolean checkSolution(SudokuBoard board){
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                int value = board.getCell(row, col);
                if (value == 0 || !board.isValidEntry(row, col, value)){
                    return false;
                }
            }
        }
        return true;
    }

}//end of class

