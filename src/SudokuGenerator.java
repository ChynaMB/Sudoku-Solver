import java.util.Random;

public class SudokuGenerator {
    private SudokuSolver solver;
    private SudokuBoard solvedBoard;
    private SudokuBoard unsolvedBoard;
    String difficultyLevel;
    private final Random random = new Random();

    public SudokuGenerator(String difficultyLevel){
        this.solvedBoard = generateSolvedBoard();
        this.difficultyLevel = difficultyLevel;
        this.unsolvedBoard = generateUnsolvedBoard();
    }

    //method which generates a fully solved sudoku board
    public SudokuBoard generateSolvedBoard(){
        SudokuBoard board = new SudokuBoard(); //create an empty board
        board.fillBoard(); //fill the empty board
        return board;
    }

    public SudokuBoard generateUnsolvedBoard() {
        unsolvedBoard = this.solvedBoard.getCopyOfBoard();

        int cluesToKeep;
        if(this.difficultyLevel.equals("easy")){
            cluesToKeep = 36 + random.nextInt(5); //36-40 clues
        } else if (this.difficultyLevel.equals("medium")){
            cluesToKeep = 32 + random.nextInt(5); //32-36 clues
        } else if (this.difficultyLevel.equals("hard")){
            cluesToKeep = 28 + random.nextInt(5); //28-32 clues
        } else{
            cluesToKeep = 36 + random.nextInt(5); //default to easy if invalid input
        }

        int cellsToRemove = (9*9) - cluesToKeep;
        boolean notSolvable = true;
        while(notSolvable) {
            while (cellsToRemove > 0) {
                int row = random.nextInt(9);
                int col = random.nextInt(9);
                unsolvedBoard.setCell(row, col, 0);
                cellsToRemove--;
            }
            this.solver = new SudokuSolver(unsolvedBoard);
            notSolvable = !(solver.logicSolver(unsolvedBoard));
            if(notSolvable) {
                unsolvedBoard.emptyBoard();
                unsolvedBoard.fillBoard();
            }
        }
        return unsolvedBoard;
    }
}
