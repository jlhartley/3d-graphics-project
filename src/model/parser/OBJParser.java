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
import model.vertices.Mesh;
import model.vertices.Vertex;

import static model.parser.ParserUtils.*;

public class OBJParser {
	
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
	private List<Integer> indices; 
	//private int[] indices;
	
	public static Mesh parse(String path) {
		return new OBJParser(path).getMesh();
	}
	
	public OBJParser(String relativePath) {
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
		}
	}
	
	// For a line formatted like this:
	// f v1/vt1/vn1 v2/vt2/vn2 v3/vt3/vn3
	private void parseFaceLine(String line) {
		String[] lineParts = line.split(" ");
		
		// Iterate through each of the 3 vertices that makes up a face, starting
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
		// Therefore the indices list should be the same size
		indices = new ArrayList<>(nonIndexedVertices.size());
		
		// Map vertices to their position in the indexed vertices array,
		// for O(1) lookup / duplicate finding
		Map<Vertex, Integer> vertexIndexMap = new HashMap<>();
		
		for (Vertex vertex : nonIndexedVertices) {
			
			// If a duplicate does exist
			if (vertexIndexMap.containsKey(vertex)) {
				// Add the index of the duplicate vertex, instead of adding the same vertex twice
				int duplicateVertexIndex = vertexIndexMap.get(vertex);
				indices.add(duplicateVertexIndex);
			} else { // If this is a unique vertex
				// The current size of the indexed list gives the index 
				// where the vertex will be added
				int vertexIndex = indexedVertices.size();
				indexedVertices.add(vertex);
				indices.add(vertexIndex);
				// Update the map for further indexing
				vertexIndexMap.put(vertex, vertexIndex);
			}
			
		}
		
	}
	
	
	
	public Mesh getMesh() {
		
		try ( BufferedReader reader = new BufferedReader(new FileReader(fullPath)) ) {
			
			String line;
			
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
		
		
		
		Mesh mesh = new Mesh(indexedVertices, indices);
		
		// Debugging info
		System.out.println("Loaded Mesh: " + fullPath);
		System.out.println("Unique Vertex Count: " + mesh.getUniqueVertexCount());
		System.out.println("Total Vertex Count: " + mesh.getTotalVertexCount());
		System.out.println("Triangle Count: " + mesh.getTriangleCount());
		
		return mesh;
	}
	
	
	
}
