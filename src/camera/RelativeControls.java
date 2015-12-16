package camera;

import math.geometry.Matrix4f;
import math.geometry.MatrixUtils;
import math.geometry.Vector3f;
import math.geometry.Vector4f;
import window.Window;

public class RelativeControls extends CameraControls {

	public RelativeControls(Camera camera, Window window) {
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
		
		Vector4f velocity4 = new Vector4f(velocity, 0);
		
		Vector3f rotation = camera.getRotation();
		
		Matrix4f matrix = new Matrix4f();
		matrix.rotate((float) -Math.toRadians(rotation.y), MatrixUtils.Y_AXIS);
		matrix.rotate((float) -Math.toRadians(rotation.x), MatrixUtils.X_AXIS);
		// Roll is not required
		//matrix.rotate((float) -Math.toRadians(rotation.z), MatrixUtils.Z_AXIS);
		
		velocity4.multiply(matrix);
		
		velocity.set(velocity4.x, velocity4.y, velocity4.z).setMagnitude(movementSpeed);
		
		Vector3f cameraPosition = camera.getPosition();

		cameraPosition.x += velocity.x * deltaTime;
		cameraPosition.y += velocity.y * deltaTime;
		cameraPosition.z += velocity.z * deltaTime;
		
		
		/*
		if (window.isKeyPressed(FORWARD_KEY) || window.isKeyPressed(BACK_KEY)) {
			
			Vector3f cameraRotation = camera.getRotation();
			
			//float pitch = cameraRotation.x;
			//float yaw = cameraRotation.y;
			
			
			// Project the movement speed onto the x-z plane
			float projectedMovementSpeed = (float) (movementSpeed * Math.cos(Math.toRadians(cameraRotation.x)));
			
			// Movement in x and z can now be considered as an orthographic projection
			// Angle is z, therefore x is sin and z is cosine (against the convention)
			cameraVelocity.x = (float) (projectedMovementSpeed * Math.sin(Math.toRadians(cameraRotation.y)));
			// z is negated since z decreases into the screen (with depth, when considering perspective)
			cameraVelocity.z = -(float) (projectedMovementSpeed * Math.cos(Math.toRadians(cameraRotation.y)));
			
			// Rotation around x increases as the camera is pointed down. Since we want to move down in y
			// more as the camera is pointed further down, the rotation needs to be negated.
			// Also note: sin(-theta)=-sin(theta)
			cameraVelocity.y = (float) (movementSpeed * Math.sin(Math.toRadians(-cameraRotation.x)));
			
			if (window.isKeyPressed(BACK_KEY)) {
				
				cameraVelocity.x = -cameraVelocity.x;
				cameraVelocity.y = -cameraVelocity.y;
				cameraVelocity.z = -cameraVelocity.z;
				
			}
			
			if (window.isKeyPressed(RIGHT_KEY)) {
				
				
				
			}
			
		}*/
	}
	
	
	

}
