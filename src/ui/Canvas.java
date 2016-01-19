package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import math.geometry.Vector2f;
import window.InputCallbacks;

public class Canvas {
	
	private static final int STYLE_BITS = SWT.BORDER | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
	
	
	private GLCanvas glCanvas;
	
	private int width, height;
	
	private Vector2f centre = new Vector2f();
	
	private Vector2f mousePosition = new Vector2f();
	
	private InputCallbacks inputCallbacks;
	
	public Canvas(Shell shell) {
		
		GLData glData = new GLData();
		glData.doubleBuffer = true;
		// TODO: Find a way to enable multisampling
		
		glCanvas = new GLCanvas(shell, STYLE_BITS, glData);
		
		glCanvas.setLayoutData(UIUtils.getGLCanvasLayoutData());
		
		
		initListeners();
		
		//GL.createCapabilities();
	}
	
	public void setCurrent() {
		glCanvas.setCurrent();
	}
	
	public void swapBuffers() {
		glCanvas.swapBuffers();
	}
	
	public void setSize(int width, int height) {
		this.width = width;
		this.height = height;
		centre.x = (float)width / 2;
		centre.y = (float)height / 2;
	}
	
	public boolean isDisposed() {
		return glCanvas.isDisposed();
	}
	
	
	public void initListeners() {
		
		glCanvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e);
				System.out.println("Key Pressed: " + Key.fromSWT(e.keyCode));
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e);
				System.out.println("Key Pressed: " + Key.fromSWT(e.keyCode));
			}
		});
		
		glCanvas.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int width = glCanvas.getBounds().width;
				int height = glCanvas.getBounds().height;
				setSize(width, height);
				// Should be using this size to update the framebuffer?
			}
		});
		
		glCanvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					//inputCallbacks.onMouseUp(MouseButton.LEFT);
				} else if (e.button == 2) {
					//inputCallbacks.onMouseUp(MouseButton.MIDDLE);
				} else if (e.button == 3) {
					//inputCallbacks.onMouseUp(MouseButton.RIGHT);
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					//inputCallbacks.onMouseDown(MouseButton.LEFT);
				} else if (e.button == 2) {
					//inputCallbacks.onMouseDown(MouseButton.MIDDLE);
				} else if (e.button == 3) {
					//inputCallbacks.onMouseDown(MouseButton.RIGHT);
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
		});
		
		glCanvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				mousePosition.x = (float) (e.x - centre.x);
				mousePosition.y = -(float) (e.y - centre.y);
			}
		});
		
	}
	
	
	public int getWidth() { return width; }
	public int getHeight() { return height; }
	
	
	
}
