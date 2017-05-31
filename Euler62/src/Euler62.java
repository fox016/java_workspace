import java.util.Arrays;
import java.util.LinkedList;
import java.util.Iterator;

class Euler62 {
	
	public static LinkedList<Double> cubes = getCubes(5000, 10000);
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		Iterator<Double> iter = cubes.iterator();
		while(iter.hasNext()) {
			Double i = iter.next();
			if(getCubicPermutationCount(i) == 5) {
				System.out.println(i);
				break;
			}
		}
		long end = System.currentTimeMillis();
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	public static LinkedList<Double> getCubes(int startBase, int endBase) {
		LinkedList<Double> cubes = new LinkedList<Double>();
		for(int i = startBase; i <= endBase; i++) {
			cubes.add(Math.pow(i, 3));
		}
		return cubes;
	}
	
	public static short getCubicPermutationCount(double i) {
		Iterator<Double> iter = cubes.iterator();
		short count = 0;
		while(iter.hasNext()) {
			if(isPermutation(i, iter.next()))
				count++;
		}
		return count;
	}
	
	public static boolean isPermutation(double i, double j) {
		char[] ic = (i+"").toCharArray();
		char[] jc = (j+"").toCharArray();
		Arrays.sort(ic);
		Arrays.sort(jc);
		return Arrays.equals(ic, jc);
	}
}