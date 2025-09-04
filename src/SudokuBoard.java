public class SudokuBoard {
    private int[][] board;

    //constructor - generate an empty 9x9 sudoku board
    public SudokuBoard(){
        board = new int[9][9];
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
    public int[][] getCopyOfBoard(){
        SudokuBoard boardCopy = new SudokuBoard(this.board);
        return boardCopy.board;
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

}//end of class

