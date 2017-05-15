package xstampp.usermanagement.ui.settings;

import java.util.UUID;

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
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.Messages;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.roles.Admin;

public class UserManagementPage implements ISettingsPage {
  private static final int REFRESH_USERS = 1;
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
      noUserInfo.setText(Messages.UserManagementPage_0);
      noUserInfo.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, true));

      Button generateUserSystemButton = new Button(noUserComp, SWT.PUSH);
      generateUserSystemButton.setText(Messages.UserManagementPage_3);
      generateUserSystemButton.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

      Button loadExistingSystemBtn = new Button(noUserComp, SWT.PUSH);
      loadExistingSystemBtn.setText("Use existing Userdata");
      loadExistingSystemBtn.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false, false));

      // The normal composite that is visible to the user is Created here
      final Composite hasUserComp = new Composite(userPage, SWT.None);
      hasUserComp.setLayout(new GridLayout(2, false));
      hasUserComp.setLayoutData(data);

      final List userList = new List(hasUserComp, SWT.None);
      userList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 4));
      refreshUsers();

      final Button addButton = new Button(hasUserComp, SWT.PUSH);
      String imagePath = "/icons/usermanagement/addUser.png"; //$NON-NLS-1$
      addButton.setToolTipText(Messages.CreateUserShell_CreateUser);
      addButton.setImage(Activator.getImageDescriptor(imagePath).createImage());
      addButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      addButton.setVisible(userSystem.canCreateUser());

      final Button addAdminButton = new Button(hasUserComp, SWT.PUSH);
      imagePath = "/icons/usermanagement/addAdmin.png"; //$NON-NLS-1$
      addAdminButton.setToolTipText(Messages.CreateAdminShell_CreateAdmin);
      addAdminButton.setImage(Activator.getImageDescriptor(imagePath).createImage());
      addAdminButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      addAdminButton.setVisible(userSystem.checkAccess(AccessRights.ADMIN));

      final Button deleteButton = new Button(hasUserComp, SWT.PUSH);
      String deleteImgPath = "/icons/buttons/commontables/remove.png"; //$NON-NLS-1$
      deleteButton.setImage(Activator.getImageDescriptor(deleteImgPath).createImage());
      deleteButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      deleteButton.setVisible(userSystem.canCreateUser());
      deleteButton.setEnabled(false);

      final Button editButton = new Button(hasUserComp, SWT.PUSH);
      String editImgPath = "/icons/buttons/edit.png"; //$NON-NLS-1$
      editButton.setImage(Activator.getImageDescriptor(editImgPath).createImage());
      editButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      editButton.setVisible(userSystem.canCreateUser());
      editButton.setEnabled(false);

      addButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem.createUser();
          userList.notifyListeners(REFRESH_USERS, null);
        }
      });
      
      addAdminButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem.createAdmin();
          userList.notifyListeners(REFRESH_USERS, null);
        }
      });
      
      deleteButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          if (currentSelection != null
              && MessageDialog.openConfirm(Display.getDefault().getActiveShell(),
                  Messages.UserManagementPage_5 + currentSelection.getUsername(),
                  String.format(Messages.UserManagementPage_6, currentSelection.getUsername()))
              && userSystem.deleteUser(currentSelection.getUserId())) {
            deleteButton.setEnabled(false);
            currentSelection = null;
            userList.notifyListeners(REFRESH_USERS, null);
          }
        }
      });
      
      editButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          if (userSystem instanceof UserSystem) {
            ((UserSystem) userSystem).editUser(currentSelection);
          }
        }
      });
      
      userList.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent event) {
          currentSelection = userSystem.getRegistry().get(userList.getSelectionIndex());
          deleteButton.setEnabled(userSystem.canDeleteUser(currentSelection.getUserId()));
          editButton.setEnabled(userSystem.checkAccess(AccessRights.ADMIN));
        }
      });
      
      userList.addListener(REFRESH_USERS, new Listener() {

        @Override
        public void handleEvent(Event event) {
          userList.removeAll();
          for (IUser user : userSystem.getRegistry()) {
            String entry = user.getUsername();
            if (user.getUserId().equals(userSystem.getCurrentUserId())) {
              entry += " [current]"; //$NON-NLS-1$
            }
            if (user instanceof Admin) {
              entry += " [admin]"; //$NON-NLS-1$
            }
            userList.add(entry);

          }
          if (userSystem instanceof UserSystem) {
            noUserComp.setVisible(false);
            hasUserComp.setVisible(true);
            editButton.setVisible(userSystem.canCreateUser());
            addButton.setVisible(userSystem.canCreateUser());
            deleteButton.setVisible(userSystem.canCreateUser());
            addAdminButton.setVisible(userSystem.checkAccess(AccessRights.ADMIN));
          }
        }
      });

      generateUserSystemButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem = ((IUserProject) dataModel).createUserSystem();
          userList.notifyListeners(REFRESH_USERS, null);
        }
      });

      loadExistingSystemBtn.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent ev) {
          userSystem = ((IUserProject) dataModel).loadUserSystem();
          userList.notifyListeners(REFRESH_USERS, null);
        }
      });

      userList.notifyListeners(REFRESH_USERS, null);

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

  @Override
  public boolean isVisible(UUID projectId) {
    return true;
  }

}
