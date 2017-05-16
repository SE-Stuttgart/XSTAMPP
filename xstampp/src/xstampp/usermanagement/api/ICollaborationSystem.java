package xstampp.usermanagement.api;

import java.util.List;

/**
 * 
 * @author Lukas Balzer
 *
 */
public interface ICollaborationSystem {

  /**
   * This method gets the given users' copy of the project calling
   * {@link IUser#getWorkingProjectId()}<br>
   * If one ha been defined for the current project than its' data for which the given user is
   * responsible is merged into the current project.
   * 
   * @param user
   *          The user which should provide the necessary projectId and the responsibilities which
   *          define the entries that are going to be synchronized
   * @return if the given {@link IUser#getWorkingProjectId()} did provide a valid project Id and the
   *         current project data were successfully merged
   */
  boolean syncDataWithUser(IUser user);

  boolean syncDataWithUser(List<IUser> users);
}
