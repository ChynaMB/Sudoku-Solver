import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;
import java.util.Collections;


public class SudokuUI {
    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];
    private SudokuGenerator generator;
    private SudokuBoard unsolvedBoard;
    private SudokuBoard solvedBoard;
    private SudokuBoard currentBoard;
    private javax.swing.Timer swingTimer;
    private Timer timer;
    private int hintsUsed;
    private boolean solved;
    private DataCollector dataCollector;

    //main method to launch the application
    public static void main(String[] args) {
        System.out.println("Welcome to the Sudoku Game!");
        SudokuUI ui = new SudokuUI();
        ui.MenuUI();
    }

    //constructor to initialize the UI and show the main menu
    public void MenuUI() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.setSize(600, 600);

        //menu to select difficulty, view fastest times, or exit
        JPanel menuPanel = new JPanel(new GridLayout(0, 1, 5, 5));
        JButton newEasyGameButton = new JButton("New Easy Game");
        JButton newMediumGameButton = new JButton("New Medium Game");
        JButton newHardGameButton = new JButton("New Hard Game");
        JButton fastestTimesButton = new JButton("Fastest Solve Times");
        JButton closeBtn = new JButton("Close");

        menuPanel.add(newEasyGameButton);
        menuPanel.add(newMediumGameButton);
        menuPanel.add(newHardGameButton);
        menuPanel.add(fastestTimesButton);
        menuPanel.add(closeBtn);

        frame.add(menuPanel);
        frame.setVisible(true);

        //action listeners for buttons
        newEasyGameButton.addActionListener(e -> startNewGame(this.frame,"easy"));
        newMediumGameButton.addActionListener(e -> startNewGame(this.frame,"medium"));
        newHardGameButton.addActionListener(e -> startNewGame(this.frame,"hard"));
        fastestTimesButton.addActionListener(e -> showFastestTimes(this.frame));
        closeBtn.addActionListener(e -> System.exit(0));
    }

    //method to display the Sudoku board UI
    public void startNewGame(JFrame startMenu, String difficulty) {
        this.generator = new SudokuGenerator(difficulty);
        this.unsolvedBoard = generator.getUnsolvedBoard();
        this.solvedBoard = generator.getSolvedBoard();
        this.currentBoard = this.unsolvedBoard.getCopyOfSudokuBoard();
        this.timer = new Timer();
        this.hintsUsed = 0;
        this.solved = false;

        frame = new JFrame("Sudoku Game - " + generator.getDifficultyLevel() + " Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //timer and hint counter panel
        JPanel timerAndHintPanel = new JPanel(new FlowLayout());
        JLabel timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerAndHintPanel.add(timerLabel);
        JLabel hintLabel = new JLabel("Hints Used: 0");
        hintLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerAndHintPanel.add(hintLabel);

        //update timer every second
        this.swingTimer = new javax.swing.Timer(1000, e -> updateTime(timerLabel));

        //board panel
        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 20);
        //font colour for preset numbers is blue, user entries is black, incorrect entries is red
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);

                this.cells[row][col] = cell;
                restrictCellInput(cell);

                //set borders to visually separate 3x3 grids
                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                if (row % 3 == 0) cell.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
                if (col % 3 == 0) cell.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.BLACK));
                boardPanel.add(cell);
            }
        }

        //button panel
        JPanel buttonPanel = new JPanel(new BorderLayout());
        JButton startBtn = new JButton("Start Game");
        JButton hintBtn = new JButton("Get Hint");
        JButton checkBtn = new JButton("Check Solution");
        JButton clearBtn = new JButton("Clear All Entries");
        JButton quitBtn = new JButton("Quit");

        buttonPanel.add(startBtn, BorderLayout.NORTH);
        buttonPanel.add(hintBtn, BorderLayout.WEST);
        buttonPanel.add(checkBtn, BorderLayout.CENTER);
        buttonPanel.add(clearBtn, BorderLayout.EAST);
        buttonPanel.add(quitBtn, BorderLayout.SOUTH);

        //action listeners for buttons
        startBtn.addActionListener(e -> startSudokuGame());
        hintBtn.addActionListener(e -> getHint(hintLabel));
        checkBtn.addActionListener(e -> checkSolution());
        clearBtn.addActionListener(e -> clearBoard());
        quitBtn.addActionListener(e -> {
            frame.dispose();
            MenuUI();
        });

        //add panels to frame
        frame.add(timerAndHintPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setVisible(true);
        if (startMenu != null) {
            startMenu.dispose();
        }

        System.out.println("New " + difficulty + " game started.");
    }

    //restrict input to numbers 1-9 only
    private void restrictCellInput(JTextField cell) {
        cell.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyReleased(java.awt.event.KeyEvent e) {
                String text = cell.getText();
                if (!text.isEmpty() && !text.matches("[1-9]")) {
                    JOptionPane.showMessageDialog(frame, "Please enter a number between 1 and 9.");
                    cell.setText(""); // clear invalid input
                }
            }
        });
    }

    //start the game by displaying the board and starting the timer
    private void startSudokuGame() {
        //if the board is already solved, do nothing
        if (this.solved) {
            //display message to user
            JOptionPane.showMessageDialog(frame, "The puzzle is already solved!");
            return;
        }
        //if board is already started, do nothing
        if (this.swingTimer.isRunning()) {
            return;
        }
        displayBoard(this.currentBoard);
        this.swingTimer.start();
        this.timer.start();
        System.out.println("Game started. Timer is running.");

    }

    //end the game by stopping the timer and disabling input
    private void endSudokuGame() {
        //stop the timers
        this.swingTimer.stop();
        this.timer.stop();
        //disable further input
        for (JTextField[] row : cells) {
            for (JTextField cell : row) {
                cell.setEditable(false);
            }
        }
        System.out.println("Game ended. Timer stopped. Time taken: " + this.timer.formatElapsedTime());
    }

    //update the timer label every second
    private void updateTime(JLabel timerLabel) {
        timerLabel.setText("Time: " + this.timer.formatElapsedTime());
    }

    //display the current board state on the UI
    private void displayBoard(SudokuBoard board) {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                int value = board.getCell(row, col);
                JTextField cell = this.cells[row][col];
                if (value != 0) {
                    cell.setText(String.valueOf(value));
                    cell.setEditable(false);
                    cell.setForeground(Color.BLUE); // preset numbers in blue
                } else {
                    cell.setText("");
                    cell.setEditable(true);
                    cell.setForeground(Color.BLACK);
                }
            }
        }
    }

    //update the current board state from the UI
    private void updateBoardFromUI() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = cells[row][col].getText();
                int value = (text.isEmpty()) ? 0 : Integer.parseInt(text);
                currentBoard.setCell(row, col, value);
            }
        }
    }

    //check if the current board is solved correctly
    private void checkSolution() {
        updateBoardFromUI();
        if(!this.currentBoard.boardIsFilled()){
            JOptionPane.showMessageDialog(frame, "The board is not completely filled. Please complete all cells before checking the solution.");
            return;
        }
        this.solved = this.currentBoard.checkSolution();
        if (this.solved) {
            endSudokuGame();
            JOptionPane.showMessageDialog(frame, "Congratulations! You've solved the puzzle!");
            saveGame(); //collect data and save to CSV
        } else {
            JOptionPane.showMessageDialog(frame, "The solution is incorrect. Please try again.");
            if(this.generator.getDifficultyLevel().equals("easy")){
                //reset all editable cells to black
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (cells[row][col].isEditable()) {
                            cells[row][col].setForeground(Color.BLACK);
                        }
                    }
                }
                //display incorrect cells in red but only if the cell is not empty
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (currentBoard.getCell(row, col) != solvedBoard.getCell(row, col)
                                && currentBoard.getCell(row, col) != 0) {
                            cells[row][col].setForeground(Color.RED);
                        }
                    }
                }
            }
        }
    }

    //display a hint to the user
    private void getHint(JLabel hintLabel) {
        updateBoardFromUI();
        if (this.solved) {
            JOptionPane.showMessageDialog(frame, "The puzzle is already solved!");
            return;
        }
        if(!this.timer.isRunning()){
            JOptionPane.showMessageDialog(frame," Please start the game before requesting a hint.");
            return;
        }
        this.generator.setCurrentBoard(this.currentBoard);
        this.generator.getHint();
        this.hintsUsed++;
        displayBoard(this.currentBoard);
        hintLabel.setText("Hints Used: " + this.hintsUsed);
    }

    //clear all user entries by resetting to original unsolved board
    private void clearBoard() {
        if (this.solved) {
            JOptionPane.showMessageDialog(frame, "The puzzle is already solved!");
            return;
        }
        if(!this.timer.isRunning()){
            JOptionPane.showMessageDialog(frame," Please start the game before clearing the board.");
        }
        this.currentBoard = this.unsolvedBoard.getCopyOfSudokuBoard();
        displayBoard(this.currentBoard);
    }

    //save the current game state to a file
    public void saveGame(){
        updateBoardFromUI();
        timer.stop();
        try{
            this.dataCollector = new DataCollector(
                    this.currentBoard,
                    this.generator.getDifficultyLevel(),
                    this.hintsUsed,
                    this.timer.formatElapsedTime(),
                    this.solved
            );
            dataCollector.storeData();
        } catch (Exception e){
            throw new RuntimeException("An error occurred while saving the game data.", e);
        }
    }

    //show top 10 fastest solve times from CSV file
    public void showFastestTimes(JFrame startMenu) {
        JFrame timesFrame = new JFrame("Fastest Solve Times");
        timesFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        timesFrame.setLayout(new BorderLayout());

        JTextArea timesArea = new JTextArea();
        timesFrame.setSize(400, 400);
        timesArea.setEditable(false);
        timesArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane scrollPane = new JScrollPane(timesArea);

        //fetch top 10 times from DataCollector and display them
        Integer[] bestTimes = getTopTenTimes();
        for(int i=0; i<bestTimes.length; i++){
            int totalSeconds = bestTimes[i];
            int minutes = totalSeconds / 60;
            int seconds = totalSeconds % 60;
            String timeStr = String.format("%02d:%02d", minutes, seconds);
            timesArea.append((i+1) + ". " + timeStr + "\n");
        }

        JButton backToStartMenuBtn = new JButton("Back To Start Menu");
        backToStartMenuBtn.addActionListener(e -> {
            timesFrame.dispose();
            MenuUI();
        });

        timesFrame.add(scrollPane, BorderLayout.CENTER);
        timesFrame.add(backToStartMenuBtn, BorderLayout.SOUTH);

        timesFrame.setVisible(true);
        if (startMenu != null) {
            startMenu.dispose();
        }
    }

    //fetch and parse top 10 fastest times from CSV file
    private Integer[] getTopTenTimes(){
        try {
            String[] times = DataCollector.fetchAllTimeTakenValues();
            PriorityQueue<Integer> topTen = new PriorityQueue<>();
            for (String time : times) {
                if (time == null || !time.matches("\\d{2}:\\d{2}")) {
                    continue; // skip invalid times
                }
                int totalSeconds = parseTime(time);
                topTen.offer(totalSeconds);
                if (topTen.size() > 10) {
                    topTen.poll(); // remove largest
                }
            }
            Integer[] bestTimes = topTen.toArray(new Integer[0]);
            java.util.Arrays.sort(bestTimes);
            return bestTimes;

        } catch (Exception e) {
            throw new RuntimeException("An error occurred while reading the file.", e);
        }
    }

    //convert "mm:ss" format to total seconds
    private int parseTime(String time) {
        try{
            String[] parts = time.split(":");
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return minutes * 60 + seconds;
        } catch (Exception e) {
            return Integer.MAX_VALUE; // treat invalid times as very slow
        }
    }

}//end of SudokuUI



