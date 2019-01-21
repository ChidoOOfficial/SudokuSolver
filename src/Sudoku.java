package src;
import java.util.*;

/**
 * Sudoku class encapsulates the Sudoku board with
 * any given board state.
 *
 * @author Chidozie Onyeze
 * @version 1.0
 */
public class Sudoku {
    protected int[][] board;
    protected PossibilitySpace[][] possibilityList;
    protected boolean solvable;
    protected static int size = 16;

    /**
     * Set the size of the Sudokus to be created
     *
     * @param newSize The square root of the desired size of the Sudoku
     */
    public static void setSize(int newSize) {
        size = newSize * newSize;
    }

    /**
     * Encapsulation of the Possible Value that the Sudoku can take
     * at an index and still be valid
     *
     * @author Chidozie Onyeze
     * @version 1.0
     */
    protected class PossibilitySpace implements Iterable<Integer>{
        private List<Integer> possibilityList;

        /**
         * Constructor for a possibility space initializes
         * the backing array list
         *
         */
        PossibilitySpace() {
            possibilityList = new ArrayList<>();
        }

        /**
         * Constructor for a possibility space sets the backing
         * array list to be the input array
         *
         * @param possibilityList Array to be used as backing array list
         */
        PossibilitySpace(List<Integer> possibilityList) {
            this.possibilityList = possibilityList;
        }

        /**
         * Remove and element from the backing array list
         *
         * @param value Value to be removed
         */
        public void remove(int value) {
            possibilityList.remove((Integer)value);
        }

        /**
         * Check whether a given value is in the possibility space
         *
         * @param value Value to look for in the backing list
         * @return Whether the specified value is in the list
         */
        public boolean contains(int value) {
            return possibilityList.contains(value);
        }

        /**
         * Get the next element of the List
         *
         * @return Next element of the list
         */
        public int getNext() {
            return possibilityList.get(0);
        }

        /**
         * Get the element of the List at the specified index
         *
         * @param index index to get from
         * @return Next element of the list
         */
        public int get(int index) {
            return possibilityList.get(index);
        }
        @Override
        public Iterator<Integer> iterator() {
            return possibilityList.iterator();
        }

        /**
         * Size of the backing List
         *
         * @return Size of the backing list
         */
        public int size() {
            return possibilityList.size();
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

        updatePossibilitySpace();
        autoFillSudoku();
    }

    /**
     * Constructor for making a board from a known array
     * of integers
     *
     * @param board array of integers to be converted to a Sudoku board
     */
    public Sudoku(int[][] board) throws IllegalArgumentException {
        if(board[0].length != size || board.length != size) {
            throw new IllegalArgumentException("Dimension Mismatch");
        }

        this.board = board;
        possibilityList = new PossibilitySpace[size][size];

        updatePossibilitySpace();
        autoFillSudoku();
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

        updatePossibilitySpace();
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
     * Create a duplicate board object containing the
     * same data
     *
     * @return  Duplicate board object
     */
    public Sudoku cloneSudoku(){
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
    public boolean addNewEntry(SudokuEntry entry) throws IllegalArgumentException{

        int xPosition = entry.getXPosition();
        int yPosition = entry.getYPosition();
        if(board[xPosition][yPosition] != 0) {
            throw new IllegalArgumentException("The target square is already filled");
        } else if (possibilityList[xPosition][yPosition].contains(entry.getValue())) {
            board[xPosition][yPosition] = entry.getValue();
            updatePossibilitySpace(xPosition, yPosition);
            updatePossibilitySpaceAround(xPosition, yPosition);
            autoFillSudoku();
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds an entry that is known to be valid to a specified location
     *
     * @param entry object specifying the target location and target value
     */
    public void addValidEntry(SudokuEntry entry){
        int xPosition = entry.getXPosition();
        int yPosition = entry.getYPosition();
        board[xPosition][yPosition] = entry.getValue();
        updatePossibilitySpace(xPosition, yPosition);
        updatePossibilitySpaceAround(xPosition, yPosition);
        autoFillSudoku();
    }

    /**
     * Update the possibility space of squares in the same row, column or square
     * as the given index
     *
     * @param xPosition x value for specifying index
     * @param yPosition y value for specifying index
     */
    protected void updatePossibilitySpaceAround(int xPosition, int yPosition) {
        if(board[xPosition][yPosition] != 0) {
            for (int i = 0; i < size; i++) {
                possibilityList[xPosition][i].remove(board[xPosition][yPosition]);
                possibilityList[i][yPosition].remove(board[xPosition][yPosition]);
            }

            int p = (int)(((int)(xPosition/Math.sqrt(size))) * Math.sqrt(size));
            int q = (int)(((int)(yPosition/Math.sqrt(size))) * Math.sqrt(size));

            for (int i = p; i < p + Math.sqrt(size); i++) {
                for (int j = q; j < q + Math.sqrt(size); j++) {
                    possibilityList[i][j].remove(board[xPosition][yPosition]);
                }
            }
        }
    }

    /**
     * Update the possibility space of a given index
     *
     * @param xPosition x value for specifying index
     * @param yPosition y value for specifying index
     */
    protected void updatePossibilitySpace(int xPosition, int yPosition) {
        //Return if location is already filled
        if(board[xPosition][yPosition] != 0) {
            possibilityList[xPosition][yPosition] = new PossibilitySpace();
        } else {
            List<Integer> intList = new ArrayList<>(size);
            for (int i = 1; i <= size; i++) {
                intList.add(i);
            }

            for (int i = 0; i < size; i++) {
                intList.remove((Integer)board[xPosition][i]);
                intList.remove((Integer)board[i][yPosition]);
            }

            int p = (int)(((int)(xPosition/Math.sqrt(size))) * Math.sqrt(size));
            int q = (int)(((int)(yPosition/Math.sqrt(size))) * Math.sqrt(size));


            for (int i = p; i < p + Math.sqrt(size); i++) {
                for (int j = q; j < q + Math.sqrt(size); j++) {
                    intList.remove((Integer)board[i][j]);
                }
            }

            possibilityList[xPosition][yPosition] = new PossibilitySpace(intList);
        }
    }

    /**
     * Update the possibility space of all square on the board
     *
     */
    private void updatePossibilitySpace() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                updatePossibilitySpace(i, j);
            }
        }

        checkSolvable();
    }

    public void autoFillSudoku(){

        boolean changeMade;
        do{
            changeMade = false;
            for (int i = 0; i < size; i++) {
                for(int j = 0; j < size; j++){
                    if(possibilityList[i][j].size() == 1) {
                        board[i][j] = possibilityList[i][j].getNext();
                        updatePossibilitySpace(i,j);
                        updatePossibilitySpaceAround(i,j);
                        changeMade = true;
                        break;
                    }
                }
                if(changeMade) break;
            }
        }while(changeMade);
        checkSolvable();
    }

    public void checkSolvable() {
        solvable  = true;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if(board[i][j] == 0 && possibilityList[i][j].size() == 0) {
                    solvable = false;
                }
            }
        }
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

    public Iterable<Integer> getPossibilitySpaceIterable(int xPosition, int yPosition) {
        return ()-> possibilityList[xPosition][yPosition].iterator();
    }

    @Override
    public String toString() {
        String outString = "";
        String tmp = "%" + Integer.toString((int)Math.log10(size) + 1) + "d|";
        for (int i = 0; i < size; i++) {
            outString += "|";
            for (int j = 0; j < size; j++) {
                outString += String.format(tmp, board[i][j]);
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

    public boolean isSolvable() {
        return solvable;
    }

    public boolean isSolved() {
        if(isSolvable()) {
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++ ) {
                    if(board[i][j] == 0) return false;
                }
            }

            return true;
        } else {
            return false;
        }
    }


    //Test Methods
    public int possibilitySpaceSize(int i, int j) {
        for (int x : possibilityList[i][j]) {
            System.out.println(x);
        }
        return possibilityList[i][j].size();
    }
}