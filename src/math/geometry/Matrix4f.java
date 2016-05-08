package math.geometry;

import java.text.DecimalFormat;

public class Matrix4f {
	
	// Constants
	public static final int ROW_COUNT = 4;
	public static final int COLUMN_COUNT = 4;
	public static final int SIZE = COLUMN_COUNT * ROW_COUNT;
	
	public float[][] elements = new float[COLUMN_COUNT][ROW_COUNT];
	
	
	public Matrix4f() {
		// A new matrix will be set to the identity matrix
		identity();
	}
	
	public Matrix4f(float a, float b, float c, float d, 
					float e, float f, float g, float h,
					float i, float j, float k, float l,
					float m, float n, float o, float p) {
		
		/*
		// Column 0
		elements[0][0] = a;
		elements[0][1] = e;
		elements[0][2] = i;
		elements[0][3] = m;
		
		// Column 1
		elements[1][0] = b;
		elements[1][1] = f;
		elements[1][2] = j;
		elements[1][3] = n;
		
		// Column 2
		elements[2][0] = c;
		elements[2][1] = g;
		elements[2][2] = k;
		elements[2][3] = o;
		
		// Column 3
		elements[3][0] = d;
		elements[3][1] = h;
		elements[3][2] = l;
		elements[3][3] = p;
		*/
		
		
		// [ a , e , i , m ]
		// [ b , f , j , n ]
		// [ c , g , k , o ]
		// [ d , h , l , p ]
		
		// Column 0
		elements[0][0] = a;
		elements[0][1] = b;
		elements[0][2] = c;
		elements[0][3] = d;
		
		// Column 1
		elements[1][0] = e;
		elements[1][1] = f;
		elements[1][2] = g;
		elements[1][3] = h;
		
		// Column 2
		elements[2][0] = i;
		elements[2][1] = j;
		elements[2][2] = k;
		elements[2][3] = l;
		
		// Column 3
		elements[3][0] = m;
		elements[3][1] = n;
		elements[3][2] = o;
		elements[3][3] = p;
		
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
	
	public void setTranslation(Vector3f vec) {
		elements[3][0] = vec.x;
		elements[3][1] = vec.y;
		elements[3][2] = vec.z;
	}
	
	// Methods for rotating about a given axis
	
	public void rotateX(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);

        // Temporary variables for dependent values
        float temp10 = elements[1][0] * cos + elements[2][0] * sin;
        float temp11 = elements[1][1] * cos + elements[2][1] * sin;
        float temp12 = elements[1][2] * cos + elements[2][2] * sin;
        float temp13 = elements[1][3] * cos + elements[2][3] * sin;
        
        // Non-dependent values set directly
        elements[2][0] = elements[1][0] * -sin + elements[2][0] * cos;
        elements[2][1] = elements[1][1] * -sin + elements[2][1] * cos;
        elements[2][2] = elements[1][2] * -sin + elements[2][2] * cos;
        elements[2][3] = elements[1][3] * -sin + elements[2][3] * cos;
        
        elements[1][0] = temp10;
        elements[1][1] = temp11;
        elements[1][2] = temp12;
        elements[1][3] = temp13;
	}
	
	
	public void rotateY(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		
		// Temporary variables for dependent values
        float temp00 = elements[0][0] * cos + elements[2][0] * -sin;
        float temp01 = elements[0][1] * cos + elements[2][1] * -sin;
        float temp02 = elements[0][2] * cos + elements[2][2] * -sin;
        float temp03 = elements[0][3] * cos + elements[2][3] * -sin;
        
        // Non-dependent values set directly
        elements[2][0] = elements[0][0] * sin + elements[2][0] * cos;
        elements[2][1] = elements[0][1] * sin + elements[2][1] * cos;
        elements[2][2] = elements[0][2] * sin + elements[2][2] * cos;
        elements[2][3] = elements[0][3] * sin + elements[2][3] * cos;
        
        elements[0][0] = temp00;
        elements[0][1] = temp01;
        elements[0][2] = temp02;
        elements[0][3] = temp03;
	}
	
	public void rotateZ(float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		
		// Temporary variables for dependent values
        float temp00 = elements[0][0] * cos + elements[1][0] * sin;
        float temp01 = elements[0][1] * cos + elements[1][1] * sin;
        float temp02 = elements[0][2] * cos + elements[1][2] * sin;
        float temp03 = elements[0][3] * cos + elements[1][3] * sin;
        
        // Non-dependent values set directly
        elements[1][0] = elements[0][0] * -sin + elements[1][0] * cos;
        elements[1][1] = elements[0][1] * -sin + elements[1][1] * cos;
        elements[1][2] = elements[0][2] * -sin + elements[1][2] * cos;
        elements[1][3] = elements[0][3] * -sin + elements[1][3] * cos;
        
        elements[0][0] = temp00;
        elements[0][1] = temp01;
        elements[0][2] = temp02;
        elements[0][3] = temp03;
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
	
	// Apply a uniform scale - equivalent to scale(new Vector3f(s, s, s))
	public void scale(float s) {
		elements[0][0] = elements[0][0] * s;
		elements[0][1] = elements[0][1] * s;
		elements[0][2] = elements[0][2] * s;
		elements[0][3] = elements[0][3] * s;
		elements[1][0] = elements[1][0] * s;
		elements[1][1] = elements[1][1] * s;
		elements[1][2] = elements[1][2] * s;
		elements[1][3] = elements[1][3] * s;
		elements[2][0] = elements[2][0] * s;
		elements[2][1] = elements[2][1] * s;
		elements[2][2] = elements[2][2] * s;
		elements[2][3] = elements[2][3] * s;
	}
	
	public void setScale(Vector3f vec) {
		elements[0][0] = vec.x;
		elements[1][1] = vec.y;
		elements[2][2] = vec.z;
	}

	
	
    public Matrix4f invert() {
    	
        float a = elements[0][0] * elements[1][1] - elements[0][1] * elements[1][0];
        float b = elements[0][0] * elements[1][2] - elements[0][2] * elements[1][0];
        float c = elements[0][0] * elements[1][3] - elements[0][3] * elements[1][0];
        float d = elements[0][1] * elements[1][2] - elements[0][2] * elements[1][1];
        float e = elements[0][1] * elements[1][3] - elements[0][3] * elements[1][1];
        float f = elements[0][2] * elements[1][3] - elements[0][3] * elements[1][2];
        float g = elements[2][0] * elements[3][1] - elements[2][1] * elements[3][0];
        float h = elements[2][0] * elements[3][2] - elements[2][2] * elements[3][0];
        float i = elements[2][0] * elements[3][3] - elements[2][3] * elements[3][0];
        float j = elements[2][1] * elements[3][2] - elements[2][2] * elements[3][1];
        float k = elements[2][1] * elements[3][3] - elements[2][3] * elements[3][1];
        float l = elements[2][2] * elements[3][3] - elements[2][3] * elements[3][2];
        
        float det = a * l - b * k + c * j + d * i - e * h + f * g;
        det = 1.0f / det;
        
    	Matrix4f inverse = new Matrix4f(( elements[1][1] * l - elements[1][2] * k + elements[1][3] * j) * det,
                						(-elements[0][1] * l + elements[0][2] * k - elements[0][3] * j) * det,
                						( elements[3][1] * f - elements[3][2] * e + elements[3][3] * d) * det,
                						(-elements[2][1] * f + elements[2][2] * e - elements[2][3] * d) * det,
                						(-elements[1][0] * l + elements[1][2] * i - elements[1][3] * h) * det,
                						( elements[0][0] * l - elements[0][2] * i + elements[0][3] * h) * det,
                						(-elements[3][0] * f + elements[3][2] * c - elements[3][3] * b) * det,
                						( elements[2][0] * f - elements[2][2] * c + elements[2][3] * b) * det,
                						( elements[1][0] * k - elements[1][1] * i + elements[1][3] * g) * det,
                						(-elements[0][0] * k + elements[0][1] * i - elements[0][3] * g) * det,
                						( elements[3][0] * e - elements[3][1] * c + elements[3][3] * a) * det,
                						(-elements[2][0] * e + elements[2][1] * c - elements[2][3] * a) * det,
                						(-elements[1][0] * j + elements[1][1] * h - elements[1][2] * g) * det,
                						( elements[0][0] * j - elements[0][1] * h + elements[0][2] * g) * det,
                						(-elements[3][0] * d + elements[3][1] * b - elements[3][2] * a) * det,
                						( elements[2][0] * d - elements[2][1] * b + elements[2][2] * a) * det);
       
        return inverse;
    }

    public String toString() {
    	String spacing = "  ";
    	// The ";" separates positive and negative subpatterns
    	// For a negative number, one of the spaces is replaced with a "-"
        DecimalFormat formatter = new DecimalFormat(spacing + "0.0000E0; -");
        StringBuilder sb = new StringBuilder();
        
		for (int row = 0; row < ROW_COUNT; row++) {
			sb.append("[");
			for (int column = 0; column < COLUMN_COUNT; column++) {
				float element = elements[column][row];
				sb.append(formatter.format(element));
			}
			sb.append(spacing + "]\n");
		}
		
		// Use regular expression to replace all "E"s followed by a digit with "E+"
		// This aligns all the positive powers with any negative powers
		return sb.toString().replaceAll("E(\\d+)", "E+$1");
    }
	
	
	

}
