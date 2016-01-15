package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.opengl.GLCanvas;
import org.eclipse.swt.opengl.GLData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Monitor;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.lwjgl.opengl.GL;

import math.geometry.Vector2f;
import window.InputCallbacks;
import window.MouseButton;
import window.WindowCallbacks;

public class Window2 {
	
	// Display manages the connection between SWT and the OS
	private final Display display;
	
	// Shell represents the actual window
	private Shell shell;
	
	private int width, height;
	private String title;
	
	// Centre of the window, in pixels, relative to the top-left corner
	private Vector2f centre = new Vector2f();
	
	// Mouse position, relative the the window centre
	private Vector2f mousePosition = new Vector2f();
	
	private GLCanvas canvas;
	private int canvasWidth, canvasHeight;
	
	private Vector2f canvasCentre = new Vector2f();
	
	
	private WindowCallbacks windowCallbacks;
	private InputCallbacks inputCallbacks;
	
	
	private void updateSize(int width, int height) {
		this.width = width;
		this.height = height;
		centre.x = (float)width / 2;
		centre.y = (float)height / 2;
	}
	
	private void updateCanvasSize(int canvasWidth, int canvasHeight) {
		this.canvasWidth = canvasWidth;
		this.canvasHeight = canvasHeight;
		canvasCentre.x = (float)canvasWidth / 2;
		canvasCentre.y = (float)canvasHeight / 2;
	}
	
	
	private void init() {
		shell = new Shell(display);
		shell.setSize(width, height);
		shell.setText(title);
	}
	
	
	private void initMenu() {
		
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		
		
		// File Stuff
		// Top menu
		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("File");
		Menu fileMenu = new Menu(fileMenuItem);
		fileMenuItem.setMenu(fileMenu);
		// Menu items
		MenuItem newMenuItem = new MenuItem(fileMenu, SWT.NONE);
		newMenuItem.setText("New");
		MenuItem openMenuItem = new MenuItem(fileMenu, SWT.NONE);
		openMenuItem.setText("Open");
		MenuItem saveMenuItem = new MenuItem(fileMenu, SWT.NONE);
		saveMenuItem.setText("Save");
		new MenuItem(fileMenu, SWT.SEPARATOR); // Separator
		MenuItem closeMenuItem = new MenuItem(fileMenu, SWT.NONE);
		closeMenuItem.setText("Close");
		closeMenuItem.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				System.out.println("Selected");
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				System.out.println("Default Selected");
			}
		});
		
		// Edit Stuff
		// Top menu
		MenuItem editMenuItem = new MenuItem(menu, SWT.CASCADE);
		editMenuItem.setText("Edit");
		Menu editMenu = new Menu(editMenuItem);
		editMenuItem.setMenu(editMenu);
		// Menu items
		MenuItem addPlanetMenuItem = new MenuItem(editMenu, SWT.NONE);
		addPlanetMenuItem.setText("Add Planet");
		new MenuItem(editMenu, SWT.SEPARATOR); // Separator
		MenuItem undoMenuItem = new MenuItem(editMenu, SWT.NONE);
		undoMenuItem.setText("Undo");
		MenuItem redoMenuItem = new MenuItem(editMenu, SWT.NONE);
		redoMenuItem.setText("Redo");
		
		
		// Help Stuff
		// Top menu
		MenuItem helpMenuItem = new MenuItem(menu, SWT.CASCADE);
		helpMenuItem.setText("Help");
		Menu helpMenu = new Menu(helpMenuItem);
		helpMenuItem.setMenu(helpMenu);
		// Menu items
		MenuItem viewHelpMenuItem = new MenuItem(helpMenu, SWT.NONE);
		viewHelpMenuItem.setText("View Help");
		MenuItem aboutMenuItem = new MenuItem(helpMenu, SWT.NONE);
		aboutMenuItem.setText("About");
		
	}
	
	private void initUI() {
		
		initMenu();
		
		// Root layout is a GridLayout with 2 columns
		GridLayout rootLayout = new GridLayout();
		rootLayout.numColumns = 2;
		shell.setLayout(rootLayout);
		
		initGLCanvas();
		
		//Text text = new Text(shell, SWT.BORDER | SWT.WRAP | SWT.MULTI);
		//text.setLayoutData(getGLCanvasLayoutData());
		
		
		Composite sidePanel = getSidePanel();
		
		// Projection Stuff
		
		Group projectionTypeGroup = new Group(sidePanel, SWT.NONE);
		projectionTypeGroup.setText("Projection Type");
		projectionTypeGroup.setLayoutData(getFillHorizontalGridData());
		projectionTypeGroup.setLayout(getGroupFillLayout());
		
		Button perspectiveButton = new Button(projectionTypeGroup, SWT.RADIO);
		perspectiveButton.setText("Perspective");
		perspectiveButton.setSelection(true);
		Button orthographicButton = new Button(projectionTypeGroup, SWT.RADIO);
		orthographicButton.setText("Orthographic");
		
		
		// Camera Stuff
		
		Group cameraGroup = new Group(sidePanel, SWT.NONE);
		cameraGroup.setText("Camera");
		cameraGroup.setLayoutData(getFillHorizontalGridData());
		cameraGroup.setLayout(new GridLayout());
		
		// Control type
		
		Group cameraControlTypeGroup = new Group(cameraGroup, SWT.NONE);
		cameraControlTypeGroup.setText("Control Type");
		cameraControlTypeGroup.setLayoutData(getFillHorizontalGridData());
		cameraControlTypeGroup.setLayout(getGroupFillLayout());
		
		Button relativeButton = new Button(cameraControlTypeGroup, SWT.RADIO);
		relativeButton.setText("Relative");
		relativeButton.setSelection(true);
		Button absoluteButton = new Button(cameraControlTypeGroup, SWT.RADIO);
		absoluteButton.setText("Absolute");
		
		// Positioning
		
		Group cameraPositioningGroup = new Group(cameraGroup, SWT.NONE);
		cameraPositioningGroup.setText("Positioning");
		cameraPositioningGroup.setLayoutData(getFillHorizontalGridData());
		cameraPositioningGroup.setLayout(new GridLayout(2, false));
		
		Label xLabel = new Label(cameraPositioningGroup, SWT.NONE);
		xLabel.setText("X: ");
		Text xText = new Text(cameraPositioningGroup, SWT.BORDER);
		xText.setLayoutData(getFillHorizontalGridData());
		
		Label yLabel = new Label(cameraPositioningGroup, SWT.NONE);
		yLabel.setText("Y: ");
		Text yText = new Text(cameraPositioningGroup, SWT.BORDER);
		yText.setLayoutData(getFillHorizontalGridData());
		
		Label zLabel = new Label(cameraPositioningGroup, SWT.NONE);
		zLabel.setText("Z: ");
		Text zText = new Text(cameraPositioningGroup, SWT.BORDER);
		zText.setLayoutData(getFillHorizontalGridData());
		
		// Separator
		GridData separatorData = new GridData();
		separatorData.horizontalSpan = 2;
		separatorData.horizontalAlignment = SWT.FILL;
		new Label(cameraPositioningGroup, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorData);
		
		Label pitchLabel = new Label(cameraPositioningGroup, SWT.NONE);
		pitchLabel.setText("Pitch: ");
		Text pitchText = new Text(cameraPositioningGroup, SWT.BORDER);
		pitchText.setLayoutData(getFillHorizontalGridData());
		
		Label yawLabel = new Label(cameraPositioningGroup, SWT.NONE);
		yawLabel.setText("Yaw: ");
		Text yawText = new Text(cameraPositioningGroup, SWT.BORDER);
		yawText.setLayoutData(getFillHorizontalGridData());
		
		Label rollLabel = new Label(cameraPositioningGroup, SWT.NONE);
		rollLabel.setText("Roll: ");
		Text rollText = new Text(cameraPositioningGroup, SWT.BORDER);
		rollText.setLayoutData(getFillHorizontalGridData());
		
		
		// Simulation Stuff
		
		Group simulationGroup = new Group(sidePanel, SWT.NONE);
		simulationGroup.setText("Simulation");
		simulationGroup.setLayoutData(getFillHorizontalGridData());
		simulationGroup.setLayout(new GridLayout(2, false));
		
		Label speedLabel = new Label(simulationGroup, SWT.NONE);
		speedLabel.setText("Speed: ");
		
		Scale speedScale = new Scale(simulationGroup, SWT.NONE);
		speedScale.setPageIncrement(1);
		speedScale.setIncrement(1);
		speedScale.setMaximum(8);
		speedScale.setSelection(4); // Centre the slider at the middle value
		speedScale.setLayoutData(getFillHorizontalGridData());
		
		
		Group simulationModeGroup = new Group(simulationGroup, SWT.NONE);
		simulationModeGroup.setText("Mode");
		GridData simulationModeGroupData = getFillHorizontalGridData();
		// Need the span = 2 because there are 2 columns
		simulationModeGroupData.horizontalSpan = 2;
		simulationModeGroup.setLayoutData(simulationModeGroupData);
		simulationModeGroup.setLayout(getGroupFillLayout());
		
		Button runButton = new Button(simulationModeGroup, SWT.RADIO);
		runButton.setText("Run");
		runButton.setSelection(true);
		Button editButton = new Button(simulationModeGroup, SWT.RADIO);
		editButton.setText("Edit");
		
		/*
		Button pausedButton = new Button(simulationGroup, SWT.CHECK);
		pausedButton.setText("Paused");
		GridData pausedButtonData = new GridData();
		pausedButtonData.horizontalSpan = 2;
		pausedButtonData.horizontalAlignment = SWT.CENTER;
		pausedButton.setLayoutData(pausedButtonData);
		*/
	}
	
	private FillLayout getGroupFillLayout() {
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		layout.marginHeight = 5;
		layout.spacing = 5;
		return layout;
	}
	
	private Composite getSidePanel() {
		Composite sidePanel = new Composite(shell, SWT.BORDER);
		
		GridData layoutData = new GridData();
		// Should really not specify the width as a pixel value
		layoutData.widthHint = 250;
		// Fill any available space
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.horizontalAlignment = SWT.FILL;
		sidePanel.setLayoutData(layoutData);
		
		// Would ideally like to achieve the same effect 
		// with a simpler layout, but this doesn't seem possible.
		GridLayout sidePanelLayout = new GridLayout();
		sidePanel.setLayout(sidePanelLayout);
		
		return sidePanel;
	}
	
	private GridData getFillHorizontalGridData() {
		return new GridData(SWT.FILL, SWT.CENTER, true, false);
	}
	
	private GridData getGLCanvasLayoutData() {
		GridData layoutData = new GridData();
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		return layoutData;
	}
	
	private void initGLCanvas() {
		GLData glData = new GLData();
		glData.doubleBuffer = true;
		//glData.samples = 16;
		
		int styleBits = SWT.BORDER | SWT.NO_BACKGROUND | SWT.NO_REDRAW_RESIZE;
		
		canvas = new GLCanvas(shell, styleBits, glData);
		canvas.setLayoutData(getGLCanvasLayoutData());
		
		canvas.addKeyListener(new KeyListener() {
			
			@Override
			public void keyPressed(KeyEvent e) {
				System.out.println(e);
				inputCallbacks.onKeyPressed(e.keyCode);
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				System.out.println(e);
				inputCallbacks.onKeyReleased(e.keyCode);
			}
		});
		
		canvas.addListener(SWT.Resize, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				int canvasWidth = canvas.getBounds().width;
				int canvasHeight = canvas.getBounds().height;
				updateCanvasSize(canvasWidth, canvasHeight);
				// Should be using this size to update the framebuffer?
				windowCallbacks.onFramebufferResized(canvasWidth, canvasHeight);
			}
		});
		
		canvas.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseUp(MouseEvent e) {
				if (e.button == 1) {
					inputCallbacks.onMouseUp(MouseButton.LEFT);
				} else if (e.button == 2) {
					inputCallbacks.onMouseUp(MouseButton.MIDDLE);
				} else if (e.button == 3) {
					inputCallbacks.onMouseUp(MouseButton.RIGHT);
				}
			}
			
			@Override
			public void mouseDown(MouseEvent e) {
				if (e.button == 1) {
					inputCallbacks.onMouseDown(MouseButton.LEFT);
				} else if (e.button == 2) {
					inputCallbacks.onMouseDown(MouseButton.MIDDLE);
				} else if (e.button == 3) {
					inputCallbacks.onMouseDown(MouseButton.RIGHT);
				}
			}
			
			@Override
			public void mouseDoubleClick(MouseEvent e) {
			}
			
		});
		
		canvas.addMouseMoveListener(new MouseMoveListener() {
			
			@Override
			public void mouseMove(MouseEvent e) {
				mousePosition.x = (float) (e.x - canvasCentre.x);
				mousePosition.y = -(float) (e.y - canvasCentre.y);
			}
		});
		
		canvas.setCurrent();
		
		GL.createCapabilities();
	}
	
	
	public Window2(Display display, int width, int height, String title) {
		updateSize(width, height);
		this.title = title;
		this.display = display;
		init();
		initUI();
		//initGL();
	}
	
	public void setWindowCallbacks(WindowCallbacks windowCallbacks) {
		this.windowCallbacks = windowCallbacks;
	}
	
	public void setInputCallbacks(InputCallbacks inputCallbacks) {
		this.inputCallbacks = inputCallbacks;
	}
	
	
	public void show() {
		shell.setVisible(true);
	}
	
	public void hide() {
		shell.setVisible(false);
	}
	
	
	public void centre() {
		
		Monitor primaryMonitor = display.getPrimaryMonitor();
		
		System.out.println("Primary Monitor Bounds: " + primaryMonitor.getBounds());
		System.out.println();
		
		int screenWidth = primaryMonitor.getBounds().width;
		int screenHeight = primaryMonitor.getBounds().height;
		
		//int screenWidth = display.getBounds().width;
		//int screenHeight = display.getBounds().height;
		
		int x = (screenWidth - this.width) / 2;
		int y = (screenHeight - this.height) / 2;
		
		shell.setLocation(x, y);
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
	
	public GLCanvas getCanvas() {
		return canvas;
	}
	
	public void run() {
		
		shell.open();
		
		System.out.println("Window Bounds: " + shell.getBounds());
		System.out.println("Window Size: " + shell.getSize());
		System.out.println("Window Client Area: " + shell.getClientArea());
		System.out.println();
		
		/*
		// Calculate trim size
		Rectangle size = shell.computeTrim(0, 0, 1280, 720);
		int trimWidth = size.width - 1280 - size.x;
		int trimHeight = size.height - 720 - size.y;
		
		System.out.println("Computed Trim: Width: " + trimWidth + ", Height: " + trimHeight);
		*/
		
		int trimWidth = shell.getSize().x - shell.getClientArea().width;
		int trimHeight = shell.getSize().y - shell.getClientArea().height;
		
		System.out.println("Trim: Width: " + trimWidth + ", Height: " + trimHeight);
		System.out.println();
		
		
		
		System.out.println("Display Bounds: " + shell.getDisplay().getBounds());
		System.out.println();
		
		//System.out.println("Canvas bounds: " + canvas.getBounds());
		
		// Main event loop. Run while the window is open
		while(!shell.isDisposed()) {
			// Read event from native OS and dispatch to SWT
			if (!display.readAndDispatch()) {
				// If there was no more work to do, sleep
				display.sleep();
			}
		}
		
		display.dispose();
		
	}
	
	public boolean isKeyPressed(int key) {
		return false;
	}
	
	public void enableCursor() {
		
	}
	
	public void disableCursor() {
		
	}
	
	// Basic getters and setters
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
	public Vector2f getMousePosition() { return mousePosition; }
	
	// Cleanup
	public void cleanUp() {
		display.dispose();
	}
	
	
	public static void main(String[] args) {
		
		Display display = new Display();
		
		Window2 window = new Window2(display, 1280, 720, "Title");
		window.centre();
		window.run();
		
		//window.cleanUp();
		
	}
	
}
