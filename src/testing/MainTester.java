package testing;

import math.Matrix4f;
import math.Vector3f;
import model.VAOModel;
import model.VertexAttribute;
import shaders.ShaderProgram;
import util.MathUtils;
import window.WindowManager;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public class MainTester implements WindowManager.KeyListener {
	
	public static void main(String[] args) {
		new MainTester();
	}
	
	
	
	
	// Constants
	private static final int WIDTH = 1200;
	private static final int HEIGHT = 900;
	
	private static final String TITLE = "Test Title";
	
	private static final String VERTEX_SHADER_PATH = "src//shaders//vertexShader.txt";
	private static final String FRAGMENT_SHADER_PATH = "src//shaders//fragmentShader.txt";
	
	
	
	WindowManager windowManager;
	ShaderProgram shaderProgram;
	VAOModel vaoModel;
	
	public MainTester() {
		
		setUp();
		
		loop();
		
		cleanUp();
		
		
	}
	
	private void setUp() {
		
		windowManager = new WindowManager(WIDTH, HEIGHT, TITLE, this); // Create window and OpenGL context
		windowManager.showWindow(); // Make window visible
		
		
		float[] vertexPositionData = {
				-0.2f, 0.2f, // Top left
				-0.2f, -0.2f, // Bottom left
				0.2f, -0.2f, // Bottom right
				0.2f, 0.2f // Top right
		};

		/*float[] vertexColourData = {
				0.345f, 0.573f, 0.0f,
				0.0f, 0.04f, 0.387f,
				0.945f, 0.374f, 0.445f,
				0.834f, 0.87f, 0.01f
		};*/
		
		
		float[] vertexColourData = {
				1.0f, 0.0f, 0.0f,
				0.0f, 0.0f, 1.0f,
				0.0f, 1.0f, 0.0f,
				1.0f, 1.0f, 0.0f
		};
		
		/*float[] vertexColourData = {
				1.0f, 0.0f, 0.0f,
				0.0f, 1.0f, 0.0f,
				0.0f, 0.0f, 1.0f,
				1.0f, 1.0f, 0.0f
		};*/
		
		int[] vertexIndices = {
				0, 1, 3, // Top left triangle
				3, 1, 2  // Bottom right triangle
		};
		
		
		//int[] vertexIndices = {
		//		0, 1, 2, 0
		//};
		
		
		//int [] vertexIndices = {
		//		0, 1, 2, 3, 0
		//};
		
		// TODO: Associate the VertexAttribute objects with the actual data into one class
		// To achieve I must split the vertexData up into multiple arrays - maybe an abstract vertexAttribute class?
		
		// Detail the vertex attributes
		// data, index (in the GLSL), name (in the GLSL), size (number of elements used per vertex)
		VertexAttribute position = new VertexAttribute(vertexPositionData, 0, "position", 2);
		VertexAttribute colour = new VertexAttribute(vertexColourData, 1, "colour", 3);
		
		// Layout: (x,y,r,g,b)
		/*float[] vertices = {
				0.0f,  0.5f, 1.0f, 0.0f, 0.0f, // Vertex 1
				0.5f, -0.5f, 0.0f, 1.0f, 0.0f, // Vertex 2
				-0.5f, -0.5f, 0.0f, 0.0f, 1.0f // vertex 3
		};*/
		
		
		vaoModel = new VAOModel();
		//vaoModel.addVertexAttrib(position);
		//vaoModel.addVertexAttrib(colour);
		vaoModel.setIBOData(vertexIndices);
		//vaoModel.unbindVAO();
		//vaoModel.setUpVertexAttributes();
		
		
		//shaderProgram = new ShaderProgram.Builder()
		//		.addVertexShader(VERTEX_SHADER_PATH)
		//		.addFragmentShader(FRAGMENT_SHADER_PATH)
		//		.setAttribLocation(position)
		//		.setAttribLocation(colour)
		//		.build();
		
		
		shaderProgram = new ShaderProgram(VERTEX_SHADER_PATH, FRAGMENT_SHADER_PATH);
		
		shaderProgram.useProgram();
		
		
		
		glClearColor(158/255f, 230/255f, 249/255f, 1.0f); // Set our background colour
		
		//glEnable(GL_LINE_SMOOTH);
		//glHint(GL_LINE_SMOOTH_HINT,  GL_NICEST);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
	}
	
	
	private void loop() {
		
		double oldTime = 0;
		
		Vector3f positionVector = new Vector3f(0.0f, 0.0f, 0.0f);
		
		float rotation = 0;
		
		float speed = 1.5f;
		
		float time = 0;
		
		while(!windowManager.windowShouldClose()) {
			
			double deltaTime = glfwGetTime() - oldTime;
			oldTime = glfwGetTime();
			
			//System.out.println(1/deltaTime);
			
			if (windowManager.isKeyPressed(GLFW_KEY_RIGHT_CONTROL)) {
				deltaTime = deltaTime / 5; // Slow down time itself!
			}
			
			if (windowManager.isKeyPressed(GLFW_KEY_SPACE)) {
				deltaTime = 0; // No time passes when paused
			} else {
				time = (float) glfwGetTime(); // Only updated if we are not paused
			}
			
			//glClearColor((float)Math.sin(glfwGetTime()), (float)Math.cos(glfwGetTime()), (float)Math.sin(glfwGetTime()*10), 1.0f);
			
			// Preparation methods
			glClear(GL_COLOR_BUFFER_BIT);
			
			if (spinToggle) {
				rotation += deltaTime * 50;
			}

			
			if (windowManager.isKeyPressed(GLFW_KEY_W)) {
				positionVector.y += deltaTime * speed;
			}
			if (windowManager.isKeyPressed(GLFW_KEY_A)) {
				positionVector.x -= deltaTime * speed;
			}
			if (windowManager.isKeyPressed(GLFW_KEY_S)) {
				positionVector.y -= deltaTime * speed;
			}
			if (windowManager.isKeyPressed(GLFW_KEY_D)) {
				positionVector.x += deltaTime * speed;
			}
			
			
			//glDrawArrays(GL_TRIANGLES, 0, vaoModel.getVertexCount());
			
			shaderProgram.setUniformValue("time", time);
			
			//shaderProgram.setUniformValue("transformationMatrix", MathUtils.createTransformationMatrix(positionVector, 0.0f, rotation, rotation, 1.0f));
			shaderProgram.setUniformValue("transformationMatrix", MathUtils.createTransformationMatrix(positionVector, rotation*10, 1.0f));

			
			glDrawElements(GL_TRIANGLES, vaoModel.getVertexCount(), GL_UNSIGNED_INT, 0);
			
			
			
			windowManager.update(); // Swap the buffers and poll for events
		}
		
		
	}
	
	private void cleanUp() {
		shaderProgram.cleanUp();
		vaoModel.cleanUp();
		windowManager.close();
	}
	
	
	boolean spinToggle = false;
	
	// Keyboard listener
	@Override
	public void onKeyPressed(int keyCode) {
		if (keyCode == GLFW_KEY_LEFT_SHIFT) {
			spinToggle = !spinToggle;
		}
	}

	@Override
	public void onKeyReleased(int keyCode) {
		
		switch(keyCode) {
		case GLFW_KEY_ESCAPE:
			windowManager.setWindowShouldClose(true);
			break;
		}
		
	}
	

	
} // END OF CLASS
