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
                }
            }
        }
        return new SolverOutput(false);
    }
}
