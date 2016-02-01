package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class UIUtils {
	
	public static void initMenu(Shell shell) {
		
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

	public static FillLayout getGroupFillLayout() {
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		layout.marginHeight = 5;
		layout.spacing = 5;
		return layout;
	}
	
	public static GridData getFillHorizontalGridData() {
		return new GridData(SWT.FILL, SWT.CENTER, true, false);
	}
	
	public static GridData getGLCanvasLayoutData() {
		GridData layoutData = new GridData();
		layoutData.horizontalAlignment = SWT.FILL;
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.grabExcessHorizontalSpace = true;
		layoutData.grabExcessVerticalSpace = true;
		return layoutData;
	}

}
