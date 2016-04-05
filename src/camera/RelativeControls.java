package camera;

import math.geometry.Matrix4f;
import math.geometry.Vector3f;
import ui.UIWindow;

public class RelativeControls extends CameraControls {

	public RelativeControls(Camera camera, UIWindow window) {
		super(camera, window);
	}

	@Override
	public void moveCamera(float movementSpeed, float deltaTime) {
		
		if (!(window.isKeyPressed(FORWARD_KEY) || window.isKeyPressed(BACK_KEY) || 
				window.isKeyPressed(RIGHT_KEY) || window.isKeyPressed(LEFT_KEY) || 
				window.isKeyPressed(UP_KEY) || window.isKeyPressed(DOWN_KEY))) {
			
			return;
		}
		
		Vector3f velocity = new Vector3f();
		
		// Positional controls
		// Right and left
		if (window.isKeyPressed(RIGHT_KEY)) {
			velocity.x = 1;
		} else if (window.isKeyPressed(LEFT_KEY)) {
			velocity.x = -1;
		}
		
		
		// Up and down
		if (window.isKeyPressed(UP_KEY)) {
			velocity.y = 1;
		} else if (window.isKeyPressed(DOWN_KEY)){
			velocity.y = -1;
		}
		
		
		// Forward and back
		if (window.isKeyPressed(FORWARD_KEY)) {
			velocity.z = -1;
		} else if (window.isKeyPressed(BACK_KEY)) {
			velocity.z = 1;
		}
		
		Vector3f rotation = camera.getRotation();
		
		Matrix4f matrix = new Matrix4f();
		// Roll is rarely required
		matrix.rotateZ((float) -Math.toRadians(rotation.z));
		matrix.rotateY((float) -Math.toRadians(rotation.y));
		matrix.rotateX((float) -Math.toRadians(rotation.x));
		
		velocity.multiply(matrix, 0).setMagnitude(movementSpeed);
		
		Vector3f cameraPosition = camera.getPosition();

		cameraPosition.x += velocity.x * deltaTime;
		cameraPosition.y += velocity.y * deltaTime;
		cameraPosition.z += velocity.z * deltaTime;
		
	}
	
	
	

}
