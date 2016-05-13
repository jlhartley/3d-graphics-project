package render;

import static org.lwjgl.opengl.GL11.*;

import camera.Camera;
import entities.Entity;
import lighting.LightSource;
import math.geometry.Matrix4f;
import math.geometry.MatrixUtils;
import math.geometry.Vector3f;
import model.Model;
import shaders.ShaderProgram;

public class Renderer {
	
	// Currently supporting just one shader program
	private ShaderProgram shaderProgram;
	
	private int framebufferWidth;
	private int framebufferHeight;
	
	private Matrix4f projectionMatrix;
	private ProjectionType projectionType;
	
	private static final float FOV = 70; // Field Of View (in degrees)
	private static final float NEAR_PLANE = 0.01f; // Near Plane Distance
	private static final float FAR_PLANE = 10000; // Far Plane Distance
	
	private int clearBufferBits = GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT;
	
	public Renderer(String vertexShaderPath, String fragmentShaderPath, int framebufferWidth, int framebufferHeight) {
		
		shaderProgram = new ShaderProgram(vertexShaderPath, fragmentShaderPath);
		shaderProgram.use();
		
		this.framebufferWidth = framebufferWidth;
		this.framebufferHeight = framebufferHeight;
		
		// Default projection matrix is perspective
		this.projectionType = ProjectionType.PERSPECTIVE;
		
		updateProjection();
		
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
	
	
	// Buffer Clearing Methods
	
	public void setClearColour(Vector3f colour) {
		setClearColour(colour.x, colour.y, colour.z);
	}
	
	public void setClearColour(float r, float g, float b) {
		glClearColor(r, g, b, 1);
	}
	
	
	public void clearColourBuffer() {
		glClear(GL_COLOR_BUFFER_BIT);
	}
	
	public void clearDepthBuffer() {
		glClear(GL_DEPTH_BUFFER_BIT);
	}
	
	public void setClearBufferBits(int bits) {
		this.clearBufferBits = bits;
	}
	
	public void clear() {
		glClear(clearBufferBits);
	}
	
	
	public void clear(int bits) {
		glClear(bits);
	}
	
	
	// Depth Test Methods
	
	public void setDepthTestEnabled(boolean enabled) {
		if (enabled) {
			enableDepthTest();
		} else {
			disableDepthTest();
		}
	}
	
	public void enableDepthTest() {
		glEnable(GL_DEPTH_TEST);
	}
	
	public void disableDepthTest() {
		glDisable(GL_DEPTH_TEST);
	}
	
	
	// Face Culling Methods
	
	public void setFaceCullingEnabled(boolean enabled) {
		if (enabled) {
			enableFaceCulling();
		} else {
			disableFaceCulling();
		}
	}
	
	public void enableFaceCulling() {
		glEnable(GL_CULL_FACE);
	}
	
	public void disableFaceCulling() {
		glDisable(GL_CULL_FACE);
	}
	
	
	public void setPolygonMode(PolygonMode polygonMode) {
		switch (polygonMode) {
		case FILL:
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			break;
		case LINE:
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			break;
		}
	}
	
	public void setProjectionType(ProjectionType projectionType) {
		this.projectionType = projectionType;
		updateProjection();
	}
	
	// Should be called whenever the framebuffer changes size
	public void onFramebufferResized(int width, int height) {
		this.framebufferWidth = width;
		this.framebufferHeight = height;
		// Update the viewport
		glViewport(0, 0, width, height);
		// Recalculate the projection matrix
		updateProjection();
	}
	
	public void updateProjection() {
		switch (projectionType) {
		case ORTHOGRAPHIC:
			projectionMatrix = MatrixUtils.orthographicProjection(framebufferWidth, framebufferHeight, NEAR_PLANE, FAR_PLANE);
			break;
		case PERSPECTIVE:
			projectionMatrix = MatrixUtils.perspectiveProjection(framebufferWidth, framebufferHeight, NEAR_PLANE, FAR_PLANE, FOV);
			break;
		}
		shaderProgram.setUniformValue("projection_matrix", projectionMatrix);
	}
	
	public void updateProjection(int width, int height, ProjectionType projectionType) {
		switch (projectionType) {
		case ORTHOGRAPHIC:
			projectionMatrix = MatrixUtils.orthographicProjection(width, height, NEAR_PLANE, FAR_PLANE);
			break;
		case PERSPECTIVE:
			projectionMatrix = MatrixUtils.perspectiveProjection(width, height, NEAR_PLANE, FAR_PLANE, FOV);
			break;
		}
		shaderProgram.setUniformValue("projection_matrix", projectionMatrix);
	}
	
	// Associate entities with a given model for batch rendering
	//HashMap<Model, List<Entity>> entityModelMap = new HashMap<>();
	
	public void render(Entity entity, Camera camera) {
		
		Model model = entity.getModel();
		
		model.bind();
		
		setMatrices(entity, camera);
		
		int vertexCount = model.getVertexCount();
		
		glDrawElements(GL_TRIANGLES, vertexCount, GL_UNSIGNED_INT, 0);
		
		//Model.unbindAll();
	}
	
	
	// Uniform setting methods
	
	public void enableLighting() {
		shaderProgram.setUniformValue("lighting_enabled", true);
	}
	
	public void disableLighting() {
		shaderProgram.setUniformValue("lighting_enabled", false);
	}
	
	public void setLightSource(LightSource lightSource) {
		Vector3f lightPosition = lightSource.getLightPosition();
		shaderProgram.setUniformValue("light_position", lightPosition);
		Vector3f lightColour = lightSource.getLightColour();
		shaderProgram.setUniformValue("light_colour", lightColour);
	}
	
	private void setMatrices(Entity entity, Camera camera) {
		Matrix4f modelMatrix = entity.getModelMatrix();
		shaderProgram.setUniformValue("model_matrix", modelMatrix);
		Matrix4f viewMatrix = camera.getViewMatrix();
		shaderProgram.setUniformValue("view_matrix", viewMatrix);
	}
	
	// Projection Matrix may be needed for ray casting
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
	}
	
	public void cleanUp() {
		shaderProgram.cleanUp();
	}

}
