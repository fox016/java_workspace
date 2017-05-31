import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.IOException;

class Euler96 {
	
	public static void main(String[] args) throws IOException{
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/Admin/Downloads/sudoku.txt")));
		
		String line = reader.readLine();
		short row = 0;
		short[][] values = new short[Board.ROWS][Board.COLS];
		while(line != null) {
			
			// When you reach a new grid, solve board and display. Then move on to next grid.
			if(line.substring(0,4).equals("Grid")) {
				Board board = new Board(values);
				board.solve();
				row = 0;
				line = reader.readLine();
				continue;
			}
			
			// Fill out values
			char[] vals = line.toCharArray();
			for(short c = 0; c < vals.length; c++)
				values[row][c] = (short) Character.getNumericValue(vals[c]);
			
			// Get next line
			row++;
			line = reader.readLine();
		}
	}
}
