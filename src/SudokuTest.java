package src;

import org.junit.Test;
import org.junit.Before;
import org.junit.FixMethodOrder;

import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SudokuTest {
    private Sudoku blankSudoku, filledSudoku, filledSudokuGood;
    private ProbabalisticSudoku probSudoku;
    private static final int TIMEOUT = 200;
    private static int testsPassed = 0;
    private static int exceptionsFailed = 0;

    @Before
    public void setUp() {
        probSudoku = new ProbabalisticSudoku();
        List<SudokuEntry> fixedEntries = new ArrayList<SudokuEntry>();

        for (int i = 0; i < Sudoku.size; i++) {
            fixedEntries.add(new SudokuEntry(i,i,i));
        }
        fixedEntries.add(new SudokuEntry(5,4,2));
        fixedEntries.add(new SudokuEntry(4,1,9));
        fixedEntries.add(new SudokuEntry(8,2,3));

        filledSudoku = new Sudoku(fixedEntries);
        blankSudoku = new Sudoku();

        fixedEntries = new ArrayList<SudokuEntry>();
        for (int i = 0; i < Sudoku.size - 1; i++) {
            fixedEntries.add(new SudokuEntry(i,0,i + 1));
        }

        filledSudokuGood = new Sudoku(fixedEntries);
    }

    @Test(timeout = TIMEOUT)
    public void testInitialisation() {
        int[][] board = new int[9][9];
        for (int i = 0; i < Sudoku.size; i++) {
            for (int j = 0; j < Sudoku.size; j++) {
                board[i][j] = 0;
            }
        }

        assertArrayEquals(board, blankSudoku.getBoard());

        for (int i = 0; i < Sudoku.size; i++) {
            board[i][i]= i;
        }

        board[5][4] = 2;
        board[4][1] = 9;
        board[8][2] = 3;

        assertArrayEquals(board, filledSudoku.getBoard());
    }

    @Test(timeout = TIMEOUT)
    public void testClone() {
        assertNotSame(filledSudoku.cloneSudoku().getBoard(),filledSudoku.getBoard() );
        assertTrue(filledSudoku.cloneSudoku().equals(filledSudoku));
        assertFalse(filledSudoku.cloneSudoku()==(filledSudoku));
    }

    @Test(timeout = TIMEOUT)
    public void testAddEntry() {
        List<SudokuEntry> fixedEntries = new ArrayList<SudokuEntry>();

        for (int i = 0; i < Sudoku.size; i++) {
            fixedEntries.add(new SudokuEntry(i,i,i));
        }
        fixedEntries.add(new SudokuEntry(5,4,2));
        fixedEntries.add(new SudokuEntry(4,1,9));

        Sudoku newSudoku = new Sudoku(fixedEntries);

        newSudoku.addNewEntry(new SudokuEntry(8,2,3));
        assertEquals(newSudoku, filledSudoku);
    }

    @Test(timeout = TIMEOUT)
    public void testSolvable(){
        List<SudokuEntry> fixedEntries = new ArrayList<SudokuEntry>();
        for (int i = 0; i < 5; i++) {
            fixedEntries.add(new SudokuEntry(i,0,i + 1));
        }

        fixedEntries.add(new SudokuEntry(8,4,6));
        fixedEntries.add(new SudokuEntry(8,7,8));
        fixedEntries.add(new SudokuEntry(7,2,9));

        Sudoku sudo = new Sudoku(fixedEntries);
        assertEquals(0,sudo.possibilitySpaceSize(8,0));
        assertTrue(sudo.isSolvable());
    }

    @Test(timeout = TIMEOUT)
    public void testSolver(){
        assertNotEquals(null, SudokuSolver.SudokuSolver(new ArrayList<>()).toString());
    }

    @Test(timeout = TIMEOUT)
    public void probabalisticSudokuTest() {

    }
}