package render;

import static org.lwjgl.opengl.GL11.*;

import entities.Camera;
import entities.Entity;
import math.Matrix4f;
import model.Model;
import shaders.ShaderProgram;
import util.MathUtils;

public class Renderer {
	
	private ShaderProgram shaderProgram;
	
	private static final float FOV = 70; // Field of view in degrees
	private static final float NEAR_PLANE = 0.1f; // Near plane distance
	private static final float FAR_PLANE = 1000; // Far plane distance
	
	public Renderer(String vertexShaderPath, String fragmentShaderPath) {
		shaderProgram = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
		shaderProgram.useProgram();
		
		glEnable(GL_DEPTH_TEST);
	}
	
	public void setClearColour(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	}
	
	// Should be called whenever the framebuffer changes size
	public void updateFramebufferSize(int width, int height) {
		// Update the viewport
		glViewport(0, 0, width, height);
		// Recalculate the projection matrix
		Matrix4f projectionMatrix = MathUtils.createProjectionMatrix(width, height, FOV, NEAR_PLANE, FAR_PLANE);
		shaderProgram.setUniformValue("projection_matrix", projectionMatrix);
	}
	
	// Associate entities with a given model for batch rendering
	//HashMap<Model, List<Entity>> entityModelMap = new HashMap<>();
	
	private void prepareModel(Model model) {
		model.bindVAO();
	}
	
	public void render(Entity entity, Camera camera) {
		
		Model model = entity.getModel();
		
		prepareModel(model);
		
		setMatrices(entity, camera);
		
		//shaderProgram.setUniformValue("time", time);
		
		//glEnable(GL_LINE_SMOOTH);
		//glHint(GL_LINE_SMOOTH_HINT,  GL_NICEST);
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
