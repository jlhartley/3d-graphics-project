package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.lwjgl.opengl.swt.GLCanvas;
import org.lwjgl.opengl.swt.GLData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import math.geometry.Vector2f;
import window.MouseButton;

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
		glCanvas.setCurrent();
		
		initListeners();
	}
	
	private void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
		centre.x = (float)width / 2;
		centre.y = (float)height / 2;
	}
	
	
	public void initListeners() {
		
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
		
		glCanvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				char character = Character.toUpperCase(e.character);
				keysPressed[character] = true;
				
				Key key = Key.fromSWT(e.keyCode);
				System.out.println("Key Pressed: " + key);
				
				if (key == Key.PAGEUP) {
					pageUpPressed = true;
				} else if (key == Key.PAGEDOWN) {
					pageDownPressed = true;
				}
				
				if (callbacks != null) {
					callbacks.onKeyPressed(key);
				}
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				char character = Character.toUpperCase(e.character);
				keysPressed[character] = false;;
				
				Key key = Key.fromSWT(e.keyCode);
				System.out.println("Key Released: " + key);
				
				if (key == Key.PAGEUP) {
					pageUpPressed = false;
				} else if (key == Key.PAGEDOWN) {
					pageDownPressed = false;
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
		if (Key.fromGLFW(keyCode) == Key.PAGEUP) {
			return pageUpPressed;
		}
		if (Key.fromGLFW(keyCode) == Key.PAGEDOWN) {
			return pageDownPressed;
		}
		return keysPressed[keyCode];
	}

	public Vector2f getCursorPosition() {
		return cursorPosition;
	}
	
	
	
}
