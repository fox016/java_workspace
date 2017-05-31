package minesweeper;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class Board
{
	private Cell[][] cells;
	
	private final int rows;
	private final int cols;
	private final int bombTotal;
	
	private boolean gameOver;
	private int flagTotal;
	
	/**
	 * Constructor
	 * 
	 * @param rows
	 * @param cols
	 * @param bombCount
	 */
	public Board(int rows, int cols, int bombCount)
	{
		if(rows < 5 || rows > 50)
			throw new IllegalArgumentException("Row count must be 5-50");
		if(cols < 5 || cols > 50)
			throw new IllegalArgumentException("Column count must be 5-50");
		if(bombCount < 1 || bombCount > 999)
			throw new IllegalArgumentException("Bomb count must be 1-999");
		if(rows * cols <= bombCount)
			throw new IllegalArgumentException("Bomb count must be less than rows times columns");
		
		this.rows = rows;
		this.cols = cols;
		this.bombTotal = bombCount;
		this.flagTotal = 0;
		this.gameOver = false;
		
		cells = new Cell[rows][cols];
		for(int r = 0; r < rows; r++)
			for(int c = 0; c < cols; c++)
				cells[r][c] = new Cell();
		
		placeBombs(bombCount);
	}
	
	/**
	 * @return # rows
	 */
	public int getRows()
	{
		return rows;
	}
	
	/**
	 * @return # cols
	 */
	public int getCols()
	{
		return cols;
	}
	
	/**
	 * Get a cell at pos (row, col)
	 * 
	 * @param row 
	 * @param col
	 * @return cell at pos (row, col)
	 */
	public Cell getCell(int row, int col)
	{
		validatePosition(row, col);
		
		return cells[row][col];
	}
	
	/**
	 * Handle click on cell (row, col)
	 * 
	 * @param row
	 * @param col
	 */
	public void clickCell(int row, int col)
	{
		validatePosition(row, col);
		
		// Do nothing if cell is already exposed
		if(cells[row][col].isExposed())
			return;
		
		// If it had been flagged, unflag it
		if(cells[row][col].isFlagged())
		{
			cells[row][col].setFlagged(false);
			flagTotal--;
		}
		
		// Expose cell
		cells[row][col].expose();
		
		// If it is a bomb, game over
		if(cells[row][col].getValue() > 8)
		{
			gameOver = true;
			return;
		}
		
		// If it is a 0, expand all neighbors
		if(cells[row][col].getValue() == 0)
			expandNeighbors(row, col);
		
		// Test for win
		if(isVictory())
		{
			gameOver = true;
		}
	}
	
	/**
	 * Handle right-click on cell (row, col)
	 * 
	 * @param row
	 * @param col
	 */
	public void rightClickCell(int row, int col)
	{
		validatePosition(row, col);
		
		// Ignore if cell is exposed already
		if(cells[row][col].isExposed())
			return;
		
		// If flagged, make it queried
		if(cells[row][col].isFlagged())
		{
			cells[row][col].setQueried(true);
			cells[row][col].setFlagged(false);
			flagTotal--;
			return;
		}
		
		// If queried, make it blank
		if(cells[row][col].isQueried())
		{
			cells[row][col].setQueried(false);
			return;
		}
		
		// If blank, make it flagged
		cells[row][col].setFlagged(true);
		flagTotal++;
	}
	
	/**
	 * @return Total number of bombs on board
	 */
	public int getBombTotal()
	{
		return bombTotal;
	}
	
	/**
	 * @return Total number of flagged spaces
	 */
	public int getFlagTotal()
	{
		return flagTotal;
	}
	
	/**
	 * @return True iff game over
	 */
	public boolean isGameOver()
	{
		return gameOver;
	}
	
	/**
	 * @return True iff player has exposed all non-bombs
	 */
	public boolean isVictory()
	{
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				if(!cells[r][c].isExposed() && cells[r][c].getValue() <= 8)
					return false;
			}
		}
		return true;
	}
	
	/**
	 * Simulate a click on an unexposed cell that is not a bomb
	 */
	public void giveHint()
	{
		for(int r = 0; r < rows; r++)
		{
			for(int c = 0; c < cols; c++)
			{
				if(!cells[r][c].isExposed() && cells[r][c].getValue() <= 8)
				{
					clickCell(r, c);
					return;
				}
			}
		}
	}

	/**
	 * Randomly place bombs on board
	 * Set values of each Cell
	 * 
	 * Should only be called by constructor
	 * 
	 * @param count
	 */
	private void placeBombs(int count)
	{
		// Randomly generate #count unique locations on board
		Random rand = new Random();
		Set<Position> bombLocations = new HashSet<Position>();
		while(bombLocations.size() < count)
		{
			Position p = new Position();
			p.row = Math.abs(rand.nextInt() % rows);
			p.col = Math.abs(rand.nextInt() % cols);
			p.linearValue = (p.row * rows) + p.col;
			bombLocations.add(p);
		}
		
		// Set Cell at each bomb location as a bomb
		for(Position p : bombLocations)
		{
			cells[p.row][p.col].setBomb();
			
			// For each of Cell's neighbors, increment value
			for(Cell cell : getNeighbors(p.row, p.col))
				cell.incrementValue();
		}
	}
	
	/**
	 * Get total number of neighbors this cell at (row, col) has
	 * 3 if in corner, 5 if on edge, 8 otherwise
	 * 
	 * @param row
	 * @param col
	 * @return number of neighbors cell (row, col) has
	 */
	private int getNeighborCount(int row, int col)
	{
		validatePosition(row, col);
		
		if(col == 0 || col == cols-1) // If on left or right edge
		{
			if(row == 0 || row == rows-1) // If in corner
			{
				return 3;
			}
			return 5;
		}
		if(row == 0 || row == rows-1) // If on top or bottom edge, but not corner
		{
			return 5;
		}
		return 8; // If not on edge
	}
	
	/**
	 * Get all Cells bordering Cell at (row, col)
	 * 
	 * @param row
	 * @param col
	 * @return neighbors of (row, col)
	 */
	private Cell[] getNeighbors(int row, int col)
	{
		validatePosition(row, col);
		
		Cell[] neighbors = new Cell[getNeighborCount(row, col)];
		
		int index = 0;
		for(int r = row-1; r <= row+1; r++)
		{
			if(r < 0 || r >= rows) // If r is out of range, go to next row
				continue;
			for(int c = col-1; c <= col+1; c++)
			{
				if(r == row && c == col) // If (r,c) == (row,col), don't include as neighbor
					continue;
				if(c < 0 || c >= cols) // If c is out of range, go to next col
					continue;
				neighbors[index++] = cells[r][c];
			}
		}
		return neighbors;
	}
	
	/**
	 * Expand all neighbors
	 * Used when player 
	 * 
	 * @param row
	 * @param col
	 */
	private void expandNeighbors(int row, int col)
	{
		validatePosition(row, col);
		
		for(int r = row-1; r <= row+1; r++)
		{
			if(r < 0 || r >= rows) // If r is out of range, go to next row
				continue;
			for(int c = col-1; c <= col+1; c++)
			{
				if(r == row && c == col) // If (r,c) == (row,col), don't include as neighbor
					continue;
				if(c < 0 || c >= cols) // If c is out of range, go to next col
					continue;
				if(cells[r][c].isFlagged()) // Do not expand flagged cells
					continue;
				clickCell(r, c);
			}
		}
	}

	/**
	 * Expands this cell's neighbors if this cell is an exposed
	 * cell that has all neighbor bombs flagged
	 * 
	 * @param row
	 * @param col
	 */
	public void expandIfDone(int row, int col)
	{
		validatePosition(row, col);
		
		if(!cells[row][col].isExposed())
			return;
		
		int neighborBombs = cells[row][col].getValue();
		int neighborFlags = 0;
		for(Cell c : getNeighbors(row, col))
		{
			if(c.isFlagged())
				neighborFlags++;
		}
		
		if(neighborBombs != neighborFlags)
			return;
		
		expandNeighbors(row, col);
	}
	
	/**
	 * Validate that (row, col) is valid position on board
	 * 
	 * @param row
	 * @param col
	 * @throws IllegalArgumentException if (row, col) is not valid position
	 */
	private void validatePosition(int row, int col)
	{
		if(!((row >= 0) && (row < rows) && (col >= 0) && (col < cols)))
		{
			throw new IllegalArgumentException("Invalid position: (" + row + ", " + col + ") " +
					"for board (" + rows + ", " + cols + ")");
		}
	}
}

/**
 * Class used to represent a position on the board
 * Used to ensure that all bomb locations are unique
 *
 * Override hashCode and equals so that a HashSet won't contain duplicates
 */
class Position
{
	public int row;
	public int col;
	public int linearValue;
	
	@Override
	public int hashCode() {
		return linearValue;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(this == obj)
			return true;
		if(obj == null)
			return false;
		if(getClass() != obj.getClass())
			return false;
		
		Position other = (Position) obj;
		return linearValue == other.linearValue;
	}
}