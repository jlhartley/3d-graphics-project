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
import org.lwjgl.opengl.GLContext;

public class WindowManager {
	
	public interface Callbacks {
		public void onKeyPressed(int keyCode);
		public void onKeyReleased(int keyCode);
		public void onFramebufferResized(int width, int height);
	}
	
	
	
	private long window;
	
	private GLFWKeyCallback keyCallback;
	private GLFWErrorCallback errorCallback;
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	
	
	private void initWindow(int width, int height, String title) {
		errorCallback = errorCallbackPrint();
		glfwSetErrorCallback(errorCallback);
		int initSuccess = glfwInit();
		if (initSuccess == GL_FALSE) {
			System.err.println("Could not initialise GLFW!");
			System.exit(1);
		}
		System.out.println("GLFW Version: " + glfwGetVersionString());
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Initially the window is hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // It is also resizable
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window!");
			System.exit(1); // Indicate abnormal termination with the nonzero status code
		}
	}
	
	private void initGL() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
		GLContext.createFromCurrent();
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
	}
	
	
	public WindowManager(int width, int height, String title) {
		initWindow(width, height, title);
		initGL();
	}
	
	
	public void setCallbacks(Callbacks callbacks) {
		// Key callback just calls through to our own interface
		keyCallback = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if (action == GLFW_PRESS) {
                	callbacks.onKeyPressed(key);
                } else if (action == GLFW_RELEASE) {
                	callbacks.onKeyReleased(key);
                }
            }
        };
        glfwSetKeyCallback(window, keyCallback);
        
		// When the window is resized just call through to our own callback
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				callbacks.onFramebufferResized(width, height);
			}
		};
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
	}
	
	public void showWindow() {
        glfwShowWindow(window);
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
	
	public void update() {
		glfwSwapBuffers(window);
		glfwPollEvents();
	}
	
	public boolean isKeyPressed(int key) {
		return glfwGetKey(window, key) == GLFW_PRESS;
	}
	
	public void setWindowShouldClose(boolean value) {
		glfwSetWindowShouldClose(window, value ? GL_TRUE : GL_FALSE);
	}
	
	public boolean windowShouldClose() {
		return glfwWindowShouldClose(window) == GL_TRUE;
	}
	
	public void close() {
		// Handles GLFW cleanup and exit
		glfwDestroyWindow(window);
		keyCallback.release();
		framebufferSizeCallback.release();
		glfwTerminate();
		errorCallback.release();
	}

}
