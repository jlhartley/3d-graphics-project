package util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	
	public static DoubleBuffer toBuffer(double[] array) {
		
		DoubleBuffer doubleBuffer = org.lwjgl.BufferUtils.createDoubleBuffer(array.length);
		doubleBuffer.put(array);
		doubleBuffer.flip();
		
		return doubleBuffer;
		
	}
	
	public static FloatBuffer toBuffer(float[] array) {
		
		FloatBuffer floatBuffer = org.lwjgl.BufferUtils.createFloatBuffer(array.length);
		floatBuffer.put(array);
		floatBuffer.flip();
		
		return floatBuffer;
		
	}
	
	public static IntBuffer toBuffer(int[] array) {
		
		IntBuffer intBuffer = org.lwjgl.BufferUtils.createIntBuffer(array.length);
		intBuffer.put(array);
		intBuffer.flip();
		
		return intBuffer;
		
	}

}
