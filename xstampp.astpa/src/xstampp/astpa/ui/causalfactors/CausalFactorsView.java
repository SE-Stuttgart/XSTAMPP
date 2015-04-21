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
import java.util.List;
import java.util.Observable;
import java.util.UUID;

import messages.Messages;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalListener;
import org.eclipse.jface.fieldassist.TextContentAdapter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import xstampp.astpa.model.ISafetyConstraint;
import xstampp.astpa.model.causalfactor.ICausalComponent;
import xstampp.astpa.model.causalfactor.ICausalFactor;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.ui.common.grid.AutoCompleteField;
import xstampp.astpa.ui.common.grid.CellButton;
import xstampp.astpa.ui.common.grid.GridCellButton;
import xstampp.astpa.ui.common.grid.GridCellColored;
import xstampp.astpa.ui.common.grid.GridCellEditor;
import xstampp.astpa.ui.common.grid.GridCellLinking;
import xstampp.astpa.ui.common.grid.GridCellRenderer;
import xstampp.astpa.ui.common.grid.GridCellText;
import xstampp.astpa.ui.common.grid.GridRow;
import xstampp.astpa.ui.common.grid.GridWrapper;
import xstampp.astpa.ui.common.grid.GridWrapper.NebulaGridRowWrapper;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.IViewBase;
import xstampp.ui.common.ViewContainer;
import xstampp.util.StandartEditorPart;

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

	private class SafetyConstraintEditorCell extends GridCellEditor {

		private ICausalFactor factor;

		public SafetyConstraintEditorCell(GridWrapper gridWrapper,
				String initialText, ICausalFactor factor) {
			super(gridWrapper, initialText);

			this.factor = factor;
		}

		@Override
		public void onTextChanged(String newText) {
			CausalFactorsView.this.dataInterface.setCausalSafetyConstraintText(
					this.factor.getSafetyConstraint().getId(), newText);
		}
	}

	private class NoteCell extends GridCellEditor {

		private ICausalFactor factor;

		public NoteCell(GridWrapper gridWrapper, String initialText,
				ICausalFactor factor) {
			super(gridWrapper, initialText);

			this.factor = factor;
		}

		@Override
		public void onTextChanged(String newText) {
			CausalFactorsView.this.dataInterface.setNoteText(
					this.factor.getId(), newText);
		}
	}

	private class CausalFactorCell extends GridCellEditor {

		private ICausalFactor factor;
		private Label deleteButton = null;

		public CausalFactorCell(GridWrapper gridWrapper, String initialText,
				ICausalFactor factor) {
			super(gridWrapper, initialText);
			this.factor = factor;

			Composite area = this.getCompositeArea();
			area.setLayout(new GridLayout(2, false));

			this.deleteButton = new Label(area, SWT.PUSH);
			this.deleteButton.setImage(GridWrapper.getDeleteButton16());
			this.deleteButton.addMouseListener(new MouseListener() {

				@Override
				public void mouseDoubleClick(MouseEvent e) {
					// intentionally empty
				}

				@Override
				public void mouseDown(MouseEvent e) {
					CausalFactorsView.LOGGER.debug(Messages.DeleteCausalFactor);

					if (MessageDialog.openConfirm(CausalFactorsView.this.grid
							.getGrid().getShell(),
							CausalFactorsView.CONFIRMATION_TITLE,
							CausalFactorsView.CONFIRMATION_DESCRIPTION)) {
						CausalFactorsView.this.dataInterface
								.removeCausalFactor(CausalFactorCell.this.factor
										.getId());
					}
				}

				@Override
				public void mouseUp(MouseEvent e) {
					// intentionally empty
				}
			});

			GridData gridData = new GridData();
			gridData.horizontalAlignment = GridData.FILL;
			gridData.verticalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = 1;
			this.getTextEditor().setLayoutData(gridData);

		}

		@Override
		public void onTextChanged(String newText) {
			CausalFactorsView.this.dataInterface.setCausalFactorText(
					this.factor.getId(), newText);
		}

		@Override
		public void onEditorFocus() {
			if (this.getTextEditor().getText()
					.equals(GridCellEditor.EMPTY_CELL_TEXT)) {
				this.getTextEditor().setText(""); //$NON-NLS-1$
			}
		}

		@Override
		public void paint(GridCellRenderer renderer, GC gc,
				NebulaGridRowWrapper item) {
			this.getCompositeArea().setBackground(
					this.getBackgroundColor(renderer, gc));
			this.getTextEditor().setBackground(
					this.getBackgroundColor(renderer, gc));
			this.deleteButton.setBackground(this.getBackgroundColor(renderer,
					gc));

			super.paint(renderer, gc, item);
		}

		@Override
		public UUID getUUID() {
			return this.factor.getId();
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
			scLinking.getContentProposalAdapter().addContentProposalListener(
					new IContentProposalListener() {

						@Override
						public void proposalAccepted(IContentProposal proposal) {
							CausalFactorsView.this.dataInterface
									.setCausalSafetyConstraintText(
											ImportButton.this.factor
													.getSafetyConstraint()
													.getId(), proposal
													.getDescription());
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

	/**
	 * Ctor.
	 * 
	 * @author Patrick Wickenhaeuser
	 * 
	 */
	public CausalFactorsView() {
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


	@Override
	public void createPartControl(Composite parent) {
		this.setDataModelInterface(ViewContainer.getContainerInstance()
				.getDataModel(this.getProjectID()));
		this.grid = new GridWrapper(parent, new String[] { Messages.Component,
				Messages.CausalFactors, Messages.HazardLinks,
				Messages.SafetyConstraint, Messages.NotesSlashRationale });

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
			GridRow csRow = new GridRow();
			GridCellEditor causalComp = new GridCellEditor(this.grid,
					component.getText());
			causalComp.getTextEditor().setEditable(false);
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
				GridRow childRow = new GridRow();
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

			GridRow buttonRow = new GridRow();
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
		this.grid.clearRows();
		this.fillTable(this.dataInterface.getCausalComponents());
		this.grid.reloadTable();
	}

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
			this.reloadTable();
			break;
		}
	}


	@Override
	public void dispose() {
		this.dataInterface.deleteObserver(this);
		super.dispose();
	}
}
