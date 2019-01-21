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
        entropy /= 2;
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

        int p = xPosition / 3;
        int q = yPosition / 3;
        p *= 3;
        q *= 3;

        for (int i = p; i < p + 3; i++) {
            for (int j = q; j < q + 3; j++) {
                if(board[i][yPosition] == board[xPosition][yPosition] && i != xPosition && j != yPosition) {
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
}
