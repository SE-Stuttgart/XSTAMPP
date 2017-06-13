/*******************************************************************************
 * Copyright (c) 2013, 2017 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.draw2d.Viewport;
import org.eclipse.gef.palette.CombinedTemplateCreationEntry;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.PaletteSeparator;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.tools.AbstractTool;
import org.eclipse.gef.tools.MarqueeSelectionTool;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;

import messages.Messages;
import xstampp.astpa.Activator;
import xstampp.astpa.controlstructure.controller.factorys.CSModelCreationFactory;
import xstampp.astpa.controlstructure.controller.factorys.ConnectionCreationFactory;
import xstampp.astpa.controlstructure.utilities.DragSelectionToolEntry;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.components.ConnectionType;
import xstampp.ui.common.ProjectManager;

/**
 * The Graphical Editor is responsible for creating the manipulatable content of
 * the Editor view. The extension of GraphicalEditorWithFlyoutPalette also adds
 *
 *
 * @since 1.0
 * @author Lukas Balzer, Aliaksei Babkovich
 *
 */
public class CSEditor extends CSAbstractEditor {

	/**
	 * The ID is used to reference the Editor
	 */
	public static final String ID = "astpa.steps.step1_8"; //$NON-NLS-1$
	private double zoomLevel;
	private Viewport viewLocation;
	private ToolEntry simpleConnectionEntry;
	private Map<Object, ToolEntry> toolEntryToComponentType;

	private class OpenAction extends Action {
		private ToolEntry entry;

		public OpenAction(ToolEntry entry) {
			this.entry = entry;
		}

		@Override
		public void run() {
			getEditDomain().getPaletteViewer().setActiveTool(this.entry);
		}
	}

	/**
	 * this sets the zoom initially to 100%
	 *
	 * @author Lukas Balzer
	 *
	 */
	public CSEditor() {
		super(Activator.getDefault().getPreferenceStore());
		this.zoomLevel = 1.0;
		this.viewLocation = null;
		this.toolEntryToComponentType = new HashMap<>();
	}

	/**
	 * creates fly out palette with tools
	 *
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @return
	 */
	@Override
	public PaletteRoot getPaletteRoot() {
		PaletteRoot root = new PaletteRoot();
		PaletteDrawer manipGroup = new PaletteDrawer(Messages.ManipulationObjects);
		root.add(manipGroup);
		ToolEntry entry = new DragSelectionToolEntry();
		entry.setDescription(Messages.SpacePlusMouseTo);
		manipGroup.add(entry);
		this.toolEntryToComponentType.put("SELECT", entry);
		root.setDefaultEntry(entry);

		entry = new MarqueeToolEntry();
		entry.setToolProperty(MarqueeSelectionTool.PROPERTY_MARQUEE_BEHAVIOR,
							  MarqueeSelectionTool.BEHAVIOR_NODES_CONTAINED_AND_RELATED_CONNECTIONS);
		entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED,true);
		manipGroup.add(entry);
		this.toolEntryToComponentType.put("MARQUEE", entry);

		PaletteSeparator separator = new PaletteSeparator();
		root.add(separator);
		PaletteDrawer componentElements = new PaletteDrawer(Messages.ComponentElements);
		root.add(componentElements);

		ImageDescriptor imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/controller_palette_32.png"); //$NON-NLS-1$
		ImageDescriptor imgDescLarge = Activator
				.getImageDescriptor("/icons/buttons/controlstructure/controller_palette_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.Controller + "", Messages.CreateController,
				ComponentType.CONTROLLER,
				new CSModelCreationFactory(ComponentType.CONTROLLER, this.getRoot()), imgDesc, imgDescLarge);
		componentElements.add(entry);
		this.toolEntryToComponentType.put(ComponentType.CONTROLLER, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/actuator_palette_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/actuator_palette_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.Actuator, Messages.CreateActuator, ComponentType.ACTUATOR,
				new CSModelCreationFactory(ComponentType.ACTUATOR, this.getRoot()), imgDesc, imgDescLarge);
		componentElements.add(entry);
		this.toolEntryToComponentType.put(ComponentType.ACTUATOR, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/process_palette_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/process_palette_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.ControlledProcess, Messages.CreateControlledProcess,
				ComponentType.CONTROLLED_PROCESS,
				new CSModelCreationFactory(ComponentType.CONTROLLED_PROCESS, this.getRoot()), imgDesc,
				imgDescLarge);
		componentElements.add(entry);
		this.toolEntryToComponentType.put(ComponentType.CONTROLLED_PROCESS, entry);

    imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/sensor_palette_32.png"); //$NON-NLS-1$
    imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/sensor_palette_40.png"); //$NON-NLS-1$
    entry = new CombinedTemplateCreationEntry(Messages.Sensor, Messages.CreateSensor, ComponentType.SENSOR,
        new CSModelCreationFactory(ComponentType.SENSOR, this.getRoot()), imgDesc, imgDescLarge);
    componentElements.add(entry);
    this.toolEntryToComponentType.put(ComponentType.SENSOR, entry);

    imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/Feedback_palette_32.png"); //$NON-NLS-1$
    imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/Feedback_palette_40.png"); //$NON-NLS-1$
    entry = new CombinedTemplateCreationEntry("Feedback", "Create a Feedback Component", ComponentType.FEEDBACK,
        new CSModelCreationFactory(ComponentType.FEEDBACK, this.getRoot()), imgDesc, imgDescLarge);
    componentElements.add(entry);
    this.toolEntryToComponentType.put(ComponentType.FEEDBACK, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/controlAction_palette_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/controlAction_palette_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.ControlAction, Messages.CreateControlAction,
				ComponentType.CONTROLACTION,
				new CSModelCreationFactory(ComponentType.CONTROLACTION, this.getRoot()), imgDesc,
				imgDescLarge);
		componentElements.add(entry);
		this.toolEntryToComponentType.put(ComponentType.CONTROLACTION, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/controlAction_List_palette_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/controlAction_List_palette_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry("List of Control Actions",
				"Create a container, in which you can bundle Control Actions", ComponentType.CONTAINER,
				new CSModelCreationFactory(ComponentType.CONTAINER, this.getRoot()), imgDesc, imgDescLarge);
		componentElements.add(entry);
		root.add(separator);
		PaletteDrawer connectionElements = new PaletteDrawer(Messages.ConnectingElements);
		root.add(connectionElements);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/arrow_simple_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/arrow_simple_40.png"); //$NON-NLS-1$
		entry = new ConnectionCreationToolEntry(Messages.Arrow, Messages.CreateConnections,
				new ConnectionCreationFactory(ConnectionType.ARROW_SIMPLE), imgDesc, imgDescLarge);
		entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED,true);
		connectionElements.add(entry);
		this.toolEntryToComponentType.put(ConnectionType.ARROW_SIMPLE, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/arrow_dashed_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/arrow_dashed_40.png"); //$NON-NLS-1$
		entry = new ConnectionCreationToolEntry(Messages.DashedArrows, Messages.CreateConnections,
				new ConnectionCreationFactory(ConnectionType.ARROW_DASHED), imgDesc, imgDescLarge);
		connectionElements.add(entry);
		entry.setToolProperty(AbstractTool.PROPERTY_UNLOAD_WHEN_FINISHED,true);
		this.toolEntryToComponentType.put(ConnectionType.ARROW_DASHED, entry);

		root.add(separator);
		PaletteDrawer otherElements = new PaletteDrawer(Messages.Others);
		root.add(otherElements);
		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/text_box_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/text_box_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.TextBox, Messages.CreateTextBox, ComponentType.TEXTFIELD,
				new CSModelCreationFactory(ComponentType.TEXTFIELD, this.getRoot()), imgDesc, imgDescLarge);
		otherElements.add(entry);
		this.toolEntryToComponentType.put(ComponentType.TEXTFIELD, entry);

		imgDesc = Activator.getImageDescriptor("/icons/buttons/controlstructure/dashed_box_32.png"); //$NON-NLS-1$
		imgDescLarge = Activator.getImageDescriptor("/icons/buttons/controlstructure/dashed_box_40.png"); //$NON-NLS-1$
		entry = new CombinedTemplateCreationEntry(Messages.DashedBox, Messages.CreateDashedBox, ComponentType.DASHEDBOX,
				new CSModelCreationFactory(ComponentType.DASHEDBOX, this.getRoot()), imgDesc, imgDescLarge);
		otherElements.add(entry);
	this.toolEntryToComponentType.put(ComponentType.DASHEDBOX, entry);

		return root;
	}

	public void changeActiveTool(String activeTool) {
		if (this.toolEntryToComponentType.containsKey(activeTool)) {
			this.getEditDomain().getPaletteViewer().setActiveTool(this.toolEntryToComponentType.get(activeTool));
			return;
		}
		for (ComponentType type : ComponentType.values()) {
			if (type.toString().equals(activeTool)) {
				this.getEditDomain().getPaletteViewer().setActiveTool(this.toolEntryToComponentType.get(type));
				return;
			}
		}

		for (ConnectionType type : ConnectionType.values()) {
			if (type.toString().equals(activeTool)) {
				this.getEditDomain().getPaletteViewer().setActiveTool(this.toolEntryToComponentType.get(type));
				return;
			}
		}
		ProjectManager.getLOGGER().debug("there is no tool registered for " + activeTool + " in " + ID);
	}

	@Override
	public String getTitle() {
		return Messages.ControlStructure;
	}

	@Override
	public String getId() {
		return CSEditor.ID;
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
		if (!getSite().getId().equals("acast.steps.step2_1")) {
			getGraphicalControl().addFocusListener(new FocusListener() {
				private IContextActivation activation;

				@Override
				public void focusLost(FocusEvent e) {
					IContextService contextService = (IContextService) getSite().getService(IContextService.class);
					contextService.deactivateContext(this.activation); // $NON-NLS-1$
				}

				@Override
				public void focusGained(FocusEvent e) {
					IContextService contextService = (IContextService) getSite().getService(IContextService.class);
					this.activation = contextService.activateContext("xstampp.astpa.csContext"); //$NON-NLS-1$
				}
			});
		}

	}

}
