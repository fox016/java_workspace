package euler112;

public class Euler112
{
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		int n = 99;
		int bouncyCount = 0;
		
		while(100 * bouncyCount < 99 * n)
		{
			n++;
			if(isBouncy(n))
			{
				bouncyCount++;
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + n);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static boolean isBouncy(long n)
	{
		if(isIncreasing(n))
			return false;
		if(isDecreasing(n))
			return false;
		return true;
	}

	private static boolean isDecreasing(long n)
	{
		String numStr = n + "";
		char[] digits = numStr.toCharArray();
		for(int i = 1; i < digits.length; i++)
		{
			if(digits[i-1] > digits[i])
				return false;
		}
		return true;
	}

	private static boolean isIncreasing(long n)
	{
		String numStr = n + "";
		char[] digits = numStr.toCharArray();
		for(int i = 1; i < digits.length; i++)
		{
			if(digits[i-1] < digits[i])
				return false;
		}
		return true;
	}
}
