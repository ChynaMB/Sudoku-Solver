# Sudoku Generator

## Overview

Sudoku Generator is a Java-based Sudoku generator and solver that uses both logical techniques and backtracking algorithms to solve puzzles. The project features a graphical user interface (GUI) built with Java Swing, allowing users to interactively play and solve Sudoku puzzles.


## Features

- **Puzzle Generation**: Generates new Sudoku puzzles of varying difficulty levels.
- **Sudoku Solver**: Solves Sudoku puzzles using a combination of logic and backtracking algorithms.
- **Graphical User Interface**: Interactive GUI built with Java Swing for an engaging user experience.
- **Timer & Hint System**: Tracks solving time and provides hints to assist players.
- **Input Validation**: Ensures only valid numbers (1–9) are entered into the puzzle.
- **Data Storage**: Saves game progress, hints used, and solve times to CSV files, enabling users to track performance and view top scores.  

## Installation

1. **Clone the Repository:**

  ```bash
  git clone https://github.com/ChynaMB/Sudoku-Generator.git
  ```
   
2. **Navigate to the Project Directory:**

  ```bash
  cd Sudoku-Generator
  ```

3. **Compile the Java Files:**
  ```bash
  javac -d bin src/*.java
  ```
4. **Run the Application:**

  ```bash
  java -cp bin SudokuUI
  ```

## Usage
- **Select Difficulty**: The start menu will have an easy, medium and hard option. Click the button of your choice
- **Start a New Game**: Click on "Start Game" to begin a new puzzle.
- **Input Numbers**: Click on a cell and enter a number between 1 and 9.
- **Use Hints**: Click on "Get Hint" to receive a suggestion for an empty cell.
- **Check Solution**: Click on "Check Solution" to verify if the puzzle is solved correctly.
- **Clear Entries**: Click on "Clear All Entries" to reset the board.
- **Quit Game**: Click on "Quit" to exit the game.
- **Fastest Times**: Click "Fastest Solve Times" to see the previous top 10 fastest sudoku solving times 

## Development
- Java Version: 1.8 or higher
- IDE Recommendations: IntelliJ IDEA, Eclipse, or Visual Studio Code
- Libraries Used: Java Swing for GUI components

## Contributing
Contributions are welcome!
