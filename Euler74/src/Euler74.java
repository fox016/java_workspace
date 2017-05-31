import java.util.HashSet;

class Euler74 {
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		int exactSixtyCount = 0;
		for(int n = 1000; n < 1000000; n++) {
			if(countUniqueTerms(n) == 60) {
				exactSixtyCount++;
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("60 count: " + exactSixtyCount);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static int factorial(int n) {
		if(n <= 1) return 1;
		int product = 1;
		for(int i = n; i > 1; i--)
			product *= i;
		return product;
	}
	
	private static int factorialSum(int n) {
		char[] digits = (n + "").toCharArray();
		int sum = 0;
		for(int i = 0; i < digits.length; i++)
			sum += factorial(Character.getNumericValue(digits[i]));
		return sum;
	}
	
	private static int countUniqueTerms(int n) {
		HashSet<Integer> terms = new HashSet<Integer>();
		int count = 0;
		int term = n;
		while(!terms.contains(term)) {
			terms.add(term);
			term = factorialSum(term);
			count++;
		}
		return count;
	}
}