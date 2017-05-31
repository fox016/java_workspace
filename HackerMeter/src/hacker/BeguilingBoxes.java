package hacker;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class BeguilingBoxes
{
	public static void run(Scanner scanner)
	{
		String json = scanner.nextLine();

		int level = 0;
		boolean inWord = false;
		Set<String> boxes = new TreeSet<String>();
		String temp = "";
		
		int outLevel = Integer.MAX_VALUE;
		boolean inBox = false;

		char[] jsonChars = json.toCharArray();
		for (char j : jsonChars) {
			System.out.println("\nLevel: " + level);
			System.out.println("Temp: " + temp + " (" + inWord + ")");
			System.out.println("Out Level: " + outLevel);
			System.out.println("In Box: " + inBox);
			switch (j) {
			case '{':
				level++;
				break;
			case '}':
				level--;
				break;
			case '\"':
				if (inWord && inBox && level == 3) {
					boxes.add(temp);
				}
				if(inWord && temp.substring(0,3).equals("box") && level <= outLevel)
				{
					inBox = true;
					outLevel = Integer.MAX_VALUE;
				}
				else if (inWord)
				{
					inBox = false;
					outLevel = level;
				}
				temp = "";
				inWord = !inWord;
				break;
			default:
				if (!inWord)
					break;
				temp += j;
				break;
			}
		}

		String result = "";
		for (String box : boxes) {
			result += box + ",";
		}
		if (result.length() > 0)
			System.out.println(result.substring(0, result.length() - 1));
	}

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		int cases = Integer.parseInt(scanner.nextLine());
		for (int i = 0; i < cases; i++) {
			run(scanner);
		}
	}
}
