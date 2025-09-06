import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class DataCollector {
    private String stringBoard;
    private String difficulty;
    private String hintsUsed;
    private String timeTaken; // in seconds
    private int numberOfEntries;
    private int entryNumber;
    }

    public DataCollector(SudokuBoard board, String difficulty, int hintsUsed, String timeTaken){
        this.stringBoard = boardToString(board);
        this.difficulty = difficulty;
        this.hintsUsed = String.valueOf(hintsUsed);
        this.timeTaken = timeTaken;
        this.numberOfEntries = getNumberOfEntries();
        this.entryNumber = numberOfEntries + 1;
    }

    //setter methods
    public void setHintsUsed(int hintsUsed) {
        this.hintsUsed = String.valueOf(hintsUsed);
    }
    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    //convert the board to a string representation
    public String boardToString(SudokuBoard board) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                sb.append(board.getCell(i, j));
                if (j < 8) sb.append(" ");
            }
            if (i < 8) sb.append("\n");
        }
        return sb.toString();
    }

    //convert string representation back to a board
    public SudokuBoard stringToBoard(String boardString) {
        SudokuBoard newBoard = new SudokuBoard();
        String[] rows = boardString.split("\n");
        for (int i = 0; i < 9; i++) {
            String[] values = rows[i].split(" ");
            for (int j = 0; j < 9; j++) {
                newBoard.setCell(i, j, Integer.parseInt(values[j]));
            }
        }
        return newBoard;
    }

    //store the data in a CSV file
    public void storeData() {
        try (FileWriter writer = new FileWriter("sudoku_data.csv", true)) {
            //store the entry in the format: entryNumber,board,difficulty,hintsUsed,timeTaken
            writer.append(String.valueOf(entryNumber)).append(",");
            writer.append(stringBoard.replace("\n", ";")).append(","); //replace newlines with semicolons for CSV format
            writer.append(difficulty).append(",");
            writer.append(hintsUsed).append(",");
            writer.append(timeTaken).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //convert a CSV entry back to a DataCollector object
    public static DataCollector csvEntryToDataCollector(String csvEntry) {
        String[] parts = csvEntry.split(",");
        int entryNumber = Integer.parseInt(parts[0]);
        String boardString = parts[1].replace(";", "\n"); //replace semicolons back to newlines
        String difficulty = parts[2];
        int hintsUsed = Integer.parseInt(parts[3]);
        String timeTaken = parts[4];

    //rewrite an entry in the CSV file (based on entry number)
    public void updateEntry(int entryNumber) {
        //convert the csv entry back to a DataCollector object
        //use available setter methods to update hintsUsed and timeTaken
        //rewrite the entire CSV file with the updated entry


    //get number of lines in the CSV file (number of entries)
    public int getNumberOfEntries() {
        int lines = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("sudoku_data.csv"));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
    }
        return lines;
    }


}//end of DataCollector
