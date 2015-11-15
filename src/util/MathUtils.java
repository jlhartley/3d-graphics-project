package util;

import math.Matrix4f;
import math.Vector3f;

public class MathUtils {
	
	// Basis vectors
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
	
	// Model matrix
	public static Matrix4f createModelMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = new Matrix4f();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), X_AXIS);
		matrix.rotate((float) Math.toRadians(ry), Y_AXIS);
		matrix.rotate((float) Math.toRadians(rz), Z_AXIS);
		matrix.scale(new Vector3f(scale, scale, scale)); // Uniform scale in all axes
		return matrix;
	}
	
	// View matrix
	public static Matrix4f createViewMatrix(Vector3f translation, float pitch, float yaw, float roll) {
		Matrix4f matrix = new Matrix4f();
		matrix.rotate((float) Math.toRadians(pitch), X_AXIS);
		matrix.rotate((float) Math.toRadians(yaw), Y_AXIS);
		matrix.rotate((float) Math.toRadians(roll), Z_AXIS);
		Vector3f negativeTranslation = new Vector3f(-translation.x, -translation.y, -translation.z);
		matrix.translate(negativeTranslation);
		return matrix;
	}
	
	// Perspective projection matrix
	public static Matrix4f perspectiveProjectionMatrix(int width, int height, float fov, float nearPlane, float farPlane) {
		
		// Aspect ratio
		float ratio = (float) width / (float) height;
		
		float yScale = (float) (1 / Math.tan(Math.toRadians(fov / 2)) * ratio);
		float xScale = yScale / ratio;
		
		// Length of the viewing volume
		float frustrumLength = farPlane - nearPlane;
		
		Matrix4f matrix = new Matrix4f();
		matrix.elements[0][0] = xScale;
		matrix.elements[1][1] = yScale;
		matrix.elements[2][2] = -((farPlane + nearPlane) / frustrumLength);
		matrix.elements[2][3] = -1;
		matrix.elements[3][2] = -((2 * nearPlane * farPlane) / frustrumLength);
		matrix.elements[3][3] = 0;
		
		return matrix;
	}
	
	public static Matrix4f orthographicProjectionMatrix(int width, int height, float nearPlane, float farPlane) {
		
		// Aspect ratio
		float ratio = (float) width / (float) height;
		
		// Correcting for aspect ratio
		//float xScale = 1 / ratio;
		float yScale = ratio;
		
		// A uniform scale for x and y
		float scale = 0.003f;
		
		// The length of the viewing volume
		float cuboidLength = farPlane - nearPlane;
		
		Matrix4f matrix = new Matrix4f();
		
		// Scale in y for aspect ratio and apply uniform x/y scale
		matrix.elements[0][0] = scale;
		matrix.elements[1][1] = yScale * scale;
		
		// Uniform scale equivalent to
		//matrix.scale(new Vector3f(scale, scale, 1));
		
		// Scale everything in Z so it fits from -1 to 1
		matrix.elements[2][2] = -2 / cuboidLength;
		
		// Translate to the centre of the cuboid in Z
		matrix.elements[3][2] = -((farPlane + nearPlane) / cuboidLength);
		
		return matrix;
	}
	
	
	// Returns uniformly distributed random doubles in a given range
	public static double randRange(double min, double max) {
		return min + Math.random() * (max - min);
	}
	
	
}
