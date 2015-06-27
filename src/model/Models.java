package model;

public class Models {
	
	private static CubeModel cubeModel;
	private static ShipModel shipModel;
	
	public static CubeModel getCubeModel() {
		
		if (cubeModel == null) {
			cubeModel = new CubeModel();
		}
		
		return cubeModel;
		
	}
	
	public static ShipModel getShipModel() {
		
		if (shipModel == null) {
			shipModel = new ShipModel();
		}
		
		return shipModel;
		
	}
	
	
	public static void cleanUpUsedModels() {
		
		if (cubeModel != null) {
			cubeModel.cleanUp();
			cubeModel = null;
		}
		
		if (shipModel != null) {
			shipModel.cleanUp();
			shipModel = null;
		}
		
	}

}
