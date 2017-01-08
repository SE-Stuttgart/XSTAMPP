/*******************************************************************************
 * Copyright (c) 2013, 2016 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
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

package xstampp;

import org.eclipse.equinox.p2.core.IProvisioningAgent;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IPerspectiveRegistry;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.ActionBarAdvisor;
import org.eclipse.ui.application.IActionBarConfigurer;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;
import org.osgi.framework.ServiceReference;

import messages.Messages;
import xstampp.ui.common.ProjectManager;
import xstampp.util.ChooseWorkLocation;
import xstampp.util.STPAPluginUtils;

/**
 * Configures the workbench window.
 * 
 * @author Patrick Wickenhaeuser
 */
public class ApplicationWorkbenchWindowAdvisor extends WorkbenchWindowAdvisor {

  /**
   * The minimum and the initial size of the window:
   */
  private static final Point MINIMUM_WINDOW_SIZE = new Point(1024, 768);

  /**
   * Constructor of the advisor.
   * 
   * @author Patrick Wickenhaeuser
   * 
   * @param configurer
   *          the configurer used to configure the workbench.
   */
  public ApplicationWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) {
    super(configurer);
  }

  @Override
  public ActionBarAdvisor createActionBarAdvisor(IActionBarConfigurer configurer) {
    return new ApplicationActionBarAdvisor(configurer);
  }

  @Override
  public void postWindowOpen() {
    ChooseWorkLocation.initiateWorkspace();
  }

  @Override
  public void preWindowOpen() {
    IWorkbenchWindowConfigurer configurer = this.getWindowConfigurer();
    configurer.setInitialSize(ApplicationWorkbenchWindowAdvisor.MINIMUM_WINDOW_SIZE);
    configurer.setShowCoolBar(true);
    configurer.setShowPerspectiveBar(false);
    configurer.setShellStyle(SWT.APPLICATION_MODAL);
    configurer.getActionBarConfigurer().getCoolBarManager().setLockLayout(false);
    configurer.setShowStatusLine(true);
    configurer.setShowProgressIndicator(true);
    configurer.setTitle(Messages.PlatformName);
    // configurer.setShowPerspectiveBar(true);

  }


  @Override
  public void postWindowCreate() {
    super.postWindowCreate();
    final Shell shell = this.getWindowConfigurer().getWindow().getShell();
    shell.setMinimumSize(ApplicationWorkbenchWindowAdvisor.MINIMUM_WINDOW_SIZE);
    //to prevent any broken editor references to show up, this command makes sure
    // xstampp always starts up without any open editors
    PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
    ServiceReference<?> reference = Activator.getContext()
                                              .getServiceReference(IProvisioningAgent.SERVICE_NAME);
    Activator.getContext().ungetService(reference);
  }

  @Override
  public boolean isDurableFolder(String perspectiveId, String folderId) {
    return true;
  }

  @Override
  public boolean preWindowShellClose() {
    IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
    if (page.findView("A-CAST.view1") != null) { //$NON-NLS-1$
      page.hideView(page.findView("A-CAST.view1")); //$NON-NLS-1$
    }
    IPerspectiveRegistry registry = PlatformUI.getWorkbench().getPerspectiveRegistry();
    for (IPerspectiveDescriptor dec : registry.getPerspectives()) {
      registry.revertPerspective(dec);
    }
    page.resetPerspective();
    // PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeAllEditors(false);
    if (!STPAPluginUtils.getUnfinishedJobs().isEmpty()) {
      MessageDialog.openError(null,
                      Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Title,
                      Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Short
                       + Messages.ApplicationWorkbenchWindowAdvisor_Unfinished_Jobs_Message);
      return false;
    }
    if (ProjectManager.getContainerInstance().getUnsavedChanges()) {
      MessageDialog dialog = new MessageDialog(Display.getCurrent().getActiveShell(),
                                               Messages.PlatformName, null,
          Messages.ThereAreUnsafedChangesDoYouWantToStoreThem, MessageDialog.CONFIRM,
          new String[] { Messages.Store, Messages.Discard, Messages.Abort }, 0);
      int resultNum = dialog.open();
      switch (resultNum) {
        case -1:
          return false;
        case 0:
          return ProjectManager.getContainerInstance().saveAllDataModels();
        case 1:
          return true;
        case 2:
          return false;
        default:
          break;
      }
    }

    return true;
  }
}
