import java.util.Collections;
import java.util.*;

/**
 * The Sudoku number puzzle to be solved
 */
public class Puzzle {
    // All variables have package access
    // The numbers on the puzzle
    int[][] numbers = new int[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];
    // The clues - isGiven (no need to guess) or need to guess
    boolean[][] isGiven = new boolean[SudokuConstants.GRID_SIZE][SudokuConstants.GRID_SIZE];

    // Constructor
    public Puzzle() {
        super();
    }

    // Generate a new puzzle given the number of cells to be guessed, which can be used
    //  to control the difficulty level.
    // This method shall set (or update) the arrays numbers and isGiven
    public void newPuzzle(int cellsToGuess) {
        // I hardcode a puzzle here for illustration and testing.
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = 0;
                isGiven[row][col] = false;
            }
        }
        solutionSudoku();
        setGuesses(cellsToGuess);
        randomNumbers();
    }

    private boolean randomNumbers () {
        Stack<Integer> numStack = new Stack<>();
        for (int i = 1; i <= SudokuConstants.GRID_SIZE; i++) {
            numStack.push(i);
        }
        Collections.shuffle(numStack);
        return fillPuzzle(numStack);
    }

    private boolean fillPuzzle (Stack<Integer> numStack) {
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = 0;
            }
        }
        return fillPuzzleRec(0, 0, numStack);
    }
    private boolean fillPuzzleRec(int startRow, int startCol, Stack<Integer> numStack) {
        int row = startRow;
        int col = startCol;

        if (row == SudokuConstants.GRID_SIZE - 1 && col == SudokuConstants.GRID_SIZE) {
            return true;
        }
        if (col == SudokuConstants.GRID_SIZE){
            row++;
            col = 0;
        }
        for (int num : numStack){
            if (isValid(row, col, num)) {
                numbers[row][col] = num;
                if (fillPuzzleRec(row, col + 1, numStack)) {
                    return true;
                }
                numbers[row][col] = 0;
                }

            }
        return false;
        }

        private boolean isNumberValidInRow(int row, int num) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; col++) {
                if (numbers[row][col] == num) {
                    return false;
                }
            }
            return true;
        }

        private boolean isNumberValidInColumn(int col, int num) {
            for (int row = 0; row < SudokuConstants.GRID_SIZE; row++) {
                if (numbers[row][col] == num) {
                    return false;
                }
            }
            return true;
        }

    private boolean isNumberValidInSubgrid(int startRow, int startCol, int num) {
        for (int row = 0; row < SudokuConstants.SUBGRID_SIZE; row++) {
            for (int col = 0; col < SudokuConstants.SUBGRID_SIZE; col++) {
                if (numbers[startRow + row][startCol + col] == num) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValid(int row, int col, int num){
        return isNumberValidInRow(row, num) && isNumberValidInColumn(col, num)
                && isNumberValidInSubgrid(row - row % SudokuConstants.SUBGRID_SIZE, col - col % SudokuConstants.SUBGRID_SIZE, num);
            }
            private void solutionSudoku() {
                solution();
            }

            private boolean solution(){
        Stack<Cell> cellStack = new Stack<>();
        int curRow = 0; int curCol = 0; int curValue = 1;

        while (cellStack.size() < SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE){
            if (isGiven[curRow][curCol]) {
                cellStack.push(new Cell(curRow, curCol, numbers[curRow][curCol]));
                int[] next = getNextCell(curRow, curCol);
                curRow = next[0];
                curCol = next[1];
                continue;
            }

            boolean foundValidValue = false;
            while (curValue <= SudokuConstants.GRID_SIZE){
                if (isValid(curRow, curCol, curValue)) {
                    foundValidValue = true;
                    break;
                }
                curValue++;
            }

            if (foundValidValue && curValue <= SudokuConstants.GRID_SIZE){
                numbers[curRow][curCol] = curValue;
                cellStack.push(new Cell(curRow, curCol, curValue));
                int[] next = getNextCell(curRow, curCol);
                curRow = next[0];
                curCol = next[1];
                curValue = 1;
            } else {
                if (!cellStack.isEmpty()){
                    Cell cell = cellStack.pop();
                    while (isGiven[cell.getRow()][cell.getCol()]){
                        if(!cellStack.isEmpty()){
                            cell = cellStack.pop();
                        } else {
                            return false;
                        }
                    }
                    curRow = cell.getRow();
                    curCol = cell.getCol();
                    curValue = cell.getValue() +1;
                    numbers[curRow][curCol] = 0;
                } else {
                    return false;
                }
            }
        }
        return true;
    }

    private void setGuesses (int cellsToGuess) {
        int targetFilledCells = cellsToGuess + (SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE) / 2;
        Stack<Integer> indexes = new Stack<>();
        for (int i = 0; i < SudokuConstants.GRID_SIZE * SudokuConstants.GRID_SIZE; i++) {
            indexes.push(i);
        }
        Collections.shuffle(indexes);

        int filledCells = 0;
        while (!indexes.isEmpty() && filledCells < targetFilledCells) {
            int idx = indexes.pop();
            int row = idx / SudokuConstants.GRID_SIZE;
            int col = idx % SudokuConstants.GRID_SIZE;
            if (numbers[row][col] != 0) {
                isGiven[row][col] = true;
                filledCells++;
            }
        }
    }

    private int[] getNextCell (int row, int col) {
        int[] nextCell = new int[2];
        col++;
        if (col == SudokuConstants.GRID_SIZE) {
            col = 0;
            row++;
        }
        nextCell[0] = row;
        nextCell[1] = col;
        return nextCell;
    }
}

       /* int[][] hardcodedNumbers =
                {{5, 3, 4, 6, 7, 8, 9, 1, 2},
                        {6, 7, 2, 1, 9, 5, 3, 4, 8},
                        {1, 9, 8, 3, 4, 2, 5, 6, 7},
                        {8, 5, 9, 7, 6, 1, 4, 2, 3},
                        {4, 2, 6, 8, 5, 3, 7, 9, 1},
                        {7, 1, 3, 9, 2, 4, 8, 5, 6},
                        {9, 6, 1, 5, 3, 7, 2, 8, 4},
                        {2, 8, 7, 4, 1, 9, 6, 3, 5},
                        {3, 4, 5, 2, 8, 6, 1, 7, 9}};

        // Copy from hardcodedNumbers into the array "numbers"
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                numbers[row][col] = hardcodedNumbers[row][col];
            }
        }

        // Need to use input parameter cellsToGuess!
        // Hardcoded for testing, only 2 cells of "8" is NOT GIVEN
        boolean[][] hardcodedIsGiven =
                {{true, true, true, true, true, false, true, true, true},
                        {true, true, true, true, true, true, true, true, false},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true},
                        {true, true, true, true, true, true, true, true, true}};

        // Copy from hardcodedIsGiven into array "isGiven"
        for (int row = 0; row < SudokuConstants.GRID_SIZE; ++row) {
            for (int col = 0; col < SudokuConstants.GRID_SIZE; ++col) {
                isGiven[row][col] = hardcodedIsGiven[row][col];
            }
        } */
