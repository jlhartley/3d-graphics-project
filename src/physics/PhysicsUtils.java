package physics;

public class PhysicsUtils {
	
	public static double getVolume(double mass, double density) {
		return mass / density;
	}
	
	public static double getDensity(double mass, double volume) {
		return mass / volume;
	}
	
	public static double getMass(double density, double volume) {
		return density * volume;
	}
	
}
