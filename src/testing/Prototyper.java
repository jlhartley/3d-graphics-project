package testing;

import static logging.Logger.log;
import static org.lwjgl.opengl.GL11.*;

import org.eclipse.swt.widgets.Display;

import logging.Logger;
import render.PolygonMode;
import render.ProjectionType;
import render.Renderer;
import ui.Canvas;
import ui.SidePanel;
import ui.UIWindow;
import util.ModelUtils;

public abstract class Prototyper implements SidePanel.Callbacks, Canvas.Callbacks {
	
	// Constants
	// Initial display dimensions - 16:9
	private static final int WIDTH = 1280;
	private static final int HEIGHT = 720;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src/shaders/vertexShader.glsl";
	private static final String FRAGMENT_SHADER_PATH = "src/shaders/fragmentShader.glsl";
	
	
	protected UIWindow window;
	protected Renderer renderer;
	
	// Default projection matrix is perspective
	private ProjectionType projectionType = ProjectionType.PERSPECTIVE;
	
	
	// UI Callbacks
	
	@Override
	public void onProjectionTypeSet(ProjectionType projectionType) {
		switchProjection(projectionType);
	}
	
	protected void switchProjection(ProjectionType type) {
		this.projectionType = type;
		int framebufferWidth = window.getCanvas().getWidth();
		int framebufferHeight = window.getCanvas().getHeight();
		renderer.updateProjection(framebufferWidth, framebufferHeight, type);
	}
	
	@Override
	public void onTraceSet(boolean shouldTrace) {
		if (shouldTrace) {
			// Trace entities by not clearing the colour buffer
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
	
	
	// Canvas Callbacks

	@Override
	public void onFramebufferResized(int width, int height) {
		log("Framebuffer Width: " + width + ", Height: " + height);
		renderer.onFramebufferResized(width, height, projectionType);
	}


	public Prototyper() {
		// Create window and OpenGL context.
		// It is important that this happens before anything else,
		// since other stuff depends on OpenGL context creation.
		window = new UIWindow(new Display(), WIDTH, HEIGHT, TITLE, this, this);
		
		// Instantiate the renderer
		renderer = new Renderer(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		renderer.setClearColour(0, 0, 0); // Set clear colour to black
		
		// Centre window
		window.centre();
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
		
		// Zero the timer
		//glfwSetTime(0);
		//double oldTime = 0;
		
		// On the first run deltaTime will approximately = 0,
		// so nothing will happen and the first run is like a trial 
		// run to find the deltaTime
		
		double startTime = getTime();
		
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
					logic((float) deltaTime);
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
					
					updateUI(window);
					
					window.getCanvas().swapBuffers();
					window.asyncExec(this);
					
				}
			}
		});
		
		window.loop();
		
	}
	
	protected abstract void logic(float deltaTime);
	protected abstract void render(Renderer renderer);	
	protected abstract void updateUI(UIWindow window);
	
	protected abstract void close();
	
	// Convenience methods
	protected boolean isKeyPressed(int key) {
		return window.getCanvas().isKeyPressed(key);
	}
	
	//protected float getTime() {
		//return (float) glfwGetTime();
	//}
	
	protected double getTime() {
		return System.nanoTime() / 10E8;
	}
	
	protected void closeWindow() {
		//window.setShouldClose(true);
	}
	
	
	private void cleanUp() {
		renderer.cleanUp();
		ModelUtils.cleanUp();
		window.cleanUp();
	}
	
}
