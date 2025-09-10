import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;

public class DataCollector{
    private String stringBoard;
    private String difficulty;
    private int hintsUsed;
    private String timeTaken; // in seconds
    private boolean solved;
    private int numberOfEntries;
    private int entryNumber;


    //constructor - for new entries
    public DataCollector(SudokuBoard board, String difficulty, int hintsUsed, String timeTaken, boolean solved){
        this.stringBoard = boardToString(board);
        this.difficulty = difficulty;
        this.hintsUsed = hintsUsed;
        this.timeTaken = timeTaken;
        this.solved = solved;
        this.numberOfEntries = getNumberOfEntries();
        this.entryNumber = numberOfEntries + 1;
    }

    //constructor - for existing entries (when reading from CSV)
    public DataCollector(String stringBoard, String difficulty, int hintsUsed, String timeTaken, boolean solved, int entryNumber){
        this.stringBoard = stringBoard;
        this.difficulty = difficulty;
        this.hintsUsed = hintsUsed;
        this.timeTaken = timeTaken;
        this.solved = solved;
        this.numberOfEntries = getNumberOfEntries();
        this.entryNumber = entryNumber;
    }

    //setter methods
    public void setHintsUsed(int hintsUsed) {
        this.hintsUsed = hintsUsed;
    }
    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    //getter methods
    public String getStringBoard() {
        return stringBoard;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public int getHintsUsed() {
        return hintsUsed;
    }
    public String getTimeTaken() {
        return timeTaken;
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
        //append to the file if it exists, otherwise create a new file
        try (FileWriter writer = new FileWriter("sudoku_data.csv", true)) {
            //store the entry in the format: entryNumber,board,difficulty,hintsUsed,timeTaken
            writer.append(String.valueOf(this.entryNumber)).append(",");
            writer.append(this.stringBoard.replace("\n", ";")).append(","); //replace newlines with semicolons for CSV format
            writer.append(this.difficulty).append(",");
            writer.append(String.valueOf(this.hintsUsed)).append(",");
            writer.append(this.timeTaken).append("\n");
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while writing to the file.", e);
        }
    }

    //convert a CSV entry back to a DataCollector object
    public static DataCollector csvToData(int entryNumber) {
        if(entryNumber <= 0) {
            throw new IllegalArgumentException("Entry number must be greater than 0");
        }

        String[] parts = (String.valueOf(entryNumber)).split(",");
        String boardString = parts[1].replace(";", "\n"); //replace semicolons back to newlines
        String difficulty = parts[2];
        int hintsUsed = Integer.parseInt(parts[3]);
        String timeTaken = parts[4];
        boolean solved = Boolean.parseBoolean(parts[5]);
        DataCollector data = new DataCollector(boardString, difficulty, hintsUsed, timeTaken, solved, entryNumber);
        return data;
    }

    //rewrite an entry in the CSV file (based on entry number)
    public void updateEntry(int entryNumber, int hintsUsed, String timeTaken) {
        //convert the csv entry back to a DataCollector object
        DataCollector data = csvToData(entryNumber);

        //use available setter methods to update hintsUsed and timeTaken
        data.setHintsUsed(hintsUsed);
        data.setTimeTaken(timeTaken);

        //rewrite the entire CSV file with the updated entry
        try {
            BufferedReader reader = new BufferedReader(new FileReader("sudoku_data.csv"));
            StringBuilder sb = new StringBuilder();
            String line;
            int currentEntryNumber = 1;
            //read through each line of the file
            while ((line = reader.readLine()) != null) {
                //if this is the line to be updated
                if (currentEntryNumber == entryNumber) {
                    //replace the line with the updated data
                    sb.append(data.entryNumber).append(",");
                    sb.append(data.stringBoard.replace("\n", ";")).append(","); //replace newlines with semicolons for CSV format
                    sb.append(data.difficulty).append(",");
                    sb.append(data.hintsUsed).append(",");
                    sb.append(data.timeTaken).append("\n");
                    sb.append(data.solved).append("\n");
                } else {
                    sb.append(line).append("\n");
                }
                currentEntryNumber++;
            }
            reader.close();
            //write the updated content back to the file
            FileWriter writer = new FileWriter("sudoku_data.csv");
            writer.write(sb.toString());
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while updating the file.", e);
        }
    }

    //get number of lines in the CSV file (number of entries)
    public int getNumberOfEntries() {
        int lines = 0;
        try{
            BufferedReader reader = new BufferedReader(new FileReader("sudoku_data.csv"));
            while (reader.readLine() != null) lines++;
            reader.close();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while updating the file.", e);
        }
        return lines;
    }

    //fetch all timeTaken values from the CSV file
    public String[] fetchAllTimeTakenValues() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("sudoku_data.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length >= 5) {
                    sb.append(parts[4]).append("\n");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("An error occurred while reading the file.", e);
        }
        return sb.toString().split("\n");
    }
}//end of DataCollector
