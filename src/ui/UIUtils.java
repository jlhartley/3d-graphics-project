package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;

public class UIUtils {
	
	public static FillLayout getGroupFillLayout() {
		FillLayout layout = new FillLayout();
		layout.type = SWT.VERTICAL;
		layout.marginHeight = 5;
		layout.spacing = 5;
		return layout;
	}
	
	public static Composite getSidePanel(Shell shell) {
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
