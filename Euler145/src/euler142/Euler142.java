package euler142;

public class Euler142
{
	public static void main(String[] args)
	{
		for(int x = 1; x < Integer.MAX_VALUE; x++)
		{
			for(int y = 1; y < x; y++)
			{
				for(int z = 1; z < y; z++)
				{
					if(isSquare(x + y) &&
							isSquare(x - y) &&
							isSquare(x + z) &&
							isSquare(x - z) &&
							isSquare(y + z) &&
							isSquare(y - z))
					{
						System.out.println("Solution: " + (x + y + z));
						System.exit(0);
					}	
				}
			}
		}
	}

	private static boolean isSquare(int i)
	{
		double root = Math.sqrt(i);
		return (root == (long) root);
	}
}
