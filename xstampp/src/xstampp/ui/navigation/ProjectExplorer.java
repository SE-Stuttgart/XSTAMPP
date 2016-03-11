package xstampp.ui.navigation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.UUID;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.contexts.IContextActivation;
import org.eclipse.ui.contexts.IContextService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.eclipse.ui.services.ISourceProviderService;

import xstampp.Activator;
import xstampp.model.ObserverValue;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.STPAEditorInput;
import xstampp.ui.menu.file.commands.CommandState;
import xstampp.util.STPAPluginUtils;

/**
 * This class uses the extension point astpa.extensions.steppedEditor to create
 * the navigation depending on the files in the workspace and on the definitions
 * given for the extension
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 */
public final class ProjectExplorer extends ViewPart implements IMenuListener,
		Observer, ISelectionProvider {
	
	/**
	 * The ID of this view
	 */
	public static final String ID = "astpa.explorer"; //$NON-NLS-1$
	private static final String MENU_ID = "openWith.menu";
	private static final String EXTENSION = "extension";
	private static final String PATH_SEPERATOR="->";
	private static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
	private Map<UUID, TreeItem> treeItemsToProjectIDs;
	private Map<TreeItem, String> selectionIdsToTreeItems;
	private Map<TreeItem, IConfigurationElement> perspectiveElementsToTreeItems;
	private Map<String, IProjectSelection> selectorsToSelectionId;
	private Map<String, List<IConfigurationElement>> stepEditorsToStepId;
	private Map<String, List<IConfigurationElement>> stepPerspectivesToStepId; 
	private List<ISelectionChangedListener> selectionListener;
	private Tree tree;
	private Listener listener;
	private TreeViewer treeViewer;
	private MenuManager contextMenu;
	private IMenuManager openWithMenu;
	private Font defaultFont;
	private Listener expandListener;
	private GC gc;
	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(null);
		this.selectionIdsToTreeItems = new HashMap<>();
		this.selectorsToSelectionId = new HashMap<>();
		this.selectionListener = new ArrayList<>();
		this.treeItemsToProjectIDs = new HashMap<>();
		this.perspectiveElementsToTreeItems = new HashMap<>();
		this.expandListener = new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				if(event.item instanceof TreeItem && ((TreeItem)event.item).getItemCount() > 0){
					for(TreeItem item: ((TreeItem)event.item).getItems()){
						item.setExpanded(true);
						if(item.getItemCount() > 0){
							for(TreeItem step: item.getItems()){
								step.setExpanded(true);
							}
						}
					}
				}
			}
		};
		
		Composite container = new Composite(parent, SWT.None);
		container.setBackground(null);
		container.setLayout(new FillLayout());
		this.contextMenu = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		this.contextMenu.addMenuListener(this);
		this.contextMenu.setRemoveAllWhenShown(true);
		this.listener= new Listener() {
			

			@Override
			public void handleEvent(Event event) {
				for (ISelectionChangedListener listenerObj : ProjectExplorer.this.selectionListener) {
					listenerObj.selectionChanged(new SelectionChangedEvent(
							ProjectExplorer.this, ProjectExplorer.this
									.getSelection()));
				}
				if(event !=null && (event.detail == SWT.CR || event.detail ==SWT.MouseDoubleClick)){
					STPAPluginUtils.executeCommand("astpa.command.openStep");
				}
			}
		};
		initializeTree(container);
		this.getSite().registerContextMenu(this.contextMenu, this.treeViewer);
		this.openWithMenu = new MenuManager("Open With..",MENU_ID);
		this.getSite().setSelectionProvider(this);
		
		
		gc = new GC(Display.getDefault());
		this.tree.addListener(SWT.MeasureItem, new Listener() {
		     public void handleEvent(Event event) {
		    	 int size =gc.getFontMetrics().getHeight();
		    	 if(event.height != size){
		    		 event.height = size;
		    	 }
		        
		      }
		});
		this.searchExtensions();

		this.updateProjects();
		
		// Enable the save entries in the menu
		ISourceProviderService sourceProviderService = (ISourceProviderService) PlatformUI
				.getWorkbench().getService(ISourceProviderService.class);
		CommandState saveStateService = (CommandState) sourceProviderService
				.getSourceProvider(CommandState.SAVE_STATE);
		addSelectionChangedListener(saveStateService);
	}
	
	private void initializeTree(Composite container){
		this.tree = new Tree(container, SWT.NONE);
		this.treeViewer = new TreeViewer(this.tree, SWT.FULL_SELECTION);
		if(contextMenu.getMenu() == null){
			this.contextMenu.createContextMenu(this.tree);
		}
		this.tree.setMenu(contextMenu.getMenu());
		this.tree.addFocusListener(new FocusListener() {
			private IContextActivation activation;
			@Override
			public void focusLost(FocusEvent e) {
				  IContextService contextService=(IContextService)getSite().getService(IContextService.class);
				  if(this.activation != null){
					  contextService.deactivateContext(this.activation);
				  }
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				  IContextService contextService=(IContextService)getSite().getService(IContextService.class);
				  this.activation=  contextService.activateContext("xstampp.navigation.context"); //$NON-NLS-1$
			}
		});
		this.tree.addListener(SWT.Expand, expandListener);
		this.tree.addListener(SWT.MouseDown, this.listener);
		this.tree.addListener(SWT.KeyDown, this.listener);
		this.tree.addListener(SWT.MouseDoubleClick, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				STPAPluginUtils.executeCommand("astpa.command.openStep");
			}
		});
		
		this.tree.addSelectionListener(new SelectionListener() {
			
			@Override
			public void widgetSelected(SelectionEvent e) {
				ProjectExplorer.this.listener.handleEvent(null);
				ProjectExplorer.this.getSite().setSelectionProvider(ProjectExplorer.this);

				ProjectExplorer.this.getSite().
						registerContextMenu(ProjectExplorer.this.contextMenu,
										ProjectExplorer.this.treeViewer);
			}
			
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				//not used by this implementation
			}
		});
	}
	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void buildTree(final UUID projectID,String pluginID) {
		IConfigurationElement projectExt = ProjectManager.getContainerInstance().
													getConfigurationFor(projectID);
		/**
		 * The selection id identifies each step in the current runtime
		 */
		String selectionId = projectID.toString();
		String projectName=projectExt.getAttribute("name") 
							+ PATH_SEPERATOR 
							+ ProjectManager.getContainerInstance().getTitle(projectID);
		final TreeItem projectItem = new TreeItem(this.tree, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL);
		FontData fData = PreferenceConverter.getFontData(store, IPreferenceConstants.DEFAULT_FONT);
		if(fData != null){
			defaultFont = new Font(null, fData);
		}else{
			defaultFont = new Font(null, projectItem.getFont().getFontData());
		}
		gc.setFont(defaultFont);
		this.tree.setFont(defaultFont);
		projectItem.setFont(defaultFont);
		IProjectSelection selector = new ProjectSelector(projectItem, projectID,null);
		this.addOrReplaceItem(selectionId, selector, projectItem);
		projectItem.addListener(SWT.MouseDown, this.listener);
		projectItem.setData(ProjectExplorer.EXTENSION, projectExt);
		projectItem.setText(ProjectManager.getContainerInstance().getTitle(
				projectID) + " ["+ ProjectManager.getContainerInstance().getProjectExtension(projectID) +"]");
		ImageDescriptor imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin(pluginID, projectExt
				.getAttribute("icon")); //$NON-NLS-1$
		projectItem.setImage(imgDesc.createImage());
		selector.setPathHistory(projectName);
		this.treeItemsToProjectIDs.put(projectID, projectItem);
		//this two for-loops construct the process tree which consists out of 
		//steps grouped by categorys, each step or category is represented by a treeItem 
		//which is accosiated with a Step/CategorySelector which handles the linking to the step editor
		for (IConfigurationElement categoryExt : projectExt.getChildren()) {
			addTreeItem(categoryExt,selector,projectName,projectID, pluginID);
		}
	}

	
	private void addTreeItem(IConfigurationElement element,
			IProjectSelection parent, String pathName, UUID projectID, String pluginID) {
		String name = element.getName();
		String selectionId = element.getAttribute("id") + projectID.toString();
		String navigationPath=pathName + PATH_SEPERATOR + element.getAttribute("name");
		final TreeItem subItem = new TreeItem(parent.getItem(), SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL);
		subItem.addListener(SWT.Selection, expandListener);
		subItem.setFont(defaultFont);
		subItem.addListener(SWT.Expand, new Listener() {
			
			@Override
			public void handleEvent(Event event) {
				// TODO Auto-generated method stub
				
			}
		});
		subItem.setText(element.getAttribute("name"));//$NON-NLS-1$
		subItem.addListener(SWT.SELECTED, this.listener);
		this.addImage(subItem, element.getAttribute("icon"), element.getNamespaceIdentifier());//$NON-NLS-1$
		IProjectSelection selector;
		
		if(name.equals("step") || name.equals("stepEditor")){
			selector = new StepSelector(subItem, parent, projectID,
					element.getAttribute("editorId"), //$NON-NLS-1$
					element.getAttribute("name")); //$NON-NLS-1$
			//register all additional stepEditors defined as xstampp.extension.stepEditor extension in any of the plugins
			if(this.stepEditorsToStepId.containsKey(element.getAttribute("id"))){
				for(IConfigurationElement e : this.stepEditorsToStepId.get(element.getAttribute("id"))){
					((StepSelector)selector).addStepEditor(e.getAttribute("editorId"), e.getAttribute("name"));
				}
			}
			if(this.stepPerspectivesToStepId.containsKey(element.getAttribute("id"))){
				TreeItem perspectiveItem;
				for(IConfigurationElement perspConf : this.stepPerspectivesToStepId.get(element.getAttribute("id"))){
					
					String viewID = perspConf.getChildren("view")[0].getAttribute("id");
					
					Image img = PlatformUI.getWorkbench().getViewRegistry().find(viewID).getImageDescriptor().createImage();
					perspectiveItem = new TreeItem(subItem, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
					perspectiveItem.setFont(defaultFont);
					this.perspectiveElementsToTreeItems.put(perspectiveItem,perspConf);
					perspectiveItem.setText(perspConf.getAttribute("name"));
					this.selectionIdsToTreeItems.put(perspectiveItem, selectionId);
					perspectiveItem.setImage(img);
				}
			}
			if(this.stepEditorsToStepId.containsKey(element.getAttribute("id"))){
				for(IConfigurationElement subEditorConf : this.stepEditorsToStepId.get(element.getAttribute("id"))){
					addTreeItem(subEditorConf,selector,navigationPath,projectID, pluginID);
				}
			}

			selector.setPathHistory(navigationPath);
			this.contextMenu.addMenuListener((IMenuListener) selector);
			this.addOrReplaceItem(selectionId, selector, subItem);
			
		}else{
			selector = new CategorySelector(subItem, projectID,parent);
			selector.setPathHistory(navigationPath);
			this.addOrReplaceItem(selectionId, selector, subItem);
			for (IConfigurationElement childExt : element.getChildren()) {
				addTreeItem(childExt,selector,navigationPath,projectID, pluginID);
			}
			
		}
		selector.setSelectionListener(listener);
		parent.addChild(selector);
	}

	/**
	 * This method deals with the two maps which are used to define a selection,</br>
	 * <table border="1">
	 * <tr>
	 * 		<th>treeItemToStepId -</th>
	 * 		<th>every treeitem gets an id for the project,step or category</th>
	 * </tr>
	 * <tr>
	 * 		<th>selectionIdToSelector -</th>
	 * 		<th>every selectionId is than mapped to a Selector item which defines the selection</th>
	 * </tr>
	 * </table>
	 *   
	 *
	 * @author Lukas Balzer
	 *
	 * @param selectionId the id 
	 * @param selector
	 * @param item
	 */
	private void addOrReplaceItem(String selectionId,
			IProjectSelection selector, TreeItem item) {
		if (this.selectorsToSelectionId.containsKey(selectionId)) {
			this.selectionIdsToTreeItems.remove(this.selectorsToSelectionId.get(selectionId).getItem());
			this.selectionIdsToTreeItems.put(item, selectionId);
			this.selectorsToSelectionId.get(selectionId).changeItem(item);
			
			
		} else {
			this.selectionIdsToTreeItems.put(item, selectionId);
			this.selectorsToSelectionId.put(selectionId, selector);
		}
	}

	private void addImage(TreeItem item, String path, String pluginId) {
		ImageDescriptor imgDesc;
		if ((path == null) || path.equals("")) {
			item.setImage(PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE));
		} else {
			imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, path);
			item.setImage(imgDesc.createImage());
		}
	}

	/**
	 * looks up all registered projects in viewController and refreshes the tree
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public void updateProjects() {
		for (TreeItem item : this.tree.getItems()) {
			item.dispose();
		}
		this.tree.clearAll(true);
		this.treeViewer.refresh();
		Set<UUID> oldProjects=this.treeItemsToProjectIDs.keySet();
		this.treeItemsToProjectIDs= new HashMap<>();
		for (UUID id : ProjectManager.getContainerInstance().getProjectKeys()) {
			
			this.updateProject(id);
			oldProjects.remove(id);
		}
		for(UUID unusedId:oldProjects){
			for(String idEntry: this.selectorsToSelectionId.keySet()){
				if(idEntry.contains(unusedId.toString())){
					this.selectorsToSelectionId.get(idEntry).cleanUp();
				}
			}
		}
		
	}

	/**
	 *
	 * @author Lukas Balzer
	 *
	 * @param expand 
	 * 			true if the tree should expand it's children false if 
	 * 			he should either collapse or do nothing
	 * 
	 */
	public void expandTree(boolean expand) {
		for (TreeItem item : this.tree.getItems()) {
			item.setExpanded(expand);
		}
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectID
	 *            the id of the project
	 */
	public void updateProject(UUID projectID) {
		if(projectID == null){
			updateProjects();
		}else if(ProjectManager.getContainerInstance().getProjectKeys().contains(projectID)){
			ProjectManager.getContainerInstance().getDataModel(projectID).addObserver(this);
			String plugin = ProjectManager.getContainerInstance().getDataModel(projectID).getPluginID();
			this.buildTree(projectID,plugin);
		}else if(this.treeItemsToProjectIDs.containsKey(projectID)){
			this.treeItemsToProjectIDs.get(projectID).dispose();
			for(String idEntry: this.selectorsToSelectionId.keySet()){
				if(idEntry.contains(projectID.toString())){
					this.selectorsToSelectionId.get(idEntry).cleanUp();
				}
			}
		}
		
	}

	@Override
	public void setFocus() {
		this.treeViewer.getControl().setFocus();
		this.getSite().registerContextMenu(this.contextMenu, this.treeViewer);
		this.getSite().setSelectionProvider(this);
	}

	@Override
	public void menuAboutToShow(IMenuManager manager) {
		this.setFocus();
		IContributionItem item = manager.find(MENU_ID);
		if(item == null && this.getSelection() instanceof StepSelector){
			this.openWithMenu.removeAll();
			for(STPAEditorInput input : ((StepSelector) this.getSelection()).getInputs()){
				this.openWithMenu.add(input.getActivationAction());
			}
			manager.add( this.openWithMenu);
		}
		for (ISelectionChangedListener listenerObj : ProjectExplorer.this.selectionListener) {
			listenerObj.selectionChanged(new SelectionChangedEvent(
					ProjectExplorer.this, ProjectExplorer.this.getSelection()));
		}
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case DELETE:
		case PROJECT_NAME: {
			this.updateProjects();
			break;
		}
		case CLEAN_UP:{
			for(UUID model: ProjectManager.getContainerInstance().getProjectKeys()){
				ProjectManager.getContainerInstance().getDataModel(model).deleteObserver(this);
			}
		}
		default:
			break;
		}

	}

	/**
	 * searches the registered plugins for any extensions of the type
	 * <code>"astpa.extension.steppedProcess"</code>
	 * and for each maps the declared file extension to the IConfigurationElement
	 * 
	 * @see IConfigurationElement
	 *
	 * @author Lukas Balzer
	 *
	 */
	private void searchExtensions() {
		this.stepEditorsToStepId = new HashMap<>();
		this.stepPerspectivesToStepId = new HashMap<>();
		
		for (IConfigurationElement element : Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("xstampp.extension.stepEditors")) { //$NON-NLS-1$
			String confName = element.getName();
			String stepId= element.getAttribute("parentStep"); //$NON-NLS-1$
			//the if structure handles both the stepEditor (if cases 1 &2) 
			//and the stepPerspective extension (if-cases 3 & 4) 
			//inside the stepEditors point, doing that all extensions  are mapped seperately to the stepIds
			if(confName.equals("stepEditor")){ //$NON-NLS-1$
				if(this.stepEditorsToStepId.containsKey(stepId)){
					this.stepEditorsToStepId.get(stepId).add(element);
				}else{
					List<IConfigurationElement> list = new ArrayList<>();
					list.add(element);
					this.stepEditorsToStepId.put(stepId, list);
				}
			}
			else if(confName.equals("stepPerspective")){ //$NON-NLS-1$
				if(this.stepPerspectivesToStepId.containsKey(stepId)){
					this.stepPerspectivesToStepId.get(stepId).add(element);
				}else{
					List<IConfigurationElement> list = new ArrayList<>();
					list.add(element);
					this.stepPerspectivesToStepId.put(stepId, list);
				}
			}
			
		}
		
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		this.selectionListener.add(arg0);

	}

	@Override
	public ISelection getSelection() {
		if (!this.tree.isDisposed() && (this.tree.getSelectionCount() > 0)
				&& this.selectionIdsToTreeItems.containsKey(this.tree.getSelection()[0])) {
			TreeItem item = this.tree.getSelection()[0];
			String stepId = this.selectionIdsToTreeItems.get(this.tree.getSelection()[0]);
			IProjectSelection selector =this.selectorsToSelectionId.get(stepId);
			selector.activate();
			if(this.perspectiveElementsToTreeItems.containsKey(item)){
				((StepSelector)selector).setOpenWithPerspective(this.perspectiveElementsToTreeItems.get(item));
			}else if (selector instanceof StepSelector){
				((StepSelector)selector).setOpenWithPerspective("xstampp.defaultPerspective");//$NON-NLS-1$
			}
			return this.selectorsToSelectionId.get(stepId);
		}
		return new TreeSelection();
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener selectListener) {
		if (this.selectionListener.contains(selectListener)) {
			this.selectionListener.remove(selectListener);
		}

	}

	@Override
	public void setSelection(ISelection selection) {
		// the selection can not be changed from outside

	}

}
