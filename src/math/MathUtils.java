package math;

import math.geometry.Matrix4f;
import math.geometry.Vector2f;
import math.geometry.Vector3f;
import math.geometry.Vector4f;

public class MathUtils {
	
	// Returns uniformly distributed random doubles in a given range
	public static double randRange(double min, double max) {
		return min + Math.random() * (max - min);
	}
	
	
	public static Vector3f project(Vector2f cursorPosition, float width, float height, Matrix4f projectionMatrix, Matrix4f viewMatrix) {
		return eyeToWorld(clipToEye(NDCToClip(cursorPositionToNDC(cursorPosition, width, height)), projectionMatrix), viewMatrix);
	}
	
	// Note that this returns a new object
	public static Vector2f cursorPositionToNDC(Vector2f cursorPosition, float width, float height) {
		Vector2f ndcCursorPosition = new Vector2f();
		ndcCursorPosition.x = 2 * cursorPosition.x / width;
		ndcCursorPosition.y = 2 * cursorPosition.y / height;
		return ndcCursorPosition;
	}
	
	public static Vector4f NDCToClip(Vector2f ndc) {
		return new Vector4f(ndc.x, ndc.y, -1, 1);
	}
	
	public static Vector4f clipToEye(Vector4f clip, Matrix4f projectionMatrix) {
		Vector4f vec = new Vector4f(clip);
		Matrix4f projInverse = projectionMatrix.invert();
		vec.multiply(projInverse);
		vec.z = -1;
		vec.w = 0;
		return vec;
	}
	
	public static Vector3f eyeToWorld(Vector4f eye, Matrix4f viewMatrix) {
		Vector4f vec4 = new Vector4f(eye);
		Matrix4f viewInverse = viewMatrix.invert();
		vec4.multiply(viewInverse);
		Vector3f vec3 = new Vector3f(vec4.x, vec4.y, vec4.z);
		vec3.normalise();
		return vec3;
	}
	
}
