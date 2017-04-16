package xstampp.usermanagement.api;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;

import xstampp.usermanagement.RestrictedUserSystem;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.roles.Admin;
import xstampp.usermanagement.ui.CreateAdminShell;

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

public class UserManagement {
  /**
   * Opens up a shell to create a new administrator for a new user system which initiates with the
   * administrator as initial user and which is stored in a file called
   * 
   * <p><code>[projectName].user</code>.
   * 
   * @param projectName
   *          the name of the project for which the user database should be created.
   * @return The new {@link IUserSystem} with one user
   */
  public static IUserSystem createUserSystem(String projectName) {
    CreateAdminShell create = new CreateAdminShell(new EmptyUserSystem());
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
   * @param project
   *          the project which is using the user system which should be loaded.
   * @param systemId
   *          the id of the system which should be loaded.
   * @return either the {@link IUserSystem} which was found on the file system and has the same
   *         systemId as the given one or a {@link RestrictedUserSystem} which prevents any access
   *         to the project by the unauthorized user.
   */
  public static IUserSystem loadSystem(final IUserProject project, UUID systemId) {
    String wsUrl = Platform.getInstanceLocation().getURL().getPath();

    File file = new File(wsUrl, project.getProjectName() + ".user");
    try (StringWriter writer = new StringWriter();
        FileInputStream inputStream = new FileInputStream(file);) {
      // validate the file
      if (file == null || !file.exists()) {
        FileDialog diag = new FileDialog(Display.getDefault().getActiveShell());
        diag.setText("Please select the User Database or conntact the System administrator");
        diag.setFilterExtensions(new String[] { "*.user" });
        String filePath = diag.open();
        file = new File(filePath);
      }
      URL schemaFile;
      schemaFile = UserManagement.class.getResource("/userSystem.xsd"); //$NON-NLS-1$

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
      if (system.getSystemId().equals(systemId)) {
        system.setProjectName(file.getName().split("\\.")[0]);
        return system;
      }
    } catch (Exception exc) {
      exc.printStackTrace();
    }
    MessageDialog.openError(Display.getDefault().getActiveShell(), "User database error!",
        "The User database for the project " + project.getProjectName() + " could not"
            + "be read its either broken or corrupt\n please conntact the system administrator!");

    return new RestrictedUserSystem();
  }

}
