package xstampp.usermanagement.api;

import java.util.List;

import xstampp.usermanagement.ui.settings.SyncShell;

public abstract class CollaborationSystem implements ICollaborationSystem {

  @Override
  public int syncDataWithUser(IUser user) {
    SyncShell syncShell = new SyncShell(user, this);
    syncShell.open();
    return (int) syncShell.getReturnValue();
  }

  @Override
  public boolean syncDataWithUser(List<IUser> users) {
    SyncShell syncShell = new SyncShell(users, this);
    syncShell.open();
    return (boolean) syncShell.getReturnValue();
  }
}
