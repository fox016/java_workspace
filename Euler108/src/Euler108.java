import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Euler108
{	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		int x = 4;
		int solution = getSolution(x);
		int high = solution;
		while(solution < 1000)
		{
			if(solution > high)
			{
				System.out.println((x - 1) + ": " + solution);
				high = solution;
			}
			solution = getSolution(x++);
		}
		long end = System.currentTimeMillis();
		System.out.println((x - 1) + ": " + solution);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	public static int getSolution(int z)
	{
		List<Integer> div = getDivisors(z);
		Set<Set<Integer>> solutions = new HashSet<>();
		for(Integer k : div)
		{
			for(Integer m : div)
			{
				for(Integer n : div)
				{
					if(k * m * n == z)
					{
						int x = k * m * (m + n);
						int y = k * n * (m + n);
						Set<Integer> s = new HashSet<>(2);
						s.add(x);
						s.add(y);
						solutions.add(s);
					}
				}
			}
		}
		return solutions.size();
	}
	
	public static List<Integer> getDivisors(int n)
	{
		List<Integer> div = new ArrayList<>();
		div.add(1);
		div.add(n);
		for(int i = 2; i <= (n/2.0); i++)
		{
			if(n % i == 0)
				div.add(i);
		}
		return div;
	}
}
