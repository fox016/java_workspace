package euler124;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class Euler124
{
	public static List<Integer> primes = getPrimes(1, 100000);
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		List<Radical> rads = new ArrayList<>();
		for(int i = 1; i <= 100000; i++)
		{
			rads.add(new Radical(i, rad(i)));
		}
		Collections.sort(rads);
		
		long end = System.currentTimeMillis();
		
		System.out.println("Solution: " + rads.get(9999).getN());
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static int rad(int n)
	{
		HashSet<Integer> divisors = new HashSet<>();
		for(Integer p : primes)
		{
			while(n % p == 0)
			{
				divisors.add(p);
				n /= p;
				if(n == 1)
					break;
			}
		}
		
		int rad = 1;
		for(Integer d : divisors)
		{
			rad *= d;
		}
		
		return rad;
	}
	
	private static List<Integer> getPrimes(int start, int end)
	{
		ArrayList<Integer> primes = new ArrayList<Integer>();
		for(int x = start; x < end; x++) 
			if(MillerRabin32.miller_rabin_32(x))
				primes.add(x);
		return primes;
	}
}

class Radical implements Comparable<Radical>
{
	private int n;
	private int rad;
	
	public Radical(int n, int rad)
	{
		this.n = n;
		this.rad = rad;
	}
	
	public int getRad()
	{
		return rad;
	}
	
	public int getN()
	{
		return n;
	}

	@Override
	public int compareTo(Radical other)
	{
		return rad - other.getRad();
	}
	
	@Override
	public String toString()
	{
		return "N: " + n + ", " + "Rad: " + rad + "\n";
	}
}

class MillerRabin32
{
    private static int modular_exponent_32(int base, int power, int modulus) {
        long result = 1;
        for (int i = 31; i >= 0; i--) {
            result = (result*result) % modulus;
            if ((power & (1 << i)) != 0) {
                result = (result*base) % modulus;
            }
        }
        return (int)result; // Will not truncate since modulus is an int
    }


    private static boolean miller_rabin_pass_32(int a, int n) {
        int d = n - 1;
        int s = Integer.numberOfTrailingZeros(d);
        d >>= s;
        int a_to_power = modular_exponent_32(a, d, n);
        if (a_to_power == 1) return true;
        for (int i = 0; i < s-1; i++) {
            if (a_to_power == n-1) return true;
            a_to_power = modular_exponent_32(a_to_power, 2, n);
        }
        if (a_to_power == n-1) return true;
        return false;
    }

    public static boolean miller_rabin_32(int n) {
        if (n <= 1) return false;
        else if (n == 2) return true;
        else if (miller_rabin_pass_32( 2, n) &&
            (n <= 7  || miller_rabin_pass_32( 7, n)) &&
            (n <= 61 || miller_rabin_pass_32(61, n)))
            return true;
        else
            return false;
    }
}
