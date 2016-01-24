package testing;

import static logging.Logger.log;

import org.eclipse.swt.widgets.Display;

import logging.Logger;
import math.geometry.Vector2f;
import render.ProjectionType;
import render.Renderer;
import ui.Canvas;
import ui.Window2;
import util.ModelUtils;

public abstract class Prototyper implements /*InputCallbacks, WindowCallbacks*/ Window2.UICallbacks, Canvas.Callbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "src/shaders/fragmentShader.glsl";
	
	
	protected Window2 window;
	protected Renderer renderer;
	
	// Default projection matrix is perspective
	private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	
	
	// UI Callbacks
	
	@Override
	public void onProjectionChanged(ProjectionType projectionType) {
		switchProjection(projectionType);
	}

	// Canvas Callbacks
	
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
	
	private void setUp() {
		
		window = new Window2(new Display(), WIDTH, HEIGHT, TITLE, this, this);
		
		// Create window and OpenGL context
		// It is important this is the first setup stage
		//window = new Window(WIDTH, HEIGHT, TITLE);
		//window.setInputCallbacks(this); // Callbacks for keyboard
		//window.setWindowCallbacks(this); // Callbacks for framebuffer resize
		
		// Set up the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setClearColour(0, 0, 0); // Set background colour to black
		
		// Centre window
		window.centre();
	}
	
	
	public void run() {
		// Show the window just before the main loop
		// This ensures it is only displayed after all other
		// resources have finished loading
		//window.show();
		window.open();
		
		// The main loop
		loop();
		
		// Final clean up of used resources
		cleanUp();
	}
	
	

	
	private void loop() {
		
		// Zero the timer
		//glfwSetTime(0);
		//double oldTime = 0;
		
		// On the first run deltaTime will approximately = 0,
		// so nothing will happen and the first run is like a trial 
		// run to find the deltaTime
		
		long startTime = getTime();
		
		new Thread(new Runnable() {
			
			Logger logicLogger = new Logger();
			
			@Override
			public void run() {
				long oldTime = 0;
				while (!window.isCanvasDisposed()) {
					long currentTime = getTime() - startTime;
					long deltaTime = currentTime - oldTime;
					oldTime = currentTime;
					logicLogger.onFrame(currentTime / 10E8);
					logic((float) (deltaTime / 10E8));
				}
			}
			
		}).start();
		
		window.asyncExec(new Runnable() {
			
			Logger renderLogger = new Logger();
			
			@Override
			public void run() {
				if (!window.isCanvasDisposed()) {
					
					long currentTime = getTime() - startTime;
					
					renderLogger.onFrame(currentTime / 10E8);
					
					renderer.clear();
					render(renderer);
					
					window.getCanvas().swapBuffers();
					window.asyncExec(this);
					
				}
			}
		});
		
		window.loop();
		
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
	
	
	// Mostly a set of convenience methods
	protected boolean isKeyPressed(int key) {
		return window.getCanvas().isKeyPressed(key);
	}
	
	protected Vector2f getCursorPosition() {
		return window.getCanvas().getCursorPosition();
	}
	
	protected void disableCursor() {
		window.disableCursor();
	}
	
	protected void enableCursor() {
		window.enableCursor();
	}
	
	//protected float getTime() {
		//return (float) glfwGetTime();
	//}
	
	protected long getTime() {
		return System.nanoTime();
		//return System.nanoTime() / 10E8;
	}
	
	//private double nanosecondsToSeconds(long nanoseconds) {
	//	return nanoseconds / 10E8;
	//}
	
	protected void closeWindow() {
		//window.setShouldClose(true);
	}
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		window.cleanUp();
	}
	
}
