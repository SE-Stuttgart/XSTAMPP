package xstampp.usermanagement.api;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.net.URL;
import java.util.UUID;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import xstampp.usermanagement.RestrictedUserSystem;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.roles.Admin;
import xstampp.usermanagement.ui.CreateAdminShell;

public class UserManagement {
  private static UserManagement instance;

  /**
   * The get instance method of the singleton pattern. On the initial call this method also creates
   * the instance for this runtime.
   * 
   * @return an instance of {@link UserManagement}
   */
  public static UserManagement getInstance() {
    if (instance == null) {
      instance = new UserManagement();
    }
    return instance;
  }

  /**
   * Opens up a shell to create a new administrator for a new user system which initiates with the
   * administrator as initial user and which is stored in a file called
   * 
   * <br>
   * <code>[projectName].user</code>.
   * 
   * @param projectName
   *          the name of the project for which the user database should be created.
   * @return The new {@link IUserSystem} with one user
   */
  public IUserSystem createUserSystem(String projectName) {
    CreateAdminShell create = new CreateAdminShell(new UserSystem());
    IUser admin = create.pullUser();
    IUserSystem system = new EmptyUserSystem();
    if (admin != null && admin instanceof Admin) {
      system = new UserSystem((Admin) admin, projectName);
    }
    return system;
  }

  /**
   * This method tries to load a user database in <code>[project.projectName].user</code>. If the
   * database file cannot be found automatically a {@link FileDialog} is opened to search manually
   * for the file.
   * 
   * @param systemName
   *          the project which is using the user system which should be loaded.
   * @param systemId
   *          the id of the system which should be loaded.
   * @return either the {@link IUserSystem} which was found on the file system and has the same
   *         systemId as the given one or a restricted instance which prevents any access
   *         to the project by the unauthorized user.
   */
  public IUserSystem loadSystem(String systemName, UUID systemId) {
    IUserSystem system = new RestrictedUserSystem();
    String wsUrl = Platform.getInstanceLocation().getURL().getPath();
    String fileName = systemName;
    File file = new File(wsUrl, fileName);
    try (StringWriter writer = new StringWriter();
        FileInputStream inputStream = new FileInputStream(file);) {
      // validate the file
      if (file == null || !file.exists()) {
        FileDialog diag = new FileDialog(Display.getDefault().getActiveShell());
        diag.setFilterPath(wsUrl);
        diag.setText("Please select the User Database or conntact the System administrator");
        diag.setFilterExtensions(new String[] { "*.user" });
        String filePath = diag.open();
        file = new File(filePath);
      }
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
      ((UserSystem) system).setSystemName(file.getName());
      if (system.getSystemId().equals(systemId)) {
        return system;
      }
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    MessageDialog.openError(Display.getDefault().getActiveShell(), "User database error!",
        "The User database for the project " + systemName + " could not "
            + "be read its either broken or corrupt\nplease conntact the system administrator!");
    
    return system;
  }

  /**
   * Opens a {@link FileDialog} and lets the user select an existing {@link IUserSystem} that should
   * be used for this project.
   * 
   * @return The loaded {@link IUserSystem} or an {@link EmptyUserSystem} if the chosen file doesn't
   *         contain a valid system.
   */
  public IUserSystem loadExistingSystem() {
    try {
      // validate the file
      FileDialog diag = new FileDialog(Display.getDefault().getActiveShell());

      String wsUrl = Platform.getInstanceLocation().getURL().getFile();
      diag.setFilterNames(null);
      diag.setFilterPath(wsUrl);
      diag.setText("Please select the User Database which should be used by the project");
      diag.setFilterExtensions(new String[] { "*.user" });
      String filePath = diag.open();
      File file = new File(filePath);
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
      UserSystem system = ((UserSystem) um.unmarshal(xmlFile));
      system.setSystemName(file.getName());
      return system;
    } catch (Exception exc) {
      exc.printStackTrace();
      MessageDialog.openError(Display.getDefault().getActiveShell(), "User database error!",
          "The User database could not "
              + "be read its either broken or corrupt or incompatible with the system!");
    }

    return new EmptyUserSystem();
  }

}
