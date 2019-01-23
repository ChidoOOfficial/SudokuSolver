package src;

public class SolverOutput {
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

    public Sudoku getSudoku() {
        return sudoku;
    }

    public boolean isSolved() {
        return isSolved;
    }
}