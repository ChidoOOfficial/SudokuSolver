package src;
/**
 * SudokuEntry class is the class of objects taken in the
 * Sudoku class for filling out the Sudoku board.
 *
 * @author Chidozie Onyeze
 * @version 1.0
 */
class SudokuEntry {
    private int xPosition;
    private int yPosition;
    private int value;

    public SudokuEntry(int xPosition, int yPosition, int value) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.value = value;
    }

    public int[] getPosition() {
        return new int[]{xPosition, yPosition};
    }

    public int getValue() {
        return value;
    }
}
