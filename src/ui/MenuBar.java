package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

import math.geometry.Vector3f;

public class MenuBar {
	
	private Shell shell;
	
	// Wrapping this object
	private Menu menu;
	
	private Callbacks callbacks;
	
	public interface Callbacks {
		public void onSave(String path);
		public void onOpen(String path);
		public void onNew();
		public void onAddPlanet(Vector3f velocity, boolean autoVelocity, float mass);
	}
	
	public MenuBar(Shell shell, Callbacks callbacks) {
		this.shell = shell;
		this.callbacks = callbacks;
		menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);
		init();
	}
	
	private void init() {
		
		// File Stuff
		// Top menu
		MenuItem fileMenuItem = new MenuItem(menu, SWT.CASCADE);
		fileMenuItem.setText("File");
		Menu fileMenu = new Menu(fileMenuItem);
		fileMenuItem.setMenu(fileMenu);
		// Menu items
		MenuItem newMenuItem = new MenuItem(fileMenu, SWT.NONE);
		newMenuItem.setText("New");
		newMenuItem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				callbacks.onNew();
			}
			
		});
		MenuItem openMenuItem = new MenuItem(fileMenu, SWT.NONE);
		openMenuItem.setText("Open");
		openMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				fileDialog.setFilterNames(new String[] {"JSON Files (*.json)", "All Files (*.*)"});
				fileDialog.setFilterExtensions(new String[] {"*.json", "*.*"});
				//fileDialog.setFileName("planets.json");
				String path = fileDialog.open();
				if (path != null) {
					callbacks.onOpen(path);
				}
			}
			
		});
		MenuItem saveMenuItem = new MenuItem(fileMenu, SWT.NONE);
		saveMenuItem.setText("Save");
		saveMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shell, SWT.SAVE);
				fileDialog.setFilterNames(new String[] {"JSON Files (*.json)", "All Files (*.*)"});
				fileDialog.setFilterExtensions(new String[] {"*.json", "*.*"});
				//fileDialog.setFileName("planets.json");
				fileDialog.setOverwrite(true); // Check for overwrite
				String path = fileDialog.open();
				if (path != null) {
					callbacks.onSave(path);
				}
			}
			
		});
		new MenuItem(fileMenu, SWT.SEPARATOR); // Separator
		MenuItem exitMenuItem = new MenuItem(fileMenu, SWT.NONE);
		exitMenuItem.setText("Exit");
		exitMenuItem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_QUESTION | SWT.YES | SWT.NO);
				messageBox.setText("Confirm Exit");
				messageBox.setMessage("Are you sure you want to exit?");
				int response = messageBox.open();
				if (response == SWT.YES) {
					shell.close();
				}
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
		addPlanetMenuItem.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				AddPlanetDialog dialog = new AddPlanetDialog(shell);
				dialog.open();
				callbacks.onAddPlanet(dialog.getVelocity(), dialog.getAutoVelocity(), dialog.getMass());
			}
			
		});
		
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
		aboutMenuItem.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				// Replace with MessageDialog.openInformation?
				MessageBox messageBox = new MessageBox(shell);
				messageBox.setText("About Orbitator");
				messageBox.setMessage("Orbitator was developed for simulating orbital motion. It is an illustration of the n-body problem.\n© James Hartley");
				messageBox.open();
			}
			
		});
		
	}

}
