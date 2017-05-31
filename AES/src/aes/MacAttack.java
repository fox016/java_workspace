package aes;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class MacAttack {
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		
		String originalMessage = "No one has completed lab 2 so give them all a 0";
		byte[] originalBytes = originalMessage.getBytes();

		String extension = ". P.S. Except for Nathan Fox, just give him an A in the course";
		byte[] extensionBytes = extension.getBytes();
		SHA1 attackSha = new SHA1(0xf4b645e8, 0x9faaec2f, 0xf8e443c5,
				0x95009c16, 0xdbdfba4b);
		byte[] newDigest = attackSha.digest(extension.getBytes());
		System.out.println("\n\nNew Digest: " + getHexString(newDigest));

		int length = (originalBytes.length * 8) + 128;
		int paddingBytes = 1;
		while (((length + paddingBytes) % 512) != (448 % 512))
			paddingBytes++;
		paddingBytes /= 8;

		System.out.println("Key bytes: " + 128 / 8);
		System.out.println("Original bytes: " + originalBytes.length);
		System.out.println("Padding bytes: " + paddingBytes);
		System.out.println("Length bytes: " + 8);
		System.out.println("Total bytes: "
				+ ((128 / 8) + originalBytes.length + paddingBytes + 8));
		System.out.println("Total bits: "
				+ ((128 / 8) + originalBytes.length + paddingBytes + 8) * 8);

		byte[] newMessageBytes = new byte[originalBytes.length + paddingBytes
				+ 8 + extensionBytes.length];
		int newIndex = 0;

		// Copy original message
		System.out.println("---------------------Copy original...");
		for (int i = 0; i < originalBytes.length; i++)
			newMessageBytes[newIndex++] = originalBytes[i];
		System.out.println(getHexString(newMessageBytes));

		// Pad with 0x80000... (same as 0b100000...)
		System.out.println("---------------------Pad...");
		newMessageBytes[newIndex++] = (byte) 0x00000080;
		paddingBytes--;
		for (int i = 0; i < paddingBytes; i++)
			newMessageBytes[newIndex++] = 0x00;
		System.out.println(getHexString(newMessageBytes));

		// Add length at the end (include key in length)
		System.out.println("---------------------Add length...");
		System.out.println("Length: " + (length-128) + " + 128 = " + length + " (0x" + Integer.toHexString(length) + ")");
		for (int i = 0; i < 4; i++)
			newMessageBytes[newIndex++] = 0x00;
		Word lengthBin = new Word(length);
		for (int i = 0; i < 4; i++)
			newMessageBytes[newIndex++] = lengthBin.getByte(i);
		System.out.println(getHexString(newMessageBytes));

		// Add extension
		System.out.println("---------------------Add extension...");
		for (int i = 0; i < extensionBytes.length; i++)
			newMessageBytes[newIndex++] = extensionBytes[i];
		System.out.println(getHexString(newMessageBytes));

		System.out.println("\n---------------------First 2 blocks (assuming Bob prepends 128-bit key):");
		System.out.println(getHexString(newMessageBytes, (1024-128)));
		System.out.println("---------------------New Message Bytes:");
		for (Byte b : newMessageBytes)
			System.out.print(String.format("%02x", b));
		System.out.println();
		String newMessage = new String(newMessageBytes);
		System.out.println(new BigInteger(newMessage.getBytes()).toString(16));
		System.out.println(new BigInteger(newMessageBytes).toString(16));
		System.out.println("\nNew Message: " + newMessage);
	}

	/**
	 * @param bytes
	 * @return String representing hex value of byte array
	 */
	public static String getHexString(byte[] bytes) {
		String hex = "";
		for (Byte b : bytes) {
			String bHex = Integer.toHexString(b);
			if (bHex.length() > 1)
				bHex = bHex.substring(bHex.length() - 2);
			else if (bHex.length() == 1)
				bHex = "0" + bHex;
			else
				throw new IllegalStateException("That's just wrong");
			hex += bHex;
		}
		return hex;
	}

	/**
	 * @param bytes
	 * @param bitCount - number of bits to return
	 * @return String representing hex value of byte array
	 */
	public static String getHexString(byte[] bytes, int bitCount) {
		String hex = "";
		for (int i = 0; i < (bitCount/8); i++) {
			String bHex = Integer.toHexString(bytes[i]);
			if (bHex.length() > 1)
				bHex = bHex.substring(bHex.length() - 2);
			else if (bHex.length() == 1)
				bHex = "0" + bHex;
			else
				throw new IllegalStateException("That's just wrong");
			hex += bHex;
		}
		return hex;
	}
}

class SHA1 {
	
	private final static boolean _DEBUG = true;
	private int H0, H1, H2, H3, H4;

	public SHA1() {
		H0 = 0x67452301;
		H1 = 0xEFCDAB89;
		H2 = 0x98BADCFE;
		H3 = 0x10325476;
		H4 = 0xC3D2E1F0;
	}

	/**
	 * New constructor used to override initialization vector
	 * 
	 * @param h0
	 * @param h1
	 * @param h2
	 * @param h3
	 * @param h4
	 */
	public SHA1(int h0, int h1, int h2, int h3, int h4) {
		H0 = h0;
		H1 = h1;
		H2 = h2;
		H3 = h3;
		H4 = h4;
	}

	public byte[] digest(byte[] message) {

		if (_DEBUG) {
			System.out.println("\n--------- DEBUG ---------");
			System.out.println("Original Hex: "
					+ MacAttack.getHexString(message));
			System.out.println("Original Length: " + message.length * 8
					+ " (0x" + Integer.toHexString(message.length * 8) + ")");
		}

		byte[] block = new byte[64];
		byte[] padded = new byte[(message.length + 64 - (message.length % 64))];
		byte[] hashed = new byte[20];
		int A, B, C, D, E, F, K;

		// Append 0b10000... to the message
		padded = padMessage(message);

		for (int x = 0; x < padded.length / 64; x++) {

			if (_DEBUG) {
				Word[] currentState = new Word[5];
				currentState[0] = new Word(H0);
				currentState[1] = new Word(H1);
				currentState[2] = new Word(H2);
				currentState[3] = new Word(H3);
				currentState[4] = new Word(H4);
				System.out.print("Current state: ");
				for (Word w : currentState)
					System.out.print(w.getHex());
				System.out.println();
			}

			System.arraycopy(padded, 64 * x, block, 0, 64);

			int[] words = new int[80];
			for (int j = 0; j < 16; j++) {
				words[j] = 0;
				for (int k = 0; k < 4; k++) {
					words[j] |= ((block[j * 4 + k] & 0x000000FF) << (24 - k * 8));
				}
			}

			for (int j = 16; j < 80; j++) {
				words[j] = Integer.rotateLeft((words[j - 3] ^ words[j - 8]
						^ words[j - 14] ^ words[j - 16]), 1);
			}

			A = H0;
			B = H1;
			C = H2;
			D = H3;
			E = H4;

			for (int i = 0; i < 80; i++) {
				if (i < 20) {
					F = (B & C) | (~B & D);
					K = 0x5A827999;
				} else if (19 < i && i < 40) {
					F = B ^ C ^ D;
					K = 0x6ED9EBA1;
				} else if (39 < i && i < 60) {
					F = (B & C) | (B & D) | (C & D);
					K = 0x8F1BBCDC;
				} else {
					F = B ^ C ^ D;
					K = 0xCA62C1D6;
				}
				int temp = Integer.rotateLeft(A, 5) + F + E + K + words[i];
				E = D;
				D = C;
				C = Integer.rotateLeft(B, 30);
				B = A;
				A = temp;
			}
			H0 += A;
			H1 += B;
			H2 += C;
			H3 += D;
			H4 += E;
		}

		hashed[0] = (byte) ((H0 >>> 24) & 0xff);
		hashed[1] = (byte) ((H0 >>> 16) & 0xff);
		hashed[2] = (byte) ((H0 >>> 8) & 0xff);
		hashed[3] = (byte) (H0 & 0xff);
		hashed[4] = (byte) ((H1 >>> 24) & 0xff);
		hashed[5] = (byte) ((H1 >>> 16) & 0xff);
		hashed[6] = (byte) ((H1 >>> 8) & 0xff);
		hashed[7] = (byte) (H1 & 0xff);
		hashed[8] = (byte) ((H2 >>> 24) & 0xff);
		hashed[9] = (byte) ((H2 >>> 16) & 0xff);
		hashed[10] = (byte) ((H2 >>> 8) & 0xff);
		hashed[11] = (byte) (H2 & 0xff);
		hashed[12] = (byte) ((H3 >>> 24) & 0xff);
		hashed[13] = (byte) ((H3 >>> 16) & 0xff);
		hashed[14] = (byte) ((H3 >>> 8) & 0xff);
		hashed[15] = (byte) (H3 & 0xff);
		hashed[16] = (byte) ((H4 >>> 24) & 0xff);
		hashed[17] = (byte) ((H4 >>> 16) & 0xff);
		hashed[18] = (byte) ((H4 >>> 8) & 0xff);
		hashed[19] = (byte) (H4 & 0xff);

		if (_DEBUG) {
			System.out.println("--------- END DEBUG ---------\n");
		}

		return hashed;
	}

	private static byte[] padMessage(byte[] data) {
		
		int origLength = data.length;
		int tailLength = origLength % 64;
		int padLength = 0;
		if ((64 - tailLength >= 9))
			padLength = 64 - tailLength;
		else
			padLength = 128 - tailLength;
		byte[] thePad = new byte[padLength];
		thePad[0] = (byte) 0x80;
		long lengthInBits = (origLength * 8) + 1024; // Add 1024 to account for first 2 blocks
		for (int i = 0; i < 8; i++) {
			thePad[thePad.length - 1 - i] = (byte) ((lengthInBits >> (8 * i)) & 0xFF);
		}

		byte[] output = new byte[origLength + padLength];

		System.arraycopy(data, 0, output, 0, origLength);
		System.arraycopy(thePad, 0, output, origLength, thePad.length);

		if (_DEBUG) {
			System.out.println("Padded Hex: " + MacAttack.getHexString(output));
			System.out.println("Padded Length: " + output.length * 8);
		}
		return output;
	}
}

class Word {
	
	private byte[] word;
	
	/**
	 * Create a new Word from byte array
	 * 
	 * @param word
	 */
	public Word(byte[] word)
	{
		this.word = word;
	}
	
	/**
	 * Create a new Word from int
	 * 
	 * @param n
	 */
	public Word(int n)
	{
		word = new byte[4];
		for(int i = 3; i >= 0; i--)
		{
			word[i] = (byte) n;
			n = n >>> 8;
		}
	}
	
	/**
	 * @return byte array representation
	 */
	public byte[] getWord()
	{
		return word;
	}
	
	/**
	 * @param index [0, word.length)
	 * @return byte at index
	 */
	public byte getByte(int index)
	{
		if(index >= word.length || index < 0)
			throw new IllegalArgumentException("Index out of bounds: " + index);
		
		return word[index];
	}
	
	/**
	 * Sets value of word at index
	 * 
	 * @param index [0, word.length)
	 * @param b
	 */
	public void setByte(int index, byte b)
	{
		if(index >= word.length || index < 0)
			throw new IllegalArgumentException("Index out of bounds: " + index);
		
		word[index] = b;
	}
	
	/**
	 * @return word size
	 */
	public int size()
	{
		return word.length;
	}
	
	/**
	 * @return word with first byte shifted to end
	 */
	public Word rotWord()
	{
		byte[] temp = new byte[word.length];
		for(int i = 0; i + 1 < word.length; i++)
		{
			temp[i] = word[i+1];
		}
		temp[word.length - 1] = word[0];
		return new Word(temp);
	}
	
	/**
	 * @param other
	 * @return this ^ other
	 */
	public Word xor(Word other)
	{
		byte[] temp = new byte[word.length];
		for(int i = 0; i < word.length; i++)
		{
			temp[i] = (byte) (word[i] ^ other.getByte(i));
		}
		return new Word(temp);
	}

	/**
	 * @param other
	 * @return this | other
	 */
	public Word or(Word other)
	{
		byte[] temp = new byte[word.length];
		for(int i = 0; i < word.length; i++)
		{
			temp[i] = (byte) (word[i] | other.getByte(i));
		}
		return new Word(temp);
	}

	/**
	 * @param other
	 * @return this & other
	 */
	public Word and(Word other)
	{
		byte[] temp = new byte[word.length];
		for(int i = 0; i < word.length; i++)
		{
			temp[i] = (byte) (word[i] & other.getByte(i));
		}
		return new Word(temp);
	}

	/**
	 * @return ~this
	 */
	public Word not()
	{
		byte[] temp = new byte[word.length];
		for(int i = 0; i < word.length; i++)
		{
			temp[i] = (byte) ~(word[i]);
		}
		return new Word(temp);
	}

	/**
	 * @param other
	 * @return (other + this) % (2^32)
	 */
	public Word add(Word other)
	{
		long sum = (long)this.getInt() + (long)other.getInt();
		double pow = Math.pow(2, 32);
		
		while(sum > pow) sum -= pow;
		
		return new Word((int)sum);
	}

	/**
	 * @param n
	 * @return left bit shift n spaces
	 */
	public Word shiftLeft(int n) {
		return new Word(this.getInt() << n);
	}

	/**
	 * @param n
	 * @return right bit shift n spaces
	 */
	public Word shiftRight(int n) {
		return new Word(this.getInt() >> n);
	}
	
	/**
	 * @return hexadecimal representation (String)
	 */
	public String getHex()
	{
		String hex = "";
		for(int i = 0; i < word.length; i++)
			hex += String.format("%02x", word[i]);
		return hex;
	}
	
	/**
	 * @return integer value
	 */
	public int getInt()
	{
		int factor = 1;
		int total = 0;
		for(int i = word.length-1; i >= 0; i--)
		{
			total += ((word[i] & 0xFF) * factor);
			factor *= 256;
		}
		return total;
	}
	
	@Override
	public String toString()
	{
		return "0x" + getHex();
	}
}
