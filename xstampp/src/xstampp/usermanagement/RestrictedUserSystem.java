package xstampp.usermanagement;

import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.EmptyUserSystem;
import xstampp.usermanagement.api.IUserSystem;

public class RestrictedUserSystem extends EmptyUserSystem implements IUserSystem {

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public boolean isResponsible(UUID userId, UUID entryId) {
    return false;
  }

  @Override
  public boolean isResponsible(UUID entryId) {
    return false;
  }

}
