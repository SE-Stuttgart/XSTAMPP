package xstampp.astpa.usermanagement.settings;

import org.eclipse.core.runtime.Assert;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxCellEditor;
import org.eclipse.jface.viewers.EditingSupport;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;

import xstampp.astpa.model.hazacc.ATableModel;
import xstampp.astpa.model.interfaces.IEntryWithNameId;
import xstampp.astpa.usermanagement.Messages;
import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;
import xstampp.usermanagement.api.IUserProject;
import xstampp.usermanagement.api.IUserSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ResponsibilityEditingSupport extends EditingSupport {

  private Map<UUID, IUser> userToResponibilitiesMap;
  private List<String> userList;
  private IUserSystem userSystem;
  private List<Integer> registryIndexList;

  /**
   * 
   * @param viewer
   *          must be a {@link TableViewer}
   * @param project
   */
  public ResponsibilityEditingSupport(ColumnViewer viewer, IUserProject project) {
    super(viewer);
    Assert.isTrue(viewer instanceof TableViewer);
    this.userToResponibilitiesMap = new HashMap<>();
    userSystem = project.getUserSystem();
    List<IUser> registry = userSystem.getRegistry();
    this.userList = new ArrayList<>();
    this.registryIndexList = new ArrayList<>();
    for (int i = 0; i < registry.size(); i++) {
      if (registry.get(i).checkAccess(AccessRights.ACCESS)) {
        this.userList.add(registry.get(i).getUsername());
        this.registryIndexList.add(i);
      }
    }

  }

  @Override
  protected CellEditor getCellEditor(Object element) {
    return new ComboBoxCellEditor(((TableViewer) getViewer()).getTable(),
        userList.toArray(new String[0]), SWT.BORDER | SWT.READ_ONLY);
  }

  @Override
  protected boolean canEdit(Object element) {
    return userSystem.checkAccess(AccessRights.ADMIN);
  }

  @Override
  protected Object getValue(Object element) {
    return userList.indexOf(getStringValue(element));
  }

  protected String getStringValue(Object element) {
    if (userToResponibilitiesMap.containsKey(((ATableModel) element).getId())) {
      return userToResponibilitiesMap.get(((ATableModel) element).getId()).getUsername();
    } else {
      for (IUser user : this.userSystem.getRegistry()) {
        if (this.userSystem.isResponsible(user.getUserId(), ((ATableModel) element).getId())) {
          userToResponibilitiesMap.put(((ATableModel) element).getId(), user);
          return user.getUsername();
        }
      }
    }
    return Messages.ResponsibilityEditingSupport_0;
  }

  @Override
  protected void setValue(Object element, Object value) {
    try {
      Integer registryIndex = this.registryIndexList.get((int) value);
      IUser user = userSystem.getRegistry().get(registryIndex);
      userToResponibilitiesMap.put(((IEntryWithNameId) element).getId(), user);
      getViewer().refresh(true);
    } catch (Exception exc) {
      // ignore the call in case of an illegal argument
    }
  }

  public Map<UUID, IUser> save() {
    return userToResponibilitiesMap;
  }
}
