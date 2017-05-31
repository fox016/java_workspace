package anagram;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;

public class Anagram
{
	private static BufferedReader reader;
	private static ArrayList<String> wordlist;
	
	public static void main(String[] args) throws FileNotFoundException
	{
		String wordFile = "/Users/Admin/Documents/shortDictionary.txt";
		initWordlist(wordFile);
		
		reader = new BufferedReader(new InputStreamReader(System.in));
		
		PrintWriter writer = new PrintWriter("/Users/Admin/Documents/anagramOut.txt");
		
		String phrase = prompt("Enter a phrase: ");
		int maxWords = Integer.parseInt(prompt("Enter max number of words for phrase anagram: "));
		String[] words = phrase.split(" ");
		String noSpace = "";
		for(String word: words)
		{
			writer.println("\nORIGINAL WORD: " + word);
			Set<String> anagrams = getWordAnagrams(word);
			for(String anagram: anagrams)
			{
				writer.println(anagram);
			}
			noSpace += word;
		}

		writer.println("\nPHRASE: " + phrase);
		Set<String> anagrams = getPhraseAnagrams(noSpace, maxWords-1);
		for(String anagram: anagrams)
		{
			writer.println(anagram);
		}
		writer.close();
	}

	public static Set<String> getPhraseAnagrams(String noSpace, int numSpace)
	{
		for(int i = 0; i < numSpace; i++)
			noSpace += " ";
		TreeSet<String> anagrams = new TreeSet<String>();
		permutePhrase("", noSpace.toLowerCase(), anagrams);
		return anagrams;
	}

	/**
	 * @param word - A string with no spaces
	 * @return A set of words that are anagrams of the input word
	 */
	public static Set<String> getWordAnagrams(String word)
	{
		TreeSet<String> anagrams = new TreeSet<>();
		permute("", word.toLowerCase(), anagrams);
		return anagrams;
	}
	
	private static void permutePhrase(String first, String rest, Set<String> anagrams)
	{
		String[] words = first.split(" ");
		if(words.length == 0)
			return;
		int i = 0;
		for(i = 0; i < words.length - 1; i++)
			if(!isWord(words[i]))
				return;
		if(!isValidPrefix(words[i]))
			return;
		if(rest.length() == 0)
		{
			if(isWord(words[i]))
			{
				System.out.println(first);
				anagrams.add(first);
				return;
			}
		}
		for(i = 0; i < rest.length(); i++)
		{
			permutePhrase(first + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1), anagrams);
		}	
	}
	
	/**
	 * Build set of all permutations of a string that are words
	 * 
	 * @param first - The beginning of a permutation of a string
	 * @param rest - The rest of the characters that need to be added
	 * 	to make first a valid permutation
	 * @param anagrams - The set that all valid words that are permutations
	 * 	of the original input rest
	 */
	private static void permute(String first, String rest, Set<String> anagrams)
	{
		if(!isValidPrefix(first))
			return;
		if(rest.length() == 0)
		{
			if(isWord(first))
			{
				anagrams.add(first);
				return;
			}
		}
		for(int i = 0; i < rest.length(); i++)
		{
			permute(first + rest.charAt(i), rest.substring(0, i) + rest.substring(i + 1), anagrams);
		}		
	}
	
	/**
	 * @param prefix - A string that represents a word or
	 * 	the first part of a word
	 * @return True iff there exists a word that is or begins
	 * 	with the input prefix
	 */
	private static boolean isValidPrefix(String prefix)
	{	
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
	
	/**
	 * @param s - The string to cut
	 * @param length - The max length of the new string
	 * @return The input string cut down to length, or the
	 * 	entire input string if shorter than input length
	 */
	private static String cutstring(String s, int length)
	{	
		if(s.length() <= length)
			return s;
		
		return s.substring(0, length);
	}
	
	/**
	 * @param word - A string with no spaces
	 * @return True iff input word is in dictionary
	 */
	private static boolean isWord(String word)
	{	
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
	 * Populates wordlist with dictionary words retrieved
	 * from file
	 * 
	 * @param file - Dictionary file, each word separated by newline
	 */
	private static void initWordlist(String file)
	{	
		if(wordlist != null) return;
		
		wordlist = new ArrayList<String>();
		
		BufferedReader reader = null;
		
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
		}catch(Exception e){
			System.out.println("Failed loading word file: " + file);
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
		
		TreeSet<String> sort = new TreeSet<String>(wordlist);
		wordlist = null;
		wordlist = new ArrayList<String>(sort);
	}
}
