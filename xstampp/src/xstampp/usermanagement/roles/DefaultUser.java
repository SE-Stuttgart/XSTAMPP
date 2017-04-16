package xstampp.usermanagement.roles;

import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;

public class DefaultUser implements IUser{

  @Override
  public UUID getUserId() {
    return null;
  }

  @Override
  public String getUsername() {
    return "default";
  }

  @Override
  public String getPassword() {
    return "";
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

}
