import java.io.FileWriter;
import java.io.IOException;

public class DataCollector {
    private SudokuBoard board;
    private String difficulty;
    private int hintsUsed;
    private int timeTaken; // in seconds

    public DataCollector(SudokuBoard board, String difficulty, int hintsUsed, int timeTaken) {
        this.board = board;
        this.difficulty = difficulty;
        this.hintsUsed = hintsUsed;
        this.timeTaken = timeTaken;
    }

    //store the data in a CSV file
    public void storeData() {
        try (FileWriter writer = new FileWriter("sudoku_data.csv", true)) {
            writer.append(difficulty).append(",");
            writer.append(String.valueOf(hintsUsed)).append(",");
            writer.append(String.valueOf(timeTaken)).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}//end of DataCollector
