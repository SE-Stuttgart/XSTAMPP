package acast.jobs;

import java.io.InputStream;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import org.xml.sax.SAXException;

import acast.Activator;
import acast.controller.Controller;
import xstampp.astpa.util.jobs.STPALoadJob;

public class CASTLoadJob extends STPALoadJob {

  public CASTLoadJob() {
    super();
  }

  @Override
  protected Schema getSchema() throws SAXException {
    InputStream schemaFile = Activator.class.getResourceAsStream("/accschema.xsd"); //$NON-NLS-1$
    InputStream mainSchemaFile = xstampp.astpa.Activator.class.getResourceAsStream("/hazschema.xsd"); //$NON-NLS-1$
    Source imp = new StreamSource(schemaFile);
    Source main = new StreamSource(mainSchemaFile);
    Source[] schemaFiles = new Source[] { main, imp };
    SchemaFactory schemaFactory = SchemaFactory
        .newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
    return schemaFactory.newSchema(schemaFiles);
  }

  @Override
  protected Class<?> getLoadModel() {
    return Controller.class;
  }
}
