package model;

import shaders.ShaderProgram;

public class Models {
	
	private static CubeModel cubeModel;
	
	// Anti-clockwise
	private static float[] squareVertexPositions = { 
			-0.5f, 0.5f, 0, // V1
			-0.5f, -0.5f, 0, // V2
			0.5f, -0.5f, 0, // V3
			0.5f, 0.5f, 0 // V4
			
	};
	
	private static int[] squareIndices = {
		0, 3, 1, // Triangle 1
		1, 2, 3 // Triangle 2 - 3, 1, 2?
	};
	
	private static float[] squareColourData = {
			0, 0, 0,
			1, 0, 0,
			1, 1, 1,
			0, 0, 1
	};
	
	private static Model squareModel;
	
	private static void buildSquareModel() {
		squareModel = new Model();
		squareModel.addVertexAttrib(squareVertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 3);
		squareModel.addVertexAttrib(squareColourData, ShaderProgram.COLOUR_ATTRIB_LOCATION, 3);
		squareModel.setIBOData(squareIndices);
		squareModel.unbindVAO();
	}
	
	public static Model getSquareModel() {
		
		if (squareModel == null) {
			buildSquareModel();
		}
		
		return squareModel;
	}
	
	public static CubeModel getCubeModel() {
		
		if (cubeModel == null) {
			cubeModel = new CubeModel();
		}
		
		return cubeModel;
		
	}
	
	// The problem with this is that it just makes the reference to each
	// model object null within the scope of this class - 
	// there could well be a non-null reference somewhere else
	public static void cleanUpUsedModels() {
		
		if (cubeModel != null) {
			cubeModel.cleanUp();
			cubeModel = null;
		}
		
		if (squareModel != null) {
			squareModel.cleanUp();
			squareModel = null;
		}
		
		
	}

}
