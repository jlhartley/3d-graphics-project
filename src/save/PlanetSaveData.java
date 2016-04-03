package save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.JSONSerializable;
import math.geometry.Vector3f;

public class PlanetSaveData implements JSONSerializable {
	
	static Gson gson = new GsonBuilder()
			.setPrettyPrinting()
			.create();
	
	
	public Vector3f position;
	public Vector3f rotation;
	public float scale;
	
	public Vector3f velocity;
	public Vector3f acceleration;
	public float mass;
	
	
	@Override
	public String toJSON() {
		return gson.toJson(this);
	}
	
}
