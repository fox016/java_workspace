package aes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;

public class HashAttack
{
	private static int DIGEST_SIZE;
	
	public static void main(String[] args)
	{
		for(DIGEST_SIZE = 1; DIGEST_SIZE <= 26; DIGEST_SIZE++)
		{
			BitSet preimageHash = getHash("Hello, world!");
			System.out.println("\"Hello, world!\" => " + getBinary(preimageHash));
			preimageAttack(preimageHash);
			collisionAttack();
		}
	}
	
	public static void collisionAttack()
	{
		long start = System.currentTimeMillis();
		Map<BitSet, String> hashToMessage = new HashMap<>();
		int i = 0;
		String prev = hashToMessage.put(getHash(i+""),  i+"");
		while(prev == null)
		{
			i++;
			prev = hashToMessage.put(getHash(i+""),  i+"");
		}
		long end = System.currentTimeMillis();
		System.out.println("Collision Message 1: \"" + prev + "\" => " + getBinary(getHash(prev)));
		System.out.println("Collision Message 2: \"" + i + "\" => " + getBinary(getHash(i+"")));
		System.out.println("Collision Time: " + (end - start) + " ms");
		System.out.println("Collision Iterations: " + i);
	}
	
	public static void preimageAttack(BitSet findHash)
	{
		long start = System.currentTimeMillis();
		int i = 0;
		BitSet hash = getHash(i + "");
		while(!hash.equals(findHash))
		{
			i++;
			hash = getHash(i + "");
		}
		long end = System.currentTimeMillis();
		System.out.println("Pre-image attack: \"" + i + "\" => " + getBinary(hash));
		System.out.println("Pre-image Time: " + (end - start) + " ms");
		System.out.println("Pre-image Iterations: " + i);
	}
	
	public static BitSet getHash(byte[] message)
	{
		MessageDigest digest = null;
		
		try {
			digest = MessageDigest.getInstance("SHA-1");
		}
		catch(NoSuchAlgorithmException e)
		{
			System.err.println("Could not find SHA-1 algorithm");
			System.exit(1);
		}
		
		BitSet hash = BitSet.valueOf(digest.digest(message));
		
		return hash.get(0, DIGEST_SIZE);
	}
	
	public static BitSet getHash(String message)
	{
		return getHash(message.getBytes());
	}
	
	public static String getBinary(BitSet b)
	{
		String binary = "";
		for(int i = DIGEST_SIZE-1; i >= 0; i--)
		{
			if(b.get(i))
				binary += "1";
			else
				binary += "0";
		}
		return binary;
	}
}
