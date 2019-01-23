package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SudokuSolver {

    public static Sudoku SudokuSolver(List<SudokuEntry> fixedEntries) {
        Sudoku startingSudoku = new Sudoku(fixedEntries);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

    public static Sudoku SudokuSolver(int[][] sudoku) {
        Sudoku startingSudoku = new Sudoku(sudoku);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

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
