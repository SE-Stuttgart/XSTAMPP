/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.ui.navigation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Observable;
import java.util.Observer;
import java.util.UUID;

import messages.Messages;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
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
 * This class uses the extension point astpa.extensions.steppedEditor to create the navigation
 * depending on the files in the workspace and on the definitions given for the extension
 * 
 * @author Lukas Balzer
 * @since version 2.0.0
 */
public final class ProjectExplorer extends ViewPart
    implements IMenuListener, Observer, ISelectionProvider {

  /**
   * The ID of this view.
   */
  public static final String ID = "astpa.explorer"; //$NON-NLS-1$
  private static final String MENU_ID = "openWith.menu"; //$NON-NLS-1$
  private static final String EXTENSION = "extension"; //$NON-NLS-1$
  private static final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
  private Map<UUID, TreeItem> treeItemsToProjectIDs;
  private Map<TreeItem, IProjectSelection> selectorsToTreeItems;
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
  private List<DynamicStepSelector> dynamicSelectors;
  private ISelection activeSelection;

  @Override
  public void createPartControl(Composite parent) {
    parent.setBackground(null);
    this.selectorsToTreeItems = new HashMap<>();
    this.selectorsToSelectionId = new HashMap<>();
    this.selectionListener = new ArrayList<>();
    this.dynamicSelectors = new ArrayList<>();
    this.treeItemsToProjectIDs = new HashMap<>();
    this.expandListener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (event.item instanceof TreeItem && ((TreeItem) event.item).getItemCount() > 0) {
          for (TreeItem item : ((TreeItem) event.item).getItems()) {
            item.setExpanded(true);
            if (item.getItemCount() > 0) {
              for (TreeItem step : item.getItems()) {
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
    this.listener = new Listener() {

      @Override
      public void handleEvent(Event event) {
        for (ISelectionChangedListener listenerObj : ProjectExplorer.this.selectionListener) {
          listenerObj.selectionChanged(
              new SelectionChangedEvent(ProjectExplorer.this, ProjectExplorer.this.getSelection()));
        }
        if (event != null && (event.detail == SWT.CR || event.detail == SWT.MouseDoubleClick)) {
          STPAPluginUtils.executeCommand("astpa.command.openStep"); //$NON-NLS-1$
        }
      }
    };
    initializeTree(container);
    this.getSite().registerContextMenu(this.contextMenu, this.treeViewer);
    this.openWithMenu = new MenuManager(Messages.ProjectExplorer_OpenWith, MENU_ID);
    this.getSite().setSelectionProvider(this);

    gc = new GC(Display.getDefault());
    this.tree.addListener(SWT.MeasureItem, new Listener() {
      public void handleEvent(Event event) {
        int size = gc.getFontMetrics().getHeight();
        if (event.height != size) {
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

  private void initializeTree(Composite container) {
    this.tree = new Tree(container, SWT.NONE);
    this.treeViewer = new TreeViewer(this.tree, SWT.FULL_SELECTION);
    if (contextMenu.getMenu() == null) {
      this.contextMenu.createContextMenu(this.tree);
    }
    this.tree.setMenu(contextMenu.getMenu());
    this.tree.addFocusListener(new FocusListener() {
      private IContextActivation activation;

      @Override
      public void focusLost(FocusEvent event) {
        IContextService contextService = (IContextService) getSite()
            .getService(IContextService.class);
        if (this.activation != null) {
          contextService.deactivateContext(this.activation);
        }
      }

      @Override
      public void focusGained(FocusEvent eve) {
        IContextService contextService = (IContextService) getSite()
            .getService(IContextService.class);
        activation = contextService.activateContext("xstampp.navigation.context"); //$NON-NLS-1$
      }
    });
    this.tree.addListener(SWT.Expand, expandListener);
    this.tree.addListener(SWT.MouseDown, this.listener);
    this.tree.addListener(SWT.KeyDown, this.listener);
    this.tree.addListener(SWT.MouseDoubleClick, new Listener() {

      @Override
      public void handleEvent(Event event) {
        if (tree.getSelectionCount() > 0) {
          tree.getSelection()[0].setExpanded(!tree.getSelection()[0].getExpanded());
        }
        STPAPluginUtils.executeCommand("astpa.command.openStep"); //$NON-NLS-1$
      }
    });

    this.tree.addSelectionListener(new SelectionListener() {

      @Override
      public void widgetSelected(SelectionEvent event) {
        if (selectorsToTreeItems.containsKey(event.item)) {
          selectorsToTreeItems.get(event.item).activate();
        }
        ProjectExplorer.this.listener.handleEvent(null);
        ProjectExplorer.this.getSite().setSelectionProvider(ProjectExplorer.this);

        ProjectExplorer.this.getSite().registerContextMenu(ProjectExplorer.this.contextMenu,
            ProjectExplorer.this.treeViewer);
      }

      @Override
      public void widgetDefaultSelected(SelectionEvent event) {
        // not used by this implementation
      }
    });
  }

  /**
   * .
   * 
   * @author Lukas Balzer
   * 
   */
  private void buildTree(final UUID projectId, String pluginId) {

    final TreeItem projectItem = new TreeItem(this.tree, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    FontData fontData = PreferenceConverter.getFontData(store, IPreferenceConstants.DEFAULT_FONT);
    if (fontData != null) {
      defaultFont = new Font(null, fontData);
    } else {
      defaultFont = new Font(null, projectItem.getFont().getFontData());
    }
    gc.setFont(defaultFont);
    this.tree.setFont(defaultFont);
    projectItem.setFont(defaultFont);
    ProjectSelector selector = new ProjectSelector(projectItem,
        new TreeItemDescription(new HeadSelector(), projectId), getSite());

    /**
     * The selection id identifies each step in the current runtime.
     */
    String selectionId = projectId.toString();
    this.addOrReplaceItem(selectionId, selector, projectItem);
    IConfigurationElement projectExt = ProjectManager.getContainerInstance()
        .getConfigurationFor(projectId);
    projectItem.setData(ProjectExplorer.EXTENSION, projectExt);
    String identifier = projectExt.getNamespaceIdentifier();
    selector.setReadOnly(!ProjectManager.getContainerInstance().canWriteOnProject(projectId));
    ImageDescriptor imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin(identifier,
        projectExt.getAttribute("icon")); //$NON-NLS-1$
    projectItem.setImage(imgDesc.createImage());

    this.treeItemsToProjectIDs.put(projectId, projectItem);
    // this two for-loops construct the process tree which consists out of
    // steps grouped by categorys, each step or category is represented by a
    // treeItem
    // which is accosiated with a Step/CategorySelector which handles the
    // linking to the step editor
    for (IConfigurationElement categoryExt : projectExt.getChildren()) {
      addTreeItem(new TreeItemDescription(categoryExt, selector, projectId));
    }
  }

  private void addTreeItem(TreeItemDescription descriptor) {
    if (!descriptor.isVisible()) {
      return;
    }
    String selectionId = descriptor.getId() + descriptor.getProjectId().toString();
    final TreeItem subItem = new TreeItem(descriptor.getParent().getItem(),
        SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
    subItem.addListener(SWT.Selection, expandListener);
    subItem.setFont(defaultFont);

    subItem.setText(descriptor.getName());// $NON-NLS-1$
    subItem.addListener(SWT.SELECTED, this.listener);
    this.addImage(subItem, descriptor.getIcon(), descriptor.getNamespaceIdentifier());// $NON-NLS-1$
    IProjectSelection selector = null;
    List<String> arrayList = new ArrayList<>();
    Collections.addAll(arrayList, descriptor.getCommandAdditions());
    for (IConfigurationElement el : descriptor.getChildren()) {
      if (el.getName().equals("commandAddition")) {
        arrayList.add(el.getAttribute("commandId"));
      }
    }
    descriptor.setCommandAdditions(arrayList.toArray(new String[0]));
    String name = descriptor.getElementName();
    if (name.equals("step") || name.equals("stepEditor")) { //$NON-NLS-1$ //$NON-NLS-2$
      selector = new StepSelector(subItem, descriptor, getSite());
      this.contextMenu.addMenuListener((IMenuListener) selector);
    } else if (name.equals("category")) {
      selector = new CategorySelector(subItem, descriptor, getSite());
      for (IConfigurationElement childExt : descriptor.getChildren()) {
        addTreeItem(new TreeItemDescription(childExt, selector, descriptor.getProjectId()));
      }
    } else if (name.equals("editor_List")) {
      try {
        IDynamicStepsProvider provider = (IDynamicStepsProvider) descriptor.getElement()
            .createExecutableExtension("provider");
        selector = new DynamicStepSelector(subItem, descriptor, provider, getSite());

        this.contextMenu.addMenuListener((IMenuListener) selector);
        dynamicSelectors.add(((DynamicStepSelector) selector));
        createDynamicStep(((DynamicStepSelector) selector));
      } catch (CoreException exc) {
        exc.printStackTrace();
      }
    }
    if (selector != null) {
      this.addOrReplaceItem(selectionId, selector, subItem);
      if (this.stepEditorsToStepId.containsKey(descriptor.getElement().getAttribute("id"))) { // $NON-NLS-1$
        for (IConfigurationElement subEditorConf : this.stepEditorsToStepId
            .get(descriptor.getElement().getAttribute("id"))) {
          addTreeItem(new TreeItemDescription(subEditorConf, selector, descriptor.getProjectId()));
        }
      }
      selector.setPathHistory(descriptor.getName());
      selector.setSelectionListener(listener);
      descriptor.getParent().addChild(selector);
    }
  }

  /**
   * This method deals with the two maps which are used to define a selection,<br> <table
   * border="1"> <tr> <th>treeItemToStepId -</th> <th>every treeitem gets an id for the project,step
   * or category</th> </tr> <tr> <th>selectionIdToSelector -</th> <th>every selectionId is than
   * mapped to a Selector item which defines the selection</th> </tr> </table> .
   * 
   *
   * @author Lukas Balzer
   *
   * @param selectionId
   *          the id
   * @param selector
   *          the selector that is either added or replaced in the selectorsToSelectionId map
   * @param item
   *          the tree item that is either added or replaced in the selectionIdsToTreeItems map
   */
  private void addOrReplaceItem(String selectionId, IProjectSelection selector, TreeItem item) {
    if (this.selectorsToSelectionId.containsKey(selectionId)) {
      this.selectorsToTreeItems.remove(this.selectorsToSelectionId.get(selectionId).getItem());
      this.selectorsToTreeItems.put(item, selector);
      this.selectorsToSelectionId.get(selectionId).changeItem(item);

    } else {
      this.selectorsToTreeItems.put(item, selector);
      this.selectorsToSelectionId.put(selectionId, selector);
    }
  }

  private void addImage(TreeItem item, String path, String pluginId) {
    ImageDescriptor imgDesc;
    if ((path == null) || path.equals("")) { //$NON-NLS-1$
      item.setImage(
          PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE));
    } else {
      imgDesc = AbstractUIPlugin.imageDescriptorFromPlugin(pluginId, path);
      item.setImage(imgDesc.createImage());
    }
  }

  private void updateDynamicSteps() {
    for (int i = dynamicSelectors.size() - 1; i >= 0; i--) {
      if (!createDynamicStep(dynamicSelectors.get(i))) {
        dynamicSelectors.remove(i);
      }
    }
  }

  private boolean createDynamicStep(DynamicStepSelector selector) {
    int i = 0;
    try {
      selector.getItem().clearAll(true);
      for (TreeItem item : selector.getItem().getItems()) {
        remove(item);
      }
      for (IDynamicStepsProvider.DynamicDescriptor descriptor : selector.getProvider()
          .getStepMap(selector.getProjectId())) {
        TreeItemDescription subDesc = new TreeItemDescription(selector, selector.getProjectId());
        subDesc.setProperties(descriptor.getProperties());
        subDesc.setName(descriptor.getName());
        subDesc.setCommandAdditions(selector.getCommandAdditions());
        subDesc.setEditorId(selector.getEditorId());
        subDesc.setNamespaceIdentifier(selector.getPluginId());
        subDesc.setId(selector.getSelectionId() + "" + i++);
        subDesc.setIcon(selector.getIcon());
        addTreeItem(subDesc);
      }
      return true;
    } catch (Exception exc) {
      return false;
    }
  }

  /**
   * looks up all registered projects in viewController and refreshes the tree.
   * 
   * @author Lukas Balzer
   * 
   */
  public void updateProjects() {
    Map<UUID, TreeItem> oldProjects = new HashMap<>(treeItemsToProjectIDs);
    for (UUID id : ProjectManager.getContainerInstance().getProjectKeys()) {
      this.updateProject(id);
      oldProjects.remove(id);
    }
    for (Entry<UUID, TreeItem> unusedItem : oldProjects.entrySet()) {
      this.treeItemsToProjectIDs.remove(unusedItem.getKey());
      remove(unusedItem.getValue());
    }

  }

  private void remove(TreeItem item) {

    IProjectSelection remove = this.selectorsToTreeItems.remove(item);
    if (remove != null) {
      remove.cleanUp();
    }
    if (!item.isDisposed()) {
      for (TreeItem childItem : item.getItems()) {
        remove(childItem);
      }
      for (TreeItem child : item.getItems()) {
        remove(child);
      }
      item.dispose();
    }
  }

  /**
   * .
   * 
   * @author Lukas Balzer
   *
   * @param expand
   *          true if the tree should expand it's children false if he should either collapse or do
   *          nothing
   * 
   */
  public void expandTree(boolean expand) {
    for (TreeItem item : this.tree.getItems()) {
      item.setExpanded(expand);
    }
  }

  /**
   * .
   * 
   * @author Lukas Balzer
   * 
   * @param projectId
   *          the id of the project
   */
  public void updateProject(UUID projectId) {
    if (this.treeItemsToProjectIDs.containsKey(projectId)) {
      updateDynamicSteps();
    } else if (ProjectManager.getContainerInstance().getProjectKeys().contains(projectId)) {
      ProjectManager.getContainerInstance().getDataModel(projectId).addObserver(this);
      String plugin = ProjectManager.getContainerInstance().getDataModel(projectId).getPluginID();
      this.buildTree(projectId, plugin);
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
    if (this.getSelection() instanceof AbstractSelectorWithAdditions) {
      ((AbstractSelectorWithAdditions) getSelection()).addCommandAdditions(manager);
    }
    if (this.getSelection() instanceof StepSelector
        && ((StepSelector) this.getSelection()).getInputs().size() > 1) {
      this.openWithMenu.removeAll();
      for (STPAEditorInput input : ((StepSelector) this.getSelection()).getInputs()) {
        this.openWithMenu.add(input.getActivationAction());
      }

      manager.add(this.openWithMenu);
    }
    manager.add(new Separator());

  }

  @Override
  public void update(Observable dataModelController, Object updatedValue) {
    ObserverValue type = (ObserverValue) updatedValue;
    switch (type) {
    case DELETE:
    case PROJECT_TREE:
    case PROJECT_NAME: {
      this.updateProjects();
      break;
    }
    case CLEAN_UP: {
      for (UUID model : ProjectManager.getContainerInstance().getProjectKeys()) {
        ProjectManager.getContainerInstance().getDataModel(model).deleteObserver(this);
      }
      break;
    }
    case SAVE: {
      IProjectSelection selection = this.selectorsToSelectionId
          .get(ProjectManager.getContainerInstance().getProjectID(dataModelController).toString());
      if (selection != null) {
        ((ProjectSelector) selection).setUnsaved(false);
      }

      break;
    }
    case UNSAVED_CHANGES: {
      IProjectSelection selection = this.selectorsToSelectionId
          .get(ProjectManager.getContainerInstance().getProjectID(dataModelController).toString());
      ((ProjectSelector) selection).setUnsaved(true);
      ((ProjectSelector) selection)
          .setReadOnly(!ProjectManager.getContainerInstance().canWriteOnProject(
              ProjectManager.getContainerInstance().getProjectID(dataModelController)));
      break;
    }
    default:
      break;
    }
  }

  /**
   * searches the registered plugins for any extensions of the type
   * <code>"astpa.extension.steppedProcess"</code> and for each maps the declared file extension to
   * the IConfigurationElement
   * 
   * @see IConfigurationElement
   *
   * @author Lukas Balzer
   *
   */
  private void searchExtensions() {
    this.stepEditorsToStepId = new HashMap<>();
    this.stepPerspectivesToStepId = new HashMap<>();

    for (IConfigurationElement element : Platform.getExtensionRegistry()
        .getConfigurationElementsFor("xstampp.extension.stepEditors")) { //$NON-NLS-1$
      String confName = element.getName();
      String stepId = element.getAttribute("parentStep"); //$NON-NLS-1$
      // the if structure handles both the stepEditor (if cases 1 &2)
      // and the stepPerspective extension (if-cases 3 & 4)
      // inside the stepEditors point, doing that all extensions are
      // mapped
      // seperately to the stepIds
      if (confName.equals("stepEditor")) { //$NON-NLS-1$
        if (this.stepEditorsToStepId.containsKey(stepId)) {
          this.stepEditorsToStepId.get(stepId).add(element);
        } else {
          List<IConfigurationElement> list = new ArrayList<>();
          list.add(element);
          this.stepEditorsToStepId.put(stepId, list);
        }
      } else if (confName.equals("stepPerspective")) { //$NON-NLS-1$
        if (this.stepPerspectivesToStepId.containsKey(stepId)) {
          this.stepPerspectivesToStepId.get(stepId).add(element);
        } else {
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
    if (this.activeSelection != null) {
      return this.activeSelection;
    }
    return new TreeSelection();
  }

  @Override
  public void removeSelectionChangedListener(ISelectionChangedListener selectListener) {
    if (this.selectionListener.contains(selectListener)) {
      this.selectionListener.remove(selectListener);
    }

  }

  @Override
  public void setSelection(ISelection selection) {
    this.activeSelection = selection;
    for (ISelectionChangedListener listenerObj : ProjectExplorer.this.selectionListener) {
      listenerObj.selectionChanged(
          new SelectionChangedEvent(ProjectExplorer.this, ProjectExplorer.this.activeSelection));
    }
  }

  public IProjectSelection getActiveSelection() {
    return AbstractSelector.activeSelection;
  }
}
