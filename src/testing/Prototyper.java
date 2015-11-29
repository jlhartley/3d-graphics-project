package testing;

import render.Renderer;
import render.ProjectionType;
import util.ModelUtils;
import window.Window;
import window.InputCallbacks;
import window.WindowCallbacks;

import static org.lwjgl.glfw.GLFW.*;

import logging.Logger;
import math.geometry.Vector2f;

public abstract class Prototyper implements InputCallbacks, WindowCallbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "src/shaders/fragmentShader.glsl";
	
	
	protected Window window;
	private Renderer renderer;
	
	private Logger logger;
	
	// Default projection matrix is perspective
	private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	
	@Override
	public void onFramebufferResized(int width, int height) {
		log("Framebuffer Width: " + width + ", Height: " + height);
		renderer.onFramebufferResized(width, height, projectionType);
	}
	
	protected void switchProjection(ProjectionType type) {
		this.projectionType = type;
		renderer.updateProjection(window.getWidth(), window.getHeight(), type);
	}


	public Prototyper() {
		setUp();
	}
	
	
	public void run() {
		// Show the window just before the main loop
		// This ensures it is only displayed after all other
		// resources have finished loading
		window.show();
		
		loop(); // The main game loop
		cleanUp();
	}
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return window.isKeyPressed(key);
	}
	
	protected Vector2f getMousePosition() {
		return window.getMousePosition();
	}
	
	protected void disableCursor() {
		window.disableCursor();
	}
	
	protected void enableCursor() {
		window.enableCursor();
	}
	
	protected float getTime() {
		return (float) glfwGetTime();
	}
	
	protected void log(String message) {
		logger.log(message);
	}
	
	protected void closeWindow() {
		window.setShouldClose(true);
	}
	
	
	private void setUp() {
		// Instantiate logger
		logger = new Logger();
		
		// Create window and OpenGL context
		// It is important this is the first setup stage
		window = new Window(WIDTH, HEIGHT, TITLE);
		window.setInputCallbacks(this); // Callbacks for keyboard
		window.setWindowCallbacks(this); // Callbacks for framebuffer resize
		
		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setClearColour(0, 0, 0); // Set background colour to black
		
		// Centre window
		window.centre();
	}
	
	private void loop() {
		
		// Zero the timer
		glfwSetTime(0);
		double oldTime = 0;
		
		// On the first run deltaTime will approximately = 0,
		// so nothing will happen and the first run is like a trial 
		// run to find the deltaTime
		
		while (!window.shouldClose()) {
			
			double currentTime = glfwGetTime();
			
			// Calculate delta time
			double deltaTime = currentTime - oldTime;
			oldTime = currentTime;
			
			// Output data at regular intervals
			logger.onFrame(currentTime);
			
			logic((float) deltaTime); // Logic goes here
			renderer.clear();
			render(renderer); // Rendering entities goes here
			
			window.update(); // Swap buffers and poll for events
			
		}
		
	}
	
	protected abstract void logic(float deltaTime);
	protected abstract void render(Renderer renderer);
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		window.cleanup();
	}
	
}
