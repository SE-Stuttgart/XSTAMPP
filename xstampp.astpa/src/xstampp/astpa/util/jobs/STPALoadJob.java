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
package xstampp.astpa.util.jobs;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.URL;
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
import org.xml.sax.SAXParseException;

import messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
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
      URL schemaFile;
      schemaFile = getClass().getResource("/hazschema.xsd"); //$NON-NLS-1$

      IOUtils.copy(inputStream, writer, "UTF-8");
      String line = writer.toString();
      // gt and lt are replaced by null chars for the time of unescaping
      line = line.replace("&#xA;", "\n"); //$NON-NLS-1$ //$NON-NLS-2$
      Reader stream = new StringReader(line);
      Source xmlFile = new StreamSource(stream, getFile().toURI().toString());
      SchemaFactory schemaFactory = SchemaFactory
          .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(schemaFile);

      Validator validator = schema.newValidator();
      try {
        validator.validate(xmlFile);
      } catch (SAXException exc) {
        this.getLog().error(exc.getMessage(), exc);
        addErrorMsg("Parse Error in file " + getFile().getName() + " invalid content");
        if (exc instanceof SAXParseException) {
          SAXParseException parseExc = (SAXParseException) exc;
          StringReader lineReader = new StringReader(line);
          BufferedReader re = new BufferedReader(lineReader);
          for (int i = 0; i < parseExc.getLineNumber() - 1; i++) {
            re.readLine();
          }
          String excLine = re.readLine();
          re.close();
          lineReader.close();
          addErrorMsg(excLine);
        }
        setError(exc);
        return Status.CANCEL_STATUS;
      }
      System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true"); //$NON-NLS-1$ //$NON-NLS-2$
      JAXBContext context = JAXBContext.newInstance(DataModelController.class);

      Unmarshaller um = context.createUnmarshaller();
      stream = new StringReader(line);
      xmlFile = new StreamSource(stream, getFile().toURI().toString());
      this.setController((IDataModel) um.unmarshal(xmlFile));
      ProjectManager.getLOGGER().debug("Loading of " + getFile().getName() + " successful");
      System.getProperties().remove("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize"); //$NON-NLS-1$

    } catch (SAXException e) {
      this.getLog().error(e.getMessage(), e);
      addErrorMsg(String.format(Messages.InvalidSchemaFile, "hazschema.xsd"));
      setError(e);
      return Status.CANCEL_STATUS;
    } catch (IOException e) {
      this.getLog().error(e.getMessage(), e);
      setError(e);
      return Status.CANCEL_STATUS;
    } catch (JAXBException e) {
      e.printStackTrace();
      addErrorMsg(String.format(Messages.ThisHazFileIsInvalid, getFile().getName()));
      setError(e);
      return Status.CANCEL_STATUS;

    }
    return Status.OK_STATUS;
  }

  @Override
  protected Observable getModelObserver() {
    return null;
  }
}
