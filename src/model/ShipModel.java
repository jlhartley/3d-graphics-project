package model;

import shaders.ShaderProgram;

public class ShipModel extends VAOModel {
	
	private static final float[] vertexPositions = { 
		8, -24,
		5, -8,
		0, -3, 
		-5, -8,
		-8, -24,
		-15, 1,
		-10, 15,
		-5, 20,
		5, 20,
		10, 15,
		15, 1
	};
	
	private static final int[] vertexIndices = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
	
	public ShipModel() {
		super.addVertexAttrib(vertexPositions, ShaderProgram.POSITION_ATTRIB_LOCATION, 2);
		super.setIBOData(vertexIndices);
		super.unbindVAO();
	}

}
