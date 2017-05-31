
class Board {
	
	public static final int ROWS = 9;
	public static final int COLS = 9;
	
	public Cell[][] cells = new Cell[ROWS][COLS];
	
	public Board() {
		
	}
	
	public Board(short[][] values) {
		for (short r = 0; r < ROWS; r++) {
			for(short c = 0; c < COLS; c++) {
				cells[r][c] = new Cell(r, c, getSquare(r,c), values[r][c]);
			}
		}
	}
	
	public Board copy() {
		Board temp = new Board();
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLS; c++) {
				temp.cells[r][c] = cells[r][c].copy();
			}
		}
		return temp;
	}
	
	public void displayBoard() {
		for(short r = 0; r < ROWS; r++) {
			for(short c = 0; c < COLS; c++) {
				System.out.print(cells[r][c].value);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public void solve() {
		
		// Set initial possible values for each cell in board
		for(short r = 0; r < ROWS; r++) {
			for(short c = 0; c < COLS; c++) {
				try {
					setInitPossibleValues(cells[r][c]);
				}catch(Exception e) {
					System.out.println("Init failed: " + e.getMessage());
				}
			}
		}
		
		Cell first = getFirstUnknown();
		if(first != null) {
			short[] pV = first.getPossibleValues();
			for(int i = 0; i < pV.length; i++)
				guess(this, first, pV[i]);
		}
		else guess(this, first, (short)-1);
	}
	
	public boolean isValid() {
		
		for(int r = 0; r < ROWS; r++) {
			Cell[] tempCells = getRowCells(r);
			for(int i = 0; i < tempCells.length-1; i++) {
				for(int j = i+1; j < tempCells.length; j++) {
					if(tempCells[i].value == tempCells[j].value)
						return false;
				}
			}
		}
		
		for(int r = 0; r < ROWS; r++) {
			Cell[] tempCells = getColCells(r);
			for(int i = 0; i < tempCells.length-1; i++) {
				for(int j = i+1; j < tempCells.length; j++) {
					if(tempCells[i].value == tempCells[j].value)
						return false;
				}
			}
		}
		
		for(int r = 0; r < ROWS; r++) {
			Cell[] tempCells = getSquareCells(r);
			for(int i = 0; i < tempCells.length-1; i++) {
				for(int j = i+1; j < tempCells.length; j++) {
					if(tempCells[i].value == tempCells[j].value)
						return false;
				}
			}
		}
		
		return true;
	}
	
	public Cell[] getRowCells(int index) {
		return cells[index];
	}
	
	public Cell[] getColCells(int index) {
		Cell[] temp = new Cell[ROWS];
		for(int r = 0; r < ROWS; r++)
			temp[r] = cells[r][index];
		return temp;
	}
	
	public Cell[] getSquareCells(int index) {
		Cell[] temp = new Cell[ROWS];
		int i = 0;
		for(int r = 0; r < ROWS; r++) {
			for(int c = 0; c < COLS; c++) {
				if(getSquare((short)r,(short)c) == index) {
					temp[i++] = cells[r][c];
					if(i == 9)
						return temp;
				}
			}
		}
		return temp;
	}

	private static void guess(Board board, Cell cell, short newValue) {
		
		// If done
		if(cell == null) {
			if(board.isValid()) {
				board.displayBoard();
			}
			return;
		}
		
		// Copy objects
		Board newBoard = board.copy();
		Cell newCell = newBoard.cells[cell.row][cell.col];
		
		// Set value and update row, column, square
		newCell.value = newValue;
		try {
			newBoard.updateRow(newCell.row, newValue);
			newBoard.updateColumn(newCell.col, newValue);
			newBoard.updateSquare(newCell.square, newValue);
		}catch(Exception e) {
			return; // End recursion if error is found
		}
		
		// Recursive call
		Cell first = newBoard.getFirstUnknown();
		if(first != null) {
			short[] pV = first.getPossibleValues();
			for(int i = 0; i < pV.length; i++)
				guess(newBoard, first, pV[i]);
		}
		else guess(newBoard, first, (short)-1);
	}
	
	private Cell getFirstUnknown() {
		for(short r = 0; r < ROWS; r++) {
			for(short c = 0; c < COLS; c++) {
				if(cells[r][c].value == 0) return cells[r][c];
			}
		}
		return null;
	}
	
	private void setInitPossibleValues(Cell cell) throws Exception {
		
		// If cell has no value yet
		if(cell.value == 0) {
			
			// Eliminate values in row
			for(short c = 0; c < COLS; c++) {
				if(cell.removePossibleValue(cells[cell.row][c].value)) {
					updateRow(cell.row, cell.value);
					updateColumn(cell.col, cell.value);
					updateSquare(cell.square, cell.value);
				}
			}
			
			// Eliminate values in column
			for(short r = 0; r < ROWS; r++) {
				if(cell.removePossibleValue(cells[r][cell.col].value)) {
					updateRow(cell.row, cell.value);
					updateColumn(cell.col, cell.value);
					updateSquare(cell.square, cell.value);
				}
			}
			
			// Eliminate values in square
			for(short r = 0; r < ROWS; r++) {
				for(short c = 0; c < COLS; c++) {
					if(cells[r][c].square == cell.square) {
						if(cell.removePossibleValue(cells[r][c].value)) {
							updateRow(cell.row, cell.value);
							updateColumn(cell.col, cell.value);
							updateSquare(cell.square, cell.value);
						}
					}
				}
			}
		}
	}
	
	public void updateRow(short r, short v) throws Exception {
		for(short c = 0; c < COLS; c++) {
			if(cells[r][c].removePossibleValue(v)) {
				updateRow(r, cells[r][c].value);
				updateColumn(c, cells[r][c].value);
				updateSquare(cells[r][c].square, cells[r][c].value);
			}
		}
	}
	
	public void updateColumn(short c, short v) throws Exception {
		for(short r = 0; r < ROWS; r++) {
			if(cells[r][c].removePossibleValue(v)) {
				updateRow(r, cells[r][c].value);
				updateColumn(c, cells[r][c].value);
				updateSquare(cells[r][c].square, cells[r][c].value);
			}
		}
	}
	
	public void updateSquare(short s, short v) throws Exception {
		for(short r = 0; r < ROWS; r++) {
			for(short c = 0; c < COLS; c++) {
				if(cells[r][c].square == s) {
					if(cells[r][c].removePossibleValue(v)) {
						updateRow(r, cells[r][c].value);
						updateColumn(c, cells[r][c].value);
						updateSquare(cells[r][c].square, cells[r][c].value);
					}
				}
			}
		}
	}
	
	public static short getSquare(short r, short c) {
		if(r < 3) {
			if(c < 3)
				return 0;
			if(c < 6)
				return 1;
			return 2;
		}
		else if(r < 6) {
			if(c < 3)
				return 3;
			if(c < 6)
				return 4;
			return 5;
		}
		if(c < 3)
			return 6;
		if(c < 6)
			return 7;
		return 8;
	}
}