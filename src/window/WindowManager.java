package window;

import static org.lwjgl.glfw.Callbacks.errorCallbackPrint;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
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
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE); // Initially the window is hidden
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE); // It is also re-sizable
		window = glfwCreateWindow(width, height, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window!");
			System.exit(1); // Indicate abnormal termination with the nonzero status code
		}
	}
	
	
	private void initCallbacks(Callbacks callbacks) {
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
        
		// When the window is resized, update our instance variables and adjust the viewport
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {
			@Override
			public void invoke(long window, int width, int height) {
				System.out.println("Framebuffer width: " + width + ", Framebuffer height: " + height);
				callbacks.onFramebufferResized(width, height);
				glViewport(0, 0, width, height);
			}
		};
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
	}
	
	
	private void initGL() {
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
		GLContext.createFromCurrent();
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
	}
	
	
	public WindowManager(int width, int height, String title, Callbacks callbacks) {
		initWindow(width, height, title);
		initCallbacks(callbacks);
		initGL();
	}
	
	
	public void showWindow() {
        glfwShowWindow(window);
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
