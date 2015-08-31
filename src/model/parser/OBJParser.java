package model.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import math.Vector3f;
import model.Model;

public class OBJParser {
	
	private static final String EXTENSION = ".obj";
	private static final String PATH = "res//models//";
	
	private String fullPath;
	
	private List<Vector3f> vertexPositionsList = new ArrayList<>();
	private List<Vector3f> vertexNormalsList = new ArrayList<>();
	private List<Integer> indicesList = new ArrayList<>();
	
	private float[] vertexPositions;
	private float[] vertexNormals;
	private int[] indices;
	
	
	public OBJParser(String fileName) {
		this.fullPath = PATH + fileName + EXTENSION;
	}
	
	
	
	
	public Model getModel() {
		
		try ( BufferedReader reader = new BufferedReader(new FileReader(fullPath)) ) {
			
			String line;
			
			// Read until we reach the face definitions
			while (!(line = reader.readLine()).startsWith("f ")) {
				
				String[] lineParts = line.split(" ");
				
				if (line.startsWith("v ")) {
					float x = Float.parseFloat(lineParts[1]);
					float y = Float.parseFloat(lineParts[2]);
					float z = Float.parseFloat(lineParts[3]);
					vertexPositionsList.add(new Vector3f(x, y, z));
				} else if (line.startsWith("vt ")) {
					
				} else if (line.startsWith("vn ")) {
					float x = Float.parseFloat(lineParts[1]);
					float y = Float.parseFloat(lineParts[2]);
					float z = Float.parseFloat(lineParts[3]);
					vertexNormalsList.add(new Vector3f(x, y, z));
				}
				
			}
			
			// Multiplied by 3 since there are 3 components for each vector
			vertexPositions = new float[vertexPositionsList.size() * 3];
			vertexNormals = new float[vertexNormalsList.size() * 3];
			
			do {
				if (line.startsWith("f ")) {
					String[] lineParts = line.split(" ");
					// Each vertex contains 3 numbers separated by "/"
					String[] vertex1 = lineParts[1].split("/");  
					String[] vertex2 = lineParts[2].split("/");  
					String[] vertex3 = lineParts[3].split("/");  
					processFaceVertex(vertex1);
					processFaceVertex(vertex2);
					processFaceVertex(vertex3);
				}
			} while ((line = reader.readLine()) != null);
		
		} catch (FileNotFoundException e) {
			System.err.println("The file: \"" + fullPath + "\" could not be found.");
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
		// Copy the vertex positions into the array
		for (int i = 0; i < vertexPositionsList.size(); i++) {
			Vector3f pos = vertexPositionsList.get(i);
			vertexPositions[i*3] = pos.x;
			vertexPositions[i*3 + 1] = pos.y;
			vertexPositions[i*3 + 2] = pos.z;
		}
		
		indices = new int[indicesList.size()];
		
		// Copy the indices into the array
		for (int i = 0; i < indicesList.size(); i++) {
			indices[i] = indicesList.get(i);
		}
		
		
		return new Model(vertexPositions, vertexNormals, indices);
	}
	
	
	private void processFaceVertex(String[] vertexData) {
		// - 1 to convert from OBJ 1-based index system
		int vertexPositionPointer = Integer.parseInt(vertexData[0]) - 1;
		//int vertexTexturePointer = Integer.parseInt(vertexData[1]) - 1;
		int vertexNormalPointer = Integer.parseInt(vertexData[2]) - 1;
		
		//Vector3f vertexPos = vertexPositionsList.get(vertexPositionPointer);
		Vector3f vertexNormal = vertexNormalsList.get(vertexNormalPointer);
		
		// Align everything to the vertex position
		// Multiple writes will occur because some vertices will belong to multiple faces
		indicesList.add(vertexPositionPointer);
		vertexNormals[vertexPositionPointer*3] = vertexNormal.x;
		vertexNormals[vertexPositionPointer*3 + 1] = vertexNormal.y;
		vertexNormals[vertexPositionPointer*3 + 2] = vertexNormal.z;
		
	}
	
	
	
}
