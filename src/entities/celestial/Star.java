package entities.celestial;

import math.geometry.Vector3f;
import model.Model;

public class Star extends CelestialEntity {

	public Star(Model model, Vector3f position) {
		super(model, position);
	}
	
	public Star(Model model, Vector3f position, Vector3f velocity) {
		super(model, position, velocity);
	}
	
}
