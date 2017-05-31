package wordchange;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class WordChange
{
	private static BufferedReader reader;
	private static Set<String> wordset;
	private static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static long startTime;
	private static int solutionCount = 0;
	
	public static void main(String[] args)
	{
		// Build dictionary
		initWordSet("/Users/Admin/Documents/dictionary.txt");
		
		// Init reader
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		// Define user vars
		String startWord = "";
		String endWord = "";
		int steps = 0;
		
		// Prompt user
		startWord = prompt("Start word: ");
		endWord = prompt("End word: ");
		try
		{
			steps = Integer.parseInt(prompt("Number of Steps: "));
		}catch(NumberFormatException e)
		{
			System.out.println("Steps must be a whole number");
			System.exit(-3);
		}
		
		// Start and end words must have equal length
		if(startWord.length() != endWord.length())
		{
			System.out.println("Start word and end word must have same length");
			System.exit(-4);
		}
		
		// Solve
		startTime = System.currentTimeMillis();
		solve(startWord, endWord, steps, new LinkedList<String>());
		System.out.println(solutionCount + " solution(s) found");
	}
	
	/**
	 * Recursively finds all solutions.  A solution is found when startWord equals endWord and
	 * steps is 0.  When a solution is found, the history will be displayed to the user.
	 * 
	 * @param startWord - The initial word
	 * @param endWord - The target word
	 * @param steps - The number of steps required to go from the initial word to the target word
	 * @param history - A history of all words in a particular recursive branch
	 */
	private static void solve(String startWord, String endWord, int steps, List<String> history)
	{
		// If out of steps, discontinue this branch
		if(steps < 0)
			return;
		
		// If not a word, discontinue this branch
		if(!isWord(startWord))
			return;
		
		// If this word has been used in this branch before, discontinue this branch
		if(history.contains(startWord))
			return;
		
		// Add word to branch history
		history.add(startWord);
		
		// If a solution is found, print history
		if(startWord.equals(endWord) && steps == 0)
		{
			displaySolution(history);
			return;
		}
		
		// Replace each character in start word with each letter of the alphabet
		// and recursively feed to solver
		for(int i = 0; i < startWord.length(); i++)
		{
			for(int j = 0; j < alphabet.length(); j++)
			{
				// No need to replace a character with itself
				if(startWord.charAt(i) == alphabet.charAt(j))
					continue;
				
				String newWord = replaceChar(startWord, i, alphabet.charAt(j)); 
				solve(newWord, endWord, steps-1, new LinkedList<String>(history));
			}
		}
	}

	/** 
	 * Replaces one char of a string with a new char
	 * 
	 * @param word - The string that will have a character replaced
	 * @param pos - The index of the char in word that will be replaced
	 * @param newChar - The char that will replace word[pos]
	 * 
	 * @return The string with the replaced char
	 */
	private static String replaceChar(String word, int pos, char newChar)
	{
		String begin = word.substring(0, pos);
		String end = word.substring(pos);
		return begin + newChar + end.substring(1);
	}

	/**
	 * Displays a list of strings to the user. Also shows user how
	 * long program has been running.
	 * 
	 * @param history - A list of strings to display to the user
	 */
	private static void displaySolution(List<String> history)
	{
		System.out.println("Solution #" + (++solutionCount) + ":");
		for(String s : history)
		{
			System.out.println(s);
		}
		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end - startTime) + " ms");
	}

	/**
	 * Tests if string is a dictionary word
	 * 
	 * @param str - word to test validity of
	 * 
	 * @return true iff str is a dictionary word
	 */
	private static boolean isWord(String str)
	{
		return wordset.contains(str);
	}

	/**
	 * Prompts the user for input
	 * 
	 * @param display - A string used to prompt the user for input
	 * 
	 * @return Line of user input
	 */
	private static String prompt(String display)
	{
		String input = "";
		System.out.println(display);
		
		try
		{
			input = reader.readLine();
		}catch(IOException e)
		{
			System.out.println("Error reading input");
			System.exit(-5);
		}
		
		return input;
	}
	
	/**
	 * Initializes private variable wordset using given dictionary file
	 * 
	 * @param file - Dictionary file, each word separated by newline
	 */
	private static void initWordSet(String file)
	{	
		if(wordset != null) return;
		
		wordset = new HashSet<>();
		
		BufferedReader wordReader = null;
		
		try {
			wordReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}catch(Exception e){
			System.out.println("Failed loading word file");
			System.exit(-1);
		}
		
		String word = "";
		try {
			word = wordReader.readLine();
			while(word != null) {
				wordset.add(word.toLowerCase());
				word = wordReader.readLine();
			}
		}catch(Exception e) {
			System.out.println("Failed reading word file");
			System.exit(-2);
		}
	}
}