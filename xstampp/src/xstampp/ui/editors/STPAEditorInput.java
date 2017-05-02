/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.ui.editors;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;

import xstampp.Activator;
import xstampp.preferences.IPreferenceConstants;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.navigation.StepSelector;
import xstampp.util.STPAPluginUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The Standard Editor input for this Platform.
 *
 * @author Lukas Balzer
 * @since version 2.0.0
 * @see IEditorInput
 */
public class STPAEditorInput implements IEditorInput {
  private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
  private UUID projectId;
  private String stepEditorId;
  private UUID id;
  private String stepId;
  private String stepName;
  private StepSelector stepItem;
  private List<String> additionalViews;
  private boolean isActive;
  private boolean lock = false;
  private Map<String, String> properties;

  /**
   * The Default editorInput.
   *
   * @author Lukas Balzer
   *
   * @param projectId
   *          the id of the project which is related to this input
   * @param editorId
   *          {@link StepSelector#getDefaultEditorId()}
   *
   * @param refItem
   *          {@link StepSelector}
   */
  public STPAEditorInput(UUID projectId, String editorId, StepSelector refItem) {
    this.stepItem = refItem;
    this.projectId = projectId;
    this.stepEditorId = editorId;
    this.id = UUID.randomUUID();
    this.properties = new HashMap<>();
    this.stepName = ""; //$NON-NLS-1$

  }

  @Override
  public Object getAdapter(Class adapter) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean exists() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public String getName() {
    return null;
  }

  @Override
  public IPersistableElement getPersistable() {
    // no persistence supported at the moment
    return null;
  }

  @Override
  public String getToolTipText() {
    return this.stepName + " - " + ProjectManager.getContainerInstance().getTitle(this.projectId); //$NON-NLS-1$
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @return the id of the project which is related to this input
   */
  public UUID getProjectID() {
    return this.projectId;
  }

  @Override
  public ImageDescriptor getImageDescriptor() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public int hashCode() {
    return (int) this.id.getMostSignificantBits();
  }

  @Override
  public boolean equals(Object arg0) {
    boolean equality = false;

    if (arg0 instanceof STPAEditorInput) {
      return getStepId().equals(((STPAEditorInput) arg0).getStepId());
    }
    return super.equals(arg0);
  }

  /**
   * @return the stepName
   */
  public String getStepName() {
    return this.stepName;
  }

  /**
   * @param stepName
   *          the stepName to set
   */
  public void setStepName(String stepName) {
    this.stepName = stepName;
  }

  /**
   * @param pathHistory
   *          the pathHistory to set
   */
  public void setPathHistory(String pathHistory) {
  }

  /**
   * called when the editor handeled by this input is opened updates the workbench and optionally
   * highlightes the related step in the project Explorer.
   *
   * @author Lukas Balzer
   *
   */
  public void activate() {
    this.stepItem.activate();
    if (this.isActive || this.lock) {
      return;
    }
    this.lock = true;
    this.isActive = true;
    for (int i = 0; this.additionalViews != null && i < this.additionalViews.size(); i++) {
      IViewPart part = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .findView(this.additionalViews.get(i));
      if (!PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .isPartVisible(part)) {
        try {
          if (this.additionalViews.get(i).equals("A-CAST.view1")) { //$NON-NLS-1$

          } else {
            PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
                .showView(this.additionalViews.get(i));
          }
        } catch (PartInitException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }

      }
    }

    this.lock = false;
    if (!this.store.getBoolean(IPreferenceConstants.USE_NAVIGATION_COLORS)) {
      return;
    }
    Color navigationColor = new Color(Display.getCurrent(),
        PreferenceConverter.getColor(this.store, IPreferenceConstants.NAVIGATION_ITEM_SELECTED));
    this.stepItem.getItem().setBackground(navigationColor);
    if (!this.stepItem.getItem().getParentItem().getExpanded()) {

      this.stepItem.getItem().getParentItem().setBackground(ColorConstants.lightGray);
    }
    if (!this.stepItem.getItem().getParentItem().getParentItem().getExpanded()) {
      this.stepItem.getItem().getParentItem().getParentItem().setBackground(ColorConstants.lightGray);
    }
  }

  /**
   * if the step is deactivated this method notifys the platform to reset the path highlighting
   *
   * @author Lukas Balzer
   *
   */
  public void deactivate() {
    if (!this.lock && isActive) {

      this.lock = true;
      this.stepItem.getItem().setBackground(null);

      this.stepItem.getItem().getParentItem().setBackground(null);
      this.stepItem.getItem().getParentItem().getParentItem().setBackground(null);
      this.isActive = false;
      this.lock = false;
    }
  }

  /**
   *
   * @author Lukas Balzer
   *
   * @param item
   *          the related step item
   */
  public void setStepItem(TreeItem item) {
    this.stepItem.changeItem(item);

  }

  public void setPerspective(String id) {
  }

  public void addViews(List<String> view) {
    this.additionalViews = view;
  }

  public IAction getActivationAction() {
    return new ActivationAction();
  }

  /**
   * @return the properties
   */
  public Map<String, String> getProperties() {
    return properties;
  }

  /**
   * @param properties
   *          the properties to set
   */
  public void setProperties(Map<String, String> properties) {
    if (properties != null) {
      this.properties = properties;
    }
  }

  public String getStepId() {
    return stepId;
  }

  public void setStepId(String stepId) {
    this.stepId = stepId;
  }

  private class ActivationAction extends Action {
    public ActivationAction() {
      setText(getStepName());
    }

    @Override
    public void run() {
      Map<String, String> values = new HashMap<>();
      values.put("xstampp.command.steps.open", STPAEditorInput.this.stepEditorId); //$NON-NLS-1$
      activate();
      STPAPluginUtils.executeParaCommand("astpa.command.openStep", values); //$NON-NLS-1$
    }

  }
}
