/*******************************************************************************
 * Copyright (c) 2013, 2016 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 *  
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.util;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;

import xstampp.ui.common.ProjectManager;

public class LoadWorkspace extends Job {

  private ArrayList<File> fileList;

  public LoadWorkspace(String name) {
    super(name);
    fileList = new ArrayList<>();
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
    IConfigurationElement[] elements = Platform.getExtensionRegistry()
        .getConfigurationElementsFor("xstampp.extension.steppedProcess");
    for (IConfigurationElement extElement : elements) {
      if(extElement.getName().contains("process")) {
        String[] ext = extElement.getAttribute("extension").split(";"); //$NON-NLS-1$
        String modelClass = extElement.getAttribute("DataModelClass");
        for (int i = 0; i < ext.length; i++) {
          ProjectManager.getContainerInstance().registerExtension(modelClass, ext[i], extElement);
        }
      }
    }

    File wsPath = new File(Platform.getInstanceLocation().getURL().getPath());
    if (wsPath.isDirectory()) {
      for (File f : wsPath.listFiles()) {
        if (f.getName().startsWith(".")) { //$NON-NLS-1$
          continue;
        }

        String[] fileSegments = f.getName().split("\\."); //$NON-NLS-1$
        if ((fileSegments.length > 1) && ProjectManager.getContainerInstance().isRegistered(fileSegments[1])) {
          fileList.add(f);
        }
      }
      Collections.sort(fileList, new Comparator<File>() {

        @Override
        public int compare(File o1, File o2) {
          long sign = o1.length() - o2.length();
          if (sign == 0) {
            return 0;
          }
          return (int) (sign / Math.abs(sign));
        }

      });
      for (File projectFile : fileList) {
        try {
          Object ob = ProjectManager.getContainerInstance().loadDataModelFile(projectFile.getAbsolutePath(),
              projectFile.getAbsolutePath());
          if (ob instanceof AbstractLoadJob) {
            ((AbstractLoadJob) ob).join();
          }
        } catch (InterruptedException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }

    }
    return Status.OK_STATUS;
  }

}
