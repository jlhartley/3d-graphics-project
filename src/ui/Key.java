package ui;

import static org.lwjgl.glfw.GLFW.*;

import org.eclipse.swt.SWT;

public enum Key {
	W, A, S, D,
	RIGHT, LEFT, UP, DOWN,
	SPACE, CONTROL, SHIFT,
	O, P, I, R, U, ONE, TWO;
	
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
			
		case GLFW_KEY_O:
			return O;
		case GLFW_KEY_P:
			return P;
		case GLFW_KEY_I:
			return I;
		case GLFW_KEY_R:
			return R;
		case GLFW_KEY_U:
			return U;
		case GLFW_KEY_1:
			return ONE;
		case GLFW_KEY_2:
			return TWO;
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
			
		case 'o':
			return O;
		case 'p':
			return P;
		case 'i':
			return I;
		case 'r':
			return R;
		case 'u':
			return U;
		case '1':
			return ONE;
		case '2':
			return TWO;
		}
		return null;
	}
}
