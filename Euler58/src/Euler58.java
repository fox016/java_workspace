
public class Euler58 {
	
	private static double primeCount = 0;
	private static int totalCount = 1;
	private static int prevUR = 1;
	private static int prevUL = 1;
	private static int prevLR = 1;
	private static int prevLL = 1;
	private static int offsetUR = 0;
	private static int offsetUL = 1;
	private static int offsetLR = 3;
	private static int offsetLL = 3;
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int iter = 1;
		while(nextRatio() > .1) { iter++; }
		long end = System.currentTimeMillis();
		System.out.println(iter * 2 + 1);
		System.out.println("Time: " + (end - start));
	}
	
	private static double nextRatio() {
		int[] corners = new int[4];
		corners[0] = prevUR + (2 * offsetUR) + 2;
		corners[1] = prevUL + (2 * offsetUL) + 2;
		corners[2] = prevLR + (2 * offsetLR) + 2;
		corners[3] = prevLL + (2 * offsetLL);
		for(int i = 0; i < corners.length; i++)
			if(MillerRabin32.miller_rabin_32(corners[i]))
				primeCount++;
		prevUR = corners[0];
		prevUL = corners[1];
		prevLR = corners[2];
		prevLL = corners[3];
		offsetUR += 4;
		offsetUL += 4;
		offsetLR += 4;
		offsetLL += 4;
		totalCount += 4;
		return primeCount/totalCount;
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