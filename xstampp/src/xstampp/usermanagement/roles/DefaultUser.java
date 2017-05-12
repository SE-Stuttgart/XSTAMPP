package xstampp.usermanagement.roles;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;

import java.util.UUID;

public class DefaultUser implements IUser {

  @Override
  public UUID getUserId() {
    return null;
  }

  @Override
  public String getUsername() {
    return "default";
  }

  @Override
  public boolean setUsername(String password, String username) {
    return false;
  }

  @Override
  public boolean setPassword(String oldPassword, String newPassword) {
    return false;
  }

  @Override
  public boolean checkAccess(UUID entryId, AccessRights accessLevel) {
    return false;
  }

  @Override
  public boolean checkAccess(AccessRights accessRight) {
    return false;
  }

  @Override
  public boolean verifyPassword(String password) {
    return true;
  }

  @Override
  public boolean isResponibleFor(UUID responsibility) {
    //the default user has no responsibilities
    return false;
  }

}
