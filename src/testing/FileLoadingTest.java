package testing;

import java.nio.file.Paths;

import util.FileUtils;

public class FileLoadingTest {
	
	public static void main(String[] args) {
		
		System.out.println("DEBUG: Current path: " + Paths.get("").toAbsolutePath());
		System.out.println();
		
		String fileContents = FileUtils.getFileContents("D:\\Users\\James\\Documents\\FileReadingTest.txt");
		
		System.out.println(fileContents);
		
	}

}
