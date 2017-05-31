
class Euler71 {
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		int num = 2;
		int den = 5;
		while(den + 7 <= 1000000) {
			num += 3;
			den += 7;
		}
		long end = System.currentTimeMillis();
		System.out.println(num);
		System.out.println("Time: " + (end - start) + " ms");
	}
}