package testing;

import static org.lwjgl.glfw.GLFW.*;

import java.util.ArrayList;
import java.util.List;

import entities.Camera;
import entities.Entity;
import entities.MovableEntity;
import math.Matrix4f;
import math.Vector3f;
import math.Vector4f;
import model.Model;
import model.Models;
import render.Renderer;
import util.MathUtils;

public class Tester5 extends Prototyper {
	
	public static void main(String[] args) {
		new Tester5().run();
	}

	@Override
	public void onKeyPressed(int keyCode) {
		
	}

	@Override
	public void onKeyReleased(int keyCode) {

	}
	
	
	Camera camera = new Camera();
	
	List<MovableEntity> planets = new ArrayList<>();
	
	private void addPlanets() {
		
		Model sphereModel = Models.getUVsphereModel();
		
		int planetCount = 10;
		
		for (int i = 0; i < planetCount; i++) {
			Vector3f pos = getPlanetPosition();
			planets.add(new MovableEntity(sphereModel, pos, new Vector3f(), new Vector3f()));
		}
		
	}
	
	private float calculateOrbitRadius(Vector3f pos) {
		return (float) Math.sqrt(pos.x*pos.x + pos.z*pos.z);
	}
	
	private Vector3f getPlanetPosition() {
		float x = (float) MathUtils.randRange(-20, 20);
		float y = 0; // All planets in same y-plane
		float z = (float) MathUtils.randRange(-20, 20);
		return new Vector3f(x, y, z);
	}
	
	
	public Tester5() {
		// Place the camera up and back from the origin
		Vector3f initialCameraPos = new Vector3f(0, 50, 50);
		camera.setPosition(initialCameraPos);
		camera.setPitch(45); // Point camera downwards
		
		addPlanets();
	}
	
	
	
	private void updatePlanetVelocity(MovableEntity planet) {
		
	}
	
	//private float calcTimePeriod(float radius) {
		//return 
	//}
	
	private void updatePlanetPosition(Entity planet, float deltaTime) {
		//Matrix4f transform = Matrix4f.identity();
		//transform.rotate(deltaTime * 2, MathUtils.Y_AXIS);
		
		Vector3f planetPos = planet.getPosition();
		//Vector4f vec4 = new Vector4f(planetPos.x, planetPos.y, planetPos.z, 1);
		
		//vec4.multiply(transform);
		
		//planet.setPosition(vec4.x, vec4.y, vec4.z);
		
		float radius = calculateOrbitRadius(planetPos);
		// Full rotation every 3.6 seconds
		float x = (float) (radius * Math.cos(Math.toRadians(getTime() * 2000 / radius)));
		float z = (float) -(radius * Math.sin(Math.toRadians(getTime() * 2000 / radius)));
		planet.setPosition(x, 0, z);
	}

	@Override
	protected void logic(float deltaTime) {
		
		if (isKeyPressed(GLFW_KEY_S)) {
			camera.increasePitch(30, deltaTime);
		} else if (isKeyPressed(GLFW_KEY_W)) {
			camera.decreasePitch(30, deltaTime);
		}
		
		for (MovableEntity planet : planets) {
			//updatePlanetVelocity(planet);
			//planet.tick(deltaTime);
			updatePlanetPosition(planet, deltaTime);
		}
	}

	@Override
	protected void render(Renderer renderer) {
		
		for (Entity planet : planets) {
			renderer.render(planet, camera);
		}
		
	}
	
	

}
