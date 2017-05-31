import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;

public class RuzzleSolver {
	
	private static final int size = 4;
	private static char[][] board;
	private static ArrayList<String> wordlist;
	private static HashSet<String> solution;
	
	public static void main(String[] args) {
		
		// Build dictionary
		// String wordFile = "/usr/share/dict/web2";
		String wordFile = "/Users/Admin/Documents/dictionary.txt";
		initWordlist(wordFile);
		
		// Build board
		String boardString = "tolsheoetermaisb";
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Enter Board String: ");
		try {
			boardString = reader.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		initBoard(boardString);
		
		// Solve
		solve();
		String[] allWords = new String[solution.size()];
		solution.toArray(allWords);
		sortByLength(allWords);
		printArray(allWords);
	}
	
	public static int getSize() {
		return size;
	}
	
	private static void solve() {
		
		assert(board != null);
		assert(wordlist != null);
		
		solution = new HashSet<String>();
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				HashSet<Position> already = new HashSet<Position>();
				findWords("", i, j, already);
			}
		}
	}
	
	private static void findWords(String root, int nextRow, int nextCol, HashSet<Position> already) {
		
		assert(board != null);
		assert(wordlist != null);
		assert(solution != null);
		
		// If location defined by nextRow and nextCol is not on board, return
		if(nextRow < 0 || nextRow >= size) return;
		if(nextCol < 0 || nextCol >= size) return;
		
		// Copy set of positions already used
		HashSet<Position> newAlready = new HashSet<Position>(already);
		
		// If add returns true, then the next position was added to the already set
		// If add returns false, the next position has already been used, so return
		if(!newAlready.add(new Position(nextRow, nextCol)))
			return;
		
		// Append char at next location to root
		String newRoot = root + String.valueOf(board[nextRow][nextCol]);
		
		// If it is a word, add it to solution set
		if(isWord(newRoot))
			solution.add(newRoot);
		
		// If it is a valid prefix, recursively check all paths
		// Otherwise, return
		if(isValidPrefix(newRoot)) {
			
			// Go up
			findWords(newRoot, nextRow-1, nextCol, newAlready);
			
			// Go down
			findWords(newRoot, nextRow+1, nextCol, newAlready);
			
			// Go left
			findWords(newRoot, nextRow, nextCol-1, newAlready);
			
			// Go right
			findWords(newRoot, nextRow, nextCol+1, newAlready);
			
			// Go NW
			findWords(newRoot, nextRow-1, nextCol-1, newAlready);
			
			// Go NE
			findWords(newRoot, nextRow-1, nextCol+1, newAlready);
			
			// Go SW
			findWords(newRoot, nextRow+1, nextCol-1, newAlready);
			
			// Go SE
			findWords(newRoot, nextRow+1, nextCol+1, newAlready);
		}
	}
	
	public static void sortByLength(String[] words) {
		
		int i, j;
		int iMin;
		
		for(j = 0; j < words.length-1; j++) {
			
			iMin = j;
			for(i = j+1; i < words.length; i++) 
				if(words[i].length() < words[iMin].length()) 
					iMin = i;
			
			if(iMin != j) {
				String temp = words[j];
				words[j] = words[iMin];
				words[iMin] = temp;
			}
		}
	}
	
	public static void printArray(String[] words) {
		System.out.println("Count: " + words.length);
		for(int i = words.length-1; i >= 0; i--)
			System.out.println(words[i]);
	}
	
	private static void initBoard(String b) {
		
		assert(b.length() == size * size);
		
		board = new char[size][size];
		
		for(int i = 0; i < size; i++) {
			for(int j = 0; j < size; j++) {
				board[i][j] = b.charAt(i * size + j);
			}
		}
	}
	
	private static void initWordlist(String file) {
		
		if(wordlist != null) return;
		
		wordlist = new ArrayList<String>();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}catch(Exception e){
			System.out.println("Failed loading word file");
			System.exit(-1);
		}
		
		String word = "";
		try {
			word = reader.readLine();
			while(word != null) {
				wordlist.add(word.toLowerCase());
				word = reader.readLine();
			}
		}catch(Exception e) {
			System.out.println("Failed reading word file");
			System.exit(-2);
		}
	}
	
	public static boolean isWord(String word) {
		
		assert(wordlist != null);
		assert(word != null);
		assert(word.length() > 0);
		
		int high = wordlist.size() - 1;
		int low = 0;
		
		while(high > low) {
			
			int index = (int) Math.floor((high + low) / 2.0);
			
			if(wordlist.get(index).equals(word))
				return true;
			if(wordlist.get(high).equals(word))
				return true;
			if(wordlist.get(low).equals(word))
				return true;
			
			if(wordlist.get(index).compareTo(word) < 0)
				low = index + 1;
			else
				high = index - 1;
		}
		
		return false;
	}
	
	public static boolean isValidPrefix(String prefix) {
		
		assert(wordlist != null);
		assert(prefix != null);
		assert(prefix.length() > 0);
		
		int high = wordlist.size() - 1;
		int low = 0;
		int length = prefix.length();
		
		while(high > low) {
			
			int index = (int) Math.floor((high + low) / 2.0);
			
			if(cutstring(wordlist.get(index), length).equals(prefix))
				return true;
			if(cutstring(wordlist.get(high), length).equals(prefix))
				return true;
			if(cutstring(wordlist.get(low), length).equals(prefix))
				return true;
			
			if(cutstring(wordlist.get(index), length).compareTo(prefix) < 0)
				low = index + 1;
			else
				high = index - 1;
		}
		
		return false;
	}
	
	public static String cutstring(String s, int length) {
		
		if(s.length() <= length)
			return s;
		
		return s.substring(0, length);
	}
}

class Position {

	public int row;
	public int col;
	
	public Position(int r, int c) {
		row = r;
		col = c;
	}
	
	@Override
	public int hashCode() {
		return row * RuzzleSolver.getSize() + col;
	}
	
	@Override
	public boolean equals(Object other) {
		if(other == null) return false;
		return this.hashCode() == other.hashCode();
	}
	
	@Override
	public String toString() {
		return "(" + row + ", " + col + ")";
	}
}