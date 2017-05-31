package euler145;

public class Euler145
{
	public static void main(String[] args)
	{
		long limit = 100000000;
		int total = 0;
		for(int n = 1; n < limit; n++)
		{
			if(n % 10 == 0) continue;
			int sum = n + Integer.parseInt(new StringBuffer(n + "").reverse().toString());
			if(allOdd(sum))
			{
				total++;
			}
		}
		System.out.println("Solution: " + total);
	}

	private static boolean allOdd(int sum)
	{
		String numStr = sum + "";
		char[] num = numStr.toCharArray();
		for(int i = 0; i < num.length; i++)
		{
			if(Character.getNumericValue(num[i]) % 2 == 0)
				return false;
		}
		return true;
	}
}
