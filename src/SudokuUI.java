import javax.swing.*;
import java.awt.*;
import java.util.PriorityQueue;


public class SudokuUI {
    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];

    private SudokuBoard unsolvedBoard;
    private SudokuBoard solvedBoard;
    private SudokuBoard currentBoard;
    private SudokuGenerator generator;
    private Timer timer;
    private int hintsUsed = 0;
    private boolean solved = false;
    private DataCollector dataCollector;

    public void MenuUI() {
        frame = new JFrame("Sudoku Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //menu to select difficulty or load saved game
        JPanel menuPanel = new JPanel();
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

        frame.add(menuPanel, BorderLayout.NORTH);
        frame.setSize(600, 600);
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
        System.out.println("Generating a new " + difficulty + " Sudoku puzzle...");
        this.generator = new SudokuGenerator(difficulty);
        System.out.println("Sudoku puzzle generated.");
        this.unsolvedBoard = generator.getUnsolvedBoard();
        System.out.println("Successfully generated a solvable sudoku puzzle");
        this.currentBoard = this.unsolvedBoard.getCopyOfSudokuBoard();
        this.solvedBoard = generator.getSolvedBoard();
        this.timer = new Timer();
        System.out.println("Puzzle and timer generated. Now displaying game...");

        frame = new JFrame("Sudoku Game - " + generator.getDifficultyLevel() + " Level");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        //timer panel
        JPanel timerPanel = new JPanel();
        JLabel timerLabel = new JLabel("Time: 00:00");
        timerLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timerPanel.add(timerLabel);
        //update timer every second
        javax.swing.Timer swingTimer = new javax.swing.Timer(1000, e -> updateTime(timerLabel));
        swingTimer.start();

        //board panel
        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 20);
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);
                this.cells[row][col] = cell;

                cell.setBorder(BorderFactory.createLineBorder(Color.GRAY));
                if (row % 3 == 0) cell.setBorder(BorderFactory.createMatteBorder(2, 1, 1, 1, Color.BLACK));
                if (col % 3 == 0) cell.setBorder(BorderFactory.createMatteBorder(1, 2, 1, 1, Color.BLACK));
                boardPanel.add(cell);
            }
        }

        //button panel
        JPanel buttonPanel = new JPanel();
        JButton startBtn = new JButton("Start Game");
        JButton checkBtn = new JButton("Check Solution");
        JButton hintBtn = new JButton("Get Hint");
        JButton solveBtn = new JButton("Solve Sudoku");
        JButton clearBtn = new JButton("Clear All Entries");
        JButton backToStartMenuBtn = new JButton("Back To Start Menu");

        buttonPanel.add(startBtn);
        buttonPanel.add(checkBtn);
        buttonPanel.add(hintBtn);
        buttonPanel.add(solveBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(backToStartMenuBtn);

        //action listeners for buttons
        startBtn.addActionListener(e -> startSudokuGame());
        checkBtn.addActionListener(e -> checkSolution());
        hintBtn.addActionListener(e -> getHint());
        solveBtn.addActionListener(e -> solveBoard());
        clearBtn.addActionListener(e -> clearBoard());
        backToStartMenuBtn.addActionListener(e -> {
            frame.dispose();
            MenuUI();
        });

        //add panels to frame
        frame.add(timerPanel, BorderLayout.NORTH);
        frame.add(boardPanel, BorderLayout.CENTER);
        frame.add(buttonPanel, BorderLayout.SOUTH);
        frame.setSize(800, 800);
        frame.setVisible(true);
        if (startMenu != null) {
            startMenu.dispose();
        }

        System.out.println("New " + difficulty + " game started.");
    }

    private void startSudokuGame() {
        displayBoard(this.currentBoard);
        timer.start();
        System.out.println("Game started. Timer is running.");
    }

    //method to update the timer label every second
    private void updateTime(JLabel timerLabel) {
        timerLabel.setText("Time: " + timer.formatElapsedTime());
    }

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

    private void updateBoardFromUI() {
        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                String text = cells[row][col].getText();
                int value = (text.isEmpty()) ? 0 : Integer.parseInt(text);
                currentBoard.setCell(row, col, value);
            }
        }
    }

    private void checkSolution() {
        updateBoardFromUI();
        this.solved = this.currentBoard.checkSolution();
        if (this.solved) {
            timer.stop();
            JOptionPane.showMessageDialog(frame, "Congratulations! You've solved the puzzle!");
            //collect data and save to CSV
            saveGame();
            //disable further input
            for (JTextField[] row : cells) {
                for (JTextField cell : row) {
                    cell.setEditable(false);
                }
            }
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
                //display incorrect cells in red
                for (int row = 0; row < 9; row++) {
                    for (int col = 0; col < 9; col++) {
                        if (currentBoard.getCell(row, col) != solvedBoard.getCell(row, col)) {
                            cells[row][col].setForeground(Color.RED);
                        }
                    }
                }
            }
        }

    }

    //method to save the current game state to a file
    public void saveGame() {
        updateBoardFromUI();
        timer.stop();
        this.dataCollector = new DataCollector(
                this.currentBoard,
                this.generator.getDifficultyLevel(),
                this.hintsUsed,
                this.timer.formatElapsedTime(),
                this.solved
        );
        dataCollector.storeData();
    }

    //display a hint to the user
    private void getHint() {
        updateBoardFromUI();
        if (this.solved) {
            JOptionPane.showMessageDialog(frame, "The puzzle is already solved!");
            return;
        }
        this.generator.setCurrentBoard(this.currentBoard);
        this.generator.getHint();
        this.hintsUsed++;
        displayBoard(this.currentBoard);
    }

    //solve the board and display the solution to the user
    private void solveBoard() {
        updateBoardFromUI();
        displayBoard(this.solvedBoard);
        this.solved = true;
        timer.stop();
        timer.reset();
    }

    //clear all user entries and reset to original unsolved board
    private void clearBoard() {
        this.currentBoard = this.unsolvedBoard.getCopyOfSudokuBoard();
        displayBoard(this.currentBoard);
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

    private Integer[] getTopTenTimes(){
        String[] times = dataCollector.fetchAllTimeTakenValues();

        PriorityQueue<Integer> topTen = new PriorityQueue<>();
        for (String time : times) {
            int totalSeconds = parseTime(time);
            topTen.offer(totalSeconds);
            if (topTen.size() > 10) {
                topTen.poll(); // remove smallest
            }
        }
        Integer[] bestTimes = topTen.toArray(new Integer[0]);
        java.util.Arrays.sort(bestTimes);
        return bestTimes;
    }

    private int parseTime(String time) {
        String[] parts = time.split(":");
        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        return minutes * 60 + seconds;
    }

}//end of SudokuUI



