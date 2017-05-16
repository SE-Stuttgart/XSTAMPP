package xstampp.usermanagement.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URL;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.api.UserManagement;

public class UserSystemLoader {
  
  public IUserSystem loadSystem(File file) {
    
    IUserSystem system = new EmptyUserSystem();
    try (StringWriter writer = new StringWriter();
        FileInputStream inputStream = new FileInputStream(file);) {
     
      URL schemaFile;
      String string = "/xstampp/usermanagement/io/userSystem.xsd"; //$NON-NLS-1$
      schemaFile = UserManagement.class.getResource(string);

      Source xmlFile = new StreamSource(file.toURI().toString());
      SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
      Schema schema = schemaFactory.newSchema(schemaFile);

      Validator validator = schema.newValidator();
      validator.validate(xmlFile);
      System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", //$NON-NLS-1$
          "true"); //$NON-NLS-1$
      JAXBContext context = JAXBContext.newInstance(UserSystem.class);

      Unmarshaller um = context.createUnmarshaller();
      system = ((UserSystem) um.unmarshal(xmlFile));
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    return system;
  }
}
