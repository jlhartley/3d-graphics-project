package window;

import static org.lwjgl.glfw.GLFW.*;

public enum MouseButton {
	
	LEFT(GLFW_MOUSE_BUTTON_LEFT),
	RIGHT(GLFW_MOUSE_BUTTON_RIGHT),
	MIDDLE(GLFW_MOUSE_BUTTON_MIDDLE);
	
	// Code corresponding to GLFW representation
	private int mouseCode;
	
	MouseButton(int mouseCode) {
		this.mouseCode = mouseCode;
	}
	
	public int getMouseCode() {
		return mouseCode;
	}
	
	public static MouseButton fromCode(int mouseCode) {
		switch(mouseCode) {
		case 1:
			return LEFT;
		case 2:
			return MIDDLE;
		case 3:
			return RIGHT;
		}
		return null;
	}

}
