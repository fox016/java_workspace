package aes;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.params.KeyParameter;

public class HW7
{
	private static final int MAX_FILE_SIZE = 16000;
	private static final int BLOCK_SIZE = 16;
	private static AESEngine engine;
	
	public static void main(String[] args) throws IOException
	{
		// Get user input
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		String mode = "";
		while(!(mode.equals("ecb") || mode.equals("cbc")))
		{
			System.out.println("Enter mode (ecb or cbc): ");
			mode = reader.readLine();
		}
		System.out.println("Enter char for padding: ");
		char padding = reader.readLine().charAt(0);
		
		// Read in file as byte array
		FileInputStream fis = new FileInputStream("/Users/Admin/Downloads/plaintext.txt");
		byte[] file = new byte[MAX_FILE_SIZE];
		int filesize = fis.read(file);
		if(filesize == -1)
			throw new EOFException("File is larger than " + MAX_FILE_SIZE + " bytes");
		
		// Separate into blocks
		List<byte[]> blocks = getBlocks(padding, file, filesize);
		
		// Initialize AES engine
		String secretKey = "Nate is awesome!";
		engine = new AESEngine();
		engine.init(true, new KeyParameter(secretKey.getBytes()));
		
		// Encrypt plaintext
		byte[] cipher = null;
		if(mode.equals("ecb"))
			cipher = ecbEncrypt(blocks);
		else if(mode.equals("cbc"))
			cipher = cbcEncrypt(blocks);
		
		// Display ciphertext
		System.out.print("PLN:  ");
		for(int i = 0; i < filesize; i++)
			System.out.printf("%02x ", file[i]);
		System.out.print("\nCPR:  ");
		for(int i = 0; i < filesize; i++)
			System.out.printf("%02x ", cipher[i]);
		
		// Separate ciphertext into blocks
		blocks = getBlocks(padding, cipher, cipher.length);
		
		// Set up AES engine for decryption
		engine.init(false, new KeyParameter(secretKey.getBytes()));
		
		// Decrypt ciphertext
		byte[] invCipher = null;
		if(mode.equals("ecb"))
			invCipher = ecbEncrypt(blocks);
		else if(mode.equals("cbc"))
			invCipher = cbcDecrypt(blocks);
		
		// Display inverse cipher text
		System.out.print("\nINV:  ");
		for(int i = 0; i < filesize; i++)
			System.out.printf("%02x ", invCipher[i]);
	}

	/**
	 * Get list of blocks
	 * 
	 * @param padding - char to be used for padding
	 * @param file - byte[] representing file (or other input)
	 * @param filesize - size of file (not including padding)
	 * @return ArrayList of byte[], each byte[] represeting a block
	 */
	private static List<byte[]> getBlocks(char padding, byte[] file, int filesize)
	{
		ArrayList<byte[]> blocks = new ArrayList<>();
		int index = 0;
		while(index < filesize)
		{
			byte[] block = new byte[BLOCK_SIZE];
			for(int j = 0; j < BLOCK_SIZE; j++)
			{
				if(index >= filesize)
					block[j] = (byte)padding;
				else
					block[j] = file[index];
				index++;
			}
			blocks.add(block);
		}
		return blocks;
	}

	/**
	 * Encrypt/decrypt input blocks using ECB mode (function
	 * 	depends on the state of the AES engine)
	 * 
	 * @param blocks - List of blocks (byte[]) to encrypt
	 * @return Ciphertext as byte[]
	 */
	private static byte[] ecbEncrypt(List<byte[]> blocks)
	{
		byte[] cipher = new byte[blocks.size() * BLOCK_SIZE];
		
		for(int i = 0; i < blocks.size(); i++)
		{
			byte[] blockCipher = new byte[BLOCK_SIZE];
			engine.processBlock(blocks.get(i), 0, blockCipher, 0);
			for(int j = 0; j < blocks.get(i).length; j++)
			{
				cipher[i*BLOCK_SIZE + j] = blockCipher[j];
			}
		}
		
		return cipher;
	}

	/**
	 * Encrypt input blocks using CBC mode (AES engine must be
	 * 	in encrypt mode)
	 * 
	 * @param blocks - List of blocks (byte[]) to encrypt
	 * @return Ciphertext as byte[]
	 */
	private static byte[] cbcEncrypt(List<byte[]> blocks)
	{
		String IV = "A fly went by...";
		byte[] prevCipherBlock = IV.getBytes();
		byte[] cipher = new byte[blocks.size() * BLOCK_SIZE];
		
		for(int i = 0; i < blocks.size(); i++)
		{
			byte[] blockCipher = new byte[BLOCK_SIZE];
			byte[] xor = xor(blocks.get(i), prevCipherBlock);
			engine.processBlock(xor, 0, blockCipher, 0);
			for(int j = 0; j < blocks.get(i).length; j++)
			{
				cipher[i*BLOCK_SIZE + j] = blockCipher[j];
			}
			prevCipherBlock = blockCipher;
		}
		
		return cipher;
	}
	
	/**
	 * Decrypt input blocks using CBC mode (AES engine must
	 * 	be in decrypt mode)
	 * 
	 * @param blocks
	 * @return
	 */
	private static byte[] cbcDecrypt(List<byte[]> blocks)
	{
		String IV = "A fly went by...";
		byte[] prevCipherBlock = IV.getBytes();
		byte[] cipher = new byte[blocks.size() * BLOCK_SIZE];
		
		for(int i = 0; i < blocks.size(); i++)
		{
			byte[] blockCipher = new byte[BLOCK_SIZE];
			engine.processBlock(blocks.get(i), 0, blockCipher, 0);
			byte[] xor = xor(blockCipher, prevCipherBlock);
			for(int j = 0; j < blocks.get(i).length; j++)
			{
				cipher[i*BLOCK_SIZE + j] = xor[j];
			}
			prevCipherBlock = blocks.get(i);
		}
		
		return cipher;
	}
	
	/**
	 * @param a
	 * @param b
	 * @return a ^ b
	 */
	private static byte[] xor(byte[] a, byte[]b)
	{
		byte[] xor = new byte[BLOCK_SIZE];
		for(int i = 0; i < BLOCK_SIZE; i++)
			xor[i] = (byte) (a[i] ^ b[i]);
		return xor;
	}
}
