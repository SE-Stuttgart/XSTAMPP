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
	private Map<UUID, TreeItem> projects;
	private Map<TreeItem, String> treeItemToStepId;
	private Map<String, IProjectSelection> selectionIdToSelector;
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

	@SuppressWarnings("unused")
	private class OpenEditorAction extends Action {

		/**
		 * @see org.eclipse.jface.action.Action#Action(String text,
		 *      ImageDescriptor image)
		 * 
		 */
		public OpenEditorAction(String text, ImageDescriptor image) {
			super(text, image);
		}

	}

	@Override
	public void createPartControl(Composite parent) {
		parent.setBackground(null);
		this.extensions = new HashMap<>();
		this.treeItemToStepId = new HashMap<>();
		this.selectionIdToSelector = new HashMap<>();
		this.selectionListener = new ArrayList<>();
		this.searchExtensions();
		this.projects = new HashMap<>();
		Composite container = new Composite(parent, SWT.None);
		container.setBackground(null);
		container.setLayout(new FillLayout());
		this.tree = new Tree(container, SWT.NONE);
		this.treeViewer = new TreeViewer(this.tree, SWT.FULL_SELECTION);
		this.contextMenu = new MenuManager("#PopupMenu");
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
	private void buildTree(final UUID projectID) {
		IConfigurationElement projectExt = this.extensions.get("haz");
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
		ImageDescriptor imgDesc = Activator.getImageDescriptor(projectExt
				.getAttribute("icon")); //$NON-NLS-1$
		projectItem.setImage(imgDesc.createImage());
		selector.setPathHistory(projectName);
		this.projects.put(projectID, projectItem);
	
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
			this.addImage(categoryItem, categoryExt.getAttribute("icon"));//$NON-NLS-1$

			for (IConfigurationElement stepExt : categoryExt.getChildren()) {

				selectionId = stepExt.getAttribute("id") + projectID.toString();
				TreeItem stepItem = new TreeItem(categoryItem, SWT.BORDER
						| SWT.MULTI | SWT.V_SCROLL);
				stepItem.addListener(SWT.SELECTED, this.listener);
				stepItem.setText(stepExt.getAttribute("name"));//$NON-NLS-1$
				this.addImage(stepItem, stepExt.getAttribute("icon"));//$NON-NLS-1$
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
		if (this.selectionIdToSelector.containsKey(selectionId)) {
			this.treeItemToStepId.remove(this.selectionIdToSelector.get(selectionId).getItem());
			this.treeItemToStepId.put(item, selectionId);
			this.selectionIdToSelector.get(selectionId).changeItem(item);
			
			
		} else {
			this.treeItemToStepId.put(item, selectionId);
			this.selectionIdToSelector.put(selectionId, selector);
		}
	}

	private void addImage(TreeItem item, String path) {
		ImageDescriptor imgDesc;
		if ((path == null) || path.equals("")) {
			item.setImage(PlatformUI.getWorkbench().getSharedImages()
					.getImage(ISharedImages.IMG_OBJ_FILE));
		} else {
			imgDesc = Activator.getImageDescriptor(path);
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
		Set<UUID> oldProjects=this.projects.keySet();
		this.projects= new HashMap<>();
		for (UUID id : ViewContainer.getContainerInstance().getProjectKeys()) {
			ViewContainer.getContainerInstance().getDataModel(id)
					.addObserver(this);
			this.buildTree(id);
			this.updateProject(id);
			oldProjects.remove(id);
		}
		for(UUID unusedId:oldProjects){
			for(String idEntry: this.selectionIdToSelector.keySet()){
				if(idEntry.contains(unusedId.toString())){
					this.selectionIdToSelector.get(idEntry).cleanUp();
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

		if (this.projects.containsKey(projectID)
				&& !this.projects.get(projectID).equals(temp)) {
			this.projects.get(projectID).setText(temp);
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

	private void searchExtensions() {
		this.extensions = new HashMap<>();
		for (IConfigurationElement extElement : Platform
				.getExtensionRegistry()
				.getConfigurationElementsFor("xstampp.extension.steppedProcess")) {
			String ext = extElement.getAttribute("extension");

			this.extensions.put(ext, extElement);
			ProjectExplorer.LOGGER.debug("registered extension: " + ext);
		}
	}

	@Override
	public void addSelectionChangedListener(ISelectionChangedListener arg0) {
		this.selectionListener.add(arg0);

	}

	@Override
	public ISelection getSelection() {
		if ((this.tree.getSelectionCount() > 0)
				&& this.treeItemToStepId.containsKey(this.tree.getSelection()[0])) {
			String stepId = this.treeItemToStepId.get(this.tree.getSelection()[0]);
			this.selectionIdToSelector.get(stepId).activate();
			return this.selectionIdToSelector.get(stepId);
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
