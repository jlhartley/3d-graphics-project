package simulation;

import static logging.Logger.log;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;

import camera.AbsoluteControls;
import camera.Camera;
import camera.CameraControls;
import camera.RelativeControls;
import input.Key;
import input.MouseButton;
import logging.Logger;
import math.geometry.Vector3f;
import model.Models;
import render.PolygonMode;
import render.ProjectionType;
import render.Renderer;
import ui.Canvas;
import ui.MenuBar;
import ui.SidePanel;
import ui.UIWindow;
import util.ModelUtils;

public abstract class Simulation implements SidePanel.Callbacks, MenuBar.Callbacks, Canvas.Callbacks {
	
	// Constants
	// Initial Display Dimensions - 1280:720 = 16:9
	private static final int INITIAL_WIDTH = 1280;
	private static final int INITIAL_HEIGHT = 720;
	
	private static final String TITLE = "Default Title";
	
	private static final String VERTEX_SHADER_PATH = "res/shaders/simulation/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "res/shaders/simulation/fragmentShader.glsl";
	
	
	UIWindow window;
	Renderer renderer;
	
	Camera camera;
	
	RelativeControls relativeCameraControls;
	AbsoluteControls absoluteCameraControls;
	
	CameraControls cameraControls;
	
	
	// Scales deltaTime
	private double timeMultiplier = 1;
	
	// Pause all logic other than camera movement
	// Should be volatile?
	protected boolean paused = false;
	
	
	// UI Callbacks
	
	@Override
	public void onProjectionTypeSet(ProjectionType projectionType) {
		switchProjection(projectionType);
	}
	
	protected void switchProjection(ProjectionType projectionType) {
		renderer.setProjectionType(projectionType);;
	}
	
	@Override
	public void onTraceSet(boolean shouldTrace) {
		// Trace entities by not clearing the colour buffer
		if (shouldTrace) {
			renderer.setClearBufferBits(GL_DEPTH_BUFFER_BIT);
		} else {
			renderer.setClearBufferBits(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
		}
	}
	
	@Override
	public void onDepthTestEnabledSet(boolean enabled) {
		renderer.setDepthTestEnabled(enabled);
	}
	
	@Override
	public void onFaceCullingEnabledSet(boolean enabled) {
		renderer.setFaceCullingEnabled(enabled);
	}
	
	@Override
	public void onPolygonModeSet(PolygonMode polygonMode) {
		renderer.setPolygonMode(polygonMode);
	}
	
	// UI Input
	
	@Override
	public void onCameraControlTypeSet(boolean relative) {
		if (relative) {
			log("Relative Camera Controls");
			cameraControls = relativeCameraControls;
		} else {
			log("Absolute Camera Controls");
			cameraControls = absoluteCameraControls;
		}
	}
	
	@Override
	public void onCameraPositionSet(Vector3f newPosition) {
		camera.setPosition(newPosition);
	}
	
	@Override
	public void onCameraRotationSet(Vector3f newRotation) {
		camera.setRotation(newRotation);
	}
	
	@Override
	public void onTimeMultiplierSet(double timeMultiplier) {
		this.timeMultiplier = timeMultiplier;
	}
	
	
	// Canvas Callbacks

	@Override
	public void onFramebufferResized(int width, int height) {
		log("Framebuffer Width: " + width + ", Height: " + height);
		renderer.onFramebufferResized(width, height);
	}
	
	// Keyboard Input

	@Override
	public void onKeyPressed(Key key) {
		if (key == Key.P) {
			paused = !paused;
		} else if (key == Key.R) {
			resetCamera();
		} else if (key == Key.U) {
			// Place the camera directly up in y
			camera.setPosition(0, (float) (350 * Math.sqrt(2)), 0);
			// Point camera straight down
			camera.setPitch(90);
			camera.setYaw(0);
		}
	}
	
	@Override
	public void onKeyReleased(Key key) {
		
	}
	
	// Mouse Input
	
	@Override
	public void onMouseDown(MouseButton mouseButton) {
		switch (mouseButton) {
		case LEFT:
			log("Left mouse button down.");
			break;
		case RIGHT:
			log("Right mouse button down.");
			cameraControls.onMouseDown();
			break;
		case MIDDLE:
			log("Middle mouse button down.");
			break;
		}
	}

	@Override
	public void onMouseUp(MouseButton mouseButton) {
		switch (mouseButton) {
		case LEFT:
			log("Left mouse button up.");
			break;
		case RIGHT:
			log("Right mouse button up.");
			cameraControls.onMouseUp();
			break;
		case MIDDLE:
			log("Middle mouse button up.");
			break;
		}
	}
	
	
	
	public Simulation() {
		// Create window and OpenGL context.
		// It is important that this happens before anything else,
		// since other stuff depends on OpenGL context creation.
		window = new UIWindow(INITIAL_WIDTH, INITIAL_HEIGHT, TITLE, this, this, this);
		
		// Instantiate the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH, INITIAL_WIDTH, INITIAL_HEIGHT);
		renderer.setClearColour(0, 0, 0); // Set clear colour to black
		
		// Centre window
		window.centre();
		
		
		camera = new Camera();
		
		relativeCameraControls = new RelativeControls(camera, window);
		absoluteCameraControls = new AbsoluteControls(camera, window);
		
		cameraControls = relativeCameraControls;
		
		resetCamera();
	}
	
	protected void resetCamera() {
		camera.setPosition(0, 350, 350);
		// Point camera downwards at 45 degrees
		camera.setPitch(45);
		camera.setYaw(0);
	}
	
	public void run() {
		// Open the window just before the main loop.
		// This ensures it is only displayed after 
		// all other resources have finished loading.
		//window.show();
		window.open();
		
		// The main loop
		loop();
		
		// Allow subclass to handle closing if needed
		close();
		
		// Final clean up of used resources
		cleanUp();
	}
	
	private void loop() {
		
		// On the first run deltaTime will approximately = 0,
		// so nothing will happen and the first run is like a trial 
		// run to find the deltaTime
		
		final double startTime = getTime();
		
		// The logic thread
		new Thread(new Runnable() {
			
			Logger logicLogger = new Logger();
			
			@Override
			public void run() {
				double oldTime = 0;
				while (!window.isCanvasDisposed()) {
					double currentTime = getTime() - startTime;
					double deltaTime = currentTime - oldTime;
					oldTime = currentTime;
					logicLogger.onFrame(currentTime);
					cameraControls.move((float) deltaTime);
					if (paused) {
						continue;
					}
					logic((float) (deltaTime * timeMultiplier));
				}
			}
			
		}).start();
		
		// The rendering and UI thread
		window.asyncExec(new Runnable() {
			
			Logger renderLogger = new Logger();
			
			@Override
			public void run() {
				if (!window.isCanvasDisposed()) {
					
					double currentTime = getTime() - startTime;
					
					renderLogger.onFrame(currentTime);
					
					renderer.clear();
					render(renderer);
					
					updateUI();
					
					window.getCanvas().swapBuffers();
					window.asyncExec(this);
					
				}
			}
		});
		
		window.loop();
		
	}
	
	protected abstract void logic(float deltaTime);
	protected abstract void render(Renderer renderer);	
	
	private void updateUI() {
		window.getSidePanel().updateCameraPosition(camera.getPosition());
		window.getSidePanel().updateCameraRotation(camera.getRotation());
	}
	
	protected abstract void close();
	
	// Convenience methods
	protected boolean isKeyPressed(int key) {
		return window.getCanvas().isKeyPressed(key);
	}
	
	protected double getTime() {
		return System.nanoTime() / 10E8;
	}
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		Models.getIcosphereModel().cleanUp();
		Models.getRockModel().cleanUp();
		window.cleanUp();
	}
	
	
}
