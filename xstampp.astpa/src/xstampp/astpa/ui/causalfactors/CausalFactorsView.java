/*******************************************************************************
 * 
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

package xstampp.astpa.ui.causalfactors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IWorkbenchPart;

import xstampp.astpa.model.ISafetyConstraint;
import xstampp.astpa.model.causalfactor.CausalFactor;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.ui.common.grid.AutoCompleteField;
import xstampp.astpa.ui.common.grid.CellButton;
import xstampp.astpa.ui.common.grid.GridCellButton;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellLinking;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridCellTextEditor;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.common.grid.GridWrapper;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;

/**
 * The view to add causal factors to control structure components, edit them and
 * add links to the related hazards.
 * 
 * @author Benedikt Markt, Patrick Wickenhaeuser
 */
public class CausalFactorsView extends StandartEditorPart{

	private static final RGB PARENT_BACKGROUND_COLOR = new RGB(215, 240, 255);
	private static final Point ADD_BUTTON_COORDINATE = new Point(40, 1);

	private static final String CONFIRMATION_TITLE = Messages.DeleteCausalFactor;
	private static final String CONFIRMATION_DESCRIPTION = Messages.WantToDeleteTheCF;
	private static final String CAUSALFACTORS= "Text filter for Causal Factors";
	private Map<UUID,CausalFactor> factorsToUUIDs;
	private class SafetyConstraintEditorCell extends GridCellTextEditor {

		private UUID safetyConstraintId;
		private UUID factorId;

		public SafetyConstraintEditorCell(GridWrapper gridWrapper,
				String initialText, ICausalFactor factor) {
			super(gridWrapper, initialText,false);
			this.factorId = factor.getId();
			this.safetyConstraintId = factor.getSafetyConstraint().getId();
		}

		@Override
		public void updateDataModel(String newText) {
			
			CausalFactorsView.this.addOrGetFactor(this.factorId, this.safetyConstraintId).getSafetyConstraint().setText(newText);
		}
		
		@Override
		public void delete() {
			// a safety constraint cannot be deleted
			
		}
		@Override
		public UUID getUUID() {
			return this.safetyConstraintId;
		}

		@Override
		public void onTextChange() {
			reloadTable();
		}
	}

	private class NoteCell extends GridCellTextEditor {

		private UUID factorId;
		private UUID safetyConstraintId;

		public NoteCell(GridWrapper gridWrapper, String initialText,
				ICausalFactor factor) {
			super(gridWrapper, initialText,false);
			this.factorId = factor.getId();
			this.safetyConstraintId = factor.getSafetyConstraint().getId();
		}

		@Override
		public void delete() {
			// a note cannot be deleted
		}

		@Override
		public void updateDataModel(String newValue) {
			CausalFactorsView.this.addOrGetFactor(this.factorId, this.safetyConstraintId).setNote(newValue);
		}

		@Override
		public UUID getUUID() {
			return this.factorId;
		}
	}

	private class CausalFactorCell extends GridCellTextEditor {

		private UUID factorID;
		private UUID safetyConstraintId;

		public CausalFactorCell(GridWrapper gridWrapper, String initialText,
				ICausalFactor factor) {
			super(gridWrapper, initialText,true);
			this.factorID = factor.getId();
			this.safetyConstraintId = factor.getSafetyConstraint().getId();
		}

		@Override
		public UUID getUUID() {
			return this.factorID;
		}

		@Override
		public void delete() {
			CausalFactorsView.LOGGER.debug(Messages.DeleteCausalFactor);

			if (MessageDialog.openConfirm(CausalFactorsView.this.grid
					.getGrid().getShell(),
					CausalFactorsView.CONFIRMATION_TITLE,
					CausalFactorsView.CONFIRMATION_DESCRIPTION)) {
				CausalFactorsView.this.dataInterface
						.removeCausalFactor(this.factorID);
			}
		}

		@Override
		public void updateDataModel(String newValue) {
			CausalFactorsView.this.addOrGetFactor(this.factorID, this.safetyConstraintId).setText(newValue);
		}
	}

	private class ImportButton extends CellButton {

		private ICausalFactor factor = null;

		public ImportButton(Rectangle rect, Image image, ICausalFactor factor) {
			super(rect, image);

			this.factor = factor;
		}

		@Override
		public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
			List<ISafetyConstraint> safetyConstraints = CausalFactorsView.this.dataInterface
					.getCorrespondingSafetyConstraints();
			Text selectedString = new Text(
					CausalFactorsView.this.grid.getGrid(), SWT.PUSH);
			if (safetyConstraints.size() <= 0) {
				return;
			}
			List<String> labels = new ArrayList<>();
			List<String> description = new ArrayList<>();
			List<String> literals = new ArrayList<>();
			String tmp;
			for (int i = 0; i < safetyConstraints.size(); i++) {
				tmp = safetyConstraints.get(i).getText();
				if (tmp.length() >= 1) {
					labels.add(tmp.substring(0, Math.min(tmp.length(), 40))
							+ "...");
					description.add(tmp);
					literals.add("");
				}
			}

			AutoCompleteField scLinking = new AutoCompleteField(selectedString,
					new TextContentAdapter(), literals.toArray(new String[0]),
					labels.toArray(new String[0]),
					description.toArray(new String[0]));
			scLinking.setProposalListener(
					new IContentProposalListener() {

						@Override
						public void proposalAccepted(IContentProposal proposal) {
							CausalFactorsView.this.dataInterface
									.setCausalSafetyConstraintText(
											ImportButton.this.factor
													.getSafetyConstraint()
													.getId(), proposal
													.getDescription());
							reloadTable();
						}
					});

			scLinking.setPopupPosition(relativeMouse, cellBounds, 0);
			scLinking.openPopup();

		}

	}

	private class NewButton extends CellButton {

		private ICausalFactor factor;

		public NewButton(Rectangle rect, Image image, ICausalFactor factor) {
			super(rect, image);

			this.factor = factor;
		}

		@Override
		public void onButtonDown(Point relativeMouse, Rectangle cellBounds) {
			CausalFactorsView.this.dataInterface.setCausalSafetyConstraintText(
					this.factor.getSafetyConstraint().getId(),
					Messages.NewSafetyConstraint);
		}
	}

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "astpa.steps.step3_2";

	private ICausalFactorDataModel dataInterface = null;

	/**
	 * The log4j logger.
	 */
	private static final Logger LOGGER = Logger.getRootLogger();

	private static final String HAZARD_ID_PREFIX = "H-"; //$NON-NLS-1$

	private GridWrapper grid;

	private CausalContentProvider causalContentProvider;
	private Text filterText;
	private Combo componentCombo;
	private boolean lockreload;

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public CausalFactorsView() {
		this.factorsToUUIDs = new HashMap<>();
		this.dataInterface = null;
		this.grid = null;
	}

	@Override
	public String getId() {
		return CausalFactorsView.ID;
	}

	@Override
	public String getTitle() {
		return Messages.CausalFactorsTable;
	}


	private CausalFactor addOrGetFactor(UUID id, UUID constraintId){
		if(this.factorsToUUIDs.containsKey(id)){
			return this.factorsToUUIDs.get(id);
		}
		CausalFactor factor= new CausalFactor(new String());
		factor.getSafetyConstraint().setId(constraintId);
		this.factorsToUUIDs.put(id, factor);
		return factor;
	}
	@Override
	public void createPartControl(Composite parent) {
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		parent.setLayout(new GridLayout(1, false));
		
		Composite filter= new Composite(parent, SWT.None);
		filter.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
		filter.setLayout(new GridLayout(3, false));
		
		Label label= new Label(filter,SWT.LEFT);
		label.setText("Component Filter");
		this.componentCombo = new Combo(filter, SWT.READ_ONLY|SWT.None);
		this.componentCombo.add("ALL");
		this.componentCombo.add(ComponentType.ACTUATOR.name());
		this.componentCombo.add(ComponentType.CONTROLLER.name());
		this.componentCombo.add(ComponentType.CONTROLLED_PROCESS.name());
		this.componentCombo.add(ComponentType.SENSOR.name());
		this.componentCombo.add(CAUSALFACTORS);
		this.componentCombo.select(0);
		this.componentCombo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				reloadTable();
			}
		});
		this.filterText= new Text(filter, SWT.LEFT |SWT.BORDER);
		this.filterText.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		this.filterText.addModifyListener(new ModifyListener() {
			
			@Override
			public void modifyText(ModifyEvent e) {
				reloadTable();
			}
		});
		this.grid = new GridWrapper(parent, new String[] { Messages.Component,
				Messages.CausalFactors, Messages.HazardLinks,
				Messages.SafetyConstraint, Messages.NotesSlashRationale });
		this.grid.getGrid().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		
		this.reloadTable();
	}

	/**
	 * The add button.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private class AddCausalFactorButton extends GridCellButton {

		private ICausalComponent component;

		/**
		 * Ctor.
		 * 
		 * @author Patrick Wickenhaeuser
		 * 
		 * @param component
		 *            the component the add buttons adds causal factors to.
		 */
		public AddCausalFactorButton(ICausalComponent component) {
			super(Messages.AddNewCausalFactor);

			this.component = component;
		}

		@Override
		public void onMouseDown(MouseEvent e,
				org.eclipse.swt.graphics.Point relativeMouse,
				Rectangle cellBounds) {
			CausalFactorsView.LOGGER.info(Messages.AddingNewCausalFactor);

			UUID factorId = CausalFactorsView.this.dataInterface
					.addCausalFactor(this.component.getId(), ""); //$NON-NLS-1$
			CausalFactorsView.this.dataInterface.setNoteText(factorId,
					Messages.Note);
			CausalFactorsView.this.grid.activateCell(factorId);
		}
	}
	private boolean isFiltered(ICausalFactor factor) {
		return factor.getText().startsWith(this.filterText.getText());
	}
	
	private boolean isFiltered(ICausalComponent component) {
		//filters for the text filter 
		boolean filter=component.getText().startsWith(this.filterText.getText());
		if(this.componentCombo.getText().equals("ALL")){
			return filter;
		}
		else if(this.componentCombo.getText().equals(CAUSALFACTORS)){
			for(ICausalFactor factor: component.getCausalFactors()){
				if(isFiltered(factor)){
					return true;
				}
			}
			return false;
		}
		return filter && component.getComponentType().name().equals(this.componentCombo.getText());
	}
	/**
	 * Fill the table.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 * @param components
	 *            the list of components.
	 */
	private void fillTable(List<ICausalComponent> components) {
		for (ICausalComponent component : components) {
			if(!isFiltered(component)){
				continue;
			}
			GridRow csRow = new GridRow(1);
			GridCellText causalComp = new GridCellText(component.getText());
			csRow.addCell(causalComp);
			csRow.addCell(new GridCellColored(this.grid,
					CausalFactorsView.PARENT_BACKGROUND_COLOR));
			csRow.addCell(new GridCellColored(this.grid,
					CausalFactorsView.PARENT_BACKGROUND_COLOR));
			csRow.addCell(new GridCellColored(this.grid,
					CausalFactorsView.PARENT_BACKGROUND_COLOR));
			csRow.addCell(new GridCellColored(this.grid,
					CausalFactorsView.PARENT_BACKGROUND_COLOR));
			this.grid.addRow(csRow);

			for (ICausalFactor factor : component.getCausalFactors()) {
				if(!isFiltered(factor)){
					continue;
				}
				GridRow childRow = new GridRow(1);
				childRow.addCell(new CausalFactorCell(this.grid, factor
						.getText(), factor));

				CausalFactorsView.LOGGER.info("Adding new GridCellLinking"); //$NON-NLS-1$
				childRow.addCell(new GridCellLinking<CausalContentProvider>(
						factor.getId(), this.causalContentProvider, this.grid,
						CausalFactorsView.HAZARD_ID_PREFIX));

				if (factor.getSafetyConstraint().getText().equals("")) { //$NON-NLS-1$
					GridCellText constraintsCell = new GridCellText(""); //$NON-NLS-1$
					constraintsCell.addCellButton(new NewButton(new Rectangle(
							4, 1,
							GridWrapper.getAddButton16().getBounds().width,
							GridWrapper.getAddButton16().getBounds().height),
							GridWrapper.getAddButton16(), factor));
					constraintsCell
							.addCellButton(new ImportButton(
									new Rectangle(
											CausalFactorsView.ADD_BUTTON_COORDINATE.x,
											CausalFactorsView.ADD_BUTTON_COORDINATE.y,
											GridWrapper.getLinkButton16()
													.getBounds().width,
											GridWrapper.getLinkButton16()
													.getBounds().height),
									GridWrapper.getLinkButton16(), factor));
					childRow.addCell(constraintsCell);
				} else {
					childRow.addCell(new SafetyConstraintEditorCell(this.grid,
							factor.getSafetyConstraint().getText(), factor));
				}

				childRow.addCell(new NoteCell(this.grid, factor.getNote(),
						factor));
				csRow.addChildRow(childRow);
			}

			GridRow buttonRow = new GridRow(1);
			buttonRow.addCell(new AddCausalFactorButton(component));

			csRow.addChildRow(buttonRow);
		}
	}

	/**
	 * Reload the whole table.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	private void reloadTable() {
		if(!this.lockreload){
			this.lockreload = true;
			int tmp= this.grid.getGrid().getVerticalBar().getSelection();
			for(UUID factorID: this.factorsToUUIDs.keySet()){
				this.dataInterface.setCausalFactorText(factorID, this.factorsToUUIDs.get(factorID).getText());
				this.dataInterface.setNoteText(factorID, this.factorsToUUIDs.get(factorID).getNote());
				this.dataInterface.setCausalSafetyConstraintText(this.factorsToUUIDs.get(factorID).getSafetyConstraint().getId(),
						this.factorsToUUIDs.get(factorID).getSafetyConstraint().getText());
			}
	
			this.factorsToUUIDs = new HashMap<>();
			this.grid.clearRows();
			this.fillTable(this.dataInterface.getCausalComponents());
			this.grid.reloadTable();
			this.lockreload = false;
			this.grid.getGrid().setTopIndex(tmp);
		}
	}

	/**
	 * sets the data model object for this editor
	 *
	 * @author Lukas
	 *
	 * @param dataInterface the data model object
	 */
	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (ICausalFactorDataModel) dataInterface;
		this.dataInterface.addObserver(this);
		this.causalContentProvider = new CausalContentProvider(
				(ICausalFactorDataModel) dataInterface);
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		default:
			Display.getDefault().syncExec(new Runnable() {

				@Override
				public void run() {
					CausalFactorsView.this.reloadTable();
				}
			});
			break;
		}
	}


	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
		super.dispose();
	}
	
	@Override
	public void partBroughtToTop(IWorkbenchPart arg0) {
		if(!this.lockreload && this.factorsToUUIDs.size() > 0){
			this.lockreload = true;
			for(UUID factorID: this.factorsToUUIDs.keySet()){
				this.dataInterface.setCausalFactorText(factorID, this.factorsToUUIDs.get(factorID).getText());
				this.dataInterface.setNoteText(factorID, this.factorsToUUIDs.get(factorID).getNote());
				this.dataInterface.setCausalSafetyConstraintText(this.factorsToUUIDs.get(factorID).getSafetyConstraint().getId(),
						this.factorsToUUIDs.get(factorID).getSafetyConstraint().getText());
			}
			this.lockreload = false;
			this.factorsToUUIDs = new HashMap<>();
		}
		super.partBroughtToTop(arg0);
	}
}
