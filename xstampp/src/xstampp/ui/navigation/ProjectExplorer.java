package xstampp.ui.navigation;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;
import java.util.UUID;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.plugin.AbstractUIPlugin;

import xstampp.Activator;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ViewContainer;
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

	private static final String EXTENSION = "extension";
	private final static String PATH_SEPERATOR="->";
	private Map<UUID, TreeItem> treeItemsToProjectIDs;
	private Map<TreeItem, String> stepIdsToTreeItems;
	private Map<String, IProjectSelection> selectorsToSelectionId;
	private Tree tree;
	private Listener listener;
	private TreeViewer treeViewer;
	private MenuManager contextMenu;
	private final static String CUSTOM_MENU = "customPart";
	/**
	 * Interface to communicate with the data model.
	 */

	/**
	 * The log4j logger
	 */
	private static final Logger LOGGER = ViewContainer.getLOGGER();
	/**
	 * The ID of this view
	 */
	public static final String ID = "astpa.explorer"; //$NON-NLS-1$
	private Map<String, IConfigurationElement> extensions;

	private List<ISelectionChangedListener> selectionListener;


	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(null);
		this.stepIdsToTreeItems = new HashMap<>();
		this.selectorsToSelectionId = new HashMap<>();
		this.selectionListener = new ArrayList<>();
		this.treeItemsToProjectIDs = new HashMap<>();
		
		Composite container = new Composite(parent, SWT.None);
		container.setBackground(null);
		container.setLayout(new FillLayout());
		this.tree = new Tree(container, SWT.NONE);
		this.treeViewer = new TreeViewer(this.tree, SWT.FULL_SELECTION);
		this.contextMenu = new MenuManager("#PopupMenu"); //$NON-NLS-1$
		this.contextMenu.addMenuListener(this);
		this.contextMenu.setRemoveAllWhenShown(true);
		Menu context = this.contextMenu.createContextMenu(this.tree);
		this.tree.setMenu(context);
		this.getSite().registerContextMenu(this.contextMenu, this.treeViewer);
		this.getSite().setSelectionProvider(this);
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
				
			}
		});

		this.searchExtensions();
		File wsPath = new File(Platform.getInstanceLocation().getURL()
				.getPath());
		if (wsPath.isDirectory()) {
			for (File f : wsPath.listFiles()) {
				if (f.getName().startsWith(".")) { //$NON-NLS-1$
					continue;
				}
				
				String[] fileSegments = f.getName().split("\\."); //$NON-NLS-1$
				if ((fileSegments.length > 1)
						&& this.extensions.containsKey(fileSegments[1])) {
					Map<String, String> values = new HashMap<>();
					values.put("loadRecentProject", f.getAbsolutePath()); //$NON-NLS-1$
					STPAPluginUtils.executeParaCommand("xstampp.command.load", values);//$NON-NLS-1$
				}
			}
		}

		this.updateProjects();

	}
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	private void buildTree(final UUID projectID,String pluginID) {
		IConfigurationElement projectExt = this.extensions.get(ViewContainer.
																getContainerInstance().
																getProjectExtension(projectID));
		String selectionId = projectID.toString();
		String projectName=projectExt.getAttribute("name") 
							+ PATH_SEPERATOR 
							+ ViewContainer.getContainerInstance().getTitle(projectID);
		String categoryName="";
		String stepName="";
		final TreeItem projectItem = new TreeItem(this.tree, SWT.BORDER
				| SWT.MULTI | SWT.V_SCROLL);
		IProjectSelection selector = new ProjectSelector(projectItem, projectID);
		this.addOrReplaceStep(selectionId, selector, projectItem);
		projectItem.addListener(SWT.MouseDown, this.listener);
		projectItem.setData(ProjectExplorer.EXTENSION, projectExt);
		projectItem.setText(ViewContainer.getContainerInstance().getTitle(
				projectID));
		
		ImageDescriptor imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin(pluginID, projectExt
				.getAttribute("icon")); //$NON-NLS-1$
		projectItem.setImage(imgDesc.createImage());
		selector.setPathHistory(projectName);
		this.treeItemsToProjectIDs.put(projectID, projectItem);
	
		for (IConfigurationElement categoryExt : projectExt.getChildren()) {
			categoryName=projectName + PATH_SEPERATOR + categoryExt.getAttribute("name");
			selectionId = categoryExt.getAttribute("id") + projectID.toString();
			TreeItem categoryItem = new TreeItem(projectItem, SWT.BORDER
					| SWT.MULTI | SWT.V_SCROLL);
			selector = new CategorySelector(categoryItem, projectID);
			selector.setPathHistory(categoryName);
			this.addOrReplaceStep(selectionId, selector, categoryItem);
			categoryItem.setText(categoryExt.getAttribute("name"));//$NON-NLS-1$
			categoryItem.addListener(SWT.SELECTED, this.listener);
			this.addImage(categoryItem, categoryExt.getAttribute("icon"), pluginID);//$NON-NLS-1$

			for (IConfigurationElement stepExt : categoryExt.getChildren()) {

				selectionId = stepExt.getAttribute("id") + projectID.toString();
				TreeItem stepItem = new TreeItem(categoryItem, SWT.BORDER
						| SWT.MULTI | SWT.V_SCROLL);
				stepItem.addListener(SWT.SELECTED, this.listener);
				stepItem.setText(stepExt.getAttribute("name"));//$NON-NLS-1$
				this.addImage(stepItem, stepExt.getAttribute("icon"), pluginID);//$NON-NLS-1$
				selector = new StepSelector(stepItem, projectID,
						stepExt.getAttribute("editorId"));
				((StepSelector)selector).setEditorName(stepExt.getAttribute("name")); //$NON-NLS-1$
				selector.setPathHistory(categoryName + PATH_SEPERATOR + stepItem.getText());
				this.addOrReplaceStep(selectionId, selector, stepItem);

			}
		}
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
	private void addOrReplaceStep(String selectionId,
			IProjectSelection selector, TreeItem item) {
		if (this.selectorsToSelectionId.containsKey(selectionId)) {
			this.stepIdsToTreeItems.remove(this.selectorsToSelectionId.get(selectionId).getItem());
			this.stepIdsToTreeItems.put(item, selectionId);
			this.selectorsToSelectionId.get(selectionId).changeItem(item);
			
			
		} else {
			this.stepIdsToTreeItems.put(item, selectionId);
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
		Set<UUID> oldProjects=this.treeItemsToProjectIDs.keySet();
		this.treeItemsToProjectIDs= new HashMap<>();
		for (UUID id : ViewContainer.getContainerInstance().getProjectKeys()) {
			ViewContainer.getContainerInstance().getDataModel(id)
					.addObserver(this);
			String plugin = ViewContainer.getContainerInstance().getDataModel(id).getPluginID();
			this.buildTree(id,plugin);
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
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param projectID
	 *            the id of the project
	 */
	public void updateProject(UUID projectID) {
		String temp = ViewContainer.getContainerInstance().getTitle(projectID);

		if (this.treeItemsToProjectIDs.containsKey(projectID)
				&& !this.treeItemsToProjectIDs.get(projectID).equals(temp)) {
			this.treeItemsToProjectIDs.get(projectID).setText(temp);
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
		for (ISelectionChangedListener listenerObj : ProjectExplorer.this.selectionListener) {
			listenerObj.selectionChanged(new SelectionChangedEvent(
					ProjectExplorer.this, ProjectExplorer.this.getSelection()));
		}
	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case DELETE: {
			this.updateProjects();
			
			break;
		}
		case PROJECT_NAME: {
			this.updateProjects();
			break;
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
		this.extensions = new HashMap<>();
		for (IConfigurationElement extElement : Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("astpa.extension.steppedProcess")) { //$NON-NLS-1$
			String ext = extElement.getAttribute("extension"); //$NON-NLS-1$

			this.extensions.put(ext, extElement);
			ProjectExplorer.LOGGER.debug("registered extension: " + ext); //$NON-NLS-1$
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		this.selectionListener.add(arg0);

	}

	@Override
	public ISelection getSelection() {
		if ((this.tree.getSelectionCount() > 0)
				&& this.stepIdsToTreeItems.containsKey(this.tree.getSelection()[0])) {
			String stepId = this.stepIdsToTreeItems.get(this.tree.getSelection()[0]);
			this.selectorsToSelectionId.get(stepId).activate();
			return this.selectorsToSelectionId.get(stepId);
		}
		return new TreeSelection();
	}

	@Override
	public void removeSelectionChangedListener(
			ISelectionChangedListener listener) {
		if (this.selectionListener.contains(listener)) {
			this.selectionListener.remove(listener);
		}

	}

	@Override
	public void setSelection(ISelection selection) {
		// the selection can not be changed from outside

	}

	
}
