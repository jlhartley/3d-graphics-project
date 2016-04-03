package io;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

public class FileUtils {
	
	public static String getFileContents(String path) {

		StringBuilder stringBuilder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new FileReader(path))) {

			String line;
			while ((line = reader.readLine()) != null) {
				// Add line of file, maintaining line breaks
				stringBuilder.append(line + '\n');
			}

		} catch (FileNotFoundException e) {
			System.err.println("The file \"" + path + "\" could not be found.");
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}

		return stringBuilder.toString();
	}
	
	public static void writeToFile(String path, String contents) {

		try (Writer writer = new FileWriter(path)) {

			writer.write(contents);

		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}

	}
	
	
	// For float array
	public static void writeArrayToFile(String path, float[] array, int lineBreakInterval) {

		try (Writer writer = new BufferedWriter(new FileWriter(path))) {

			writer.write(array[0] + ", ");

			for (int i = 1; i < array.length; i++) {
				if (i % lineBreakInterval == 0) {
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

		try (Writer writer = new BufferedWriter(new FileWriter(path))) {

			writer.write(array[0] + ", ");

			for (int i = 1; i < array.length; i++) {
				if (i % lineBreakInterval == 0) {
					writer.write('\n');
				}
				writer.write(array[i] + ", ");
			}

		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}

	}
	
	

}
