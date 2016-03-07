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

package xstampp.astpa.controlstructure;

import messages.Messages;

import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;

import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.controller.factorys.CSModelCreationFactory;
import xstampp.astpa.controlstructure.utilities.DragSelectionToolEntry;
import xstampp.astpa.model.controlstructure.components.ComponentType;

/**
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * 
 */
public class CSEditorWithPM extends CSAbstractEditor {

	/**
	 * The ID is used to reference the {@link CSEditorWithPM}
	 */
	public static final String ID = "astpa.steps.step3_1"; //$NON-NLS-1$
	private double zoomLevel;
	private Viewport viewLocation;

	/**
	 * this sets the zoom initially to 100%
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public CSEditorWithPM() {
		super(Activator.getDefault().getPreferenceStore());
		this.zoomLevel = 1.0;
		this.viewLocation = null;
	}

	@Override
	public PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();

		PaletteDrawer manipGroup = new PaletteDrawer(
				Messages.ManipulationObjects);
		root.add(manipGroup);

		DragSelectionToolEntry selectionToolEntry = new DragSelectionToolEntry();
		selectionToolEntry.setDescription(Messages.SpacePlusMouseTo);
		manipGroup.add(selectionToolEntry);
		MarqueeToolEntry entry = new MarqueeToolEntry();

		entry.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR,
							  MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS);
		entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED,true);
		manipGroup.add(entry);
		root.setDefaultEntry(selectionToolEntry);

		PaletteSeparator separator = new PaletteSeparator();
		root.add(separator);
		PaletteDrawer componentElements = new PaletteDrawer(
				Messages.ComponentElements);
		root.add(componentElements);

		ImageDescriptor imgDesc = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_model_32.png"); //$NON-NLS-1$
		ImageDescriptor imgDescLarge = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_model_40.png"); //$NON-NLS-1$
		componentElements.add(new CombinedTemplateCreationEntry(
				Messages.ProcessModel, Messages.CreateProcessModel,
				ComponentType.PROCESS_MODEL, new CSModelCreationFactory(
						ComponentType.PROCESS_MODEL, this.getModelInterface()),
				imgDesc, imgDescLarge));

		PaletteDrawer additionalElements = new PaletteDrawer(Messages.Others);
		root.add(additionalElements);

		imgDesc = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_variable_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_variable.png"); //$NON-NLS-1$
		additionalElements.add(new CombinedTemplateCreationEntry(
				Messages.ProcessVariable, Messages.CreateProcessVariable,
				ComponentType.PROCESS_VARIABLE, new CSModelCreationFactory(
						ComponentType.PROCESS_VARIABLE, this.getModelInterface()),
				imgDesc, imgDescLarge));

		imgDesc = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_value_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/process_value.png"); //$NON-NLS-1$
		additionalElements.add(new CombinedTemplateCreationEntry(
				Messages.ProcessValue, Messages.CreateProcessValue,
				ComponentType.PROCESS_VALUE, new CSModelCreationFactory(
						ComponentType.PROCESS_VALUE, this.getModelInterface()),
				imgDesc, imgDescLarge));
		
		PaletteDrawer otherElements = new PaletteDrawer(Messages.Others);
		root.add(otherElements);
		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/text_box_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/text_box_40.png"); //$NON-NLS-1$
		otherElements.add(new CombinedTemplateCreationEntry(Messages.TextBox, Messages.CreateTextBox, ComponentType.TEXTFIELD,
				new CSModelCreationFactory(ComponentType.TEXTFIELD, this.getModelInterface()), imgDesc, imgDescLarge));

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/dashed_box_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/dashed_box_40.png"); //$NON-NLS-1$
		otherElements.add(new CombinedTemplateCreationEntry(Messages.DashedBox, Messages.CreateDashedBox, ComponentType.DASHEDBOX,
				new CSModelCreationFactory(ComponentType.DASHEDBOX, this.getModelInterface()), imgDesc, imgDescLarge));
		return root;
	}

	@Override
	public String getTitle() {
		return Messages.ControlStructureDiagramWithProcessModel;
	}

	@Override
	public String getId() {
		return CSEditorWithPM.ID;
	}

	@Override
	public double getZoomLevel() {
		return this.zoomLevel;
	}

	@Override
	public void setZoomLevel(double zoom) {
		this.zoomLevel = zoom;

	}

	@Override
	public Viewport getViewport() {
		return this.viewLocation;
	}

	@Override
	public void setViewport(Viewport view) {
		this.viewLocation = view;
	}

	@Override
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		getGraphicalControl().addFocusListener(new FocusListener() {
			
			private IContextActivation activation;

			@Override
			public void focusLost(FocusEvent e) {
				IContextService contextService=(IContextService)getSite().getService(IContextService.class);
				
				 contextService.deactivateContext(this.activation); 
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				 IContextService contextService=(IContextService)getSite().getService(IContextService.class);
				
				 this.activation = contextService.activateContext("xstampp.astpa.csContext"); //$NON-NLS-1$
			}
		});
	}
}
