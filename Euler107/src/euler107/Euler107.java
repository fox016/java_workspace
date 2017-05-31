package euler107;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Euler107
{
	private static List<Edge> edges;
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		long initCost = 0;
		try {
			initCost = initEdges();
		} catch (IOException e) {
			System.out.println("Could not load matrix");
			System.exit(1);
		}
		
		long finalCost = solve();
		
		long end = System.currentTimeMillis();
		
		System.out.println("Savings: " + (initCost - finalCost));
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static long solve()
	{
		long weight = 0;
		
		VertexSetWrapper sets = new VertexSetWrapper();
		
		for(Edge edge : edges)
		{
			boolean isValid = sets.merge(edge.vertex1, edge.vertex2);
			if(isValid)
			{
				weight += edge.weight;
				if(sets.getCount() == 1)
				{
					break;
				}
			}
		}
		
		return weight;
	}

	public static long initEdges() throws IOException
	{
		edges = new ArrayList<>();
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/Admin/Downloads/network.txt")));
		
		String line = reader.readLine();
		long totalCost = 0;
		int row = 0;
		while(line != null)
		{
			String[] tokens = line.split(",");
			for(int col = 0; col < tokens.length; col++)
			{
				if(row < col && !tokens[col].equals("-"))
				{
					int weight = Integer.parseInt(tokens[col]);
					edges.add(new Edge(row, col, weight));
					totalCost += weight;
				}
			}
			line = reader.readLine();
			row++;
		}
		
		Collections.sort(edges);
		return totalCost;
	}
}

class VertexSetWrapper
{
	private Set<VertexSet> allSets;
	
	public VertexSetWrapper()
	{
		allSets = new HashSet<>();
		for(int i = 0; i < 40; i++)
		{
			allSets.add(new VertexSet(i));
		}
	}
	
	public int getCount()
	{
		return allSets.size();
	}
	
	public boolean merge(int i, int j)
	{
		VertexSet one = find(i);
		
		if(one.contains(j))
			return false;
		
		VertexSet two = find(j);
		
		one.addAll(two);
		
		allSets.remove(two);
		two = null;
		
		return true;
	}
	
	private VertexSet find(int n)
	{
		for(VertexSet set : allSets)
		{
			if(set.contains(n))
				return set;
		}
		System.err.println("You should never get here");
		return null;
	}
}

class VertexSet
{
	private Set<Integer> vertices;
	
	public VertexSet(int n)
	{
		vertices = new HashSet<>();
		vertices.add(n);
	}
	
	public Set<Integer> getVertices()
	{
		return vertices;
	}
	
	public boolean contains(int n)
	{
		return vertices.contains(n);
	}
	
	public boolean addAll(VertexSet other)
	{
		return vertices.addAll(other.getVertices());
	}
}

class Edge implements Comparable<Edge>
{
	public int vertex1;
	public int vertex2;
	public int weight;
	
	public Edge(int v1, int v2, int w)
	{
		vertex1 = v1;
		vertex2 = v2;
		weight = w;
	}

	@Override
	public int compareTo(Edge other)
	{
		return weight - other.weight;
	}
	
	@Override
	public String toString()
	{
		return weight + ": {" + vertex1 + ", " + vertex2 + "}\n";
	}
}
