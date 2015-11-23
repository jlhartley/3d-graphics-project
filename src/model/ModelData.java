package model;

public class ModelData {
	
	// Square
	
	protected static final float[] SQUARE_VERTEX_POSITIONS = { 
			-1, 1, 0, // V0 - top left
			-1, -1, 0, // V1 - bottom left
			1, -1, 0, // V2 - bottom right
			1, 1, 0 // V3 - top right
	};
	
	protected static final float[] SQUARE_VERTEX_NORMALS = {
			0, 0, 1,
			0, 0, 1,
			0, 0, 1,
			0, 0, 1
	};
	
	protected static final float[] SQUARE_VERTEX_COLOURS = {
			0, 0, 0, // V0 - black
			1, 0, 0, // V1 - red
			1, 1, 1, // V2 - white
			0, 0, 1 // V3 - blue
	};
	
	// Each triangle must be specified with the vertices winding in an
	// anti-clockwise direction for face culling purposes
	protected static final int[] SQUARE_INDICES = {
			0, 1, 3, // Triangle 1
			3, 1, 2 // Triangle 2
	};
	
	
	// Cube
	
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
	protected static final float[] CUBE_VERTEX_POSITIONS = {
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
	protected static final float[] CUBE_VERTEX_NORMALS = {
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
	
	protected static final float[] CUBE_VERTEX_COLOURS = {
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
	protected static final int[] CUBE_INDICES = {
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
	

}
