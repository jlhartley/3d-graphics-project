package entities.celestial;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.JSONSerializable;
import math.geometry.Vector3f;
import model.Model;

// TODO: Should do something about MoveableEntity

public class Planet extends CelestialEntity implements JSONSerializable {
	
	static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	public Planet(Model model) {
		super(model);
	}

	public Planet(Model model, Vector3f position) {
		super(model, position);
	}
	
	public Planet(Model model, Vector3f position, Vector3f velocity) {
		super(model, position, velocity);
	}
	
	
	@Override
	public String toJSON() {
		return gson.toJson(this);
	}
	
}
