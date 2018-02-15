/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.stpapriv.util.jobs;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Observable;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.io.IOUtils;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.xml.sax.SAXException;

import messages.Messages;
import xstampp.model.IDataModel;
import xstampp.stpapriv.model.PrivacyController;
import xstampp.ui.common.ProjectManager;
import xstampp.util.AbstractLoadJob;

/**
 *
 * @author Lukas Balzer
 *
 */
public class STPALoadJob extends AbstractLoadJob {

  /**
   *
   * @author Lukas Balzer
   *
   */
  public STPALoadJob() {
    super();
  }

  @Override
  protected IStatus run(IProgressMonitor monitor) {
    setName("Loading " + getFile().getName() + "...");
    monitor.beginTask(getName(), IProgressMonitor.UNKNOWN);
    try (StringWriter writer = new StringWriter();
        FileInputStream inputStream = new FileInputStream(getFile());) {
      // validate the file
      InputStream schemaFile;
      schemaFile = getClass().getResourceAsStream("/secschema.xsd"); //$NON-NLS-1$
      InputStream mainSchemaFile = xstampp.astpa.Activator.class.getResourceAsStream("/hazschema.xsd"); //$NON-NLS-1$

      Source imp = new StreamSource(schemaFile);
      Source main = new StreamSource(mainSchemaFile);
      Source[] schemaFiles = new Source[] { main, imp };
      IOUtils.copy(inputStream, writer, "UTF-8");
      String line = writer.toString();
      Reader stream = new StringReader(line);
      Source xmlFile = new StreamSource(stream, getFile().toURI().toString());
      SchemaFactory schemaFactory = SchemaFactory
          .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(schemaFiles);

      Validator validator = schema.newValidator();
      validator.validate(xmlFile);
      System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true"); //$NON-NLS-1$ //$NON-NLS-2$
      JAXBContext context = JAXBContext.newInstance(PrivacyController.class);

      Unmarshaller um = context.createUnmarshaller();
      stream = new StringReader(line);
      xmlFile = new StreamSource(stream, getFile().toURI().toString());
      this.setController((IDataModel) um.unmarshal(xmlFile));
      ProjectManager.getLOGGER().debug("Loading of " + getFile().getName() + " successful");
      System.getProperties().remove("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize"); //$NON-NLS-1$

    } catch (SAXException e) {
      this.getLog().error(e.getMessage(), e);
      addErrorMsg(String.format(Messages.InvalidSchemaFile, "secschema.xsd"));
      addErrorMsg(e.getMessage());
      return Status.CANCEL_STATUS;
    } catch (IOException e) {
      this.getLog().error(e.getMessage(), e);
      addErrorMsg(e.getMessage());
      return Status.CANCEL_STATUS;
    } catch (JAXBException e) {
      e.printStackTrace();
      addErrorMsg(String.format(Messages.ThisHazFileIsInvalid, getFile().getName()));
      addErrorMsg(e.getMessage());
      return Status.CANCEL_STATUS;

    }
    return Status.OK_STATUS;
  }

  @Override
  protected Observable getModelObserver() {
    return null;
  }
}
