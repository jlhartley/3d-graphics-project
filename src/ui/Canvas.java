package ui;

import static logging.Logger.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.lwjgl.opengl.swt.GLCanvas;
import org.lwjgl.opengl.swt.GLData;

import input.Key;
import input.MouseButton;

import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import math.geometry.Vector2f;

public class Canvas {
	
	public interface Callbacks {
		public void onKeyPressed(Key key);
		public void onKeyReleased(Key key);
		public void onMouseDown(MouseButton mouseButton);
		public void onMouseUp(MouseButton mouseButton);
		public void onCursorPositionChanged(Vector2f cursorPosition);
		public void onFramebufferResized(int width, int height);
	}
	
	private static final int STYLE_BITS = SWT.BORDER | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
	
	private Shell shell;
	
	// Wrapping the SWT GLCanvas
	private GLCanvas glCanvas;
	
	// Width and height of the canvas in pixels
	private int width, height;
	
	// Centre of the canvas, in pixels, relative to the top-left corner
	private Vector2f centre = new Vector2f();
	
	// The mouse position relative to the canvas centre
	private Vector2f cursorPosition = new Vector2f();
	
	private boolean[] keysPressed = new boolean[400];
	
	private boolean pageUpPressed = false;
	private boolean pageDownPressed = false;
	
	private boolean upPressed = false;
	private boolean downPressed = false;
	private boolean rightPressed = false;
	private boolean leftPressed = false;
	
	private boolean controlPressed = false;
	
	private Callbacks callbacks;
	
	
	public Canvas(Shell shell, Callbacks callbacks) {
		this.shell = shell;
		this.callbacks = callbacks;
		init();
	}
	
	public void init() {
		GLData glData = new GLData();
		// Use double-buffering
		glData.doubleBuffer = true;
		// 8x Multisampling for anti-aliasing
		glData.samples = 8;
		// Enable vsync
		glData.swapInterval = 1;
		
		glCanvas = new GLCanvas(shell, STYLE_BITS, glData);
		glCanvas.setLayoutData(UIUtils.getGLCanvasLayoutData());
		
		initListeners();
	}
	
	private void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
		centre.x = (float)width / 2;
		centre.y = (float)height / 2;
	}
	
	
	public void initListeners() {
		
		// Resizing
		
		glCanvas.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int width = glCanvas.getBounds().width;
				int height = glCanvas.getBounds().height;
				updateSize(width, height);
				// Should be using this size to update the framebuffer?
				if (callbacks != null) {
					callbacks.onFramebufferResized(width, height);
				}
			}
		});
		
		
		// Input handling
		
		glCanvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				log("Key Press Event: " + e);
				//char character = Character.toUpperCase(e.character);
				// Modifier keys can interfere with the e.character field, so use keyCode instead
				char character = (char) Character.toUpperCase(e.keyCode);
				keysPressed[character] = true;
				
				Key key = Key.fromSWT(e.keyCode);
				
				if (key == Key.PAGEUP) {
					pageUpPressed = true;
				} else if (key == Key.PAGEDOWN) {
					pageDownPressed = true;
				} else if (key == Key.CONTROL) {
					controlPressed = true;
				} else if (key == Key.RIGHT) {
					rightPressed = true;
				} else if (key == Key.LEFT) {
					leftPressed = true;
				} else if (key == Key.UP) {
					upPressed = true;
				} else if (key == Key.DOWN) {
					downPressed = true;
				}
				
				if (callbacks != null) {
					callbacks.onKeyPressed(key);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				log("Key Release event: " + e);
				//char character = Character.toUpperCase(e.character);
				// When a character is released the e.character field is no longer set strangely,
				// so use e.keyCode instead
				// This is related in some way to the SWT patch
				char character = (char) Character.toUpperCase(e.keyCode);
				keysPressed[character] = false;
				
				Key key = Key.fromSWT(e.keyCode);
				
				if (key == Key.PAGEUP) {
					pageUpPressed = false;
				} else if (key == Key.PAGEDOWN) {
					pageDownPressed = false;
				} else if (key == Key.CONTROL) {
					controlPressed = false;
				} else if (key == Key.RIGHT) {
					rightPressed = false;
				} else if (key == Key.LEFT) {
					leftPressed = false;
				} else if (key == Key.UP) {
					upPressed = false;
				} else if (key == Key.DOWN) {
					downPressed = false;
				}
				
				if (callbacks != null) {
					callbacks.onKeyReleased(key);
				}
			}
		});
		
		glCanvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				callbacks.onMouseUp(MouseButton.fromCode(e.button));
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				callbacks.onMouseDown(MouseButton.fromCode(e.button));
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				
			}
			
		});
		
		glCanvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				cursorPosition.x = (float) (e.x - centre.x);
				cursorPosition.y = -(float) (e.y - centre.y);
				if (callbacks != null) {
					callbacks.onCursorPositionChanged(cursorPosition);
				}
			}
		});
		
	}
	
	
	
	// Wrapper methods
	
	public boolean isDisposed() {
		return glCanvas.isDisposed();
	}
	
	public void setCurrent() {
		glCanvas.setCurrent();
	}
	
	public void swapBuffers() {
		glCanvas.swapBuffers();
	}
	
	
	// Getters
	
	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isKeyPressed(int keyCode) {
		switch (Key.fromGLFW(keyCode)) {
		case PAGEUP:
			return pageUpPressed;
		case PAGEDOWN:
			return pageDownPressed;
		case CONTROL:
			return controlPressed;
		case RIGHT:
			return rightPressed;
		case LEFT:
			return leftPressed;
		case UP:
			return upPressed;
		case DOWN:
			return downPressed;
		default:
			// Printable keys map to capital ASCII keys for GLFW
			return keysPressed[keyCode];
		}
	}

	public Vector2f getCursorPosition() {
		return cursorPosition;
	}
	
	
	
}
