import java.util.Arrays;

class Cell {
	
	public short row;
	public short col;
	public short square;
	
	public short value;
	
	public boolean[] possibleValues = new boolean[Board.ROWS];
	
	public Cell(short row, short col, short square, short value) {
		
		this.row = row;
		this.col = col;
		this.square = square;
		this.value = value;
		
		// If value is 0, set all values to possible
		if(value == 0)
			for(short x = 0; x < possibleValues.length; x++)
				possibleValues[x] = true;
		
		// If value is specific, set all values to impossible (except specific value)
		else {
			for(short x = 0; x < possibleValues.length; x++)
				possibleValues[x] = false;
			possibleValues[value-1] = true;
		}
	}
	
	public Cell copy() {
		Cell temp = new Cell(row, col, square, value);
		temp.possibleValues = Arrays.copyOf(this.possibleValues, this.possibleValues.length);
		return temp;
	}
	
	// Returns true iff cell's value was set to only remaining possible value
	public boolean removePossibleValue(short v) throws Exception {
		
		if(value != 0) return false;
		if(v == 0) return false;
		
		possibleValues[v-1] = false;
		
		// If only one possible value, set value
		short possibleIndex = -1;
		for(short x = 0; x < possibleValues.length; x++) {
			if(possibleValues[x]) {
				if(possibleIndex == -1)
					possibleIndex = x;
				else {
					possibleIndex = -2;
					break;
				}
			}	
		}
		if(possibleIndex == -1)
			throw new Exception("Error: all values false at row " + row + ", col " + col);
		if(possibleIndex == -2) 
			return false;
		value = (short) (possibleIndex + 1);
		return true;
	}
	
	public void displayPossibleValues() {
		System.out.print("[");
		for(short x = 0; x < possibleValues.length; x++) {
			if(possibleValues[x])
				System.out.print(x+1);
		}
		System.out.println("]");
	}
	
	public short[] getPossibleValues() {
		short size = 0;
		for(short x = 0; x < possibleValues.length; x++)
			if(possibleValues[x])
				size++;
		short[] temp = new short[size];
		int i = 0;
		for(short x = 0; x < possibleValues.length; x++)
			if(possibleValues[x])
				temp[i++] = (short) (x+1);
		return temp;
	}
}