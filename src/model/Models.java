package model;

import entities.Entity;
import math.Vector3f;
import model.parser.OBJParser;
import model.parser.OBJParser2;

public class Models {
	
	// SQUARE
	
	private static final float[] squareVertexPositions = { 
			-1, 1, 0, // V0 - top left
			-1, -1, 0, // V1 - bottom left
			1, -1, 0, // V2 - bottom right
			1, 1, 0 // V3 - top right
	};
	
	private static final float[] squareVertexNormals = {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1
	};
	
	private static final float[] squareVertexColours = {
			0, 0, 0, // V0 - black
			1, 0, 0, // V1 - red
			1, 1, 1, // V2 - white
			0, 0, 1 // V3 - blue
	};
	
	// Each triangle must be specified with the vertices winding in an
	// anti-clockwise direction for face culling purposes
	private static final int[] squareIndices = {
			0, 1, 3, // Triangle 1
			3, 1, 2 // Triangle 2
	};
	
	
	private static Model squareModel;
	
	public static Model getSquareModel() {
		if (squareModel == null) {
			squareModel = new Model(squareVertexPositions, squareVertexNormals, squareVertexColours, squareIndices);
		}
		return squareModel;
	}
	
	
	
	// CUBE
	
	/*
	 * "Compact" cube data, without any normal data 
	 * i.e maximum vertex sharing using indices
	 * 
	
	public static final float[] cubeVertexPositions = {
			// Face 1 - Front
			-0.5f,0.5f,0.5f,	// V0
			-0.5f,-0.5f,0.5f,	// V1
			0.5f,-0.5f,0.5f,	// V2
			0.5f,0.5f,0.5f,		// V3

			// Face 2 - Back
			-0.5f,0.5f,-0.5f,	// V4
			-0.5f,-0.5f,-0.5f,	// V5
			0.5f,-0.5f,-0.5f,	// V6
			0.5f,0.5f,-0.5f,	// V7
	};
	
	private static final float[] cubeVertexNormals = {
			
	};
	
	public static final float[] cubeVertexColours = {
			// Face 1 - Front
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			// Face 2 - Back
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
	};
	
	// Each face is defined here by a square consisting of 2 triangles
	public static final int[] cubeIndices = {
			// Front face
			0,1,3,
			3,1,2,
			
			// Back face
			7,6,4,
			4,6,5,
			
			// Right face
			3,2,7,
			7,2,6,
			
			// Left face
			4,5,0,
			0,5,1,
			
			// Top face
			4,0,7,
			7,0,3,
			
			// Bottom face
			1,5,2,
			2,5,6
	};
	
	*/
	
	
	// All values painstakingly worked out for counter-clockwise
	// winding order, for correct face culling
	private static final float[] cubeVertexPositions = {
			// Front
			-0.5f,0.5f,0.5f,
			-0.5f,-0.5f,0.5f,
			0.5f,-0.5f,0.5f,
			0.5f,0.5f,0.5f,

			// Back = the front translated by (0,0,-1) and rotated 180 degrees around y-axis
			0.5f,0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			-0.5f,-0.5f,-0.5f,
			-0.5f,0.5f,-0.5f,

			// Right
			0.5f,0.5f,0.5f,
			0.5f,-0.5f,0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,0.5f,-0.5f,

			// Left = the right translated by (-1,0,0) and rotated 180 degrees around y-axis
			-0.5f,0.5f,-0.5f,
			-0.5f,-0.5f,-0.5f,
			-0.5f,-0.5f,0.5f,
			-0.5f,0.5f,0.5f,

			// Top
			-0.5f,0.5f,-0.5f,
			-0.5f,0.5f,0.5f,
			0.5f,0.5f,0.5f,
			0.5f,0.5f,-0.5f,

			// Bottom = the top translated by (0,-1,0) and rotated 180 degrees around x-axis
			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
	};
	
	// "Pure" rotations from face to face, no translation like position
	// since normals are direction vectors
	private static final float[] cubeVertexNormals = {
			// Front
			0,0,1,
			0,0,1,
			0,0,1,
			0,0,1,
			
			// Back
			0,0,-1,
			0,0,-1,
			0,0,-1,
			0,0,-1,
			
			// Right
			1,0,0,
			1,0,0,
			1,0,0,
			1,0,0,
			
			// Left
			-1,0,0,
			-1,0,0,
			-1,0,0,
			-1,0,0,
			
			// Top
			0,1,0,
			0,1,0,
			0,1,0,
			0,1,0,
			
			// Bottom
			0,-1,0,
			0,-1,0,
			0,-1,0,
			0,-1,0,
	};
	
	public static final float[] cubeVertexColours = {
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
			
			1.0f, 0.0f, 0.0f,	// Red
			0.0f, 0.0f, 1.0f,	// Blue
			0.0f, 1.0f, 0.0f,	// Green
			1.0f, 1.0f, 0.0f,	// Yellow
	};
	
	// The indices are simplified since winding order has already been
	// taken care of when specifying the positions.
	public static final int[] cubeIndices = {
			0,1,3,
			3,1,2,
			
			4,5,7,
			7,5,6,
			
			8,9,11,
			11,9,10,
			
			12,13,15,
			15,13,14,
			
			16,17,19,
			19,17,18,
			
			20,21,23,
			23,21,22
	};
	
	
	private static Model cubeModel;
	
	public static Model getCubeModel() {
		if (cubeModel == null) {
			cubeModel = new Model(cubeVertexPositions, cubeVertexNormals, cubeVertexColours, cubeIndices);
		}
		return cubeModel;
	}
	
	
	
	
	
	
	// Custom built models
	
	// EXPLODED CUBE
	
	private static Model explodedCubeModel;
	
	public static Model getExplodedCubeModel() {
		if (explodedCubeModel == null) {
			Model squareModel = getSquareModel();
			ModelBuilder explodedCubeBuilder = new ModelBuilder();
			
			float spacing = 1.2f; // Distance from each square to the model centre
			
			// Must be careful here with the rotation, for face culling purposes - the normals must point the right way!
			// Important to remember that rotation is counter-clockwise for that reason
			// TODO: Have face culling disabled on a per model basis, for models such as this
			
			//Front
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, spacing)));
			// Back
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, -spacing), new Vector3f(0, 180, 0)));
			// Top
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, spacing, 0), new Vector3f(-90, 0, 0)));
			// Bottom
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, -spacing, 0), new Vector3f(90, 0, 0)));
			// Right
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(spacing, 0, 0), new Vector3f(0, 90, 0)));
			// Left
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(-spacing, 0, 0), new Vector3f(0, -90, 0)));
			
			explodedCubeModel = explodedCubeBuilder.build();
		}
		return explodedCubeModel;
	}
	
	
	// CUBE GRID
	
	private static Model cubeGridModel;
	
	public static Model getCubeGridModel() {
		if (cubeGridModel == null) {
			Model cubeModel = getCubeModel();
			ModelBuilder cubeGridBuilder = new ModelBuilder();
			int cubesPerSide = 3;
			float spacing = 1.5f;
			float offset = -((float)(cubesPerSide - 1) / 2) * spacing;
			int cubeCount = cubesPerSide*cubesPerSide*cubesPerSide;
			// Use of integer division and modulo operator for generating grid
			for (int i = 0; i < cubeCount; i++) {
				float x = (i%cubesPerSide) * spacing;
				float y = (i/cubesPerSide)%cubesPerSide * spacing;
				float z = (i/(cubesPerSide*cubesPerSide)) * spacing;
				Vector3f pos = new Vector3f(x, y, z);
				// Correct offset / stay centred
				pos.translate(new Vector3f(offset, offset, offset));
				
				Entity cube = new Entity(cubeModel, pos);
				cubeGridBuilder.addEntity(cube);
			}
			cubeGridModel = cubeGridBuilder.build();
		}
		return cubeGridModel;
	}
	
	
	
	
	
	
	// Parsed models
	
	// DRAGON
	
	private static final String DRAGON_MODEL_FILENAME = "dragon";
	
	private static Model dragonModel;
	
	public static Model getDragonModel() {
		if (dragonModel == null) {
			OBJParser2 parser = new OBJParser2(DRAGON_MODEL_FILENAME);
			dragonModel = parser.getModel();
		}
		return dragonModel;
	}
	
	private static final String ICOSPHERE_DIRECTORY = "ico-spheres/";
	private static final String ICOSPHERE_BASE_FILENAME = "icosphere";
	private static final String ICOSPHERE_SHADING = "flat";
	private static final int ICOSPHERE_SUBDIVISIONS = 1;
	
	private static Model icosphereModel;
	
	public static Model getIcosphereModel() {
		if (icosphereModel == null) {
			String relativePath = ICOSPHERE_DIRECTORY + ICOSPHERE_BASE_FILENAME + ICOSPHERE_SUBDIVISIONS + "-"
					+ ICOSPHERE_SHADING;
			OBJParser2 parser = new OBJParser2(relativePath);
			icosphereModel = parser.getModel();
		}
		return icosphereModel;
	}
	
	
	private static final String UVSPHERE_DIRECTORY = "uv-spheres/";
	private static final String UVSPHERE_BASE_FILENAME = "uvsphere";
	private static final String UVSPHERE_SHADING = "smooth";
	
	private static Model uvsphereModel;
	
	public static Model getUVsphereModel() {
		if (uvsphereModel == null) {
			String relativePath = UVSPHERE_DIRECTORY + UVSPHERE_BASE_FILENAME + "-" + UVSPHERE_SHADING;
			OBJParser2 parser = new OBJParser2(relativePath);
			uvsphereModel = parser.getModel();
		}
		return uvsphereModel;
	}
	
	
	private static final String TORUS_BASE_FILENAME = "torus";
	private static final String TORUS_SHADING = "smooth";
	
	private static Model torusModel;
	
	public static Model getTorusModel() {
		if (torusModel == null) {
			String relativePath = TORUS_BASE_FILENAME + "-" + TORUS_SHADING;
			OBJParser parser = new OBJParser(relativePath);
			torusModel = parser.getModel();
		}
		return torusModel;
	}
	
	
	
	/*private static final String CUBE_MODEL_FILENAME = "cube";
	
	private static Model cubeModel;
	
	public static Model getCubeModel() {
		if (cubeModel == null) {
			OBJParser2 parser = new OBJParser2(CUBE_MODEL_FILENAME);
			cubeModel = parser.getModel();
		}
		return cubeModel;
	}*/

}
