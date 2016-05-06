package ui;

import static ui.UIUtils.displayErrorDialogue;
import static ui.UIUtils.getFillHorizontalGridData;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import math.geometry.Vector3f;

public class AddPlanetDialog extends TitleAreaDialog {
	
	private Text xText;
	private Text yText;
	private Text zText;
	
	private Text massText;
	
	
	private Vector3f velocity = new Vector3f();
	private boolean autoVelocity;
	private float mass;
	
	
	public AddPlanetDialog(Shell parent) {
		super(parent);
	}

	
	@Override
	public void create() {
		super.create();
		setTitle("Add Planet");
		setMessage("Enter the planet details below", IMessageProvider.INFORMATION);
	}
	
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite dialogArea = (Composite) super.createDialogArea(parent);
		
		Composite container = new Composite(dialogArea, SWT.NONE);
		container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		container.setLayout(new GridLayout());
		
		createVelocityInput(container);
		
		createMassInput(container);
		
		return dialogArea;
	}
	
	
	
	@Override
	protected void okPressed() {
		try {
			velocity.x = Float.parseFloat(xText.getText());
			velocity.y = Float.parseFloat(yText.getText());
			velocity.z = Float.parseFloat(zText.getText());
			mass = Float.parseFloat(massText.getText());
		} catch (NumberFormatException e) {
			displayErrorDialogue(getShell(), e.getMessage());
			return;
		}
		if (!autoVelocity && !Validator.validate(velocity)) {
			String message = "The entered velocity value or values are not within the required range. " + 
			" Values must be in the range " + Validator.MIN_VALUE + " to " + Validator.MAX_VALUE;
			displayErrorDialogue(getShell(), message);
			// Return so that superclass implementation isn't called
			return;
		}
		super.okPressed();
	}


	private void createVelocityInput(Composite container) {
		
		Group velocityGroup = new Group(container, SWT.NONE);
		velocityGroup.setText("Initial Velocity");
		velocityGroup.setLayoutData(getFillHorizontalGridData());
		velocityGroup.setLayout(new GridLayout(2, false));
		
		
		Label xLabel = new Label(velocityGroup, SWT.NONE);
		xLabel.setText("X: ");
		xText = new Text(velocityGroup, SWT.BORDER);
		xText.setLayoutData(getFillHorizontalGridData());
		xText.setText("0.0");
		
		Label yLabel = new Label(velocityGroup, SWT.NONE);
		yLabel.setText("Y: ");
		yText = new Text(velocityGroup, SWT.BORDER);
		yText.setLayoutData(getFillHorizontalGridData());
		yText.setText("0.0");
		
		Label zLabel = new Label(velocityGroup, SWT.NONE);
		zLabel.setText("Z: ");
		zText = new Text(velocityGroup, SWT.BORDER);
		zText.setLayoutData(getFillHorizontalGridData());
		zText.setText("0.0");
		
		Button autoVelocityButton = new Button(velocityGroup, SWT.CHECK);
		GridData layoutData = getFillHorizontalGridData();
		layoutData.horizontalSpan = 2;
		autoVelocityButton.setLayoutData(layoutData);
		autoVelocityButton.setText("Automatically Calculate Stable Velocity");
		autoVelocityButton.setSelection(false);
		autoVelocityButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (autoVelocityButton.getSelection()) {
					xText.setEnabled(false);
					yText.setEnabled(false);
					zText.setEnabled(false);
					//xText.setEditable(false);
					//yText.setEditable(false);
					//zText.setEditable(false);
					autoVelocity = true;
				} else {
					xText.setEnabled(true);
					yText.setEnabled(true);
					zText.setEnabled(true);
					autoVelocity = false;
				}
			}
			
		});
	}
	
	private void createMassInput(Composite container) {
		Group propertiesGroup = new Group(container, SWT.NONE);
		propertiesGroup.setText("Properties");
		GridData layoutData = getFillHorizontalGridData();
		propertiesGroup.setLayoutData(layoutData);
		propertiesGroup.setLayout(new GridLayout(2, false));
		
		Label massLabel = new Label(propertiesGroup, SWT.NONE);
		massLabel.setText("Mass: ");
		massText = new Text(propertiesGroup, SWT.BORDER);
		massText.setLayoutData(getFillHorizontalGridData());
		massText.setText("10");
	}


	
	
	public Vector3f getVelocity() {
		return velocity;
	}

	public boolean getAutoVelocity() {
		return autoVelocity;
	}

	public float getMass() {
		return mass;
	}
	
	
	
}
