package math;

import java.text.DecimalFormat;

public class Matrix4f {
	
	// Factory methods
	
	public static Matrix4f translation(Vector3f vector) {
		Matrix4f matrix = new Matrix4f();
		// Set each value in the final column
		matrix.elements[3][0] = vector.x;
		matrix.elements[3][1] = vector.y;
		matrix.elements[3][2] = vector.z;
		return matrix;
	}
	
	
	// Constants
	public static final int ROW_COUNT = 4;
	public static final int COLUMN_COUNT = 4;
	public static final int SIZE = COLUMN_COUNT * ROW_COUNT;
	
	public float[][] elements = new float[COLUMN_COUNT][ROW_COUNT];
	
	
	public Matrix4f() {
		// A new matrix will be set to the identity matrix
		identity();
	}
	
	
	public void identity() {
		
		// [ 1 , 0 , 0 , 0 ]
		// [ 0 , 1 , 0 , 0 ]
		// [ 0 , 0 , 1 , 0 ]
		// [ 0 , 0 , 0 , 1 ]
		
		// Column 0
		elements[0][0] = 1.0f;
		elements[0][1] = 0.0f;
		elements[0][2] = 0.0f;
		elements[0][3] = 0.0f;
		
		// Column 1
		elements[1][0] = 0.0f;
		elements[1][1] = 1.0f;
		elements[1][2] = 0.0f;
		elements[1][3] = 0.0f;
		
		// Column 2
		elements[2][0] = 0.0f;
		elements[2][1] = 0.0f;
		elements[2][2] = 1.0f;
		elements[2][3] = 0.0f;
		
		// Column 3
		elements[3][0] = 0.0f;
		elements[3][1] = 0.0f;
		elements[3][2] = 0.0f;
		elements[3][3] = 1.0f;
	}
	
	
	public void translate(Vector3f vec) {
		elements[3][0] += elements[0][0] * vec.x + elements[1][0] * vec.y + elements[2][0] * vec.z;
		elements[3][1] += elements[0][1] * vec.x + elements[1][1] * vec.y + elements[2][1] * vec.z;
		elements[3][2] += elements[0][2] * vec.x + elements[1][2] * vec.y + elements[2][2] * vec.z;
		elements[3][3] += elements[0][3] * vec.x + elements[1][3] * vec.y + elements[2][3] * vec.z;
	}
	
	public void setTranslation(Vector3f vector) {
		elements[3][0] = vector.x;
		elements[3][1] = vector.y;
		elements[3][2] = vector.z;
	}
	
	
	// Axis must be a unit vector
	public void rotate(float angle, Vector3f axis) {
		
		// Only operates on the 3x3 portion
		
		float c = (float) Math.cos(angle);
		float s = (float) Math.sin(angle);
		float oneminusc = 1.0f - c;
		float xy = axis.x*axis.y;
		float yz = axis.y*axis.z;
		float xz = axis.x*axis.z;
		float xs = axis.x*s;
		float ys = axis.y*s;
		float zs = axis.z*s;

		float f00 = axis.x*axis.x*oneminusc+c;
		float f01 = xy*oneminusc+zs;
		float f02 = xz*oneminusc-ys;
		
		float f10 = xy*oneminusc-zs;
		float f11 = axis.y*axis.y*oneminusc+c;
		float f12 = yz*oneminusc+xs;
		
		float f20 = xz*oneminusc+ys;
		float f21 = yz*oneminusc-xs;
		float f22 = axis.z*axis.z*oneminusc+c;

		float t00 = elements[0][0] * f00 + elements[1][0] * f01 + elements[2][0] * f02;
		float t01 = elements[0][1] * f00 + elements[1][1] * f01 + elements[2][1] * f02;
		float t02 = elements[0][2] * f00 + elements[1][2] * f01 + elements[2][2] * f02;
		float t03 = elements[0][3] * f00 + elements[1][3] * f01 + elements[2][3] * f02;
		float t10 = elements[0][0] * f10 + elements[1][0] * f11 + elements[2][0] * f12;
		float t11 = elements[0][1] * f10 + elements[1][1] * f11 + elements[2][1] * f12;
		float t12 = elements[0][2] * f10 + elements[1][2] * f11 + elements[2][2] * f12;
		float t13 = elements[0][3] * f10 + elements[1][3] * f11 + elements[2][3] * f12;
		
		elements[2][0] = elements[0][0] * f20 + elements[1][0] * f21 + elements[2][0] * f22;
		elements[2][1] = elements[0][1] * f20 + elements[1][1] * f21 + elements[2][1] * f22;
		elements[2][2] = elements[0][2] * f20 + elements[1][2] * f21 + elements[2][2] * f22;
		elements[2][3] = elements[0][3] * f20 + elements[1][3] * f21 + elements[2][3] * f22;
		
		elements[0][0] = t00;
		elements[0][1] = t01;
		elements[0][2] = t02;
		elements[0][3] = t03;
		elements[1][0] = t10;
		elements[1][1] = t11;
		elements[1][2] = t12;
		elements[1][3] = t13;
	}
	
	public void scale(Vector3f vec) {
		elements[0][0] = elements[0][0] * vec.x;
		elements[0][1] = elements[0][1] * vec.x;
		elements[0][2] = elements[0][2] * vec.x;
		elements[0][3] = elements[0][3] * vec.x;
		elements[1][0] = elements[1][0] * vec.y;
		elements[1][1] = elements[1][1] * vec.y;
		elements[1][2] = elements[1][2] * vec.y;
		elements[1][3] = elements[1][3] * vec.y;
		elements[2][0] = elements[2][0] * vec.z;
		elements[2][1] = elements[2][1] * vec.z;
		elements[2][2] = elements[2][2] * vec.z;
		elements[2][3] = elements[2][3] * vec.z;
	}
	
	public void setScale(Vector3f vec) {
		elements[0][0] = vec.x;
		elements[1][1] = vec.y;
		elements[2][2] = vec.z;
	}


    public String toString() {
    	String spacing = "  ";
        DecimalFormat formatter = new DecimalFormat(spacing + "0.000E0; -");
        StringBuilder sb = new StringBuilder();
        
		for (int row = 0; row < ROW_COUNT; row++) {
			sb.append("[");
			for (int column = 0; column < COLUMN_COUNT; column++) {
				float element = elements[column][row];
				sb.append(formatter.format(element));
			}
			sb.append(spacing + "]\n");
		}
		
		return sb.toString().replaceAll("E(\\d+)", "E+$1");
    }
	
	
	

}
