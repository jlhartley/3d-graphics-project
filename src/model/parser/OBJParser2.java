package model.parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import math.geometry.Vector3f;
import model.Model;

import static model.parser.Utils.*;

public class OBJParser2 {
	
	// TODO: A lot of optimisation
	
	private static final String EXTENSION = ".obj";
	private static final String PATH = "res/models/";
	
	private String fullPath;
	
	// Vertex data that is read directly from file into these arrays
	private List<Vector3f> positionsIn = new ArrayList<>();
	private List<Vector3f> normalsIn = new ArrayList<>();
	
	// All the vertex data arranged in drawing order, non-indexed (with duplicates)
	private int totalVertexCount = 0; // Reflects the size of both of these lists
	private List<Vector3f> unindexedPositions = new ArrayList<>();
	private List<Vector3f> unindexedNormals = new ArrayList<>();
	
	// The final data, all indexed (without duplicates)
	private int uniqueVertexCount = 0; // Reflects the size of both of these lists
	private List<Vector3f> indexedPositions = new ArrayList<>();
	private List<Vector3f> indexedNormals = new ArrayList<>();
	
	// The final arrays
	private float[] vertexPositions;
	private float[] vertexNormals;
	
	private int[] indices;
	
	
	public OBJParser2(String relativePath) {
		this.fullPath = PATH + relativePath + EXTENSION;
	}
	
	

	// For a line such as:
	// v 0.123 0.234 0.345
	private void parseVertexLine(String line) {
		String[] lineParts = line.split(" ");
		
		if (line.startsWith("v ")) { // Vertex position definition
			Vector3f pos = lineToVector3f(lineParts);
			positionsIn.add(pos);
		} else if (line.startsWith("vt ")) { // Vertex texture coordinate definition
			// TODO: add implementation
		} else if (line.startsWith("vn ")) { // Vertex normal definition
			Vector3f normal = lineToVector3f(lineParts);
			normalsIn.add(normal);
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
			
			processIndexData(vertexPositionIndex, vertexNormalIndex);
			
		}
		
		// 3 vertices per face, so increment by 3 since we just processed a face
		totalVertexCount+=3;
	}
	
	private void processIndexData(int vertexPositionIndex, int vertexNormalIndex) {
		// Grab the data out of the correct place and simply append it to the respective list
		unindexedPositions.add(positionsIn.get(vertexPositionIndex));
		unindexedNormals.add(normalsIn.get(vertexNormalIndex));
	}
	
	private void indexVertices() {
		
		// There will be an index added for each of the current vertices
		// declared in the unindexed lists
		indices = new int[totalVertexCount];
		
		Map<Vertex, Integer> vertexIndexMap = new HashMap<>();
		
		for (int i = 0; i < totalVertexCount; i++) {
			
			Vector3f unindexedPos = unindexedPositions.get(i);
			Vector3f unindexedNorm = unindexedNormals.get(i);
			
			Vertex unindexedVertex = new Vertex(unindexedPos, unindexedNorm);
			
			Integer duplicateVertexIndex = vertexIndexMap.get(unindexedVertex);
			
			//int duplicateVertexIndex = getDuplicateVertexIndex(unindexedPos, unindexedNorm);
			
			if (duplicateVertexIndex != null) {
				// Add the index of the duplicate vertex, instead of adding the same vertex data twice
				indices[i] = duplicateVertexIndex;
			} else {
				// This is a truly unique vertex
				indexedPositions.add(unindexedPos);
				indexedNormals.add(unindexedNorm);
				// Use the current unique vertex count to determine the next index
				indices[i] = uniqueVertexCount;
				vertexIndexMap.put(unindexedVertex, uniqueVertexCount);
				uniqueVertexCount++;
			}
			
		}
		
	}
	
	/*private int getDuplicateVertexIndex(Vector3f unindexedPos, Vector3f unindexedNorm) {
		// Basic linear search
		for (int i = 0; i < indexedPositions.size(); i++) {
			
			Vector3f indexedPos = indexedPositions.get(i);
			Vector3f indexedNorm = indexedNormals.get(i);
			
			if (unindexedPos.x == indexedPos.x && unindexedPos.y == indexedPos.y && unindexedPos.z == indexedPos.z &&
					unindexedNorm.x == indexedNorm.x && unindexedNorm.y == indexedNorm.y && unindexedNorm.z == indexedNorm.z) {
				return i;
			}
			
		}
		
		// Returns -1 if there was no duplicate
		return -1;
	}*/
	
	
	
	public Model getModel() {
		
		try ( BufferedReader reader = new BufferedReader(new FileReader(fullPath)) ) {
			
			String line;
			
			// Read until we reach the face definitions
			while ((line = reader.readLine()) != null) {
				
				if (line.startsWith("v")) {
					parseVertexLine(line);
				} else if (line.startsWith("f")) {
					parseFaceLine(line);
				}
				
			}
		
		} catch (FileNotFoundException e) {
			System.err.println("The file: \"" + fullPath + "\" could not be found.");
		} catch (IOException e) {
			System.err.println("An I/O exception occurred.");
		}
		
		
		indexVertices();
		
		// There are 3 (x, y and z) components to each vertex
		vertexPositions = new float[uniqueVertexCount * 3];
		vertexNormals = new float[uniqueVertexCount * 3];
		
		// Copy the vertex positions into the array
		for (int i = 0; i < uniqueVertexCount; i++) {
			Vector3f pos = indexedPositions.get(i);
			vertexPositions[i*3] = pos.x;
			vertexPositions[i*3 + 1] = pos.y;
			vertexPositions[i*3 + 2] = pos.z;
		}
		
		// Copy the vertex normals into the array
		for (int i = 0; i < uniqueVertexCount; i++) {
			Vector3f norm = indexedNormals.get(i);
			vertexNormals[i*3] = norm.x;
			vertexNormals[i*3 + 1] = norm.y;
			vertexNormals[i*3 + 2] = norm.z;
		}
		
		
		// Debugging info
		System.out.println("Loaded Model: " + fullPath);
		System.out.println("Unique Vertex Count: " + uniqueVertexCount);
		System.out.println("Total Vertex Count: " + totalVertexCount);
		System.out.println("Triangle Count: " + totalVertexCount / 3);
		
		// Randomly generate colours
		float[] colours = new float[uniqueVertexCount * 3];
		for (int i = 0; i < uniqueVertexCount; i++) {
			Vector3f colour = new Vector3f((float)Math.random(), (float)Math.random(), (float)Math.random());
			colours[i*3] = colour.x;
			colours[i*3 + 1] = colour.y;
			colours[i*3 + 2] = colour.z;
		}
		
		// Currently just using the normals to colour the model
		return new Model(vertexPositions, vertexNormals, colours, indices);
	}
	
	
	
}
