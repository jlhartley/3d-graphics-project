package entities.celestial;

import math.geometry.Vector3f;
import model.Model;
import save.PlanetSaveData;

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

	// Constructor for restoring from save
	public Planet(Model model, PlanetSaveData saveData) {
		this(model);
		setPosition(saveData.position);
		setRotation(saveData.rotation);
		setScale(saveData.scale);
		setVelocity(saveData.velocity);
		setAcceleration(saveData.acceleration);
		setMass(saveData.mass);
	}

	public PlanetSaveData getPlanetSaveData() {
		PlanetSaveData planetSaveData = new PlanetSaveData();
		planetSaveData.position = getPosition();
		planetSaveData.rotation = getRotation();
		planetSaveData.scale = getScale();
		planetSaveData.velocity = getVelocity();
		planetSaveData.acceleration = getAcceleration();
		planetSaveData.mass = getMass();
		return planetSaveData;
	}

}
