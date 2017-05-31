package aes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

public class RSA
{
	private static final int P_SIZE = 512;
	private static final int CERTAINTY = 15;
	
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		BigInteger e, d, p, q, n, phi;
		e = p = q = n = phi = null;
		
		// Choose new p and q until (p-1)(q-1) relatively prime to e
		boolean valid = false;
		while(!valid)
		{
			// Generate large numbers
			e = new BigInteger("65537");
			p = generateLargePrime();
			q = generateLargePrime();
			phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
			
			// Verify (p-1)(q-1) is relatively prime to e
			valid = isRelativePrime(phi, e);
		}
		
		// Calculate d
		n = p.multiply(q);
		BigInteger[] inverse = calcInverse(e, phi);
		d = inverse[0];
		
		System.out.println(p + "\n" + q + "\n" + n + "\n" + e + "\n" + d);
		
		// Verify ((m^e%n)^d)%n == m for 0 < m < n
		for(int i = 0; i < 20; i++)
		{
			BigInteger m = (new BigInteger(P_SIZE, new Random())).mod(n);
			if(!m.equals(modularExp(modularExp(m, e, n), d, n)))
			{
				String error = "Verification failed:\n";
				error += "m: " + m + "\n";
				error += "e: " + e + "\n";
				error += "n: " + n + "\n";
				error += "d: " + d + "\n";
				throw new IllegalStateException(error);
			}
		}
		
		// Encrypt
		System.out.println("Enter message to encrypt:");
		BigInteger m = new BigInteger(reader.readLine());
		BigInteger c = modularExp(m, e, n);
		System.out.println("Cipher:\n" + c);
		
		// Decrypt
		System.out.println("Enter message to decrypt:");
		c = new BigInteger(reader.readLine());
		m = modularExp(c, d, n);
		System.out.println("Message:\n" + m);
	}
	
	/**
	 * @param e
	 * @param phi
	 * @return d where ed = 1 mod phi
	 */
	private static BigInteger[] calcInverse(BigInteger e, BigInteger phi)
	{
		System.out.println(e + "x - " + phi + "y = 1");
		BigInteger[] solution = new BigInteger[2];
		
		if(phi.equals(BigInteger.ZERO))
		{
			solution[0] = BigInteger.ONE;
			solution[1] = BigInteger.ZERO;
			return solution;
		}
		BigInteger[] qr = e.divideAndRemainder(phi);
		System.out.println("((" + phi + " * " + qr[0] + " + " + qr[1] + ")x - " + phi + "y = 1");
		BigInteger[] st = calcInverse(phi, qr[1]);
		solution[0] = st[1];
		System.out.println("solution: " + solution[0]);
		solution[1] = st[0].subtract(qr[0].multiply(st[1]));
		return solution;
	}

	/**
	 * @param a
	 * @param b
	 * @return true iff a and b are relatively prime
	 */
	private static boolean isRelativePrime(BigInteger a, BigInteger b)
	{
		BigInteger gcd = GCD(a, b);
		return gcd.equals(BigInteger.ONE);
	}
	
	/**
	 * @param a 
	 * @param b
	 * @return gcd of (a, b)
	 */
	private static BigInteger GCD(BigInteger a, BigInteger b)
	{
		// Ensure a >= b
		if(a.compareTo(b) < 0)
		{
			BigInteger temp = a;
			a = b;
			b = temp;
		}
		
		return GCDcalc(a, b);
	}
	
	/**
	 * @param a (must be >= b)
	 * @param b (must be <= a)
	 * @return gcd of (a, b)
	 */
	private static BigInteger GCDcalc(BigInteger a, BigInteger b)
	{
		BigInteger r = a.remainder(b);
		if(r.equals(BigInteger.ZERO))
			return b;
		return GCD(b, r);
	}
	
	/**
	 * Get 512-bit prime, high order bit set
	 * @return A large prime (size defined by P_SIZE)
	 */
	private static BigInteger generateLargePrime()
	{
		BigInteger TWO = new BigInteger("2");
		BigInteger i = new BigInteger(P_SIZE, CERTAINTY, new Random());
		BigInteger big = TWO.pow(511);
		if(i.compareTo(big) < 0)
			throw new IllegalStateException("High order bit not set:\n" + i + "\n" + big);
		return i;
	}
	
	/**
	 * @param base
	 * @param exp
	 * @param mod
	 * @return base^exp % mod
	 */
	private static BigInteger modularExp(BigInteger base, BigInteger exp, BigInteger mod)
	{
		if(exp.equals(BigInteger.ZERO))
			return BigInteger.ONE;
		BigInteger TWO = new BigInteger("2");
		BigInteger z = modularExp(base, exp.divide(TWO), mod);
		if(exp.mod(TWO).equals(BigInteger.ZERO))
			return (z.multiply(z)).mod(mod);
		return (base.multiply(z).multiply(z)).mod(mod);
	}
}
