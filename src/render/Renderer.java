package render;

import static org.lwjgl.opengl.GL11.*;

import camera.Camera;
import entities.Entity;
import math.geometry.Matrix4f;
import math.geometry.MatrixUtils;
import model.Model;
import shaders.ShaderProgram;

public class Renderer {
	
	// Currently supporting just one shader program
	// TODO: Support multiple shader programs
	private ShaderProgram shaderProgram;
	
	private static final float FOV = 70; // Field of view in degrees
	private static final float NEAR_PLANE = 0.01f; // Near plane distance
	private static final float FAR_PLANE = 10000; // Far plane distance
	
	public Renderer(String vertexShaderPath, String fragmentShaderPath) {
		
		shaderProgram = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
		shaderProgram.useProgram();
		
		glEnable(GL_DEPTH_TEST);
		
		// Implies glFrontFace(GL_CCW)
		// This is because by default front faces have
		// a counter-clockwise winding order
		
		glEnable(GL_CULL_FACE);
		
		// Cull back faces
		// This is so the fragment shader won't need to
		// run for back faces, providing a performance gain.
		// Obscured pixels would normally be discarded by more
		// expensive depth-testing
		glCullFace(GL_BACK);
	}
	
	public void setClearColour(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	}
	
	// Should be called whenever the framebuffer changes size
	public void onFramebufferResized(int width, int height, ProjectionType type) {
		// Update the viewport
		glViewport(0, 0, width, height);
		// Recalculate the projection matrix
		updateProjection(width, height, type);
	}
	
	public void updateProjection(int width, int height, ProjectionType projectionType) {
		Matrix4f projectionMatrix;
		if (projectionType == ProjectionType.ORTHOGRAPHIC) {
			projectionMatrix = MatrixUtils.orthographicProjectionMatrix(width, height, NEAR_PLANE, FAR_PLANE);
		} else {
			projectionMatrix = MatrixUtils.perspectiveProjectionMatrix(width, height, FOV, NEAR_PLANE, FAR_PLANE);
		}
		shaderProgram.setUniformValue("projection_matrix", projectionMatrix);
	}
	
	// Associate entities with a given model for batch rendering
	//HashMap<Model, List<Entity>> entityModelMap = new HashMap<>();
	
	public void render(Entity entity, Camera camera) {
		
		Model model = entity.getModel();
		
		model.bindVAO();
		
		setMatrices(entity, camera);
		
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		glDrawElements(GL_TRIANGLES, model.getVertexCount(), GL_UNSIGNED_INT, 0);
		
		//model.unbindVAO();
		
	}
	
	public void setMatrices(Entity entity, Camera camera) {
		
		Matrix4f modelMatrix = entity.getModelMatrix();
		shaderProgram.setUniformValue("model_matrix", modelMatrix);
		
		Matrix4f viewMatrix = camera.getViewMatrix();
		shaderProgram.setUniformValue("view_matrix", viewMatrix);
		
	}
	
	public void cleanUp() {
		shaderProgram.cleanUp();
	}

}
