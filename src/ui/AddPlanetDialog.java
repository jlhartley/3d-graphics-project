package ui;

import static ui.UIUtils.displayErrorDialogue;
import static ui.UIUtils.getFillHorizontalGridData;

import org.eclipse.jface.dialogs.IMessageProvider;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
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
		container.setLayout(new GridLayout(2, false));
		
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
			displayErrorDialogue(getShell(), e);
			return;
		}
		super.okPressed();
	}


	private void createVelocityInput(Composite container) {
		
		Group velocityGroup = new Group(container, SWT.NONE);
		velocityGroup.setText("Initial Velocity");
		GridData layoutData = getFillHorizontalGridData();
		// Need to span both columns of the container
		layoutData.horizontalSpan = 2;
		velocityGroup.setLayoutData(layoutData);
		velocityGroup.setLayout(new GridLayout(2, false));
		
		
		/*
		GridData separatorData = new GridData();
		separatorData.horizontalSpan = 2;
		separatorData.horizontalAlignment = SWT.FILL;
		Label separator = new Label(container, SWT.HORIZONTAL);
		separator.setLayoutData(separatorData);
		separator.setText("Initial Velocity");
		*/
		
		Label xLabel = new Label(velocityGroup, SWT.NONE);
		xLabel.setText("X: ");
		xText = new Text(velocityGroup, SWT.BORDER);
		xText.setLayoutData(getFillHorizontalGridData());
		
		Label yLabel = new Label(velocityGroup, SWT.NONE);
		yLabel.setText("Y: ");
		yText = new Text(velocityGroup, SWT.BORDER);
		yText.setLayoutData(getFillHorizontalGridData());
		
		Label zLabel = new Label(velocityGroup, SWT.NONE);
		zLabel.setText("Z: ");
		zText = new Text(velocityGroup, SWT.BORDER);
		zText.setLayoutData(getFillHorizontalGridData());
	}
	
	private void createMassInput(Composite container) {
		Label massLabel = new Label(container, SWT.NONE);
		massLabel.setText("Mass: ");
		massText = new Text(container, SWT.BORDER);
		massText.setLayoutData(getFillHorizontalGridData());
	}


	
	
	public Vector3f getVelocity() {
		return velocity;
	}


	public float getMass() {
		return mass;
	}
	
	
	
}
