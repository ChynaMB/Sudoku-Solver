import java.util.HashMap;
import java.util.*;

public class SudokuSolver{
    //private SudokuBoard solvedBoard;
    private Map<List<Integer>, List<Integer>> sudokuMap;

    //constructor
    public SudokuSolver(SudokuBoard board) {
        this.sudokuMap = generateSudokuMap(board);
    }

    //create a map to represent a sudoku board storing all its possible entries in each of its cells
    public Map<List<Integer>, List<Integer>> generateSudokuMap(SudokuBoard board) {
        sudokuMap = new HashMap<>();
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                List<Integer> cell = new ArrayList<>(2);
                cell.add(0,row);
                cell.add(1,col);
                List<Integer> entries = board.potentialEntries(row, col);
                sudokuMap.put(cell, entries);
            }
        }
        return sudokuMap;
    }

    //solve the board using backtracking
    public boolean backtrackingSolver(SudokuBoard board) {
        for (int row=0; row<9; row++){
            for (int col=0; col<9; col++){ //nested for loop allows us to loop through each cell
                if (board.getCell(row, col) == 0) { //if empty cell has been found
                    for (int i=1; i<=9; i++){
                        if (board.isValidEntry(row, col, i)){
                            board.setCell(row, col, i);
                            if (backtrackingSolver(board)){ // Recursive call
                                return true;
                            }else{
                                board.setCell(row, col, 0); //backtrack if solveBoard returns false (no valid number solution found)
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
    public boolean logicSolver(SudokuBoard board){
        System.out.println("Starting logic solver");
        SudokuBoard unsolvedBoard = board.getCopyOfBoard();
        SudokuBoard changesToBoard;
        int changes = 1;
        while(changes > 0){
            changes = 0;
            changesToBoard = fillNakedSingles(unsolvedBoard);
            if (!equalSudoku(changesToBoard, unsolvedBoard)){
                changes = 1;
                unsolvedBoard = changesToBoard;
            }
            changesToBoard = fillHiddenSingles(unsolvedBoard);
            if (!equalSudoku(changesToBoard, unsolvedBoard)){
                changes = 1;
                unsolvedBoard = changesToBoard;
            }
            System.out.println("Logic iteration complete");
        }
        if(!unsolvedBoard.boardIsFilled()){
            System.out.println("Board not solved using logic, backtracking required");
            return false;
        }

        //this.solvedBoard = unsolvedBoard;
        return true;
    }

    //fill in the naked singles inn a board
    public SudokuBoard fillNakedSingles(SudokuBoard board){
        //naked singles are when there is only one possible entry in a cell (based on sudoku logic)
        SudokuBoard boardCopy = board.getCopyOfBoard();
        int changes = 1;
        while(changes > 0){
            changes = 0;
            for(int row=0; row<9; row++){
                for(int col=0; col<9; col++){
                    List<Integer> entries = boardCopy.potentialEntries(row,col);
                    if (entries.size() == 1){
                        int entry = entries.getFirst();
                        boardCopy.setCell(row,col,entry);
                        changes = 1;
                    }
                }
            }
        }
        return boardCopy;
    }

    //fill in the hidden singles on a board
    public SudokuBoard fillHiddenSingles(SudokuBoard board) {
        //hidden singles are when there are multiple possible entries for each cell in a row/column/box
        //but a number appears in only one set of possible entries, thus being the solution to that cell
        SudokuBoard boardCopy = board.getCopyOfBoard();
        //check each row
        for(int row=0; row<9; row++) {
            List<Integer> rowEntries = new ArrayList<>();
            for (int col = 0; col < 9; col++) {
                List<Integer> entries = boardCopy.potentialEntries(row, col);
                rowEntries.addAll(entries);
            }
            for (int num = 1; num <= 9; num++) {
                if (Collections.frequency(rowEntries, num) == 1) {
                    //find the cell that contains this number in its potential entries
                    for (int col = 0; col < 9; col++) {
                        List<Integer> cell = new ArrayList<>(2);
                        cell.add(0, row);
                        cell.add(1, col);
                        List<Integer> entries = boardCopy.potentialEntries(row, col);
                        if (entries.contains(num)) {
                            boardCopy.setCell(row, col, num);
                        }
                    }
                }
            }
        }

        //check each column
        for(int col=0; col<9; col++) {
            List<Integer> colEntries = new ArrayList<>();
            for (int row = 0; row < 9; row++) {
                List<Integer> entries = boardCopy.potentialEntries(row, col);
                colEntries.addAll(entries);
            }
            for (int num = 1; num <= 9; num++) {
                if (Collections.frequency(colEntries, num) == 1) {
                    //find the cell that contains this number in its potential entries
                    for (int row = 0; row < 9; row++) {
                        List<Integer> cell = new ArrayList<>(2);
                        cell.add(0, row);
                        cell.add(1, col);
                        List<Integer> entries = boardCopy.potentialEntries(row, col);
                        if (entries.contains(num)) {
                            boardCopy.setCell(row, col, num);
                        }
                    }
                }
            }
        }

        //check each 3x3 grid
        for(int boxRow=0; boxRow<3; boxRow++) {
            for(int boxCol=0; boxCol<3; boxCol++) {
                List<Integer> boxEntries = new ArrayList<>();
                for (int row = 0; row < 3; row++) {
                    for (int col = 0; col < 3; col++) {
                        int actualRow = boxRow * 3 + row;
                        int actualCol = boxCol * 3 + col;
                        List<Integer> entries = boardCopy.potentialEntries(actualRow, actualCol);
                        boxEntries.addAll(entries);
                    }
                }
                for (int num = 1; num <= 9; num++) {
                    if (Collections.frequency(boxEntries, num) == 1) {
                        //find the cell that contains this number in its potential entries
                        for (int row = 0; row < 3; row++) {
                            for (int col = 0; col < 3; col++) {
                                int actualRow = boxRow * 3 + row;
                                int actualCol = boxCol * 3 + col;
                                List<Integer> cell = new ArrayList<>(2);
                                cell.add(0, actualRow);
                                cell.add(1, actualCol);
                                List<Integer> entries = boardCopy.potentialEntries(actualRow, actualCol);
                                if (entries.contains(num)) {
                                    boardCopy.setCell(actualRow, actualCol, num);
                                }
                            }
                        }
                    }
                }
            }
        }

        return boardCopy;
    }

    //check if two sudoku boards are equal
    public boolean equalSudoku(SudokuBoard board1, SudokuBoard board2){
        for(int row=0; row<9; row++){
            for(int col=0; col<9; col++){
                if (board1.getCell(row, col) != board2.getCell(row, col)){
                    return false;
                }
            }
        }
        return true;

    }

}//end of class
