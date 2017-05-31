package hacker;
import java.util.*;

public class HackerMeter
{
	
	public static void run(Scanner scanner)
	{
	    int n = Integer.parseInt(scanner.nextLine());
	    
	    String str = "";
	    for(int i = 0; i < Math.pow(3, n); i++)
	    	str += "-";
	    
	    System.out.println(solve(str));
	}
	
	private static String solve(String s)
	{
		if(s.length() <= 1)
			return s;
		
		int length = s.length();
		
		String middle = "";
		for(int i = 0; i < length/3; i++)
			middle += " ";
		
		String ends = solve(s.substring(0, length/3));
		
		return ends + middle + ends;
	}

public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    int cases = Integer.parseInt(scanner.nextLine());
    for(int i = 0; i < cases; i++) {
      run(scanner);
    }
  }
}