import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SudokuSolver{
    final private SudokuBoard originalBoard;
    private SudokuBoard solvedBoard;

    //constructor
    public SudokuSolver(SudokuBoard board) {
        this.originalBoard = board.getCopyOfSudokuBoard();
        this.solvedBoard = board.getCopyOfSudokuBoard();
    }

    //solve the board using backtracking
    public boolean backtrackingSolver() {
        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){ //nested for loop allows us to loop through each cell
                if (this.solvedBoard.getCell(row, col) == 0) { //if empty cell has been found
                    for (int i=1; i<=9; i++){
                        if (this.solvedBoard.isValidEntry(row, col, i)){
                            this.solvedBoard.setCell(row, col, i);
                            if (backtrackingSolver()){ // Recursive call
                                return true;
                            }else{
                                this.solvedBoard.setCell(row, col, 0); //backtrack if solveBoard returns false (no valid number solution found)
                            }
                        }
                    }
                    return false; //no valid number found -> backtrack
                }
            }
        }
        return true; //no empty cells -> solved
    }

    //solve the board using logic (naked and hidden singles)
    public boolean logicSolver(){
        boolean changed = true;
        while(changed){
            System.out.println("Starting logic iteration");
            changed = fillNakedSingles();
            changed = fillHiddenSingles() || changed;
            System.out.println("Logic iteration complete");
        }
        if(!this.solvedBoard.boardIsFilled()){
            //loop through the board to count the number of empty cells
            int emptyCells = 0;
            for(int row=0; row<9; row++){
                for(int col=0; col<9; col++) {
                    if (this.solvedBoard.getCell(row, col) == 0) {
                        emptyCells++;
                    }
                }
            }
            if (emptyCells <= 4){
                //see if solvable using backtracking
                System.out.println("Only " + emptyCells + " empty cells remaining, trying backtracking solver.");
                if (backtrackingSolver()){
                    System.out.println("Backtracking solver succeeded.");
                    return true;
                }else{
                    System.out.println("Backtracking solver failed.");
                }
            }

            System.out.println("Board has " + emptyCells + " empty cells remaining after logic solver.");
            return false;
        }
        return true;
    }

    //fill in the naked singles inn a board
    public boolean fillNakedSingles(){
        //naked singles are when there is only one possible entry in a cell (based on sudoku logic)
        boolean changed = false;
        boolean progress = true;

        while(progress){
            progress = false;
            for(int row=0; row<9; row++){
                for(int col=0; col<9; col++){
                    if (this.solvedBoard.getCell(row, col) == 0) { //only check empty cells
                        List<Integer> entries = this.solvedBoard.potentialEntries(row,col);
                        if (entries.size() == 1) {
                            int entry = entries.get(0);
                            this.solvedBoard.setCell(row, col, entry);
                            progress = true;
                            changed = true;
                        }
                    }
                }
            }
        }
        System.out.println("Naked singles filled");
        return changed;
    }

    //fill in the hidden singles on a board
    public boolean fillHiddenSingles() {
        //hidden singles are when there are multiple possible entries for each cell in a row/column/box
        //but a number appears in only one set of possible entries, thus being the solution to that cell
        boolean progress = true;
        boolean changed = false;

        while(progress) {
            progress = false;
            //check each row
            for (int row = 0; row < 9; row++) {
                List<Integer> rowEntries = new ArrayList<>();
                for (int col = 0; col < 9; col++) {
                    if (this.solvedBoard.getCell(row, col) == 0) {
                        List<Integer> entries = this.solvedBoard.potentialEntries(row, col);
                        rowEntries.addAll(entries);
                    }
                }
                for (int num = 1; num <= 9; num++) {
                    if (Collections.frequency(rowEntries, num) == 1) {
                        //find the cell that contains this number in its potential entries
                        for (int col=0; col < 9; col++) {
                            if (this.solvedBoard.getCell(row, col) == 0) {
                                List<Integer> entries = this.solvedBoard.potentialEntries(row, col);
                                if (entries.contains(num)) {
                                    this.solvedBoard.setCell(row, col, num);
                                    progress = true;
                                    changed = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            //check each column
            for (int col = 0; col < 9; col++) {
                List<Integer> colEntries = new ArrayList<>();
                for (int row = 0; row < 9; row++) {
                    if (this.solvedBoard.getCell(row, col) == 0) {
                        List<Integer> entries =  this.solvedBoard.potentialEntries(row, col);
                        colEntries.addAll(entries);
                    }
                }
                for (int num = 1; num <= 9; num++) {
                    if (Collections.frequency(colEntries, num) == 1) {
                        //find the cell that contains this number in its potential entries
                        for (int row = 0; row < 9; row++) {
                            if (this.solvedBoard.getCell(row, col) == 0) {
                                List<Integer> cell = new ArrayList<>(2);
                                cell.add(0, row);
                                cell.add(1, col);
                                List<Integer> entries =  this.solvedBoard.potentialEntries(row, col);
                                if (entries.contains(num)) {
                                    this.solvedBoard.setCell(row, col, num);
                                    progress = true;
                                    changed = true;
                                    break;
                                }
                            }
                        }
                    }
                }
            }

            //check each 3x3 box
            for (int boxRow = 0; boxRow < 3; boxRow++) {
                for (int boxCol = 0; boxCol < 3; boxCol++) {
                    List<Integer> boxEntries = new ArrayList<>();
                    for (int row = 0; row < 3; row++) {
                        for (int col = 0; col < 3; col++) {
                            if (this.solvedBoard.getCell(row, col) == 0) {
                                int actualRow = boxRow * 3 + row;
                                int actualCol = boxCol * 3 + col;
                                List<Integer> entries =  this.solvedBoard.potentialEntries(actualRow, actualCol);
                                boxEntries.addAll(entries);
                            }
                        }
                    }
                    for (int num = 1; num <= 9; num++) {
                        if (Collections.frequency(boxEntries, num) == 1) {
                            //find the cell that contains this number in its potential entries
                            for (int row = 0; row < 3; row++) {
                                for (int col = 0; col < 3; col++) {
                                    if (this.solvedBoard.getCell(row, col) == 0) {
                                        int actualRow = boxRow * 3 + row;
                                        int actualCol = boxCol * 3 + col;
                                        List<Integer> cell = new ArrayList<>(2);
                                        cell.add(0, actualRow);
                                        cell.add(1, actualCol);
                                        List<Integer> entries =   this.solvedBoard.potentialEntries(actualRow, actualCol);
                                        if (entries.contains(num)) {
                                            this.solvedBoard.setCell(actualRow, actualCol, num);
                                            progress = true;
                                            changed = true;
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        System.out.println("Hidden singles filled");
        return changed;
    }

}//end of class
