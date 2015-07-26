package model;

public class Models {
	
	private static CubeModel cubeModel;
	
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
		
		
	}

}
