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
	
	
	public OBJParser(String relativePath) {
		this.fullPath = PATH + relativePath + EXTENSION;
	}
	
	
	private static Vector3f lineToVector3f(String[] lineParts) {
		// Start at index 1 because the first part of the line is the identifier
		// E.g "v ", "vt ", "vn " etc.
		float x = Float.parseFloat(lineParts[1]);
		float y = Float.parseFloat(lineParts[2]);
		float z = Float.parseFloat(lineParts[3]);
		return new Vector3f(x, y, z);
	}
	
	// For a line such as:
	// v 0.123 0.234 0.345
	private void parseVertexLine(String line) {
		String[] lineParts = line.split(" ");
		
		if (line.startsWith("v ")) { // Vertex position definition
			Vector3f pos = lineToVector3f(lineParts);
			vertexPositionsList.add(pos);
		} else if (line.startsWith("vt ")) { // Vertex texture coordinate definition
			// TODO: add implementation
		} else if (line.startsWith("vn ")) { // Vertex normal definition
			Vector3f normal = lineToVector3f(lineParts);
			vertexNormalsList.add(normal);
		}
	}
	
	// For a line formatted like this:
	// f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	private void parseFaceLine(String line) {
		String[] lineParts = line.split(" ");
		
		// Iterate though each of the 3 vertices that makes up a face, starting
		// at index 1 because the first part of the line is the "f " identifier
		for (int vertex = 1; vertex <= 3; vertex++) {
			
			// Split the index data into the position, texture and normal index data
			// - 1 to convert from OBJ 1-based index system
			String[] vertexIndexData = lineParts[vertex].split("/");
			int vertexPositionIndex = Integer.parseInt(vertexIndexData[0]) - 1;
			//int vertexTextureIndex = Integer.parseInt(vertexIndexData[1]) - 1;
			int vertexNormalIndex = Integer.parseInt(vertexIndexData[2]) - 1;
			
			processIndexData(vertexPositionIndex, /*vertexTextureIndex, */ vertexNormalIndex);
		}
	}
	
	private void processIndexData(int vertexPositionIndex, /*int vertexTextureIndex, */ int vertexNormalIndex) {
		
		indicesList.add(vertexPositionIndex);
		
		// Align everything to the vertex position index
		// Multiple writes will occur because some vertices will belong to multiple faces
		Vector3f vertexNormal = vertexNormalsList.get(vertexNormalIndex);
		vertexNormals[vertexPositionIndex*3] = vertexNormal.x;
		vertexNormals[vertexPositionIndex*3 + 1] = vertexNormal.y;
		vertexNormals[vertexPositionIndex*3 + 2] = vertexNormal.z;
		
	}
	
	public Model getModel() {
		
		try ( BufferedReader reader = new BufferedReader(new FileReader(fullPath)) ) {
			
			String line;
			
			// Read until we reach the face definitions
			while (!(line = reader.readLine()).startsWith("f ")) {
				parseVertexLine(line);
			}
			
			// Use the size of the vertexPositionsList and not the vertexNormalsList,
			// this is because there must be exactly 1 normal entry for each vertex.
			// In many OBJ files there are vastly different numbers of normals to vertices.
			// size * 3 since there are 3 components for each vector
			vertexNormals = new float[vertexPositionsList.size() * 3];
			
			
			do {
				// Make sure that the line is a face definition
				if (line.startsWith("f ")) {
					parseFaceLine(line);
				}
			} while ((line = reader.readLine()) != null);
		
		} catch (FileNotFoundException e) {
			System.err.println("The file: \"" + fullPath + "\" could not be found.");
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
		
		
		vertexPositions = new float[vertexPositionsList.size() * 3];
		
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
		
		
		// Currently just using the normals to colour the model
		return new Model(vertexPositions, vertexNormals, vertexNormals, indices);
	}
	
	
	
}
