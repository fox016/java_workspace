package euler139;

public class Euler139
{
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		int total = 0;
		long limit = 100000000;
		for(int m = 1; m < Math.sqrt(limit / 2); m++)
		{
			for(int n = 1; n < m; n++) // m > n
			{
				if(GCD(m, n) != 1) // m and n must be coprime
					continue;
				if((m - n) % 2 == 0) // m - n must be odd
					continue;
				int a = (m*m) - (n*n);
				int b = 2 * m * n;
				int c = (m*m) + (n*n);
				if(c % (b - a) == 0)
				{
					System.out.println(a + ", " + b + ", " + c + " (" + m + ", " + n + ")");
					total += (limit / (a + b + c));
				}
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + total);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	public static int GCD(int a, int b) { return b==0 ? a : GCD(b, a%b); }
}
