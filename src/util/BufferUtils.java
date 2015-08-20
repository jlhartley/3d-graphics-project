package util;

import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BufferUtils {
	
	/* 
	 * Much of the code in this class is for using buffers efficiently
	 * and avoiding the problems associated with Java GC calls.
	 * 
	 * Each method is set out like this:
	 * 
	 * First check if we can reuse the same object.
	 * This is dependent on the buffer being non-null
	 * and having sufficient capacity.
	 * 
	 * If the buffer has sufficient capacity, just the limit is too restrictive 
	 * due to it being used previously for a smaller array, then clear it.
	 * 
	 * This is because buffers can be under-used (not used to their maximum capacity)
	 * if the buffer was initialised with a larger capacity for a previous array.
	 * 
	 * There is no object creation unless absolutely necessary.
	 * 
	 */
	
	private static DoubleBuffer doubleBuffer;
	private static FloatBuffer floatBuffer;
	private static IntBuffer intBuffer;
	
	public static DoubleBuffer toBuffer(double[] array) {
		
		if (doubleBuffer == null || array.length > doubleBuffer.capacity()) {
			doubleBuffer = org.lwjgl.BufferUtils.createDoubleBuffer(array.length);
		} else if (array.length > doubleBuffer.limit()) {
			doubleBuffer.clear();
		}
		
		doubleBuffer.put(array);
		doubleBuffer.flip();
		
		return doubleBuffer;
	}
	
	public static FloatBuffer toBuffer(float[] array) {
		
		if (floatBuffer == null || array.length > floatBuffer.capacity()) {
			floatBuffer = org.lwjgl.BufferUtils.createFloatBuffer(array.length);
		} else if (array.length > floatBuffer.limit()) {
			floatBuffer.clear();
		}
		
		floatBuffer.put(array);
		floatBuffer.flip();
		
		return floatBuffer;
	}
	
	public static IntBuffer toBuffer(int[] array) {
		
		if (intBuffer == null || array.length > intBuffer.capacity()) {
			intBuffer = org.lwjgl.BufferUtils.createIntBuffer(array.length);
		} else if (array.length > intBuffer.limit()) {
			intBuffer.clear();
		}
		
		intBuffer.put(array);
		intBuffer.flip();
		
		return intBuffer;
	}
	

}
