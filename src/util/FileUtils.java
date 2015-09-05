package util;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
	
	public static String getFileContents(String path){
		
		StringBuilder sb = new StringBuilder();
		
		try ( BufferedReader reader = new BufferedReader(new FileReader(path)) ) {
			
			String line;
			while ((line = reader.readLine()) != null) {
				// Add line of file, maintaining line breaks
				sb.append(line + '\n');
			}
		
		} catch (FileNotFoundException e) {
			System.err.println("The file: \"" + path + "\" could not be found.");
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
		return sb.toString();
	}
	
	
	// For float array
	public static void writeArrayToFile(String path, float[] array, int lineBreakInterval) {
		
		try ( FileWriter writer = new FileWriter(path) ) {
			
			for (int i = 0; i < array.length; i++) {
				if (i != 0 && i % lineBreakInterval == 0) {
					writer.write('\n');
				}
				writer.write(array[i] + ", ");
			}
		
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
	}
	
	
	// For int array
	public static void writeArrayToFile(String path, int[] array, int lineBreakInterval) {
		
		try ( FileWriter writer = new FileWriter(path) ) {
			
			for (int i = 0; i < array.length; i++) {
				if (i != 0 && i % lineBreakInterval == 0) {
					writer.write('\n');
				}
				writer.write(array[i] + ", ");
			}
		
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
	}
	
	

}
