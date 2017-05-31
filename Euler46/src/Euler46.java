import java.util.ArrayList;
import java.util.TreeSet;

public class Euler46 {
	
	public static ArrayList<Integer> squares = getSquares(1, 200);
	public static TreeSet<Integer> primes = getPrimes(1, 40000);
	public static ArrayList<Integer> oddComp = getOddComp(1, 80000);
	
	public static void main(String[] args) {
		for(int i = 0; i < oddComp.size(); i++)
			if(!isSum(oddComp.get(i)))
				System.out.println(oddComp.get(i));
	}
	
	private static boolean isSum(int n) {
		for(int i = 0; i < squares.size(); i++) {
			if(primes.contains(n - (2 * squares.get(i))))
				return true;
		}
		return false;
	}
	
	private static boolean isOddComposite(int n) {
		if(n % 2 == 1)
			if(!MillerRabin32.miller_rabin_32(n))
				return true;
		return false;
	}
	
	private static TreeSet<Integer> getPrimes(int start, int end) {
		TreeSet<Integer> primes = new TreeSet<Integer>();
		for(int x = start; x < end; x++) 
			if(MillerRabin32.miller_rabin_32(x))
				primes.add(x);
		return primes;
	}
	
	private static ArrayList<Integer> getOddComp(int start, int end) {
		ArrayList<Integer> oddComp = new ArrayList<Integer>();
		for(int x = start; x < end; x++)
			if(isOddComposite(x))
				oddComp.add(x);
		return oddComp;
	}
	
	private static ArrayList<Integer> getSquares(int start, int end) {
		ArrayList<Integer> squares = new ArrayList<Integer>();
		for(int x = start; x < end; x++)
			squares.add(x * x);
		return squares;
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
