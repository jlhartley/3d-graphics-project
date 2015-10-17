package window;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWvidmode;
import org.lwjgl.opengl.GL;

public class WindowManager {
	
	public interface InputCallbacks {
		public void onKeyPressed(int keyCode);
		public void onKeyReleased(int keyCode);
	}
	
	public interface WindowCallbacks {
		public void onFramebufferResized(int width, int height);
	}
	
	
	
	private long window;
	
	// Callbacks require strong references because they are called from native code
	private GLFWErrorCallback errorCallback;
	private GLFWKeyCallback keyCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	
	
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
		
		// Set our window  hints
		initWindowHints();
		
		// Create the window, not in fullscreen mode and 
		// without sharing any resources with another window
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window!");
			cleanup();
			System.exit(1);
		}
	}
	
	private void initWindowHints() {
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Initially the window is hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // It is also resizable
		glfwWindowHint(GLFW_SAMPLES, 16); // Enable 16x anti-aliasing
	}
	
	private void initGL() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
		GL.createCapabilities();
		// If OpenGL initialisation was successful, display the version
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
	}
	
	
	public WindowManager(int width, int height, String title) {
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
	
	
	public void showWindow() {
        glfwShowWindow(window);
	}
	
	public void hideWindow() {
		glfwHideWindow(window);
	}
	
	// Place the window at the centre of the screen
	public void centreWindow() {
		// Get the width and height of the primary monitor in screen coordinates
		long primaryMonitor = glfwGetPrimaryMonitor();
		ByteBuffer videoMode = glfwGetVideoMode(primaryMonitor);
		int screenWidth = GLFWvidmode.width(videoMode);
		int screenHeight = GLFWvidmode.height(videoMode);
		
		// Get the width and height of the window in screen coordinates
		IntBuffer windowWidthBuffer = BufferUtils.createIntBuffer(1);
		IntBuffer windowHeightBuffer = BufferUtils.createIntBuffer(1);
		glfwGetWindowSize(window, windowWidthBuffer, windowHeightBuffer);
		int windowWidth = windowWidthBuffer.get();
		int windowHeight = windowHeightBuffer.get();
		
		glfwSetWindowPos(window,
				(screenWidth - windowWidth) / 2,
				(screenHeight - windowHeight) / 2);
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
	
	// Handles GLFW cleanup and exit
	public void cleanup() {
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
		glfwTerminate();
		errorCallback.release();
	}

}
