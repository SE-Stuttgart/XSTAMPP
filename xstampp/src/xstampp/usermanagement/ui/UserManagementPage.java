package xstampp.usermanagement.ui;

import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.ui.common.ModalShell;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.roles.Admin;

import java.util.UUID;

public class UserManagementPage implements ISettingsPage {

  private IUserSystem userSystem;
  private IUser currentSelection;

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
    final IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(modelId);
    Composite userPage = new Composite(control, SWT.NONE);
    userPage.setLayout(new FormLayout());
    FormData data = new FormData();
    data.top = new FormAttachment(0);
    data.bottom = new FormAttachment(100);
    data.left = new FormAttachment(0);
    data.right = new FormAttachment(100);

    if (dataModel instanceof IUserProject) {
      userSystem = ((IUserProject) dataModel).getUserSystem();

      // Create composite thats visible in case that no user
      // system has been created yet
      final Composite noUserComp = new Composite(userPage, SWT.None);
      noUserComp.setLayout(new GridLayout());
      noUserComp.setLayoutData(data);

      Label noUserInfo = new Label(noUserComp, SWT.WRAP);
      noUserInfo.setText("This project doesn't have" + "a user management at the moment,\n"
          + "Do you want to create one and sign in as administrator?");
      noUserInfo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

      Button generateUserSystemButton = new Button(noUserComp, SWT.PUSH);
      generateUserSystemButton.setText("Create user database");
      generateUserSystemButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

      // The normal composite that is visible to the user is Created here
      final Composite hasUserComp = new Composite(userPage, SWT.None);
      hasUserComp.setLayout(new GridLayout(2, false));
      hasUserComp.setLayoutData(data);

      final List userList = new List(hasUserComp, SWT.None);
      userList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
      refreshUsers();

      final Button addButton = new Button(hasUserComp, SWT.PUSH);
      addButton.setImage(
          Activator.getImageDescriptor("/icons/buttons/commontables/add.png").createImage());
      addButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      addButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem.createUser();
          userList.notifyListeners(1, null);
        }
      });
      addButton.setEnabled(userSystem.canCreateUser());

      final Button deleteButton = new Button(hasUserComp, SWT.PUSH);
      deleteButton.setImage(
          Activator.getImageDescriptor("/icons/buttons/commontables/remove.png").createImage());
      deleteButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      deleteButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          if (currentSelection != null
              && MessageDialog
                  .openConfirm(Display.getDefault().getActiveShell(),
                      "Delete " + currentSelection.getUsername(),
                      "Do you really want to delete the User entry "
                          + currentSelection.getUsername() + "?")
              && userSystem.deleteUser(currentSelection.getUserId())) {
            deleteButton.setEnabled(false);
            currentSelection = null;
            userList.notifyListeners(1, null);
          }
        }
      });
      deleteButton.setEnabled(false);

      userList.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
          currentSelection = userSystem.getRegistry().get(userList.getSelectionIndex());
          deleteButton.setEnabled(userSystem.canDeleteUser(currentSelection.getUserId()));
        }
      });
      userList.addListener(1, new Listener() {

        @Override
        public void handleEvent(Event event) {
          userList.removeAll();
          for (IUser user : userSystem.getRegistry()) {
            String entry = user.getUsername();
            if (user.getUserId().equals(userSystem.getCurrentUserId())) {
              entry += " [current]";
            }
            if (user instanceof Admin) {
              entry += " [admin]";
            }
            userList.add(entry);
          }
        }
      });

      userList.notifyListeners(1, null);

      generateUserSystemButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem = ((IUserProject) dataModel).createUserSystem();
          if (userSystem instanceof UserSystem) {
            addButton.setEnabled(userSystem.canCreateUser());
            userList.notifyListeners(1, null);
            noUserComp.setVisible(false);
            hasUserComp.setVisible(true);
          }

        }
      });

      boolean hasUsers = userSystem instanceof UserSystem;
      noUserComp.setVisible(!hasUsers);
      hasUserComp.setVisible(hasUsers);
    }
    return userPage;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    return true;
  }

  private void refreshUsers() {
  }

}
