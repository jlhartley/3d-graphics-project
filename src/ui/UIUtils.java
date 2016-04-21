package ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;

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
	
	
	// There can only be one error dialog displayed, so protect statically
	private static boolean errorDialogDisplayed = false;
	
	public static void displayErrorDialogue(final Shell shell, final Exception e) {
		
		if (errorDialogDisplayed) {
			return;
		}
		
		errorDialogDisplayed = true;
		
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
		messageBox.setText("Invalid Input");
		messageBox.setMessage(e.getMessage());
		
		messageBox.open();
		
		errorDialogDisplayed = false;
		
		/*
		// Need to asyncExec because otherwise there may be duplicate dialogs caused
		// by focus changes
		shell.getDisplay().asyncExec(new Runnable() {
			
			@Override
			public void run() {
				MessageBox messageBox = new MessageBox(shell, SWT.ICON_WARNING);
				messageBox.setText("Invalid Input");
				messageBox.setMessage(e.getMessage());
				
				messageBox.open();
			}
		});
		*/
	}

}
