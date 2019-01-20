package src;

import java.util.List;
/**
 * Sudoku class encapulates the Sudoku board with
 * any given board state.
 *
 * @author Chidozie Onyeze
 * @version 1.0
 */
class Sudoku {
    private int[][] board;
    private static int size = 9;

    public Sudoku(List<SudokuEntry> fixedEntries) {
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
        for (SudokuEntry entry : fixedEntries) {
            board[entry.getXPosition()][entry.getYPosition()] = entry.getValue();
        }
    }

    public Sudoku(int[][] board) {
        this.board = board;
    }

    public Sudoku() {
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }
    public int[][] getBoard() {
        return  board;
    }

    public Sudoku cloneBoard(){
        int[][] boardClone = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        return new Sudoku(boardClone);
    }

    @Override
    public String toString() {
        String outString = "";

        for (int i = 0; i < size; i++) {
            outString += "|";
            for (int j = 0; j < size; j++) {
                outString += Integer.toString(board[i][j]) + "|";
            }
            outString += "\n";

        }

        return outString;
    }

    public static void main(String[] args) {
        System.out.print((new Sudoku()).toString());
    }
}
