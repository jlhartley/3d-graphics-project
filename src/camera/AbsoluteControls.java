package camera;

import math.geometry.Vector3f;
import window.Window;

public class AbsoluteControls extends CameraControls {
	
	
	public AbsoluteControls(Camera camera, Window window) {
		super(camera, window);
	}
	
	
	@Override
	public void moveCamera(float speed, float deltaTime) {
		
		if (!(window.isKeyPressed(FORWARD_KEY) || window.isKeyPressed(BACK_KEY) || 
				window.isKeyPressed(RIGHT_KEY) || window.isKeyPressed(LEFT_KEY) || 
				window.isKeyPressed(UP_KEY) || window.isKeyPressed(DOWN_KEY))) {
			
			return;
		}
		
		Vector3f cameraVelocity = new Vector3f();
		
		// Positional controls
		// Right and left
		if (window.isKeyPressed(RIGHT_KEY)) {
			cameraVelocity.x = 1;
		} else if (window.isKeyPressed(LEFT_KEY)) {
			cameraVelocity.x = -1;
		}
		
		// Up and down
		if (window.isKeyPressed(UP_KEY)) {
			cameraVelocity.y = 1;
		} else if (window.isKeyPressed(DOWN_KEY)){
			cameraVelocity.y = -1;
		}
		
		// Forward and back
		if (window.isKeyPressed(FORWARD_KEY)) {
			cameraVelocity.z = -1;
		} else if (window.isKeyPressed(BACK_KEY)) {
			cameraVelocity.z = 1;
		}
		
		cameraVelocity.setMagnitude(speed);
		
		Vector3f cameraPosition = camera.getPosition();
		
		cameraPosition.x += cameraVelocity.x * deltaTime;
		cameraPosition.y += cameraVelocity.y * deltaTime;
		cameraPosition.z += cameraVelocity.z * deltaTime;
		
	}
	
	
}
