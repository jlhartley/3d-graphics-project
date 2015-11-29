package entities.celestial;

import lighting.LightSource;
import math.geometry.Vector3f;
import model.Model;

public class Star extends CelestialEntity implements LightSource {

	public Star(Model model, Vector3f position) {
		super(model, position);
	}
	
	public Star(Model model, Vector3f position, Vector3f velocity) {
		super(model, position, velocity);
	}
	
	
	
	
	@Override
	public Vector3f getLightPosition() {
		return this.getPosition();
	}

	@Override
	public Vector3f getLightColour() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public float getLightIntensity() {
		// TODO Auto-generated method stub
		return 0;
	}
	
}
