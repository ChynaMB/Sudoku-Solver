import javax.swing.*;
import java.awt.*;

public class SudokuUI {
    private JFrame frame;
    private JTextField[][] cells = new JTextField[9][9];

    private SudokuBoard board;
    private SudokuGenerator generator;
    private SudokuSolver solver;
    private Timer timer;
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
        JButton loadGameButton = new JButton("Previous Games");

        menuPanel.add(newEasyGameButton);
        menuPanel.add(newMediumGameButton);
        menuPanel.add(newHardGameButton);
        menuPanel.add(loadGameButton);

        frame.add(menuPanel, BorderLayout.NORTH);
        frame.setSize(600, 600);
        frame.setVisible(true);

        //action listeners for buttons
        newEasyGameButton.addActionListener(e -> startNewGame("easy"));
        newMediumGameButton.addActionListener(e -> startNewGame("medium"));
        newHardGameButton.addActionListener(e -> startNewGame("hard"));
        loadGameButton.addActionListener(e -> loadPreviousGames());

    }

    //method to display the Sudoku board UI
    public void startNewGame(JFrame startMenu, String difficulty) {
        generator = new SudokuGenerator(difficulty);
        board = generator.getUnsolvedBoard();
        solver = new SudokuSolver(board);
        timer = new Timer();

        frame = new JFrame("Sudoku Game - " + generator.getDifficultyLevel() + " Level");
        openNextFrame(startMenu, frame);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel boardPanel = new JPanel(new GridLayout(9, 9));
        Font font = new Font("Arial", Font.BOLD, 20);

        for (int row = 0; row < 9; row++) {
            for (int col = 0; col < 9; col++) {
                JTextField cell = new JTextField();
                cell.setHorizontalAlignment(JTextField.CENTER);
                cell.setFont(font);
                cells[row][col] = cell;

                // Thicker borders for 3x3 sub-grids
                int top = (row % 3 == 0) ? 3 : 1;
                int left = (col % 3 == 0) ? 3 : 1;
                int bottom = (row == 8) ? 3 : 1;
                int right = (col == 8) ? 3 : 1;

                cell.setBorder(BorderFactory.createMatteBorder(top, left, bottom, right, Color.BLACK));
                boardPanel.add(cell);
            }
        }

        JPanel buttonPanel = new JPanel();
        JButton backToStartMenuBtn = new JButton("Back To Start Menu");
        JButton clearBtn = new JButton("Clear");
        JButton checkBtn = new JButton("Check Solution");
        JButton hintBtn = new JButton("Get Hint");

        buttonPanel.add(backToStartMenuBtn);
        buttonPanel.add(clearBtn);
        buttonPanel.add(checkBtn);
        buttonPanel.add(hintBtn);
    }

    //method to close current frame and open a new one
    public void openNextFrame(JFrame currentFrame, JFrame nextFrame) {
        currentFrame.dispose();
        nextFrame.setVisible(true);
    }

}//end of SudokuUI



