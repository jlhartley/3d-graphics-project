package model;


import model.parser.OBJParser;

public class Models {
	
	// Parsed models
	
	private static final String ICOSPHERE_DIRECTORY = "ico-spheres/";
	private static final String ICOSPHERE_BASE_FILENAME = "icosphere";
	private static final String ICOSPHERE_SHADING = "smooth";
	private static final int ICOSPHERE_SUBDIVISIONS = 4;
	
	private static Model icosphereModel;
	public static Model getIcosphereModel() {
		if (icosphereModel == null) {
			String relativePath = ICOSPHERE_DIRECTORY + ICOSPHERE_BASE_FILENAME + ICOSPHERE_SUBDIVISIONS + "-"
					+ ICOSPHERE_SHADING;
			OBJParser parser = new OBJParser(relativePath);
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
			OBJParser parser = new OBJParser(relativePath);
			uvsphereModel = new Model(parser.getMesh());
		}
		return uvsphereModel;
	}
	
	
	
	private static Model rockModel;
	public static Model getRockModel() {
		if (rockModel == null) {
			rockModel = new Model(OBJParser.parse("rock1"));
		}
		return rockModel;
	}
	
	
}
