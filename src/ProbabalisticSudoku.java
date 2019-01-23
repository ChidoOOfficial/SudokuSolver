package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProbabalisticSudoku extends Sudoku {
    private int entropy;
    private boolean[][] fixed;
    Random rand = new Random();

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

    private void calculateBoardEntropy(){
        entropy = 0;
        for(int i = 0; i < size; i++) {
            for(int j = 0; j < size; j++) {
                entropy += calculateEntropy(i,j);
            }
        }
    }

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

    public boolean isFixed(int xPosition, int yPosition) {
        return fixed[xPosition][yPosition];
    }

}
