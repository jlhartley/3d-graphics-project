package ui;

import static logging.Logger.log;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;

import math.geometry.Vector2f;
import math.geometry.Vector3f;
import render.ProjectionType;

import static ui.UIUtils.*;

public class Window2 implements Window {
	
	public interface UICallbacks {
		public void onProjectionChanged(ProjectionType projectionType);
		public void onCameraControlTypeChanged(boolean relative);
		public void onCameraPositionChanged(Vector3f newPosition);
		public void onCameraRotationChanged(Vector3f newRotation);
		public void onTimeMultiplierChanged(double timeMultiplier);
	}
	
	// Display manages the connection between SWT and the OS
	private final Display display;
	
	// Shell represents the actual window
	private Shell shell;
	
	// Width, height and title
	private int width, height;
	private String title;
	
	// Centre of the window, in pixels, relative to the top-left corner
	private Vector2f centre = new Vector2f();
	
	// Mouse position, relative the the window centre
	//private Vector2f mousePosition = new Vector2f();
	
	// Wraps GLCanvas
	private Canvas canvas;
	
	// Wraps the side panel composite
	private SidePanel sidePanel;
	
	
	public Window2(Display display, int width, int height, String title, UICallbacks uiCallbacks, Canvas.Callbacks canvasCallbacks) {
		this.display = display;
		this.title = title;
		updateSize(width, height);
		initShell();
		initUI(uiCallbacks, canvasCallbacks);
		log("Display Bounds: " + display.getBounds());
		log("Window Bounds: " + shell.getBounds());
		log("Window Size: " + shell.getSize());
		log("Window Client Area: " + shell.getClientArea());
	}
	
	
	private void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
		centre.x = (float)width / 2;
		centre.y = (float)height / 2;
	}
	
	
	private void initShell() {
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}

	
	private void initUI(UICallbacks uiCallbacks, Canvas.Callbacks canvasCallbacks) {
		initMenu(shell);
		
		initRootLayout(shell);
		
		// The order of object creation for canvas and sidePanel
		// is important. It determines which one lies on the right
		// or the left.
		
		canvas = new Canvas(shell, canvasCallbacks);
		GL.createCapabilities();
		
		sidePanel = new SidePanel(shell, uiCallbacks);
	}
	
	
	
	public Canvas getCanvas() {
		return canvas;
	}
	
	public SidePanel getSidePanel() {
		return sidePanel;
	}
	
	
	
	
	
	
	
	public boolean isDisposed() {
		return shell.isDisposed();
	}
	
	public void asyncExec(Runnable runnable) {
		display.asyncExec(runnable);
	}
	
	public boolean isCanvasDisposed() {
		return canvas.isDisposed();
	}
	
	
	public void open() {
		shell.open();
	}
	
	public void loop() {
		// Main event loop. Run while the window is open
		while(!shell.isDisposed()) {
			// Read event from native OS and dispatch to SWT
			if (!display.readAndDispatch()) {
				// If there was no more work to do, sleep
				display.sleep();
			}
		}
	}
	
	
	


	@Override
	public void show() {
		shell.setVisible(true);
	}

	@Override
	public void hide() {
		shell.setVisible(false);
	}


	@Override
	public void centre() {
		Monitor primaryMonitor = display.getPrimaryMonitor();
		
		int screenWidth = primaryMonitor.getBounds().width;
		int screenHeight = primaryMonitor.getBounds().height;
		
		//int screenWidth = display.getBounds().width;
		//int screenHeight = display.getBounds().height;
		
		int x = (screenWidth - this.width) / 2;
		int y = (screenHeight - this.height) / 2;
		
		shell.setLocation(x, y);
	}
	
	@Override
	public boolean shouldClose() {
		return shell.isDisposed();
	}
	
	@Override
	public int getWidth() {
		return width;
	}


	@Override
	public int getHeight() {
		return height;
	}

	@Override
	public boolean isKeyPressed(int keyCode) {
		return canvas.isKeyPressed(keyCode);
	}


	@Override
	public void enableCursor() {
		
	}


	@Override
	public void disableCursor() {
		
	}


	@Override
	public Vector2f getCursorPosition() {
		return canvas.getCursorPosition();
	}
	
	
	
	
	// Cleanup
	public void cleanUp() {
		// Once the shell is disposed, the display should be disposed
		display.dispose();
	}
	
}
