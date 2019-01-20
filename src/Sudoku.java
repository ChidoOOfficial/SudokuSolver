package src;
import java.util.ArrayList;
import java.util.Iterator;
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
    private PossibilitySpace[][] possibilityList;
    private static int size = 9;

    private class PossibilitySpace implements Iterable<Integer>{
        private List<Integer> possibilityList;

        public  PossibilitySpace() {
            possibilityList = new ArrayList<>();
        }

        public PossibilitySpace(List<Integer> possibilityList) {
            this.possibilityList = possibilityList;
        }

        public void remove(int value) {
            possibilityList.remove(value);
        }

        public boolean contains(int value) {
            return possibilityList.contains(value);
        }

        @Override
        public Iterator<Integer> iterator() {
            return possibilityList.iterator();
        }
    }

    //Constructors
    /**
     * Constructor for making a board with known elements
     * and making all unknown elements empty
     *
     * @param fixedEntries list of the known elements
     */
    public Sudoku(List<SudokuEntry> fixedEntries) {
        board = new int[size][size];
        possibilityList = new PossibilitySpace[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
        for (SudokuEntry entry : fixedEntries) {
            board[entry.getXPosition()][entry.getYPosition()] = entry.getValue();
        }
    }

    /**
     * Constructor for making a board from a known array
     * of integers
     *
     * @param board array of integers to be converted to a Sudoku board
     */
    public Sudoku(int[][] board) {
        this.board = board;
        possibilityList = new PossibilitySpace[size][size];
    }

    /**
     * Constructor for making a new empty board
     *
     */
    public Sudoku() {
        possibilityList = new PossibilitySpace[size][size];
        board = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = 0;
            }
        }
    }

    //Public Methods
    /**
     * Get the backing array for the board
     *
     * @return  Array of integers backing the board
     */
    public int[][] getBoard() {
        return  board;
    }

    /**
     * Create a duplicate board object containg the
     * same data
     *
     * @return  Duplicate board object
     */
    public Sudoku cloneBoard(){
        int[][] boardClone = new int[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                boardClone[i][j] = board[i][j];
            }
        }
        return new Sudoku(boardClone);
    }

    /**
     * Adds an entry to a specified location and checks if the
     * board is still valid
     *
     * @param entry object specifying the target location and target value
     *
     * @return  true or false depending on if the desired entry is empty
     */
    public boolean addEntry(SudokuEntry entry){
        int xPosition = entry.getXPosition();
        int yPosition = entry.getYPosition();
        int currentValue = board[xPosition][yPosition];
        board[xPosition][yPosition] = entry.getValue();

        if (CheckValidity(xPosition,yPosition)) {
            updateProbabilitySpace(xPosition, yPosition);
            updateProbabilitySpaceAround(xPosition, yPosition);
            return true;
        } else {
            board[xPosition][yPosition] = currentValue;
            return false;
        }
    }

    private void updateProbabilitySpaceAround(int xPosition, int yPosition) {
        if(board[xPosition][yPosition] != 0) {
            for (int i = 0; i < size; i++) {
                possibilityList[xPosition][i].remove(board[xPosition][yPosition]);
                possibilityList[i][yPosition].remove(board[xPosition][yPosition]);
            }

            int p = xPosition / 3;
            int q = yPosition / 3;

            for (int i = p; i < p + 3; i++) {
                for (int j = q; j < q + 3; j++) {
                    possibilityList[i][j].remove(board[xPosition][yPosition]);
                }
            }
        }
    }

    private void updateProbabilitySpace(int xPosition, int yPosition) {
        //Return if location is already filled
        if(board[xPosition][yPosition] == 0) {
            List<Integer> intList = new ArrayList<>(9);
            for (int i = 1; i < 10; i++) {
                intList.add(i);
            }

            for (int i = 0; i < size; i++) {
                intList.remove(board[xPosition][i]);
                intList.remove(board[i][yPosition]);
            }

            int p = xPosition / 3;
            int q = yPosition / 3;

            for (int i = p; i < p + 3; i++) {
                for (int j = q; j < q + 3; j++) {
                    intList.remove(board[i][j]);
                }
            }

            possibilityList[xPosition][yPosition] = new PossibilitySpace(intList);
        }
    }

    private void updateProbabilitySpace() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                updateProbabilitySpace(i, j);
            }
        }
    }

    private boolean CheckValidity(int xPosition, int yPosition) {
        if(board[xPosition][yPosition] == 0) {
            return true;
        } else {
            for (int i = 0; i < size; i++) {
                if (i != xPosition) {
                    if (board[i][yPosition] == board[xPosition][yPosition]) {
                        return false;
                    }
                }

                if (i != yPosition) {
                    if (board[xPosition][i] == board[xPosition][yPosition]) {
                        return false;
                    }
                }
            }

            int p = xPosition / 3;
            int q = yPosition / 3;

            for (int i = p; i < p + 3; i++) {
                for (int j = q; j < q + 3; j++) {
                    if (i != xPosition || j != yPosition) {
                        if (board[i][j] == board[xPosition][yPosition]) {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }

    private boolean CheckValidity() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if( !CheckValidity(i, j)) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check whether a specific location of the board
     * is empty
     *
     * @param i First index of location
     * @param j Second index of Location
     *
     * @return  True or False depending on if the desired entry is empty
     */
    public boolean isEmpty(int i, int j) {
        return board[i][j] == 0;
    }

    public Iterator<Integer> getPossibilitySpaceIterator(int xPosition, int yPosition) {
        return possibilityList[xPosition][yPosition].iterator();
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

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        else if(!(other instanceof  Sudoku)) return false;
        else {
            boolean isEqual = true;
            int[][] otherBoard = ((Sudoku) other).getBoard();
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if(this.board[i][j] != otherBoard[i][j]) isEqual = false;
                }
            }
            return isEqual;
        }
    }

    public static void main(String[] args) {
        System.out.print(Integer.toString(6/3));
    }
}


