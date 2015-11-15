package math;

public class MathUtils {
	
	// Returns uniformly distributed random doubles in a given range
	public static double randRange(double min, double max) {
		return min + Math.random() * (max - min);
	}
	
}
