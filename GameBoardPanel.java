
/**
 * ES234317-Algorithm and Data Structures
 * Semester Ganjil, 2024/2025
 * Group Capstone Project
 * Group #14
 * 1 - 5026231009 - Bernadetta Graciela
 * 2 - 5026231021 - Zaskia Muazatun Mahmud
 * 3 - 5026231080 - Binar Faisha Wijdan
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class GameBoardPanel extends JPanel {
    private static final long serialVersionUID = 1L; // to prevent serial warning

    // Define named constants for UI sizes
    public static final int CELL_SIZE = 60; // Cell width/height in pixels
    public static final int BOARD_WIDTH = CELL_SIZE * SudokuConstants.GRID_SIZE;
    public static final int BOARD_HEIGHT = CELL_SIZE * SudokuConstants.GRID_SIZE;
    // Board width/height in pixels

    // Define properties
    /** The game board composes of 9x9 Cells (customized JTextFields) */
    private Cell[][] cells = new Cell[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    /** It also contains a Puzzle with array numbers and isGiven */
    private Puzzle puzzle ;

    /** Constructor */
    public GameBoardPanel() {
        super.setLayout(new GridLayout(SudokuConstants.GRID_SIZE, SudokuConstants.GRID_SIZE)); // JPanel

        // Allocate the 2D array of Cell, and added into JPanel.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col] = new Cell(row, col);
                super.add(cells[row][col]); // JPanel
            }
        }

        // [TODO 3] Allocate a common listener as the ActionEvent listener for all the
        // Cells (JTextFields)
        CellInputListener listener = new CellInputListener();

        // [TODO 4] Adds this common listener to all editable cells
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].isEditable()) {
                    cells[row][col].addActionListener(listener); // For all editable rows and cols
                }
            }
        }
        super.setPreferredSize(new Dimension(BOARD_WIDTH, BOARD_HEIGHT));
    }

    /**
     * Generate a new puzzle; and reset the game board of cells based on the puzzle.
     * You can call this method to start a new game.
     * @param medium 
     */
    public void newGame() {
        // Generate a new puzzle
        puzzle = new Puzzle();
        puzzle.newPuzzle(Puzzle.DifficultyLevel.MEDIUM);

        // Initialize all the 9x9 cells, based on the puzzle.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                cells[row][col].newGame(puzzle.numbers[row][col], puzzle.isGiven[row][col]);
            }
        }
    }

    /**
     * Return true if the puzzle is solved
     * i.e., none of the cell have status of TO_GUESS or WRONG_GUESS
     */
    public boolean isSolved() {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                if (cells[row][col].status == CellStatus.TO_GUESS || cells[row][col].status == CellStatus.WRONG_GUESS) {
                    return false;
                }
            }
        }
        return true;
    }

    // [TODO 2] Define a Listener Inner Class for all the editable Cells
    private class CellInputListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Get a reference of the JTextField that triggers this action event
            Cell sourceCell = (Cell) e.getSource();
            String textEntered = sourceCell.getText();
            
            try {
                // Attempt to parse the input as an integer
                int numberIn = Integer.parseInt(textEntered);
    
                // Check if the number is within the valid range (1-9)
                if (numberIn < 1 || numberIn > 9) {
                    JOptionPane.showMessageDialog(null, 
                        "Please enter a valid number between 1 and 9", 
                        "Invalid Input", 
                        JOptionPane.WARNING_MESSAGE);
                    sourceCell.setText(""); // Clear the invalid input
                    return; // Stop further processing
                }
    
                // Debugging output
                System.out.println("You entered " + numberIn);
    
                // Check the number against sourceCell.number
                if (numberIn == sourceCell.number) {
                    sourceCell.status = CellStatus.CORRECT_GUESS;
                } else {
                    sourceCell.status = CellStatus.WRONG_GUESS;
                }
                sourceCell.paint(); // Repaint the cell based on its status
            } catch (NumberFormatException ex) {
                // Handle non-integer inputs
                JOptionPane.showMessageDialog(null, 
                    "Please enter a valid number between 1 and 9", 
                    "Invalid Input", 
                    JOptionPane.WARNING_MESSAGE);
                sourceCell.setText(""); // Clear the invalid input
                return; // Stop further processing
            }
                
            /*
            * [TODO 6] (later)
            * Check if the player has solved the puzzle after this move,
            * by calling isSolved(). Put up a congratulation JOptionPane, if so.
            */
            if (isSolved())
            JOptionPane.showMessageDialog(null, "Congratulation!");
        }
    }
}
