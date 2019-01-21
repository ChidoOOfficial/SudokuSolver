package src;

import java.util.List;

public class SudokuSolver {
    private static class SolverOutput {
        private boolean isSolved;
        private Sudoku sudoku;

        public SolverOutput(boolean isSolved) throws IllegalArgumentException{
            if(isSolved) {
                throw new IllegalArgumentException("You cannot pass no sudoku if sudoku was solved!");
            } else {
                this.isSolved = false;
                this.sudoku = null;
            }
        }

        public SolverOutput(boolean isSolved, Sudoku sudoku) {
            if(isSolved && sudoku == null) {
                throw new IllegalArgumentException("You cannot pass no sudoku if sudoku was solved!");
            } else {
                this.isSolved = isSolved;
                this.sudoku = sudoku;
            }
        }
    }

    public static Sudoku SudokuSolver(List<SudokuEntry> fixedEntries) {
        Sudoku startingSudoku = new Sudoku(fixedEntries);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.sudoku;
    }

    public static Sudoku SudokuSolver(int[][] sudoku) {
        Sudoku startingSudoku = new Sudoku(sudoku);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.sudoku;
    }

    private static SolverOutput SolveSudoku(Sudoku sudoku) {
        for(int i = 0; i < Sudoku.size; i++) {
            for(int j = 0; j < Sudoku.size; j++) {
                if(sudoku.isEmpty(i,j)) {
                    for (int possibleValue : sudoku.getPossibilitySpaceIterable(i,j)) {
                        Sudoku newSudoku = sudoku.cloneBoard();
                        newSudoku.addValidEntry(new SudokuEntry(i,j,possibleValue));
                        if(newSudoku.isSolved()) {
                            return new SolverOutput(true, newSudoku);
                        } else if(newSudoku.isSolvable()) {
                            SolverOutput possibleSolve = SolveSudoku(newSudoku);
                            if(possibleSolve.isSolved) {
                                return possibleSolve;
                            }
                        }
                    }
                    return new SolverOutput(false);
                }
            }
        }
        if(sudoku.isSolved()) {
            return new SolverOutput(true, sudoku);
        } else {
            return new SolverOutput(false);
        }
    }

    public static void main(String[] args) {
        int[][] sudo = new int[9][9];
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                sudo[i][j] = 0;
            }
        }
        sudo[0][0] = 3;
        sudo[0][2] = 9;
        sudo[0][8] = 6;
        sudo[1][2] = 5;
        sudo[1][3] = 9;
        sudo[1][4] = 3;
        sudo[1][6] = 7;
        sudo[2][2] = 7;
        sudo[2][3] = 4;
        sudo[2][4] = 2;
        sudo[3][0] = 8;
        sudo[3][1] = 1;
        sudo[3][2] = 3;
        sudo[3][3] = 5;
        sudo[3][5] = 2;
        sudo[3][6] = 6;
        sudo[4][2] = 6;
        sudo[4][4] = 8;
        sudo[4][5] = 4;
        sudo[5][4] = 9;
        sudo[5][5] = 3;
        sudo[5][7] = 7;
        sudo[6][0] = 6;
        sudo[6][1] = 5;
        sudo[6][2] = 1;
        sudo[6][5] = 9;
        sudo[6][7] = 2;
        sudo[6][8] = 8;
        sudo[7][0] = 9;
        sudo[7][1] = 7;
        sudo[7][2] = 2;
        sudo[7][3] = 3;
        sudo[7][8] = 5;
        sudo[8][4] = 6;
        sudo[8][6] = 1;
        sudo[8][8] = 7;
        System.out.print(SudokuSolver.SudokuSolver(sudo));
    }
}
