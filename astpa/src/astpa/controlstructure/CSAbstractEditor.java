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

package astpa.controlstructure;

import java.awt.Event;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;

import messages.Messages;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.CoordinateListener;
import org.eclipse.draw2d.FigureCanvas;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.SWTGraphics;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Slider;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.part.EditorPart;

import astpa.controlstructure.controller.editparts.CSAbstractEditPart;
import astpa.controlstructure.controller.editparts.CSConnectionEditPart;
import astpa.controlstructure.controller.editparts.RootEditPart;
import astpa.controlstructure.controller.factorys.CSEditPartFactory;
import astpa.controlstructure.figure.IControlStructureFigure;
import astpa.controlstructure.figure.RootFigure;
import astpa.controlstructure.utilities.CSContextMenuProvider;
import astpa.controlstructure.utilities.CSPalettePage;
import astpa.controlstructure.utilities.CSPalettePreferences;
import astpa.controlstructure.utilities.CSTemplateTransferDropTargetListener;
import astpa.model.ObserverValue;
import astpa.model.controlstructure.interfaces.IRectangleComponent;
import astpa.model.interfaces.IControlStructureEditorDataModel;
import astpa.model.interfaces.IDataModel;

/**
 * 
 * CSGraphicalEditor is an implementation of the
 * <code>org.eclipse.ui.part.EditorPart</code> the code is based on the
 * implementation of GraphicalEditor
 * 
 * @version 1.0
 * @author Lukas Balzer
 * 
 */
public abstract class CSAbstractEditor extends EditorPart implements IControlStructureEditor {
	
	/**
	 * this property stores the active step editor the value is always expectedf
	 * to be the id of the Editor
	 * 
	 * @author Lukas Balzer
	 */
	public static final String STEP_EDITOR = "step"; //$NON-NLS-1$
	
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
	private IWorkbenchPartSite workSite;
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
	private static final int IMG_EXPAND = 10;
	
	private ToolBar toolBar;
	private ZoomManager manager;
	private Label label;
	private Slider scale;
	private Button redo;
	private Button undo;
	
	private int timeOfLastChange = 0;
	
	private final String workspacePath;
	private String imagePath;
	
	
	/**
	 * This constructor defines the Domain where the editable content should be
	 * displayed in
	 */
	public CSAbstractEditor() {
		this.setEditDomain(this);
		this.workspacePath = ResourcesPlugin.getWorkspace().getRoot().getLocation().toString();
	}
	
	@Override
	public void createPartControl(Composite parent) {
		
		FormLayout layout = new FormLayout();
		Composite editorComposite = new Composite(parent, SWT.BORDER);
		
		editorComposite.setLayout(layout);
		
		FormData data = new FormData();
		data.height = CSAbstractEditor.TOOL_HEIGHT;
		data.bottom = new FormAttachment(FULL_SCALE);
		data.right = new FormAttachment(FULL_SCALE);
		data.left = new FormAttachment(0);
		
		this.createToolBar(editorComposite, data);
		this.getCommandStack().addCommandStackListener(this);
		
		data = new FormData();
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(this.toolBar);
		data.right = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.left = new FormAttachment(0);
		this.splitter =
			new FlyoutPaletteComposite(editorComposite, SWT.CENTER, this.workSite.getPage(),
				this.getPaletteViewerProvider(), this.getPalettePreferences());
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
		viewer.addSelectionChangedListener(this);
		viewer.addPropertyChangeListener(this);
		
		viewer.createControl(parent);
		this.setGraphicalViewer(viewer);
		this.configureGraphicalViewer();
		this.hookGraphicalViewer();
		this.initializeGraphicalViewer();
		viewer.getControl().addMouseListener(this);
		viewer.setProperty(STEP_EDITOR, this.getId());
		
	}
	
	/**
	 * This method initializes all functions of the Graphical viewer
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void initializeGraphicalViewer() {
		
		GraphicalViewer viewer = this.getGraphicalViewer();
		this.splitter.hookDropTargetListener(viewer);
		viewer.setEditPartFactory(new CSEditPartFactory(this.modelInterface));
		
		viewer.setContents(this.createRoot());
		viewer.getContents().refresh();
		
	}
	
	/**
	 * @author Lukas Balzer
	 * 
	 * @return the root component
	 */
	private IRectangleComponent createRoot() {
		IRectangleComponent root = this.modelInterface.getRoot();
		
		if (root == null) {
			this.modelInterface.setRoot(new Rectangle(), new String());
			root = this.modelInterface.getRoot();
		}
		return root;
	}
	
	/**
	 * Called to configure the graphical viewer before it receives its contents.
	 * This is where the root editpart should be configured. Subclasses should
	 * extend or override this method as needed.
	 */
	private void configureGraphicalViewer() {
		this.getGraphicalViewer().getControl().setBackground(ColorConstants.listBackground);
		
		double[] zoomLevel = new double[CSAbstractEditor.ZOOM_LEVEL];
		ArrayList<String> zoomContributions;
		
		GraphicalViewer viewer = this.getGraphicalViewer();
		
		viewer.setEditPartFactory(new CSEditPartFactory(this.modelInterface));
		// zooming
		ScalableRootEditPart rootEditPart = new ScalableRootEditPart();
		
		viewer.setRootEditPart(rootEditPart);
		viewer.addDropTargetListener(new CSTemplateTransferDropTargetListener(viewer));
		
		this.manager = rootEditPart.getZoomManager();
		
		this.manager.getViewport().addCoordinateListener(new CoordinateListener() {
			
			@Override
			public void coordinateSystemChanged(IFigure source) {
				
				CSAbstractEditor.this.setViewport(CSAbstractEditor.this.manager.getViewport());
				CSAbstractEditor.this.manager.getViewport().setVerticalRangeModel(
					CSAbstractEditor.this.getViewport().getVerticalRangeModel());
				CSAbstractEditor.this.manager.getViewport().setHorizontalRangeModel(
					CSAbstractEditor.this.getViewport().getHorizontalRangeModel());
				CSAbstractEditor.this.manager.getViewport().setViewLocation(
					CSAbstractEditor.this.getViewport().getViewLocation());
				CSAbstractEditor.this.manager.getViewport().validate();
			}
		});
		this.getActionRegistry().registerAction(new ZoomInAction(this.manager));
		
		this.getActionRegistry().registerAction(new ZoomOutAction(this.manager));
		
		// array of possibles zoom levels. 0.2 = 20%, 3.0 = 300%
		for (int zoomFactor = 0; zoomFactor < CSAbstractEditor.ZOOM_LEVEL; zoomFactor++) {
			zoomLevel[zoomFactor] = CSAbstractEditor.MIN_ZOOM + (zoomFactor * CSAbstractEditor.ZOOM_STEP);
		}
		
		this.manager.setZoomLevels(zoomLevel);
		
		zoomContributions = new ArrayList<String>();
		zoomContributions.add(ZoomManager.FIT_ALL);
		zoomContributions.add(ZoomManager.FIT_HEIGHT);
		zoomContributions.add(ZoomManager.FIT_WIDTH);
		this.manager.setZoomLevelContributions(zoomContributions);
		
		this.manager.addZoomListener(this);
		
		// keyboard shortcuts
		KeyHandler keyHandler = new KeyHandler();
		
		keyHandler.put(KeyStroke.getPressed(SWT.DEL, CSAbstractEditor.DEL_KEY, 0),
			this.getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		
		keyHandler.put(KeyStroke.getPressed('+', SWT.KEYPAD_ADD, 0),
			this.getActionRegistry().getAction(GEFActionConstants.ZOOM_IN));
		
		keyHandler.put(KeyStroke.getPressed('-', SWT.KEYPAD_SUBTRACT, 0),
			this.getActionRegistry().getAction(GEFActionConstants.ZOOM_OUT));
		
		viewer.setProperty(MouseWheelHandler.KeyGenerator.getKey(SWT.CONTROL), MouseWheelZoomHandler.SINGLETON);
		
		viewer.setKeyHandler(keyHandler);
		
		ContextMenuProvider contextProvider = new CSContextMenuProvider(viewer, this.getActionRegistry());
		viewer.setContextMenu(contextProvider);
		((FigureCanvas) viewer.getControl()).setScrollBarVisibility(FigureCanvas.AUTOMATIC);
	}
	
	/**
	 * Hooks the GraphicalViewer to the rest of the Editor. By default, the
	 * viewer is added to the SelectionSynchronizer, which can be used to keep 2
	 * or more EditPartViewers in sync. The viewer is also registered as the
	 * ISelectionProvider for the Editor's PartSite.
	 */
	protected void hookGraphicalViewer() {
		this.workSite.setSelectionProvider(this.getGraphicalViewer());
		
	}
	
	/**
	 * this method (re-)creates the toolbar on the given composite The parent
	 * should have a formlayout since this class only accepts one
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param parent the composite on which the toolbar should appear
	 * @param toolbarData the data whoch determine where the toolbar shall be
	 *            displayed
	 * 
	 * @see CSAbstractEditor#toolBar
	 */
	private void createToolBar(Composite parent, FormData toolbarData) {
		this.toolBar = new ToolBar(parent, SWT.HORIZONTAL | SWT.SHADOW_OUT);
		FormLayout layout = new FormLayout();
		
		this.toolBar.setLayout(layout);
		this.toolBar.setLayoutData(toolbarData);
		
		FormData data = new FormData();
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		
		// adding the undo/redo Buttons
		this.undo = new Button(this.toolBar, SWT.BUTTON_MASK);
		data = new FormData(CSAbstractEditor.TOOL_HEIGHT, CSAbstractEditor.TOOL_HEIGHT);
		data.left = new FormAttachment(0);
		this.undo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
		this.undo.setLayoutData(data);
		this.redo = new Button(this.toolBar, SWT.BUTTON1);
		data = new FormData(CSAbstractEditor.TOOL_HEIGHT, CSAbstractEditor.TOOL_HEIGHT);
		data.left = new FormAttachment(this.undo);
		this.redo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
		this.redo.setLayoutData(data);
		
		data = new FormData();
		data.top = new FormAttachment(0);
		data.bottom = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		data.right = new FormAttachment(CSAbstractEditor.FULL_SCALE);
		this.scale = new Slider(this.toolBar, SWT.HORIZONTAL);
		this.scale.setBounds(0, 0, CSAbstractEditor.SCALE_WIDTH, CSAbstractEditor.TOOL_HEIGHT);
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
		this.label.setSize(CSAbstractEditor.SCLAE_TEXT_WIDTH, CSAbstractEditor.TOOL_HEIGHT / 2);
		this.label.setFont(new Font(null, Messages.ZoomLevel, CSAbstractEditor.SCALE_FONT, SWT.NORMAL));
		this.label.setText(Integer.toString(this.scale.getSelection()) + "%"); //$NON-NLS-1$
		this.label.setLayoutData(data);
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
	 * @param registry the registry
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
	 * @param the enum constant which says what actions should be returned
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
	 * @param event the change event
	 */
	@Override
	public void commandStackChanged(EventObject event) {
		this.firePropertyChange(IEditorPart.PROP_DIRTY);
		ISharedImages sharedImages = PlatformUI.getWorkbench().getSharedImages();
		
		// the undo/redo buttons in the toolbar are either activated or
		// deactivated
		// depending on the change
		if (this.getCommandStack().canRedo()) {
			this.redo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_FORWARD));
			if (!this.redo.isListening(Event.MOUSE_UP)) {
				this.redo.addMouseListener(this);
			}
			
		} else {
			this.redo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_FORWARD_DISABLED));
			this.redo.removeMouseListener(this);
			
		}
		
		if (this.getCommandStack().canUndo()) {
			this.undo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_BACK));
			if (!this.undo.isListening(Event.MOUSE_UP)) {
				this.undo.addMouseListener(this);
			}
		} else {
			this.undo.setImage(sharedImages.getImage(ISharedImages.IMG_TOOL_BACK_DISABLED));
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
	 * @param viewer the Viewer instance wich says what type of viewer is used,
	 *            for example a scalable
	 */
	public void setGraphicalViewer(GraphicalViewer viewer) {
		this.getEditDomain().addViewer(viewer);
		this.graphicalViewer = viewer;
	}
	
	/**
	 * This returns the viewer of this editor, the graphical viewer is a
	 * displays a canvas object on which alll the content of this editor is
	 * drawn
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The viewer which represents the Editor content
	 */
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
	 * @param editor the editor
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
			viewer.addDragSourceListener(new TemplateTransferDragSourceListener(viewer));
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
		this.workSite.getWorkbenchWindow().getSelectionService().removeSelectionListener(this);
		this.getEditDomain().setActiveTool(null);
		this.getActionRegistry().dispose();
		super.dispose();
	}
	
	@Override
	public IWorkbenchPartSite getSite() {
		return this.workSite;
	}
	
	@Override
	public void setSite(IWorkbenchPartSite workSite) {
		this.workSite = workSite;
	}
	
	/**
	 * Creates the GraphicalViewer on the specified <code>Composite.
	 * 
	 * @param parent the parent composite
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
			return ((GraphicalEditPart) this.getGraphicalViewer().getRootEditPart()).getFigure();
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
	 * @param actions the list of IDs to update
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
	
	@Override
	public void doSave(IProgressMonitor monitor) {
		int imageType;
		FileDialog dlg = new FileDialog(PlatformUI.createDisplay().getActiveShell(), SWT.SAVE);
		dlg.setText(Messages.ExportImage);
		dlg.setOverwrite(true);
		dlg.setFileName(this.getId());
		dlg.setFilterExtensions(new String[] {"*.jpg", "*.png", ".bmp"}); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String path = dlg.open();
		if (path == null) {
			return;
		} else if (path.endsWith("png")) { //$NON-NLS-1$
		
			imageType = SWT.IMAGE_PNG;
		} else if (path.endsWith("bmp")) { //$NON-NLS-1$
			imageType = SWT.IMAGE_BMP;
		} else if (path.endsWith("jpg")) { //$NON-NLS-1$
			imageType = SWT.IMAGE_JPEG;
		} else {
			return;
		}
		
		this.printViewer(path, imageType);
	}
	
	
	@Override
	public void onActivateView() {
		this.setFocus();
		this.getGraphicalViewer().getRootEditPart().deactivate();
		this.getGraphicalViewer().getEditPartRegistry().clear();
		
		this.configureGraphicalViewer();
		this.initializeGraphicalViewer();
		
		this.manager.setZoom(this.getZoomLevel());
		if (this.getViewport() != null) {
			this.manager.getViewport().setVerticalRangeModel(this.getViewport().getVerticalRangeModel());
			this.manager.getViewport().setHorizontalRangeModel(this.getViewport().getHorizontalRangeModel());
			this.manager.getViewport().setViewLocation(this.getViewport().getViewLocation());
			this.manager.getViewport().validate();
			
		}
		int level = (int) (this.getZoomLevel() * CSAbstractEditor.FULL_SCALE);
		this.scale.setSelection(level);
		this.label.setText(level + "%"); //$NON-NLS-1$
		this.setFocus();
		this.workSite.setSelectionProvider(null);
		this.workSite.setSelectionProvider(this.getGraphicalViewer());
		this.workSite.getWorkbenchWindow().getSelectionService().addSelectionListener(this);
		}
	
	@Override
	public void setDataModelInterface(IDataModel dataInterface) {
		this.modelInterface = (IControlStructureEditorDataModel) dataInterface;
		this.modelInterface.addObserver(this);
	}
	
	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		CSAbstractEditPart root = (CSAbstractEditPart) this.getGraphicalViewer().getContents();
		switch (type) {
		case CONTROL_STRUCTURE: {
			if (root != null) {
				root.refresh();
			}
			break;
		}
		case EXPORT: {
			if (!this.graphicalViewer.getControl().isVisible()) {
				// assures that the current data model is shown in the export
				this.getGraphicalViewer().getRootEditPart().deactivate();
				this.getGraphicalViewer().getEditPartRegistry().clear();
				
				this.configureGraphicalViewer();
				this.initializeGraphicalViewer();
				this.graphicalViewer.getContents().refresh();
			}
			
			String path = this.workspacePath + File.separator + this.getId() + ".png";//$NON-NLS-1$
			
			this.printViewer(path, SWT.IMAGE_PNG);
			this.imagePath = path;
			File tmpImg = new File(path);
			path = tmpImg.toURI().toString();
			if (this instanceof CSEditor) {
				
				this.modelInterface.setCSImagePath(path);
			} else {
				this.modelInterface.setCSPMImagePath(path);
			}
			break;
		}
		case EXPORT_FINISHED: {
			File tmp = new File(this.imagePath);
			tmp.delete();
			break;
		}
		default:
			break;
		}
		
	}
	
	/**
	 * this function prints the current contents of the CSGraphicalEditor
	 * 
	 * @author Lukas Balzer
	 * @param path the path defined as a String
	 * @param imageType The SWT constant which says in which format the img
	 *            should be stored
	 * @see GC
	 * @see ImageLoader
	 * @see SWT#IMAGE_PNG
	 * @see SWT#IMAGE_JPEG
	 */
	public final void printViewer(String path, int imageType) {
		GraphicalViewer viewer = this.getGraphicalViewer();
		ScalableRootEditPart rootEditPart = (ScalableRootEditPart) viewer.getRootEditPart();
		boolean isFirst = true;
		IFigure printableFigure =rootEditPart.getLayer(LayerConstants.PRINTABLE_LAYERS);
		
		//create a clip rectangle to cut the unnecessary whitespace
		Rectangle clipRectangle = new Rectangle();
		for(Object layers: printableFigure.getChildren()){
			//Layer&ConnectionLayer
			for(Object part: ((IFigure)layers).getChildren()){
				if(part instanceof RootFigure){
					for(final Object child: ((IFigure)part).getChildren()){
						if(isFirst){
							//the first component which is found by the loop is added
							//as starting Point for the rectangle
							isFirst= false;
							clipRectangle =new Rectangle(((IFigure)child).getBounds());
						}else{
							clipRectangle.union(((IFigure)child).getBounds());
						}
					}
				}
				else{
					clipRectangle.union(((IFigure)part).getBounds());
				}
			}
			
		}

		clipRectangle.expand(IMG_EXPAND, IMG_EXPAND);
		//a plain Image is created on which we can draw any graphics
		Image srcImage = new Image(null, printableFigure.getBounds().width+ IMG_EXPAND,
								printableFigure.getBounds().height + IMG_EXPAND);
		GC imageGC = new GC(srcImage);
		Graphics graphics = new SWTGraphics(imageGC);
		printableFigure.paint(graphics);
		
		//this additional Image is created with the actual Bounds
		//and the first one is clipped inside the scaled image
		Image scaledImage = new Image(null,clipRectangle.width,
        											  clipRectangle.height);
		imageGC = new GC(scaledImage);
		graphics = new SWTGraphics(imageGC);
		clipRectangle.x= Math.max(0, clipRectangle.x);
		clipRectangle.y= Math.max(0, clipRectangle.y);
		if(clipRectangle.x != 0 || clipRectangle.y !=0){
		
			graphics.drawImage(srcImage, clipRectangle, 
				new Rectangle(0, 0, clipRectangle.width,
									clipRectangle.height ));
		}else{
			graphics.drawImage(srcImage, 0, 0);
		}
        ImageLoader imgLoader = new ImageLoader();
		imgLoader.data = new ImageData[] {scaledImage.getImageData()};
		
		imgLoader.save(path, imageType);
		
	}
	
	@Override
	public void mouseDoubleClick(MouseEvent e) {
		List<EditPart> selection = this.getGraphicalViewer().getSelectedEditParts();
		if ((selection.size() == 1) && !(selection.get(0) instanceof CSConnectionEditPart)) {
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
			
			if (e.getSource().equals(this.undo) && this.getCommandStack().canUndo()) {
				this.getCommandStack().undo();
			} else if (e.getSource().equals(this.redo) && this.getCommandStack().canRedo()) {
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
			this.manager.setZoomAsText(zoom);
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
		
		// this.stepEditor.setViewLocation(this.manager.getViewport().getViewLocation());
		
	}
	
	@Override
	public void propertyChange(PropertyChangeEvent event) {
		
		if ((event.getPropertyName() == CSAbstractEditor.STEP_EDITOR) && (this.getGraphicalViewer() != null)) {
			this.getGraphicalViewer().getContents().refresh();
		}
		
	}
	
	@Override
	public void activeToolChanged(PaletteViewer palette, ToolEntry tool) {
		if (this.getGraphicalViewer() == null) {
			return;
		}
		if (tool instanceof ConnectionCreationToolEntry) {
			((RootEditPart) this.getGraphicalViewer().getContents()).enableFigureOffset();
			((RootEditPart) this.getGraphicalViewer().getContents()).addAnchorsGrid();
		} else {
			((IControlStructureFigure) ((RootEditPart) this.getGraphicalViewer().getContents()).getFigure())
				.removeHighlighter();
			((RootEditPart) this.getGraphicalViewer().getContents()).disableFigureOffset();
			((RootEditPart) this.getGraphicalViewer().getContents()).removeAnchorsGrid();
		}
		
	}
	
	@Override
	public void initialSync(boolean step1, boolean step3) {
		if (step1 && !step3) {
			this.modelInterface.synchronizeLayouts();
		}
	}
	
	@Override
	public void doSaveAs() {
		// This method is not called since A-STPA has it's own dataModel and
		// store function
		
	}
	
	@Override
	public boolean isDirty() {
		// every change made in the diagram first changed in the DataModel so
		// this function is not needed
		return true;
	}
	
	@Override
	public boolean isSaveAsAllowed() {
		// this method should always return false because save is forbidden by
		// default
		return true;
	}
	
	@Override
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		// The Editor is opened in a custom ViewContainer, so this method is
		// shouldn't do anything
		// because there is no EditorInput
		
	}
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		this.updateActions(this.selectionActions);
	}
}
