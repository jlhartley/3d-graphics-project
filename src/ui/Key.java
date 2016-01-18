package ui;

import static org.lwjgl.glfw.GLFW.*;

import org.eclipse.swt.SWT;

public enum Key {
	W, A, S, D,
	RIGHT, LEFT, UP, DOWN,
	SPACE, CONTROL, SHIFT;
	
	public static Key fromGLFW(int value) {
		switch(value) {
		case GLFW_KEY_W:
			return W;
		case GLFW_KEY_A:
			return A;
		case GLFW_KEY_S:
			return S;
		case GLFW_KEY_D:
			return D;
		case GLFW_KEY_RIGHT:
			return RIGHT;
		case GLFW_KEY_LEFT:
			return LEFT;
		case GLFW_KEY_UP:
			return UP;
		case GLFW_KEY_DOWN:
			return DOWN;
		case GLFW_KEY_SPACE:
			return SPACE;
		case GLFW_KEY_LEFT_CONTROL:
			return CONTROL;
		case GLFW_KEY_LEFT_SHIFT:
			return SHIFT;
		}
		return null;
	}
	
	public static Key fromSWT(int value) {
		switch(value) {
		case 'w':
			return W;
		case 'a':
			return A;
		case 's':
			return S;
		case 'd':
			return D;
		case SWT.ARROW_RIGHT:
			return RIGHT;
		case SWT.ARROW_LEFT:
			return LEFT;
		case SWT.ARROW_UP:
			return UP;
		case SWT.ARROW_DOWN:
			return DOWN;
		case SWT.SPACE:
			return SPACE;
		case SWT.CONTROL:
			return CONTROL;
		case SWT.SHIFT:
			return SHIFT;
		}
		return null;
	}
}
