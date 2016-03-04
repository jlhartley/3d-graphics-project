package model.parser;

import model.vertices.Mesh;

public enum OBJMesh {
	ICOSPHERE1_FLAT("res/models/ico-spheres/icosphere1-flat.obj"), 
	ICOSPHERE1_SMOOTH("res/models/ico-spheres/icosphere1-smooth.obj"), 
	ICOSPHERE2_FLAT("res/models/ico-spheres/icosphere2-flat.obj"), 
	ICOSPHERE2_SMOOTH("res/models/ico-spheres/icosphere2-smooth.obj"),
	ICOSPHERE3_FLAT("res/models/ico-spheres/icosphere3-flat.obj"), 
	ICOSPHERE3_SMOOTH("res/models/ico-spheres/icosphere3-smooth.obj"), 
	ICOSPHERE4_FLAT("res/models/ico-spheres/icosphere4-flat.obj"), 
	ICOSPHERE4_SMOOTH("res/models/ico-spheres/icosphere4-smooth.obj"),
	
	UVSPHERE_FLAT("res/models/uv-spheres/uvsphere-flat.obj"),
	UVSPHERE_SMOOTH("res/models/uv-spheres/uvsphere-smooth.obj"),
	
	TORUS_SMOOTH("res/models/torus-smooth.obj"),
	
	TEST1("res/models/test1.obj"),
	TEST2("res/models/test2.obj"),
	
	ROCK1("res/models/rock1.obj");
	
	private String path;
	private Mesh mesh;
	
	OBJMesh(String path) {
		this.path = path;
		this.mesh = OBJParser2.parse(path);
	}
	
	public String getPath() {
		return path;
	}
	
	public Mesh getMesh() {
		return mesh;
	}
}
