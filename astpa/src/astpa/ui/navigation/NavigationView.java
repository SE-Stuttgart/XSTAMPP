/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.ui.navigation;

import java.util.Observable;

import messages.Messages;

import org.eclipse.jface.layout.GridLayoutFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

import astpa.Activator;
import astpa.controlstructure.CSEditor;
import astpa.controlstructure.CSEditorWithPM;
import astpa.model.ObserverValue;
import astpa.model.interfaces.IDataModel;
import astpa.model.interfaces.INavigationViewDataModel;
import astpa.preferences.IPreferenceConstants;
import astpa.ui.acchaz.AccidentsView;
import astpa.ui.acchaz.HazardsView;
import astpa.ui.causalfactors.CausalFactorsView;
import astpa.ui.common.IViewBase;
import astpa.ui.linking.LinkingView;
import astpa.ui.sds.CSCView;
import astpa.ui.sds.ControlActionView;
import astpa.ui.sds.DesignRequirementView;
import astpa.ui.sds.SafetyConstraintView;
import astpa.ui.sds.SystemGoalView;
import astpa.ui.systemdescription.SystemDescriptionView;
import astpa.ui.unsafecontrolaction.UnsafeControlActionsView;

/**
 * This class generates a view to navigate through the single steps used for an
 * STPA analysis
 * 
 * @author Fabian Toth, Sebastian Sieber
 */
public class NavigationView implements IViewBase {
	
	/**
	 * The ID of this view
	 */
	public static final String ID = "astpa.ui.navigation.navigationView"; //$NON-NLS-1$
	
	// the strings for the buttons
	private static final String STEP1 = Messages.OneAnalysisFundamentals;
	private static final String STEP1_1 = Messages.SystemDescription;
	private static final String STEP1_2 = Messages.Accidents;
	private static final String STEP1_3 = Messages.Hazards;
	private static final String STEP1_4 = Messages.LinkingOfAccidentsAndHazards;
	private static final String STEP1_5 = Messages.SafetyConstraints;
	private static final String STEP1_6 = Messages.SystemGoals;
	private static final String STEP1_7 = Messages.DesignRequirements;
	private static final String STEP1_8 = Messages.ControlStructure;
	private static final String STEP2 = Messages.TwoUnsafeControlActions;
	private static final String STEP2_1 = Messages.ControlActions;
	private static final String STEP2_2 = Messages.UnsafeControlActionsTable;
	private static final String STEP2_3 = Messages.CorrespondingSafetyConstraints;
	private static final String STEP3 = Messages.ThreeCausalAnalysis;
	private static final String STEP3_1 = Messages.ControlStructureDiagramWithProcessModel;
	private static final String STEP3_2 = Messages.CausalFactorsTable;
	
	/**
	 * The height of the splitters
	 */
	private static final int SPLITTER_HEIGHT = 30;
	private static final int GAP_HEIGHT = 5;
	
	private GridData gridData;
	private GridData splitterGridData;
	private GridData gapGridData;
	private Composite grid;
	private NavigationItem activeItem;
	private GridLayout gridLayout;
	private INavigationViewDataModel dataInterface;
	private NavigationItem systemDescriptionItem;
	
	private boolean projectCreated = false;
	
	
	@Override
	public void createPartControl(Composite parent) {
		this.grid = new Composite(parent, SWT.NONE);
		this.initializeLayout();
		this.createFirstStep();
		this.createSecondStep();
		this.createThirdStep();
		this.setNewProjectName(this.dataInterface.getProjectName());
	}
	
	@Override
	public String getId() {
		return NavigationView.ID;
	}
	
	@Override
	public void onActivateView() {
		// Nothing to do here
	}
	
	@Override
	public String getTitle() {
		return Messages.Navigation;
	}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (INavigationViewDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case PROJECT_NAME:
			this.setNewProjectName(this.dataInterface.getProjectName());
			break;
		default:
			break;
		}
	}
	
	/**
	 * Sets the given item to active
	 * 
	 * @author Fabian Toth
	 * 
	 * @param item the item which is active
	 */
	public void setItemActive(NavigationItem item) {
		if (this.activeItem != null) {
			this.activeItem.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(IViewBase.STORE,
				IPreferenceConstants.NAVIGATION_ITEM_UNSELECTED)));
			this.activeItem.setInactive();
		}
		item.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(IViewBase.STORE,
			IPreferenceConstants.NAVIGATION_ITEM_SELECTED)));
		item.setActive();
		this.activeItem = item;
	}
	
	/**
	 * Sets the system description as the active item
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public void activateSystemDescription() {
		this.setItemActive(this.systemDescriptionItem);
	}
	
	/**
	 * Initializes the layout and the layoutData for the grid
	 * 
	 * @author Fabian Toth
	 */
	private void initializeLayout() {
		this.gridLayout = GridLayoutFactory.fillDefaults().create();
		this.gridLayout.horizontalSpacing = 0;
		this.gridLayout.verticalSpacing = 0;
		this.grid.setLayout(this.gridLayout);
		this.grid.setBackground(new Color(Display.getCurrent(), PreferenceConverter.getColor(IViewBase.STORE,
			IPreferenceConstants.NAVIGATION_ITEM_UNSELECTED)));
		this.gridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.splitterGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.splitterGridData.heightHint = NavigationView.SPLITTER_HEIGHT;
		this.gapGridData = new GridData(SWT.FILL, SWT.FILL, true, false);
		this.gapGridData.heightHint = NavigationView.GAP_HEIGHT;
	}
	
	/**
	 * Creates all necessary controls for the first step of the analysis
	 * 
	 * @author Fabian Toth
	 */
	private void createFirstStep() {
		NavigationItemSplitter splitter = new NavigationItemSplitter(this.grid, 0, NavigationView.STEP1);
		splitter.setLayoutData(this.splitterGridData);
		this.systemDescriptionItem = new NavigationItem(this.grid, 0, "STEP1_1", //$NON-NLS-1$
			NavigationView.STEP1_1, this, SystemDescriptionView.ID);
		this.systemDescriptionItem.setLayoutData(this.gridData);
		NavigationItem item = new NavigationItem(this.grid, 0, "STEP1_2", //$NON-NLS-1$
			NavigationView.STEP1_2, this, AccidentsView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_3", //$NON-NLS-1$
			NavigationView.STEP1_3, this, HazardsView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_4", //$NON-NLS-1$
			NavigationView.STEP1_4, this, LinkingView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_5", //$NON-NLS-1$
			NavigationView.STEP1_5, this, SafetyConstraintView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_6", //$NON-NLS-1$
			NavigationView.STEP1_6, this, SystemGoalView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_7", //$NON-NLS-1$
			NavigationView.STEP1_7, this, DesignRequirementView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP1_8", //$NON-NLS-1$
			NavigationView.STEP1_8, this, CSEditor.ID);
		item.setLayoutData(this.gridData);
		this.createGap();
	}
	
	/**
	 * Creates all necessary controls for the second step of the analysis
	 * 
	 * @author Fabian Toth
	 */
	private void createSecondStep() {
		NavigationItemSplitter splitter = new NavigationItemSplitter(this.grid, 0, NavigationView.STEP2);
		splitter.setLayoutData(this.splitterGridData);
		NavigationItem item = new NavigationItem(this.grid, 0, "STEP2_1", //$NON-NLS-1$
			NavigationView.STEP2_1, this, ControlActionView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP2_2", //$NON-NLS-1$
			NavigationView.STEP2_2, this, UnsafeControlActionsView.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP2_3", //$NON-NLS-1$
			NavigationView.STEP2_3, this, CSCView.ID);
		item.setLayoutData(this.gridData);
		this.createGap();
	}
	
	/**
	 * Creates all necessary controls for the third step of the analysis
	 * 
	 * @author Fabian Toth
	 */
	private void createThirdStep() {
		NavigationItemSplitter splitter = new NavigationItemSplitter(this.grid, 0, NavigationView.STEP3);
		splitter.setLayoutData(this.splitterGridData);
		NavigationItem item = new NavigationItem(this.grid, 0, "STEP3_1", //$NON-NLS-1$
			NavigationView.STEP3_1, this, CSEditorWithPM.ID);
		item.setLayoutData(this.gridData);
		item = new NavigationItem(this.grid, 0, "STEP3_2", //$NON-NLS-1$
			NavigationView.STEP3_2, this, CausalFactorsView.ID);
		item.setLayoutData(this.gridData);
	}
	
	/**
	 * Sets the window title and the project name label to the given projectName
	 * 
	 * @author Fabian Toth
	 * 
	 * @param projectName the new projectName
	 */
	private void setNewProjectName(String projectName) {
		if (this.projectCreated) {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText(Messages.ASTPAminus + projectName);
		} else {
			PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell().setText(Messages.ASTPA);
		}
	}
	
	private void createGap() {
		Canvas can = new Canvas(this.grid, SWT.NONE);
		can.addPaintListener(new PaintListener() {
			
			@Override
			public void paintControl(PaintEvent e) {
				e.gc.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));
			}
		});
		can.setLayoutData(this.gapGridData);
	}
	
	/**
	 * Sets, that a project has been created
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public void setPojectCreated() {
		this.projectCreated = true;
	}

	@Override
	public boolean triggerExport(Object[] values) {
		// nothing to export in this view
		return false;
	}

}
