package window;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButton {
	
	LEFT(GLFW_MOUSE_BUTTON_LEFT),
	RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
	MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE);
	
	private int mouseCode;
	
	MouseButton(int mouseCode) {
		this.mouseCode = mouseCode;
	}
	
	public int getMouseCode() {
		return mouseCode;
	}

}
