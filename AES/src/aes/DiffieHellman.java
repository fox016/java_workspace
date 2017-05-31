package aes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.Random;

public class DiffieHellman
{
	private static final int P_SIZE = 500;
	private static final int CERTAINTY = 10;
	
	public static void main(String[] args) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		
		BigInteger p = generateLargePrime();
		BigInteger s = generateLargeInt();
		BigInteger g = new BigInteger("5");
		
		BigInteger modExp = modularExp(g, s, p);
		System.out.println(p);
		System.out.println(s);
		System.out.println(g);
		System.out.println(modExp);
		
		System.out.println("Enter Server Response:");
		BigInteger response = new BigInteger(reader.readLine());
		BigInteger key = modularExp(response, s, p);
		System.out.println("Shared Key:\n" + key);
	}
	
	/**
	 * @return A large prime (size defined by P_SIZE)
	 */
	private static BigInteger generateLargePrime()
	{
		return new BigInteger(P_SIZE, CERTAINTY, new Random());
	}
	
	/**
	 * @return A large integer (size defined by P_SIZE)
	 */
	private static BigInteger generateLargeInt()
	{
		return new BigInteger(P_SIZE, new Random());
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
