package aes;

import java.util.Random;

public class SecondBayesHomework
{
	private static final int iterations = 100000;
	private static Random random;
	
	public static void main(String[] args)
	{
		random = new Random();
		int aCount = 0;
		int bCount = 0;
		int cCount = 0;
		double bPardonCount = 0;
		
		for(int i = 0; i < iterations; i++)
		{
			char executed = getExecuted();
			char message = getMessage(executed);
			if(message == 'B')
			{
				bPardonCount++;
				switch(executed)
				{
					case 'A':
						aCount++;
						break;
					case 'B':
						bCount++;
						break;
					case 'C':
						cCount++;
						break;
					default: throw new IllegalArgumentException("Only A B C can be executed: " + executed);
				}
			}
		}
		
		System.out.println("Which prisoner is executed when B gets pardoned?");
		System.out.println("A: " + aCount + " / " + bPardonCount + ": " + (aCount/bPardonCount));
		System.out.println("B: " + bCount + " / " + bPardonCount + ": " + (bCount/bPardonCount));
		System.out.println("C: " + cCount + " / " + bPardonCount + ": " + (cCount/bPardonCount));
	}
	
	private static char getMessage(char executed)
	{
		switch(executed)
		{
			case 'A':
				switch(Math.abs(random.nextInt() % 2))
				{
					case 0: return 'B';
					case 1: return 'C';
					default: throw new IllegalStateException("You did % 2 wrong");
				}
			case 'B': return 'C';
			case 'C': return 'B';
			default: throw new IllegalArgumentException("Only A B C can be executed: " + executed);
		}
	}
	
	private static char getExecuted()
	{
		switch(Math.abs(random.nextInt() % 3))
		{
			case 0: return 'A';
			case 1: return 'B';
			case 2: return 'C';
			default: throw new IllegalStateException("You did % 3 wrong");
		}
	}
}
