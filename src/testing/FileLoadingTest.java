package testing;

import java.nio.file.Paths;

import io.FileUtils;

public class FileLoadingTest {
	
	private static final String PATH = "D:\\Users\\James\\Documents\\FileReadingTest.txt";
	
	public static void main(String[] args) {
		
		System.out.println("DEBUG: Current path: " + Paths.get("").toAbsolutePath());
		System.out.println();
		
		String fileContents = FileUtils.getFileContents(PATH);
		
		System.out.println(fileContents);
		
	}

}
