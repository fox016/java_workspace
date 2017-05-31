import java.util.ArrayList;
import java.util.List;

public class Euler61
{
	private static List<Integer>[] polys;
	private static long startTime;
	
	public static void main(String[] args)
	{
		startTime = System.currentTimeMillis();
		
		polys = new List[6];
		
		for(int i = 3; i <= 8; i++)
		{
			polys[i-3] = getPolygonalTypes(i, 1000, 10000);
		}
		
		for(int i = 0; i < polys.length; i++)
		{
			ArrayList<Integer> indices = new ArrayList<>();
			for(int x = 0; x < polys.length; x++)
			{
				if(x != i)
					indices.add(x);
			}
			
			for(int j = 0; j < polys[i].size(); j++)
			{
				getCycle(polys[i].get(j), indices, polys[i].get(j) + "");
			}
		}
	}
	
	private static void getCycle(int start, ArrayList<Integer> indices, String solution)
	{
		if(indices.isEmpty())
		{
			if(solution.substring(0, 2).equals(solution.substring(solution.length()-2)))
			{
				long end = System.currentTimeMillis();
				System.out.println(solution);
				System.out.println("Time: " + (end-startTime) + " ms");
				System.exit(0);
			}
			return;
		}
		
		int lastTwo = start % 100;
		
		for(Integer i: indices)
		{
			for(int j = 0; j < polys[i].size(); j++)
			{
				if(polys[i].get(j) >= (lastTwo * 100) && polys[i].get(j) < ((lastTwo+1) * 100))
				{
					ArrayList<Integer> copy = new ArrayList<Integer>(indices);
					copy.remove(i);
					getCycle(polys[i].get(j), copy, solution + ", " + polys[i].get(j));
				}
			}
		}
	}
	
	private static List<Integer> getPolygonalTypes(int type, int low, int high)
	{
		List<Integer> polygons = new ArrayList<>();
		
		int current = 0;
		int n = 1;
		while(current < high)
		{
			switch(type)
			{
			case 3:
				current = n * (n+1) / 2;
				break;
			case 4:
				current = n * n;
				break;
			case 5:
				current = n * (3 * n - 1) / 2;
				break;
			case 6:
				current = n * (2 * n - 1);
				break;
			case 7:
				current = n * (5 * n - 3) / 2;
				break;
			case 8:
				current = n * (3 * n - 2);
				break;
			}
			
			if(current >= low && current < high)
			{
				polygons.add(current);
			}
			++n;
		}
		
		return polygons;
	}
}
