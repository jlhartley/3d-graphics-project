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
import model.vertices.Mesh;
import model.vertices.Vertex;

import static model.parser.ParserUtils.*;

public class OBJParser2 {
	
	// TODO: A lot of optimisation
	
	private static final String PATH = "res/models/";
	private static final String EXTENSION = ".obj";
	
	// A triangle has 3 vertices per face
	private static final int VERTICES_PER_FACE = 3;
	
	// The path relative to the working directory, for the .obj file
	private String fullPath;
	
	// Vertex data that is read directly from file into these arrays
	private List<Vector3f> positionsIn = new ArrayList<>();
	private List<Vector3f> normalsIn = new ArrayList<>();
	
	// All the vertices arranged in drawing order, non-indexed (with duplicates)
	private List<Vertex> nonIndexedVertices = new ArrayList<>();
	
	// The final vertex list, all indexed (without duplicates)
	private List<Vertex> indexedVertices = new ArrayList<>();
	private int[] indices;
	
	
	public OBJParser2(String relativePath) {
		this.fullPath = PATH + relativePath + EXTENSION;
	}
	
	

	// For a line such as:
	// v 0.123 0.234 0.345
	private void parseVertexLine(String line) {
		String[] lineParts = line.split(" ");
		
		if (line.startsWith("v ")) { // Vertex position definition
			Vector3f position = lineToVector3f(lineParts);
			positionsIn.add(position);
		} else if (line.startsWith("vn ")) { // Vertex normal definition
			Vector3f normal = lineToVector3f(lineParts);
			normalsIn.add(normal);
		} else if (line.startsWith("vt ")) { // Vertex texture coordinate definition
			// TODO: add implementation
		}
	}
	
	// For a line formatted like this:
	// f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	private void parseFaceLine(String line) {
		String[] lineParts = line.split(" ");
		
		// Iterate though each of the 3 vertices that makes up a face, starting
		// at index 1 because the first part of the line is the "f " identifier
		for (int vertex = 1; vertex <= VERTICES_PER_FACE; vertex++) {
			
			// Split the index data into the position, texture and normal index data
			// - 1 to convert from OBJ 1-based index system
			String[] vertexIndexData = lineParts[vertex].split("/");
			int vertexPositionIndex = Integer.parseInt(vertexIndexData[0]) - 1;
			// Texture index data would be at position 1, if there was any
			//int vertexTextureIndex = Integer.parseInt(vertexIndexData[1]) - 1;
			int vertexNormalIndex = Integer.parseInt(vertexIndexData[2]) - 1;
			
			processIndexData(vertexPositionIndex, vertexNormalIndex);
			
		}
		
	}
	
	private void processIndexData(int vertexPositionIndex, int vertexNormalIndex) {
		
		// Grab the vertex attributes out of the correct place
		Vector3f position = positionsIn.get(vertexPositionIndex);
		Vector3f normal = normalsIn.get(vertexNormalIndex);
		
		// Create a vertex and append it to the list of non-indexed vertices
		Vertex vertex = new Vertex(position, normal);
		nonIndexedVertices.add(vertex);
		
	}
	
	private void indexVertices() {
		
		// There will be 1 index added for every vertex in the non-indexed list
		// Therefore the indices array should be the same size
		indices = new int[nonIndexedVertices.size()];
		
		// Map vertices to their position in the indexed vertices array,
		// for O(1) lookup / duplicate finding
		Map<Vertex, Integer> vertexIndexMap = new HashMap<>();
		
		
		//TODO: Look at a revised implementation of this
		// Ideally using an enhanced for loop
		
		for (int i = 0; i < nonIndexedVertices.size(); i++) {
			
			// For each non-indexed vertex
			Vertex nonIndexedVertex = nonIndexedVertices.get(i);
			
			// If a duplicate does exist
			if (vertexIndexMap.containsKey(nonIndexedVertex)) {
				// Add the index of the duplicate vertex, instead of adding the same vertex twice
				int duplicateVertexIndex = vertexIndexMap.get(nonIndexedVertex);
				indices[i] = duplicateVertexIndex;
			} else { // If this is a unique vertex
				// The size of the indexed list reflects the index 
				// where the vertex will be added
				int nextVertexIndex = indexedVertices.size();
				indices[i] = nextVertexIndex;
				vertexIndexMap.put(nonIndexedVertex, nextVertexIndex);
				// Else if this is a unique vertex
				indexedVertices.add(nonIndexedVertex);
			}
			
		}
		
	}
	
	
	
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
		
		int uniqueVertexCount = indexedVertices.size();
		
		// There are 3 (x, y and z) components to each vertex
		float[] vertexPositions = new float[uniqueVertexCount * 3];
		float[] vertexNormals = new float[uniqueVertexCount * 3];
		
		// Copy the vertex positions into the array
		for (int i = 0; i < uniqueVertexCount; i++) {
			Vector3f position = indexedVertices.get(i).position;
			vertexPositions[i * 3] = position.x;
			vertexPositions[i * 3 + 1] = position.y;
			vertexPositions[i * 3 + 2] = position.z;
		}
		
		// Copy the vertex normals into the array
		for (int i = 0; i < uniqueVertexCount; i++) {
			Vector3f normal = indexedVertices.get(i).normal;
			vertexNormals[i * 3] = normal.x;
			vertexNormals[i * 3 + 1] = normal.y;
			vertexNormals[i * 3 + 2] = normal.z;
		}
		
		
		Mesh mesh = new Mesh(indexedVertices, indices);
		
		// Debugging info
		System.out.println("Loaded Mesh: " + fullPath);
		System.out.println("Unique Vertex Count: " + mesh.getUniqueVertexCount());
		System.out.println("Total Vertex Count: " + mesh.getTotalVertexCount());
		System.out.println("Triangle Count: " + mesh.getTriangleCount());
		
		// Currently just using the normals to colour the model
		return new Model(vertexPositions, vertexNormals, vertexNormals, indices);
	}
	
	
	
}
