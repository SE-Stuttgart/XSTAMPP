package xstampp.usermanagement.ui;

import java.util.Observable;
import java.util.Observer;
import java.util.UUID;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;

import xstampp.Activator;
import xstampp.model.IDataModel;
import xstampp.ui.common.ModalShell;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.projectSettings.ISettingsPage;
import xstampp.usermanagement.UserSystem;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;
import xstampp.usermanagement.roles.Admin;

public class UserManagementPage implements ISettingsPage, Observer {

  private IUserSystem userSystem;
  private IUser currentSelection;
  private Listener observeListener;

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(modelId);
    Composite comp = null;

    if (dataModel instanceof IUserProject) {
      userSystem = ((IUserProject) dataModel).getUserSystem();
      if (userSystem instanceof EmptyUserSystem
          && MessageDialog.openQuestion(Display.getDefault().getActiveShell(), "Create User System",
              "This project doesn't have" + "a user management at the moment,\n"
                  + "Do you want to create one and sign in as administrator?")) {
        userSystem = ((IUserProject) dataModel).createUserSystem();
      }
      if (userSystem instanceof UserSystem) {
        ((UserSystem) userSystem).addObserver(this);
      }
      comp = new Composite(control, SWT.None);
      comp.setLayout(new GridLayout(2, false));

      final List userList = new List(comp, SWT.None);
      userList.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 2));
      this.observeListener = new Listener() {

        @Override
        public void handleEvent(Event event) {

        }
      };
      refreshUsers();

      Button addButton = new Button(comp, SWT.PUSH);
      addButton.setImage(
          Activator.getImageDescriptor("/icons/buttons/commontables/add.png").createImage());
      addButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      addButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          userSystem.createUser();
          userList.notifyListeners(1, null);
        }
      });
      addButton.setEnabled(userSystem.canCreateUser());

      final Button deleteButton = new Button(comp, SWT.PUSH);
      deleteButton.setImage(
          Activator.getImageDescriptor("/icons/buttons/commontables/remove.png").createImage());
      deleteButton.setLayoutData(new GridData(SWT.END, SWT.TOP, false, false));
      deleteButton.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
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

    }
    return comp;
  }

  @Override
  public boolean validate() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean doAccept() {
    // TODO Auto-generated method stub
    return false;
  }

  private void refreshUsers() {
  }

  @Override
  public void update(Observable system, Object arg) {
    this.observeListener.handleEvent(null);
  }

}
