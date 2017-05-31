import java.util.Iterator;
import java.util.TreeSet;

class Euler49 {
	
	public static TreeSet<Integer> primes = getPrimes(1000, 10000);
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Iterator<Integer> iter = primes.iterator();
		while(iter.hasNext()) {
			int p = iter.next();
			Integer[] perms = new Integer[11];
			getPrimePermutations(p).toArray(perms);
			for(int high = 3; high < 11; high++)
				for(int mid = 1; mid < high; mid++)
					for(int low = 0; low < mid; low++)
						if(perms[high] != null && perms[mid] != null && perms[low] != null)
							if(perms[high] - perms[mid] == perms[mid] - perms[low])
								System.out.println(perms[low] + "" + perms[mid] + "" + perms[high]);
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println("Time: " + time + " ms");
	}
	
	private static TreeSet<Integer> getPrimes(int start, int end) {
		TreeSet<Integer> primes = new TreeSet<Integer>();
		for(int x = start; x < end; x++) 
			if(MillerRabin32.miller_rabin_32(x))
				primes.add(x);
		return primes;
	}
	
	private static TreeSet<Integer> getPrimePermutations(int n) {
		TreeSet<Integer> perms = new TreeSet<Integer>();
		char[] chars = (""+n).toCharArray();
		for(int a = 0; a < 4; a++)
			for(int b = 0; b < 4; b++)
				if(a != b)
					for(int c = 0; c < 4; c++)
						if(a != c && b != c)
							for(int d = 0; d < 4; d++)
								if (a != d && b != d && c != d) {
									char[] temp = {chars[a], chars[b], chars[c], chars[d]};
									int perm = Integer.parseInt(new String(temp));
									if(primes.contains(perm))
										perms.add(perm);
								}
		return perms;
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