package src;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ProbabalisticSudokuSolver {

    public static Sudoku SudokuSolver(List<SudokuEntry> fixedEntries) {
        ProbabalisticSudoku startingSudoku = new ProbabalisticSudoku(fixedEntries);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

    public static Sudoku SudokuSolver(int[][] sudoku) {
        ProbabalisticSudoku startingSudoku = new ProbabalisticSudoku(sudoku);
        SolverOutput solverOutput =  SolveSudoku(startingSudoku);
        return solverOutput.getSudoku();
    }

    private static SolverOutput SolveSudoku(ProbabalisticSudoku sudoku) {
        int minEntropy = sudoku.getEntropy();
        int initial = sudoku.getEntropy();

        Random rand = new Random();
        int x1Rand;
        int y1Rand;
        int x2Rand;
        int y2Rand;

        int currentEntopy;
        int entropyChange;
        int stringOfFail = 0;

        while(sudoku.getEntropy() > 0) {
            do {
                x1Rand = rand.nextInt(Sudoku.size);
                y1Rand = rand.nextInt(Sudoku.size);
            } while(sudoku.isFixed(x1Rand, y1Rand));

            do {
                x2Rand = rand.nextInt(Sudoku.size);
                y2Rand = rand.nextInt(Sudoku.size);
            } while(sudoku.isFixed(x2Rand, y2Rand));

            currentEntopy = sudoku.getEntropy();
            sudoku.swapSquares(x1Rand,y1Rand,x2Rand,y2Rand);
            entropyChange = sudoku.getEntropy() - currentEntopy;


            if(entropyChange > 0) {
                if(rand.nextDouble() > Math.exp(-2 * entropyChange/minEntropy)) {
                    sudoku.swapSquares(x1Rand,y1Rand,x2Rand,y2Rand);
                    //System.out.println(false);
                    stringOfFail++;
                } else stringOfFail = 0;
            } else stringOfFail = 0;

            System.out.println(stringOfFail);
            //System.out.println(sudoku.getEntropy());
            //System.out.println(minEntropy + "---" + initial);

            if(sudoku.getEntropy() < minEntropy) {
                minEntropy = sudoku.getEntropy();
            }
        }

        return new SolverOutput(true, sudoku);
    }

    public static void main(String[] args) {
        Sudoku.setSize(3);
        System.out.print(ProbabalisticSudokuSolver.SudokuSolver(new ArrayList<>()));

    }

    private static void Test1() {
        int size = 3;
        int[][] sudo = new int[size * size][size * size];
        for(int i = 0; i < size * size; i++) {
            for(int j = 0; j < size * size; j++) {
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
        Sudoku.setSize(3);
        System.out.print(ProbabalisticSudokuSolver.SudokuSolver(sudo));
    }
}
