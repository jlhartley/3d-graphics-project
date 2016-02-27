package model;

import static model.ModelData.*;

import entities.Entity;
import math.geometry.Vector3f;
import model.parser.OBJParser;
import model.parser.OBJParser2;

public class Models {
	
	// Hard-coded models
	
	private static Model squareModel;
	
	public static Model getSquareModel() {
		if (squareModel == null) {
			squareModel = new Model(SQUARE_VERTEX_POSITIONS, SQUARE_VERTEX_NORMALS, SQUARE_VERTEX_COLOURS,
					SQUARE_INDICES);
		}
		return squareModel;
	}
	
	
	private static Model cubeModel;
	
	public static Model getCubeModel() {
		if (cubeModel == null) {
			cubeModel = new Model(CUBE_VERTEX_POSITIONS, CUBE_VERTEX_NORMALS, CUBE_VERTEX_COLOURS,
					CUBE_INDICES);
		}
		return cubeModel;
	}
	
	
	
	// Custom built models
	
	// Exploded Cube
	
	private static Model explodedCubeModel;
	
	public static Model getExplodedCubeModel() {
		if (explodedCubeModel == null) {
			Model squareModel = getSquareModel();
			ModelBuilder explodedCubeBuilder = new ModelBuilder();
			
			float spacing = 1.2f; // Distance from each square to the model centre
			
			// Must be careful here with the rotation, for face culling purposes - the normals must point the right way!
			// Important to remember that rotation is counter-clockwise, for this reason. Therefore sometimes negative angles
			// must be specified, so that the rotation is clockwise.
			
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
	
	
	// Cube Grid
	
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
			dragonModel = new Model(parser.getMesh());
		}
		return dragonModel;
	}
	
	private static final String ICOSPHERE_DIRECTORY = "ico-spheres/";
	private static final String ICOSPHERE_BASE_FILENAME = "icosphere";
	private static final String ICOSPHERE_SHADING = "smooth";
	private static final int ICOSPHERE_SUBDIVISIONS = 4;
	
	private static Model icosphereModel;
	
	public static Model getIcosphereModel() {
		if (icosphereModel == null) {
			String relativePath = ICOSPHERE_DIRECTORY + ICOSPHERE_BASE_FILENAME + ICOSPHERE_SUBDIVISIONS + "-"
					+ ICOSPHERE_SHADING;
			OBJParser2 parser = new OBJParser2(relativePath);
			icosphereModel = new Model(parser.getMesh());
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
			uvsphereModel = new Model(parser.getMesh());
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
