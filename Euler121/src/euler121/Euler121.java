package euler121;

public class Euler121
{
	private final static int rounds = 10;
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		double solution = Math.floor(calcDenomenatorOdds() * 1.0 / calcTotalNumerator());
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + solution);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	public static long calcTotalNumerator()
	{
		long total = 0;
		boolean[] game = new boolean[rounds];
		
		for(int i = 0; i < rounds; i++)
			game[i] = true;
		
		while(true)
		{
			long num = calcNumeratorOdds(game);
			total += num;
			
			int pos = rounds - 1;
			while(pos >= 0 && !game[pos])
			{
				game[pos] = true;
				pos--;
			}
			if(pos == -1)
				break;
			game[pos] = false;
		}
		
		return total;
	}

	public static long calcNumeratorOdds(boolean[] game)
	{
		if(!moreTrue(game))
			return 0;
		
		long total = 1;
		for(int i = 0; i < rounds; i++)
		{
			if(!game[i])
			{
				total *= (i + 1);
			}
		}
		return total;
	}
	
	private static boolean moreTrue(boolean[] game)
	{
		int trueCount = 0;
		for(int i = 0; i < game.length; i++)
			if(game[i])
				trueCount++;
		
		return trueCount > (game.length - trueCount);
	}

	public static long calcDenomenatorOdds()
	{
		long total = 1;
		for(int i = 0; i < rounds; i++)
		{
			total *= (i + 2);
		}
		return total;
	}
}
