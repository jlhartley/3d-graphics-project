package testing;

import render.Renderer;
import window.WindowManager;
import static org.lwjgl.glfw.GLFW.*;

public abstract class Prototyper implements WindowManager.KeyListener {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src//shaders//vertexShader.txt";
	private static final String FRAGMENT_SHADER_PATH = "src//shaders//fragmentShader.txt";
	
	
	private WindowManager windowManager;
	private Renderer renderer;
	
	public Prototyper() {
		
		setUp();
		
	}
	
	
	public void run() {
		
		loop();
		
		cleanUp();
		
	}
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return windowManager.isKeyPressed(key);
	}
	
	protected double getTime() {
		return glfwGetTime();
	}
	
	protected int getFramebufferHeight() {
		return windowManager.getFramebufferHeight();
	}
	
	protected int getFramebufferWidth() {
		return windowManager.getFramebufferWidth();
	}
	
	protected float getFramebufferAspectRatio() {
		return windowManager.getFramebufferWidth() / windowManager.getFramebufferHeight();
	}
	
	protected void closeWindow() {
		windowManager.setWindowShouldClose(true);
	}
	
	
	private void setUp() {

		// Set up the window
		windowManager = new WindowManager(WIDTH, HEIGHT, TITLE, this); // Create window and OpenGL context
		windowManager.showWindow(); // Make window visible

		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setBackgroundColour(0, 0, 0); // Set colour to black

	}
	
	private void loop() {
		
		double oldTime = 0;
		
		while (!windowManager.windowShouldClose()) {
			
			// Calculate delta time
			double deltaTime = glfwGetTime() - oldTime;
			oldTime = glfwGetTime();
			
			logic(deltaTime); // Game logic goes here
			renderer.setFramebufferDimensions(getFramebufferWidth(), getFramebufferHeight());
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
