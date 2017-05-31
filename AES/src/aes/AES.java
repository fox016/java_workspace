package aes;

public class AES
{
	private static int[][] sbox = {
		{ 0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76 } ,
		{ 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0 } ,
		{ 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15 } ,
		{ 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75 } ,
		{ 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84 } ,
		{ 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf } ,
		{ 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8 } ,
		{ 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2 } ,
		{ 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73 } ,
		{ 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb } ,
		{ 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79 } ,
		{ 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08 } ,
		{ 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a } ,
		{ 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e } ,
		{ 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf } ,
		{ 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16 }
	};

	private static int[][] invsbox = {
			{ 0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb } ,
			{ 0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb } ,
			{ 0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e } ,
			{ 0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25 } ,
			{ 0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92 } ,
			{ 0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84 } ,
			{ 0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06 } ,
			{ 0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b } ,
			{ 0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73 } ,
			{ 0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e } ,
			{ 0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b } ,
			{ 0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4 } ,
			{ 0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f } ,
			{ 0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef } ,
			{ 0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61 } ,
			{ 0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d }
	};
	
	private static int[] rcon = { 0x00000000, // Rcon[] is 1-based, so the first entry is just a place holder 
			0x01000000, 0x02000000, 0x04000000, 0x08000000, 
			0x10000000, 0x20000000, 0x40000000, 0x80000000, 
			0x1B000000, 0x36000000, 0x6C000000, 0xD8000000, 
			0xAB000000, 0x4D000000, 0x9A000000, 0x2F000000, 
			0x5E000000, 0xBC000000, 0x63000000, 0xC6000000, 
			0x97000000, 0x35000000, 0x6A000000, 0xD4000000, 
			0xB3000000, 0x7D000000, 0xFA000000, 0xEF000000, 
			0xC5000000, 0x91000000, 0x39000000, 0x72000000, 
			0xE4000000, 0xD3000000, 0xBD000000, 0x61000000, 
			0xC2000000, 0x9F000000, 0x25000000, 0x4A000000, 
			0x94000000, 0x33000000, 0x66000000, 0xCC000000, 
			0x83000000, 0x1D000000, 0x3A000000, 0x74000000, 
			0xE8000000, 0xCB000000, 0x8D000000};
	
	private static int nr = -1;
	
	public static void main(String[] args)
	{
		/*
		byte b1 = (byte) 0x57;
		byte b2 = (byte) 0x83;
		
		// Test addition
		byte b3 = ffAdd(b1, b2);
		System.out.println(getHex(b3)); // D4
		
		// Test multiplication
		byte b4 = ffMultiply(b1, (byte)0x13);
		System.out.println(getHex(b4)); // FE
		
		// Test create Word from byte array
		byte[] bytes = {b1, b2, b3, b4};
		Word w1 = new Word(bytes);
		System.out.println(w1.getHex()); // 5783D4FE
		
		// Test subWord, rotWord, getHex, getInt
		Word w2 = subWord(w1);
		System.out.println(w2.getHex()); // 5BEC48BB
		System.out.println(w2.getInt()); // 1542211771
		System.out.println(w2.rotWord().getHex()); // EC48BB5B
		
		// Test create Word from int
		Word w3 = new Word(1542211771);
		System.out.println(w3.getHex()); // 5BEC48BB
		
		// Test 128-bit key expansion
		byte[] testKey128 = {0x2b, 0x7e, (byte) 0x15, (byte) 0x16,
						(byte) 0x28, (byte) 0xae, (byte) 0xd2, (byte) 0xa6,
						(byte) 0xab, (byte) 0xf7, (byte) 0x15, (byte) 0x88,
						(byte) 0x09, (byte) 0xcf, 0x4f, 0x3c};
		nr = getRoundCount(testKey128);
		Word[] words128 = keyExpansion(testKey128, testKey128.length / 4);
		System.out.println("\n----- Test 128-bit Key Expansion (A.1)");
		for(Word w : words128)
			System.out.println(w.getHex());
		
		// Test 192-bit key expansion
		byte[] testKey192 = {(byte) 0x8e, 0x73, (byte) 0xb0, (byte) 0xf7, (byte) 0xda, 0x0e, 0x64, 0x52,
							(byte) 0xc8, 0x10, (byte) 0xf3, 0x2b, (byte) 0x80, (byte) 0x90, 0x79, (byte) 0xe5,
							0x62, (byte) 0xf8, (byte) 0xea, (byte) 0xd2, 0x52, 0x2c, 0x6b, 0x7b};
		nr = getRoundCount(testKey192);
		Word[] words192 = keyExpansion(testKey192, testKey192.length / 4);
		System.out.println("\n----- Test 192-bit Key Expansion (A.2)");
		for(Word w : words192)
			System.out.println(w.getHex());
		
		// Test 256-bit key expansion
		byte[] testKey256 = {0x60, 0x3d, (byte) 0xeb, 0x10, 0x15, (byte) 0xca, 0x71, (byte) 0xbe, 0x2b, 0x73,
							(byte) 0xae, (byte) 0xf0, (byte) 0x85, 0x7d, 0x77, (byte) 0x81, 0x1f, 0x35, 0x2c, 0x07,
							0x3b, 0x61, 0x08, (byte) 0xd7, 0x2d, (byte) 0x98, 0x10, (byte) 0xa3, 0x09, 0x14,
							(byte) 0xdf, (byte) 0xf4};
		nr = getRoundCount(testKey256);
		Word[] words256 = keyExpansion(testKey256, testKey256.length / 4);
		System.out.println("\n----- Test 256-bit Key Expansion (A.3)");
		for(Word w : words256)
			System.out.println(w.getHex());

		// Test cipher/inverse on 16 byte string
		*/
		byte[] cipherBytes;
		byte[] invCipherBytes;
		/*
		nr = getRoundCount(testKey128);
		String plainText = "Nate is awesome!";
		cipherBytes = cipher(plainText.getBytes(), words128);
		System.out.println("\nPlain Text: " + plainText);
		invCipherBytes = invCipher(cipherBytes, words128);
		System.out.println("Inv Cipher Text: " + new String(invCipherBytes));
		
		// Test cipher/inverse on random length string
		plainText = "This is a super secret message.  Whoever can crack it is the coolest.";
		cipherBytes = cipher(plainText.getBytes(), words128);
		System.out.println("\nPlain Text: " + plainText);
		invCipherBytes = invCipher(cipherBytes, words128);
		String invStr = (new String(invCipherBytes)).trim();
		System.out.println("Inv Cipher Text: " + invStr);
		
		// Test cipher/inverse on 16 byte array
		System.out.println("\n----- Test Cipher Example (B)");
		byte[] inputBytes = {0x32, 0x43, (byte) 0xf6, (byte) 0xa8,
				(byte) 0x88, 0x5a, 0x30, (byte) 0x8d,
				0x31, 0x31, (byte) 0x98, (byte) 0xa2,
				(byte) 0xe0, 0x37, 0x07, 0x34};
		System.out.println("\nInput Bytes:");
		displayState(inputBytes);
		cipherBytes = cipher(inputBytes, words128);
		System.out.println("Cipher Bytes:");
		displayState(cipherBytes);
		invCipherBytes = invCipher(cipherBytes, words128);
		System.out.println("Inv Cipher Bytes:");
		displayState(invCipherBytes);
		
		// AES 128 Test
		System.out.println("\n----- Test AES-128 (C.1)");
		byte[] keyC1 = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
						0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f};
		nr = getRoundCount(keyC1);
		Word[] wordsC1 = keyExpansion(keyC1, keyC1.length / 4);
		*/
		byte[] inputC1 = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77, (byte) 0x88,
						(byte) 0x99, (byte) 0xaa, (byte) 0xbb, (byte) 0xcc, (byte) 0xdd,
						(byte) 0xee, (byte) 0xff};
		/*
		System.out.println("\nInput Bytes:");
		displayState(inputC1);
		cipherBytes = cipher(inputC1, wordsC1);
		System.out.println("Cipher Bytes:");
		displayState(cipherBytes);
		invCipherBytes = invCipher(cipherBytes, wordsC1);
		System.out.println("Inv Cipher Bytes:");
		displayState(invCipherBytes);
		
		// AES 192 Test
		System.out.println("\n----- Test AES-192 (C.2)");
		byte[] keyC2 = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
				0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
				0x14, 0x15, 0x16, 0x17};
		nr = getRoundCount(keyC2);
		Word[] wordsC2 = keyExpansion(keyC2, keyC2.length / 4);
		System.out.println("\nInput Bytes:");
		displayState(inputC1);
		cipherBytes = cipher(inputC1, wordsC2);
		System.out.println("Cipher Bytes:");
		displayState(cipherBytes);
		invCipherBytes = invCipher(cipherBytes, wordsC2);
		System.out.println("Inv Cipher Bytes:");
		displayState(invCipherBytes);
		*/
		
		// AES 256 Test
		System.out.println("\n----- Test AES-256 (C.3)");
		byte[] keyC3 = {0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08,
				0x09, 0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13,
				0x14, 0x15, 0x16, 0x17, 0x18, 0x19, 0x1a, 0x1b, 0x1c, 0x1d, 0x1e, 0x1f};
		nr = getRoundCount(keyC3);
		Word[] wordsC3 = keyExpansion(keyC3, keyC3.length / 4);
		System.out.println("\nInput Bytes:");
		displayState(inputC1);
		cipherBytes = cipher(inputC1, wordsC3);
		System.out.println("Cipher Bytes:");
		displayState(cipherBytes);
		invCipherBytes = invCipher(cipherBytes, wordsC3);
		System.out.println("Inv Cipher Bytes:");
		displayState(invCipherBytes);
	}
	
	/**
	 * @param key - 16, 24, or 32 byte key
	 * @param nk - number of words in the key (4, 6, or 8)
	 * @return array of words representing expanded key
	 */
	public static Word[] keyExpansion(byte[] key, int nk)
	{
		Word words[] = new Word[4 * (nr + 1)];
		
		for(int i = 0; i < nk; i++)
		{
			byte[] keyBytes = {key[4*i], key[4*i+1], key[4*i+2], key[4*i+3]};
			words[i] = new Word(keyBytes);
		}
		
		for(int i = nk; i < 4 * (nr + 1); i++)
		{
			Word temp = words[i - 1];
			if(i % nk == 0)
			{
				temp = subWord(temp.rotWord()).xor(new Word(rcon[i / nk]));
			}
			else if((nk > 6) && (i % nk == 4))
			{
				temp = subWord(temp);
			}
			words[i] = words[i - nk].xor(temp);
		}
		
		return words;
	}
	
	/**
	 * @param in - array of bytes representing plain text
	 * @param words - result of key expansion
	 * @return array of bytes representing cipher text
	 */
	public static byte[] cipher(byte[] in, Word[] words)
	{
		// Calculate number of 16-byte chunks
		int stateCount = (in.length / 16);
		if(in.length % 16 != 0)
			stateCount ++;
		byte[] cipherResult = new byte[stateCount * 16];
		
		int resultIndex = 0;
		
		// For each chunk:
		for(int i = 0; i < stateCount; i++)
		{
			// Fill chunk with input
			byte[] state = new byte[16];
			for(int s = 0; s < 16; s++)
			{
				if(in.length > i * 16 + s)
					state[s] = in[i * 16 + s];
				
				// Pad spaces at the end (if input doesn't divide evenly into chunks)
				else
					state[s] = ' ';
			}
			
			// Cipher chunk and append to cipherResult array
			byte[] cipherBytes = cipherState(state, words);
			for(int s = 0; s < 16; s++)
			{
				cipherResult[resultIndex++] = cipherBytes[s];
			}
		}
		
		return cipherResult;
	}
	
	/**
	 * @param in - array of bytes representing cipher text
	 * @param words - result of key expansion
	 * @return array of bytes representing plain text
	 */
	public static byte[] invCipher(byte[] in, Word[] words)
	{
		// Must be able to divide into 16-byte chunks
		if(in.length % 16 != 0)
			throw new IllegalArgumentException("Inverted cipher input length must be multiple of 16: " + in.length);
		
		byte[] invCipherResult = new byte[in.length];
		int resultIndex = 0;
		
		// For each chunk:
		for(int i = 0; i < in.length/16; i++)
		{
			// Fill chunk with input
			byte[] state = new byte[16];
			for(int s = 0; s < 16; s++)
			{
					state[s] = in[i * 16 + s];
			}
			
			// Inverse cipher chunk and append to invCipherResult array
			byte[] cipherBytes = invCipherState(state, words);
			for(int s = 0; s < 16; s++)
			{
				invCipherResult[resultIndex++] = cipherBytes[s];
			}
		}
		
		return invCipherResult;
	}

	/**
	 * @param state
	 * @param words
	 * @return byte[] representing ciphered state
	 */
	private static byte[] cipherState(byte[] state, Word[] words)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		state = addRoundKey(state, words, 0);
		
		for(int round = 1; round < nr; round++)
		{
			state = subBytes(state);
			state = shiftRows(state);
			state = mixColumns(state, true);
			state = addRoundKey(state, words, round);
		}

		state = subBytes(state);
		state = shiftRows(state);
		state = addRoundKey(state, words, nr);
		
		return state;
	}

	/**
	 * @param state
	 * @param words
	 * @return byte[] representing inverted ciphered state (original state)
	 */
	private static byte[] invCipherState(byte[] state, Word[] words)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		state = addRoundKey(state, words, nr);
		
		for(int round = nr-1; round >= 1; round--)
		{
			state = invShiftRows(state);
			state = invSubBytes(state);
			state = addRoundKey(state, words, round);
			state = mixColumns(state, false);
		}
		
		state = invShiftRows(state);
		state = invSubBytes(state);
		state = addRoundKey(state, words, 0);
		
		return state;
	}
	
	/**
	 * @param state
	 * @param forward - true iff you are calculating a cipher, false for inverse cipher
	 * @return state after matrix multiplication is applied
	 */
	private static byte[] mixColumns(byte[] state, boolean forward)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		for(int i = 0; i < 4; i++)
		{
			if(forward)
				state = mixColumn(state, i*4);
			else
				state = invMixColumn(state, i*4);
		}
		
		return state;
	}
	
	/**
	 * @param state
	 * @param start - Represents column [start, start+3]
	 * @return state after matrix multiplication is applyed to single column
	 */
	private static byte[] invMixColumn(byte[] state, int start)
	{
		byte[] newColumn = new byte[4];

		newColumn[0] = ffAdd(
							ffAdd(
								ffAdd(
										ffMultiply((byte)0x0E, state[start]),
										ffMultiply((byte)0x0B, state[start+1])
								),
								ffMultiply((byte)0x0D, state[start+2])
							),
							ffMultiply((byte)0x09, state[start+3])
						);

		newColumn[1] = ffAdd(
							ffAdd(
								ffAdd(
										ffMultiply((byte)0x09, state[start]),
										ffMultiply((byte)0x0E, state[start+1])
								),
								ffMultiply((byte)0x0B, state[start+2])
							),
							ffMultiply((byte)0x0D, state[start+3])
						);

		newColumn[2] = ffAdd(
							ffAdd(
								ffAdd(
										ffMultiply((byte)0x0D, state[start]),
										ffMultiply((byte)0x09, state[start+1])
								),
								ffMultiply((byte)0x0E, state[start+2])
							),
							ffMultiply((byte)0x0B, state[start+3])
						);

		newColumn[3] = ffAdd(
							ffAdd(
								ffAdd(
										ffMultiply((byte)0x0B, state[start]),
										ffMultiply((byte)0x0D, state[start+1])
								),
								ffMultiply((byte)0x09, state[start+2])
							),
							ffMultiply((byte)0x0E, state[start+3])
						);
		
		// Copy new column values into state
		for(int i = 0; i < 4; i++)
		{
			state[start+i] = newColumn[i];
		}
		
		return state;
	}
	
	/**
	 * @param state - A byte array representing the 16-byte state
	 * @param start - Represents column [start, start+3]
	 * @return State after matrix multiplication is applied to single column
	 */
	private static byte[] mixColumn(byte[] state, int start)
	{
		// Create temporary byte array newColumn (size 4) to represent new column values
		byte[] newColumn = new byte[4];

		// Hard-code linear result of matrix multiplication into newColumn
		newColumn[0] = ffAdd(ffAdd(ffAdd(ffMultiply((byte) 0x02, state[start]),
				ffMultiply((byte) 0x03, state[start+1])), state[start+2]), state[start+3]);
		newColumn[1] = ffAdd(ffAdd(ffAdd(ffMultiply((byte) 0x02, state[start+1]),
				ffMultiply((byte) 0x03, state[start+2])), state[start]), state[start+3]);
		newColumn[2] = ffAdd(ffAdd(ffAdd(ffMultiply((byte) 0x02, state[start+2]),
				ffMultiply((byte) 0x03, state[start+3])), state[start]), state[start+1]);
		newColumn[3] = ffAdd(ffAdd(ffAdd(ffMultiply((byte) 0x02, state[start+3]),
				ffMultiply((byte) 0x03, state[start])), state[start+2]), state[start+1]);

		// Copy new column values into state
		for(int i = 0; i < 4; i++)
			state[start+i] = newColumn[i];
			
		return state;
	}

	/**
	 * Shift second row left 1, third row left 2, fourth row right 1
	 * 
	 * @param state
	 * @return state after transformation
	 */
	private static byte[] shiftRows(byte[] state)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		// Rotate row 2 (left 1)
		byte temp1 = state[1];
		state[1] = state[5];
		state[5] = state[9];
		state[9] = state[13];
		state[13] = temp1;
		
		// Rotate row 3 (left 2)
		byte temp2 = state[2];
		byte temp6 = state[6];
		state[2] = state[10];
		state[6] = state[14];
		state[10] = temp2;
		state[14] = temp6;
		
		// Rotate row 4 (right 1)
		byte temp15 = state[15];
		state[15] = state[11];
		state[11] = state[7];
		state[7] = state[3];
		state[3] = temp15;
		
		return state;
	}

	/**
	 * Shift second row right 1, third row right 2, fourth row left 1
	 * 
	 * @param state
	 * @return state after transformation
	 */
	private static byte[] invShiftRows(byte[] state)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		// Rotate row 2 (right 1)
		byte temp13 = state[13];
		state[13] = state[9];
		state[9] = state[5];
		state[5] = state[1];
		state[1] = temp13;
		
		// Rotate row 3 (right 2)
		byte temp2 = state[2];
		byte temp6 = state[6];
		state[2] = state[10];
		state[6] = state[14];
		state[10] = temp2;
		state[14] = temp6;
		
		// Rotate row 4 (left 1)
		byte temp3 = state[3];
		state[3] = state[7];
		state[7] = state[11];
		state[11] = state[15];
		state[15] = temp3;
		
		return state;
	}

	/**
	 * Replace each byte of state with value from sbox
	 * 
	 * @param state
	 * @return substituted state
	 */
	private static byte[] subBytes(byte[] state)
	{
		for(int i = 0; i < state.length; i++)
		{
			state[i] = subBox(state[i]);
		}
		return state;
	}
	
	/**
	 * Replaces each byte of state with value from invsbox
	 * 
	 * @param state
	 * @return substituted state
	 */
	private static byte[] invSubBytes(byte[] state)
	{
		for(int i = 0; i < state.length; i++)
		{
			state[i] = invSubBox(state[i]);
		}
		return state;
	}

	/**
	 * @param state
	 * @param words - Result of key expansion
	 * @param round - Used as index to words to get round key
	 * @return state after XOR with round key
	 */
	private static byte[] addRoundKey(byte[] state, Word[] words, int round)
	{
		if(state.length != 16)
			throw new IllegalArgumentException("State must be 16 bytes, yours is: " + state.length);
		
		for(int column = 0; column < 4; column++)
		{
			for(int row = 0; row < 4; row++)
			{
				state[column*4 + row] = ffAdd(state[column*4 + row],
											words[round*4 + column].getByte(row));
			}
		}
		return state;
	}

	/**
	 * @param key
	 * @return number of rounds (based on key length)
	 */
	private static int getRoundCount(byte[] key)
	{
		int nk = key.length / 4;
		switch(nk)
		{
			case 4:
				return 10;
			case 6:
				return 12;
			case 8:
				return 14;
		}
		throw new IllegalArgumentException("Bad key: " + key);
	}

	/**
	 * @param b byte to replace
	 * @return sbox value of b
	 */
	private static byte subBox(byte b)
	{
		char[] hex = getHex(b).toCharArray();
		if(hex.length != 2)
			throw new IllegalArgumentException("subBox illegal parm: " + b);
		
		return (byte) (sbox[getDecimal(hex[0])][getDecimal(hex[1])]);
	}

	/**
	 * @param b byte to replace
	 * @return invsbox value of b
	 */
	private static byte invSubBox(byte b)
	{
		char[] hex = getHex(b).toCharArray();
		if(hex.length != 2)
			throw new IllegalArgumentException("invSubBox illegal parm: " + b);
		
		return (byte) (invsbox[getDecimal(hex[0])][getDecimal(hex[1])]);
	}

	/**
	 * @param c '0' - 'F'
	 * @return 0 - 15
	 */
	private static int getDecimal(char c)
	{
		switch(c)
		{
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				return Character.getNumericValue(c);
			case 'A':
				return 10;
			case 'B':
				return 11;
			case 'C':
				return 12;
			case 'D':
				return 13;
			case 'E':
				return 14;
			case 'F':
				return 15;
		}
		throw new IllegalArgumentException("getDecimal does not accept: " + c);
	}
	
	/**
	 * Multiplies byte (representing polynomial f(x)) by x
	 * 
	 * @param a
	 * @return x * f(x)
	 */
	private static byte xtime(byte a)
	{
		// If leftmost bit is 0, just perform a bit shift
		if((a & 0x80) == 0x00)
		{
			return (byte) (a << 1);
		}

		// Otherwise, perform the bit shift and then modulo the irreducible polynomial
		return (byte) ((a << 1) ^ (0x1b));
	}
	
	/**
	 * Iteratively nests #iter xtime calls around byte
	 * 
	 * @param a
	 * @param iter
	 * @return xtime(xtime(....(a)))
	 */
	private static byte xtimeIter(byte a, int iter)
	{
		byte result = a;
		while(iter > 0)
		{
			result = xtime(result);
			iter--;
		}
		return result;
	}
	
	/**
	 * Calculates a + b within the field
	 * 
	 * @param a
	 * @param b
	 * @return a + b, + defined by field
	 */
	private static byte ffAdd(byte a, byte b)
	{
		return (byte) (a ^ b);
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return a * b, * defined by field
	 */
	private static byte ffMultiply(byte a, byte b)
	{
		byte result = 0x00;
		byte current = b;
		int iter = 0;

		// While the byte we are multiplying by still has 1 bits:
		while(current != 0)
		{
			// If lowest bit of current is a 1, add result to xtime(a) nested #iter times,
			// where iter represents how far left the bit is in the original byte b
			if((current & 0x01) == 0x01)
				result = ffAdd(result, xtimeIter(a, iter));
			
			// Shift byte right 1 bit and increment iter
			current >>>= 1;
			current &= 0x7f;
			iter++;
		}

		return result;
	}
	
	/**
	 * Returns String hex representation of byte
	 * 
	 * @param a
	 * @return String hex representation of a
	 */
	private static String getHex(byte a)
	{
		return String.format("%02X", a);
	}
	
	/**
	 * @param in - Word to be converted
	 * @return in with substitutions from sbox
	 */
	private static Word subWord(Word in)
	{
		byte[] temp = new byte[in.size()];
		for(int i = 0; i < in.size() ;i++)
		{
			temp[i] = subBox(in.getByte(i));
		}
		return new Word(temp);
	}
	
	/**
	 * Print representation of state to stdout
	 * 
	 * @param state
	 */
	private static void displayState(byte[] state)
	{
		for(int row = 0; row < 4; row++)
		{
			for(int col = 0; col < 4; col++)
			{
				System.out.print(String.format("%02X", state[col*4 + row] & 0xff) + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
}
