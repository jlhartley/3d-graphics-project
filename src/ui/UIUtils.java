package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;

public class UIUtils {

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
