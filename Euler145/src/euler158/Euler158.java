package euler158;

import java.math.BigInteger;

public class Euler158
{
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		BigInteger high = BigInteger.ZERO;
		for(int i = 1; i <= 26; i++)
		{
			BigInteger n = calcPofN(i);
			System.out.println(i + ": " + n);
			if(n.compareTo(high) > 0)
				high = n;
		}
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + high);
		System.out.println("Time: " + (end - start) + " ms");
	}

	private static BigInteger factorial(int n)
	{
		BigInteger f = BigInteger.ONE;
		for(int i = 1; i <= n; i++)
			f = f.multiply(new BigInteger(i+""));
		return f;
	}
	
	private static BigInteger fallingFactorial(int x, int n)
	{
		BigInteger f = BigInteger.ONE;
		for(int i = 0; i < n; i++)
		{
			f = f.multiply(new BigInteger((x - i)+""));
		}
		return f;
	}
	
	private static BigInteger choose(int n, int k)
	{
		BigInteger num = fallingFactorial(n, k);
		return num.divide(factorial(k));
	}
	
	private static BigInteger calcPofN(int n)
	{
		long temp = ((long) Math.pow(2,n)) - n - 1;
		BigInteger part1 = new BigInteger(temp+"");
		return part1.multiply(choose(26, n));
	}
}
