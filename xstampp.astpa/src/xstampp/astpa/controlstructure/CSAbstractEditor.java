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

import java.awt.Event;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import messages.Messages;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.core.runtime.jobs.JobChangeAdapter;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.MouseWheelHandler;
import org.eclipse.gef.MouseWheelZoomHandler;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.dnd.TemplateTransferDragSourceListener;
import org.eclipse.gef.editparts.ScalableRootEditPart;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.palette.ConnectionCreationToolEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.ToolEntry;
import org.eclipse.gef.ui.actions.ActionBarContributor;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.DeleteAction;
import org.eclipse.gef.ui.actions.DirectEditAction;
import org.eclipse.gef.ui.actions.GEFActionConstants;
import org.eclipse.gef.ui.actions.PrintAction;
import org.eclipse.gef.ui.actions.RedoAction;
import org.eclipse.gef.ui.actions.SaveAction;
import org.eclipse.gef.ui.actions.SelectAllAction;
import org.eclipse.gef.ui.actions.UndoAction;
import org.eclipse.gef.ui.actions.UpdateAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite;
import org.eclipse.gef.ui.palette.FlyoutPaletteComposite.FlyoutPreferences;
import org.eclipse.gef.ui.palette.PaletteViewer;
import org.eclipse.gef.ui.palette.PaletteViewerProvider;
import org.eclipse.gef.ui.parts.ScrollingGraphicalViewer;
import org.eclipse.gef.ui.views.palette.PalettePage;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.ToolBarContributionItem;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import xstampp.astpa.controlstructure.controller.editparts.CSConnectionEditPart;
import xstampp.astpa.controlstructure.controller.editparts.RootEditPart;
import xstampp.astpa.controlstructure.controller.factorys.CSEditPartFactory;
import xstampp.astpa.controlstructure.utilities.CSContextMenuProvider;
import xstampp.astpa.controlstructure.utilities.CSPalettePage;
import xstampp.astpa.controlstructure.utilities.CSPalettePreferences;
import xstampp.astpa.controlstructure.utilities.CSTemplateTransferDropTargetListener;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;
import xstampp.astpa.util.IZoomContributor;
import xstampp.astpa.util.jobs.CSExportJob;
import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import xstampp.ui.menu.file.commands.CommandState;
import xstampp.util.STPAPluginUtils;

/**
 * 
 * CSGraphicalEditor is an implementation of the
 * <code>org.eclipse.ui.part.EditorPart</code> the code is based on the
 * implementation of GraphicalEditor
 * 
 * @version 1.0
 * @author Lukas Balzer
 * @since 2.1
 * 
 */
public abstract class CSAbstractEditor extends StandartEditorPart implements
		IControlStructureEditor,IZoomContributor {



	/**
	 * The GraphicalViewer is responsible for displaying all graphical Elements
	 * on the screen
	 */
	private GraphicalViewer graphicalViewer;

	/**
	 * the actionRegistry stores all Actions which can be performed by this
	 * Editor
	 * 
	 * @see CSAbstractEditor#createActions()
	 */
	private ActionRegistry actionRegistry;
	private IControlStructureEditorDataModel modelInterface;
	private DefaultEditDomain editDomain;
	private List<String> selectionActions = new ArrayList<String>();
	private List<String> stackActions = new ArrayList<String>();
	private List<String> propertyActions = new ArrayList<String>();
	private PaletteViewerProvider paletteProvider;
	private FlyoutPaletteComposite splitter;
	private CSPalettePage page;
	private static final int DEL_KEY = 127;
	private static final int ZOOM_LEVEL = 30;
	private static final double ZOOM_STEP = 0.1;
	private static final double MIN_ZOOM = 0.2;
	private static final int SCALE_STEP = 10;
	private static final int MIN_SCALE = 20;
	private static final int MAX_SCALE = 300;
	private static final int FULL_SCALE = 100;
	private static final int TOOL_HEIGHT = 20;
	private static final int SCALE_WIDTH = 200;
	private static final int SCLAE_TEXT_WIDTH = 150;
	private static final int SCALE_FONT = 10;
	protected static final int IMG_EXPAND = 10;

	private ToolBar toolBar;
	private ZoomManager zoomManager;
	private Label label;
	private Slider scale;
	private Button redo;
	private Button undo;
	// this bool can be set if a decoration in the next Image is wished
	private int timeOfLastChange = 0;

	private Button decoSwitch;

	private boolean asExport;

	/**
	 * This constructor defines the Domain where the editable content should be
	 * displayed in
	 */
	public CSAbstractEditor() {
		
		this.setEditDomain(this);
		this.asExport=false;
	}

	public void prepareForExport(){
		this.asExport=true;
	}
	@Override
	public void createPartControl(Composite parent) {
		this.setDataModelInterface(ProjectManager.getContainerInstance()
				.getDataModel(this.getProjectID()));
		FormLayout layout = new FormLayout();
		Composite editorComposite = new Composite(parent, SWT.BORDER);
		editorComposite.setBackground(null);
		editorComposite.setLayout(layout);
		
		FormData data = new FormData();
		data.height = CSAbstractEditor.TOOL_HEIGHT;
		data.bottom = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.right = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.left = new FormAttachment(0);
		if(!asExport){
			this.createToolBar(editorComposite, data);
		}
		this.getCommandStack().addCommandStackListener(this);

		data = new FormData();
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(this.toolBar);
		data.right = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.left = new FormAttachment(0);
		this.splitter = new FlyoutPaletteComposite(editorComposite, SWT.CENTER,
				this.getSite().getPage(), this.getPaletteViewerProvider(),
				this.getPalettePreferences());
		this.splitter.setLayoutData(data);
		this.initializeActionRegistry();
		this.createGraphicalViewer(this.splitter);
		this.splitter.setGraphicalControl(this.getGraphicalControl());
		if (this.page != null) {
			this.splitter.setExternalViewer(this.page.getPaletteViewer());
			this.page = null;

		}
		this.getEditDomain().setPaletteRoot(this.getPaletteRoot());
	}

	protected void createGraphicalViewer(Composite parent) {

		GraphicalViewer viewer = new ScrollingGraphicalViewer();
		viewer.setKeyHandler(new KeyHandler());
		this.setGraphicalViewer(viewer);
		viewer.setProperty(STEP_EDITOR, this.getId());
		viewer.setProperty(IS_DECORATED, this.decoSwitch.getSelection());
		viewer.addSelectionChangedListener(this);

		viewer.createControl(parent);
		this.configureGraphicalViewer(viewer);
		this.hookGraphicalViewer();
		this.initializeGraphicalViewer(viewer);
		viewer.getControl().addMouseListener(this);

		

	}

	/**
	 * This method initializes all functions of the Graphical viewer
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void initializeGraphicalViewer(GraphicalViewer viewer) {

		this.splitter.hookDropTargetListener(viewer);

		viewer.setContents(this.createRoot());
		viewer.getContents().refresh();

	}

	/**
	 * @author Lukas Balzer
	 * 
	 * @return the root component
	 */
	private IRectangleComponent createRoot() {
		IRectangleComponent root = this.getModelInterface().getRoot();

		if (root == null) {
			this.getModelInterface().setRoot(new Rectangle(), new String());
			root = this.getModelInterface().getRoot();
		}
		return root;
	}

	/**
	 * Called to configure the graphical viewer before it receives its contents.
	 * This is where the root editpart should be configured. Subclasses should
	 * extend or override this method as needed.
	 */
	public void configureGraphicalViewer(GraphicalViewer viewer) {
		viewer.getControl()
				.setBackground(ColorConstants.listBackground);

		double[] zoomLevel = new double[CSAbstractEditor.ZOOM_LEVEL];
		ArrayList<String> zoomContributions;


		viewer.setEditPartFactory(new CSEditPartFactory(this.getModelInterface(),
				this.getId()));
		// zooming
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();

		viewer.setRootEditPart(rootEditPart);
		viewer.addDropTargetListener(new CSTemplateTransferDropTargetListener(
				viewer, this.getModelInterface()));

		this.zoomManager = rootEditPart.getZoomManager();

		this.zoomManager.getViewport().addCoordinateListener(
				new CoordinateListener() {
					@Override
					public void coordinateSystemChanged(IFigure source) {

						CSAbstractEditor.this
								.setViewport(CSAbstractEditor.this.zoomManager
										.getViewport());
						CSAbstractEditor.this.zoomManager.getViewport()
								.setVerticalRangeModel(CSAbstractEditor.this.getViewport()
												.getVerticalRangeModel());
						CSAbstractEditor.this.zoomManager.getViewport()
								.setHorizontalRangeModel(CSAbstractEditor.this.getViewport()
												.getHorizontalRangeModel());
						CSAbstractEditor.this.zoomManager.getViewport()
								.setViewLocation(CSAbstractEditor.this.getViewport()
												.getViewLocation());
						CSAbstractEditor.this.zoomManager.getViewport().validate();
					}
				});
		this.getActionRegistry().registerAction(new ZoomInAction(this.zoomManager));

		this.getActionRegistry()
				.registerAction(new ZoomOutAction(this.zoomManager));

		// array of possibles zoom levels. 0.2 = 20%, 3.0 = 300%
		for (int zoomFactor = 0; zoomFactor < CSAbstractEditor.ZOOM_LEVEL; zoomFactor++) {
			zoomLevel[zoomFactor] = CSAbstractEditor.MIN_ZOOM
					+ (zoomFactor * CSAbstractEditor.ZOOM_STEP);
		}

		this.zoomManager.setZoomLevels(zoomLevel);

		zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		this.zoomManager.setZoomLevelContributions(zoomContributions);

		this.zoomManager.addZoomListener(this);

		// keyboard shortcuts
		KeyHandler keyHandler = new KeyHandler();

		viewer.getKeyHandler().put(KeyStroke.getPressed(SWT.DEL, CSAbstractEditor.DEL_KEY,
				0),
				this.getActionRegistry()
						.getAction(ActionFactory.DELETE.getId()));

		viewer.getKeyHandler().put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0), this
				.getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));

		viewer.getKeyHandler().put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0), this
				.getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));

		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CONTROL),
				MouseWheelZoomHandler.SINGLETON);


		ContextMenuProvider contextProvider = new CSContextMenuProvider(viewer,
				this.getActionRegistry());
		viewer.setContextMenu(contextProvider);
		((FigureCanvas) viewer.getControl())
				.setScrollBarVisibility(FigureCanvas.AUTOMATIC);
		this.getSite().registerContextMenu(viewer.getContextMenu(), viewer);
	}

	/**
	 * Hooks the GraphicalViewer to the rest of the Editor. By default, the
	 * viewer is added to the SelectionSynchronizer, which can be used to keep 2
	 * or more EditPartViewers in sync. The viewer is also registered as the
	 * ISelectionProvider for the Editor's PartSite.
	 */
	protected void hookGraphicalViewer() {
		this.getSite().setSelectionProvider(this.getGraphicalViewer());

	}

	/**
	 * this method (re-)creates the toolbar on the given composite The parent
	 * should have a formlayout since this class only accepts one
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param parent
	 *            the composite on which the toolbar should appear
	 * @param toolbarData
	 *            the data whoch determine where the toolbar shall be displayed
	 * 
	 * @see CSAbstractEditor#toolBar
	 */
	private void createToolBar(Composite parent, FormData toolbarData) {
		this.toolBar = new ToolBar(parent, SWT.HORIZONTAL | SWT.SHADOW_OUT);
		FormLayout layout = new FormLayout();

		this.toolBar.setLayout(layout);
		this.toolBar.setLayoutData(toolbarData);

		FormData data = new FormData();
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();

		// adding the undo/redo Buttons
		this.undo = new Button(this.toolBar, SWT.BUTTON_MASK);
		data = new FormData(CSAbstractEditor.TOOL_HEIGHT,
				CSAbstractEditor.TOOL_HEIGHT);
		data.left = new FormAttachment(0);
		this.undo.setImage(sharedImages
				.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
		this.undo.setLayoutData(data);
		this.redo = new Button(this.toolBar, SWT.BUTTON1);
		data = new FormData(CSAbstractEditor.TOOL_HEIGHT,
				CSAbstractEditor.TOOL_HEIGHT);
		data.left = new FormAttachment(this.undo);
		this.redo.setImage(sharedImages
				.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
		this.redo.setLayoutData(data);

		this.decoSwitch = new Button(this.toolBar, SWT.TOGGLE);
		data = new FormData();
		data.height = CSAbstractEditor.TOOL_HEIGHT;
		data.left = new FormAttachment(this.redo, 30);
		this.decoSwitch.setLayoutData(data);
		setDecoration(true);
		this.decoSwitch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
					CSAbstractEditor.this.setDecoration(((Button)e.getSource()).getSelection());

			}
		});

		final Button preferenceButton= new Button(this.toolBar, SWT.PUSH);
		preferenceButton.setText("Preferenes");
		data = new FormData();
		data.height = CSAbstractEditor.TOOL_HEIGHT;
		data.left = new FormAttachment(this.decoSwitch, 30);
		preferenceButton.setLayoutData(data);
		preferenceButton.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				Map<String,String> values=new HashMap<>();
				values.put("xstampp.command.preferencePage", "xstampp.astpa.preferencepage.cs");
				STPAPluginUtils.executeParaCommand("astpa.preferencepage", values);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				// TODO Auto-generated method stub
				
			}
		});
		
		data = new FormData();
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.right = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		this.scale = new Slider(this.toolBar, SWT.HORIZONTAL);
		this.scale.setBounds(0, 0, CSAbstractEditor.SCALE_WIDTH,
				CSAbstractEditor.TOOL_HEIGHT);
		this.scale.setMaximum(CSAbstractEditor.MAX_SCALE);
		this.scale.setMinimum(CSAbstractEditor.MIN_SCALE);
		this.scale.setPageIncrement(CSAbstractEditor.SCALE_STEP);
		this.scale.setIncrement(CSAbstractEditor.SCALE_STEP);
		this.scale.setToolTipText(Messages.ZoomItem);
		this.scale.setSelection(CSAbstractEditor.FULL_SCALE);
		this.scale.addSelectionListener(this);
		this.scale.setLayoutData(data);

		data = new FormData();
		data.right = new FormAttachment(this.scale);
		data.top = new FormAttachment(this.scale, 0, SWT.CENTER);
		this.label = new Label(this.toolBar, SWT.HORIZONTAL);
		this.label.setSize(CSAbstractEditor.SCLAE_TEXT_WIDTH,
				CSAbstractEditor.TOOL_HEIGHT / 2);
		this.label.setFont(new Font(null, Messages.ZoomLevel,
				CSAbstractEditor.SCALE_FONT, SWT.NORMAL));
		this.label.setText(Integer.toString(this.scale.getSelection()) + "%"); //$NON-NLS-1$
		this.label.setLayoutData(data);
		IToolBarManager toolManager = new ToolBarManager(this.toolBar);
		ToolBarContributionItem item = new ToolBarContributionItem(toolManager);
		this.getEditorSite().getActionBars().getToolBarManager().add(item);
	}

	/**
	 * Initializes the ActionRegistry. This registry may be used by
	 * {@link ActionBarContributor ActionBarContributors} and/or
	 * {@link ContextMenuProvider ContextMenuProviders}.
	 * <P>
	 * This method may be called on Editor creation, or lazily the first time
	 * {@link #getActionRegistry()} is called.
	 */
	protected void initializeActionRegistry() {
		this.createActions();
		this.updateActions(this.propertyActions);
		this.updateActions(this.stackActions);
		this.updateActions(this.selectionActions);

	}

	/**
	 * Sets the ActionRegistry for this EditorPart.
	 * 
	 * @param registry
	 *            the registry
	 */
	protected void setActionRegistry(ActionRegistry registry) {
		this.actionRegistry = registry;
	}

	/**
	 * Returns the list of {@link IAction IActions} dependant any actions in the
	 * Editor. These actions should implement the {@link UpdateAction} interface
	 * so that they can be updated in response to property changes. An example
	 * is the "Save" action.
	 * 
	 * @param the
	 *            enum constant which says what actions should be returned
	 * @return the list of actions, depending on the enum constant
	 * 
	 * @author Lukas Balzer
	 */
	protected List<String> getCSActions(CSAction actionName) {
		switch (actionName) {
		case PROPERTY_ACTION: {
			return this.propertyActions;
		}
		case STACK_ACTION: {
			return this.stackActions;
		}
		case SELECTION_ACTION: {
			return this.selectionActions;
		}
		case CREATE_ACTION:
			break;
		case DEFAULT:
			break;
		case RECONNECT_ACTION:
			break;
		default:
			break;
		}
		return this.propertyActions;
	}

	/**
	 * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart,
	 *      ISelection)
	 */
	@Override
	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		// If not the active editor, ignore selection changed.
		this.updateActions(this.selectionActions);
	}

	/**
	 * Creates actions for this editor. Subclasses should override this method
	 * to create and register actions with the {@link ActionRegistry}.
	 * 
	 * @author Lukas Balzer
	 */
	protected void createActions() {

		ActionRegistry registry = new ActionRegistry();
		IAction action;
		action = new UndoAction(this);
		registry.registerAction(action);
		this.stackActions.add(action.getId());

		action = new RedoAction(this);
		registry.registerAction(action);
		this.stackActions.add(action.getId());

		action = new SelectAllAction(this);
		registry.registerAction(action);

		action = new DeleteAction((IWorkbenchPart) this);
		registry.registerAction(action);
		this.selectionActions.add(action.getId());

		action = new DirectEditAction(this);
		registry.registerAction(action);
		this.selectionActions.add(action.getId());

		action = new SaveAction(this);
		action.setText(Messages.ExportImage);
		registry.registerAction(action);
		this.selectionActions.add(action.getId());

		action = new PrintAction(this);

		registry.registerAction(action);
		this.setActionRegistry(registry);
	}

	/**
	 * When the command stack changes, the actions interested in the command
	 * stack are updated.
	 * 
	 * @param event
	 *            the change event
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		this.firePropertyChange(IEditorPart.PROP_DIRTY);
		ISharedImages sharedImages = PlatformUI.getWorkbench()
				.getSharedImages();

		// the undo/redo buttons in the toolbar are either activated or
		// deactivated
		// depending on the change
		if (this.getCommandStack().canRedo()) {
			this.redo.setImage(sharedImages
					.getImage(ISharedImages.IMG_TOOL_FORWARD));
			this.redo.setGrayed(false);
			this.redo.setEnabled(true);

		} else {
			this.redo.setGrayed(true);
			this.redo.setEnabled(false);

		}

		if (this.getCommandStack().canUndo()) {
			this.undo.setImage(sharedImages
					.getImage(ISharedImages.IMG_TOOL_BACK));
			if (!this.undo.isListening(Event.MOUSE_UP)) {
				this.undo.addMouseListener(this);
			}
		} else {
			this.undo.setImage(sharedImages
					.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
			this.undo.removeMouseListener(this);
		}
		this.updateActions(this.stackActions);
		this.updateActions(this.propertyActions);
		this.updateActions(this.selectionActions);

	}

	/**
	 * this method returns a stack with all executed commands in it, this Stack
	 * is used for e.g. undo/redo by removing/re-adding commands to the stack
	 * 
	 * @return the stack of the domain where the executed commands are stored
	 */
	protected CommandStack getCommandStack() {
		return this.getEditDomain().getCommandStack();

	}

	/**
	 * This method sets the GraphicalViewer of this Editor and adds it to the
	 * EditDomain
	 * 
	 * @author Lukas
	 * 
	 * @param viewer
	 *            the Viewer instance wich says what type of viewer is used, for
	 *            example a scalable
	 */
	public void setGraphicalViewer(GraphicalViewer viewer) {
		this.getEditDomain().addViewer(viewer);
		this.graphicalViewer = viewer;
	}

	
	@Override
	public GraphicalViewer getGraphicalViewer() {
		return this.graphicalViewer;
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The DefaultEditDomain referring to this instance
	 */
	public DefaultEditDomain getEditDomain() {
		return this.editDomain;
	}

	/**
	 * sets the EditDomain and calls <code>CSEditor.getPaletteRoot()</code> on
	 * it to create the attached palette
	 * 
	 * @author Lukas Balzer
	 * @param editor
	 *            the editor
	 */
	public final void setEditDomain(EditorPart editor) {

		this.editDomain = new DefaultEditDomain(editor);

	}

	/**
	 * Lazily creates and returns the action registry.
	 * 
	 * @return the action registry
	 */
	protected ActionRegistry getActionRegistry() {
		if (this.actionRegistry == null) {
			this.actionRegistry = new ActionRegistry();
		}
		return this.actionRegistry;
	}

	@Override
	public void setFocus() {
		this.getGraphicalViewer().getControl().setFocus();
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);
		
	}

	/**
	 * Creates a PaletteViewerProvider that will be used to create palettes for
	 * the view and the flyout.
	 * 
	 * @return the palette provider
	 */
	private PaletteViewerProvider createPaletteViewerProvider() {
		return new CSPaletteViewerProvider(this.getEditDomain());
	}

	private class CSPaletteViewerProvider extends PaletteViewerProvider {

		public CSPaletteViewerProvider(DefaultEditDomain domain) {
			super(domain);
		}

		@Override
		protected void configurePaletteViewer(PaletteViewer viewer) {
			super.configurePaletteViewer(viewer);
			viewer.addDragSourceListener(new TemplateTransferDragSourceListener(
					viewer));
			viewer.addPaletteListener(CSAbstractEditor.this);

		}
	}

	/**
	 * @return a newly-created {@link CustomPalettePage}
	 */
	protected CSPalettePage createPalettePage() {
		return new CSPalettePage(this.getPaletteViewerProvider());
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	@Override
	public void dispose() {
		this.getCommandStack().removeCommandStackListener(this);
		this.modelInterface.deleteObserver(this);
		this.getSite().getWorkbenchWindow().getSelectionService()
				.removeSelectionListener(this);
		this.getEditDomain().setActiveTool(null);
		this.getActionRegistry().dispose();
		super.dispose();
	}

	/**
	 * Creates the GraphicalViewer on the specified <code>Composite.
	 * 
	 * @param parent
	 *            the parent composite
	 */

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class type) {
		if (type == GraphicalViewer.class) {
			return this.getGraphicalViewer();
		}
		if (type == CommandStack.class) {
			return this.getCommandStack();
		}
		if (type == ActionRegistry.class) {
			return this.getActionRegistry();
		}
		if ((type == EditPart.class) && (this.getGraphicalViewer() != null)) {
			return this.getGraphicalViewer().getRootEditPart();
		}
		if ((type == IFigure.class) && (this.getGraphicalViewer() != null)) {
			return ((GraphicalEditPart) this.getGraphicalViewer()
					.getRootEditPart()).getFigure();
		}
		if (type == PalettePage.class) {
			if (this.splitter == null) {
				this.page = this.createPalettePage();
				return this.page;
			}
			return this.createPalettePage();
		}
		return super.getAdapter(type);
	}

	/**
	 * @return the graphical viewer's control
	 */
	protected Control getGraphicalControl() {
		return this.getGraphicalViewer().getControl();
	}

	/**
	 * By default, this method returns a FlyoutPreferences object that stores
	 * the flyout settings in the GEF plugin. Sub-classes may override.
	 * 
	 * @return the FlyoutPreferences object used to save the flyout palette's
	 *         preferences
	 */

	protected FlyoutPreferences getPalettePreferences() {
		return new CSPalettePreferences();
	}

	/**
	 * Returns the PaletteRoot for the palette viewer.
	 * 
	 * @return the palette root
	 */
	protected abstract PaletteRoot getPaletteRoot();

	/**
	 * Returns the palette viewer provider that is used to create palettes for
	 * the view and the flyout. Creates one if it doesn't already exist.
	 * 
	 * @return the PaletteViewerProvider that can be used to create
	 *         PaletteViewers for this editor
	 * @see #createPaletteViewerProvider()
	 */
	protected final PaletteViewerProvider getPaletteViewerProvider() {
		if (this.paletteProvider == null) {
			this.paletteProvider = this.createPaletteViewerProvider();
		}

		return this.paletteProvider;
	}

	/**
	 * A convenience method for updating a set of actions defined by the given
	 * List of action IDs. The actions are found by looking up the ID in the
	 * {@link #getActionRegistry() action registry}. If the corresponding action
	 * is an {@link UpdateAction}, it will have its
	 * <code>update() method called.
	 * 
	 * @param actions
	 *            the list of IDs to update
	 */
	protected void updateActions(List<String> actions) {
		ActionRegistry registry = this.getActionRegistry();
		Iterator<String> iter = actions.iterator();
		while (iter.hasNext()) {
			IAction action = registry.getAction(iter.next());
			if (action instanceof UpdateAction) {
				((UpdateAction) action).update();
			}
		}
	}

	protected boolean printStructure(String path, int imgOffset, String name,
			String processName, boolean decorate) {

		Job exportJob = new CSExportJob(path, imgOffset,
				(String) this.getGraphicalViewer().getProperty(
						IControlStructureEditor.STEP_EDITOR), this.getProjectID(),
				true, decorate);
		exportJob.schedule();
		exportJob.addJobChangeListener(new JobChangeAdapter());
		return true;
	}


	public void setDataModelInterface(IDataModel dataInterface) {
		this.setModelInterface((IControlStructureEditorDataModel) dataInterface);
		this.getModelInterface().addObserver(this);

//		this.getEditDomain().setPaletteRoot(this.getPaletteRoot());
	}

	@Override
	public void setSite(IWorkbenchPartSite site) {
		super.setSite(site);
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		CSAbstractEditPart root = (CSAbstractEditPart) this
				.getGraphicalViewer().getContents();
		switch (type) {
		case CONTROL_STRUCTURE: {
			if (root != null) {
				root.refresh();
			}
			break;
		}
		default:
			break;
		}

	}

	@Override
	public void mouseDoubleClick(MouseEvent e) {
		List<EditPart> selection = this.getGraphicalViewer()
				.getSelectedEditParts();
		if ((selection.size() == 1)
				&& !(selection.get(0) instanceof CSConnectionEditPart)) {
			Request req = new Request();
			req.setType(RequestConstants.REQ_DIRECT_EDIT);
			((CSAbstractEditPart) selection.get(0)).performRequest(req);
		}

	}

	@Override
	public void mouseDown(MouseEvent e) {
		// is never used
	}

	@Override
	public void mouseUp(MouseEvent e) {

		if (e.time != this.timeOfLastChange) {
			// makes sure that the method is called only one time with one
			// trigger

			if (e.getSource().equals(this.undo)
					&& this.getCommandStack().canUndo()) {
				this.getCommandStack().undo();
			} else if (e.getSource().equals(this.redo)
					&& this.getCommandStack().canRedo()) {
				this.getCommandStack().redo();
			}
			this.timeOfLastChange = e.time;
		}
	}

	@Override
	public void widgetSelected(SelectionEvent e) {
		if (e.getSource().equals(this.scale)) {
			int tmpZoom = this.scale.getSelection();
			if (Math.abs(CSAbstractEditor.FULL_SCALE - tmpZoom) < CSAbstractEditor.SCALE_STEP) {
				tmpZoom = CSAbstractEditor.FULL_SCALE;
				this.scale.setSelection(tmpZoom);
			}
			String zoom = Integer.toString(tmpZoom);
			this.zoomManager.setZoomAsText(zoom);
			this.label.setText(zoom + "%"); //$NON-NLS-1$

		}

	}

	@Override
	public void widgetDefaultSelected(SelectionEvent e) {
		// is never used
	}

	@Override
	public void zoomChanged(double zoom) {
		this.update(null, ObserverValue.CONTROL_STRUCTURE);
		int level = (int) (zoom * CSAbstractEditor.FULL_SCALE);
		this.scale.setSelection(level);
		this.label.setText(level + "%"); //$NON-NLS-1$
		this.setZoomLevel(zoom);
		this.zoomManager.setZoom(zoom);
		// this.stepEditor.setViewLocation(this.manager.getViewport().getViewLocation());

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {
		if(event.getPropertyName().equals(IZoomContributor.IS_DECORATED)){
			this.decoSwitch.notifyListeners(SWT.Selection, null);
			this.decoSwitch.setSelection((boolean) event.getNewValue());
		}
		if ((event.getPropertyName() == IControlStructureEditor.STEP_EDITOR)
				&& (this.getGraphicalViewer() != null)) {
			this.getGraphicalViewer().getContents().refresh();
		}

	}
	
	@Override
	public Object getProperty(String propertyString){
		return getGraphicalViewer().getProperty(propertyString);
	}

	@Override
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (this.getGraphicalViewer() == null) {
			return;
		}
		if (tool instanceof ConnectionCreationToolEntry) {
			((RootEditPart) this.getGraphicalViewer().getContents())
					.enableFigureOffset();
			((RootEditPart) this.getGraphicalViewer().getContents())
					.addAnchorsGrid();
		} else {
			((RootEditPart) this.getGraphicalViewer().getContents())
					.getFigure().removeHighlighter();
			((RootEditPart) this.getGraphicalViewer().getContents())
					.disableFigureOffset();
			((RootEditPart) this.getGraphicalViewer().getContents())
					.setAnchorsGrid(false);
		}

	}

	@Override
	public void initialSync(boolean step1, boolean step3) {
		if (step1 && !step3) {
			this.getModelInterface().synchronizeLayouts();
		}
	}

	/**
	 * @param values
	 *            <ol>
	 *            <li>[0] must be the filePath
	 *            <li>[1] if there is a second value it is assumed as the offset
	 *            if not a default value is used
	 *            <li>[3] if there is a third value it is assumed as the boolean
	 *            deciding the decoration if there is no such value decoration
	 *            is turned off
	 *            </ol>
	 */
	protected boolean initExport(String name, String processName,
			Object[] values) {
		int offset;
		boolean decorate;
		// these if-blocks check whether there are appropriate values, and if
		// it's the right one if not it sets a default value
		if ((values[0] == null) || !(values[0] instanceof String)) {
			return false;
		}

		if ((values.length < 2) || (values[1] == null)
				|| !(values[1] instanceof Integer)) {
			offset = CSAbstractEditor.IMG_EXPAND;
		} else {
			offset = (int) values[1];
		}

		if ((values.length < 3) || (values[2] == null)
				|| !(values[2] instanceof Boolean)) {
			decorate = false;
		} else {
			decorate = (boolean) values[2];
		}
		return this.printStructure((String) values[0], offset, name,
				processName, decorate);
	}

	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		this.updateActions(this.selectionActions);
	}

	private void setDecoration(boolean deco){
		if(getGraphicalViewer() != null){
			this.graphicalViewer.setProperty(IS_DECORATED, deco);
			RootEditPart root = (RootEditPart) this.getGraphicalViewer()
					.getContents();
			root.getFigure().setDeco(deco);
			root.refresh();
		}
		this.decoSwitch.setSelection(deco);
		if(deco){
			this.decoSwitch.setText(Messages.ControlStructure_DecoOn);
		}else{
			this.decoSwitch.setText(Messages.ControlStructure_DecoOff);
		}
		
	}
	
	@Override
	public void updateZoom(double zoom) {
		this.zoomManager.setZoom(zoom);
	}

	@Override
	public ZoomManager getZoomManager() {
		return this.zoomManager;
	}
	
	@Override
	public void addPropertyListener(PropertyChangeListener listener) {
		this.getGraphicalViewer().addPropertyChangeListener(listener);
		
	}
	
	@Override
	public void fireToolPropertyChange(String property, Object value){
		if(property.equals(IS_DECORATED) && value instanceof Boolean){
			this.setDecoration((boolean) value);
			
		}
		
	}

	/**
	 * @return the modelInterface
	 */
	public IControlStructureEditorDataModel getModelInterface() {
		return modelInterface;
	}

	/**
	 * @param modelInterface the modelInterface to set
	 */
	public void setModelInterface(IControlStructureEditorDataModel modelInterface) {
		this.modelInterface = modelInterface;
	}
	

}


