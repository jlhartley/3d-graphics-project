package ui;

import math.geometry.Vector3f;

public class Validator {
	
	public static final double MAX_VALUE = 10000;
	public static final double MIN_VALUE = -10000;
	
	public static boolean validate(float val) {
		return (val < MAX_VALUE && val > MIN_VALUE);
	}
	
	public static boolean validate(Vector3f vec) {
		return (validate(vec.x) && validate(vec.y) && validate(vec.z));
	}
	
}
