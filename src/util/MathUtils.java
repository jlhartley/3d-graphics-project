package util;

import math.Matrix4f;
import math.Vector3f;

public class MathUtils {
	
	
	public static final Vector3f X_AXIS = new Vector3f(1.0f, 0.0f, 0.0f);
	public static final Vector3f Y_AXIS = new Vector3f(0.0f, 1.0f, 0.0f);
	public static final Vector3f Z_AXIS = new Vector3f(0.0f, 0.0f, 1.0f);
	
	// Model matrix positions a model in the world
	public static Matrix4f createModelMatrix(Vector3f translation, float rx, float ry, float rz, float scale) {
		Matrix4f matrix = Matrix4f.identity();
		matrix.translate(translation);
		matrix.rotate((float) Math.toRadians(rx), X_AXIS);
		matrix.rotate((float) Math.toRadians(ry), Y_AXIS);
		matrix.rotate((float) Math.toRadians(rz), Z_AXIS);
		matrix.scale(new Vector3f(scale, scale, scale));
		return matrix;
	}
	
	// 3D to 2D perspective projection matrix
	public static Matrix4f createProjectionMatrix(int width, int height, float fov, float nearplane, float farplane) {
		float ratio = (float) width / (float) height;
		float y_scale = (float) (1f / Math.tan(Math.toRadians(fov / 2f)) * ratio);
		float x_scale = y_scale / ratio;
		float frustrum_length = farplane - nearplane;
		
		Matrix4f matrix = Matrix4f.identity();
		matrix.elements[0 + 0 * 4] = x_scale;
		matrix.elements[1 + 1 * 4] = y_scale;
		matrix.elements[2 + 2 * 4] = -((farplane + nearplane) / frustrum_length);
		matrix.elements[2 + 3 * 4] = -1;
		matrix.elements[3 + 2 * 4] = -((2 * nearplane * farplane) / frustrum_length);
		matrix.elements[3 + 3 * 4] = 0;
		
		return matrix;
	}
	
	// View matrix positions models relative to the camera
	public static Matrix4f createViewMatrix(Vector3f translation, float pitch, float yaw, float roll) {
		Matrix4f matrix = Matrix4f.identity();
		matrix.rotate((float) Math.toRadians(pitch), X_AXIS);
		matrix.rotate((float) Math.toRadians(yaw), Y_AXIS);
		matrix.rotate((float) Math.toRadians(roll), Z_AXIS);
		Vector3f negativeTranslation = new Vector3f(-translation.x, -translation.y, -translation.z);
		matrix.translate(negativeTranslation);
		return matrix;
	}
	
	
}
