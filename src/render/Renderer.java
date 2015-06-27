package render;

import static org.lwjgl.opengl.GL11.*;
import entities.Camera;
import entities.Entity3D;
import math.Matrix4f;
import shaders.ShaderProgram;
import util.MathUtils;

public class Renderer {
	
	private ShaderProgram shaderProgram;
	
	// Need to update in case aspect ratio changes, so it can be set correctly in the projection matrix
	private int framebufferWidth;
	private int framebufferHeight;
	
	private static final float FOV = 70; // Field of view in degrees
	private static final float NEAR_PLANE = 0.1f; // Near plane distance
	private static final float FAR_PLANE = 1000; // Far plane distance
	
	public Renderer(String vertexShaderPath, String fragmentShaderPath) {
		
		shaderProgram = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
		shaderProgram.useProgram();
		
		glEnable(GL_DEPTH_TEST);
		
	}
	
	public void setBackgroundColour(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	public void clear() {
		glClear(GL_COLOR_BUFFER_BIT|GL_DEPTH_BUFFER_BIT);
	}
	
	// Should be called every frame in case the window is resized.
	public void setFramebufferDimensions(int width, int height) {
		framebufferWidth = width;
		framebufferHeight = height;
	}
	
	
	public void render(Entity3D entity, Camera camera, float time) {
		
		//VAOModel model = entity.getModel();
		//model.bindVAO();
		
		
		Matrix4f modelMatrix = entity.getModelMatrix();
		shaderProgram.setUniformValue("model_matrix", modelMatrix);
		
		Matrix4f projectionMatrix = MathUtils.createProjectionMatrix(framebufferWidth, framebufferHeight, FOV, NEAR_PLANE, FAR_PLANE);
		shaderProgram.setUniformValue("projection_matrix", projectionMatrix);
		
		Matrix4f viewMatrix = camera.getViewMatrix();
		shaderProgram.setUniformValue("view_matrix", viewMatrix);
		
		//shaderProgram.setUniformValue("overallColour", entity.getColour());
		shaderProgram.setUniformValue("time", time);
		
		//glEnable(GL_LINE_SMOOTH);
		//glHint(GL_LINE_SMOOTH_HINT,  GL_NICEST);
		//glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		
		glDrawElements(GL_TRIANGLES, entity.getModel().getVertexCount(), GL_UNSIGNED_INT, 0);
		
		//model.unbindVAO();
		
	}
	
	public void cleanUp() {
		shaderProgram.cleanUp();
	}

}
