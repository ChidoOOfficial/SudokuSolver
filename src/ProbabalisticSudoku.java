package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sudoku class encapsulates the Sudoku board with
 * any given board state and is calculates the entropy of the board for
 * the probabilistic solving algorithm
 *
 * @author Chidozie Onyeze
 * @version 1.0
 */
public class ProbabalisticSudoku extends Sudoku {
    private int entropy;
    private boolean[][] fixed;
    Random rand = new Random();

    /**
     * Constructor for making a board with known elements
     * and making all unknown elements empty
     *
     * @param fixedEntries list of the known elements
     */
    public ProbabalisticSudoku(List<SudokuEntry> fixedEntries) {
        super(fixedEntries);

        fixed = new boolean[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                fixed[i][j] = board[i][j] != 0;
            }
        }

        fillBoard();
        calculateBoardEntropy();
    }

    /**
     * Constructor for making a board from a known array
     * of integers
     *
     * @param board array of integers to be converted to a Sudoku board
     */
    public ProbabalisticSudoku(int[][] board) {
        super(board);

        fixed = new boolean[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                fixed[i][j] = board[i][j] != 0;
            }
        }
        fillBoard();
        calculateBoardEntropy();
    }

    /**
     * Constructor for making a new empty board
     *
     */
    public ProbabalisticSudoku() {
        super();

        fixed = new boolean[size][size];
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                fixed[i][j] = board[i][j] != 0;
            }
        }

        fillBoard();
        calculateBoardEntropy();
    }

    /**
     * Calculated the total number of violation on the board
     * (ie. the entropy) and sets the entropy variable
     */
    private void calculateBoardEntropy(){
        entropy = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                entropy += calculateEntropy(i,j);
            }
        }
    }

    /**
     * Calculate the number of violation caused by any given square
     *
     * @param xPosition the x position of the square to check
     * @param yPosition the y position of the square to check
     * @return the number of violations
     */
    private int calculateEntropy(int xPosition, int yPosition) {
        int sumEntropy = 0;
        for (int i = 0; i < size; i++) {
            if(board[xPosition][i] == board[xPosition][yPosition] && i != yPosition) {
                sumEntropy++;
            }
            if(board[i][yPosition] == board[xPosition][yPosition] && i != xPosition) {
                sumEntropy++;
            }
        }

        int p = (int)(((int)(xPosition/Math.sqrt(size))) * Math.sqrt(size));
        int q = (int)(((int)(yPosition/Math.sqrt(size))) * Math.sqrt(size));


        for (int i = p; i < p + Math.sqrt(size); i++) {
            for (int j = q; j < q + Math.sqrt(size); j++){
                if(board[i][j] == board[xPosition][yPosition] && i != xPosition && j != yPosition) {
                    sumEntropy++;
                }
            }
        }
        return sumEntropy;
    }

    /**
     * Getter for entropy value
     * @return entropy value
     */
    public int getEntropy() {
        return entropy;
    }

    @Override
    public ProbabalisticSudoku cloneSudoku() {
        int[][] boardClone = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        return new ProbabalisticSudoku(boardClone);
    }

    /**
     * Fill the board with number such that there are the correct number
     * of each number in the board
     */
    private void fillBoard() {
        List<Integer> availableValues = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            for (int j = 0; j < size; j++) {
                availableValues.add(i);
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] != 0) {
                    availableValues.remove(availableValues.indexOf(board[i][j]));
                }
            }
        }
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == 0) {
                    if(possibilityList[i][j].size() > 0) {
                        int value = rand.nextInt(possibilityList[i][j].size());
                        board[i][j] = possibilityList[i][j].get(value);
                        availableValues.remove(availableValues.indexOf(possibilityList[i][j].get(value)));
                        updatePossibilitySpace(i, j);
                        updatePossibilitySpaceAround(i, j);
                    }
                }
            }
        }

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == 0) {
                    int value = rand.nextInt(availableValues.size());
                    board[i][j] = availableValues.get(value);
                    availableValues.remove(availableValues.indexOf(availableValues.get(value)));
                }
            }
        }
    }

    /**
     * Swap the contents of square 1 and square 2 and recalculate entropy
     *
     * @param x1Position x value of square 1
     * @param y1Position y value of square 1
     * @param x2Position x value of square 2
     * @param y2Position y value of square 2
     */
    public void swapSquares(int x1Position, int y1Position, int x2Position, int y2Position) {
        //Manage Updating Entropy Efficiently
        int entropyChange = 2 * calculateEntropy(x1Position, y1Position);
        entropyChange += 2 * calculateEntropy(x2Position, y2Position);
        int tmp = board[x1Position][y1Position];
        board[x1Position][y1Position] = board[x2Position][y2Position];
        board[x2Position][y2Position] = tmp;

        entropyChange -= 2 * calculateEntropy(x1Position, y1Position);
        entropyChange -= 2 * calculateEntropy(x2Position, y2Position);

        entropy -= entropyChange;
    }

    /**
     * Getter for isFixed for a given square
     * @param xPosition x value of the square
     * @param yPosition x value of the square
     * @return
     */
    public boolean isFixed(int xPosition, int yPosition) {
        return fixed[xPosition][yPosition];
    }

}
