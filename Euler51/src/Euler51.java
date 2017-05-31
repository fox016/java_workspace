
public class Euler51
{
	public static char[] primeEndings = {'1', '3', '7', '9', '*'};
	public static char[] numbers = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '*'}; 
	
	public static void main(String[] args)
	{
		long start = System.currentTimeMillis();
		
		for(int first = 1; first < numbers.length; first++)
		{
			for(int second = 0; second < numbers.length; second++)
			{
				for(int third = 0; third < numbers.length; third++)
				{
					for(int fourth = 0; fourth < numbers.length; fourth++)
					{
						for(int fifth = 0; fifth < numbers.length; fifth++)
						{
							for(int last = 0; last < primeEndings.length; last++)
							{
								String input = "" + numbers[first] + numbers[second] + numbers[third] + numbers[fourth] + numbers[fifth] + primeEndings[last];
								int primeFamilyCount = getPrimeFamilyCount(input);
								if(primeFamilyCount == 8)
								{
									long end = System.currentTimeMillis();
									System.out.println("N: " + input + ", Count: " + primeFamilyCount);
									System.out.println("Time: " + (end - start) + " ms");
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	private static int getPrimeFamilyCount(String input)
	{
		if(!input.contains("*")) return 0;
		
		int total = 0;
		for(char c = '0'; c <= '9'; c++)
		{
			if(c == '0' && input.charAt(0) == '*')
				continue;
			int n = Integer.parseInt(input.replace('*', c));
			if(MillerRabin32.miller_rabin_32(n))
			{
				if(input.equals("*2*3*3"))
					System.out.println(n);
				total++;
			}
		}
		return total;
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
