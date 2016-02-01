package ui;

import static ui.UIUtils.*;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusAdapter;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Scale;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import math.geometry.Vector3f;
import render.ProjectionType;
import ui.Window2.UICallbacks;

public class SidePanel {
	
	// All widgets in this class are the direct or indirect
	// children of this composite.
	private Composite sidePanelComposite;
	
	private Button perspectiveButton;
	private Button orthographicButton;
	
	private Button relativeButton;
	private Button absoluteButton;
	
	private Button runButton;
	private Button editButton;
	
	private Text xText;
	private Text yText;
	private Text zText;
	
	private Text pitchText;
	private Text yawText;
	private Text rollText;
	
	private Scale speedScale;
	
	
	private UICallbacks uiCallbacks;
	
	public SidePanel(Shell shell, UICallbacks uiCallbacks) {
		this.uiCallbacks = uiCallbacks;
		sidePanelComposite = new Composite(shell, SWT.BORDER);
		initComposite();
		initProjectionTypeGroup();
		initCameraGroup();
		initSimulationGroup();
	}
	
	public void updateCameraPosition(Vector3f cameraPosition) {
		// Make sure the user isn't focused on the text box
		// when we try to change the contents.
		if (!xText.isFocusControl()) {
			xText.setText(String.valueOf(cameraPosition.x));
		}
		if (!yText.isFocusControl()) {
			yText.setText(String.valueOf(cameraPosition.y));
		}
		if (!zText.isFocusControl()) {
			zText.setText(String.valueOf(cameraPosition.z));
		}
	}
	
	public void updateCameraRotation(Vector3f cameraRotation) {
		// Make sure the user isn't focused on the text box
		// when we try to change the contents.
		if (!pitchText.isFocusControl()) {
			pitchText.setText(String.valueOf(cameraRotation.x));
		}
		if (!yawText.isFocusControl()) {
			yawText.setText(String.valueOf(cameraRotation.y));
		}
		if (!rollText.isFocusControl()) {
			rollText.setText(String.valueOf(cameraRotation.z));
		}
	}
	
	
	private void onCameraPositionEntered() {
		Vector3f newCameraPosition = new Vector3f();
		try {
			newCameraPosition.x = Float.parseFloat(xText.getText());
			newCameraPosition.y = Float.parseFloat(yText.getText());
			newCameraPosition.z = Float.parseFloat(zText.getText());
		} catch (NumberFormatException e) {
			// If there was an exception, return so that onCameraPositionChanged
			// isn't called with a new (0, 0, 0) vector
			return;
		}
		uiCallbacks.onCameraPositionChanged(newCameraPosition);
	}
	
	private void onCameraRotationEntered() {
		Vector3f newCameraRotation = new Vector3f();
		try {
			newCameraRotation.x = Float.parseFloat(pitchText.getText());
			newCameraRotation.y = Float.parseFloat(yawText.getText());
			newCameraRotation.z = Float.parseFloat(rollText.getText());
		} catch (NumberFormatException e) {
			// If there was an exception, return so that onCameraRotationChanged
			// isn't called with a new (0, 0, 0) vector
			return;
		}
		uiCallbacks.onCameraRotationChanged(newCameraRotation);
	}
	
	
	
	private void initComposite() {
		GridData layoutData = new GridData();
		// Should really not specify the width as a pixel value
		layoutData.widthHint = 250;
		// Fill any available space
		layoutData.verticalAlignment = SWT.FILL;
		layoutData.horizontalAlignment = SWT.FILL;
		sidePanelComposite.setLayoutData(layoutData);
		
		// Would ideally like to achieve the same effect 
		// with a simpler layout, but this doesn't seem possible.
		GridLayout sidePanelLayout = new GridLayout();
		sidePanelComposite.setLayout(sidePanelLayout);
	}
	
	private void initProjectionTypeGroup() {
		Group projectionTypeGroup = new Group(sidePanelComposite, SWT.NONE);
		projectionTypeGroup.setText("Projection Type");
		projectionTypeGroup.setLayoutData(getFillHorizontalGridData());
		projectionTypeGroup.setLayout(getGroupFillLayout());
		
		perspectiveButton = new Button(projectionTypeGroup, SWT.RADIO);
		perspectiveButton.setText("Perspective");
		perspectiveButton.setSelection(true);
		// Selection adapter used to avoid the "default selection" override
		perspectiveButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (perspectiveButton.getSelection()) {
					uiCallbacks.onProjectionChanged(ProjectionType.PERSPECTIVE);
				} else {
					uiCallbacks.onProjectionChanged(ProjectionType.ORTHOGRAPHIC);
				}
			}
			
		});
		orthographicButton = new Button(projectionTypeGroup, SWT.RADIO);
		orthographicButton.setText("Orthographic");
	}
	
	private void initCameraGroup() {
		Group cameraGroup = new Group(sidePanelComposite, SWT.NONE);
		cameraGroup.setText("Camera");
		cameraGroup.setLayoutData(getFillHorizontalGridData());
		cameraGroup.setLayout(new GridLayout());
		
		initCameraControlTypeGroup(cameraGroup);
		
		initCameraPositioningGroup(cameraGroup);
	}
	
	private void initCameraControlTypeGroup(Group cameraGroup) {
		Group cameraControlTypeGroup = new Group(cameraGroup, SWT.NONE);
		cameraControlTypeGroup.setText("Control Type");
		cameraControlTypeGroup.setLayoutData(getFillHorizontalGridData());
		cameraControlTypeGroup.setLayout(getGroupFillLayout());
		
		relativeButton = new Button(cameraControlTypeGroup, SWT.RADIO);
		relativeButton.setText("Relative");
		relativeButton.setSelection(true);
		relativeButton.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (relativeButton.getSelection()) {
					uiCallbacks.onCameraControlTypeChanged(true);
				} else {
					uiCallbacks.onCameraControlTypeChanged(false);
				}
			}
			
		});
		absoluteButton = new Button(cameraControlTypeGroup, SWT.RADIO);
		absoluteButton.setText("Absolute");
	}
	
	private void initCameraPositioningGroup(Group cameraGroup) {
		Group cameraPositioningGroup = new Group(cameraGroup, SWT.NONE);
		cameraPositioningGroup.setText("Positioning");
		cameraPositioningGroup.setLayoutData(getFillHorizontalGridData());
		cameraPositioningGroup.setLayout(new GridLayout(2, false));
		
		SelectionListener cameraPositionTextSelectionListener = new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				onCameraPositionEntered();
			}
			
		};
		
		FocusListener cameraPositionTextFocusListener = new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				onCameraPositionEntered();
			}
			
		};
		
		
		Label xLabel = new Label(cameraPositioningGroup, SWT.NONE);
		xLabel.setText("X: ");
		xText = new Text(cameraPositioningGroup, SWT.BORDER);
		xText.setLayoutData(getFillHorizontalGridData());
		xText.addSelectionListener(cameraPositionTextSelectionListener);
		xText.addFocusListener(cameraPositionTextFocusListener);
		
		Label yLabel = new Label(cameraPositioningGroup, SWT.NONE);
		yLabel.setText("Y: ");
		yText = new Text(cameraPositioningGroup, SWT.BORDER);
		yText.setLayoutData(getFillHorizontalGridData());
		yText.addSelectionListener(cameraPositionTextSelectionListener);
		yText.addFocusListener(cameraPositionTextFocusListener);
		
		Label zLabel = new Label(cameraPositioningGroup, SWT.NONE);
		zLabel.setText("Z: ");
		zText = new Text(cameraPositioningGroup, SWT.BORDER);
		zText.setLayoutData(getFillHorizontalGridData());
		zText.addSelectionListener(cameraPositionTextSelectionListener);
		zText.addFocusListener(cameraPositionTextFocusListener);
		
		
		
		// Position-Rotation Separator
		GridData separatorData = new GridData();
		separatorData.horizontalSpan = 2;
		separatorData.horizontalAlignment = SWT.FILL;
		new Label(cameraPositioningGroup, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(separatorData);
		
		
		SelectionListener cameraRotationTextSelectionListener = new SelectionAdapter() {

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				onCameraRotationEntered();
			}
			
		};
		
		FocusListener cameraRotationTextFocusListener = new FocusAdapter() {
			
			@Override
			public void focusLost(FocusEvent e) {
				onCameraRotationEntered();
			}
			
		};
		
		
		Label pitchLabel = new Label(cameraPositioningGroup, SWT.NONE);
		pitchLabel.setText("Pitch: ");
		pitchText = new Text(cameraPositioningGroup, SWT.BORDER);
		pitchText.setLayoutData(getFillHorizontalGridData());
		pitchText.addSelectionListener(cameraRotationTextSelectionListener);
		pitchText.addFocusListener(cameraRotationTextFocusListener);
		
		Label yawLabel = new Label(cameraPositioningGroup, SWT.NONE);
		yawLabel.setText("Yaw: ");
		yawText = new Text(cameraPositioningGroup, SWT.BORDER);
		yawText.setLayoutData(getFillHorizontalGridData());
		yawText.addSelectionListener(cameraRotationTextSelectionListener);
		yawText.addFocusListener(cameraRotationTextFocusListener);
		
		Label rollLabel = new Label(cameraPositioningGroup, SWT.NONE);
		rollLabel.setText("Roll: ");
		rollText = new Text(cameraPositioningGroup, SWT.BORDER);
		rollText.setLayoutData(getFillHorizontalGridData());
		rollText.addSelectionListener(cameraRotationTextSelectionListener);
		rollText.addFocusListener(cameraRotationTextFocusListener);
	}
	
	private void initSimulationGroup() {
		Group simulationGroup = new Group(sidePanelComposite, SWT.NONE);
		simulationGroup.setText("Simulation");
		simulationGroup.setLayoutData(getFillHorizontalGridData());
		simulationGroup.setLayout(new GridLayout(2, false));

		Label speedLabel = new Label(simulationGroup, SWT.NONE);
		speedLabel.setText("Speed: ");

		speedScale = new Scale(simulationGroup, SWT.NONE);
		speedScale.setPageIncrement(1);
		speedScale.setIncrement(1);
		speedScale.setMaximum(8);
		speedScale.setSelection(4); // Centre the slider at the middle value
		speedScale.setLayoutData(getFillHorizontalGridData());
		speedScale.addSelectionListener(new SelectionAdapter() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				int selection = speedScale.getSelection();
				System.out.println(selection);
				uiCallbacks.onTimeMultiplierChanged((double)selection / 4);
			}
			
		});


		Group simulationModeGroup = new Group(simulationGroup, SWT.NONE);
		simulationModeGroup.setText("Mode");
		GridData simulationModeGroupData = getFillHorizontalGridData();
		// Need the span = 2 because there are 2 columns
		simulationModeGroupData.horizontalSpan = 2;
		simulationModeGroup.setLayoutData(simulationModeGroupData);
		simulationModeGroup.setLayout(getGroupFillLayout());

		runButton = new Button(simulationModeGroup, SWT.RADIO);
		runButton.setText("Run");
		runButton.setSelection(true);
		editButton = new Button(simulationModeGroup, SWT.RADIO);
		editButton.setText("Edit");
	}

}
