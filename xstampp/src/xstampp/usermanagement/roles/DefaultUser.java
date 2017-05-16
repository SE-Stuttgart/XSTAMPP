package xstampp.usermanagement.roles;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import xstampp.usermanagement.api.AccessRights;
import xstampp.usermanagement.api.IUser;

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
    return false;
  }

  @Override
  public UUID getWorkingProjectId() {
    return null;
  }

  @Override
  public List<UUID> getResponsibilities() {
    // TODO Auto-generated method stub
    return new ArrayList<>();
  }

}
