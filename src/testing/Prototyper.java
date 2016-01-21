package testing;

import render.Renderer;
import ui.Canvas;
import ui.Window2;
import render.ProjectionType;
import util.ModelUtils;

import static org.lwjgl.glfw.GLFW.*;

import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.widgets.Display;

import logging.Logger;
import math.geometry.Vector2f;

public abstract class Prototyper implements /*InputCallbacks, WindowCallbacks*/ Canvas.Callbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "src/shaders/fragmentShader.glsl";
	
	
	protected Window2 window;
	protected Renderer renderer;
	
	protected Logger logger;
	
	// Default projection matrix is perspective
	private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	
	@Override
	public void onFramebufferResized(int width, int height) {
		log("Framebuffer Width: " + width + ", Height: " + height);
		renderer.onFramebufferResized(width, height, projectionType);
	}
	
	protected void switchProjection(ProjectionType type) {
		this.projectionType = type;
		int framebufferWidth = window.getCanvas().getWidth();
		int framebufferHeight = window.getCanvas().getHeight();
		renderer.updateProjection(framebufferWidth, framebufferHeight, type);
	}


	public Prototyper() {
		setUp();
	}
	
	
	public void run() {
		// Show the window just before the main loop
		// This ensures it is only displayed after all other
		// resources have finished loading
		window.show();
		
		// The main loop
		loop();
		
		// Final clean up of used resources
		cleanUp();
	}
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return window.isKeyPressed(key);
	}
	
	protected Vector2f getMousePosition() {
		return window.getCanvas().getMousePosition();
	}
	
	protected void disableCursor() {
		//window.disableCursor();
	}
	
	protected void enableCursor() {
		//window.enableCursor();
	}
	
	//protected float getTime() {
		//return (float) glfwGetTime();
	//}
	
	protected void log(String message) {
		logger.log(message);
	}
	
	protected void closeWindow() {
		//window.setShouldClose(true);
	}
	
	
	private void setUp() {
		// Instantiate logger
		logger = new Logger();
		
		window = new Window2(new Display(), WIDTH, HEIGHT, TITLE, this);
		//window.setWindowCallbacks(this);
		//window.setInputCallbacks(this);
		
		//window.setCanvasCallbacks(this);
		
		// Create window and OpenGL context
		// It is important this is the first setup stage
		//window = new Window(WIDTH, HEIGHT, TITLE);
		//window.setInputCallbacks(this); // Callbacks for keyboard
		//window.setWindowCallbacks(this); // Callbacks for framebuffer resize
		
		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setClearColour(0, 0, 0); // Set background colour to black
		
		//int canvasWidth = window.getCanvas().getWidth();
		//int canvasHeight = window.getCanvas().getHeight();
		
		//renderer.onFramebufferResized(canvasWidth, canvasHeight, projectionType);
		
		// Centre window
		window.centre();
	}
	
	private double getTime() {
		return System.nanoTime() / 10E8;
	}
	
	private void loop() {
		
		// Zero the timer
		//glfwSetTime(0);
		//double oldTime = 0;
		double startTime = getTime();
		
		// On the first run deltaTime will approximately = 0,
		// so nothing will happen and the first run is like a trial 
		// run to find the deltaTime
		
		Canvas canvas = window.getCanvas();
		
		window.asyncExec(new Runnable() {
			
			private double oldTime = 0;
			
			@Override
			public void run() {
				if (!window.isCanvasDisposed()) {
					
					//canvas.setCurrent();
					
					//System.out.println(oldTime);
					//System.out.println(deltaTime);
					
					double currentTime = getTime() - startTime;
					
					double deltaTime = currentTime - oldTime;
					oldTime = currentTime;
					
					logger.onFrame(currentTime);
					
					//logic((float) deltaTime);
					renderer.clear();
					render(renderer);
					
					canvas.swapBuffers();
					window.asyncExec(this);
					
					
				}
			}
		});
		
		
		Logger logicLogger = new Logger();
		
		new Thread(new Runnable() {
			
			private double oldTime = 0;
			
			@Override
			public void run() {
				while (true) {
					double currentTime = getTime() - startTime;
					double deltaTime = currentTime - oldTime;
					oldTime = currentTime;
					logicLogger.onFrame(currentTime);
					logic((float) deltaTime);
				}
			}
			
		}).start();
		
		window.run();
		
		/*
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
		*/
		
	}
	
	protected abstract void logic(float deltaTime);
	protected abstract void render(Renderer renderer);
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		//window.cleanup();
	}
	
}
