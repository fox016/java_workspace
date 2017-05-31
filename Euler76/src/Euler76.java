
class Euler76 {
	
	public static void main(String[] args) {
		long start = System.nanoTime();
		int target = 45;
		int[] ways = new int[target+1];
		ways[0] = 1;
		
		for(int i = 1; i <= target-1; i++) {
			for(int j = i; j <= target; j++) {
				ways[j] += ways[j - i];
			}
		}
		long end = System.nanoTime();
		
		System.out.println(ways[target]);
		System.out.println("Time: " + (end - start) + " ns");
	}
}