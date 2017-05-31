
class Euler92 {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int count = 0;
		for(int x = 1; x < 10000000; x++) {
			if(numberChainResult(x) == 89)
				count++;
		}
		long end = System.currentTimeMillis();
		long time = end - start;
		System.out.println(count);
		System.out.println("Time: " + time + " ms");
	}
	
	private static int numberChainResult(int n) {
		while(n != 1 && n != 89) {
			char[] chars = (n+"").toCharArray();
			n = 0;
			for(int i = 0; i < chars.length; i++)
				n += Math.pow(Character.getNumericValue(chars[i]), 2);
		}
		return n;
	}
}