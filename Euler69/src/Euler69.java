import java.util.ArrayList;

class Euler69 {
	
	private static int high_n = 6;
	private static double high_ratio = 3;
	private static ArrayList<Integer> primes = getPrimes(2, 1000000);
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int x = 1; x <= 1000000; x++) {
			double tempRatio = ((double)x) / phi(x);
			if(tempRatio > high_ratio) {
				high_n = x;
				high_ratio = tempRatio;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("N: " + high_n + ", Ratio: " + high_ratio);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static long phi(int n) {
		double total = 1;
		ArrayList<Integer> factors = getFactors(n);
		for(int i = 0; i < factors.size(); i++)
			total *= (1 - (((double)1)/factors.get(i)));
		return Math.round(n * total);
	}
	
	private static ArrayList<Integer> getFactors(int n) {
		ArrayList<Integer> factors = new ArrayList<Integer>();
		while(n != 1) {
			int prime = -1;
			int i = 0;
			while(prime == -1) {
				if(n % primes.get(i) == 0)
					prime = primes.get(i);
				i++;
			}
			do 
				n /= prime;
			while(n % prime == 0);
			factors.add(prime);
		}
		return factors;
	}
	
	private static ArrayList<Integer> getPrimes(int start, int end) {
		ArrayList<Integer> primes = new ArrayList<Integer>();
		for(int x = start; x < end; x++) 
			if(MillerRabin32.miller_rabin_32(x))
				primes.add(x);
		return primes;
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