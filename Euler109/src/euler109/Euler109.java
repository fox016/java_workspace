package euler109;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Euler109
{
	private static Set<Integer> doubles;
	private static List<Integer> allScores;
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		initScoreSets();
		
		int solution = 0;
		for(int n = 0; n <= 170; n++)
		{
			solution += solve(n);
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + solution);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	public static int solve(int n)
	{
		int count = 0;
		for(int i = 0; i < allScores.size(); i++)
		{
			for(int j = 0; j <= i; j++)
			{
				int total = allScores.get(i) + allScores.get(j);
				int remainder = n - total;
				if(doubles.contains(remainder))
				{
					count++;
				}
			}
		}
		return count;
	}
	
	public static void initScoreSets()
	{
		doubles = new HashSet<>();
		allScores = new ArrayList<>();
		
		for(int i = 1; i <= 20; i++)
		{
			doubles.add(i * 2);
			
			allScores.add(i);
			allScores.add(i * 2);
			allScores.add(i * 3);
		}
		
		doubles.add(50);
		
		allScores.add(0);
		allScores.add(25);
		allScores.add(50);
		
		Collections.sort(allScores);
	}
}
