package model;

public class Models {
	
	private static float[] squareVertexPositions = { 
			-0.5f, 0.5f, 0, // V0 - top left
			-0.5f, -0.5f, 0, // V1 - bottom left
			0.5f, -0.5f, 0, // V2 - bottom right
			0.5f, 0.5f, 0 // V3 - top right
			
	};
	
	private static float[] squareColourData = {
			0, 0, 0, // V0 - black
			1, 0, 0, // V1 - red
			1, 1, 1, // V2 - white
			0, 0, 1 // V3 - blue
	};
	
	private static int[] squareIndices = {
		0, 3, 1, // Triangle 1 - V0, V3, V1
		1, 2, 3 // Triangle 2 - V1, V2, V3?
	};
	
	private static Model squareModel;
	
	public static Model getSquareModel() {
		if (squareModel == null) {
			squareModel = new Model(squareVertexPositions, squareColourData, squareIndices);
		}
		return squareModel;
	}
	
	
	
	private static CubeModel cubeModel;
	
	public static CubeModel getCubeModel() {
		if (cubeModel == null) {
			cubeModel = new CubeModel();
		}
		return cubeModel;
	}

}
