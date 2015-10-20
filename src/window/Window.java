package window;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;

import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.libffi.Closure;

import math.Vector2f;

public class Window {
	
	public interface InputCallbacks {
		public void onKeyPressed(int keyCode);
		public void onKeyReleased(int keyCode);
	}
	
	public interface WindowCallbacks {
		public void onFramebufferResized(int width, int height);
	}
	
	
	// The pointer that this class wraps
	private long window;
	
	// Keep track of the window width and height in screen coordinates
	private int width;
	private int height;
	
	// The centre point of the window, in screen coordinates
	// relative to the client area origin
	private Vector2f centre = new Vector2f();
	
	// Mouse position, relative the the client area centre
	private Vector2f mousePosition = new Vector2f();
	
	// Callbacks require strong references because they are called from native code
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	private GLFWWindowSizeCallback windowSizeCallback;
	private GLFWCursorPosCallback cursorPosCollback;
	
	private Closure debugMessageCallback;
	
	private void initWindow(int width, int height, String title) {
		// Set an error callback so that a human-readable description of any
		// errors relating to GLFW are output
		// Print any errors to the standard error stream (System.err)
		errorCallback = errorCallbackPrint();
		glfwSetErrorCallback(errorCallback);
		
		// Initialise GLFW - this is required before most GLFW functions
		// can be called
		int initSuccess = glfwInit();
		if (initSuccess == GL_FALSE) {
			System.err.println("Could not initialise GLFW!");
			cleanup();
			// Exiting with non-zero status code indicates abnormal termination
			System.exit(1);
		}
		// Display version for debugging purposes if initialisation was successful
		System.out.println("GLFW Version: " + glfwGetVersionString());
		
		// Set our window hints
		initWindowHints();
		
		// Create the window, not in fullscreen mode and 
		// without sharing any resources with another window
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window!");
			cleanup();
			System.exit(1);
		}
		
		
		// Make sure that width and height get updated
		// Note that virtual screen coordinates do not necessarily correspond
		// to pixels - imagine dragging a window from a higher resolution monitor
		// to a lower resolution one - the window size stays the same
		windowSizeCallback = new GLFWWindowSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				Window.this.width = width;
				Window.this.height = height;
				centre.x = width / 2;
				centre.y = height / 2;
				System.out.println("Window Width: " + width + ", Height: " + height);
			}
		};
		glfwSetWindowSizeCallback(window, windowSizeCallback);
		
		// Set up the cursor position callback so we can grab the current cursor position
		// at any time
		cursorPosCollback = new GLFWCursorPosCallback() {
			@Override
			public void invoke(long window, double xpos, double ypos) {
				// Translate our cursor coordinates so that they are relative to the centre
				//mousePosition.x = (float) (xpos - centre.x);
				//mousePosition.y = (float) (ypos - centre.y);
				
				//mousePosition.negate();
				
				mousePosition.x = (float) xpos;
				mousePosition.y = (float) ypos;
				
				// Flip the coordinate system
				mousePosition.negate();
				
				// Translate to the centre
				mousePosition.translate(centre);
				
				System.out.println("Mouse Position X: " + mousePosition.x + ", Y: " + mousePosition.y);
			}
		};
		glfwSetCursorPosCallback(window, cursorPosCollback);
	}
	
	private void initWindowHints() {
		glfwDefaultWindowHints(); // Start from default hints
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Initially the window is hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // It is also resizable
		glfwWindowHint(GLFW_SAMPLES, 16); // Enable 16x anti-aliasing
	}
	
	private void initGL() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
		GL.createCapabilities();
		debugMessageCallback = GLUtil.setupDebugMessageCallback();
		// If OpenGL initialisation was successful, display the version
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
	}
	
	
	public Window(int width, int height, String title) {
		// Set some initial fields
		this.width = width;
		this.height = height;
		centre.x = width / 2;
		centre.y = height / 2;
		initWindow(width, height, title);
		initGL();
	}
	
	
	public void setInputCallbacks(final InputCallbacks inputCallbacks) {
		// Key callback just calls through to our own interface
		keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				// Here repeat does not count as a key press
				if (action == GLFW_PRESS) {
					inputCallbacks.onKeyPressed(key);
				} else if (action == GLFW_RELEASE) {
					inputCallbacks.onKeyReleased(key);
				}
			}
		};
		glfwSetKeyCallback(window, keyCallback);
	}
	
	public void setWindowCallbacks(final WindowCallbacks windowCallbacks) {
		// When the framebuffer is resized just call through to our own callback
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				windowCallbacks.onFramebufferResized(width, height);
			}
		};
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
	}
	
	
	public void show() {
        glfwShowWindow(window);
	}
	
	public void hide() {
		glfwHideWindow(window);
	}
	
	// Place the window at the centre of the screen
	public void centre() {
		// Get the width and height of the primary monitor in screen coordinates
		long primaryMonitor = glfwGetPrimaryMonitor();
		ByteBuffer videoMode = glfwGetVideoMode(primaryMonitor);
		int screenWidth = GLFWvidmode.width(videoMode);
		int screenHeight = GLFWvidmode.height(videoMode);
		
		glfwSetWindowPos(window,
				(screenWidth - this.width) / 2,
				(screenHeight - this.height) / 2);
	}
	
	// Swap the front and back buffers and poll for events
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public boolean isKeyPressed(int key) {
		return glfwGetKey(window, key) == GLFW_PRESS;
	}
	
	public void setShouldClose(boolean value) {
		glfwSetWindowShouldClose(window, value ? GL_TRUE : GL_FALSE);
	}
	
	public boolean shouldClose() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	
	// Basic getters and setters
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Vector2f getMousePosition() {
		return mousePosition;
	}
	
	
	// Handles GLFW and GL cleanup and exit
	public void cleanup() {
		if (debugMessageCallback != null) {
			debugMessageCallback.release();
		}
		if (window != NULL) {
			glfwDestroyWindow(window);
		}
		// Callbacks need to be released because they use native resources
		if (keyCallback != null) {
			keyCallback.release();
		}
		if (framebufferSizeCallback != null) {
			framebufferSizeCallback.release();
		}
		if (windowSizeCallback != null) {
			windowSizeCallback.release();
		}
		if (cursorPosCollback != null) {
			cursorPosCollback.release();
		}
		glfwTerminate();
		errorCallback.release();
	}

}
