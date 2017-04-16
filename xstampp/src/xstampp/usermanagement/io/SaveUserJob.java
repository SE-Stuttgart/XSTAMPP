/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/

package xstampp.usermanagement.io;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import xstampp.ui.common.ProjectManager;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.util.XstamppJob;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Observable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

/**
 * This Job uses Jaxb to store the User database.
 * 
 * @author Lukas Balzer - initial implementation
 *
 */
public class SaveUserJob extends XstamppJob {

  private File file;
  private UserSystem database;

  /**
   * Constructs the job.
   * @param database the {@link IUserSystem} which is stored
   * @param name the name of the project for which this user database is used
   */
  public SaveUserJob(UserSystem database, String name) {
    super("save user data...");
    this.database = database;
    String wsUrl = Platform.getInstanceLocation().getURL().getPath();
    this.file = new File(wsUrl, name + ".user");
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    monitor.beginTask("save user data...", IProgressMonitor.UNKNOWN);
    JAXBContext context;
    try {

      file.createNewFile();
      context = JAXBContext.newInstance(UserSystem.class);
      Marshaller marshaller = context.createMarshaller();

      marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
      // set the Jaxb encoding format to set a more powerful encoding mechanism
      // than the
      // Standard UTF-8
      marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

      FileOutputStream writer = new FileOutputStream(file);
      marshaller.marshal(database, writer);
      writer.close();
    } catch (Exception exc) {
      exc.printStackTrace();
      setError(exc);
      ProjectManager.getLOGGER().error(exc.getMessage(), exc);
      return Status.CANCEL_STATUS;
    }

    return Status.OK_STATUS;
  }

  @Override
  protected Observable getModelObserver() {
    // TODO Auto-generated method stub
    return null;
  }

}
