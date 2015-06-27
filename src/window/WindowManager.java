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
	
	public interface KeyListener {
		public void onKeyPressed(int keyCode);
		public void onKeyReleased(int keyCode);
	}
	
	
	private int windowWidth;
	private int windowHeight;
	private String title;
	
	private int framebufferWidth;
	private int framebufferHeight;
	
	private long window;
	private GLFWKeyCallback keyCallback;
	private GLFWErrorCallback errorCallback;
	
	private GLFWFramebufferSizeCallback framebufferSizeCallback;
	
	private KeyListener keyListener;
	
	private void init() {
		errorCallback = errorCallbackPrint(System.err);
		glfwSetErrorCallback(errorCallback);
		int initSuccess = glfwInit();
		if (initSuccess == GL_FALSE) {
			System.err.println("Could not initialise GLFW!");
			System.exit(1);
		}
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		window = glfwCreateWindow(windowWidth, windowHeight, title, NULL, NULL);
		if (window == NULL) {
			System.err.println("Could not create window!");
			System.exit(1);
		}
		glfwMakeContextCurrent(window);
		
		keyCallback = new GLFWKeyCallback() {
			
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                
                if (action == GLFW_PRESS) {
                	keyListener.onKeyPressed(key);
                } else if (action == GLFW_RELEASE) {
                	keyListener.onKeyReleased(key);
                }
                
            }
        };
		
        glfwSetKeyCallback(window, keyCallback);
        
        glfwMakeContextCurrent(window);
        glfwSwapInterval(1);
        
		GLContext.createFromCurrent();
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
        
		// When the window is resized, update our instance variables and adjust the viewport
		framebufferSizeCallback = new GLFWFramebufferSizeCallback() {

			@Override
			public void invoke(long window, int width, int height) {
				framebufferWidth = width;
				framebufferHeight = height;
				System.out.println("Framebuffer width: " + width + ", Framebuffer height: " + height);
				glViewport(0, 0, width, height);
			}
		};
		
		glfwSetFramebufferSizeCallback(window, framebufferSizeCallback);
		
	}
	
	public WindowManager(int width, int height, String title, KeyListener keyListener) {
		
		this.windowWidth = width;
		this.windowHeight = height;
		this.title = title;
		this.keyListener = keyListener;
		
		init();
		
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
	
	public int getFramebufferHeight() {
		return framebufferHeight;
	}
	
	public int getFramebufferWidth() {
		return framebufferWidth;
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
