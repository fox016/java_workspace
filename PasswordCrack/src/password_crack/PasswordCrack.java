package password_crack;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashSet;
import java.util.Set;

public class PasswordCrack
{
	private static MessageDigest md;
	private static Set<String> hashes;
	private static final char[] hexArray = "0123456789abcdef".toCharArray();
	private static final char[] lowercase = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final char[] uppercase = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
	
	public static void main(String[] args) throws NoSuchAlgorithmException, IOException
	{
		hashes = new HashSet<>();
		hashes.add("bc90a50b2fa0d31ce6125f9ca1fb49983accd9400aeb1ea193af349c9be5f08d");
		hashes.add("1c7ed7db63641bd51e6e18fe8b0bad974272fc4e5ea59744707acace81a49b6b");
		hashes.add("001950779868ab537674c0b64cfa6c84942563458046b6253a8b705718ec4edb");
		hashes.add("28354ed5c940f96266292929fa541f23f03eb2ac248239166677c7342e5f59cc");
		hashes.add("1708d954f24efed9dafe514f7249e01642d8a63f5030cd9f3642bfeae39cc820");
		
		md = MessageDigest.getInstance("SHA-256");
		
		long t1 = System.currentTimeMillis();
		dictionaryAttack("/Users/Admin/Documents/dictionary.txt");
		long t2 = System.currentTimeMillis();
		System.out.println("Dictionary Attack: " + (t2-t1) + " ms");
		lowercaseAttack(8);
		long t3 = System.currentTimeMillis();
		System.out.println("Lowercase Attack: " + (t3-t2) + " ms");
		uppercaseAttack(8);
		long t4 = System.currentTimeMillis();
		System.out.println("Uppercase Attack: " + (t4-t3) + " ms");
	}
	
	private static void dictionaryAttack(String dictionaryFile) throws IOException
	{
		BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(dictionaryFile)));
		String word = reader.readLine();
		while(word != null)
		{
			word = word.toLowerCase();
			String hash = hash(word);
			if(hashes.contains(hash))
			{
				System.out.println(hash + ": " + word);
			}
			word = reader.readLine();
		}
	}
	
	private static void lowercaseAttack(int length) throws UnsupportedEncodingException
	{
		searchAllLength(lowercase, "", length);
	}
	
	private static void uppercaseAttack(int length) throws UnsupportedEncodingException
	{
		searchAllLength(uppercase, "", length);
	}
	
	private static String hash(String input) throws UnsupportedEncodingException
	{
		md.update(input.getBytes("UTF-8"));
		byte[] bytes = md.digest();
		char[] hexChars = new char[bytes.length * 2];
		for(int j = 0; j < bytes.length; j++)
		{
			int v = bytes[j] & 0xFF;
			hexChars[j*2] = hexArray[v >>> 4];
			hexChars[j*2+1] = hexArray[v & 0x0F];
		}
		return new String(hexChars);
	}
	
	private static void searchAllLength(char set[], String prefix, int k) throws UnsupportedEncodingException
	{
		if(k == 0)
		{
			String hash = hash(prefix);
			if(hashes.contains(hash))
				System.out.println(hash + ": " + prefix);
			return;
		}
		
		for(int i = 0; i < set.length; i++)
		{
			String newPrefix = prefix + set[i];
			searchAllLength(set, newPrefix, k-1);
		}
	}
}
