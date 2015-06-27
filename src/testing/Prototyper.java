package testing;

import render.Renderer;
import window.WindowManager;
import static org.lwjgl.glfw.GLFW.*;

public abstract class Prototyper implements WindowManager.Callbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src//shaders//vertexShader.txt";
	private static final String FRAGMENT_SHADER_PATH = "src//shaders//fragmentShader.txt";
	
	
	private WindowManager windowManager;
	private Renderer renderer;
	
	@Override
	public void onFramebufferResized(int width, int height) {
		renderer.setFramebufferDimensions(width, height);
	}


	public Prototyper() {
		setUp();
	}
	
	
	public void run() {
		loop(); // The main game loop
		cleanUp();
	}
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return windowManager.isKeyPressed(key);
	}
	
	protected double getTime() {
		return glfwGetTime();
	}
	
	protected void closeWindow() {
		windowManager.setWindowShouldClose(true);
	}
	
	
	private void setUp() {

		// Create window and OpenGL context
		// It is important this is the first setup stage
		windowManager = new WindowManager(WIDTH, HEIGHT, TITLE);
		windowManager.setCallbacks(this); // Callbacks for keyboard and framebuffer resize
		
		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setBackgroundColour(0, 0, 0); // Set background colour to black
		
		// Make window visible now that everything is ready to go!
		windowManager.showWindow();

	}
	
	private void loop() {
		
		double oldTime = 0;
		
		while (!windowManager.windowShouldClose()) {
			
			// Calculate delta time
			double deltaTime = glfwGetTime() - oldTime;
			oldTime = glfwGetTime();
			
			logic(deltaTime); // Game logic goes here
			render(renderer); // Rendering entities goes here
			
			windowManager.update(); // Swap buffers and poll for events
		}
		
	}
	
	protected abstract void logic(double deltaTime);
	protected abstract void render(Renderer renderer);
	
	
	private void cleanUp() {
		renderer.cleanUp();
		cleanUpModels();
		windowManager.close();
	}
	
	protected abstract void cleanUpModels();
	
}
