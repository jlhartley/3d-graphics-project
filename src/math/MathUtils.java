package math;

import math.geometry.Vector2f;

public class MathUtils {
	
	// Returns uniformly distributed random doubles in a given range
	public static double randRange(double min, double max) {
		return min + Math.random() * (max - min);
	}
	
	// Note that this returns a new object
	public static Vector2f cursorPositionToNDC(Vector2f cursorPosition, float width, float height) {
		Vector2f ndcCursorPosition = new Vector2f();
		ndcCursorPosition.x = cursorPosition.x / (width / 2);
		ndcCursorPosition.y = cursorPosition.y / (height / 2);
		return ndcCursorPosition;
	}
	
}
