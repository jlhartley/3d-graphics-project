package ui;

import static logging.Logger.log;
import static org.lwjgl.opengl.GL11.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Shell;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.libffi.Closure;

import math.geometry.Vector2f;

import static ui.UIUtils.*;

public class UIWindow implements Window {
	
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
	
	private Closure debugMessageCallback;
	
	public UIWindow(Display display, int width, int height, String title, SidePanel.Callbacks sidePanelCallbacks, Canvas.Callbacks canvasCallbacks) {
		this.display = display;
		this.title = title;
		updateSize(width, height);
		initShell();
		initUI(sidePanelCallbacks, canvasCallbacks);
		log("Display Bounds: " + display.getBounds());
		log("Window Bounds: " + shell.getBounds());
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
		
		shell.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int width = shell.getBounds().width;
				int height = shell.getBounds().height;
				updateSize(width, height);
			}
		});
	}

	
	private void initUI(SidePanel.Callbacks sidePanelCallbacks, Canvas.Callbacks canvasCallbacks) {
		initMenu(shell);
		
		// Root layout is a GridLayout with 2 columns
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 2;
		shell.setLayout(rootLayout);
		
		// The order of object creation for canvas and sidePanel
		// is important. It determines which is in the first
		// or the second column (left versus right).
		
		canvas = new Canvas(shell, canvasCallbacks);
		GL.createCapabilities();
		debugMessageCallback = GLUtil.setupDebugMessageCallback();
		// If OpenGL initialisation was successful, display the version
		System.out.println("OpenGL Version: " + glGetString(GL_VERSION));
		
		sidePanel = new SidePanel(shell, sidePanelCallbacks);
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
		if (debugMessageCallback != null) {
			debugMessageCallback.release();
		}
		// Once the shell is disposed, the display should be disposed
		display.dispose();
	}
	
}
