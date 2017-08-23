/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer- initial API and implementation
 ******************************************************************************/
package xstampp.usermanagement.io;

import java.io.File;

import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchListener;
import org.eclipse.ui.PlatformUI;

import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUserSystem;

public class RefreshRunner extends Thread {
  private boolean terminated;
  private File file;
  private long modifyStamp;
  private UserSystem system;
  private UserSystemLoader loader;

  public RefreshRunner(File file, UserSystem system) {
    setName("userRefresh");
    this.file = file;
    modifyStamp = file.lastModified();
    this.system = system;
    loader = new UserSystemLoader();
    PlatformUI.getWorkbench().addWorkbenchListener(new IWorkbenchListener() {

      @Override
      public boolean preShutdown(IWorkbench workbench, boolean forced) {
        terminated = true;
        return true;
      }

      @Override
      public void postShutdown(IWorkbench workbench) {
        // TODO Auto-generated method stub

      }
    });
  }

  @Override
  public void run() {
    ProjectManager.getLOGGER().debug("Started user system refresh runner");
    while (!terminated) {
      if (file.exists() && file.lastModified() > modifyStamp) {
        IUserSystem update;
        try {
          update = loader.loadSystem(file);
          if (update instanceof UserSystem) {
            ProjectManager.getLOGGER().debug("User System updated");
            system.refresh((UserSystem) update);
          }
          modifyStamp = file.lastModified();
        } catch (Exception e1) {
          ProjectManager.getLOGGER().debug("refresh failure");
          // ignore
        }
        try {
          Thread.sleep(100);
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
    ProjectManager.getLOGGER().debug("Stopped user system refresh runner");
  }
}
