package acast.jobs;

import java.io.InputStream;

import org.xml.sax.SAXException;

import acast.Activator;
import acast.controller.Controller;
import xstampp.astpa.util.jobs.STPALoadJob;

public class CASTLoadJob extends STPALoadJob {

  public CASTLoadJob() {
    super();
  }

  @Override
  protected InputStream[] getSchema() throws SAXException {
    InputStream schemaFile = Activator.class.getResourceAsStream("/accschema.xsd"); //$NON-NLS-1$
    InputStream mainSchemaFile = xstampp.astpa.Activator.class.getResourceAsStream("/hazschema.xsd"); //$NON-NLS-1$
    return new InputStream[] { mainSchemaFile, schemaFile };
  }

  @Override
  protected Class<?> getLoadModel() {
    return Controller.class;
  }
}
