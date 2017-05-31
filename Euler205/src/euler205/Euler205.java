package euler205;

//import java.util.Random;

public class Euler205
{
	/*
	private static final int pDiceCount = 9;
	private static final int pMax = 4;
	
	private static final int cDiceCount = 6;
	private static final int cMax = 6;
	
	private static final double trials = 100000000;
	
	private static Random rand = new Random();
	
	public static void main(String[] args)
	{
		double pWins = 0;
		for(int i = 0; i < trials; i++)
		{
			int pScore = rollDice(pDiceCount, pMax);
			int cScore = rollDice(cDiceCount, cMax);
			if(pScore > cScore)
				pWins++;
		}
		double percent = pWins / trials;
		System.out.println("P win %: " + percent);
	}

	private static int rollDice(int diceCount, int max)
	{
		int total = 0;
		for(int i = 0; i < diceCount; i++)
			total += (Math.abs(rand.nextInt() % max) + 1);
		return total;
	}
	*/
	
	public static void main(String[] args)
	{
		long start = System.nanoTime();
		
		int[] pyrOptions = {0, 0,  0,  1,   9,  45, 165, 486, 1206, 2598, 4950, 8451,
					13051, 18351, 23607, 27876, 30276, 30276, 27876, 23607, 18351,
					13051, 8451, 4950, 2598, 1206, 486, 165, 45, 9, 1};
		int[] cubeOptions = {1, 6, 21, 56, 126, 252, 456, 756, 1161, 1666, 2247, 2856,
					3431, 3906, 4221, 4332, 4221, 3906, 3431, 2856, 2247, 1666,
					1161, 756, 456, 252, 126, 56, 21, 6, 1};
		
		long numer = 0;
		double denom = Math.pow(4, 9) * Math.pow(6,  6);
		
		for(int cube = 0; cube < cubeOptions.length; cube++)
		{
			for(int pyr = cube + 1; pyr < pyrOptions.length; pyr++)
			{
				numer += pyrOptions[pyr] * cubeOptions[cube];
			}
		}
		
		long end = System.nanoTime();
		
		System.out.println("Solution: " + (numer/denom));
		System.out.println("Time: " + ((end - start) / 1000000.0) + " ms");
	}
}
