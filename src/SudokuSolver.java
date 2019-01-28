package src;

import java.util.List;

public abstract class SudokuSolver {

    /**
     * Solver Sudoku method for utilizing the (DFS) sudoku
     * solving algorithm
     *
     * @param fixedEntries List of fixed entried
     * @return Solved Sudoku
     */
    public static Sudoku SudokuSolver(List<SudokuEntry> fixedEntries) {
        Sudoku startingSudoku = new Sudoku(fixedEntries);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

    /**
     * Solver Sudoku method for utilizing the (DFS) sudoku
     * solving algorithm
     *
     * @param sudoku array representing sudoku
     * @return Solved Sudoku
     */
    public static Sudoku SudokuSolver(int[][] sudoku) {
        Sudoku startingSudoku = new Sudoku(sudoku);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

    /**
     * Method that implements the algorithm
     *
     * @param sudoku Input Sudoku to solve
     * @return Output from the Solver
     */
    private static SolverOutput SolveSudoku(Sudoku sudoku) {
        for(int i = 0; i < Sudoku.size; i++) {
            for(int j = 0; j < Sudoku.size; j++) {
                if(sudoku.isEmpty(i,j)) {
                    for (int possibleValue : sudoku.getPossibilitySpaceIterable(i,j)) {
                        Sudoku newSudoku = sudoku.cloneSudoku();
                        newSudoku.addValidEntry(new SudokuEntry(i,j,possibleValue));
                        if(newSudoku.isSolved()) {
                            return new SolverOutput(true, newSudoku);
                        } else if(newSudoku.isSolvable()) {
                            SolverOutput possibleSolve = SolveSudoku(newSudoku);
                            if(possibleSolve.isSolved()) {
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

}
