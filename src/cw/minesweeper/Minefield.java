package cw.minesweeper;
/**
 *
 * @author ak581
 */
public class Minefield 
{
    // Check that there are fields to hold the current state of the minefield
    private MineTile[][] tiles;
    private final int rows, columns, numMines;
    /**
     * Initialize the fields, and then populate the minefield
     *
     * @param rows
     * @param columns
     * @param numMines
     */
    public Minefield(int rows, int columns, int numMines) 
    {
        this.rows = rows;
        this.columns = columns;
        this.numMines = numMines;
        // Note that these could be rows+2, columns+2 if edge boundaries are
        // used
        this.tiles = new MineTile[rows][columns];
        for(int i = 0; i < rows; i++) {
            for(int n = 0; n < columns; n++) {
                tiles[i][n] = new MineTile();
            }
        }
    }

    public boolean mineTile(int row, int column) 
    {
        if (tiles[row][column].isMined()) {
            return false;
        } else {
            tiles[row][column].mine();
        }
        // There are a number of ways of doing this.  I've chosen to precalculate
        // the start and end points of the for loops
        // However, these checks could be internal to the for loops
        // or the arrays could have unused boundary row and columns
        int startRow, endRow, startColumn, endColumn;
        if (row == 0) {
            startRow = 0;
        } else {
            startRow = row - 1;
        }
        if (column == 0) {
            startColumn = 0;
        } else {
            startColumn = column - 1;
        }
        if (row == this.rows - 1) {
            endRow = row;
        } else {
            endRow = row + 1;
        }
        if (column == this.columns - 1) {
            endColumn = column;
        } else {
            endColumn = column + 1;
        }
        for (int i = startRow; i <= endRow; i++) {
            for (int j = startColumn; j <= endColumn; j++) {
                tiles[i][j].addMinedNeighbours();
            }
        }
        return true;
    }   
    
    public int getRows() 
    {
        return rows;
    }

    public int getColumns() 
    {
        return columns;
    }
    
    public int getMines()
    {
        return numMines;
    }
    
    /**
     * A simple loop creating random coordinates, mining the tile until the
     * required number of mines have been laid
     */
    public void populate() 
    {
        int created = 0;
        while (created < this.numMines) {
            int row = (int) (Math.random() * this.rows);
            int col = (int) (Math.random() * this.columns);
            if (!tiles[row][col].isMined() && !(row == 0 && col == 0)) {
                mineTile(row, col);
                created++;
            }

        }
    }
    
    /**
     * Provide the grid representation
     *
     * @return Stars for mines, ints for adjacent
     */
    /*@Override
    public String toString() 
    {
        String s = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[i][j].isMined()) {
                    s += "* ";
                } else {
                    s += tiles[i][j].getNeighbours() + " ";
                }
            }
            s += "\n";
        }
        return s;
    }
    
    public String currentGrid() 
    {
        String s = "";
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[i][j].isRevealed()) {
                    s += tiles[i][j].getNeighbours() + " ";
                } else if(tiles[i][j].isMarked()) {
                    s += "F ";
                } else {
                    s += "▋ ";
                }
            }
            s += "\n";
        }
        return s;
    }*/
    
    public void markTile(int row, int column) 
    {
        tiles[row][column].mark();
    }
    
    public boolean isMarked(int row, int column)
    {
        return tiles[row][column].isMarked();
    }
    
    public boolean isMined(int row, int column)
    {
        return tiles[row][column].isMined();
    }
    
    public int getNeighbours(int row, int column)
    {        
        return tiles[row][column].getNeighbours();
    }
    
    public boolean isRevealed(int row, int column)
    {
        return tiles[row][column].isRevealed();
    }
    
    public boolean step(int row, int column)
    {
        
        if(!tiles[row][column].isMined()) {
            if(tiles[row][column].getNeighbours() == 0) {
                tiles[row][column].reveal();
                for(int i = row - 1; i <= row + 1; i++) {
                    for(int j = column - 1; j <= column + 1; j++) {
                        if(i >= 0 && i < rows && j >= 0 && j < columns) {    
                            if(!tiles[i][j].isRevealed() && !isMarked(i, j)) {
                                step(i, j);
                            }
                        }
                    }
                }
            } else {
                tiles[row][column].reveal();
            }            
            return true;
        } else {
            //System.out.println("You hit a mined tile! Game over!");
            //System.out.println(toString());
            return false;
        }        
    }
    
    public boolean areAllMinesRevealed()
    {
        int minesMarked = 0;
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                if(tiles[i][j].isMarked() && tiles[i][j].isMined()) {
                    minesMarked++;
                }
            }
        }
        if(minesMarked == numMines) {
            //System.out.println("All mines revealed!");
        }
        return minesMarked == numMines;
    }    
}
