package testing;

import render.Renderer;
import util.ModelUtils;
import window.WindowManager;
import static org.lwjgl.glfw.GLFW.*;

public abstract class Prototyper implements WindowManager.InputCallbacks, WindowManager.WindowCallbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "src/shaders/fragmentShader.glsl";
	
	
	private WindowManager windowManager;
	private Renderer renderer;
	
	@Override
	public void onFramebufferResized(int width, int height) {
		System.out.println("Framebuffer Width: " + width + ", Height: " + height);
		renderer.updateFramebufferSize(width, height);
	}


	public Prototyper() {
		setUp();
	}
	
	
	public void run() {
		// Show the window just before the main loop
		// This ensures it is only displayed after all other
		// resources have finished loading
		windowManager.showWindow();
		
		loop(); // The main game loop
		cleanUp();
	}
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return windowManager.isKeyPressed(key);
	}
	
	protected float getTime() {
		return (float) glfwGetTime();
	}
	
	protected void closeWindow() {
		windowManager.setShouldClose(true);
	}
	
	
	private void setUp() {
		// Create window and OpenGL context
		// It is important this is the first setup stage
		windowManager = new WindowManager(WIDTH, HEIGHT, TITLE);
		windowManager.setInputCallbacks(this); // Callbacks for keyboard
		windowManager.setWindowCallbacks(this); // Callbacks for framebuffer resize
		
		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setClearColour(0, 0, 0); // Set background colour to black
		
		// Centre window
		windowManager.centreWindow();
	}
	
	private void loop() {
		
		// Zero the timer
		glfwSetTime(0);
		double oldTime = 0;
		
		while (!windowManager.shouldClose()) {
			
			// Calculate delta time
			double deltaTime = glfwGetTime() - oldTime;
			oldTime = glfwGetTime();
			
			logic((float) deltaTime); // Game logic goes here
			renderer.clear();
			render(renderer); // Rendering entities goes here
			
			windowManager.update(); // Swap buffers and poll for events
		}
		
	}
	
	protected abstract void logic(float deltaTime);
	protected abstract void render(Renderer renderer);
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		windowManager.cleanup();
	}
	
}
