import java.math.BigInteger;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;


public class Euler160
{

	private static final int[] primes = getPrimes(30000);
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		int bestSolution = Integer.MAX_VALUE;
		
		Map<Integer, Set<Integer>> pairs = new HashMap<>(primes.length);
		
		for(int first = 1; first < primes.length; first++)
		{
			if(primes[first] * 5 >= bestSolution)
				break;
			
			if(pairs.get(first) == null)
				pairs.put(first, makePairs(first));
			
			for(int second = first + 1; second < primes.length; second++)
			{
				if(primes[first] + primes[second] * 4 >= bestSolution)
					break;
				
				if(!pairs.get(first).contains(primes[second]))
					continue;
				
				if(pairs.get(second) == null)
					pairs.put(second, makePairs(second));
				
				for(int third = second + 1; third < primes.length; third++)
				{
					if(primes[first] + primes[second] + primes[third] * 3 >= bestSolution)
						break;
					
					if(!pairs.get(first).contains(primes[third])) continue;
					if(!pairs.get(second).contains(primes[third])) continue;
					
					if(pairs.get(third) == null)
						pairs.put(third, makePairs(third));
					
					for(int fourth = third + 1; fourth < primes.length; fourth++)
					{
						if(primes[first] + primes[second] + primes[third] + primes[fourth] * 2 >= bestSolution)
							break;
						
						if(!pairs.get(first).contains(primes[fourth])) continue;
						if(!pairs.get(second).contains(primes[fourth])) continue;
						if(!pairs.get(third).contains(primes[fourth])) continue;
						
						if(pairs.get(fourth) == null)
							pairs.put(fourth, makePairs(fourth));
						
						for(int fifth = fourth + 1; fifth < primes.length; fifth++)
						{
							if(primes[first] + primes[second] + primes[third] + primes[fourth] + primes[fifth] >= bestSolution)
								break;
							
							if(!pairs.get(first).contains(primes[fifth])) continue;
							if(!pairs.get(second).contains(primes[fifth])) continue;
							if(!pairs.get(third).contains(primes[fifth])) continue;
							if(!pairs.get(fourth).contains(primes[fifth])) continue;
							
							bestSolution = primes[first] + primes[second] + primes[third] + primes[fourth] + primes[fifth];
						}
					}
				}
			}
		}
		
		long end = System.currentTimeMillis();
		System.out.println(bestSolution);
		System.out.println("Time: " + (end - start) + " ms");
	}

	private static Set<Integer> makePairs(int a)
	{
		Set<Integer> pairs = new HashSet<>();
		for(int b = a+1; b < primes.length; b++)
		{
			if(isPrime(concat(primes[a], primes[b])) && isPrime(concat(primes[b], primes[a])))
				pairs.add(primes[b]);
		}
		return pairs;
	}

	private static boolean isPrime(BigInteger n)
	{
		return n.isProbablePrime(7);
	}

	private static BigInteger concat(int i, int j)
	{
		return new BigInteger(i + "" + j);
	}

	private static int[] getPrimes(int limit)
	{
		// Assume all numbers are prime except 0 and 1
		int primeCount = 0;
		boolean[] isPrime = new boolean[limit];
		for(int i = 0; i < limit; i++)
		{
			isPrime[i] = true;
			primeCount++;
		}
		isPrime[0] = false;
		isPrime[1] = false;
		primeCount -= 2;
		
		// Weed out multiples of primes
		for(int i = 0; i < limit; i++)
		{
			if(isPrime[i])
			{
				for(int j = i+1; j < limit; j++)
				{
					if(j % i == 0)
					{
						if(isPrime[j])
							primeCount--;
						isPrime[j] = false;
					}
				}
			}
		}
		
		// Copy primes to new array
		int[] primes = new int[primeCount];
		int primeIndex = 0;
		for(int i = 0; i < limit; i++)
		{
			if(isPrime[i])
			{
				primes[primeIndex++] = i;
			}
		}
		
		return primes;
	}
}
