
class Euler85 {
	
	private static final int target = 2000000;
	
	public static void main(String[] args) {
		
		long start = System.currentTimeMillis();
		
		int closestDifference = target;
		int closestDifferenceArea = 0;
		for(int h = 1; h < 100; h++) {
			for(int w = 1; w < 100; w++) {
				int difference = Math.abs(countInnerRectangles(h, w) - target);
				if(difference < closestDifference) {
					closestDifference = difference;
					closestDifferenceArea = w * h;
				}
			}
		}
		
		long end = System.currentTimeMillis();
		
		System.out.println("Closest Area: " + closestDifferenceArea);
		System.out.println("Time: " + (end - start) + " ms");
	}
	
	private static int countInnerRectangles(int height, int width) {
		
		int count = 0;
		for(int h = 1; h <= height; h++) {
			for(int w = 1; w <= width; w++) {
				count += (height - h + 1) * (width - w + 1);
			}
		}
		
		return count;
	}
}