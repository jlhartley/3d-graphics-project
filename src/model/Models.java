package model;

import entities.Entity;
import math.Vector3f;
import model.parser.OBJParser;
import model.parser.OBJParser2;

public class Models {
	
	// SQUARE
	
	private static final float[] squareVertexPositions = { 
		-0.5f, 0.5f, 0, // V0 - top left
		-0.5f, -0.5f, 0, // V1 - bottom left
		0.5f, -0.5f, 0, // V2 - bottom right
		0.5f, 0.5f, 0 // V3 - top right
	};
	
	private static final float[] squareVertexNormals = {
			
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
	
	public static final float[] cubeVertexPositions = {
			// Face 1 - Front
			-0.5f,0.5f,-0.5f,	// V0
			-0.5f,-0.5f,-0.5f,	// V1
			0.5f,-0.5f,-0.5f,	// V2
			0.5f,0.5f,-0.5f,	// V3

			// Face 2 - Back
			-0.5f,0.5f,0.5f,	// V4
			-0.5f,-0.5f,0.5f,	// V5
			0.5f,-0.5f,0.5f,	// V6
			0.5f,0.5f,0.5f,		// V7
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
			2,5,6,
			
			// Back face
			7,6,4,
			4,6,5
	};
	
	
	private static Model cubeModel;
	
	public static Model getCubeModel() {
		if (cubeModel == null) {
			cubeModel = new Model(cubeVertexPositions, cubeVertexNormals, cubeVertexColours, cubeIndices);
		}
		return cubeModel;
	}
	
	*/
	
	
	// Custom built models
	
	// EXPLODED CUBE
	
	private static Model explodedCubeModel;
	
	public static Model getExplodedCubeModel() {
		if (explodedCubeModel == null) {
			Model squareModel = getSquareModel();
			ModelBuilder explodedCubeBuilder = new ModelBuilder();
			// Must be careful here with the rotation, for face culling purposes
			// TODO: Have face culling disabled on a per model basis, for models such as this
			//Front
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, 0.7f)));
			// Back
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, -0.7f), new Vector3f(180, 0, 0)));
			// Top
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0.7f, 0), new Vector3f(270, 0, 0)));
			// Bottom
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, -0.7f, 0), new Vector3f(90, 0, 0)));
			// Right
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0.7f, 0, 0), new Vector3f(0, 90, 0)));
			// Left
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(-0.7f, 0, 0), new Vector3f(0, 270, 0)));
			
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
			int cubeCount = 27;
			for (int i = 0; i < cubeCount; i++) {
				Vector3f pos = new Vector3f((i%3) * 1.5f, (i/3)%3 * 1.5f, (i/9) * 1.5f);
				// Correct offset
				pos.translate(new Vector3f(-1.5f, -1.5f, -1.5f));
				
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
			OBJParser parser = new OBJParser(DRAGON_MODEL_FILENAME);
			dragonModel = parser.getModel();
		}
		return dragonModel;
	}
	
	private static final String ICOSPHERE_DIRECTORY = "ico-spheres/";
	private static final String ICOSPHERE_BASE_FILENAME = "icosphere";
	private static final String ICOSPHERE_SHADING = "flat";
	private static final int ICOSPHERE_SUBDIVISIONS = 3;
	
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
	
	private static final String CUBE_MODEL_FILENAME = "cube";
	
	private static Model cubeModel;
	
	public static Model getCubeModel() {
		if (cubeModel == null) {
			OBJParser2 parser = new OBJParser2(CUBE_MODEL_FILENAME);
			cubeModel = parser.getModel();
		}
		return cubeModel;
	}

}
