import java.util.Iterator;
import java.util.TreeSet;
import java.math.BigInteger;

class Euler66 {

	public static TreeSet<Integer> primes = getPrimes(2, 1000);
	
	public static void main(String[] args) {
		
		Iterator<Integer> iter = primes.iterator();
		BigInteger highPell = BigInteger.ZERO;
		int highD = 0;
		while(iter.hasNext()) {
			int d = iter.next();
			BigInteger pell = pell(d);
			System.out.println("D: " + d + ", Pell: " + pell);
			if(pell.compareTo(highPell) > 0) {
				highPell = pell;
				highD = d;
			}
		}
		System.out.println("High Pell: " + highPell + ", High D: " + highD);
	}
	
	private static BigInteger pell(int d) {
		
		BigInteger p = BigInteger.ONE;
		BigInteger k = BigInteger.ONE;
		BigInteger x1 = BigInteger.ONE;
		BigInteger y = BigInteger.ZERO;
		BigInteger sd = new BigInteger(Math.sqrt(d) +"");
		
		while(!k.equals(BigInteger.ONE) || y.equals(BigInteger.ZERO)) {
			
			p = (k.multiply(((p.divide(k)).add(BigInteger.ONE)))).subtract(p);
			p = p.subtract(((p.subtract(sd))).divide(k)).multiply(k);
			
			double x = (p * x1 + d * y) / Math.abs(k);
			y = (p * y + x1) / Math.abs(k);
			k = ((p * p - d) / k);
			
			x1 = x;
		}
		
		return x1;
		
	}

	private static TreeSet<Integer> getPrimes(int start, int end) {
		TreeSet<Integer> primes = new TreeSet<Integer>();
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