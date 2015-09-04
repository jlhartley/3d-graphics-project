package testing;

import util.FileUtils;

public class FileWritingTest {
	
	private static final String PATH = "D:\\Users\\James\\Documents\\FileWritingTest.txt";
	
	public static void main(String[] args) {
		
		// Generate the array of random values
		float[] values = new float[50];
		for (int i = 0; i < values.length; i++) {
			values[i] = (float) Math.random();
		}
		
		FileUtils.writeArrayToFile(PATH, values, 3);
		
	}

}
