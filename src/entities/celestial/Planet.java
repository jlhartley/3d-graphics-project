package entities.celestial;

import math.geometry.Vector3f;
import model.Model;

// TODO: Should do something about MoveableEntity

public class Planet extends CelestialEntity {
	
	public Planet(Model model) {
		super(model);
	}

	public Planet(Model model, Vector3f position) {
		super(model, position);
	}
	
	public Planet(Model model, Vector3f position, Vector3f velocity) {
		super(model, position, velocity);
	}
	
}
