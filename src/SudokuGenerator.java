import java.util.Random;

public class SudokuGenerator {
    private SudokuSolver solver;
    private SudokuBoard solvedBoard;
    private SudokuBoard unsolvedBoard;
    private SudokuBoard currentBoard;
    private String difficultyLevel;
    private final Random random = new Random();

    //constructor - generates a new sudoku puzzle based on the difficulty level
    public SudokuGenerator(String difficultyLevel){
        this.solvedBoard = generateSolvedBoard();
        System.out.println("Solved Board Generated");
        this.difficultyLevel = difficultyLevel;
        this.unsolvedBoard = generateUnsolvedBoard();
        System.out.println("Unsolved Board Generated");
        this.currentBoard = this.unsolvedBoard.getCopyOfBoard();
    }

    //getter methods
    public SudokuBoard getSolvedBoard() {
        return this.solvedBoard;
    }
    public SudokuBoard getUnsolvedBoard() {
        return this.unsolvedBoard;
    }
    public String getDifficultyLevel() {
        return this.difficultyLevel;
    }

    //setter methods
    public void setDifficultyLevel(String difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }
    public void setCurrentBoard(SudokuBoard board) {
        this.currentBoard = board;
    }

    //method which generates a fully solved sudoku board
    public SudokuBoard generateSolvedBoard(){
        SudokuBoard board = new SudokuBoard(); //create an empty board
        board.fillBoard(); //fill the empty board using backtracking algorithm
        return board;
    }

    public SudokuBoard generateUnsolvedBoard() {
        unsolvedBoard = this.solvedBoard.getCopyOfBoard();

        int cluesToKeep;
        if(this.difficultyLevel.equals("easy")){
            cluesToKeep = 52 + random.nextInt(5); //52-56 clues
        } else if (this.difficultyLevel.equals("medium")){
            cluesToKeep = 48 + random.nextInt(5); //48-52 clues
        } else if (this.difficultyLevel.equals("hard")){
            cluesToKeep = 44 + random.nextInt(5); //44-48 clues
        } else{
            cluesToKeep = 52 + random.nextInt(5); //default to easy if invalid input
        }

        boolean notSolvable = true;
        while(notSolvable) {
            unsolvedBoard = this.solvedBoard.getCopyOfBoard();
            int cellsToRemove = (9*9) - cluesToKeep;

            while (cellsToRemove > 0) {
                int row = random.nextInt(9);
                int col = random.nextInt(9);
                if (unsolvedBoard.getCell(row, col) == 0) {
                    continue; //cell is already empty, skip to next iteration
                }
                unsolvedBoard.setCell(row, col, 0);
                cellsToRemove--;
            }
            System.out.println("puzzle generated");
            this.solver = new SudokuSolver(unsolvedBoard);
            System.out.println("SOLVER INITIALISED");
            notSolvable = !(solver.logicSolver(unsolvedBoard));
            System.out.println("LOGIC SOLVER ATTEMPTED");
            if(notSolvable){
                System.out.println("puzzle not solvable, regenerating");
            }
        }
        return unsolvedBoard;
    }

    public void getHint(){
        if (currentBoard.equals(solvedBoard)) {
            return; //board is already solved
        }

        int row;
        int col;

        if(currentBoard.boardIsFilled()){
            row = random.nextInt(9);
            col = random.nextInt(9);
        }else {

            do {
                row = random.nextInt(9);
                col = random.nextInt(9);
            } while (currentBoard.getCell(row, col) != 0);
        }

        int hintValue = solvedBoard.getCell(row, col);
        //ensure the hint is not in a cell that is already correctly filled in
        if (hintValue == currentBoard.getCell(row,col)){
            getHint();
            return;
        }
        currentBoard.setCell(row, col, hintValue);
    }
}

