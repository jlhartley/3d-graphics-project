package model.parser;

import math.geometry.Vector3f;

public class Utils {
	
	protected static Vector3f lineToVector3f(String[] lineParts) {
		// Start at index 1 because the first part of the line is the identifier
		// E.g "v ", "vt ", "vn " etc.
		float x = Float.parseFloat(lineParts[1]);
		float y = Float.parseFloat(lineParts[2]);
		float z = Float.parseFloat(lineParts[3]);
		return new Vector3f(x, y, z);
	}
	
	

}
