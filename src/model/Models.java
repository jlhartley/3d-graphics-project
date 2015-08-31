package model;

import entities.Entity;
import math.Vector3f;
import model.parser.OBJParser;

public class Models {
	
	// SQUARE
	
	private static final float[] squareVertexPositions = { 
		-0.5f, 0.5f, 0, // V0 - top left
		-0.5f, -0.5f, 0, // V1 - bottom left
		0.5f, -0.5f, 0, // V2 - bottom right
		0.5f, 0.5f, 0 // V3 - top right
	};
	
	private static final float[] squareColourData = {
		0, 0, 0, // V0 - black
		1, 0, 0, // V1 - red
		1, 1, 1, // V2 - white
		0, 0, 1 // V3 - blue
	};
	
	private static final int[] squareIndices = {
		0, 3, 1, // Triangle 1 - V0, V3, V1
		1, 2, 3 // Triangle 2 - V1, V2, V3
	};
	
	
	private static Model squareModel;
	
	public static Model getSquareModel() {
		if (squareModel == null) {
			squareModel = new Model(squareVertexPositions, squareColourData, squareIndices);
		}
		return squareModel;
	}
	
	
	// CUBE
	
	public static final float[] cubeVertexPositions = {
			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,0.5f,-0.5f,		

			-0.5f,0.5f,0.5f,	
			-0.5f,-0.5f,0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,

			0.5f,0.5f,-0.5f,	
			0.5f,-0.5f,-0.5f,	
			0.5f,-0.5f,0.5f,	
			0.5f,0.5f,0.5f,

			-0.5f,0.5f,-0.5f,	
			-0.5f,-0.5f,-0.5f,	
			-0.5f,-0.5f,0.5f,	
			-0.5f,0.5f,0.5f,

			-0.5f,0.5f,0.5f,
			-0.5f,0.5f,-0.5f,
			0.5f,0.5f,-0.5f,
			0.5f,0.5f,0.5f,

			-0.5f,-0.5f,0.5f,
			-0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,-0.5f,
			0.5f,-0.5f,0.5f
	};
	
	public static final float[] cubeVertexColours = {
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f,
			
			1.0f, 0.0f, 0.0f,
			0.0f, 0.0f, 1.0f,
			0.0f, 1.0f, 0.0f,
			1.0f, 1.0f, 0.0f
	};
	
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
			cubeModel = new Model(cubeVertexPositions, cubeVertexColours, cubeIndices);
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
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, 0.7f)));
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0, -0.7f)));
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, 0.7f, 0), new Vector3f(90, 0, 0)));
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0, -0.7f, 0), new Vector3f(90, 0, 0)));
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(0.7f, 0, 0), new Vector3f(0, 90, 0)));
			explodedCubeBuilder.addEntity(new Entity(squareModel, new Vector3f(-0.7f, 0, 0), new Vector3f(0, 90, 0)));
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

}
